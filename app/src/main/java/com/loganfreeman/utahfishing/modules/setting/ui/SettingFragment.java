package com.loganfreeman.utahfishing.modules.setting.ui;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseApplication;
import com.loganfreeman.utahfishing.common.utils.FileSizeUtil;
import com.loganfreeman.utahfishing.common.utils.FileUtil;
import com.loganfreeman.utahfishing.common.utils.RxUtils;
import com.loganfreeman.utahfishing.common.utils.SharedPreferenceUtil;
import com.loganfreeman.utahfishing.common.utils.SimpleSubscriber;
import com.loganfreeman.utahfishing.component.ImageLoader;
import com.loganfreeman.utahfishing.component.RxBus;
import com.loganfreeman.utahfishing.modules.main.domain.ChangeCityEvent;
import com.loganfreeman.utahfishing.modules.main.ui.MainActivity;
import com.loganfreeman.utahfishing.modules.service.AutoUpdateService;
import java.io.File;
import rx.Observable;
import rx.functions.Func1;

public class SettingFragment extends PreferenceFragment
    implements Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {
    private static String TAG = SettingFragment.class.getSimpleName();
    //private SettingActivity mActivity;
    private SharedPreferenceUtil mSharedPreferenceUtil;
    private Preference mClearCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();

        mClearCache = findPreference(SharedPreferenceUtil.CLEAR_CACHE);




        mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(BaseApplication.getAppCacheDir() + "/NetCache"));


        mClearCache.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mClearCache == preference) {

            ImageLoader.clear(getActivity());
            Observable.just(FileUtil.delete(new File(BaseApplication.getAppCacheDir() + "/NetCache")))
                    .filter(new Func1<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean aBoolean) {
                            return aBoolean;
                        }
                    }).compose(RxUtils.rxSchedulerHelper()).subscribe(new SimpleSubscriber<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(BaseApplication.getAppCacheDir() + "/NetCache"));
                    Snackbar.make(getView(), "Cache cleared", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
