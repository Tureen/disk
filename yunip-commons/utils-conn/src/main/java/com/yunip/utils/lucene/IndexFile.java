package com.yunip.utils.lucene;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.yunip.utils.util.FileUtil;

/**
 * 读取硬盘文件，创建索引
 * 
 * @author can.du
 * 
 */
public class IndexFile {
    
    @SuppressWarnings("unused")
    private final static String DOC = ".doc";
    
    private static Logger logger = Logger.getLogger(IndexFile.class);
    
	public static void main(String[] args) throws IOException {
		/*String dirPath = "C:\\file\\upload\\49\\";
		String indexPath = "C:\\file\\index\\49\\";*/
		//createIndex(dirPath, indexPath, "aa");
	}
	
	/**
	 * 创建索引
	 * @param dirPath       需要读取的文件所在文件目录
	 * @param indexPath     索引存放目录
	 * @param fileName      文件名称
	 * @throws IOException
	 */
	public static void createIndex(String dirPath, String indexPath, String fileId, String fileName) throws IOException {
		createIndex(dirPath, indexPath, fileId, fileName, true);
	}
	
	/**
	 * 创建索引
	 * @param dirPath         需要读取的文件所在文件目录
	 * @param indexPath       索引存放目录
	 * @param createOrAppend  始终重建索引/不存在则追加索引
	 * @throws IOException
	 */
	public static void createIndex(String dirPath, String indexPath, String fileId, String fileName,
			boolean createOrAppend) throws IOException {
		long start = System.currentTimeMillis();
		Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
		Path docDirPath = Paths.get(dirPath, new String[0]);
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
	    indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter writer = new IndexWriter(dir, indexWriterConfig);
		try {
			analyJump(writer, docDirPath, fileId, fileName, dirPath);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            writer.close();
        }
		long end = System.currentTimeMillis();
		logger.info("Time consumed:" + (end - start) + " ms");
	}
	
	/**
	 * 删除索引
	 * @param indexPath 索引地址
	 * @param fileId    文件ID
	 * @throws IOException  
	 * void 
	 * @exception
	 */
   public static void deleteIndex(String indexPath, String fileId) throws IOException {
        long start = System.currentTimeMillis();
        Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, indexWriterConfig);
        try {
            LuceneUtils.deleteIndex(writer, "id", fileId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            writer.close();
        }
        long end = System.currentTimeMillis();
        logger.info("Time consumed:" + (end - start) + " ms");
    }
   
	/**
	 * 
	 * @param writer
	 *            索引写入器
	 * @param path
	 *            文件路径
	 * @throws IOException
	 */
	public static void analyJump(final IndexWriter writer, Path path, final String fileId, final String fileName ,final String dirPath)
			throws IOException {
		// 如果是目录，查找目录下的文件
		if (Files.isDirectory(path, new LinkOption[0])) {
			Files.walkFileTree(path, new SimpleFileVisitor<Object>() {
				@Override
				public FileVisitResult visitFile(Object file,
						BasicFileAttributes attrs) throws IOException {
					Path path = (Path)file;
					indexDoc(writer, path, fileId, fileName, attrs.lastModifiedTime().toMillis(), dirPath);
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(writer, path, fileId, fileName, Files.getLastModifiedTime(path, new LinkOption[0]).toMillis(), dirPath);
		}
	}

	/**
	 * 读取文件创建索引
	 * 
	 * @param writer
	 *            索引写入器
	 * @param file
	 *            文件路径
	 * @param lastModified
	 *            文件最后一次修改时间
	 * @throws IOException
	 */
	public static void indexDoc(IndexWriter writer, Path file, String fileId, String fileName, long lastModified, String dirPath)
			throws IOException {
		Document doc = new Document();
		String content = "";
		try {
			//创建的索引分为可使用文本文档读取的类型和excel类型
			if("xls".equals(FileUtil.getFileNameSuffix(fileName))){
				//excel2003
				content = FileUtil.readExcel(dirPath);
			}
			if("xlsx".equals(FileUtil.getFileNameSuffix(fileName))){
				//excel2007
				content = FileUtil.readExcel2007(dirPath);
			}
			else {
				//文本文档读取类型
				content = FileUtil.readDoc(file.toString());
			}
			doc.add(new TextField("path", file.getFileName().toString(), Field.Store.YES));
            doc.add(new TextField("content", content, Field.Store.YES));
            doc.add(new TextField("name", fileName, Field.Store.YES));
            doc.add(new TextField("id", fileId, Field.Store.YES));
    		//doc.add(new TextField("content", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
    		if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
    			writer.addDocument(doc);
    			//writer.addDocuments(null);
    		} else {
    		   writer.updateDocument(new Term("id", fileId), doc);
    		}
    		writer.commit();
		} catch (Exception e) {
		    writer.close();
        }
	}
}
