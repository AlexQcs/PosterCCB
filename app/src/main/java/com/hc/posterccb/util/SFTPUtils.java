package com.hc.posterccb.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by alex on 2017/7/5.
 */

public class SFTPUtils {
    private String TAG = "SFTPUTils";
    private String host;//地址
    private String username;//用户名
    private String password;
    private int port = 22;//端口
    private ChannelSftp mSftp = null;
    private Session sshSession = null;

    /**
     * ChannelSftp类是JSch实现SFTP核心类，它包含了所有SFTP的方法，如：
     * put()：      文件上传
     * get()：      文件下载
     * cd()：       进入指定目录
     * ls()：       得到指定目录下的文件列表
     * rename()：   重命名指定文件或目录
     * rm()：       删除指定文件
     * mkdir()：    创建目录
     * rmdir()：    删除目录
     */

    public SFTPUtils(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    //连接sftp服务器
    public ChannelSftp connect() {
        JSch jSch = new JSch();
        try {
            sshSession = jSch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.setProperty("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            if (channel != null) {
                channel.connect();
            } else {
                Log.e(TAG, "channel connecting failed.");
            }
            mSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return mSftp;
    }

    //断开服务器
    public void disconnect() {
        if (this.mSftp != null) {
            if (this.mSftp.isConnected()) {
                this.mSftp.disconnect();
                Log.d(TAG, "sftp is closed already");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                Log.d(TAG, "sshSession is closed already");
            }
        }
    }

    //单个文件上传
    public boolean uploadFile(String remotePath, String remoteFileName,
                              String localPath, String localFileName) {
        FileInputStream in=null;

        try {
            createDir(remotePath);
            Log.e(TAG,remotePath);
            File file=new File(localPath+localFileName);
            in=new FileInputStream(file);
            Log.e(TAG, String.valueOf(in));
            mSftp.put(in,remoteFileName);
            Log.e(TAG, String.valueOf(mSftp));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return false;
    }

    //多个文件上传
    public boolean bacthUploadFile(String remotePath,String localPath,boolean del){

        try{
            File file=new File(localPath);
            File[] files=file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()&&files[i].getName().indexOf("bak")==-1){
                    synchronized (remotePath){
                        if (this.uploadFile(remotePath,files[i].getName(),localPath,files[i].getName())&&del){
                            deleteFile(localPath+files[i].getName());
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.disconnect();
        }


        return false;
    }

    /**
     * 批量下载文件
     *
     * @param remotPath
     *            远程下载目录(以路径符号结束)
     * @param localPath
     *            本地保存目录(以路径符号结束)
     * @param fileFormat
     *            下载文件格式(以特定字符开头,为空不做检验)
     * @param del
     *            下载后是否删除sftp文件
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean batchDownLoadFile(String remotPath, String localPath,
                                     String fileFormat, boolean del) {
        try {
            connect();
            Vector v = listFiles(remotPath);
            if (v.size() > 0) {

                Iterator it = v.iterator();
                while (it.hasNext()) {
                    ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) it.next();
                    String filename = entry.getFilename();
                    SftpATTRS attrs = entry.getAttrs();
                    if (!attrs.isDir()) {
                        if (fileFormat != null && !"".equals(fileFormat.trim())) {
                            if (filename.startsWith(fileFormat)) {
                                if (this.downloadFile(remotPath, filename,
                                        localPath, filename)
                                        && del) {
                                    deleteSFTP(remotPath, filename);
                                }
                            }
                        } else {
                            if (this.downloadFile(remotPath, filename,
                                    localPath, filename)
                                    && del) {
                                deleteSFTP(remotPath, filename);
                            }
                        }
                    }
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
        return false;
    }

    /**
     * 单个文件下载
     * @param remotePath
     * @param remoteFileName
     * @param localPath
     * @param localFileName
     * @return
     */
    public boolean downloadFile(String remotePath, String remoteFileName,
                                String localPath, String localFileName) {
        try {
            mSftp.cd(remotePath);
            File file = new File(localPath + localFileName);
            mkdirs(localPath + localFileName);
            mSftp.get(remoteFileName, new FileOutputStream(file));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }
        return file.delete();
    }

    public boolean createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.mSftp.cd(createpath);
                Log.d(TAG,createpath);
                return true;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(createpath)) {
                    mSftp.cd(createpath);
                } else {
                    mSftp.mkdir(createpath);
                    mSftp.cd(createpath);
                }
            }
            this.mSftp.cd(createpath);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断目录是否存在
     * @param directory
     * @return
     */
    @SuppressLint("DefaultLocale")
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = mSftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    public void deleteSFTP(String directory, String deleteFile) {
        try {
            mSftp.cd(directory);
            mSftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     * @param path
     */
    public void mkdirs(String path) {
        File f = new File(path);
        String fs = f.getParent();
        f = new File(fs);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 列出目录文件
     * @param directory
     * @return
     * @throws SftpException
     */

    @SuppressWarnings("rawtypes")
    public Vector listFiles(String directory) throws SftpException {
        return mSftp.ls(directory);
    }




}
