package com.loganfreeman.utahfishing.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.AbstractBaseFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        initView();
    }

    private void initView() {
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
