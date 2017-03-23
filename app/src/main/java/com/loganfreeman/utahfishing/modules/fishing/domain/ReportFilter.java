package com.loganfreeman.utahfishing.modules.fishing.domain;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scheng on 3/21/17.
 */

@Parcel
public class ReportFilter {


    List<String> statuses = new ArrayList<String>();

    public ReportFilter() {

    }

    public ReportFilter(List<String> statuses){
        this.statuses = statuses;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }


}
