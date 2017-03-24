package com.loganfreeman.utahfishing.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.AbstractBaseFragment;
import com.loganfreeman.utahfishing.common.PLog;
import com.loganfreeman.utahfishing.common.utils.ToastUtil;
import com.loganfreeman.utahfishing.modules.fishing.domain.StockReport;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by scheng on 3/23/17.
 */

public class CalendarFragment extends AbstractBaseFragment implements OnDateSelectedListener, OnMonthChangedListener {

    @Bind(R.id.calendarView)
    MaterialCalendarView widget;
    private View view;

    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fishing_calenar, container, false);
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
        safeSetTitle(getString(R.string.utah_fishing_calendar));
        load();
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

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
                        initCalendar(stockReports);
                    }
                });
    }

    private void initCalendar(List<StockReport> stockReports) {

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

    }
}
