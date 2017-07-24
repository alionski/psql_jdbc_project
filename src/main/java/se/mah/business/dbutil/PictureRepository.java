package se.mah.business.dbutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Picture;

import java.util.HashMap;
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
        parameters.put("alt_text", pic.getAltimage());
        simpleJdbcInsert.withTableName("picture").usingGeneratedKeyColumns("picture_id");
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    public void addPictureToArticle(int article_id, int picture_id, String description) {

    }
}
