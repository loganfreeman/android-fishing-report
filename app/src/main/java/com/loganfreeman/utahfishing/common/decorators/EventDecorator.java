package com.loganfreeman.utahfishing.common.decorators;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.common.PLog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by scheng on 3/23/17.
 */

public class EventDecorator implements DayViewDecorator {

    private HashSet<CalendarDay> dates;
    private final Drawable drawable;

    public EventDecorator(Context context, Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);

        drawable = context.getResources().getDrawable(R.drawable.my_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
