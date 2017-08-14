package com.hc.posterccb.util.file;

import android.util.Log;

import com.hc.posterccb.Constant;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 2017/7/17.
 */

public class FileUtils {


    public static boolean isExist(String path) {
        File file = new File(path);
        return (file.isFile() && file.exists());
    }

    // 读在/mnt/sdcard/目录下面的文件

    public static String readFileSdcard(String fileName) {

        String res = "";

        try {

            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];

            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return res;

    }

    /**
     * 此方法用于:创建新文件夹
     *
     * @param path
     *         文件路径
     * @author.Alex.on.2017年6月10日
     */
    public static void folderCreate(String path) {
        File dirFirstFolder = new File(path);//方法二：通过变量文件来获取需要创建的文件夹名字
        if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
            dirFirstFolder.mkdirs();//创建文件夹]
        }
    }

    /**
     * 此方法用于:创建新文件
     *
     * @param path
     *         文件路径
     * @author.Alex.on.2017年6月10日
     */
    public static void fileCreate(String path) {
        File file = new File(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
                //file is create
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void fileDelete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath
     *         文件路径
     * @param sizeType
     *         获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath
     *         文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public static void checkAppFile() {

        try {
            folderCreate(Constant.LOCAL_FILE_PATH);
            folderCreate(Constant.LOCAL_LOG_PATH);
            folderCreate(Constant.LOCAL_PROGRAM_PATH);
            folderCreate(Constant.LOCAL_PROGRAM_MEDIACFG_PATH);
            fileCreate(Constant.LOCAL_ERROR_TXT);
            fileCreate(Constant.LOCAL_CONFIG_TXT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 将字符串追加写入到文本文件中
    public static void additionTxtToFile(String strcontent, String filePath) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "hh:mm:ss   ");
            datetime = tempDate.format(new Date()).toString();
            fw = new FileWriter(filePath, true);
            bw = new BufferedWriter(fw);
            String myreadline = datetime + strcontent;
            bw.write(myreadline + "\r\n");
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {

            }
        }
    }

    // 将字符串覆盖写入到文本文件中
    public static void coverTxtToFile(String strcontent, String filePath) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(filePath, false);
            bw = new BufferedWriter(fw);
            bw.write(strcontent);
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {

            }
        }


    }

    //读取txt文件内容
    public static String getStringFromTxT(String path) {
        String str = "";
        File urlFile = new File(path);
        if (!urlFile.exists()) return str;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine = null;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str + mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 用于获取文件夹下面的子文件列表
     *
     * @param dirPath
     *         目标文件夹
     * @return List<String>
     */

    public static List<String> getPathOfDirectory(String dirPath) {
        List<String> pathList = null;
        File file = new File(dirPath);
        if (file.exists()) {
            pathList = new ArrayList<String>();
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    pathList.add(tempList[i].getPath());
                }
            }
        }
        return pathList;
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public List<String> DecFile(String path) throws IOException {
        List<String> strings = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String[] decStr = line.split("。");
            if (decStr.length >= 2)
                strings.add(decStr[1]);
        }

        br.close();
        return strings;
    }

    public static void CreatText() throws IOException{

        File file=new File(Constant.CREAT_DES_FILE_PATH);
        if (!file.exists()){
            try{
                file.mkdirs();
            }catch (Exception e){

            }
        }
        File dir=new File(Constant.CREAT_DES_FILE);
        Log.e("验证文件目录",dir.getPath()+"");
        Log.e("在data目录下是否存在文件",dir.exists()+"");
        if (!dir.exists()){
            try {
                boolean iscreat=dir.createNewFile();
                Log.e("在data目录下新增文件",iscreat+"");
            }catch (Exception e){

            }
        }
    }

    public static void writSerialTxt(String seriaStr){
        FileWriter fw=null;
        BufferedWriter bw=null;
        String datetime="";
        try{
            SimpleDateFormat tempDate=new SimpleDateFormat("yyyy-MM-dd"+" "+"hh:mm:ss");
            datetime=tempDate.format(new Date()).toString();
            fw=new FileWriter(Constant.CREAT_DES_FILE,true);

            bw=new BufferedWriter(fw);

            String myreadline=seriaStr+"\r\n"+datetime;

            bw.write(myreadline+"\r\n");
            bw.newLine();
            bw.flush();
            bw.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            }catch (IOException e1){

            }
        }
    }
}
