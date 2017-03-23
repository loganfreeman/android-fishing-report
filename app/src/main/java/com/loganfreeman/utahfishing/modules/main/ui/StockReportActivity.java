package com.loganfreeman.utahfishing.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseChildActivity;
import com.loganfreeman.utahfishing.modules.fishing.domain.StockReport;

import butterknife.ButterKnife;

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
