package com.bluewhaledt.saylove.video_record.filter;

import android.content.Context;

import com.bluewhaledt.saylove.R;


/**
 * Created by jerikc on 16/2/23.
 */
public class FilterManager {

    private static int[] mCurveArrays = new int[]{0,R.raw.portraesque,
            R.raw.cross_1, R.raw.cross_2, R.raw.cross_3, R.raw.cross_4, R.raw.cross_5,
            R.raw.cross_6, R.raw.cross_7, R.raw.cross_8, R.raw.cross_9, R.raw.cross_10,
            R.raw.cross_11
    };

    public enum FilterType {
        Normal(0), BEAUTY(1),DAZZLE_YELLOW(2), LIGHT_YELLOW(3), LIGHT_PURPLE(4), DEEP_YELLOW(5),
        BLUE(6), LIGHT_BLUE(7), PINK(8), YELLOW(9), WARM(10), CYAN(11), WARM_BLUE(12);

        private int value;

        FilterType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static FilterType typeOfValue(int value) {
            for (FilterType e : values()) {
                if (e.getValue() == value) {
                    return e;
                }
            }
            return Normal;
        }
    }


    private FilterManager() {
    }


    public static IFilter getCameraFilter(FilterType filterType, Context context) {
        if (filterType.getValue() == 0) {
            return new CameraFilter(context);
        } else {
            return new CameraFilterToneCurve(context,
                    context.getResources().openRawResource(mCurveArrays[filterType.getValue()]));
        }

    }


}
