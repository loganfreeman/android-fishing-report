package com.loganfreeman.utahfishing.common.utils;

import com.loganfreeman.utahfishing.R;

/**
 * Created by scheng on 3/21/17.
 */

public class Ids {


    public static Integer getId(int i) {
        switch (i) {
            case 0:
                return R.id.choice1;
            case 1:
                return R.id.choice2;
            case 2:
                return R.id.choice3;
            case 3:
                return R.id.choice4;
            case 4:
                return R.id.choice5;
            case 5:
                return R.id.choice6;
            case 6:
                return R.id.choice7;
            case 7:
                return R.id.choice8;
            case 8:
                return R.id.choice9;
            case 9:
                return R.id.choice10;
            default:
                break;
        }

        return null;
    }

    public static int getIndex(int id) {
        switch (id) {
            case R.id.choice1:
                return 0;
            case R.id.choice2:
                return 1;
            case R.id.choice3:
                return 2;
            case R.id.choice4:
                return 3;
            case R.id.choice5:
                return 4;
            case R.id.choice6:
                return 5;
            case R.id.choice7:
                return 6;
            case R.id.choice8:
                return 7;
            case R.id.choice9:
                return 8;
            case R.id.choice10:
                return 9;
            default:
                break;
        }

        return -1;
    }
}
