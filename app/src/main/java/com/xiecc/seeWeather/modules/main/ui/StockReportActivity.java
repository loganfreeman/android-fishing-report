package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseActivity;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;
import com.xiecc.seeWeather.modules.service.AutoUpdateService;

import org.parceler.Parcels;

import butterknife.ButterKnife;

import static com.xiecc.seeWeather.modules.main.ui.FishingReportActivity.WATER_BODY;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportActivity extends BaseChildActivity {

    public static final String STOCK_REPORT = "stock_report";
    private StockReport stockReport;

    public static void start(Context context, StockReport stockReport) {
        Intent intent = new Intent(context, StockReportActivity.class);
        intent.putExtra(STOCK_REPORT, stockReport);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report);
        stockReport = getIntent().getExtras().getParcelable(STOCK_REPORT);
        safeSetTitle(stockReport.watername);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
    }
}
