package com.loganfreeman.utahfishing.common.decorators;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseApplication;
import com.loganfreeman.utahfishing.common.utils.Time;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.loc.e.d;

/**
 * Decorate several days with a dot
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EventDecorator implements DayViewDecorator {

    private int color;
    List<String> dates;
    Drawable drawable = BaseApplication.getAppContext().getDrawable(R.drawable.fish_selector);

    public EventDecorator(int color, List<String> dates) {
        this.color = color;
        this.dates = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.stream().anyMatch( d -> Time.toCalendarDay(d).equals(day));
    }

    @Override
    public void decorate(DayViewFacade view) {
        // view.addSpan(new DotSpan(5, color));
        view.setBackgroundDrawable(drawable);
    }
}