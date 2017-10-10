package com.adcar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.UUID;
//import net.youmi.android.appoffers.YoumiOffersManager;

public class MainActivity
        extends Activity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "MA";
    public static final int PRESSED_COLOR = Color.argb(128, 255, 0, 0);
    public static final int NORMAL_COLOR = Color.rgb(128,128,128);
    public static final int ALPHA = 200;

    private static final int CHOICE_BLUETOOTH = 9;
    public static final String BLUETOOTH_DEVICE_MAC_ADDRESS = "bluetooth_device_mac_address";
    public static final int INIT_CONNECTION_BLUETOOTH = 1001;
    private static final int CONNECTED_BLUETOOTH = 1002;

    public static final float ACCELEROMETER_THRESHOLD = 2.5F;
    public static final String IS_FIRST_RUN = "isFirstRun";
    public AbsoluteLayout ASD;


    String MAC;
    public Button beijing;
    public BluetoothAdapter bt;
    private BluetoothSocket btsocket;
    SharedPreferences.Editor editor;
    private SensorEventListener ff;
    public Button ganying;
    public Button ganying2;
    private Sensor hh;
    public Button hou;
    public Button hou1;
    boolean isFirstRun;
    public Button kaishi;
    private SensorManager kk;
    int lytemp;
    int m = 0;
    public Button qian;
    public Button qian1;
    public Button ting;
    public Button tuichu;
    public BluetoothDevice xiaoche;
    public Button you;
    public Button you1;
    public Button zuo;
    public Button zuo1;


    /**
     * 当我们用手按下前进时，手机会通过蓝牙以ASCII码的形式发送一个字符“A”,释放时会发送一个字符“a”；
     * <p/>
     * 按下 后退 时，手机会通过蓝牙以ASCII码的形式发送一个字符“D”,释放时会发送一个字符“d”；
     * <p/>
     * 按下 左转 时，手机会通过蓝牙以ASCII码的形式发送一个字符“C”,释放时会发送一个字符“c”；
     * <p/>
     * 按下 右转 时，手机会通过蓝牙以ASCII码的形式发送一个字符“B”,释放时会发送一个字符“b”；
     * <p/>
     * 当使用重力感应模式时，也是根据手机的平衡状态来发送这几个字符的。
     * <p/>
     * 可以查询得到ABCDabcd 八个字符的ASCII码值：A-65  a-97  B-66  b-98   C-67  c-99  D-68  d-100.
     * <p/>
     * 有了这些，我们对单片机编程的思路就出来了——
     * <p/>
     * 我们可以使用使用51单片机的串口通信来接收发送过来的ASCII码。打开串口中断，我们可以在串口中断服务函数中接收到字符后用     switch（）语句来判断接收到的字符。例如
     * <p/>
     * case：65    break； 表示接收到的字符是A，也就是按 前进 时发送过来的，这时就可以在  break前写上小车前进的代码；
     * <p/>
     * case：97    break；表示接收到的是a，即松开 前进 时发送出来的，既然松开了 前进 按钮 ，就可以再break前加上小车停止的语句。 左转，右转，后退 也是一样的。
     * <p/>
     * 至于小车前进后退左转右转的代码，要根据你自己的电机机驱动来写。下面给出一个程序例子：
     * <p/>
     * <p/>
     * <p/>
     * #include<reg52.h>
     * <p/>
     * #define uchar unsigned char
     * <p/>
     * #define uint unsigned int
     * <p/>
     * uchar a;
     * <p/>
     * void init()
     * <p/>
     * {
     * <p/>
     * TMOD=0X20;   //设置定时器1为方式2
     * <p/>
     * TH1=0xfd ;
     * <p/>
     * TL1=0xfd ;   //装初值
     * <p/>
     * TR1=1;       //启动定时器1
     * <p/>
     * REN=1;       // 使能接收
     * <p/>
     * SM0=0;
     * <p/>
     * SM1=1;       //设置串口为工作方式1
     * <p/>
     * <p/>
     * <p/>
     * EA=1;       // 打开总中断开关
     * <p/>
     * ES=1;       // 打开串口中断开关
     * <p/>
     * }
     * <p/>
     * void main()
     * <p/>
     * {  init();
     * <p/>
     * while(1);
     * <p/>
     * }
     * <p/>
     * void ser() interrupt 4
     * <p/>
     * {      ES=0;
     * <p/>
     * RI=0;   //将接受中断标志位清0；
     * <p/>
     * a=SBUF; //将接受到的数据赋值给a
     * <p/>
     * switch(a)
     * <p/>
     * {
     * <p/>
     * case 65:  P2=0X17;  break;   //  发送的是A  前进指令
     * <p/>
     * case 66:  P2=0X1B;  break;   //  发送的是B  右转指令
     * <p/>
     * case 67:  P2=0X27;  break;   //  发送的是C  左转指令
     * <p/>
     * case 68:  P2=0X2B;  break;   //  发送的是D  后退指令
     * <p/>
     * <p/>
     * <p/>
     * case 97:   P2=0X00;  break;  //发送的是a   停止指令
     * <p/>
     * case 98:   P2=0X00;  break;  //发送的是b   停止指令
     * <p/>
     * case 99:   P2=0X00;  break;  //发送的是c   停止指令
     * <p/>
     * case 100:  P2=0X00;  break;  //发送的是d   停止指令
     * <p/>
     * //P2口是我的电机驱动的控制端
     * <p/>
     * default: break;
     * <p/>
     * }
     * <p/>
     * ES=1;
     * <p/>
     * }
     * <p/>
     * 需要注意的是，这个程序的P2口，是我这个小车上电机驱动的控制端口，这个地方大家要根据自己的驱动来写。
     * <p/>
     * 若用其它类型单片机像AVR  MSP430,等都只需在串口中断服务函数中进行上面的处理即可。
     */


    private View.OnTouchListener KJH = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            String chars = "";
            switch (v.getId()) {
                case R.id.qian:
                case R.id.qian1:
                    chars = "A";
                    break;
                case R.id.hou:
                case R.id.hou1:
                    chars = "D";
                    break;
                case R.id.zuo:
                case R.id.zuo1:
                    chars = "C";
                    break;
                case R.id.you:
                case R.id.you1:
                    chars = "B";
                    break;
                default:
                    chars = "";
                    break;
            }

            switch (event.getAction()) {
                default:
                    break;
                case MotionEvent.ACTION_DOWN: //0
                    chars = chars.toLowerCase();
                    break;
                case MotionEvent.ACTION_UP: //1
                    chars = chars.toUpperCase();
                    break;
                case MotionEvent.ACTION_MOVE: //2
                    chars = chars.toUpperCase();
                    break;
            }

            OutputStream fs = null;
            try {
                fs = MainActivity.this.btsocket.getOutputStream();

                byte[] arrayOfByte8 = chars.getBytes();
                fs.write(arrayOfByte8);

                Log.d(TAG, "action:=" + event.getAction() + " v:=" + v.getId() + " write char=" + chars);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(fs!=null) try {
                   // fs.close();
                    fs.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }
    };
    private ProgressDialog pd;


    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        // YoumiOffersManager.init(this, "0fdd2d75999ff1f5", "0a5d2b8e117337cb");
        setContentView(R.layout.activity_main);
        this.ASD = ((AbsoluteLayout) findViewById(R.id.asd));
        this.qian = ((Button) findViewById(R.id.qian));
        this.qian.getBackground().setAlpha(100);
        this.hou = ((Button) findViewById(R.id.hou));
        this.hou.getBackground().setAlpha(100);
        this.zuo = ((Button) findViewById(R.id.zuo));
        this.zuo.getBackground().setAlpha(100);
        this.you = ((Button) findViewById(R.id.you));
        this.you.getBackground().setAlpha(100);
        this.qian1 = ((Button) findViewById(R.id.qian1));
        this.hou1 = ((Button) findViewById(R.id.hou1));
        this.zuo1 = ((Button) findViewById(R.id.zuo1));
        this.you1 = ((Button) findViewById(R.id.you1));
        this.ting = ((Button) findViewById(R.id.ting));
        this.ting.getBackground().setAlpha(100);
        this.kaishi = ((Button) findViewById(R.id.ks));
        this.kaishi.getBackground().setAlpha(100);
        this.tuichu = ((Button) findViewById(R.id.tc));
        this.tuichu.getBackground().setAlpha(100);
        this.beijing = ((Button) findViewById(R.id.bj));
        this.beijing.getBackground().setAlpha(100);
        this.ganying = ((Button) findViewById(R.id.gy1));
        this.ganying.getBackground().setAlpha(100);
        this.ganying2 = ((Button) findViewById(R.id.gy2));
        this.ganying2.setBackgroundColor(0xFFFF);
        this.ganying2.getBackground().setAlpha(150);

        turnGravityGroupBtn(false);
        turnNormGroupBtn(false);


        this.ting.setVisibility(View.INVISIBLE);

        click cl = new click();
        this.ting.setOnClickListener(cl);
        this.kaishi.setOnClickListener(cl);
        this.tuichu.setOnClickListener(cl);
        this.beijing.setOnClickListener(cl);
        this.ganying.setOnClickListener(cl);
        this.ganying2.setOnClickListener(cl);
        this.qian.setOnTouchListener(this.KJH);
        this.hou.setOnTouchListener(this.KJH);
        this.zuo.setOnTouchListener(this.KJH);
        this.you.setOnTouchListener(this.KJH);
        this.bt = BluetoothAdapter.getDefaultAdapter();
        this.kk = ((SensorManager) getSystemService(Context.SENSOR_SERVICE)); ///"sensor"));
        this.hh = this.kk.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        this.ff = new SensorEventListener() {
            String message;
            byte[] zhiling;

            public void onAccuracyChanged(Sensor paramAnonymousSensor, int paramAnonymousInt) {
            }

            public void onSensorChanged(SensorEvent event) {
                int i = 1;
                float x = event.values[0];
                float y = event.values[i];
                float z = event.values[2];


                String chars = "";

                if (y < -ACCELEROMETER_THRESHOLD) {
                    MainActivity.this.qian1.setBackgroundColor(PRESSED_COLOR);
                    chars = "A";
                }
                if (y > ACCELEROMETER_THRESHOLD) {
                    MainActivity.this.hou1.setBackgroundColor(PRESSED_COLOR);
                    chars = "D";
                }
                if (x > ACCELEROMETER_THRESHOLD) {
                    MainActivity.this.zuo1.setBackgroundColor(PRESSED_COLOR);
                    chars = "C";
                }
                if (x < -ACCELEROMETER_THRESHOLD) {
                    MainActivity.this.you1.setBackgroundColor(PRESSED_COLOR);
                    chars = "B";
                }

                OutputStream fs = null;
                try {
                    fs = MainActivity.this.btsocket.getOutputStream();
                    this.message = chars;
                    this.zhiling = this.message.getBytes();

                    Log.d(TAG, "x=" + x + " y=" + y + " z=" + z + " char=" + chars);
                    fs.write(this.zhiling);


                    if (x > -ACCELEROMETER_THRESHOLD && x <= ACCELEROMETER_THRESHOLD && y >= -ACCELEROMETER_THRESHOLD && y <= ACCELEROMETER_THRESHOLD) {
                        MainActivity.this.qian1.setBackgroundColor(NORMAL_COLOR);
                        MainActivity.this.qian1.getBackground().setAlpha(ALPHA);

                        MainActivity.this.hou1.setBackgroundColor(NORMAL_COLOR);
                        MainActivity.this.hou1.getBackground().setAlpha(ALPHA);

                        MainActivity.this.zuo1.setBackgroundColor(NORMAL_COLOR);
                        MainActivity.this.zuo1.getBackground().setAlpha(ALPHA);

                        MainActivity.this.you1.setBackgroundColor(NORMAL_COLOR);
                        MainActivity.this.you1.getBackground().setAlpha(ALPHA);

                        this.message = "a";
                        this.zhiling = this.message.getBytes();

                        fs.write(this.zhiling);

                        Log.d(TAG, "stop char=" + message);

                    }


                } catch (IOException e) {
                    e.printStackTrace();


                }finally {
                    if(fs!=null) try {
                       // fs.close();
                        fs.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        };
        if (this.bt!=null && !this.bt.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        SharedPreferences localSharedPreferences = getSharedPreferences("share", 0);
        this.isFirstRun = localSharedPreferences.getBoolean(IS_FIRST_RUN, true);
        this.editor = localSharedPreferences.edit();
        this.editor.putBoolean(IS_FIRST_RUN, false);
        this.editor.apply();

    }

    class click implements View.OnClickListener {

        public void onClick(View paramView) {
            switch (paramView.getId()) {
                case R.id.textView1:
                default:
                case R.id.ting:
                    MainActivity.this.kk.unregisterListener(MainActivity.this.ff);
                    try {
                        MainActivity.this.btsocket.close();
                        MainActivity.this.lytemp = 0;
                        Toast.makeText(MainActivity.this, "与小车的连接已断开", Toast.LENGTH_SHORT).show();
                        MainActivity.this.ting.setVisibility(View.INVISIBLE);
                        MainActivity.this.tuichu.setVisibility(View.VISIBLE);

                        turnNormGroupBtn(false);
                        turnGravityGroupBtn(false);

                        MainActivity.this.kaishi.setEnabled(true);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    break;
                case R.id.ks:


                    if (MainActivity.this.bt == null) {
                        Log.i(TAG, "onClick: not bluetooth device found! try to find again...");
                        MainActivity.this.bt = BluetoothAdapter.getDefaultAdapter();
                    }else {


                        Set localSet = MainActivity.this.bt.getBondedDevices();
                        if (localSet.size() <= 0) {
                            Toast.makeText(MainActivity.this, "您没有配对的蓝牙设备", Toast.LENGTH_LONG).show();
                            return;
                        } else {

                            Intent intent = new Intent(MainActivity.this, BlueToothDeviceChoiceListViewActivity.class);
                            MainActivity.this.startActivityForResult(intent, CHOICE_BLUETOOTH);
                        }

                    }
                    break;


                case R.id.tc:
                    Toast.makeText(MainActivity.this, "退出", Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                    break;
                case R.id.bj:
                    MainActivity.this.m = (1 + MainActivity.this.m);
                    if (MainActivity.this.m == 6) {
                        MainActivity.this.m = 0;
                    }
                    switch (MainActivity.this.m) {
                        case 0:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.c));
                            break;
                        case 1:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.d));
                            break;
                        case 2:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.e));
                            break;
                        case 3:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.f));
                            break;
                        case 4:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.k));
                            break;
                        case 5:
                            MainActivity.this.ASD.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.b));
                            break;
                    }
                    break;
                case R.id.gy1:
                    turnNormGroupBtn(false);
                    turnGravityGroupBtn(true);

                    Toast.makeText(MainActivity.this, "已开启重力感应", Toast.LENGTH_SHORT).show();
                    MainActivity.this.kk.registerListener(MainActivity.this.ff, MainActivity.this.hh, 3);
                    break;
                case R.id.gy2:
                    turnNormGroupBtn(true);
                    turnGravityGroupBtn(false);

                    Toast.makeText(MainActivity.this, "已关闭重力感应", Toast.LENGTH_SHORT).show();
                    MainActivity.this.kk.unregisterListener(MainActivity.this.ff);
                    break;

            }
        }
    }

    private void turnGravityGroupBtn(boolean isOn) {
        int visible = isOn? View.VISIBLE :View.INVISIBLE;

        MainActivity.this.qian1.setVisibility(visible);
        MainActivity.this.hou1.setVisibility(visible);
        MainActivity.this.zuo1.setVisibility(visible);
        MainActivity.this.you1.setVisibility(visible);
        MainActivity.this.ganying2.setVisibility(visible);
    }

    private void turnNormGroupBtn(boolean isOn) {
        int visible = isOn? View.VISIBLE :View.INVISIBLE;

        MainActivity.this.qian.setVisibility(visible);
        MainActivity.this.hou.setVisibility(visible);
        MainActivity.this.zuo.setVisibility(visible);
        MainActivity.this.you.setVisibility(visible);
        MainActivity.this.ganying.setVisibility(visible);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case CHOICE_BLUETOOTH:
                switch (resultCode){
                    case RESULT_OK:
                        final String mac_address = getBlueTooth(intent);
                        startBluetooth(mac_address);
                        break;

                }
        }
    }

    static class AppHandler extends Handler{
        WeakReference<MainActivity> mActivity;

        public AppHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case INIT_CONNECTION_BLUETOOTH:
                final String mac_address = msg.getData().getString(BLUETOOTH_DEVICE_MAC_ADDRESS);
                if (mac_address != null) {

                    boolean res= false;
                    if(mActivity.get()!=null)res =  mActivity.get().initDevice(mac_address);

                    if(res) {
                        Message mymsg = new Message();
                        mymsg.arg1 = CONNECTED_BLUETOOTH;
                        sendMessage(mymsg);
                    }

                }
                    break;
                case CONNECTED_BLUETOOTH:
                    if(mActivity.get()!=null) mActivity.get().onConnected();
                break;
            }
        }
    };

    private  Handler mApplicationHandler = new AppHandler(this);
