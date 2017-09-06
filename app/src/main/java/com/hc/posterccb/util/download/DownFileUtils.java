package com.hc.posterccb.util.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/10/27 0027 15:56
 */
public class DownFileUtils {


    public static File createFile(Context context){


        File file=null;
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){

            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.apk");
        }else {
            file = new File(context.getCacheDir().getAbsolutePath()+"/test.apk");
        }

        Log.e("vivi","file "+file.getAbsolutePath());

        return file;

    }




    public static void writeFile2Disk(ResponseBody body, File file){


        long currentLength = 0;
        OutputStream os =null;

        InputStream is = body.byteStream();
//        long totalLength =response.body().contentLength();

        try {

            os = new FileOutputStream(file);


            int len ;

            byte [] buff = new byte[1024];

            while((len=is.read(buff))!=-1){

                os.write(buff,0,len);
                currentLength+=len;
                Log.e("vivi","当前进度:"+currentLength);

            }
           // httpCallBack.onLoading(currentLength,totalLength,true);


        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }


    public static String getResourceName(String path){
        String[] array=path.split("/");
        return array[array.length-1];
    }




}
