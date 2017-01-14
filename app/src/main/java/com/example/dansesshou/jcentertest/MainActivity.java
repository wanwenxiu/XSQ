package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;
import com.p2p.core.network.LoginResult;
import com.p2p.core.network.NetManager;

import org.json.JSONObject;

import static com.p2p.core.MediaPlayer.mContext;

public class MainActivity extends BaseMonitorActivity implements View.OnClickListener {
    public static String P2P_ACCEPT = "com.yoosee.P2P_ACCEPT";
    public static String P2P_READY = "com.yoosee.P2P_READY";
    public static String P2P_REJECT = "com.yoosee.P2P_REJECT";
     private Button choose_video_format,defence_state,close_voice,send_voice,screenshot,hungup,iv_half_screen;
//    private EditText et_callId;
//    private EditText et_callPwd;
    private String callID, CallPwd;
//    private TextView tv_content, tx_acount;
    private String LoginID;
    private boolean isMute = false;
    OrientationEventListener mOrientationEventListener;
    private boolean mIsLand = false;
    private RelativeLayout rl_p2pview,control_bottom,control_top;
    private int screenWidth, screenHeigh;
    private  Button button;
//    private LinearLayout llElse;
   private ViewPager viewPager;
    private TextView btn_mute;

    private Toolbar Toolbar;

    private int ScrrenOrientation;
    boolean isReject = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
//        setSupportActionBar(Toolbar);
        LoginID = getIntent().getStringExtra("LoginID");
        initUI();
        getScreenWithHeigh();
        regFilter();
        initData();

        callID ="5969657";//设备号
        CallPwd = "123";
        String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
        P2PHandler.getInstance().call(LoginID, pwd, true, 1, callID, "", "", 2, callID);
    }

    public void getScreenWithHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    private void initUI() {
        viewPager= (ViewPager) findViewById(R.id.viewpage);
        viewPager.setAdapter(mAdapter);
        control_bottom = (RelativeLayout) findViewById(R.id.control_bottom);
        control_top = (RelativeLayout) findViewById(R.id.control_top);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        choose_video_format = (Button) findViewById(R.id.choose_video_format);
        defence_state = (Button) findViewById(R.id.defence_state);
        close_voice = (Button) findViewById(R.id.close_voice);
        send_voice = (Button) findViewById(R.id.send_voice);
        screenshot = (Button) findViewById(R.id.screenshot);
        hungup = (Button) findViewById(R.id.hungup);
        iv_half_screen = (Button) findViewById(R.id.iv_half_screen);
        btn_mute = (TextView) findViewById(R.id.btn_mute);
        btn_mute.setOnClickListener(this);
        choose_video_format.setOnClickListener(this);
        defence_state.setOnClickListener(this);
        close_voice.setOnClickListener(this);
        screenshot.setOnClickListener(this);
        hungup.setOnClickListener(this);
        iv_half_screen.setOnClickListener(this);

        //对讲
        send_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        setMute(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        setMute(true);
                        return true;
                }
                return false;
            }
        });

        pView = (P2PView) findViewById(R.id.pview);
        rl_p2pview = (RelativeLayout) findViewById(R.id.r_p2pview);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
    }

    private void initData() {
        //此处是一种并不常见的横竖屏监听,客户可自行修改实现
        mOrientationEventListener = new OrientationEventListener(this) {

            @Override
            public void onOrientationChanged(int rotation) {
                // 设置横屏
                if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                    if (mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mIsLand = false;
                        setHalfScreen(true);
                    }
                } else if (((rotation >= 230) && (rotation <= 310))) {
                    if (!mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        mIsLand = true;
                        setHalfScreen(false);
                    }
                }
            }
        };
        mOrientationEventListener.enable();

        ScrrenOrientation = this.getResources().getConfiguration().orientation;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("dxsTest","config:"+newConfig.orientation);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            setHalfScreen(false);
            control_top.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            control_bottom.setVisibility(View.VISIBLE);
            //以下代码是因为 方案商设备类型很多,视频比例也比较多
            //客户更具自己的视频比例调整画布大小
            //这里的实现比较绕,如果能弄清楚这部分原理,客户可自行修改此处代码
            if (P2PView.type == 1) {
                if (P2PView.scale == 0) {
                    isFullScreen = false;
                    pView.halfScreen();//刷新画布比例
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            } else {
                //这里本应该用设备类型判断,如果只有一种类型可不用这么麻烦
                if (7 == P2PValue.DeviceType.NPC) {
                    isFullScreen = false;
                    pView.halfScreen();
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            }
            RelativeLayout.LayoutParams parames = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rl_p2pview.setLayoutParams(parames);//调整画布容器宽高(比例)
        }else{
            setHalfScreen(true);
            control_top.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            control_bottom.setVisibility(View.GONE);
            if (isFullScreen) {
                isFullScreen = false;
                pView.halfScreen();
            }
            //这里简写,只考虑了16:9的画面类型  大部分设备画面比例是这种
            int Heigh = screenWidth * 9 / 16;
            RelativeLayout.LayoutParams parames = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            parames.height = Heigh;
            rl_p2pview.setLayoutParams(parames);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                callID ="5969657";//设备号
                CallPwd = "123";
                String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
                P2PHandler.getInstance().call(LoginID, pwd, true, 1, callID, "", "", 2, callID);
                break;
            case R.id.choose_video_format:
                P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
                break;
            case R.id.defence_state:
                P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
                break;
            case R.id.screenshot:
                // 参数是一个标记,截图回调会原样返回这个标记
                //注意SD卡权限
                captureScreen(-1);
                break;
            case R.id.close_voice:
            case R.id.btn_mute: //静音
                changeMuteState();
                break;
            case R.id.hungup: //挂断
                reject();
            case R.id.iv_half_screen:  //竖屏
                control_bottom.setVisibility(View.INVISIBLE);
                ScrrenOrientation = Configuration.ORIENTATION_PORTRAIT;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    //挂断
    public void reject() {
        if (!isReject) {
            isReject = true;
            P2PHandler.getInstance().reject();

//            finish();
        }
    }

    FragmentPagerAdapter mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {

        private String[] mTitles = new String[]{ "设备","操作"};
        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new DeviceFragment();
            }
            return new WorkingFragment();
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

    };


    private void changeMuteState() {
        isMute = !isMute;
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            if (isMute) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            }
        }
    }


    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                button.setText("监控数据接收");
                Log.e("dxsTest", "监控数据接收:" + callID);
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            } else if (intent.getAction().equals(P2P_READY)) {
                button.setText(" 监控准备,开始监控");
                Log.e("dxsTest", "监控准备,开始监控" + callID);
                pView.sendStartBrod();
            } else if (intent.getAction().equals(P2P_REJECT)) {
                button.setText(" 监控挂断");
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().reject();
        P2PHandler.getInstance().p2pDisconnect();
        super.onDestroy();

    }

    @Override
    protected void onP2PViewSingleTap() {

    }

    @Override
    protected void onP2PViewFilling() {

    }


    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        if (isSuccess) {
            Toast.makeText(this, R.string.screenshot_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.screenshot_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onVideoPTS(long videoPTS) {

    }

    @Override
    public int getActivityInfo() {
        return 0;
    }

    @Override
    protected void onGoBack() {

    }

    @Override
    protected void onGoFront() {

    }

    @Override
    protected void onExit() {

    }
}
