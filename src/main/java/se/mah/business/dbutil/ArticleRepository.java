package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Article;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aliona on 2017-07-21.
 */
@Repository
public class ArticleRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jdbc;

    public List<Article> getArticlesList() {
        return jdbc.query("SELECT article_id, headline, lead_text, article_date FROM article", articleListMapper);
    }

    public Article getArticle(String articleId) {
        //TODO:
        int id = Integer.parseInt(articleId);
//        return jdbc.queryForObject();
//        sql = "SELECT lead_text, headline, body, article_date, sub_cat_id FROM article WHERE article_id = ?";
//        sql = "SELECT first_name, last_name FROM person WHERE person_id = (SELECT person_id FROM written_by WHERE article_id=?)";
//        sql = "SELECT sub_cat_text, main_cat_text FROM category WHERE sub_cat_id=(SELECT sub_cat_id FROM article WHERE article_id = ?)";
//        sql = "SELECT url, alt_text FROM picture WHERE picture_id=(SELECT picture_id FROM article_picture WHERE article_id=?)";
        return null;
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
}
