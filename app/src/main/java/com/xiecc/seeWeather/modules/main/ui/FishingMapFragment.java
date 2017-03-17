package com.xiecc.seeWeather.modules.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.AbstractBaseFragment;

import butterknife.ButterKnife;

/**
 * Created by shanhong on 3/17/17.
 */

public class FishingMapFragment extends AbstractBaseFragment {
    private View view;
    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fishing_map, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        load();
    }

    private void initView() {
    }

    private void load() {

    }
}
