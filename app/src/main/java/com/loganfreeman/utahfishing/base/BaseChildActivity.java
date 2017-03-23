package com.loganfreeman.utahfishing.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by shanhong on 3/21/17.
 */

public class BaseChildActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void safeSetTitle(String title) {
        ActionBar appBarLayout = getSupportActionBar();
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
    }
}
