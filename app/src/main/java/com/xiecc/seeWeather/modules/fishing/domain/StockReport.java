package com.xiecc.seeWeather.modules.fishing.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiecc.seeWeather.common.PLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.subjects.AsyncSubject;

import static com.xiecc.seeWeather.modules.fishing.domain.WaterBody.UTAH_WILDLIFE_HOTSPOTS;

/**
 * Created by shanhong on 3/21/17.
 */

public class StockReport implements Parcelable {

    public static final String UTAH_FISH_STOCK_URL = "https://dwrapps.utah.gov/fishstocking/Fish";

    private static final AsyncSubject<List<StockReport>> mSubject = AsyncSubject.create();

    public static final DateFormat df = new SimpleDateFormat("mm/dd/yyyy");

    public String watername;
    public String county;
    public String species;
    public int quantity;
    public double length;
    public Date stockdate;

    public static Observable<List<StockReport>> getStockReportAsync() {
        Observable<List<StockReport>> firstTimeObservable =
                Observable.fromCallable(StockReport::fromWildlife);

        return firstTimeObservable.concatWith(mSubject);
    }

    public static Date getDate(String date) {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    private static StockReport fromElement(Element element) {
        StockReport report = new StockReport();
        report.watername = element.select("td.watername").first().text();
        report.county = element.select("td.county").first().text();
        report.species = element.select("td.species").first().text();
        report.quantity = Integer.parseInt(element.select("td.quantity").first().text());
        report.length = Double.parseDouble(element.select("td.length").first().text());
        report.stockdate = getDate(element.select("td.stockdate").first().text());
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
        out.writeString(df.format(stockdate));

    }

    public StockReport() {

    }

    private StockReport(Parcel in) {
        watername = in.readString();
        county = in.readString();
        species = in.readString();
        quantity = in.readInt();
        length = in.readDouble();
        stockdate = getDate(in.readString());

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
}