package com.loganfreeman.utahfishing.modules.main.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.AbstractBaseFragment;
import com.loganfreeman.utahfishing.modules.fishing.domain.WaterBody;
import com.loganfreeman.utahfishing.modules.fishing.ui.WaterBodyAdapter;


import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.loganfreeman.utahfishing.modules.main.ui.ReportFilterActivity.RESULT_CODE;


/**
 * Created by scheng on 3/15/17.
 */

public class FishingReportFragment extends AbstractBaseFragment {


    @Bind(R.id.listview)
    ListView listview;

    WaterBodyAdapter adapter;



    ProgressDialog progress;


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
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        safeSetTitle(getString(R.string.utah_fishing_report));
        load();
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fishing_report_listview_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                WaterBody waterBody = adapter.search(query);
                if(waterBody != null) {
                    FishingReportActivity.start(getActivity(), waterBody);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.action_filter:
                //ReportFilterActivity.start(getActivity(), adapter.getFilter());
                if(adapter != null) {
                    startActivityForResult(ReportFilterActivity.getIntent(getActivity(), adapter.getFilter()), RESULT_CODE);
                }
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == RESULT_CODE && data != null ) {
            String filter = data.getStringExtra("MESSAGE");
            FishingReportListActivity.start(getActivity(), adapter.filterByStatus(filter));
        }
    }

    private void startWebview(int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adapter.getItem(position).getUrl()));
        startActivity(browserIntent);
    }

    private void showLoading() {
        progress = new ProgressDialog(this.getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    private void dismissLoading() {
        progress.dismiss();
        progress = null;
    }


    private void load() {
        showLoading();
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
                initView(waterBodies);

            }
        });
    }



    private void initView(List<WaterBody> waterBodies) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FishingReportActivity.start(getActivity(), adapter.getItem(position));

            }
        });

        WaterBodyAdapter adapter = new WaterBodyAdapter(FishingReportFragment.this.getActivity(), waterBodies);
        listview.setAdapter(adapter);
        FishingReportFragment.this.adapter = adapter;
        dismissLoading();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
