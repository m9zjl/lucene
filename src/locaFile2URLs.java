import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjl on 7/13/17.
 */
public class locaFile2URLs {


    public List<String> getUrls() throws IOException {
        String path = "/Users/zjl/git/untitled/web/WEB-INF/bookmarks_7_12_17.html";
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String lineTxt = null;
        List<String> list = new ArrayList<String>();
        while((lineTxt = reader.readLine()) != null){

            stringBuilder.append(lineTxt);
        }
        String pattern = "<A HREF=\"(.*?)\" ADD_DATE";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(stringBuilder.toString());
        int count =0;
        while (m.find()){
            list.add(m.group(1));
        }
        System.out.println(String.format("find URL count = %d",list.size()));
        return list;
    }
}
