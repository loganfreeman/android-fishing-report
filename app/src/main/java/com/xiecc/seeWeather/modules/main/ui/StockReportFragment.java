package com.xiecc.seeWeather.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.AbstractBaseFragment;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.common.utils.ToastUtil;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.ui.SimpleAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportFragment extends AbstractBaseFragment {
    @Bind(R.id.ultimate_recycler_view)
    UltimateRecyclerView ultimateRecyclerView;

    private View view;

    LinearLayoutManager linearLayoutManager;

    SimpleAdapter simpleRecyclerViewAdapter = null;

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
        safeSetTitle(getString(R.string.utah_stock_report_list));
        load();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stock_report_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<StockReport> reports = simpleRecyclerViewAdapter.search(query);
                PLog.i("Found reports " + reports.size());
                StockReportListActivity.start(getActivity(), reports);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initView(List<StockReport> stockReports) {
        ultimateRecyclerView.setHasFixedSize(false);

        simpleRecyclerViewAdapter = new SimpleAdapter(stockReports);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);

        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
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