//    private  Handler mApplicationHandler = new AppHandler(this);

    public void startBluetooth(String mac){
        Message msg = new Message();
        msg.arg1= INIT_CONNECTION_BLUETOOTH;
        Bundle bundle = new Bundle();
        bundle.putString(BLUETOOTH_DEVICE_MAC_ADDRESS, mac);
        msg.setData(bundle);
        pd = new ProgressDialog(this);
        pd.setTitle(R.string.connection_in_progress);
        pd.show();
        mApplicationHandler.sendMessage(msg);
    }

    private boolean initDevice(String mac) {

        if(mac==null || mac.length()<=0) return false;


        this.MAC = mac;

        this.xiaoche = this.bt.getRemoteDevice(this.MAC);
        try {

            Log.i(TAG, "连接中:"+mac);
            this.btsocket = this.xiaoche.createRfcommSocketToServiceRecord(MY_UUID);
            this.btsocket.connect();

            this.lytemp = 1;

            return true;

        } catch (IOException e) {
            Toast.makeText(this, "连接出错,请确认蓝牙设备已打开:"+e.getMessage(), Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        } finally {
           if(pd!=null) pd.dismiss();
        }
        return false;
    }

    private void onConnected() {
        this.kaishi.setEnabled(false);
        turnNormGroupBtn(true);

        this.ting.setVisibility(View.VISIBLE);
        this.tuichu.setVisibility(View.INVISIBLE);
    }

    private String getBlueTooth(Intent intent) {
        return intent.getStringExtra(BLUETOOTH_DEVICE_MAC_ADDRESS);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(this.btsocket!=null ){
            try {
                this.btsocket.close();
                Toast.makeText(this, "蓝牙设备已断开", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


       this.kk.unregisterListener(this.ff);
    }
}


/* Location:              /root/game1/classes-dex2jar.jar!/com/adcar/MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */