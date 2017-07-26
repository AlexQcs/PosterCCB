package com.hc.posterccb.ui.acitivity;

import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseActivity;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.ui.contract.MainContract;
import com.hc.posterccb.ui.fragment.Full_H_Fragment;
import com.hc.posterccb.ui.fragment.Three_H_Fragment;
import com.hc.posterccb.ui.presenter.MainPresenter;
import com.hc.posterccb.util.FileUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.MemInfo;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.VolumeUtils;
import com.hc.posterccb.widget.MarqueeTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    @BindView(R.id.tv_realtimetop)
    MarqueeTextView mStvRealTimeTop;
    @BindView(R.id.tv_realtimebottom)
    MarqueeTextView mStvRealTimeBottom;

    private AudioManager am;

    @BindView(R.id.btn_pause)
    Button mBtnPause;
    @BindView(R.id.btn_relay)
    Button mBtnRelay;
    @BindView(R.id.btn_delete)
    Button mBtnDelete;
    @BindView(R.id.btn_cancle)
    Button mBtnCancle;

    private ActivityInteraction mInteraction;

    private BaseFragment mBaseFragment;

    private Full_H_Fragment mFull_H_Fragment;

    private Three_H_Fragment mThreeHFragment;

    private android.support.v4.app.FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private String TAG = "MainActivity";


    private PowerManager localPowerManager = null;// 电源管理对象
    private PowerManager.WakeLock localWakeLock = null;// 电源锁

    //    (R.id.tv_realtime_top)
