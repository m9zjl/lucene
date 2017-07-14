import lucene.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import lucene.BuildIndex;


/**
 * Created by zjl on 7/14/17.
 */
public class lucenePipeline implements Pipeline,Closeable {

    public static final FieldType TYPE_NOT_INDEX = new FieldType();
    public static final FieldType TYPE_INDEX = new FieldType();
    public static final FieldType TYPE_INDEX_TERM = new FieldType();

    static {
        TYPE_NOT_INDEX.setIndexOptions(IndexOptions.NONE);
        TYPE_NOT_INDEX.setTokenized(false);
        TYPE_NOT_INDEX.setStored(true);
        TYPE_NOT_INDEX.freeze();

        TYPE_INDEX.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        TYPE_INDEX.setTokenized(true);
        TYPE_INDEX.setStored(true);
        TYPE_INDEX.freeze();

        TYPE_INDEX_TERM.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        TYPE_INDEX_TERM.setTokenized(true);
        TYPE_INDEX_TERM.setStored(true);
        TYPE_INDEX_TERM.setStoreTermVectors(true);
        TYPE_INDEX_TERM.freeze();
    }

    private final ReentrantLock lock = new ReentrantLock();
    private BuildIndex buildIndex;

    public void process(ResultItems resultItems, Task task) {
        if(buildIndex == null){
            lock.lock();
            try {
                if (buildIndex == null)
                    buildIndex = LuceneUtil.newIndex();
            }finally {
                lock.unlock();
            }
        }
        Document doc = new Document();
        doc.add(new Field("title",resultItems.get("title").toString(),TYPE_NOT_INDEX));
        doc.add(new Field("content",resultItems.get("content").toString(),TYPE_INDEX));
        doc.add(new Field("url",resultItems.get("url").toString(),TYPE_INDEX_TERM));
        buildIndex.update(doc.get("url"),doc);
        System.out.println(String.format("url: %s finished",doc.get("url")));
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     * <p>
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        if(buildIndex!=null)
            LuceneUtil.closeInde();
    }
}
