package com.ucai.test.constant;

/**
 * 这个类中放一些常量的值
 * Created by clawpo on 15/5/29.
 */
public class Constant {

    public static final int MSG_READ = 2;
    public static final int MSG_DEVICE_NAME = 4;
    public static final String DEVICE_NAME = "device_name";
    public static final int MSG_SDH = 8;
    public static final int MSG_SBP = 5;
    public static final int MSG_DBP = 6;
    public static final int MSG_HB = 7;

    public static final int MSG_HEIGHT = 9;



    public static final String  DEVICE_BPNOLLEC =  "BPNollec";
    public static final String  DEVICE_NOLLEC_HEIGHT =  "nollec_height";

    public static final String URL_HEIGHT = "http://wit120.nollec.com/heartrate.php";
    public static final String URL_RATE = "http://wit120.nollec.com/heartrate.php";
    public static final String URL_PULSE = "http://wit120.nollec.com/pulse.php";

    /**
     * 从此网址下载
     */
    public static final String URL_USER_DATA = "http://wit120.nollec.com/weight.php";//miracletest.php";//weight


    public static final String URL_LOGIN = "http://wit120.nollec.com/club/index.php?m=u&c=login&a=dorun";
    public static final String URL_VIDEO = "http://10.1.3.240:3000";
    public static final String URL_REGISTER = "http://wit120.nollec.com/club/index.php?m=u&c=register";
    public static final String URL_PHYSIOLOGICAL_DB = "http://wit120.nollec.com/club/index.php?m=special&id=2";
    public static final String URL_HEARTRATE_RANKING = "http://wit120.nollec.com/club/index.php?c=thread&fid=2";
    public static final String URL_COMMUNITY = "http://wit120.nollec.com:81/club/";//"http://10.1.3.240:3000";


    public static final String PACKAGE_NAME_FIREFOX = "org.mozilla.firefox"; //firefox packagename
    public static final String ACCOUNT = "account";
    public static final String LOGIN = "login";



    public static final String ACTION_LOGIN_RECEIVER = "com.nollec.wit120.activity.loginreceiver";
    public static final String ACTION_UPLOAD = "com.nollec.wit120.service.UploadReceiver";
    public static final String ACTION_BLUETOOTH_CONNECT = "com.nollec.wit120.service.bluetoothconnect";

    public static final String HIGHT_CMD = "e8 02 b0";

    public static final int HEART_RATE_WARING = 150;



    public static final int TYPE_HEIGHT = 0;
    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_HEARTRATRE = 2;
    public static final int TYPE_BLOODOXY = 3;

//	public static final int TYPE_HEIGHT = 0;
//	public static final int TYPE_HEIGHT = 0;
//	public static final int TYPE_HEIGHT = 0;
//	public static final int TYPE_HEIGHT = 0;


    public static final int DETECT_STATE_START = 0;
    public static final int DETECT_STATE_END = 1;
    public static final int INTENT_ERROR = 2;

//	public static final int DETECT_STATE_START = 0;
//
//	public static final int DETECT_STATE_START = 0;
//
//	public static final int DETECT_STATE_START = 0;
//
//	public static final int DETECT_STATE_START = 0;

//	public static final String KEY_ = ;




}