//    ScrollTextView mTvRealTimeTop;
    //轮询任务tag
    private String mTaskName = "getTask";
    //机器码
    private String mSerialNumber = Constant.getSerialNumber();
    //区分即时消息类的位置
    private String mRealTimePosition = "top";


    @Override
    protected MainPresenter loadPresenter() {
        return new MainPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initData() {
        FileUtils.checkAppFile();
        long unused=MemInfo.getAvailableSize();
        VolumeUtils.setVolum(5);
        LogUtils.e(TAG,"剩余内存"+unused);
        mPresenter.pollingTask(mTaskName, mSerialNumber);
    }

    @OnClick({R.id.btn_cancle,R.id.btn_delete,R.id.btn_relay,R.id.btn_pause})
    void contoller(View view){
        switch (view.getId()){
            case R.id.btn_pause:
                mInteraction.pause();
                break;
            case R.id.btn_relay:
                mInteraction.replay();
                break;
        }
    }


    @Override
    protected void initListener() {

    }

    public interface ActivityInteraction{
        void pause();
        void replay();
        void delProgramList();
        void interruptCancle();
    }

    @Override
    protected void initView() {


        mFull_H_Fragment = new Full_H_Fragment();
        mThreeHFragment=new Three_H_Fragment();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
//

        if (mThreeHFragment instanceof ActivityInteraction){
            mInteraction=(ActivityInteraction)mThreeHFragment;
        }else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }

        mFragmentTransaction.add(R.id.frame_fragment, mThreeHFragment, "Three_H_Fragment");
        mFragmentTransaction.commit();



        localPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        localWakeLock = this.localPowerManager.newWakeLock(32, "hahaha");// 第一个参数为电源锁级别，第二个是日志tag
        localWakeLock.acquire();// 申请设备电源锁
        if (localWakeLock.isHeld()) {
            return;
        } else {
            localWakeLock.setReferenceCounted(false);
            localWakeLock.release(); // 释放设备电源锁
        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//做我们的工作，在这个阶段，我们的屏幕会持续点亮
//释放锁，屏幕熄灭。

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void otherViewClick(View view) {

    }

    public boolean checkPostParamNull() {
        boolean isNull = false;
        if (StringUtils.isEmpty(mTaskName)) {
            LogUtils.e(TAG, "任务名称为空");
            isNull = true;
        }
//        else if (StringUtils.isEmpty(mSerialNumber)) {
//            LogUtils.e(TAG, "序列号为空");
//            isNull = true;
//        }
        return isNull;
    }

    @Override
    public void setRealTimeText(RealTimeMsgBean bean) {

        String position = bean.getPosition();
        mRealTimePosition = position;
        if (position.equals("top")) {
            setMarqueeTextView(mStvRealTimeTop, bean);

        } else if (position.equals("under")) {
            setMarqueeTextView(mStvRealTimeBottom, bean);
        }
    }

    @Override
    public void startRealtimeTask() {
        if (mRealTimePosition.equals("top")) {
            mStvRealTimeTop.startScroll();
        } else if (mRealTimePosition.equals("under")) {
            mStvRealTimeBottom.startScroll();
        }
    }

    @Override
    public void stopRealtimeTask() {
        if (mRealTimePosition.equals("top")) {
            mStvRealTimeTop.stopScroll();
        } else if (mRealTimePosition.equals("under")) {
            mStvRealTimeBottom.stopScroll();
        }
    }

    @Override
    public void cancleRealtimeTask() {
        mStvRealTimeTop.stopScroll();
        mStvRealTimeBottom.stopScroll();
    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public void replayVideo() {

    }

    @Override
    public void deleteProgramList() {

    }

    @Override
    public void cancleInterruptVideo() {

    }

    void setMarqueeTextView(MarqueeTextView view, RealTimeMsgBean bean) {
        //字体大小
        float fontSize = Float.parseFloat(bean.getFontsize());
        //背景颜色
        int bgColor = Color.parseColor(bean.getBgcolor());
        //字体颜色
        int fontColor = Color.parseColor(bean.getFontcolor());
        //播放速度
        int speed = parseInt(bean.getSpeed());
        //播放的内容
        String message = bean.getMessage();
        view.setText(message);
        view.setTextSize(fontSize);
        view.setBackgroundColor(bgColor);
        view.setTextColor(fontColor);
        //播放时长或者播放时间
        if (!("").equals(bean.getCount())) {
            int count = parseInt(bean.getCount());
            if (mStvRealTimeTop.getMarquanTimes() == count) {
                mStvRealTimeTop.stopScroll();
            }

        } else if (!("").equals(bean.getTimelength())) {
            int timeLength = parseInt(bean.getTimelength());
        }


        switch (speed) {
            case 0:
                view.setSpeed(1);
                break;
            case 1:
                view.setSpeed(2);
                break;
            case 2:
                view.setSpeed(4);
                break;
        }


    }

    @Override
    public void pollingSuccess(String msg) {

    }

    @Override
    public void pollingFail(String failStr) {
        toast(failStr);
    }


//    private String TAG = "MainActivity";
//
//    //控件
//    private PLVideoView mVideoView;
//
//    private Button mUpLoadBtn;
//    private Button mDownLoadBtn;
//
//    //xml请求与相应处理
//    public static final MediaType MEDIA_TYPE_XML
//            = MediaType.parse("text/xml;charsetutf-8");
//    private final OkHttpClient mClient = new OkHttpClient();
//    private String postStr;
//    private XStream xStream;
//    private Demo mDemo;
//    private XmlPullParser parser;
//
//    //SFTP连接
//    private SFTPUtils mSFTPUtils;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_main);
//
//        xStream = new XStream();
//        mDemo = new Demo();
//        mDemo.setTaskid(45);
//        mDemo.setTasktype(3);
//        mDemo.setStatus(1);
//        List<Adress> adressList = new ArrayList<>();
//        Adress adress01 = new Adress();
//        adress01.setStreet("北大街");
//        adress01.setNumber(1826928);
//        Adress adress02 = new Adress();
//        adress02.setStreet("南大街");
//        adress02.setNumber(1278895);
//        adressList.add(adress01);
//        adressList.add(adress02);
//        mDemo.setAdresses(adressList);
//        xStream.alias("command", Demo.class);
//        xStream.alias("Adress", Adress.class);
//        xStream.aliasAttribute(Demo.class, "mAdresses", "Adresses");
//        postStr = xStream.toXML(mDemo);
//        StringUtils.setEncoding(postStr, "UTF-8");
////        Log.e("转换结果", postStr);
//        try {
//
//            initData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        initView();
//        initEvent();
//    }
//
//
//    void initData() throws Exception {
//
//        mSFTPUtils = new SFTPUtils(Api.SFTP_PATH, Api.SFTP_USER, Api.SFTP_PSD);
//
//        String url = Api.REV_XML;
//
//        Request request = new Request.Builder().url(url).build();
//
////        Request request = new Request.Builder()
////                .addHeader("command","getTask")
////                .addHeader("mac","01001001001000")
////                .url(url)
////                .post(RequestBody.create(MEDIA_TYPE_XML, postStr))
////                .build();
//
//        mClient.newCall(request)
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (!response.isSuccessful())
//                            throw new IOException("Unexpected code" + response);
////                        String resStr = response.body().string();
////                        Log.e("返回的数据", resStr);
//                        Reader in = response.body().charStream();
//                        try {
//                            parser = Xml.newPullParser();
//                            parser.setInput(in);
//                            TestResult result = XmlUtils.getBeanByParseXml(parser, "Adress", ListAdress.class, "command", TestBean.class);
//                            Log.e("返回的数据", result.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    void initView() {
//        mVideoView = (PLVideoView) findViewById(R.id.videoview);
//        mDownLoadBtn = (Button) findViewById(R.id.btn_download);
//        mUpLoadBtn = (Button) findViewById(R.id.btn_upload);
//
//        mVideoView.setVideoPath(Constant.VIDEO_PATH);
////        mVideoView.start();
//        mVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(PLMediaPlayer player) {
//                mVideoView.seekTo(0);
//                mVideoView.start();
//            }
//        });
//    }
//
//    void initEvent() {
//        mDownLoadBtn.setOnClickListener(this);
//        mUpLoadBtn.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(final View v) {
//        new Thread() {
//            @Override
//            public void run() {
//                switch (v.getId()) {
//                    case R.id.btn_upload: {
//                        Log.e(TAG, "上传文件");
//                        String localPAth = Constant.UDP_TESTPATH;
//                        String remotePath = "test1/";
//                        mSFTPUtils.connect();
//                        Log.e(TAG, "连接成功");
//                        mSFTPUtils.uploadFile(remotePath, "client.txt", localPAth, "test.txt");
//                        Log.e(TAG, "上传成功");
//                        mSFTPUtils.disconnect();
//                        Log.e(TAG, "断开连接");
//                        break;
//                    }
//
//
//                    case R.id.btn_download:
//                        Log.e(TAG, "下载文件");
//                    {
//                        String localPath = Constant.UDP_TESTPATH;
//                        String remotePath = "test1/";
//                        mSFTPUtils.connect();
//                        Log.e(TAG, "连接成功");
//                        mSFTPUtils.downloadFile(remotePath, "aec.mp4", localPath, "test.mp4");
//                        Log.e(TAG, "下载成功");
//                        mSFTPUtils.disconnect();
//                        Log.e(TAG, "断开连接");
//                        break;
//                    }
//                    default:
//                        break;
//                }
//            }
//        }.start();
//    }
}
