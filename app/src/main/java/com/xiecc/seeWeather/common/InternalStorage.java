package com.xiecc.seeWeather.common;

import android.content.Context;
import android.os.Environment;

import com.xiecc.seeWeather.modules.fishing.domain.WaterBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.xiecc.seeWeather.common.utils.SharedPreferenceUtil.FAVORITE_WATRER;

/**
 * Created by shanhong on 3/23/17.
 */

public final class InternalStorage{

    public static final String FAVORITE_FILE_NAME = "/favorite_water_body_list";
    private InternalStorage() {}


    public static List<WaterBody> getFavoriteWaterBodies(Context context) {
        List<WaterBody> favorites = null;

        try {
            favorites = (List<WaterBody>) InternalStorage.readObject(context, FAVORITE_WATRER);



        } catch (IOException e) {
            PLog.e(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            PLog.e(e.getLocalizedMessage());
        }

        if(favorites == null) {
            favorites = new ArrayList<WaterBody>();
        }

        return favorites;
    }

    public static  String getBaseFolder(Context context) {
        String baseFolder;
        // check if external storage is available
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            baseFolder = context.getExternalFilesDir(null).getAbsolutePath();
        }
        // revert to using internal storage
        else {
            baseFolder = context.getFilesDir().getAbsolutePath();
        }

        return baseFolder;
    }

    public static void writeObject(Context context, String key, Object object) throws IOException {


        File file = new File(getBaseFolder(context) + FAVORITE_FILE_NAME);

        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);

        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        File file = new File(getBaseFolder(context) + FAVORITE_FILE_NAME);

        file.createNewFile();

        FileInputStream fis = new FileInputStream(file);

        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}