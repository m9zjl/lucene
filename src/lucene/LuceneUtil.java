package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.sandbox.queries.FuzzyLikeThisQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Dictionary;

/**
 * Created by zjl on 7/14/17.
 */
public class LuceneUtil  {
    public static final String INDEX_PATH;
    private static final Analyzer ANALYZER = new org.wltea.analyzer.lucene.IKAnalyzer(true);
    private static int count = 0;
    private static BuildIndex index = null;


    static {
        String index = "lucene/index";
        try {
            if (!ResourceUtils.getFile(index).exists()) {
                index = "lucene/index";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            INDEX_PATH = index;
        }
    }

    public synchronized static BuildIndex newIndex(){
        try{
            count++;
            if(index!=null) return index;
            String path = ResourceUtils.getFile(INDEX_PATH).getPath();
            if(path.startsWith("/")) path = path.substring(1);
            Directory directory = new SimpleFSDirectory(FileSystems.getDefault().getPath(path));
            IndexWriterConfig config = new IndexWriterConfig(ANALYZER);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter indexWriter = new IndexWriter(directory,config);
            index = new BuildIndex(indexWriter, directory);
            return index;
        } catch (IOException e) {
           throw  new RuntimeException();
        }
    }

    public synchronized static void closeInde(){
        if(index!=null){
            count--;
            if(count<1){
                index.close();
                index = null;
                count=0;
            }
        }
    }

    //unfinished

    public static void query(String q,int pageNum,int pageSize){
        try {
            String path = ResourceUtils.getFile(INDEX_PATH).getPath();
            if(path.startsWith("/")) path = path.substring(1);
            Directory directory = new SimpleFSDirectory(FileSystems.getDefault().getPath(path));
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser parser = new QueryParser("content", ANALYZER);
            Query query = parser.parse(q);
            TopDocs topDocs = searcher.search(query,10);
            ScoreDoc[] hits = topDocs.scoreDocs;
            for(int i=0;i<hits.length;i++){
                Document hitDoc = searcher.doc(hits[i].doc);
                System.out.println(hitDoc.get("title"));
                System.out.println(hitDoc.get("url"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
