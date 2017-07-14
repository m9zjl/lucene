import lucene.LuceneUtil;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.io.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
//        locaFile2URLs locaFile2URLs = new locaFile2URLs();
//        List<String> urlList = locaFile2URLs.getUrls();
//        Spider spider = Spider.create(new urlSpider());
//        spider.addPipeline(new lucenePipeline());
//        for(String url:urlList)
//            spider.addUrl(url);
//        spider.thread(10).run();
        LuceneUtil.query("面试",10,10);
    }
}
