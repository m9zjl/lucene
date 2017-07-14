package lucene;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;

import javax.print.Doc;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zjl on 7/14/17.
 */
public class BuildIndex {
    private IndexWriter writer;
    private Directory directory;

    public BuildIndex(IndexWriter writer,Directory directory){
        this.writer     = writer;
        this.directory  = directory;
    }

    public void add(Map<String,String> data){
        Document doc = new Document();
        for(Map.Entry<String,String> entry:data.entrySet()){
            doc.add(new Field(entry.getKey(),entry.getValue(), TextField.TYPE_STORED));
        }
        try {
            writer.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(String url,Map<String,String> map){
        Document doc = new Document();
        for(Map.Entry<String,String> entry : map.entrySet()){
            doc.add(new Field(entry.getKey(),entry.getValue(),TextField.TYPE_STORED));
        }
        update(url,doc);
    }

    public void update(String url,Document document){
        try {
            writer.updateDocument(new Term("url",url),document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            writer.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
