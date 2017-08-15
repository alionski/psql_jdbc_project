package se.mah.business.dbutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliona on 2017-07-22.
 */
@Repository
public class CategoryRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jdbc;

    public List<String> getMainCategories() {
        return jdbc.query("SELECT DISTINCT main_cat_text FROM category", mainCatMapper);
    }

    public HashMap<Integer, String> getSubCategories() {
        ResultSetExtractor subcatExtractor = new ResultSetExtractor() {
            public Object extractData(ResultSet rs) throws SQLException {
                Map<Integer, String> subcats = new HashMap<Integer, String>();
                while (rs.next()) {
                    Integer id = rs.getInt("sub_cat_id");
                    String text = rs.getString("sub_cat_text");
                    subcats.put(id, text);
                }
                return subcats;
            }
        };

        return (HashMap) jdbc.query("SELECT sub_cat_id, sub_cat_text FROM category", subcatExtractor);

    }

    private static final RowMapper<String> mainCatMapper = new RowMapper<String>() {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("main_cat_text");
        }
    };

//    private static final RowMapper<Subcategory> subCatMapper = new RowMapper<Subcategory>() {
//        public HashMap<Integer, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
//            HashMap<Integer, String> subcat = new HashMap<Integer, String>();
//            subcat.put(Integer.valueOf(rs.getInt("sub_cat_text")),
//                    rs.getString("sub_cat_text"));
//            return subcat;
//        }
//    };

}
