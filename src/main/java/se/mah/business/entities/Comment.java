package se.mah.business.entities;

import java.sql.Date;

/**
 * Created by aliona on 2017-05-17.
 */
public class Comment {
    private int id;
    private String article_id;
    private String nickname;
    private String comment_text;
    private Date comment_date;

    public Comment() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {

        this.article_id = article_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }
}
