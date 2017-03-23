package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseActivity;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil;
import com.xiecc.seeWeather.common.utils.StringUtils;
import com.xiecc.seeWeather.common.utils.ToastUtil;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import org.parceler.Parcels;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.xiecc.seeWeather.common.utils.SharedPreferenceUtil.FAVORITE_WATRER;
import static java.nio.charset.CodingErrorAction.REPORT;

/**
 * Created by shanhong on 3/21/17.
 */

public class FishingReportActivity extends BaseChildActivity {

    public static final String WATER_BODY = "water_body";
    private WaterBody waterBody;

    @Bind(R.id.webview)
    WebView webView;

    @Bind(R.id.bt_direction)
    ImageView btnDirection;

    @Bind(R.id.bt_share)
    ImageView btnShare;

    @Bind(R.id.bt_favorite)
    ImageView btnFavorite;

    public static void start(Context context, WaterBody waterBody) {
        Intent intent = new Intent(context, FishingReportActivity.class);
        intent.putExtra(WATER_BODY, Parcels.wrap(waterBody));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_report);
        waterBody = Parcels.unwrap(getIntent().getParcelableExtra(WATER_BODY));
        PLog.i(waterBody.toString());
        safeSetTitle(waterBody.getName());
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        webView.loadUrl(waterBody.getUrl());
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLocation(waterBody.getLatitude(), waterBody.getLongitude());
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFavorite();
            }
        });
    }

    private void saveFavorite() {
        Set<String> values = SharedPreferenceUtil.getInstance().getStringSet(FAVORITE_WATRER);
        values.add(waterBody.getName());
        SharedPreferenceUtil.getInstance().putStringSet(FAVORITE_WATRER, values);
        ToastUtil.showLong(String.format("Saved %s to your preferences", waterBody.getName()));
    }

    private void gotoLocation(double lat, double lon) {
        Uri gmmIntentUri = Uri.parse(String.format("geo:%f,%f", lat, lon));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_SUBJECT, waterBody.getName());
        share.putExtra(Intent.EXTRA_TEXT, waterBody.getUrl());

        startActivity(Intent.createChooser(share, "Share link!"));
    }

}
