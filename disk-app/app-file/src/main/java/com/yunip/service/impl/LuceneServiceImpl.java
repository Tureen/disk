/*
 * 描述：〈全文检索实现类〉
 * 创建人：can.du
 * 创建时间：2016-8-8
 */
package com.yunip.service.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.yunip.mapper.disk.IFileDao;
import com.yunip.mapper.disk.IFolderDao;
import com.yunip.model.disk.File;
import com.yunip.model.disk.Folder;
import com.yunip.service.ILuceneService;
import com.yunip.utils.lucene.HighlighterParam;
import com.yunip.utils.lucene.LuceneUtils;
import com.yunip.utils.lucene.Page;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 全文检索实现类
 */
@Service("iLuceneService")
public class LuceneServiceImpl implements ILuceneService{

    @Resource(name = "iFolderDao")
    private IFolderDao folderDao;
    
    @Resource(name = "iFileDao")
    private IFileDao fileDao;
    
    @SuppressWarnings("resource")
    @Override
    @Transactional
    public Page<Object> getPageByQuery(Page<Object> page) throws Exception {
        Analyzer analyzer = new IKAnalyzer();
        Directory directory = null;
        IndexReader reader = null;
        try{
            directory = FSDirectory.open(Paths.get(page.getQueryIndexDir()));
            reader = DirectoryReader.open(directory);
        } catch (Exception exception){
            return page;
        }
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        IndexSearcher searcher = new IndexSearcher(reader);
        String[] queries = { "name", "content" };
        Query query = new MultiFieldQueryParser(queries, analyzer).parse(page.getQueryValue());
        HighlighterParam highlighterParam = new HighlighterParam();
        highlighterParam.setHighlight(true);
        highlighterParam.setFieldNames(queries);
        highlighterParam.setFragmenterLength(350);
        List<Document> documents = LuceneUtils.pageQuery(searcher, directory, query, page, highlighterParam, writerConfig);
        List<File> files = new ArrayList<File>(); 
        if(documents != null && documents.size() > 0){
           for(Document document : documents){
               File file = new File();
               file.setFileName(document.get("name"));
               file.setContent(document.get("content"));
               file.setId(Integer.parseInt(document.get("id")));
               file.setFileSuffix(StringUtil.HtmlText(FileUtil.getFileNameSuffix(file.getFileName())));
               //查询文档信息
               files.add(file);
               File dataFile = fileDao.selectById(file.getId());
               if(dataFile != null){
                  //用户判断文档是否删除
                  file.setFilePath(dataFile.getFilePath());
                  //查询出文件列表
                  List<Folder> folders = getParentFolders(dataFile.getFolderId());
                  file.setParentFolders(folders);
               }
           } 
        }
        page.setItems(files);
        return page;
    }

    public List<Folder> getParentFolders(int folderId) {
        List<Folder> folders = new ArrayList<Folder>();
        Folder folder = folderDao.selectById(folderId);
        if (folder != null) {
            folders.add(folder);
        }
        getParentFolder(folders, folder);
        Collections.reverse(folders);
        return folders;
    }

    public void getParentFolder(List<Folder> folders, Folder folder) {
        if (folder != null && folder.getParentId() != 0) {
            Folder paFolder = folderDao.selectById(folder.getParentId());
            folders.add(paFolder);
            getParentFolder(folders, paFolder);
        }
    }

}
