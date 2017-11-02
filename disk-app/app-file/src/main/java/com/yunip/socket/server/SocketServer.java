package com.yunip.socket.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.UploadType;
import com.yunip.enums.teamwork.FileType;
import com.yunip.manage.FileManager;
import com.yunip.model.teamwork.TeamworkFile;
import com.yunip.service.IFileEncryptDecryptService;
import com.yunip.util.SpringContextUtil;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.FileUtil;
import com.yunip.utils.util.ImgCompress;

public class SocketServer {
    private FileManager fileManager = SpringContextUtil.getBean("fileManager");
    
    private IFileEncryptDecryptService fileEncryptDecryptService = SpringContextUtil.getBean("iFileEncryptDecryptService");
    /**
     * 上传文件的缓存目录
     */
    //private String uploadTempPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR;
    
    /**
     * 表示上传状态(成功)
     */
    private final String SUCCESS = "success";
    
    /**
     * 表示上传状态(失败)
     */
    private final String FAIL = "fail";
    
    /**
     * 上传文件的正式目录
     */
    //private String uploadPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR;
    
    private ExecutorService executorService;// 线程池
    private ServerSocket ss = null;
    private int port;// 监听端口
    private boolean quit;// 是否退出
    private Map<Long, FileLog> datas = new HashMap<Long, FileLog>();// 存放断点数据，最好改为数据库存放

