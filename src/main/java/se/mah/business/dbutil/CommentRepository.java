package se.mah.business.dbutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import se.mah.business.entities.Comment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by aliona on 2017-07-21.
 */
@Repository
public class CommentRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected JdbcTemplate jdbc;

    public int postComment(Comment comment) {
        return jdbc.update("INSERT into user_comment(article_id, nickname, comment_text, comment_date) VALUES (?, ?, ?, ?)",
                Integer.parseInt(comment.getArticleId()),
                comment.getNickname(),
                comment.getCommentText(),
                comment.getCommentDate()
                );
    }

    public int deleteComment(String commentId) {
        return jdbc.update("DELETE FROM user_comment WHERE comment_id=?", Integer.parseInt(commentId));
    }

    public List<Comment> getComments(String articleId) {
        int id = Integer.parseInt(articleId);
        return jdbc.query("SELECT comment_id, nickname, comment_text, comment_date FROM user_comment WHERE article_id=? " +
                        "ORDER BY comment_date ASC",
                commentsListMapper, id);
    }

    private static final RowMapper<Comment> commentsListMapper = new RowMapper<Comment>() {
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setId(rs.getInt("comment_id"));
            comment.setNickname(rs.getString("nickname"));
            comment.setCommentText(rs.getString("comment_text"));
            comment.setCommentDate(rs.getDate("comment_date"));
            return comment;
        }
    };

}
