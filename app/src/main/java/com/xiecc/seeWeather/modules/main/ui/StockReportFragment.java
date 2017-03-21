package com.xiecc.seeWeather.modules.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.AbstractBaseFragment;
import com.xiecc.seeWeather.base.BaseActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.common.utils.ToastUtil;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.ui.StockReportAdapter;
import com.xiecc.seeWeather.modules.fishing.ui.StockReportHeaderAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportFragment extends AbstractBaseFragment {
    @Bind(R.id.tableView)
    TableView tableView;
    private View view;

    private StockReportAdapter adapter;

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fishing_stock_report, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        safeSetTitle(getString(R.string.stocking));
        load();
    }

    private void initView(List<StockReport> stockReports) {
        adapter = new StockReportAdapter(StockReportFragment.this.getActivity(), stockReports);
        tableView.setHeaderAdapter(new StockReportHeaderAdapter(StockReportFragment.this.getActivity(), 6));
        tableView.setDataAdapter(adapter);
        tableView.addDataClickListener(new TableDataClickListener<StockReport>() {
            @Override
            public void onDataClicked(int rowIndex, StockReport clickedData) {
                StockReportActivity.start(getActivity(), clickedData);
            }


        });
    }

    private void load() {
        StockReport.getStockReportAsync().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<StockReport>>() {
                    @Override
                    public void onCompleted() {
                        ToastUtil.showShort("complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        PLog.e(Log.getStackTraceString(e));
                    }

                    @Override
                    public void onNext(List<StockReport> stockReports) {
                        initView(stockReports);
                    }
                });
    }
}
