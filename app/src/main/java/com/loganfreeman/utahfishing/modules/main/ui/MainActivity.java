package com.loganfreeman.utahfishing.modules.main.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseActivity;
import com.loganfreeman.utahfishing.base.Loading;
import com.loganfreeman.utahfishing.common.PLog;
import com.loganfreeman.utahfishing.common.utils.*;
import com.loganfreeman.utahfishing.modules.about.ui.AboutActivity;
import com.loganfreeman.utahfishing.modules.fishing.domain.WaterBody;
import com.loganfreeman.utahfishing.modules.service.AutoUpdateService;
import com.loganfreeman.utahfishing.modules.setting.ui.SettingActivity;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        Loading,
        OnMapReadyCallback {

    private static final String FRAGMENT_TAG = "map";


    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.bottomBar)
    BottomBar mBottomBar;

    @Bind(R.id.loading_avi)
    AVLoadingIndicatorView loading_avi;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    // Declare a variable for the cluster manager.
    private ClusterManager<WaterBody> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        PLog.i("onCreate");
        initView();
        initDrawer();
        // startService(new Intent(this, AutoUpdateService.class));
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        PLog.i("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PLog.i("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        PLog.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        PLog.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PLog.i("onStop");
    }

    /**
     * 初始化基础View
     */
    private void initView() {
        setSupportActionBar(mToolbar);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId) {
                    case R.id.tab_map:
                        showLoading();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer, new MapViewFragment()).commit();
                        break;
                    case R.id.tab_report:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer, new FishingReportFragment()).commit();
                        break;
                    case R.id.tab_stock:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer, new StockReportFragment()).commit();
                        break;
                    case R.id.tab_calendar:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentContainer, new CalendarFragment()).commit();
                        break;
                    default:
                        break;
                }
            }
        });

        mBottomBar.setDefaultTab(R.id.tab_map);
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        //https://segmentfault.com/a/1190000004151222
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
            //navigationView.setItemIconTintList(null);
            mNavView.inflateHeaderView(R.layout.nav_header_main);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }



    private void showFabDialog() {
        new AlertDialog.Builder(MainActivity.this).setTitle("点赞")
                .setMessage("去项目地址给作者个Star，鼓励下作者୧(๑•̀⌄•́๑)૭✧")
                .setPositiveButton("好嘞", (dialog, which) -> {
                    Uri uri = Uri.parse(getString(R.string.app_html));   //指定网址
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);           //指定Action
                    intent.setData(uri);                            //设置Uri
                    MainActivity.this.startActivity(intent);        //启动Activity
                })
                .show();
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        RxDrawer.close(mDrawerLayout).compose(RxUtils.rxSchedulerHelper(AndroidSchedulers.mainThread())).subscribe(
                new SimpleSubscriber<Void>() {
                    @Override
                    public void onNext(Void aVoid) {
                        switch (item.getItemId()) {
                            case R.id.nav_set:
                                SettingActivity.launch(MainActivity.this);
                                break;
                            case R.id.nav_about:
                                AboutActivity.launch(MainActivity.this);
                                break;
                            case R.id.nav_favorite:
                                FishingReportListActivity.gotoFavorite(getApplicationContext());
                                break;
                            case R.id.nav_license:
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nicusa.utdwrmobile&hl=en")));
                                break;

                        }
                    }
                });
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                ToastUtil.showShort(getString(R.string.double_exit));
            } else {
                finish();
            }
        }
    }

    public void showLoading() {
        loading_avi.show();
    }

    public void hideLoading() {
        loading_avi.hide();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //OrmLite.getInstance().close();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        hideLoading();
        mMap = map;
        enableMyLocation();
        getWaterbodies();
    }

    private GoogleMap getMap() {
        return mMap;
    }

    private ClusterManager<WaterBody> getMarkerManager() {
        return mClusterManager;
    }

    private void setupCluster(List<WaterBody> waterBodies) {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<WaterBody>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);

        // Create bounds that include all locations of the map
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for(WaterBody waterbody : waterBodies) {
            boundsBuilder.include(waterbody.getLatLan());
            mClusterManager.addItem(waterbody);
        }

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<WaterBody>() {
                    @Override
                    public boolean onClusterClick(final Cluster<WaterBody> cluster) {
                        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(getMap()
                                        .getCameraPosition().zoom + 1)), 300,
                                null);
                        return true;
                    }
                });


        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<WaterBody>() {
            @Override
            public void onClusterItemInfoWindowClick(WaterBody myItem) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myItem.getUrl()));
                startActivity(browserIntent);
            }
        });

        // Move camera to show all markers and locations
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 20));

    }

    private void addMakers(List<WaterBody> waterBodies) {
        // Create bounds that include all locations of the map
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        for(WaterBody waterbody : waterBodies) {
            mMap.addMarker(waterbody.getMarkerOptions());
            boundsBuilder.include(waterbody.getLatLan());
        }

        // Move camera to show all markers and locations
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 20));
    }


    private void getWaterbodies() {
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
                setupCluster(waterBodies);
            }
        });
    }


    public void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else  {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}
