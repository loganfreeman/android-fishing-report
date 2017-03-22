package com.xiecc.seeWeather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiecc.seeWeather.R;
import com.xiecc.seeWeather.base.BaseChildActivity;
import com.xiecc.seeWeather.common.PLog;
import com.xiecc.seeWeather.common.utils.Ids;
import com.xiecc.seeWeather.common.utils.ToastUtil;
import com.xiecc.seeWeather.modules.fishing.domain.ReportFilter;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.id.message;


/**
 * Created by scheng on 3/21/17.
 */

public class FishingReportFilterActivity extends BaseChildActivity {

    public static final String FILTER = "filter";
    public static final int RESULT_CODE = 2;
    private ReportFilter filter;

    @Bind(R.id.choices)
    RadioGroup status_choice;

    public static void start(Context context, ReportFilter filter) {
        Intent intent = new Intent(context, FishingReportFilterActivity.class);
        intent.putExtra(FILTER, Parcels.wrap(filter));
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, ReportFilter filter) {
        Intent intent = new Intent(context, FishingReportFilterActivity.class);
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
        int i = 0;
        for(String status : filter.getStatuses()) {
            RadioButton button = new RadioButton(this);
            button.setId(Ids.getId(i++));
            button.setText(status);
            status_choice.addView(button);
        }
        status_choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int idx = Ids.getIndex(checkedId);
                //ToastUtil.showLong(filter.getStatuses().get(idx));
                Intent intent=new Intent();
                intent.putExtra("MESSAGE",filter.getStatuses().get(idx));
                setResult(RESULT_CODE,intent);
                finish();//finishing activity
            }
        });
    }

}
