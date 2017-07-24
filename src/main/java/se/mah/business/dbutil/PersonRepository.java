package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Person;
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

    public Person getPerson(String firstName, String lastName) {
        return jdbc.queryForObject("SELECT person_id, first_name, last_name FROM person WHERE first_name=? AND last_name=?",
                userMapper, firstName, lastName);
    }

    public List<Person> getEmployees() {
        return jdbc.query("SELECT * FROM person", employeeMapper);
    }

    private static final RowMapper<Person> userMapper = new RowMapper<Person>() {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setPerson_id(rs.getLong("person_id"));
            person.setFirst_name(rs.getString("first_name"));
            person.setLast_name(rs.getString("last_name"));
            return person;
        }
    };

    private static final RowMapper<Person> employeeMapper = new RowMapper<Person>() {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setNote(rs.getString("note"));
            person.setPerson_id(rs.getLong("person_id"));
            person.setFirst_name(rs.getString("first_name"));
            person.setLast_name(rs.getString("last_name"));
            return person;
        }
    };
}
