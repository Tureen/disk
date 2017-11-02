package com.yunip.utils.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Lucene工具类(基于Lucene5.0封装)
 * @author can.du
 *
 */
public class LuceneUtils {
    
    private static final LuceneManager luceneManager = LuceneManager.getInstance();

    private static Analyzer            analyzer      = new IKAnalyzer();

    /**
     * 打开索引目录
     * 
     * @param luceneDir
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static FSDirectory openFSDirectory(String luceneDir) {
        FSDirectory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(luceneDir));
            /**
             * 注意：isLocked方法内部会试图去获取Lock,如果获取到Lock，会关闭它，否则return false表示索引目录没有被锁，
             * 这也就是为什么unlock方法被从IndexWriter类中移除的原因
             */
            IndexWriter.isLocked(directory);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return directory;
    }

    /**
     * 关闭索引目录并销毁
     * @param directory
     * @throws IOException
     */
    public static void closeDirectory(Directory directory) throws IOException {
        if (null != directory) {
            directory.close();
            directory = null;
        }
    }

    /**
     * 获取IndexWriter
     * @param dir
     * @param config
     * @return
     */
    public static IndexWriter getIndexWrtier(Directory dir,
            IndexWriterConfig config) {
        return luceneManager.getIndexWriter(dir, config);
    }

    /**
     * 获取IndexWriter
     * @param dir
     * @param config
     * @return
     */
    public static IndexWriter getIndexWrtier(String directoryPath,
            IndexWriterConfig config) {
        FSDirectory directory = openFSDirectory(directoryPath);
        return luceneManager.getIndexWriter(directory, config);
    }

    /**
     * 获取IndexReader
     * @param dir
     * @param enableNRTReader  是否开启NRTReader
     * @return
     */
    public static IndexReader getIndexReader(Directory dir,
            boolean enableNRTReader) {
        return luceneManager.getIndexReader(dir, enableNRTReader);
    }

    /**
     * 获取IndexReader(默认不启用NRTReader)
     * @param dir
     * @return
     */
    public static IndexReader getIndexReader(Directory dir) {
        return luceneManager.getIndexReader(dir);
    }

    /**
     * 获取IndexSearcher
     * @param reader    IndexReader对象
     * @param executor  如果你需要开启多线程查询，请提供ExecutorService对象参数
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader,
            ExecutorService executor) {
        return luceneManager.getIndexSearcher(reader, executor);
    }

    /**
     * 获取IndexSearcher(不支持多线程查询)
     * @param reader    IndexReader对象
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader) {
        return luceneManager.getIndexSearcher(reader);
    }

    /**
     * 创建QueryParser对象
     * @param field
     * @param analyzer
     * @return
     */
    public static QueryParser createQueryParser(String field, Analyzer analyzer) {
        return new QueryParser(field, analyzer);
    }

    /**
     * 关闭IndexReader
     * @param reader
     */
    public static void closeIndexReader(IndexReader reader) {
        if (null != reader) {
            try {
                reader.close();
                reader = null;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexWriter
     * @param writer
     */
    public static void closeIndexWriter(IndexWriter writer) {
        if (null != writer) {
            try {
                writer.close();
                writer = null;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexReader和IndexWriter
     * @param reader
     * @param writer
     */
    public static void closeAll(IndexReader reader, IndexWriter writer) {
        closeIndexReader(reader);
        closeIndexWriter(writer);
    }

    /**
     * 删除索引[注意：请自己关闭IndexWriter对象]
     * @param writer
     * @param field
     * @param value
     */
    public static void deleteIndex(IndexWriter writer, String field,
            String value) {
        try {
            writer.deleteDocuments(new Term[] { new Term(field, value) });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引[注意：请自己关闭IndexWriter对象]
     * @param writer
     * @param query
     */
    public static void deleteIndex(IndexWriter writer, Query query) {
        try {
            writer.deleteDocuments(query);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引[注意：请自己关闭IndexWriter对象]
     * @param writer
     * @param terms
     */
    public static void deleteIndexs(IndexWriter writer, Term[] terms) {
        try {
            writer.deleteDocuments(terms);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引[注意：请自己关闭IndexWriter对象]
     * @param writer
     * @param querys
     */
    public static void deleteIndexs(IndexWriter writer, Query[] querys) {
        try {
            writer.deleteDocuments(querys);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有索引文档
     * @param writer
     */
    public static void deleteAllIndex(IndexWriter writer) {
        try {
            writer.deleteAll();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引文档
     * @param writer
     * @param term
     * @param document
     */
    public static void updateIndex(IndexWriter writer, Term term,
            Document document) {
        try {
            writer.updateDocument(term, document);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引文档
     * @param writer
     * @param term
     * @param document
     */
    public static void updateIndex(IndexWriter writer, String field,
            String value, Document document) {
        updateIndex(writer, new Term(field, value), document);
    }

    /**
     * 添加索引文档
     * @param writer
     * @param doc
     */
    public static void addIndex(IndexWriter writer, Document document) {
        updateIndex(writer, null, document);
    }

    /**
     * 索引文档查询
     * @param searcher
     * @param query
     * @return
     */
    public static List<Document> query(IndexSearcher searcher, Query query) {
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query, Integer.MAX_VALUE);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] scores = topDocs.scoreDocs;
        int length = scores.length;
        if (length <= 0) {
            return Collections.emptyList();
        }
        List<Document> docList = new ArrayList<Document>();
        try {
            for (int i = 0; i < length; i++) {
                Document doc = searcher.doc(scores[i].doc);
                docList.add(doc);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return docList;
    }

    /**
     * 返回索引文档的总数[注意：请自己手动关闭IndexReader]
     * @param reader
     * @return
     */
    public static int getIndexTotalCount(IndexReader reader) {
        return reader.numDocs();
    }

    /**
     * 返回索引文档中最大文档ID[注意：请自己手动关闭IndexReader]
     * @param reader
     * @return
     */
    public static int getMaxDocId(IndexReader reader) {
        return reader.maxDoc();
    }

    /**
     * 返回已经删除尚未提交的文档总数[注意：请自己手动关闭IndexReader]
     * @param reader
     * @return
     */
    public static int getDeletedDocNum(IndexReader reader) {
        return getMaxDocId(reader) - getIndexTotalCount(reader);
    }

    /**
     * 根据docId查询索引文档
     * @param reader         IndexReader对象
     * @param docID          documentId
     * @param fieldsToLoad   需要返回的field
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader, int docID,
            Set<String> fieldsToLoad) {
        try {
            return reader.document(docID, fieldsToLoad);
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * 根据docId查询索引文档
     * @param reader         IndexReader对象
     * @param docID          documentId
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader, int docID) {
        return findDocumentByDocId(reader, docID, null);
    }

    /**
     * @Title: createHighlighter
     * @Description: 创建高亮器
     * @param query             索引查询对象
     * @param prefix            高亮前缀字符串
     * @param stuffix           高亮后缀字符串
     * @param fragmenterLength  摘要最大长度
     * @return
     */
    public static Highlighter createHighlighter(Query query, String prefix,
            String stuffix, int fragmenterLength) {
        Formatter formatter = new SimpleHTMLFormatter(
                (prefix == null || prefix.trim().length() == 0) ? "<font color=\"red\">" : prefix,
                (stuffix == null || stuffix.trim().length() == 0) ? "</font>" : stuffix);
        Scorer fragmentScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
        Fragmenter fragmenter = new SimpleFragmenter(
                fragmenterLength <= 0 ? 50 : fragmenterLength);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter;
    }

    /**
     * @Title: highlight
     * @Description: 生成高亮文本
     * @param document          索引文档对象
     * @param highlighter       高亮器
     * @param analyzer          索引分词器
     * @param field             高亮字段
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    public static String highlight(Document document, Highlighter highlighter,
            Analyzer analyzer, String field) throws IOException {
        List<IndexableField> list = document.getFields();
        for (IndexableField fieldable : list) {
            String fieldValue = fieldable.stringValue();
            if (fieldable.name().equals(field)) {
                try {
                    fieldValue = highlighter.getBestFragment(analyzer, field,
                            fieldValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                }
                catch (InvalidTokenOffsetsException e) {
                    fieldValue = fieldable.stringValue();
                }
                return (fieldValue == null || fieldValue.trim().length() == 0) ? fieldable.stringValue().replaceAll("<", "&lt;").replaceAll(">", "&gt;") : fieldValue;
            }
        }
        return null;
    }

    /**
     * @Title: searchTotalRecord
     * @Description: 获取符合条件的总记录数
     * @param query
     * @return
     * @throws IOException
     */
    public static int searchTotalRecord(IndexSearcher search, Query query) {
        ScoreDoc[] docs = null;
        try {
            TopDocs topDocs = search.search(query, Integer.MAX_VALUE);
            if (topDocs == null || topDocs.scoreDocs == null
                    || topDocs.scoreDocs.length == 0) {
                return 0;
            }
            docs = topDocs.scoreDocs;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return docs.length;
    }

    /**
     * @Title: pageQuery
     * @Description: Lucene分页查询
     * @param searcher
     * @param query
     * @param page
     * @throws Exception 
     * @throws IOException
     */
    public static List<Document> pageQuery(IndexSearcher searcher, Directory directory,
            Query query, Page<Object> page) throws Exception {
        //先查询上页最后一条记录
        pageSeachQuery(searcher, directory, query, page);
        TopDocs topDocs = null;
        topDocs = searcher.searchAfter(page.getAfterDoc(), query, page.getPageSize());
        List<Document> docList = new ArrayList<Document>();
        ScoreDoc[] docs = topDocs.scoreDocs;
        int index = 0;
        for (ScoreDoc scoreDoc : docs) {
            int docID = scoreDoc.doc;
            Document document = null;
            try {
                document = searcher.doc(docID);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (index == docs.length - 1) {
                page.setAfterDoc(scoreDoc);
                page.setAfterDocId(docID);
            }
            docList.add(document);
            index++;
        }
        closeIndexReader(searcher.getIndexReader());
        return docList;
    }
    
    /**
     * @Title: pageSeachQuery
     * @Description: Lucene分页查询查处前一页最后一条记录
     * @param searcher
     * @param query
     * @param page
     * @throws IOException
     */
    public static void pageSeachQuery(IndexSearcher searcher, Directory directory,
            Query query, Page<Object> page) throws Exception {
        int totalRecord = searchTotalRecord(searcher, query);
        //设置总记录数
        page.setTotalRecord(totalRecord);
        int top = totalRecord == 0 ? page.getPageSize() : totalRecord;
        if (totalRecord > 0){
          //设置搜索条件  
            TopDocs result = searcher.search(query, top);  
            //上一页的最后一个document索引  
            int index = (page.getCurrentPage() - 1) * page.getPageSize();
            //如果当前页是第一页面scoreDoc=null。  
            if(index>0){  
                //因为索引是从0开始所以要index-1  
                page.setAfterDoc(result.scoreDocs[index-1]);
            }  
        }
    }

    /**
     * @Title: pageQuery
     * @Description: 分页查询
     * @param searcher
     * @param directory
     * @param query
     * @param page
     * @param highlighterParam
     * @param writerConfig
     * @throws IOException
     */
    public static List<Document> pageQuery(IndexSearcher searcher, Directory directory,
            Query query, Page<Object> page,
            HighlighterParam highlighterParam, IndexWriterConfig writerConfig)
            throws Exception {
        IndexWriter writer = null;
        List<Document> docList = null;
        //若未设置高亮
        if (null == highlighterParam || !highlighterParam.isHighlight()) {
            docList = pageQuery(searcher, directory, query, page);
        } else {
            docList = new ArrayList<Document>();
            //先查询上页最后一条记录
            pageSeachQuery(searcher, directory, query, page);
            TopDocs topDocs = searcher.searchAfter(page.getAfterDoc(), query, page.getPageSize());
            ScoreDoc[] docs = topDocs.scoreDocs;
            int index = 0;
            writer = getIndexWrtier(directory, writerConfig);
            for (ScoreDoc scoreDoc : docs) {
                int docID = scoreDoc.doc;
                //获取查询内容
                Document document = searcher.doc(docID);
                String[] fieldNames = highlighterParam.getFieldNames();
                for(String fieldName : fieldNames){
                    String content = document.get(fieldName);
                    if (null != content && content.trim().length() > 0) {
                        //创建高亮器
                        Highlighter highlighter = LuceneUtils.createHighlighter(
                                query, highlighterParam.getPrefix(),
                                highlighterParam.getStuffix(),
                                highlighterParam.getFragmenterLength());
                        String text = highlight(document, highlighter, analyzer, fieldName);
                        //若高亮后跟原始文本不相同，表示高亮成功
                        if (!text.equals(content)) {
                            Document tempdocument = new Document();
                            List<IndexableField> indexableFieldList = document.getFields();
                            if (null != indexableFieldList && indexableFieldList.size() > 0) {
                                for (IndexableField field : indexableFieldList) {
                                    if (field.name().equals(fieldName)) {
                                        tempdocument.add(new TextField(field.name(), text, Field.Store.YES));
                                    } else {
                                        if(field.stringValue().length() > highlighterParam.getFragmenterLength()){
                                            String newContent = field.stringValue().substring(0,highlighterParam.getFragmenterLength());
                                            tempdocument.add(new TextField(field.name(), newContent, Field.Store.YES));
                                        } else {
                                            tempdocument.add(field);
                                        }
                                    }
                                }
                            }
                            //updateIndex(writer, new Term("name", content), tempdocument);
                            document = tempdocument;
                        } else {
                            if(content.length() > highlighterParam.getFragmenterLength()){
                                document.removeField(fieldName);
                                String newContent = content.substring(0,highlighterParam.getFragmenterLength());
                                document.add(new TextField(fieldName, newContent, Field.Store.YES));
                            }
                        }
                    } 
                }
                if (index == docs.length - 1) {
                    page.setAfterDoc(scoreDoc);
                    page.setAfterDocId(docID);
                }
                docList.add(document);
                index++;
            }
        }
        closeIndexReader(searcher.getIndexReader());
        closeIndexWriter(writer);
        return docList;
    }
}
