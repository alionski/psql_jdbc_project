package se.mah.business.dbutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Article;
import se.mah.business.entities.Picture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliona on 2017-07-23.
 */
@Repository
public class PictureRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    protected JdbcTemplate jdbc;
    protected SimpleJdbcInsert simpleJdbcInsert;

    public int postNewPicture(Picture pic) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("url", pic.getUrl());
        parameters.put("alt_text", pic.getAlttext());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbc.getDataSource());
        simpleJdbcInsert.withTableName("picture").usingGeneratedKeyColumns("picture_id");
        simpleJdbcInsert.usingColumns("url", "alt_text");
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    public void addArticlePicture(int articleId, int pictureId, String description) {
        jdbc.update("INSERT INTO article_picture(article_id, picture_id, description) " +
                "VALUES(?,?,?)", articleId, pictureId, description);
    }

    public List<Picture> getPictureList(String articleId) {
        int id = Integer.parseInt(articleId);
        return jdbc.query("SELECT picture.picture_id, alt_text, url, description FROM picture INNER JOIN article_picture "+
                "ON picture.picture_id = article_picture.picture_id WHERE article_id = ?", pictureListMapper, id);
    }

    private static final RowMapper<Picture> pictureListMapper = new RowMapper<Picture>() {
        public Picture mapRow(ResultSet rs, int rowNum) throws SQLException {
            Picture picture = new Picture();
            picture.setPictureId(rs.getInt("picture_id"));
            picture.setAlttext(rs.getString("alt_text"));
            picture.setUrl(rs.getString("url"));
            picture.setDescriptionForArticle(rs.getString("description"));
            return picture;
        }
    };


}
