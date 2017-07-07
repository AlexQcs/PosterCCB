package com.hc.posterccb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.hc.posterccb.api.Api;
import com.hc.posterccb.bean.Adress;
import com.hc.posterccb.bean.Demo;
import com.hc.posterccb.bean.ListAdress;
import com.hc.posterccb.bean.TestBean;
import com.hc.posterccb.bean.TestResult;
import com.hc.posterccb.util.SFTPUtils;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.XmlUtils;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.thoughtworks.xstream.XStream;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";

    //控件
    private PLVideoView mVideoView;

    private Button mUpLoadBtn;
    private Button mDownLoadBtn;

    //xml请求与相应处理
    public static final MediaType MEDIA_TYPE_XML
            = MediaType.parse("text/xml;charsetutf-8");
    private final OkHttpClient mClient = new OkHttpClient();
    private String postStr;
    private XStream xStream;
    private Demo mDemo;
    private XmlPullParser parser;

    //SFTP连接
    private SFTPUtils mSFTPUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        xStream = new XStream();
        mDemo = new Demo();
        mDemo.setTaskid(45);
        mDemo.setTasktype(3);
        mDemo.setStatus(1);
        List<Adress> adressList = new ArrayList<>();
        Adress adress01 = new Adress();
        adress01.setStreet("北大街");
        adress01.setNumber(1826928);
        Adress adress02 = new Adress();
        adress02.setStreet("南大街");
        adress02.setNumber(1278895);
        adressList.add(adress01);
        adressList.add(adress02);
        mDemo.setAdresses(adressList);
        xStream.alias("command", Demo.class);
        xStream.alias("Adress", Adress.class);
        xStream.aliasAttribute(Demo.class, "mAdresses", "Adresses");
        postStr = xStream.toXML(mDemo);
        StringUtils.setEncoding(postStr, "UTF-8");
//        Log.e("转换结果", postStr);
        try {

            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
        initEvent();
    }


    void initData() throws Exception {

        mSFTPUtils = new SFTPUtils(Api.SFTP_PATH, Api.SFTP_USER, Api.SFTP_PSD);

        String url = Api.REV_XML;

        Request request = new Request.Builder().url(url).build();

//        Request request = new Request.Builder()
//                .addHeader("command","getTask")
//                .addHeader("mac","01001001001000")
//                .url(url)
//                .post(RequestBody.create(MEDIA_TYPE_XML, postStr))
//                .build();

        mClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code" + response);
//                        String resStr = response.body().string();
//                        Log.e("返回的数据", resStr);
                        Reader in = response.body().charStream();
                        try {
                            parser = Xml.newPullParser();
                            parser.setInput(in);
                            TestResult result = XmlUtils.getBeanByParseXml(parser, "Adress", ListAdress.class, "command", TestBean.class);
                            Log.e("返回的数据", result.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    void initView() {
        mVideoView = (PLVideoView) findViewById(R.id.videoview);
        mDownLoadBtn = (Button) findViewById(R.id.btn_download);
        mUpLoadBtn = (Button) findViewById(R.id.btn_upload);

        mVideoView.setVideoPath(Constant.VIDEO_PATH);
//        mVideoView.start();
        mVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mVideoView.seekTo(0);
                mVideoView.start();
            }
        });
    }

    void initEvent() {
        mDownLoadBtn.setOnClickListener(this);
        mUpLoadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        new Thread() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.btn_upload: {
                        Log.e(TAG, "上传文件");
                        String localPAth = Constant.UDP_TESTPATH;
                        String remotePath = "test1/";
                        mSFTPUtils.connect();
                        Log.e(TAG, "连接成功");
                        mSFTPUtils.uploadFile(remotePath, "client.txt", localPAth, "test.txt");
                        Log.e(TAG, "上传成功");
                        mSFTPUtils.disconnect();
                        Log.e(TAG, "断开连接");
                        break;
                    }


                    case R.id.btn_download:
                        Log.e(TAG, "下载文件");
                    {
                        String localPath = Constant.UDP_TESTPATH;
                        String remotePath = "test1/";
                        mSFTPUtils.connect();
                        Log.e(TAG, "连接成功");
                        mSFTPUtils.downloadFile(remotePath, "aec.mp4", localPath, "test.mp4");
                        Log.e(TAG, "下载成功");
                        mSFTPUtils.disconnect();
                        Log.e(TAG, "断开连接");
                        break;
                    }
                    default:
                        break;
                }
            }
        }.start();
    }
}
