package se.mah;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by aliona on 2017-05-11.
 */
public class DatabaseConnector {
    static final String JDBC_DRIVER = "org.postgresql.Driver";

    //  Database credentials
    static final String URL = "jdbc:postgresql://pgserver.mah.se/psql_dagblad?user=ag7691&password=cf08rcex";

    public Article getArticle(String articleID) {
        int article_id = Integer.parseInt(articleID);
        int subcat = 0;
        Article article = new Article();
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stm2 = null;
        PreparedStatement stm3 = null;
        PreparedStatement stm4 = null;
        PreparedStatement stm5 = null;
        try{
            System.out.println("Connecting...");
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL);

            System.out.println("Creating statement...");
            String sql;
            sql = "SELECT lead_text, headline, body, article_date, sub_cat_id FROM article WHERE article_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, article_id);
            ResultSet rs = stmt.executeQuery();

            //STEP 5: Extract data from result set
                //Retrieve by column name
            while(rs.next()) {
                String leadText = rs.getString("lead_text");
                article.setLead(leadText);
                String headline = rs.getString("headline");
                article.setHeadline(headline);
                String body = rs.getString("body");
                article.setBody(body);
                String articleDate = rs.getString("article_date");
                article.setDate(articleDate);
                subcat = rs.getInt("sub_cat_id");
            }
            rs.close();
            stmt.close();

            sql = "SELECT first_name, last_name FROM person WHERE person_id = (SELECT person_id FROM written_by WHERE article_id=?)";
            stm2 = conn.prepareStatement(sql);
            stm2.setInt(1, article_id);
            ResultSet rs2 = stm2.executeQuery();

            while(rs2.next()) {
                String author_name = rs2.getString("first_name");
                String author_surname = rs2.getString("last_name");
                article.setAuthor(author_name + " " + author_surname);
            }

            rs2.close();
            stm2.close();

            sql = "SELECT sub_cat_text, main_cat_text FROM category WHERE sub_cat_id=?";
            stm3 = conn.prepareStatement(sql);
            stm3.setInt(1, subcat);
            ResultSet rs3 = stm3.executeQuery();

            while(rs3.next()) {
                String subcattext = rs3.getString("sub_cat_text");
                String maincattext = rs3.getString("main_cat_text");
                article.setSubcat(subcattext);
                article.setMaincat(maincattext);
            }

            rs3.close();
            stm3.close();

            sql = "SELECT url, alt_text FROM picture WHERE picture_id=(SELECT picture_id FROM article_picture WHERE article_id=?)";
            stm4 = conn.prepareStatement(sql);
            stm4.setInt(1, article_id);
            ResultSet rs4 = stm4.executeQuery();

            String imgurl = null, altimg = null;
            while(rs4.next()) {
                imgurl = rs4.getString("url");
                altimg = rs4.getString("alt_text");
            }

            article.setImage(imgurl);
            article.setAltimage(altimg);

            rs4.close();
            stm4.close();

            sql = "SELECT description FROM article_picture WHERE article_id=?";
            stm5 = conn.prepareStatement(sql);
            stm5.setInt(1, article_id);
            ResultSet rs5 = stm5.executeQuery();

            String descr = null;

            while(rs5.next()) {
                descr = rs5.getString("description");
                article.setImgDescription(descr);
            }

            rs5.close();
            stm5.close();

            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return article;
    }

    public HashMap<String, String> getArticlesList() {
        HashMap<String, String> articles = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            System.out.println("Connecting...");
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(URL);

            System.out.println("Creating statement...");
            String sql;
            sql = "SELECT article_id, headline FROM article";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            //STEP 5: Extract data from result set
            //Retrieve by column name
            while(rs.next()) {
                articles.put(rs.getString("article_id"), rs.getString("headline"));
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return articles;
    }
}
