package com.loganfreeman.utahfishing.common.utils;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.loganfreeman.utahfishing.base.BaseApplication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by hugo on 2016/2/19 0019.
 *
 * 设置相关 包括 sp 的写入
 */
public class SharedPreferenceUtil {

    public static final String CITY_NAME = "城市";//选择城市
    public static final String HOUR = "current_hour";//当前小时

    public static final String CHANGE_ICONS = "change_icons";//切换图标
    public static final String CLEAR_CACHE = "clear_cache";//清空缓存
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    public static final String NOTIFICATION_MODEL = "notification_model";
    public static final String ANIM_START = "animation_start";

    public static final String FAVORITE_WATRER = "favorite_water";

    public static int ONE_HOUR = 1000 * 60 * 60;

    private static final String SAVED_PREFS_BUNDLE_KEY_SEPARATOR = "§§";

    private SharedPreferences mPrefs;

    public static SharedPreferenceUtil getInstance() {
        return SPHolder.sInstance;
    }

    private static class SPHolder {
        private static final SharedPreferenceUtil sInstance = new SharedPreferenceUtil();
    }

    private SharedPreferenceUtil() {
        mPrefs = BaseApplication.getAppContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getEditor() {
        return mPrefs.edit();
    }

    public  SharedPreferences get() {
        return mPrefs;
    }

    /**
     * Save a Bundle object to SharedPreferences.
     *
     * NOTE: The editor must be writable, and this function does not commit.
     *
     * @param editor SharedPreferences Editor
     * @param key SharedPreferences key under which to store the bundle data. Note this key must
     *            not contain '§§' as it's used as a delimiter
     * @param preferences Bundled preferences
     */
    public static void savePreferencesBundle(SharedPreferences.Editor editor, String key, Bundle preferences) {
        Set<String> keySet = preferences.keySet();
        Iterator<String> it = keySet.iterator();
        String prefKeyPrefix = key + SAVED_PREFS_BUNDLE_KEY_SEPARATOR;

        while (it.hasNext()){
            String bundleKey = it.next();
            Object o = preferences.get(bundleKey);
            if (o == null){
                editor.remove(prefKeyPrefix + bundleKey);
            } else if (o instanceof Integer){
                editor.putInt(prefKeyPrefix + bundleKey, (Integer) o);
            } else if (o instanceof Long){
                editor.putLong(prefKeyPrefix + bundleKey, (Long) o);
            } else if (o instanceof Boolean){
                editor.putBoolean(prefKeyPrefix + bundleKey, (Boolean) o);
            } else if (o instanceof CharSequence){
                editor.putString(prefKeyPrefix + bundleKey, ((CharSequence) o).toString());
            } else if (o instanceof Bundle){
                savePreferencesBundle(editor, prefKeyPrefix + bundleKey, ((Bundle) o));
            }
        }
    }

    /**
     * Load a Bundle object from SharedPreferences.
     * (that was previously stored using savePreferencesBundle())
     *
     * NOTE: The editor must be writable, and this function does not commit.
     *
     * @param sharedPreferences SharedPreferences
     * @param key SharedPreferences key under which to store the bundle data. Note this key must
     *            not contain '§§' as it's used as a delimiter
     *
     * @return bundle loaded from SharedPreferences
     */
    public static Bundle loadPreferencesBundle(SharedPreferences sharedPreferences, String key) {
        Bundle bundle = new Bundle();
        Map<String, ?> all = sharedPreferences.getAll();
        Iterator<String> it = all.keySet().iterator();
        String prefKeyPrefix = key + SAVED_PREFS_BUNDLE_KEY_SEPARATOR;
        Set<String> subBundleKeys = new HashSet<String>();

        while (it.hasNext()) {

            String prefKey = it.next();

            if (prefKey.startsWith(prefKeyPrefix)) {
                String bundleKey = StringUtils.removeStart(prefKey, prefKeyPrefix);

                if (!bundleKey.contains(SAVED_PREFS_BUNDLE_KEY_SEPARATOR)) {

                    Object o = all.get(prefKey);
                    if (o == null) {
                        // Ignore null keys
                    } else if (o instanceof Integer) {
                        bundle.putInt(bundleKey, (Integer) o);
                    } else if (o instanceof Long) {
                        bundle.putLong(bundleKey, (Long) o);
                    } else if (o instanceof Boolean) {
                        bundle.putBoolean(bundleKey, (Boolean) o);
                    } else if (o instanceof CharSequence) {
                        bundle.putString(bundleKey, ((CharSequence) o).toString());
                    }
                }
                else {
                    // Key is for a sub bundle
                    String subBundleKey = StringUtils.substringBefore(bundleKey, SAVED_PREFS_BUNDLE_KEY_SEPARATOR);
                    subBundleKeys.add(subBundleKey);
                }
            }
            else {
                // Key is not related to this bundle.
            }
        }

        // Recursively process the sub-bundles
        for (String subBundleKey : subBundleKeys) {
            Bundle subBundle = loadPreferencesBundle(sharedPreferences, prefKeyPrefix + subBundleKey);
            bundle.putBundle(subBundleKey, subBundle);
        }


        return bundle;
    }


    public SharedPreferenceUtil putStringSet(String key, Set<String> values) {
        mPrefs.edit().putStringSet(key, values).apply();
        return this;
    }

    public Set<String> getStringSet(String key) {
        return mPrefs.getStringSet(key, new HashSet<String>());
    }

    public SharedPreferenceUtil putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SharedPreferenceUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public SharedPreferenceUtil putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    // 设置当前小时
    public void setCurrentHour(int h) {
        mPrefs.edit().putInt(HOUR, h).apply();
    }

    public int getCurrentHour() {
        return mPrefs.getInt(HOUR, 0);
    }

    // 图标种类相关
    public void setIconType(int type) {
        mPrefs.edit().putInt(CHANGE_ICONS, type).apply();
    }

    public int getIconType() {
        return mPrefs.getInt(CHANGE_ICONS, 0);
    }

    // 自动更新时间 hours
    public void setAutoUpdate(int t) {
        mPrefs.edit().putInt(AUTO_UPDATE, t).apply();
    }

    public int getAutoUpdate() {
        return mPrefs.getInt(AUTO_UPDATE, 3);
    }

    //当前城市
    public void setCityName(String name) {
        mPrefs.edit().putString(CITY_NAME, name).apply();
    }

    public String getCityName() {
        return mPrefs.getString(CITY_NAME, "北京");
    }

    //  通知栏模式 默认为常驻
    public void setNotificationModel(int t) {
        mPrefs.edit().putInt(NOTIFICATION_MODEL, t).apply();
    }

    public int getNotificationModel() {
        return mPrefs.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
    }

    // 首页 Item 动画效果 默认关闭

    public void setMainAnim(boolean b) {
        mPrefs.edit().putBoolean(ANIM_START, b).apply();
    }

    public boolean getMainAnim() {
        return mPrefs.getBoolean(ANIM_START, false);
    }
}
