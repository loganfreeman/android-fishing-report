package com.xiecc.seeWeather.modules.fishing.domain;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.AsyncSubject;
import rx.subjects.PublishSubject;

/**
 * Created by scheng on 3/15/17.
 */

public class WaterBody implements Serializable, ClusterItem {
    public  String name;
    public  double latitude;
    public double longitude;
    public String status;
    public String fish;
    public double id;
    public String href;

    public static final Pattern r = Pattern.compile("(var\\s*waterbody[^;]*;(?=\\s))", Pattern.DOTALL);

    private static final AsyncSubject<List<WaterBody>> mSubject = AsyncSubject.create();

    public static final String UTAH_WILDLIFE_HOTSPOTS = "https://wildlife.utah.gov/hotspots/";

    public LatLng getLatLan() {
        return new LatLng(this.latitude, this.longitude);
    }

    public MarkerOptions getMarkerOptions() {

        return new MarkerOptions()
                .position(this.getLatLan())
                .title(this.name)
                .icon(Status.icon(this.status));
    }

    public MyItem getClusterItem() {
        return new MyItem(this.latitude, this.longitude, this.name, this.status);
    }



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

    public String getUrl() {
        return href;
    }

    public static String getHref(double id) {
        return UTAH_WILDLIFE_HOTSPOTS + "detailed.php?id=" + id;
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

    public static List<WaterBody> getHotspots(String html) {

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


    public static List<WaterBody> fromWildlife() {
        try {
            // Connect to the web site
            Document document = Jsoup.connect(UTAH_WILDLIFE_HOTSPOTS).get();
            Elements elements = document.select("script");
            for(Element script : elements) {
                String html = script.html();
                Matcher m = r.matcher(html);
                if(m.find()) {
                    String text = m.group(1);
                    if(text != null) {
                        return getHotspots(text);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Observable<List<WaterBody>> getWaterbodiesAsync2() {
        Observable<List<WaterBody>> firstTimeObservable =
                Observable.fromCallable(WaterBody::fromWildlife);

        return firstTimeObservable.concatWith(mSubject);
    }

    public static Observable<List<WaterBody>> getWaterbodiesAsync() {
        return Observable.create(new Observable.OnSubscribe<List<WaterBody>>() {

            @Override
            public void call(Subscriber<? super List<WaterBody>> subscriber) {
                try {
                    List<WaterBody> result = fromWildlife();
                    subscriber.onNext(result);    // Pass on the data to subscriber
                    subscriber.onCompleted();     // Signal about the completion subscriber
                } catch (Exception e) {
                    subscriber.onError(e);        // Signal about the error to subscriber
                }
            }
        });
    }

    @Override
    public LatLng getPosition() {
        return getLatLan();
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return status;
    }
}
