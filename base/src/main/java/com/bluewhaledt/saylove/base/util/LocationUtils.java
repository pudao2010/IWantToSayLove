package com.bluewhaledt.saylove.base.util;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * 定位工具类
 */
public class LocationUtils {

    /**
     * GPS是否打开
     *
     * @param context Context
     * @return GPS是否打开
     */
    public static boolean isGpsEnabled(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获取系统提供的定位结果
     *
     * @param context Context
     * @return 系统提供的定位结果
     * @throws SecurityException
     */
    public static Location getLocation(Context context) throws SecurityException {
        Location location = null;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (PermissionHelper.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (PermissionHelper.hasPermissions(context, Manifest.permission
                    .ACCESS_COARSE_LOCATION)) {
                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
            }
        }
        return location;
    }
}
