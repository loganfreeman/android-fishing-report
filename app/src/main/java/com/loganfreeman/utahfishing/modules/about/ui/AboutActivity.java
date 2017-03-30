package com.loganfreeman.utahfishing.modules.about.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseActivity;
import com.loganfreeman.utahfishing.common.utils.CheckVersion;
import com.loganfreeman.utahfishing.common.utils.StatusBarUtil;
import com.loganfreeman.utahfishing.common.utils.Util;

public class AboutActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.tv_version)
    TextView mTvVersion;

    @Bind(R.id.bt_share)
    Button mBtShare;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        StatusBarUtil.setImmersiveStatusBar(this);
        StatusBarUtil.setImmersiveStatusBarToolbar(mToolbar,this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        mTvVersion.setText(String.format("Current version: %s (Build %s)", Util.getVersion(this), Util.getVersionCode(this)));
        mToolbarLayout.setTitleEnabled(false);
        // TODO: 2016/12/4 这里有个 bug
        //mToolbarLayout.setTitle(getString(R.string.app_name));
        mToolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({ R.id.bt_share })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                gotoPlayStore();
                break;
        }
    }

    private void gotoPlayStore() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}
