import lucene.LuceneUtil;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
//        locaFile2URLs locaFile2URLs = new locaFile2URLs();
//        List<String> urlList = locaFile2URLs.getUrls();
//        Spider spider = Spider.create(new urlSpider());
//        spider.addPipeline(new lucenePipeline());
//        for(String url:urlList)
//            spider.addUrl(url);
//        spider.thread(10).run();
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()){
            String keyWord = in.nextLine().trim();
            System.out.println(String.format("搜索：%s",keyWord));
            LuceneUtil.query(keyWord,10,10);
        }

    }
}
