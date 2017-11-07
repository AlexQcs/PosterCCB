package com.hc.posterccb.util.system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by alex on 2017/11/1.
 */

public class GetMac {

    public static String mac() {
        String mac=null;
        String str = "";
        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    mac = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return mac;
    }
}
