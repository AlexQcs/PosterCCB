package com.hc.posterccb.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by alex on 2017/7/24.
 */

public class ControlUtils {



    // 写文件
    public static void writeFile(String str) throws IOException, InterruptedException {

        File file = new File("/sys/class/disp/disp/attr/sys");
        file.setExecutable(true);
        file.setReadable(true);//设置可读权限
        file.setWritable(true);//设置可写权限
        if (str.equals("0")) {
//            do_exec("busybox echo 0 > /sys/class/disp/disp/attr/sys");
            //关背光命令
            do_exec("busybox echo 0 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        }  else {
            //开背光命令
            do_exec("busybox echo 1 > /sys/devices/fb.11/graphics/fb0/pwr_bl");
        }
    }

    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            if ((su.waitFor() != 0)) {
                System.out.println("cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
