package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.ui.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportListActivity extends BaseChildActivity {
    public static final String STOCK_REPORT_LIST = "stock_report_list";
    private List<StockReport> stockReports;

    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    LinearLayoutManager linearLayoutManager;

    SimpleAdapter simpleRecyclerViewAdapter = null;


    public static void start(Context context, List<StockReport> stockReports) {
        Intent intent = new Intent(context, StockReportListActivity.class);
        intent.putParcelableArrayListExtra(STOCK_REPORT_LIST, (ArrayList<? extends Parcelable>) stockReports);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report_list);
        stockReports = getIntent().getExtras().getParcelableArrayList(STOCK_REPORT_LIST);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        safeSetTitle(getString(R.string.utah_stock_report_list));
        ultimateRecyclerView.setHasFixedSize(false);

        simpleRecyclerViewAdapter = new SimpleAdapter(stockReports);

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
    }
}
