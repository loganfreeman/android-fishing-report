package com.xiecc.seeWeather.modules.fishing.ui;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiecc.seeWeather.modules.fishing.domain.StockReport;

import java.text.DateFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportAdapter extends TableDataAdapter<StockReport> {
    public StockReportAdapter(Context context, List<StockReport> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        StockReport report = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderTextView(report.watername);
                break;
            case 1:
                renderedView = renderTextView(report.county);
                break;
            case 2:
                renderedView = renderTextView(report.species);
                break;
            case 3:
                renderedView = renderTextView(String.valueOf(report.quantity));
            case 4:
                renderedView = renderTextView(String.valueOf(report.length));
            case 5:
                renderedView = renderTextView(DateFormat.getDateTimeInstance().format(report.stockdate));
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
