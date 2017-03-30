package com.loganfreeman.utahfishing.common.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import com.loganfreeman.utahfishing.component.RetrofitSingleton;
import com.loganfreeman.utahfishing.modules.about.domain.VersionAPI;

public class CheckVersion {

    public static void checkVersion(Context context) {
        RetrofitSingleton.getInstance().fetchVersion()
            .subscribe(new SimpleSubscriber<VersionAPI>() {
                @Override
                public void onNext(VersionAPI versionAPI) {
                    String firVersionName = versionAPI.versionShort;
                    String currentVersionName = Util.getVersion(context);
                    if (currentVersionName.compareTo(firVersionName) < 0) {
                        if (!SharedPreferenceUtil.getInstance().getString("version", "").equals(versionAPI.versionShort)) {
                            showUpdateDialog(versionAPI, context);
                        }
                    }
                }
            });
    }

    public static void checkVersion(Context context, boolean force) {
        RetrofitSingleton.getInstance().fetchVersion()
            .subscribe(new SimpleSubscriber<VersionAPI>() {
                @Override
                public void onNext(VersionAPI versionAPI) {
                    String firVersionName = versionAPI.versionShort;
                    String currentVersionName = Util.getVersion(context);
                    if (currentVersionName.compareTo(firVersionName) < 0) {
                        showUpdateDialog(versionAPI, context);
                    } else {
                        ToastUtil.showShort("Already the most recent version(⌐■_■)");
                    }
                }
            });
    }

    public static void showUpdateDialog(VersionAPI versionAPI, final Context context) {
        String title = "Find new version " + versionAPI.name + " Version number：" + versionAPI.versionShort;

        new AlertDialog.Builder(context).setTitle(title)
            .setMessage(versionAPI.changelog)
            .setPositiveButton("Download", (dialog, which) -> {
                Uri uri = Uri.parse(versionAPI.updateUrl);   //指定网址
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);           //指定Action
                intent.setData(uri);                            //设置Uri
                context.startActivity(intent);        //启动Activity
            })
            .setNegativeButton("Skip this version", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferenceUtil.getInstance().putString("version", versionAPI.versionShort);
                }
            })
            .show();
    }
}
