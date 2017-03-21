package com.xiecc.seeWeather.modules.fishing.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;

import de.codecrafters.tableview.TableHeaderAdapter;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportHeaderAdapter extends TableHeaderAdapter {
    public StockReportHeaderAdapter(Context context, int columnCount) {
        super(context, columnCount);
    }

    @Override
    public View getHeaderView(int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderTextView("Water name");
                break;
            case 1:
                renderedView = renderTextView("County");
                break;
            case 2:
                renderedView = renderTextView("Species");
                break;
            case 3:
                renderedView = renderTextView("Quantity");
            case 4:
                renderedView = renderTextView("Average length");
            case 5:
                renderedView = renderTextView("Stock date");
                break;
        }

        return renderedView;
    }

    private View renderTextView(String text) {
        final TextView textView = new TextView(getContext());
        textView.setText(text);
        return textView;
    }
}
