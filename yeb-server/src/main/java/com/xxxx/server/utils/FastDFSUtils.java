package com.xxxx.server.utils;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Name;
import java.io.*;

/**
 * @description: FastDFS工具类
 * @author: 吉祥
 * @created: 2021/11/10 11:40
 */
public class FastDFSUtils {

    //开启日志打印
    private static Logger LOGGER = LoggerFactory.getLogger(FastDFSUtils.class);

    /**
     * 初始化客户端
     * ClientGlobal.init 读取配置文件，并初始化对应属性
     */
    static {
        try {
            //获取配置文件路径
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            //初始化FastDFS
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            LOGGER.error("初始化FastDFS失败",e);
        }
    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage =null;
        try {
            trackerServer = getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
            return "http://"+storeStorage.getInetSocketAddress().getHostString();
        } catch (Exception e) {
            LOGGER.error("获取文件路径失败",e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
        } catch (Exception e) {
            LOGGER.error("删除文件失败",e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream download(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return inputStream;
        }catch (Exception e){
            LOGGER.error("文件下载失败",e.getMessage());
        }
        return null;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            return storageClient.get_file_info(groupName,remoteFileName);
        }catch (Exception e){
            LOGGER.error("获取文件信息失败",e.getMessage());
        }
        return null;
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        //获取文件名
        String filename = file.getOriginalFilename();
        LOGGER.info("文件名："+filename);
        StorageClient storageClient = null;
        String[] result = {};
        try{
            //获取StorageClient
            storageClient = getStorageClient();
            //上传文件
            result = storageClient.upload_file(file.getBytes(), filename.substring(filename.lastIndexOf(".") + 1), null);
        }catch (Exception e){
            LOGGER.error("文件上传失败",e);
        }
        if (result.length == 0){
            LOGGER.error("上传文件失败",storageClient.getErrorCode());
        }
        return result;
    }

    /**
     * 生成StorageClient客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 生成TrackerServer服务器
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return trackerServer;
    }
}

