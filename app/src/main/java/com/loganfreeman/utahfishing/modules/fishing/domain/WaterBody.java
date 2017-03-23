package com.loganfreeman.utahfishing.modules.fishing.domain;


import android.annotation.TargetApi;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.loganfreeman.utahfishing.common.PLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.parceler.Parcel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.AsyncSubject;

/**
 * Created by scheng on 3/15/17.
 */

@Parcel
public class WaterBody implements Serializable, ClusterItem {
    String name;
    double latitude;
    double longitude;
    String status;
    String fish;
    double id;
    String href;

    public static final Pattern r = Pattern.compile("(var\\s*waterbody[^;]*;(?=\\s))", Pattern.DOTALL);

    private static final AsyncSubject<List<WaterBody>> mSubject = AsyncSubject.create();

    public static final String UTAH_WILDLIFE_HOTSPOTS = "https://wildlife.utah.gov/hotspots/";

    public LatLng getLatLan() {
        return new LatLng(this.getLatitude(), this.getLongitude());
    }

    public MarkerOptions getMarkerOptions() {

        return new MarkerOptions()
                .position(this.getLatLan())
                .title(this.getName())
                .icon(Status.icon(this.getStatus()));
    }

    public MyItem getClusterItem() {
        return new MyItem(this.getLatitude(), this.getLongitude(), this.getName(), this.getStatus());
    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(" (");
        sb.append(getStatus());
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
        return getHref();
    }

    public static String getHref(double id) {
        return UTAH_WILDLIFE_HOTSPOTS + "detailed.php?id=" + id;
    }
    public static WaterBody fromV8Array(NativeArray array) {
        WaterBody waterBody = new WaterBody();
        waterBody.setName((String)array.get(0));
        waterBody.setLatitude((double) array.get(1));
        waterBody.setLongitude((double) array.get(2));
        waterBody.setStatus((String)array.get(4));
        waterBody.setFish((String)array.get(5));
        waterBody.setId((double)array.get(3));
        waterBody.setHref(getHref(waterBody.getId()));
        return waterBody;
    }

    public static WaterBody search(List<WaterBody> items, String query) {
        WaterBody found = null;
        outerloop:
        for(WaterBody item : items) {
            query = query.toLowerCase();
            String[] words = query.split("\\s+");
            boolean all = true;
            String name = item.getName().toLowerCase();

            for(String word : words) {
                if (!name.contains(word)) {
                    all = false;
                }
            }

            if(all) {
                found = item;
                break outerloop;
            }
        }
        return found;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static List<WaterBody> getFavorites(List<WaterBody> items, Set<String> favorites) {
        return items.stream().filter(item -> favorites.contains(item.getName())).collect(Collectors.toList());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static List<WaterBody> filterByStatus(List<WaterBody> items, String query) {
        return items.stream().filter(item -> item.getStatus().equals(query)).collect(Collectors.toList());
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
            PLog.e(e.getLocalizedMessage());
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
        return getName();
    }

    @Override
    public String getSnippet() {
        return getStatus();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFish() {
        return fish;
    }

    public void setFish(String fish) {
        this.fish = fish;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
