package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.ui.StockReportAdapter;
import com.xiecc.seeWeather.modules.fishing.ui.StockReportHeaderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportListActivity extends BaseChildActivity {
    public static final String STOCK_REPORT_LIST = "stock_report_list";
    private List<StockReport> stockReports;

    @Bind(R.id.tableView)
    TableView tableView;

    private StockReportAdapter adapter;

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
        adapter = new StockReportAdapter(this, stockReports);
        tableView.setHeaderAdapter(new StockReportHeaderAdapter(this, 6));
        tableView.setDataAdapter(adapter);
        tableView.addDataClickListener(new TableDataClickListener<StockReport>() {
            @Override
            public void onDataClicked(int rowIndex, StockReport clickedData) {
                StockReportActivity.start(getApplicationContext(), clickedData);
            }


        });
    }
}