    public SocketServer(int port) {
        this.port = port;
        // 初始化线程池
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * 50);
    }

    // 启动服务
    public void start() throws Exception {
        ss = new ServerSocket(port);
        while (!quit) {
            Socket socket = ss.accept();// 接受客户端的请求
            // 为支持多用户并发访问，采用线程池管理每一个用户的连接请求
            executorService.execute(new SocketTask(socket));// 启动一个线程来处理请求
        }
    }

    // 退出
    public void quit() {
        this.quit = true;
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SocketServer server = new SocketServer(7878);
        server.start();
    }

    private class SocketTask implements Runnable {
        private Socket socket;

        public SocketTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //标识文件是否上传完成
            boolean isUploadFinish = false; 
            PushbackInputStream inStream = null;
            PrintWriter printWriter = null;
            String toFilePath = "";//上传文件保存路径
            
            try {
                String clientIp = socket.getInetAddress().getHostAddress();
                System.out.println("accepted connenction from " + clientIp + " @ " + socket.getPort());
                inStream = new PushbackInputStream(socket.getInputStream());
                // 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;sourceid=
                // 如果用户初次上传文件，sourceid的值为空。
                BufferedReader br = new BufferedReader(new InputStreamReader(inStream, SystemContant.encodeType));
                String head = br.readLine();
                System.out.println("参数：" + head);
                
                if (head != null) {
                    // 下面从协议数据中读取各种参数值
                    String[] items = head.split(";");
                    String filelength = items[0].substring(items[0].indexOf("=") + 1);
                    String filename = items[1].substring(items[1].indexOf("=") + 1);
                    String sourceid = items[2].substring(items[2].indexOf("=") + 1);
                    //String uploadType = items[3].substring(items[3].indexOf("=") + 1);
                    String folderId = items[4].substring(items[4].indexOf("=") + 1);
                    String userId = items[5].substring(items[5].indexOf("=") + 1);
                    String fileType = items[6].substring(items[6].indexOf("=") + 1);
                    String teamworkId = "";
                    if((""+FileType.IS_TEAMWORK_FILE.getType()).equals(fileType)){
                        teamworkId = items[7].substring(items[7].indexOf("=") + 1);
                    }
                    //文件目录
                    String fileDir = sourceid;
                    Long id = System.currentTimeMillis();
                    FileLog log = null;
                    if (null != sourceid && !"".equals(sourceid)) {
                        id = Long.valueOf(sourceid);
                        log = find(id);//查找上传的文件是否存在上传记录
                    }else{
                        fileDir = String.valueOf(id);
                    }
                    String tempDir = Constant.ROOTPATH + File.separator + Constant.UPLOAD_TEMP_DIR + File.separator + Md5.encodeByMd5(fileDir);
                    File file = null;
                    int position = 0;
                    if(log==null){//如果上传的文件不存在上传记录,为文件添加跟踪记录
                        @SuppressWarnings("unused")
                        String path = new SimpleDateFormat("yyyy/MM/dd/HH/mm").format(new Date());
                        File dir = new File(tempDir);
                        if(!dir.exists()) dir.mkdirs();
                        file = new File(dir, filename);
                        if(file.exists()){//如果上传的文件发生重名，然后进行改名
                            filename = filename.substring(0, filename.indexOf(".")-1)+ dir.listFiles().length+ filename.substring(filename.indexOf("."));
                            file = new File(dir, filename);
                        }
                        save(id, file);
                    }else{// 如果上传的文件存在上传记录,读取上次的断点位置
                        file = new File(log.getPath());//从上传记录中得到文件的路径
                        if(file.exists()){
                            File logFile = new File(file.getParentFile(), file.getName()+".log");
                            if(logFile.exists()){
                                Properties properties = new Properties();
                                properties.load(new FileInputStream(logFile));
                                position = Integer.valueOf(properties.getProperty("length"));//读取断点位置
                            }
                        }
                    }
                    
                    printWriter = new PrintWriter(socket.getOutputStream());
                    //OutputStream outStream = socket.getOutputStream();
                    String response = "sourceid="+ id+ ";position=" + position;
                    //服务器收到客户端的请求信息后，给客户端返回响应信息：sourceid=1274773833264;position=0
                    //sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
                    //outStream.write(response.getBytes());
                    printWriter.println(response);
                    printWriter.flush();
                    
                    RandomAccessFile fileOutStream = new RandomAccessFile(file, "rwd");
                    if(position==0) fileOutStream.setLength(Integer.valueOf(filelength));//设置文件长度
                    fileOutStream.seek(position);//移动文件指定的位置开始写入数据
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = position;
                    while( (len=inStream.read(buffer)) != -1){//从输入流中读取数据写入到文件中
                        fileOutStream.write(buffer, 0, len);
                        length += len;
                        Properties properties = new Properties();
                        properties.put("length", String.valueOf(length));
                        FileOutputStream logFile = new FileOutputStream(new File(file.getParentFile(), file.getName()+".log"));
                        properties.store(logFile, null);//实时记录文件的最后保存位置
                        logFile.close();
                        /*if(length==fileOutStream.length()){//根据文件大小判断，上传完成后，不再继续接收
                            break;
                        }*/
                        if(length == Integer.parseInt(filelength)){//根据文件大小判断，上传完成后，不再继续接收
                            break;
                        }else if(length > Integer.parseInt(filelength)){
                            throw new Exception("接收文件大小超过原文件大小！");
                        }
                    }
                    if(length == Integer.parseInt(filelength)){
                        isUploadFinish = true;
                        delete(id);
                    }
                    fileOutStream.close();
                    //上传完成后复制上传的文件到个人目录下并删除缓存目录
                    System.out.println("是否上传完成：" + isUploadFinish);
                    if(isUploadFinish){
                        String fromFilePath = file.getPath();
                        String userDirPath = Constant.ROOTPATH + File.separator + Constant.UPLOAD_DIR + File.separator + userId;
                        String uploadFileName = UUID.randomUUID() + FileUtil.getFileNameExt(fromFilePath);
                        toFilePath = userDirPath + File.separator + uploadFileName;
                        //将上传的临时文件转移到正式目录下
                        File userDir = new File(userDirPath);
                        if(!userDir.exists()){
                            userDir.mkdirs();
                        }
                        FileUtil.copyFile(fromFilePath, toFilePath);
                        
                        File toFile = new File(toFilePath);
                        if(toFile.exists() && toFile.isFile()){//确定文件上传成功
                            if(Constant.thumbImageType.contains(FileUtil.getFileNameExt(uploadFileName).toLowerCase())){//上传文件的是图片，则生成一张缩略图
                                String thumbImagePath = userDirPath + File.separator + Constant.thumbImagePrefixName + uploadFileName;
                                ImgCompress imgCom = new ImgCompress(toFilePath, thumbImagePath);
                                imgCom.resizeFix(100, 100);
                            }
                            System.out.println("删除上传临时目录：" + tempDir);
                            FileUtil.deleteFolder(new File(tempDir));
                            
                            //TODO 
                            if((""+FileType.IS_TEAMWORK_FILE.getType()).equals(fileType)){
                                //模板将上传的协作文件保存到数据库
                                saveTeamworkFile(userId, folderId, filename, toFilePath, uploadFileName, clientIp, teamworkId);
                            }else{
                                //模板将上传的文件保存到数据库
                                saveFile(userId, folderId, filename, toFilePath, uploadFileName, clientIp);
                            }
                            //TODO 结束
                            
                            //TODO 回复APP上传状态
                            printWriter.println(SUCCESS);
                            printWriter.flush();
                        }
                    }
                    //outStream.close();
                    file = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //上传失败，删除上传的原文件
                FileUtil.deleteFile(toFilePath);
                printWriter.println(FAIL);
                printWriter.flush();
            } finally {
                try {
                    if(inStream != null){
                        inStream.close();
                    }
                    if(printWriter != null){
                        printWriter.close();
                    }
                    if(socket != null && !socket.isClosed()){
                        socket.close();
                    }
                } catch (IOException e) {}
            }
        }

    }
    
    //上传普通文件
    private void saveFile(String userId, String folderId, String filename, String toFilePath, String uploadFileName, String clientIp){
        com.yunip.model.disk.File myFile = new com.yunip.model.disk.File();
        myFile.setEmployeeId(Integer.parseInt(userId));
        myFile.setFolderId(Integer.parseInt(folderId));
        myFile.setFileName(filename);
        myFile.setFileSize(FileUtil.getFileSize(toFilePath));
        myFile.setFilePath(userId + File.separator + uploadFileName);//保存相对路径
        
        //查询缓存中版本文件保留数
        String fileVersionNum = SysConfigHelper.getValue(SystemContant.BASICSCODE, BasicsInfoCode.FILEVERSIONNUM.getKey());
        fileManager.saveFile(myFile, UploadType.OVER, Integer.parseInt(userId),Integer.valueOf(fileVersionNum), clientIp);
        //添加文件到待加密队列中
        fileEncryptDecryptService.addFileToEncryptQueue(myFile);
    }
    
    //上传协作文件
    private void saveTeamworkFile(String userId, String folderId, String filename, String toFilePath, String uploadFileName, String clientIp, String teamworkId){
        TeamworkFile myFile = new TeamworkFile();
        myFile.setEmployeeId(Integer.parseInt(userId));
        myFile.setFolderId(Integer.parseInt(folderId));
        myFile.setFileName(filename);
        myFile.setFileSize(FileUtil.getFileSize(toFilePath));
        myFile.setFilePath(userId + File.separator + uploadFileName);//保存相对路径
        myFile.setTeamworkId(Integer.parseInt(teamworkId));
        
        //查询缓存中版本文件保留数
        fileManager.saveTeamworkFile(myFile, UploadType.OVER, Integer.parseInt(userId), null, clientIp, null, null);
        //添加文件到待加密队列中
        fileEncryptDecryptService.addFileToEncryptQueue(myFile);
    }

    public FileLog find(Long sourceid) {
        return datas.get(sourceid);
    }

    // 保存上传记录
    public void save(Long id, File saveFile) {
        // 日后可以改成通过数据库存放
        datas.put(id, new FileLog(id, saveFile.getAbsolutePath()));
    }

    // 当文件上传完毕，删除记录
    public void delete(long sourceid) {
        if (datas.containsKey(sourceid))
            datas.remove(sourceid);
    }

    @SuppressWarnings("unused")
    private class FileLog {
        private Long id;
        private String path;
        
        public FileLog(Long id, String path) {
            super();
            this.id = id;
            this.path = path;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }
}
