package com.loganfreeman.utahfishing.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseChildActivity;
import com.loganfreeman.utahfishing.common.InternalStorage;
import com.loganfreeman.utahfishing.modules.fishing.domain.WaterBody;
import com.loganfreeman.utahfishing.modules.fishing.ui.WaterBodyAdapter;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by scheng on 3/21/17.
 */

public class FishingReportListActivity extends BaseChildActivity {
    private static final String HOT_SPOT_LIST = "hotspot_list";

    private List<WaterBody> hotspots;

    @Bind(R.id.listview)
    ListView listview;


    public static void gotoFavorite(Context context) {

        List<WaterBody> favorites = InternalStorage.getFavoriteWaterBodies(context);


        start(context, favorites);
    }

    public static void start(Context context, List<WaterBody> hotspots) {
        Intent intent = new Intent(context, FishingReportListActivity.class);
        intent.putExtra(HOT_SPOT_LIST, Parcels.wrap(hotspots));
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_list);
        hotspots = Parcels.unwrap(getIntent().getParcelableExtra(HOT_SPOT_LIST));
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        safeSetTitle(getString(R.string.utah_fishing_report));
        WaterBodyAdapter adapter = new WaterBodyAdapter(this, hotspots);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FishingReportActivity.start(FishingReportListActivity.this, adapter.getItem(position));
            }
        });
    }
}
