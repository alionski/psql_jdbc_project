package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Article;
import se.mah.business.entities.Person;
import se.mah.business.entities.Picture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        return jdbc.query(  "SELECT article.article_id, headline, lead_text, article_date, picture.url\n" +
                                "FROM article LEFT OUTER JOIN\n" +
                                "  (SELECT pics_to_articles.artid as unique_art_id,\n" +
                                "          min(pics_to_articles.picid) as first_pic\n" +
                                "    FROM\n" +
                                "        (SELECT aa.article_id AS artid, aa.picture_id AS picid, bb.url AS picurl\n" +
                                "        FROM article_picture AS aa\n" +
                                "        RIGHT OUTER JOIN picture AS bb\n" +
                                "        ON aa.picture_id = bb.picture_id\n" +
                                "        ) pics_to_articles\n" +
                                "    GROUP BY pics_to_articles.artid) unique_pics\n" +
                                "ON article.article_id = unique_pics.unique_art_id " +
                                "LEFT OUTER JOIN picture\n" +
                                "    ON unique_pics.first_pic = picture.picture_id\n" +
                                "ORDER BY article_date DESC", articleListMapper);
    }

    public List<Article> getLatestArticles() {
        return jdbc.query( "SELECT article.article_id, headline, lead_text, article_date, picture.url\n" +
                                "FROM article LEFT OUTER JOIN" +
                                "  (SELECT pics_to_articles.artid as unique_art_id,\n" +
                                "          min(pics_to_articles.picid) as first_pic\n" +
                                "    FROM\n" +
                                "        (SELECT aa.article_id AS artid, aa.picture_id AS picid, bb.url AS picurl\n" +
                                "        FROM article_picture AS aa\n" +
                                "        RIGHT OUTER JOIN picture AS bb\n" +
                                "        ON aa.picture_id = bb.picture_id\n" +
                                "        ) pics_to_articles\n" +
                                "    GROUP BY pics_to_articles.artid) unique_pics\n" +
                                "ON article.article_id = unique_pics.unique_art_id " +
                                "LEFT OUTER JOIN picture\n" +
                                "    ON unique_pics.first_pic = picture.picture_id\n" +
                                "ORDER BY article_date DESC LIMIT 5", articleListMapper);
    }

    public List<Article> getArticlesByMainCat(String maincat) {
        return jdbc.query("SELECT article.article_id, headline, lead_text, article_date, picture.url\n" +
                                "FROM article \n" +
                                "LEFT OUTER JOIN\n" +
                                "  (SELECT pics_to_articles.artid as unique_art_id,\n" +
                                "          min(pics_to_articles.picid) as first_pic\n" +
                                "    FROM\n" +
                                "        (SELECT aa.article_id AS artid, aa.picture_id AS picid, bb.url AS picurl\n" +
                                "        FROM article_picture AS aa\n" +
                                "        RIGHT OUTER JOIN picture AS bb\n" +
                                "        ON aa.picture_id = bb.picture_id\n" +
                                "        ) pics_to_articles\n" +
                                "    GROUP BY pics_to_articles.artid) unique_pics\n" +
                                "ON article.article_id = unique_pics.unique_art_id\n" +
                                "LEFT OUTER JOIN picture\n" +
                                "    ON unique_pics.first_pic = picture.picture_id\n" +
                                "INNER JOIN category \n" +
                                "    ON article.sub_cat_id = category.sub_cat_id \n" +
                                "    WHERE category.main_cat_text = ?" +
                                "ORDER BY article_date DESC",
                articleListMapper, maincat);
    }

    public List<Article> getArticlesBySubCat(String subcat) {
        return jdbc.query("SELECT article.article_id, headline, lead_text, article_date, picture.url\n" +
                                "FROM article \n" +
                                "LEFT OUTER JOIN\n" +
                                "  (SELECT pics_to_articles.artid as unique_art_id,\n" +
                                "          min(pics_to_articles.picid) as first_pic\n" +
                                "    FROM\n" +
                                "        (SELECT aa.article_id AS artid, aa.picture_id AS picid, bb.url AS picurl\n" +
                                "        FROM article_picture AS aa\n" +
                                "        RIGHT OUTER JOIN picture AS bb\n" +
                                "        ON aa.picture_id = bb.picture_id\n" +
                                "        ) pics_to_articles\n" +
                                "    GROUP BY pics_to_articles.artid) unique_pics\n" +
                                "ON article.article_id = unique_pics.unique_art_id\n" +
                                "LEFT OUTER JOIN picture\n" +
                                "    ON unique_pics.first_pic = picture.picture_id\n" +
                                "INNER JOIN category \n" +
                                "    ON article.sub_cat_id = category.sub_cat_id \n" +
                                "    WHERE category.sub_cat_text = ?" +
                                "ORDER BY article_date DESC",
                    articleListMapper, subcat);
    }

    public List<Article> getArticlesByAuthor(String authorId) {
        return jdbc.query("SELECT article.article_id, headline, lead_text, article_date, picture.url\n" +
                                "FROM article\n" +
                                "LEFT OUTER JOIN\n" +
                                "  (SELECT pics_to_articles.artid as unique_art_id,\n" +
                                "          min(pics_to_articles.picid) as first_pic\n" +
                                "    FROM\n" +
                                "        (SELECT aa.article_id AS artid, aa.picture_id AS picid, bb.url AS picurl\n" +
                                "        FROM article_picture AS aa\n" +
                                "        RIGHT OUTER JOIN picture AS bb\n" +
                                "        ON aa.picture_id = bb.picture_id\n" +
                                "        ) pics_to_articles\n" +
                                "    GROUP BY pics_to_articles.artid) unique_pics\n" +
                                "ON article.article_id = unique_pics.unique_art_id\n" +
                                "LEFT OUTER JOIN picture\n" +
                                "    ON unique_pics.first_pic = picture.picture_id\n" +
                                "INNER JOIN written_by\n" +
                                "    ON article.article_id = written_by.article_id\n" +
                                "    WHERE written_by.person_id = ?" +
                                "ORDER BY article_date DESC",
                        articleListMapper, Long.parseLong(authorId));
    }

    public Article getArticle(String articleId) {
        int id = Integer.parseInt(articleId);
        return jdbc.queryForObject("SELECT article_id, lead_text, headline, body, article_date, sub_cat_text, main_cat_text " +
                                        "FROM article " +
                                            "INNER JOIN category " +
                                            "ON article.sub_cat_id = category.sub_cat_id\n" +
                                        "WHERE article_id = ?",
                                    articleMapper, id);
    }

    public int postNewArticle(Article article) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lead_text", article.getLead());
        parameters.put("headline", article.getHeadline());
        parameters.put("body", article.getBody());
        parameters.put("sub_cat_id", article.getSubcatId());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbc.getDataSource());
        simpleJdbcInsert.withTableName("article").usingGeneratedKeyColumns("article_id");
        simpleJdbcInsert.usingColumns("lead_text","headline", "body", "sub_cat_id");
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    private static final RowMapper<Article> articleListMapper = new RowMapper<Article>() {
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article article = new Article();
            article.setPictures(new ArrayList<>(1));
            article.setId(rs.getInt("article_id"));
            article.setHeadline(rs.getString("headline"));
            article.setLead(rs.getString("lead_text"));
            article.setDate(rs.getString("article_date"));
            String url = rs.getString("url");
            if (url != null && !url.equals("")) {
                article.getPictures().add(new Picture());
                article.getPictures().get(0).setUrl(url);
            }
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
            article.setSubCat(rs.getString("sub_cat_text"));
            article.setMainCat(rs.getString("main_cat_text"));
            return article;
        }
    };
}

