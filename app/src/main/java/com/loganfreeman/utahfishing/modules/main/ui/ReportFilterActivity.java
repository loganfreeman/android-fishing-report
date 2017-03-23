package com.loganfreeman.utahfishing.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loganfreeman.utahfishing.R;
import com.loganfreeman.utahfishing.base.BaseChildActivity;
import com.loganfreeman.utahfishing.modules.fishing.domain.ReportFilter;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by scheng on 3/21/17.
 */

public class ReportFilterActivity extends BaseChildActivity implements View.OnClickListener {

    public static final String FILTER = "filter";
    public static final int RESULT_CODE = 2;
    private ReportFilter filter;

    @Bind(R.id.choices)
    RadioGroup status_choice;

    public static void start(Context context, ReportFilter filter) {
        Intent intent = new Intent(context, ReportFilterActivity.class);
        intent.putExtra(FILTER, Parcels.wrap(filter));
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, ReportFilter filter) {
        Intent intent = new Intent(context, ReportFilterActivity.class);
        intent.putExtra(FILTER, Parcels.wrap(filter));
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishing_report_filter);
        filter = Parcels.unwrap(getIntent().getParcelableExtra(FILTER));
        safeSetTitle(getString(R.string.choose_report_filter));
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        status_choice.removeAllViews();
        for(String status : filter.getStatuses()) {
            RadioButton button = new RadioButton(this);
            button.setText(status);
            button.setOnClickListener(this);
            status_choice.addView(button);
        }

    }

    @Override
    public void onClick(View v) {
        String btnText = ((RadioButton) v).getText().toString();

        Intent intent=new Intent();
        intent.putExtra("MESSAGE",btnText);
        setResult(RESULT_CODE,intent);
        finish();//finishing activity
    }
}
