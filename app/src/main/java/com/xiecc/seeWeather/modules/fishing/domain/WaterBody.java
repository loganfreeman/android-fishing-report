package com.xiecc.seeWeather.modules.fishing.domain;

import com.xiecc.seeWeather.modules.main.ui.FishingReportFragment;

import org.mozilla.javascript.NativeArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by scheng on 3/15/17.
 */

public class WaterBody implements Serializable {
    public  String name;
    public  double latitude;
    public double longitude;
    public String status;
    public String fish;
    public double id;
    public String href;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" (");
        sb.append(status);
        sb.append(")");
        return sb.toString();
    }

    public static List<String> toStringArray(List<WaterBody> list) {
        List<String> strings = new ArrayList<>(list.size());
        for (Object object : list) {
            strings.add(Objects.toString(object, null));
        }
        return strings;
    }

    public static String getHref(double id) {
        return FishingReportFragment.UTAH_WILDLIFE_HOTSPOTS + "detailed.php?id=" + id;
    }
    public static WaterBody fromV8Array(NativeArray array) {
        WaterBody waterBody = new WaterBody();
        waterBody.name = (String)array.get(0);
        waterBody.latitude = (double) array.get(1);
        waterBody.longitude = (double) array.get(2);
        waterBody.status = (String)array.get(4);
        waterBody.fish = (String)array.get(5);
        waterBody.id = (double)array.get(3);
        waterBody.href = getHref(waterBody.id);
        return waterBody;
    }

}
