package se.mah.business.dbutil;
import se.mah.business.entities.Comment;
import se.mah.business.entities.Article;
import se.mah.business.entities.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliona on 2017-05-11.
 */
public class DatabaseConnector {
    static final String JDBC_DRIVER = "org.postgresql.Driver";

    //  Database credentials
    static final String URL = "jdbc:postgresql://pgserver.mah.se/psql_dagblad?user=ag7691&password=cf08rcex";

//    public Article getArticle(String articleID) {
//        int article_id = Integer.parseInt(articleID);
//        int subcat = 0;
//        Article article = new Article();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        PreparedStatement stm2 = null;
//        PreparedStatement stm3 = null;
//        PreparedStatement stm4 = null;
//        PreparedStatement stm5 = null;
//        try{
//            System.out.println("Connecting...");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "SELECT lead_text, headline, body, article_date, sub_cat_id FROM article WHERE article_id = ?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, article_id);
//            ResultSet rs = stmt.executeQuery();
//
//            //STEP 5: Extract data from result set
//                //Retrieve by column name
//            while(rs.next()) {
//                String leadText = rs.getString("lead_text");
//                article.setLead(leadText);
//                String headline = rs.getString("headline");
//                article.setHeadline(headline);
//                String body = rs.getString("body");
//                article.setBody(body);
//                String articleDate = rs.getString("article_date");
//                article.setDate(articleDate);
//                subcat = rs.getInt("sub_cat_id");
//            }
//            rs.close();
//            stmt.close();
//
//            sql = "SELECT first_name, last_name FROM person WHERE person_id = (SELECT person_id FROM written_by WHERE article_id=?)";
//            stm2 = conn.prepareStatement(sql);
//            stm2.setInt(1, article_id);
//            ResultSet rs2 = stm2.executeQuery();
//
//            while(rs2.next()) {
//                String author_name = rs2.getString("first_name");
//                String author_surname = rs2.getString("last_name");
////                article.setAuthor(author_name + " " + author_surname);
//            }
//
//            rs2.close();
//            stm2.close();
//
//            sql = "SELECT sub_cat_text, main_cat_text FROM category WHERE sub_cat_id=?";
//            stm3 = conn.prepareStatement(sql);
//            stm3.setInt(1, subcat);
//            ResultSet rs3 = stm3.executeQuery();
//
//            while(rs3.next()) {
//                String subcattext = rs3.getString("sub_cat_text");
//                String maincattext = rs3.getString("main_cat_text");
//                article.setSubCat(subcattext);
//                article.setMainCat(maincattext);
//            }
//
//            rs3.close();
//            stm3.close();
//
//            sql = "SELECT url, alt_text FROM picture WHERE picture_id=(SELECT picture_id FROM article_picture WHERE article_id=?)";
//            stm4 = conn.prepareStatement(sql);
//            stm4.setInt(1, article_id);
//            ResultSet rs4 = stm4.executeQuery();
//
//            String imgurl = null, altimg = null;
//            while(rs4.next()) {
//                imgurl = rs4.getString("url");
//                altimg = rs4.getString("alt_text");
//            }
//
//            article.setImage(imgurl);
//            article.setAltimage(altimg);
//
//            rs4.close();
//            stm4.close();
//
//            sql = "SELECT description FROM article_picture WHERE article_id=?";
//            stm5 = conn.prepareStatement(sql);
//            stm5.setInt(1, article_id);
//            ResultSet rs5 = stm5.executeQuery();
//
//            String descr = null;
//
//            while(rs5.next()) {
//                descr = rs5.getString("description");
//                article.setImgDescription(descr);
//            }
//
//            rs5.close();
//            stm5.close();
//
//            conn.close();
//        }catch(SQLException se){
//            //Handle errors for JDBC
//            se.printStackTrace();
//        }catch(Exception e){
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        }finally{
//            //finally block used to close resources
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }// nothing we can do
//            try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
//        }//end try
//        System.out.println("Goodbye!");
//
//        return article;
//    }
//
//    public ArrayList<Article> getArticlesList() {
//        ArrayList<Article> articles = new ArrayList<>();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//
//        try{
//            System.out.println("Connecting...");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "SELECT article_id, headline, lead_text, article_date FROM article";
//            stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//
//            //STEP 5: Extract data from result set
//            //Retrieve by column name
//            while(rs.next()) {
//                Article article = new Article();
//                article.setId(rs.getInt("article_id"));
//                article.setHeadline(rs.getString("headline"));
//                article.setLead(rs.getString("lead_text"));
//                article.setDate(rs.getString("article_date"));
//                articles.add(article);
//            }
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            //Handle errors for JDBC
//            se.printStackTrace();
//        }catch(Exception e){
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        }finally{
//            //finally block used to close resources
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }// nothing we can do
//            try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
//        }//end try
//        System.out.println("Goodbye!");
//
//        return articles;
//    }
//
//    public void postComment(Comment comment) {
//        int article_id = Integer.parseInt(comment.getArticleId());
//        String comment_text = comment.getCommentText();
//        String nickname = comment.getNickname();
//        Date timestamp = comment.getCommentDate();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        try{
//            System.out.println("Connecting to post coment...");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "INSERT into user_comment(article_id, nickname, comment_text, comment_date) VALUES (?, ?, ?, ?)";
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, article_id);
//            stmt.setString(2, nickname);
//            stmt.setString(3, comment_text);
//            stmt.setDate(4, timestamp);
//            int affectedRows = stmt.executeUpdate();
//            System.out.println("Done posting comment");
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            //Handle errors for JDBC
//            se.printStackTrace();
//        }catch(Exception e){
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        }finally{
//            //finally block used to close resources
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }// nothing we can do
//            try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }//end finally try
//        }//end try
//    }
//
//    public ArrayList<Comment> getComments(String article_id) {
//        int articleId = Integer.parseInt(article_id);
//        ArrayList<Comment> comments = new ArrayList<Comment>();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        try{
//            System.out.println("Connecting to get comments");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "SELECT nickname, comment_text, comment_date FROM user_comment WHERE article_id=?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, articleId);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Comment cmt = new Comment();
//                cmt.setNickname(rs.getString("nickname"));
//                cmt.setCommentText(rs.getString("comment_text"));
//                cmt.setCommentDate(rs.getDate("comment_date"));
//                comments.add(cmt);
//            }
//
//            System.out.println("Done getting comments");
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            se.printStackTrace();
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        return comments;
//    }
//
//    public Person getPersonData(String firstName, String lastName) {
//        Person person = new Person();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        try{
//            System.out.println("Connecting to get admin ID");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "SELECT person_id, first_name, last_name FROM person WHERE first_name=? AND last_name=?";
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, firstName);
//            stmt.setString(2, lastName);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                person.setPersonId(rs.getInt("person_id"));
//                person.setFirstName(rs.getString("first_name"));
//                person.setLastName(rs.getString("last_name"));
//            }
//            System.out.println("Done getting admin ID");
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            se.printStackTrace();
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        return person;
//    }
//
//    public List<Person> getEmployees() {
//        List<Person> empls = new ArrayList<Person>();
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        try{
//            System.out.println("Connecting to get all employess");
//            Class.forName(JDBC_DRIVER).newInstance();
//            conn = DriverManager.getConnection(URL);
//
//            System.out.println("Creating statement...");
//            String sql;
//            sql = "SELECT * FROM person";
//            stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Person person = new Person();
//                person.setNote(rs.getString("note"));
//                person.setPersonId(rs.getLong("person_id"));
//                person.setFirstName(rs.getString("first_name"));
//                person.setLastName(rs.getString("last_name"));
//                empls.add(person);
//                System.out.println(person.getFirstName() + " " + person.getLastName());
//            }
//            System.out.println("Done getting employees");
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//            se.printStackTrace();
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                if(stmt!=null)
//                    stmt.close();
//            }catch(SQLException se2){
//            }try{
//                if(conn!=null)
//                    conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        return empls;
//    }
}
