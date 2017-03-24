package com.loganfreeman.utahfishing.modules.main.ui;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
import com.loganfreeman.utahfishing.common.decorators.EventDecorator;
import com.loganfreeman.utahfishing.common.decorators.HighlightWeekendsDecorator;
import com.loganfreeman.utahfishing.common.decorators.MySelectorDecorator;
import com.loganfreeman.utahfishing.common.decorators.OneDayDecorator;
import com.loganfreeman.utahfishing.common.utils.StringUtils;
import com.loganfreeman.utahfishing.common.utils.ToastUtil;
import com.loganfreeman.utahfishing.modules.fishing.domain.StockReport;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by scheng on 3/23/17.
 */

public class CalendarFragment extends AbstractBaseFragment implements OnDateSelectedListener, OnMonthChangedListener {

    List<StockReport> stockReports;

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
        //widget.invalidateDecorators();
        StockReportListActivity.start(getActivity(), StockReport.filterByCalendarDay(stockReports, date));
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

    @TargetApi(Build.VERSION_CODES.N)
    private void initCalendar(List<StockReport> stockReports) {

        CalendarFragment.this.stockReports = stockReports;

        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        List<String> calendarDays = stockReports.stream().map( u -> u.stockdate ).collect(Collectors.toList());

        //PLog.e(calendarDays.stream().map(Object::toString).collect(Collectors.joining("\n")));

        //PLog.e(stockReports.stream().map(u -> df.format(u.stockdate)).collect(Collectors.joining("\n")));



        widget.addDecorator(new EventDecorator(Color.RED, calendarDays));

        widget.addDecorators(
                new HighlightWeekendsDecorator()
        );

    }
}
