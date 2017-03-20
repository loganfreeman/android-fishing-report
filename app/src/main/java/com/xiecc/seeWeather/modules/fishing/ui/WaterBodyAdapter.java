package com.xiecc.seeWeather.modules.fishing.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import java.util.List;

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

    public WaterBody search(String query) {
        WaterBody found = null;
        outerloop:
        for(WaterBody item : items) {
            query = query.toLowerCase();
            String[] words = query.split("\\s+");
            boolean all = true;
            String name = item.name.toLowerCase();

            for(String word : words) {
                if (!name.contains(word)) {
                    all = false;
                }
            }

            if(all) {
                found = item;
                break outerloop;
            }
        }
        return found;
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
        return (long) getItem(position).id;
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

    private class MyViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivIcon;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvTitle);
            tvDesc = (TextView) item.findViewById(R.id.tvDesc);
            ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
        }
    }
}
