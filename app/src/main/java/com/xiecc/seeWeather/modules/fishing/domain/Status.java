package com.xiecc.seeWeather.modules.fishing.domain;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by shanhong on 3/20/17.
 */

public enum Status {

    Good("Good"),

    Fair("Fair"),

    Slow("Slow"),

    Unstable_Ice("Unstable Ice"),

    Closed("Closed"),

    No_Recent_Report("No Recent Report");

    private final String status;

    private Status(String s) {
        status = s;
    }

    public String toString() {
        return this.status;
    }

    public static BitmapDescriptor icon(String s) {
        switch (s) {
            case "Good":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
            case "Fair":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            case "Slow":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            case "Closed":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            case "No Recent Report":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            case "Unstable Ice":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
            default:
                break;
        }
        return null;
    }

    public String status() {
        return status;
    }
}
