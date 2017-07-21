package se.mah.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.mah.business.dbutil.ArticleRepository;
import se.mah.business.dbutil.CommentRepository;
import se.mah.business.dbutil.DatabaseConnector;
import se.mah.business.dbutil.PersonRepository;
import se.mah.business.entities.Article;
import se.mah.business.entities.Comment;
import se.mah.business.entities.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aliona on 2017-05-12.
 *
 */
@Controller
public class AppController {
    @Autowired
    private PersonRepository persons;
    @Autowired
    private ArticleRepository articles;
    @Autowired
    private CommentRepository comments;

    @RequestMapping("/")
    public String index(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        model.addAttribute("articles", articlesList);
        return "public/main";
    }

    @RequestMapping(value = "/public/article", method = RequestMethod.GET)
    public String article(@RequestParam(value="id") String articleID,  Model model) {
        Article article = articles.getArticle(articleID);
        List<Comment> commentsList = comments.getComments(articleID);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentsList);
        model.addAttribute("comment", new Comment());
        return "public/article";
    }

    @RequestMapping(value = "/public/article", method = RequestMethod.POST)
    public String article(@RequestParam(value="id") String articleID, Comment comment) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        comment.setComment_date(sqlDate);
        comment.setArticle_id(articleID);
        comments.postComment(comment);
        return "redirect:/public/article?id="+articleID;
    }

    @RequestMapping(value="/admin/adminpanel", method = RequestMethod.GET)
    public String adminPanel(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        List<Person> employees = persons.getEmployees();
        model.addAttribute("articles", articlesList);
        model.addAttribute("employeeList", employees);
        return "admin/adminpanel";
    }

    @RequestMapping(value = "/public/login")
    public String login() {
        return "public/login";
    }
}
