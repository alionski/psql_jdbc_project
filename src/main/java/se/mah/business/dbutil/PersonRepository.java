package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aliona on 2017-07-21.
 */
@Repository
public class PersonRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jdbc;

    public Person getPerson(String personId) {
        int id = Integer.parseInt(personId);
        return jdbc.queryForObject("SELECT first_name, last_name FROM person WHERE id=?",
                userMapper, id);
    }

    public void addNewAuthor(Person author) {
        String sql = "INSERT INTO person(personal_number, first_name, last_name, note) VALUES (?,?,?,?)";
        jdbc.update(sql, author.getPersonalNumber(), author.getFirstName(), author.getLastName(), author.getNote());
    }

    public List<Person> getEmployees() {
        return jdbc.query("SELECT * FROM person", employeeMapper);
    }

    public void addAuthorsToArticle(List<Person> authors, int articleId) {
        String sql = "INSERT INTO written_by(article_id, person_id) VALUES (?, ?)";
        if (authors.get(1).getPersonId() == -1) {
            authors.remove(1);
        }

        jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Person author = authors.get(i);
                ps.setInt(1, articleId);
                ps.setInt(2, author.getPersonId());
            }

            @Override
            public int getBatchSize() {
                return authors.size();
            }
        });
    }

    public List<Person> getArticleAuthors(String articleId) {
        int id = Integer.parseInt(articleId);
        return jdbc.query("SELECT id, first_name, last_name FROM written_by " +
                "INNER JOIN person ON written_by.person_id = id WHERE written_by.article_id = ?",
                articleAuthorsMapper, id);
    }

    private static final RowMapper<Person> userMapper = new RowMapper<Person>() {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            return person;
        }
    };

    private static final RowMapper<Person> employeeMapper = new RowMapper<Person>() {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setNote(rs.getString("note"));
            person.setPersonId(rs.getInt("id"));
            person.setPersonalNumber(rs.getLong("personal_number"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            return person;
        }
    };

    private static final RowMapper<Person> articleAuthorsMapper = new RowMapper<Person>() {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person author = new Person();
            author.setPersonId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        }
    };
}
