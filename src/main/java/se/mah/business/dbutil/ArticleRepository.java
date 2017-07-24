package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Article;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliona on 2017-07-21.
 */
@Repository
public class ArticleRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jdbc;
    protected SimpleJdbcInsert simpleJdbcInsert;

    public List<Article> getArticlesList() {
        return jdbc.query("SELECT article_id, headline, lead_text, article_date FROM article", articleListMapper);
    }

    public List<Article> getArticlesByMainCat(String maincat) {
        return jdbc.query("SELECT article_id, headline, lead_text, article_date FROM article " +
                        "INNER JOIN category ON article.sub_cat_id = category.sub_cat_id WHERE category.main_cat_text = ?",
                articleListMapper, maincat);    }

    public List<Article> getArticlesBySubCat(String subcat) {
        return jdbc.query("SELECT article_id, headline, lead_text, article_date FROM article " +
                        "INNER JOIN category ON article.sub_cat_id = category.sub_cat_id WHERE category.sub_cat_text = ?",
                articleListMapper, subcat);
    }

    public List<Article> getArticlesByAuthor(String authorId) {
        return jdbc.query("SELECT article.article_id, headline, lead_text, article_date FROM article " +
                        "INNER JOIN written_by ON article.article_id = written_by.article_id " +
                        "WHERE written_by.person_id = ?",
                articleListMapper, Long.parseLong(authorId));
    }

    public Article getArticle(String articleId) {
        int id = Integer.parseInt(articleId);
        return jdbc.queryForObject("SELECT article.article_id, lead_text, headline, body, article_date, person.person_id, first_name, last_name, sub_cat_text, main_cat_text, url, alt_text\n" +
                        "FROM article INNER JOIN written_by ON written_by.article_id = article.article_id\n" +
                        "              INNER JOIN person ON written_by.person_id = person.person_id\n" +
                        "              INNER JOIN category ON article.sub_cat_id = category.sub_cat_id\n" +
                        "              INNER JOIN article_picture ON article_picture.article_id = article.article_id\n" +
                        "              INNER JOIN picture ON picture.picture_id = article_picture.picture_id\n" +
                        "WHERE  article.article_id = ?",
                articleMapper, id);
    }

    public int postNewArticle(Article article) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lead_text", article.getLead());
        parameters.put("headline", article.getHeadline());
        parameters.put("body", article.getBody());
        simpleJdbcInsert.withTableName("article").usingGeneratedKeyColumns("article_id");
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    private static final RowMapper<Article> articleListMapper = new RowMapper<Article>() {
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article article = new Article();
            article.setId(rs.getInt("article_id"));
            article.setHeadline(rs.getString("headline"));
            article.setLead(rs.getString("lead_text"));
            article.setDate(rs.getString("article_date"));
            return article;
        }
    };

    private static final RowMapper<Article> articleMapper = new RowMapper<Article>() {
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article article = new Article();
            article.setId(rs.getInt("article_id"));
            article.setLead(rs.getString("lead_text"));
            article.setHeadline(rs.getString("headline"));
            article.setBody(rs.getString("body"));
            article.setDate(rs.getString("article_date"));
            article.setAuthorId(rs.getLong("person_id"));
            article.setAuthorName(rs.getString("first_name"));
            article.setAuthorSurname(rs.getString("last_name"));
            article.setSubcat(rs.getString("sub_cat_text"));
            article.setMaincat(rs.getString("main_cat_text"));
            article.setImage(rs.getString("url"));
            article.setAltimage("alt_text");
            return article;
        }
    };
}

