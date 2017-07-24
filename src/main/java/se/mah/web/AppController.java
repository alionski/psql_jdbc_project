package se.mah.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.mah.business.dbutil.*;
import se.mah.business.entities.Article;
import se.mah.business.entities.Comment;
import se.mah.business.entities.Person;
import se.mah.business.entities.Picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CategoryRepository categories;
    @Autowired
    private PictureRepository pictures;

    @RequestMapping(value = "/")
    public String index(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        model = prepareModelSides(model);
        model.addAttribute("articles", articlesList);

        return "public/main";
    }

    @RequestMapping(value= "/articles")
    public String showByCat(@RequestParam(value="maincat", required = false) String maincat, Model modelMain,
                            @RequestParam(value="subcat", required = false) String subcat, Model modelSub,
                            @RequestParam(value="searchAuthor", required = false) String authorId, Model modelAuthor){
        if (authorId != null && !authorId.equals("")) {
            modelAuthor = prepareModelSides(modelAuthor);
            List<Article> articlesAuthor = articles.getArticlesByAuthor(authorId);
            modelAuthor.addAttribute("articles", articlesAuthor);
            return "public/main";
        } else if (maincat != null && !maincat.equals("")) {
            modelMain = prepareModelSides(modelMain);
            List<Article> articlesMain = articles.getArticlesByMainCat(maincat);
            modelMain.addAttribute("articles", articlesMain);
            return "public/main";
        } else {
            modelSub = prepareModelSides(modelSub);
            List<Article> articlesSub = articles.getArticlesBySubCat(subcat);
            modelSub.addAttribute("articles", articlesSub);
            return "public/main";
        }
    }

    @RequestMapping(value = "/public/article", method = RequestMethod.GET)
    public String article(@RequestParam(value="id") String articleID,  Model model,
                          @RequestParam(value="deleteComment", required = false) String commentId) {
        if (commentId == null || commentId.equals("")) {
            model = prepareModelSides(model);
            Article article = articles.getArticle(articleID);
            List<Comment> commentsList = comments.getComments(articleID);
            model.addAttribute("article", article);
            model.addAttribute("comments", commentsList);
            model.addAttribute("comment", new Comment());
            return "public/article";
        } else {
            comments.deleteComment(commentId);
            return "redirect:/public/article?id=" + articleID;
        }
    }

    @RequestMapping(value = "/public/article", method = RequestMethod.POST)
    public String postComment(@RequestParam(value="id") String articleID, Comment comment) {
        System.out.println("Reached the post comment method");
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        comment.setComment_date(sqlDate);
        comment.setArticle_id(articleID);
        comments.postComment(comment);
        return "redirect:/public/article?id="+articleID;
    }

    @RequestMapping(value = "/user/new-article", method = RequestMethod.POST)
    public String postArticle(@RequestParam Article article, Picture picture) {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        int pic_id = pictures.postNewPicture(picture);
        int article_id = articles.postNewArticle(article);
        pictures.addPictureToArticle(article_id, pic_id, article.getImgDescription());
        return "redirect:/public/article?id=0";
    }

    @RequestMapping(value="/admin/adminpanel", method = RequestMethod.GET)
    public String adminPanel(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        List<Person> employees = persons.getEmployees();
        model = prepareModelSides(model);
        model.addAttribute("articles", articlesList);
        model.addAttribute("employeeList", employees);
        return "admin/adminpanel";
    }

    @RequestMapping(value="/user/authorpanel", method = RequestMethod.GET)
    public String authorPanel(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        List<Person> authors = persons.getEmployees(); //TODO: change to no-PN method later
        model = prepareModelSides(model);
        model.addAttribute("articles", articlesList);
        model.addAttribute("authors", authors);
        model.addAttribute("article", new Article());
        model.addAttribute("picture", new Picture());
        return "user/authorpanel";
    }

    @RequestMapping(value = "/public/login")
    public String login(Model model) {
        model = prepareModelSides(model);
        return "public/login";
    }

    private Model prepareModelSides(Model model) {
        List<String> mainCats = categories.getMainCategories();
        HashMap<Integer,String> subCats = categories.getSubCategories();
        model.addAttribute("maincats", mainCats);
        model.addAttribute("subcats", subCats);
        return model;
    }
}
