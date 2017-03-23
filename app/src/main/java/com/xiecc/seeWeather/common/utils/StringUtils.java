package com.xiecc.seeWeather.common.utils;

import java.util.Set;

/**
 * Created by shanhong on 3/23/17.
 */

public class StringUtils {
    public static String join(Set<String> stringSet, String s) {
        StringBuffer sb = new StringBuffer();
        for(String part : stringSet) {
            sb.append(part).append(s);
        }
        return  sb.toString();
    }

}
