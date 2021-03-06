package com.loganfreeman.utahfishing.modules.fishing.domain;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.loganfreeman.utahfishing.common.utils.Time;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import rx.Observable;
import rx.subjects.AsyncSubject;



/**
 * Created by shanhong on 3/21/17.
 */

public class StockReport implements Parcelable {

    public static final String UTAH_FISH_STOCK_URL = "https://dwrapps.utah.gov/fishstocking/Fish";

    private static final AsyncSubject<List<StockReport>> mSubject = AsyncSubject.create();


    public String watername;
    public String county;
    public String species;
    public int quantity;
    public double length;
    public String stockdate;


    public String getCounty() {
        return county;
    }

    public static Observable<List<StockReport>> getStockReportAsync() {
        Observable<List<StockReport>> firstTimeObservable =
                Observable.fromCallable(StockReport::fromWildlife);

        return firstTimeObservable.concatWith(mSubject);
    }


    @TargetApi(Build.VERSION_CODES.N)
    public static List<StockReport> filterByCounty(List<StockReport> items, String query) {
        return items.stream().filter(item -> item.county.equalsIgnoreCase(query)).collect(Collectors.toList());
    }

    public static List<StockReport> search(List<StockReport> items, String query) {
        List<StockReport> reports = new ArrayList<StockReport>();
        outerloop:
        for(StockReport item : items) {
            query = query.toLowerCase();
            String[] words = query.split("\\s+");
            boolean all = true;
            String name = item.watername.toLowerCase();

            for(String word : words) {
                if (!name.contains(word)) {
                    all = false;
                }
            }

            if(all) {
                reports.add(item);
            }
        }
        return reports;
    }



    public static List<StockReport> fromWildlife() throws IOException {
        List<StockReport> reports = new ArrayList<StockReport>();
        Document document = null;
        document = Jsoup.connect(UTAH_FISH_STOCK_URL).get();

        Elements rows = document.select("#fishTableBody > tr.table1");

        for(Element element : rows) {

            reports.add(fromElement(element));
        }


        return reports;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static StockReport fromElement(Element element) {
        StockReport report = new StockReport();
        report.watername = element.select("td.watername").first().text();
        report.county = element.select("td.county").first().text();
        report.species = element.select("td.species").first().text();
        report.quantity = Integer.parseInt(element.select("td.quantity").first().text());
        report.length = Double.parseDouble(element.select("td.length").first().text());
        report.stockdate = element.select("td.stockdate").first().text();

        return report;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(watername);
        out.writeString(county);
        out.writeString(species);
        out.writeInt(quantity);
        out.writeDouble(length);
        out.writeString(stockdate);

    }

    public StockReport() {

    }

    private StockReport(Parcel in) {
        watername = in.readString();
        county = in.readString();
        species = in.readString();
        quantity = in.readInt();
        length = in.readDouble();
        stockdate = in.readString();

    }

    public static final Parcelable.Creator<StockReport> CREATOR = new Parcelable.Creator<StockReport>(){

        @Override
        public StockReport createFromParcel(Parcel source) {
            return new StockReport(source);
        }

        @Override
        public StockReport[] newArray(int size) {
            return new StockReport[size];
        }
    };


    @TargetApi(Build.VERSION_CODES.N)
    public static List<StockReport> filterByCalendarDay(List<StockReport> items, CalendarDay date) {
        return items.stream().filter(item -> Time.toCalendarDay(item.stockdate).equals(date)).collect(Collectors.toList());
    }
}
