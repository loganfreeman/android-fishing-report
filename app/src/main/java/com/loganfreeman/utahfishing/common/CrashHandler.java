package com.loganfreeman.utahfishing.common;

import android.content.Context;
import com.loganfreeman.utahfishing.common.utils.SharedPreferenceUtil;
import com.loganfreeman.utahfishing.common.utils.Util;
import com.loganfreeman.utahfishing.component.OrmLite;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zk on 2015/12/24.
 * 捕获程序崩溃信息
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static Thread.UncaughtExceptionHandler mDefaultHandler = null;

    private Context mContext = null;

    private final String TAG = CrashHandler.class.getSimpleName();

    public CrashHandler(Context context) {
        this.mContext = context;
    }

    /**
     * 初始化,设置该CrashHandler为程序的默认处理器
     */
    public static void init(CrashHandler crashHandler) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println(ex.toString());
        PLog.e(TAG, ex.toString());
        PLog.e(TAG, collectCrashDeviceInfo());
        PLog.e(TAG, getCrashInfo(ex));

        // T崩溃后自动初始化数据
        SharedPreferenceUtil.getInstance().setCityName("北京");
        OrmLite.getInstance().deleteDatabase();
        // 调用系统错误机制
        mDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     * 得到程序崩溃的详细信息
     */
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.setStackTrace(ex.getStackTrace());
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    public String collectCrashDeviceInfo() {

        String versionName = Util.getVersion(mContext);
        String model = android.os.Build.MODEL;
        String androidVersion = android.os.Build.VERSION.RELEASE;
        String manufacturer = android.os.Build.MANUFACTURER;

        return versionName + "  " + model + "  " + androidVersion + "  " + manufacturer;
    }
}
