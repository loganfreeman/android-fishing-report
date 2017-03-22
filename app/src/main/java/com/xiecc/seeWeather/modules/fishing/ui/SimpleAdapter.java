package com.xiecc.seeWeather.modules.fishing.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.modules.fishing.domain.StockReport;

import java.util.List;

/**
 * Created by shanhong on 3/22/17.
 */

public class SimpleAdapter extends UltimateViewAdapter {
    private List<StockReport> stockReports;

    public SimpleAdapter(List<StockReport> stockReports) {
        this.stockReports = stockReports;
    }
    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_stock_report_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return stockReports.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StockReport report = stockReports.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.watername.setText(report.watername);
        viewHolder.county.setText(report.county);
        viewHolder.species.setText(report.species);
        viewHolder.quantity.setText(String.valueOf(report.quantity));
        viewHolder.length.setText(String.valueOf(report.length));
        viewHolder.stockdate.setText(StockReport.df.format(report.stockdate));

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public List<StockReport> search(String query) {
        return StockReport.search(this.stockReports, query);
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        TextView watername, county, species, quantity, length, stockdate;
        public ViewHolder(View itemView) {
            super(itemView);

            watername = (TextView) itemView.findViewById(R.id.waterName);
            county = (TextView) itemView.findViewById(R.id.county);
            species = (TextView) itemView.findViewById(R.id.species);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            length = (TextView) itemView.findViewById(R.id.length);
            stockdate = (TextView) itemView.findViewById(R.id.stockdate);

        }
    }
}
