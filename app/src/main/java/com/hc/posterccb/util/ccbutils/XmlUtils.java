package com.hc.posterccb.util.ccbutils;


import android.util.Log;
import android.util.Xml;

import com.hc.posterccb.Constant;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.UpGradeCfgBean;
import com.hc.posterccb.bean.polling.AbstractBeanTaskItem;
import com.hc.posterccb.bean.polling.BasePollingBean;
import com.hc.posterccb.bean.polling.ConfigBean;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.ControlProgramBean;
import com.hc.posterccb.bean.polling.DownLoadFileBean;
import com.hc.posterccb.bean.polling.ItemBeanConfig;
import com.hc.posterccb.bean.polling.ItemBeanControl;
import com.hc.posterccb.bean.polling.ItemBeanControlProgram;
import com.hc.posterccb.bean.polling.ItemBeanDownLoadFile;
import com.hc.posterccb.bean.polling.ItemBeanLogReport;
import com.hc.posterccb.bean.polling.ItemBeanProgram;
import com.hc.posterccb.bean.polling.ItemBeanRealTimeMsg;
import com.hc.posterccb.bean.polling.ItemBeanUpGrade;
import com.hc.posterccb.bean.polling.LogReportBean;
import com.hc.posterccb.bean.polling.PollResultBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.SyncTimeBean;
import com.hc.posterccb.bean.polling.TempBean;
import com.hc.posterccb.bean.polling.UpGradeBean;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.bean.resource.ResourceBean;
import com.hc.posterccb.util.LogUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 此方法用于: 获取轮询响应的任务类型
     *
     * @param xmlStr
     *         xml字符串
     * @return xml类型
     * @throws Exception
     * @author.Alex.on.2017年7月26日08:57:38
     */
    public static ArrayList<String> getXmlType(String xmlStr) {
        ArrayList<String> list = new ArrayList<>();
        String xmlType = "no";
        try {
            // 由android.util.Xml创建一个XmlPullParser实例
            InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
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
                            xmlType = parser.nextText().trim();
                            list.add(xmlType);
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
        return list;
    }

    /**
     * 此方法用于:
     *
     * @param tasktype
     *         任务类型
     * @param xmlStr
     *         xml字符串
     * @return 轮询响应任务
     * @throws Exception
     * @author.Alex.on.2017年7月26日09:03:16
     */
    public static PostResult getTaskBean(String tasktype, String xmlStr) {

        PostResult postResult = null;
        try {
            InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "UTF-8");
            switch (tasktype) {
                case Constant.POLLING_PROGRAM:
                    LogUtils.e("XmlUtils", "检测到数据类型为播放类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, ProgramBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_CONTROL:
                    LogUtils.e("XmlUtils", "检测到数据类型为控制类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, ControlBean.class);
                    break;
                case Constant.POLLING_UPGRADE:
                    LogUtils.e("XmlUtils", "检测到数据类型为升级类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, UpGradeBean.class);
                    break;
                case Constant.POLLING_REALTIMEMSG:
                    LogUtils.e("XmlUtils", "检测到数据类型为即时消息类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, RealTimeMsgBean.class);
                    break;
                case Constant.POLLING_CANCELREALTIMEMSG:
                    LogUtils.e("XmlUtils", "检测到数据类型为取消即时消息类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_CONFIG:
                    LogUtils.e("XmlUtils", "检测到数据类型为终端配置类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, ConfigBean.class);
                    break;
                case Constant.POLLING_CONTROLPROGRAM:
                    LogUtils.e("XmlUtils", "检测到数据类型为控制类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, ControlProgramBean.class);
                    break;
                case Constant.POLLING_CFGREPORT:
                    LogUtils.e("XmlUtils", "检测到数据类型为终端配置信息日志上报类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_WORKSTATUSREPORT:
                    LogUtils.e("XmlUtils", "检测到数据类型为终端工作状态上报类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_MONITORREPORT:
                    LogUtils.e("XmlUtils", "检测到数据类型为终端在播内容上报类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_LOGREPORT:
                    LogUtils.e("XmlUtils", "检测到数据类型为终端日志上报类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, LogReportBean.class);
                    break;
                case Constant.POLLING_DOWNLOADRES:
                    LogUtils.e("XmlUtils", "检测到数据类型为通知终端下载资源文件类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, DownLoadFileBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
                case Constant.POLLING_DOWNLOADSTATUSREPORT:
                    LogUtils.e("XmlUtils", "检测到数据类型为通知终端上报资源下载状态类任务");
                    postResult = getBeanByParseXml(parser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, PollResultBean.class);
                    break;
            }
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResult;
    }

    /**
     * 此方法用于: 解析升级xml
     *
     * @param xmlStr
     *         播放列表xml
     * @return 升级配置
     * @author.Alex.on.2017年7月26日09:01:23
     * @throw XmlPullParserException、IOException
     */
    public static ArrayList<UpGradeCfgBean> parseUpGradeXml(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        ArrayList<UpGradeCfgBean> resultArray = new ArrayList<UpGradeCfgBean>();
        UpGradeCfgBean upGradeBean = null;
        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if (tagName.equals("file")) {

                            upGradeBean = new UpGradeCfgBean();
                            upGradeBean.setResid(parser.getAttributeValue(null, "resid"));
                            upGradeBean.setHref(parser.getAttributeValue(null, "href"));
                            upGradeBean.setMd5(parser.getAttributeValue(null, "md5"));
                            upGradeBean.setFilesize(parser.getAttributeValue(null, "filesize"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("file")) {
                            if (upGradeBean != null) {
                                resultArray.add(upGradeBean);
                            }
                            upGradeBean = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resultArray;
    }

    /**
     * 此方法用于: 获取正常播放列表
     *
     * @param xmlStr
     *         播放列表xml
     * @return 播放列表
     * @author.Alex.on.2017年7月26日09:01:23
     * @throw XmlPullParserException、IOException
     */
    public static ArrayList<Program> parseNormalProgramXml(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        ArrayList<Program> programArray = new ArrayList<Program>();
        Program program = null;
        ArrayList<ProgramRes> programResArrayList = new ArrayList<>();
        ProgramRes programRes = null;

        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if (tagName.equals("defaultpls") || tagName.equals("pls")) {

                            program = new Program();
                            programResArrayList = new ArrayList<>();
                            program.setType(tagName);
                            program.setAreatype(parser.getAttributeValue(null, "areatype"));
                            program.setStdtime(parser.getAttributeValue(null, "stdtime"));
                            program.setEdtime(parser.getAttributeValue(null, "edtime"));
                        } else if (tagName.equals("res")) {

                            programRes = new ProgramRes();
                            programRes.setResnam(parser.getAttributeValue(null, "resname"));
                            programRes.setResid(parser.getAttributeValue(null, "resid"));
                            programRes.setArea(parser.getAttributeValue(null, "area"));
                            programRes.setStdtime(parser.getAttributeValue(null, "stdtime"));
                            programRes.setEdtime(parser.getAttributeValue(null, "edtime"));
                            programRes.setPriority(parser.getAttributeValue(null, "priority"));
                            programRes.setPlaycnt(parser.getAttributeValue(null, "playcnt"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("res")) {
                            if (programRes != null) {
                                programResArrayList.add(programRes);
                            }
                            programRes = null;
                        } else if (parser.getName().equalsIgnoreCase("defaultpls") || parser.getName().equalsIgnoreCase("pls")) {
                            if (program != null) {
                                program.setList(programResArrayList);
                                programArray.add(program);
                                program = null;
                            }
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return programArray;
    }

    /**
     * 此方法用于: 获取插播播放列表
     *
     * @param xmlStr
     *         播放列表xml
     * @return 播放列表
     * @author.Alex.on.2017年7月26日09:01:23
     * @throw XmlPullParserException、IOException
     */
    public static Program parseInterProgramXml(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        ArrayList<Program> programArray = new ArrayList<Program>();
        Program program = null;
        ArrayList<ProgramRes> programResArrayList = new ArrayList<>();
        ProgramRes programRes = null;

        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if (tagName.equals("defaultpls") || tagName.equals("pls")) {

                            program = new Program();
                            programResArrayList = new ArrayList<>();
                            program.setType(tagName);
                            program.setAreatype(parser.getAttributeValue(null, "areatype"));
                            program.setStdtime(parser.getAttributeValue(null, "stdtime"));
                            program.setEdtime(parser.getAttributeValue(null, "edtime"));
                        } else if (tagName.equals("res")) {

                            programRes = new ProgramRes();
                            programRes.setResnam(parser.getAttributeValue(null, "resname"));
                            programRes.setResid(parser.getAttributeValue(null, "resid"));
                            programRes.setArea(parser.getAttributeValue(null, "area"));
                            programRes.setStdtime(parser.getAttributeValue(null, "stdtime"));
                            programRes.setEdtime(parser.getAttributeValue(null, "edtime"));
                            programRes.setPriority(parser.getAttributeValue(null, "priority"));
                            programRes.setPlaycnt(parser.getAttributeValue(null, "playcnt"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("res")) {
                            if (programRes != null) {
                                programResArrayList.add(programRes);
                            }
                            programRes = null;
                        } else if (parser.getName().equalsIgnoreCase("defaultpls") || parser.getName().equalsIgnoreCase("pls")) {
                            if (program != null) {
                                program.setList(programResArrayList);
                                programArray.add(program);
                                program = null;
                            }
                        }
//                        if (parser.getName().equalsIgnoreCase("res")) {
//                            if (programRes != null) {
//                                programResArrayList.add(programRes);
//                            }
//                            programRes = null;
//                        } else if (parser.getName().equalsIgnoreCase("defaultpls") || parser.getName().equalsIgnoreCase("pls")) {
//                            if (program != null) {
//                                program.setList(programResArrayList);
//                            }
//                        }
//                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return program;
    }

    //时间同步
    public static SyncTimeBean parseSyncTimeXml(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        SyncTimeBean syncTimeBean = null;

        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if (tagName.equals("servertime")) {
                            syncTimeBean = new SyncTimeBean();
                            syncTimeBean.setServertime(parser.nextText());
                        } else if (tagName.equals("result")) {
                            syncTimeBean = new SyncTimeBean();
                            syncTimeBean.setResult(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return syncTimeBean;
    }

    public static ArrayList<ResourceBean> parseResource(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        ArrayList<ResourceBean> programResArrayList = new ArrayList<>();

        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if (tagName.equals("model") || tagName.equals("bgimg") || tagName.equals("file")) {
                            ResourceBean resource = new ResourceBean();
                            resource.setType(tagName);
                            resource.setFilesize(parser.getAttributeValue(null, "filesize"));
                            resource.setMd5(parser.getAttributeValue(null, "md5"));
                            resource.setFtpAdd(parser.getAttributeValue(null, "ftpAdd"));
                            resource.setHref(parser.getAttributeValue(null, "href"));
                            resource.setResid(parser.getAttributeValue(null, "resid"));
                            resource.setResname(parser.getAttributeValue(null, "resname"));
                            programResArrayList.add(resource);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;
                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return programResArrayList;
    }

    public static XmlPullParser getXmlPullParser(String xmlStr) throws XmlPullParserException {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "UTF-8");
        return parser;
    }

    public static BasePollingBean parsePollingXml(String xmlStr) {
        InputStream in = new ByteArrayInputStream(xmlStr.getBytes());

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(in, "UTF-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        String tempTaskType = "";//接收到的任务类型

        BasePollingBean basePollingBean = null;
        List<AbstractBeanTaskItem> taskItemArrayList = new ArrayList<>();
        AbstractBeanTaskItem taskItem = null;
        boolean isItemEventStarted = false;
        ItemBeanProgram programBean = new ItemBeanProgram();
        List<ItemBeanProgram> programs = new ArrayList<>();
        ItemBeanControl controlBean = new ItemBeanControl();
        ItemBeanUpGrade upGradeBean = new ItemBeanUpGrade();
        ItemBeanRealTimeMsg realTimeMsgBean = new ItemBeanRealTimeMsg();
        ItemBeanConfig configBean = new ItemBeanConfig();
        ItemBeanControlProgram controlProgramBean = new ItemBeanControlProgram();
        ItemBeanDownLoadFile downLoadFileBean = new ItemBeanDownLoadFile();
        List<ItemBeanDownLoadFile> loadFileBeanList = new ArrayList<>();
        ItemBeanLogReport logReportBean = new ItemBeanLogReport();

        try {
            //开始解析事件
            int eventType = parser.getEventType();

            //处理事件，不碰到文档结束就一直处理
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //因为定义了一堆静态常量，所以这里可以用switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //给当前标签起个名字
                        String tagName = parser.getName();
                        if ("tasklist".equals(tagName)) {
                            basePollingBean = new BasePollingBean();
                        } else if ("taskcount".equals(tagName)) {
                            LogUtils.e("basePollingBean是否为空", basePollingBean + "dsadsa");
//                            LogUtils.e("taskcount内容",parser.nextText()+"dsadsa");
                            basePollingBean.setTaskcount(Integer.parseInt(parser.nextText()));
                        } else if ("taskitem".equals(tagName)) {
                            isItemEventStarted = true;
                            taskItem = new AbstractBeanTaskItem();
                        } else if ("tasktype".equals(tagName)) {
                            taskItem.setTasktype(parser.nextText());
                        } else if ("taskid".equals(tagName)) {
                            taskItem.setTaskid(parser.nextText());
                        } else if ("content".equals(tagName)) {
                            if (taskItem == null || taskItem.getTasktype() == null) continue;
                            switch (taskItem.getTasktype()) {
                                case Constant.POLLING_PROGRAM:
                                    programBean = new ItemBeanProgram();
                                    break;

                                case Constant.POLLING_DOWNLOADRES:
                                    downLoadFileBean = new ItemBeanDownLoadFile();
                                    break;
                            }
                        } else if (!"command".equals(tagName)) {
                            if (taskItem == null || taskItem.getTasktype() == null) continue;
                            switch (taskItem.getTasktype()) {
                                case Constant.POLLING_PROGRAM:
                                    LogUtils.e("XmlUtils", "检测到数据类型为播放类任务");
                                    switch (tagName) {
                                        case "csize":
                                            programBean.setCsize(Integer.parseInt(parser.nextText()));
                                            break;
                                        case "link":
                                            programBean.setLink(parser.nextText());
                                            break;
                                        case "md5":
                                            programBean.setMd5(parser.nextText());
                                            break;
                                        case "playmode":
                                            programBean.setPlaymode(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_CONTROL:
                                    LogUtils.e("XmlUtils", "检测到数据类型为控制类任务");
                                    switch (tagName) {
                                        case "control":
                                            controlBean.setControl(Integer.parseInt(parser.nextText()));
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_UPGRADE:
                                    LogUtils.e("XmlUtils", "检测到数据类型为升级类任务");
                                    switch (tagName) {
                                        case "link":
                                            upGradeBean.setLink(parser.nextText());
                                            break;
                                        case "version":
                                            upGradeBean.setVersion(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_REALTIMEMSG:
                                    LogUtils.e("XmlUtils", "检测到数据类型为即时消息类任务");
                                    switch (tagName) {
                                        case "bgcolor":
                                            realTimeMsgBean.setBgcolor(parser.nextText());
                                            break;
                                        case "fontcolor":
                                            realTimeMsgBean.setFontcolor(parser.nextText());
                                            break;
                                        case "count":
                                            realTimeMsgBean.setCount(parser.getName());
                                            break;
                                        case "starttime":
                                            realTimeMsgBean.setStarttime(parser.nextText());
                                            break;
                                        case "endtime":
                                            realTimeMsgBean.setEndtime(parser.nextText());
                                            break;
                                        case "message":
                                            realTimeMsgBean.setMessage(parser.nextText());
                                            break;
                                        case "position":
                                            realTimeMsgBean.setPosition(parser.nextText());
                                            break;
                                        case "speed":
                                            realTimeMsgBean.setSpeed(parser.nextText());
                                            break;
                                        case "timelength":
                                            realTimeMsgBean.setTimelength(parser.nextText());
                                            break;
                                        case "fontsize":
                                            realTimeMsgBean.setFontsize(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_CANCELREALTIMEMSG:
                                    LogUtils.e("XmlUtils", "检测到数据类型为取消即时消息类任务");
                                    break;
                                case Constant.POLLING_CONFIG:
                                    LogUtils.e("XmlUtils", "检测到数据类型为终端配置类任务");
                                    switch (tagName) {
                                        case "startuptime":
                                            configBean.setStaruptime(parser.nextText());
                                            break;
                                        case "shutdowntime":
                                            configBean.setShutdowntime(parser.nextText());
                                            break;
                                        case "diskspacealarm":
                                            configBean.setDiskspacealarm(parser.nextText());
                                            break;
                                        case "serverconfig":
                                            configBean.setServerconfig(parser.nextText());
                                            break;
                                        case "selectinterval":
                                            configBean.setSelectinterval(parser.nextText());
                                            break;
                                        case "volume":
                                            configBean.setVolume(parser.nextText());
                                            break;
                                        case "ftpserver":
                                            configBean.setFtpserver(parser.nextText());
                                            break;
                                        case "httpserver":
                                            configBean.setHttpserver(parser.nextText());
                                            break;
                                        case "downloadrate":
                                            configBean.setDownloadrate(parser.nextText());
                                            break;
                                        case "downloadtime":
                                            configBean.setDownloadtime(parser.nextText());
                                            break;
                                        case "logserver":
                                            configBean.setLogserver(parser.nextText());
                                            break;
                                        case "uploadlogtime":
                                            configBean.setUploadlogtime(parser.nextText());
                                            break;
                                        case "keeplogtime":
                                            configBean.setKeeplogtime(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_CONTROLPROGRAM:
                                    LogUtils.e("XmlUtils", "检测到数据类型为播放任务控制类任务");
                                    switch (tagName) {
                                        case "cmd":
                                            controlProgramBean.setCmd(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_CFGREPORT:
                                    LogUtils.e("XmlUtils", "检测到数据类型为终端配置信息日志上报类任务");
                                    break;
                                case Constant.POLLING_WORKSTATUSREPORT:
                                    LogUtils.e("XmlUtils", "检测到数据类型为终端工作状态上报类任务");
                                    break;
                                case Constant.POLLING_MONITORREPORT:
                                    LogUtils.e("XmlUtils", "检测到数据类型为终端在播内容上报类任务");
                                    break;
                                case Constant.POLLING_LOGREPORT:
                                    LogUtils.e("XmlUtils", "检测到数据类型为终端日志上报类任务");
                                    switch (tagName) {
                                        case "logtype":
                                            logReportBean.setLogtype(parser.nextText());
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_DOWNLOADRES:
                                    LogUtils.e("XmlUtils", "检测到数据类型为通知终端下载资源文件类任务");
                                    switch (tagName) {
                                        case "csize":
                                            String temp=parser.nextText();
                                            Log.e("文件大小",temp);
                                            boolean a=(downLoadFileBean==null);
                                            Log.e("LoadFileBean是否为空",a+"");
                                            downLoadFileBean.setCsize(Integer.parseInt(temp));
                                            break;
                                        case "link":
                                            downLoadFileBean.setLink(parser.nextText());
                                            break;
                                        case "md5":
                                            downLoadFileBean.setMd5(parser.nextText());
                                            break;
                                        case "playmode":
                                            downLoadFileBean.setPlaymode(Integer.parseInt(parser.nextText()));
                                            break;
                                    }
                                    break;
                                case Constant.POLLING_DOWNLOADSTATUSREPORT:
                                    LogUtils.e("XmlUtils", "检测到数据类型为通知终端上报资源下载状态类任务");
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTagName = parser.getName();
                        switch (endTagName) {


                            case "taskitem":

                                switch (taskItem.getTasktype()) {
                                    case Constant.POLLING_PROGRAM:
                                        taskItem.setT(programs);
                                        break;
                                    case Constant.POLLING_UPGRADE:
                                        taskItem.setT(upGradeBean);
                                        break;
                                    case Constant.POLLING_REALTIMEMSG:
                                        taskItem.setT(realTimeMsgBean);
                                        break;
                                    case Constant.POLLING_CANCELREALTIMEMSG:

                                        break;
                                    case Constant.POLLING_CONFIG:
                                        taskItem.setT(configBean);
                                        break;
                                    case Constant.POLLING_CONTROLPROGRAM:
                                        taskItem.setT(controlProgramBean);
                                        break;
                                    case Constant.POLLING_CFGREPORT:
                                        break;
                                    case Constant.POLLING_WORKSTATUSREPORT:
                                        break;
                                    case Constant.POLLING_MONITORREPORT:
                                        break;
                                    case Constant.POLLING_LOGREPORT:
                                        taskItem.setT(logReportBean);
                                        break;
                                    case Constant.POLLING_DOWNLOADRES:
                                        taskItem.setT(loadFileBeanList);
                                        break;
                                    case Constant.POLLING_DOWNLOADSTATUSREPORT:
                                        break;
                                }
                                taskItemArrayList.add(taskItem);
                                taskItem = null;
                                break;
                            case "content":
                                switch (taskItem.getTasktype()) {
                                    case Constant.POLLING_PROGRAM:
                                        programs.add(programBean);
//                                        programBean = null;
                                        break;
                                    case Constant.POLLING_DOWNLOADRES:
                                        loadFileBeanList.add(downLoadFileBean);
//                                        downLoadFileBean = null;
                                        break;
                                }
                                break;
                            case "tasklist":
                                basePollingBean.setTaskitems(taskItemArrayList);
                                break;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        basePollingBean.setTaskitems(taskItemArrayList);
                        break;

                }
                //别忘了用next方法处理下一个事件，忘了的结果就成死循环#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return basePollingBean;
    }

}


