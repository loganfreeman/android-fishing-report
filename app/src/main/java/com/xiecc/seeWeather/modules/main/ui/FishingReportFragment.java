package com.xiecc.seeWeather.modules.main.ui;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.AbstractBaseFragment;
import com.xiecc.seeWeather.common.utils.ToastUtil;
import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by scheng on 3/15/17.
 */

public class FishingReportFragment extends AbstractBaseFragment {


    @Bind(R.id.listview)
    ListView listview;

    ArrayAdapter<String> adapter;



    ProgressDialog mProgressDialog;


    private View view;
    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_fishing_report, container, false);
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

    private void load() {
        WaterBody.getWaterbodiesAsync2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<WaterBody>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<WaterBody> waterBodies) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, WaterBody.toStringArray(waterBodies));
                listview.setAdapter(adapter);
                FishingReportFragment.this.adapter = adapter;
            }
        });
    }



    private void initView() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = adapter.getItem(position);
                ToastUtil.showShort(string);

            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class HotSpots extends AsyncTask<Void, Void, Void> {

        List<WaterBody> hotSpots = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Android Basic JSoup Tutorial");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            hotSpots = WaterBody.fromWildlife();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(hotSpots != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, WaterBody.toStringArray(hotSpots));
                listview.setAdapter(adapter);
                FishingReportFragment.this.adapter = adapter;
            }
            mProgressDialog.dismiss();
        }
    }
}
