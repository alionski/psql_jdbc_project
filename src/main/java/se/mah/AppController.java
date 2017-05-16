package se.mah;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * Created by aliona on 2017-05-12.
 */
@Controller
public class AppController {
    private DatabaseConnector dbCon = new DatabaseConnector();

    @RequestMapping("/articles")
    public String articles(Model model) {
        HashMap<String, String> articles = dbCon.getArticlesList();
        model.addAttribute("articles", articles);
        return "articles";
    }

    @RequestMapping("/article")
    public String article(@RequestParam(value="id", required=false, defaultValue="None") String articleID,  Model model) {
        Article article = dbCon.getArticle(articleID);
        model.addAttribute("article", article);
        return "article";
    }
}
