package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.modules.fishing.domain.ReportFilter;

import org.parceler.Parcels;

import butterknife.ButterKnife;


/**
 * Created by scheng on 3/21/17.
 */

public class FishingReportFilterActivity extends BaseChildActivity {

    public static final String FILTER = "filter";
    private ReportFilter filter;

    public static void start(Context context, ReportFilter filter) {
        Intent intent = new Intent(context, FishingReportFilterActivity.class);
        intent.putExtra(FILTER, Parcels.wrap(filter));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_report_filter);
        filter = Parcels.unwrap(getIntent().getParcelableExtra(FILTER));
        safeSetTitle(getString(R.string.choose_report_filter));
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
    }

}
