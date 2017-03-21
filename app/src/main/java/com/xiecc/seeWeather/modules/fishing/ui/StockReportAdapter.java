package com.xiecc.seeWeather.modules.fishing.ui;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiecc.seeWeather.modules.fishing.domain.StockReport;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReportAdapter extends TableDataAdapter<StockReport> {
    private List<StockReport> items;
    public StockReportAdapter(Context context, List<StockReport> data) {
        super(context, data);
        this.items = data;
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

    public List<StockReport> search(String query) {
        List<StockReport> reports = new ArrayList<StockReport>();
        outerloop:
        for(StockReport item : items) {
            query = query.toLowerCase();
            String[] words = query.split("\\s+");
            boolean all = true;
            String name = item.watername.toLowerCase();

            for(String word : words) {
                if (!name.contains(word)) {
                    all = false;
                }
            }

            if(all) {
                reports.add(item);
            }
        }
        return reports;
    }
}
