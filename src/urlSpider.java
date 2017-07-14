import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjl on 7/14/17.
 */
public class urlSpider implements PageProcessor{
    public void process(Page page) {
        String title    = "";
        String url      = "";
        String content  = "";
        Document document = Jsoup.parse(page.getHtml().toString());
        if(page.getUrl().regex("blog.csdn.net").match()){
            title = document.select(".article_title .link_title").text();
            url = page.getUrl().toString();
            content = document.select("div#article_details").text();
        }
        else if(page.getUrl().regex("cnblogs.com").match()){
            title = document.select("#cb_post_title_url").text();
            url = page.getUrl().toString();
            content = document.select(".post").text();
        }
        if(url.isEmpty()) page.setSkip(true);
        page.putField("title", title);
        page.putField("url", url);
        page.putField("content", content);
    }

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    public Site getSite() {
        return site;
    }
}
