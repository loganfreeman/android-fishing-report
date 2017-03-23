package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.InternalStorage;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.common.utils.SharedPreferenceUtil;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;
import com.xiecc.seeWeather.modules.fishing.ui.WaterBodyAdapter;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.xiecc.seeWeather.common.utils.SharedPreferenceUtil.FAVORITE_WATRER;


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
