package com.example.eventreminder.utils;

import android.content.Context;
import android.util.Log;

import com.example.eventreminder.R;

public final class WeatherUtils {

        private static final String LOG_TAG = WeatherUtils.class.getSimpleName();

        private static double celsiusToFahrenheit(double temperatureInCelsius) {
            double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
            return temperatureInFahrenheit;
        }

//        public static String getFormattedWind(Context context, float windSpeed, float degrees) {
//            int windFormat = R.string.format_wind_kmh;
//
//            String direction = "Unknown";
//            if (degrees >= 337.5 || degrees < 22.5) {
//                direction = context.getString(R.string.Ndirection);
//            } else if (degrees >= 22.5 && degrees < 67.5) {
//                direction = context.getString(R.string.NEdirection);
//            } else if (degrees >= 67.5 && degrees < 112.5) {
//                direction = context.getString(R.string.Edirection);
//            } else if (degrees >= 112.5 && degrees < 157.5) {
//                direction = context.getString(R.string.SEdirection);
//            } else if (degrees >= 157.5 && degrees < 202.5) {
//                direction = context.getString(R.string.Sdirection);
//            } else if (degrees >= 202.5 && degrees < 247.5) {
//                direction = context.getString(R.string.SWdirection);
//            } else if (degrees >= 247.5 && degrees < 292.5) {
//                direction = context.getString(R.string.Wdirection);
//            } else if (degrees >= 292.5 && degrees < 337.5) {
//                direction = context.getString(R.string.NWdirection);
//            }
//
//            return String.format(context.getString(windFormat), windSpeed, direction);
//        }
//
//        public static int getResourceIdForWeatherCondition(int weatherId) {
//
//            if (weatherId >= 200 && weatherId <= 232) {
//                return R.drawable.storm;
//            } else if (weatherId >= 300 && weatherId <= 321) {
//                return R.drawable.light_rain;
//            } else if (weatherId >= 500 && weatherId <= 504) {
//                return R.drawable.rain;
//            } else if (weatherId == 511) {
//                return R.drawable.snow;
//            } else if (weatherId >= 520 && weatherId <= 531) {
//                return R.drawable.rain;
//            } else if (weatherId >= 600 && weatherId <= 622) {
//                return R.drawable.snow;
//            } else if (weatherId >= 701 && weatherId <= 761) {
//                return R.drawable.fog;
//            } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
//                return R.drawable.storm;
//            } else if (weatherId == 800) {
//                return R.drawable.clear;
//            } else if (weatherId == 801) {
//                return R.drawable.light_clouds;
//            } else if (weatherId >= 802 && weatherId <= 804) {
//                return R.drawable.clouds;
//            } else if (weatherId >= 900 && weatherId <= 906) {
//                return R.drawable.storm;
//            } else if (weatherId >= 958 && weatherId <= 962) {
//                return R.drawable.storm;
//            } else if (weatherId >= 951 && weatherId <= 957) {
//                return R.drawable.clear;
//            }
//
//            Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
//            return R.drawable.storm;
//        }
}

