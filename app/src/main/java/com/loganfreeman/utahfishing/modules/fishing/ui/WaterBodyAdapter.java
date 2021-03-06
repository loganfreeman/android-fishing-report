package com.loganfreeman.utahfishing.modules.fishing.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.modules.fishing.domain.ReportFilter;
import com.loganfreeman.utahfishing.modules.fishing.domain.WaterBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shanhong on 3/20/17.
 */

public class WaterBodyAdapter extends BaseAdapter {

    Context context;

    List<WaterBody> items;

    LayoutInflater inflater;

    public WaterBodyAdapter(Context context, List<WaterBody> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(this.context);
    }

    public List<WaterBody> filterByStatus(String query) {
        return WaterBody.filterByStatus(this.items, query);
    }

    public WaterBody search(String query) {
        return WaterBody.search(this.items, query);
    }


    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public WaterBody getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.waterbody_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        WaterBody currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData.getTitle());
        mViewHolder.tvDesc.setText(currentListData.getSnippet());
        //mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

        return convertView;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public ReportFilter getFilter() {
        ReportFilter filter = new ReportFilter();
        filter.getStatuses().addAll(items.stream().map(WaterBody::getStatus).distinct().collect(Collectors.toList()));
        return filter;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivIcon;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvTitle);
            tvDesc = (TextView) item.findViewById(R.id.tvDesc);
        }
    }
}
