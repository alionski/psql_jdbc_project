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

/**
 * Created by aliona on 2017-05-12.
 *
 */
@Controller
public class AppController {
    @Autowired
    private PersonRepository personRepo;
    @Autowired
    private ArticleRepository articles;
    @Autowired
    private CommentRepository comments;
    @Autowired
    private CategoryRepository categories;
    @Autowired
    private PictureRepository pictureRepo;

    @RequestMapping(value = "/")
    public String index(Model model) {
        List<Article> articlesList = articles.getLatestArticles();
        model = preparePageSides(model);
        model.addAttribute("articles", articlesList);
        model.addAttribute("page_head", "Latest news");
        return "public/main";
    }

    @RequestMapping(value= "/articles")
    public String showByCat(@RequestParam(value="mainCat", required = false) String maincat, Model modelMain,
                            @RequestParam(value="subCat", required = false) String subcat, Model modelSub,
                            @RequestParam(value="searchAuthor", required = false) String authorId, Model modelAuthor){
        if (authorId != null && !authorId.equals("")) {
            modelAuthor = preparePageSides(modelAuthor);
            List<Article> articlesAuthor = articles.getArticlesByAuthor(authorId);
            Person author = personRepo.getPerson(authorId);
            modelAuthor.addAttribute("articles", articlesAuthor);
            modelAuthor.addAttribute("page_head",
                                     "Articles by: " + author.getFirstName() + " " + author.getLastName());
            return "public/main";
        } else if (maincat != null && !maincat.equals("")) {
            modelMain = preparePageSides(modelMain);
            List<Article> articlesMain = articles.getArticlesByMainCat(maincat);
            modelMain.addAttribute("articles", articlesMain);
            modelMain.addAttribute("page_head", "Main category: " + maincat);
            return "public/main";
        } else {
            modelSub = preparePageSides(modelSub);
            List<Article> articlesSub = articles.getArticlesBySubCat(subcat);
            modelSub.addAttribute("articles", articlesSub);
            modelSub.addAttribute("page_head", "Subcategory: " + subcat);
            return "public/main";
        }
    }

    @RequestMapping(value = "/public/article", method = RequestMethod.GET)
    public String article(@RequestParam(value="id") String articleID,  Model model,
                          @RequestParam(value="deleteComment", required = false) String commentId) {
        if (commentId == null || commentId.equals("")) {
            model = preparePageSides(model);
            Article article = articles.getArticle(articleID);
            article.setPictures(pictureRepo.getPictureList(articleID));
            article.setAuthors(personRepo.getArticleAuthors(articleID));
            article = insertImages(article);

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
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        comment.setCommentDate(sqlDate);
        comment.setArticleId(articleID);
        comments.postComment(comment);
        return "redirect:/public/article?id="+articleID;
    }

    @RequestMapping(value="/admin/adminpanel", method = RequestMethod.GET)
    public String adminPanel(Model model) {
        List<Article> articlesList = articles.getArticlesList();
        List<Person> employees = personRepo.getEmployees();
        model = preparePageSides(model);
        model.addAttribute("articles", articlesList);
        model.addAttribute("employeeList", employees);
        model.addAttribute("new_author", new Person());
        return "admin/adminpanel";
    }

    @RequestMapping(value="/admin/adminpanel", method = RequestMethod.POST)
    public String addNewAuthor( Person author) {
        personRepo.addNewAuthor(author);
        return "redirect:/admin/adminpanel";
    }

    @RequestMapping(value="/user/authorpanel", method = RequestMethod.GET)
    public String authorPanel(Model model) {
//        List<Article> articlesList = articles.getArticlesList();
        List<Person> authors = personRepo.getEmployees(); //TODO: change to no-PN method later
        model = preparePageSides(model);
//        model.addAttribute("articles", articlesList);
        model.addAttribute("found_authors", authors);
        model.addAttribute("article", new Article(2, 3));
        return "user/authorpanel";
    }

    @RequestMapping(value = "/user/new-article", method = RequestMethod.POST)
    public String postArticle(Article article) {
        int articleId = articles.postNewArticle(article);
        personRepo.addAuthorsToArticle(article.getAuthors(), articleId);
        for (Picture pic : article.getPictures()) {
            if (pic.getUrl() != null && !pic.getUrl().equals("")) {
                int picId = pictureRepo.postNewPicture(pic);
                pictureRepo.addArticlePicture(articleId, picId, pic.getDescriptionForArticle());
            }
        }
        return "redirect:/public/article?id="+articleId;
    }

    @RequestMapping(value = "/public/login")
    public String login(Model model) {
        model = preparePageSides(model);
        return "public/login";
    }

    private Model preparePageSides(Model model) {
        List<String> mainCats = categories.getMainCategories();
        HashMap<Integer,String> subCats = categories.getSubCategories();
        model.addAttribute("maincats", mainCats);
        model.addAttribute("subcats", subCats);
        return model;
    }

    private Article insertImages(Article article) {
        String articleBody = article.getBody();
        List<Picture> pics = article.getPictures();
        for (int i = 0; i < article.getPictures().size(); i++) {
//            Pattern pattern = Pattern.compile("PIC"+ (i+1));
//            Matcher matcher = pattern.matcher(articleBody);
//            articleBody = matcher.replaceFirst("</p><img class=\"articleimg\" src=\"" + article.getImage() + "\" alt=\"" +
//                    article.getAltimage() + "\"/> <p style=\"text-align:center; font-size:9px\"> <cite>" +
//                    article.getImgDescription() + "</cite> </p> <p>");

            articleBody = articleBody.replace("PIC_" + (i+1),
                    "</p><img class=\"articleimg\" src=\"" + pics.get(i).getUrl() + "\" alt=\"" +
                            pics.get(i).getAlttext() + "\"/> <p style=\"text-align:center; font-size:9px\"> <cite>" +
                            pics.get(i).getDescriptionForArticle() + "</cite> </p> <p>");
        }
        article.setBody(articleBody);
        return article;
    }
}
