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


/**
 * Created by scheng on 3/15/17.
 */

public class FishingReportFragment extends AbstractBaseFragment {

    public static final String UTAH_WILDLIFE_HOTSPOTS = "https://wildlife.utah.gov/hotspots/";

    @Bind(R.id.listview)
    ListView listview;

    ArrayAdapter<String> adapter;



    ProgressDialog mProgressDialog;

    Pattern r = Pattern.compile("(var\\s*waterbody[^;]*;(?=\\s))", Pattern.DOTALL);

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
        new HotSpots().execute();
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



    private List<WaterBody> getHotspots(String html) {

        List<WaterBody> waterBodies = new ArrayList<WaterBody>();

        Context rhino = Context.enter();

        // Turn off optimization to make Rhino Android compatible
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            // Note the forth argument is 1, which means the JavaScript source has
            // been compressed to only one line using something like YUI
            rhino.evaluateString(scope, html, "JavaScript", 1, null);

            // Get the variable waterbody defined in JavaScriptCode
            Object obj = scope.get("waterbody", scope);
            if (obj instanceof NativeArray) {
                NativeArray array = (NativeArray) obj;
                long len = array.getLength();
                for(int i = 0; i< len; i++) {
                    Object item = array.get(i);
                    if(item instanceof NativeArray) {
                        waterBodies.add(WaterBody.fromV8Array((NativeArray) item));
                    }
                }
            }

        } finally {
            Context.exit();
        }

        return waterBodies;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class HotSpots extends AsyncTask<Void, Void, Void> {

        String text = null;
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

            try {
                // Connect to the web site
                Document document = Jsoup.connect(UTAH_WILDLIFE_HOTSPOTS).get();
                Elements elements = document.select("script");
                for(Element script : elements) {
                    String html = script.html();
                    Matcher m = r.matcher(html);
                    if(m.find()) {
                        text = m.group(1);
                        if(text != null) {
                            hotSpots = getHotspots(text);
                            break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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
