package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseActivity;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import org.parceler.Parcels;

import butterknife.ButterKnife;

import static java.nio.charset.CodingErrorAction.REPORT;

/**
 * Created by shanhong on 3/21/17.
 */

public class FishingReportActivity extends BaseChildActivity {

    public static final String WATER_BODY = "water_body";
    private WaterBody waterBody;

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
    }
}
