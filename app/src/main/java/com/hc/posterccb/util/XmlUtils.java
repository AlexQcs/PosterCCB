package com.hc.posterccb.util;


import android.util.Xml;

import com.hc.posterccb.Constant;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.PollResultBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.TempBean;
import com.hc.posterccb.bean.polling.UpGradeBean;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by alex on 2017/6/30.
 */

public class XmlUtils {

    /**
     * 解析一个bean和一个list的xml文件结构的方法
     *
     * @param parser
     *         解析者
     * @param listRoot
     *         内层ListBean需要实例化对象的一个标识
     * @param listClazz
     *         ListBean.class
     * @param beanRoot
     *         外层Bean需要实例化对象的一个标识
     * @param beanClazz
     *         Bean.class
     * @return 一个bean和一个list的结果
     * @throws Exception
     */
    public static <T, T1> PostResult<T> getBeanByParseXml(XmlPullParser parser, String listRoot, Class<T> listClazz, String beanRoot, Class<T1> beanClazz) throws Exception {
        //最后结果
        PostResult<T> result = null;
        //list  存放一堆item
        ArrayList<T> list = null;
        //内层ListBean
        T t = null;
        //外层Bean
        T1 bean = null;
        //一个计数器
        int count = 0;
        try {
            //获得当前标签类型
            int eventType = parser.getEventType();
            //如果不是xml文件结束标签，则一个一个向下解析
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //如果是xml文件开始标签，则初始化一些数据
                    case XmlPullParser.START_DOCUMENT:
                        //最后的结果
                        result = new PostResult<T>();
                        //list
                        list = new ArrayList<T>();
                        //将list加入到result中，当前list是空的，等后面加入了数据后，就不是空了
                        result.setList(list);
                        break;
                    //开始标签
                    case XmlPullParser.START_TAG:
                        //获得标签的名字
                        String tagName = parser.getName();
                        //如果内层的ListBean已经实例化出来的话
                        if (t != null) {
                            try {
                                //判断当前标签在没在ListBean的属性中
                                Field field = listClazz.getField(tagName);
                                //如果ListBean中有当前标签
                                if (field != null) {
                                    //计数器+1
                                    count++;
                                    //将取出来的值赋给ListBean中对应的属性
                                    field.set(t, parser.nextText());
                                }
                            } catch (Exception e) {
                                //如果ListBean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走

                            }
                            //如果外层的Bean已经实例化出来的话
                        } else if (bean != null) {
                            try {
                                //判断当前标签在没在Bean的属性中
                                Field field = beanClazz.getField(tagName);
                                //如果Bean中有当前标签
                                if (field != null) {
                                    //计数器+1
                                    count++;
                                    //将取出来的值赋给Bean中对应的属性
                                    field.set(bean, parser.nextText());
                                }
                            } catch (Exception e) {
                                //如果Bean中没有当前标签，则会直接跳到这里，什么都不执行，然后再继续往下走
                            }
                        }
                        //如果当前标签为我们传入的内层根标签，说明ListBean需要实例化出来了
                        if (tagName.equals(listRoot)) {
                            //将ListBean实例化出来
                            t = listClazz.newInstance();
                        }
                        //如果当前标签为我们传入的内层根标签，说明Bean需要实例化出来了
                        if (tagName.equals(beanRoot)) {
                            //将Bean实例化出来
                            bean = beanClazz.newInstance();
                        }
                        break;
                    //结束标签
                    case XmlPullParser.END_TAG:
                        //如果当前标签为</item>
                        if (listRoot.equalsIgnoreCase(parser.getName())) {
                            //如果ListBean不为空
                            if (t != null) {
                                //保存到list中，同时也保存到了result中，因为list已经是保存在result中了，
                                //只不过刚才没有值，现在有值了
                                list.add(t);
                                //并且把ListBean置空，因为后续还有好多个item
                                t = null;
                            }
                            //如果当前标签为</root>
                        } else if (beanRoot.equalsIgnoreCase(parser.getName())) {
                            //将Bean保存到result中
                            result.setBean(bean);
                        }
                        break;
                }
                //移动到下一个标签
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果计数器为0说明没有解析到任何数据
        if (count == 0) {
            //将result置空就可以了
            result = null;
        }
        //将result返回
        return result;

    }


    public static String getXmlType(String xmlStr) {
        String xmlType = "no";
        try {
            // 由android.util.Xml创建一个XmlPullParser实例
            InputStream in = new ByteArrayInputStream( xmlStr.getBytes());
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            //产生第一个事件
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals(Constant.TASKTYPE)) {

                            eventType = parser.next();
                            xmlType = parser.getText().trim();
                            return xmlType;
                            //根据类型返回指定字节
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlType;
    }


    public static PostResult getTaskBean(String tasktype,String xmlStr){

        PostResult postResult = null;
        try {
            InputStream in = new ByteArrayInputStream( xmlStr.getBytes());
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            switch (tasktype) {
                case Constant.POLLING_PROGRAM:
                    LogUtils.e("XmlUtils", "检测到数据类型为播放类任务");
                    postResult=getBeanByParseXml(parser,Constant.XML_LISTTAG, ProgramBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_CONTROL:
                    LogUtils.e("XmlUtils", "检测到数据类型为控制类任务");
                    postResult=getBeanByParseXml(parser,Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, ControlBean.class);
                    break;
                case Constant.POLLING_UPGRADE:
                    LogUtils.e("XmlUtils", "检测到数据类型为升级类任务");
                    postResult=getBeanByParseXml(parser,Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, UpGradeBean.class);
                    break;
                case Constant.POLLING_REALTIMEMSG:
                    LogUtils.e("XmlUtils", "检测到数据类型为即时消息类任务");
                    postResult=getBeanByParseXml(parser,Constant.XML_LISTTAG,TempBean.class,Constant.XML_STARTDOM, RealTimeMsgBean.class);
                    break;
            }
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResult;
    }



}
