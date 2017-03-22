package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;
import com.xiecc.seeWeather.modules.fishing.ui.WaterBodyAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
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
    }
}
