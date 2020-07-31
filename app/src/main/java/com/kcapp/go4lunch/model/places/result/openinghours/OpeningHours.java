package com.kcapp.go4lunch.model.places.result.openinghours;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.kcapp.go4lunch.R;

import java.util.Calendar;
import java.util.List;

public class OpeningHours {
    @SerializedName("open_now")
    private boolean openNow;

    @SerializedName("periods")
    private List<Periods> periods;

    /**
     * GETTERS
     **/
    public boolean getOpenNow() {
        return openNow;
    }
    public List<Periods> getPeriods() {
        return periods;
    }

    /**
     * SETTERS
     **/
    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }
    public void setPeriods(List<Periods> periods) {
        this.periods = periods;
    }

    /**
     * OTHERS
     **/
    /**
     *  Get the text for opening hours
     * @param context context of the application
     * @return text for opening hours
     */
    public String getOpeningHoursText(Context context) {
        String result = null;
        Calendar calendar = Calendar.getInstance();
        int dayToday = calendar.get(Calendar.DAY_OF_WEEK)-1;

        for (Periods period : periods) {
            if (period.getClose() == null) {
                // Place is always open
                result = context.getString(R.string.opening_hours_always_open);
            } else {
                if (period.getOpen().getDay() == dayToday) {
                    int currentTime;
                    int openTime = Integer.parseInt(period.getOpen().getTime());
                    int closeTime = Integer.parseInt(period.getClose().getTime());
                    int beforeOpenTime = openTime-25;
                    int beforeCloseTime = closeTime-25;

                    if (calendar.get(Calendar.MINUTE)<10) {
                        currentTime = Integer.parseInt(calendar.get(Calendar.HOUR_OF_DAY) + "0" + calendar.get(Calendar.MINUTE));
                    } else {
                        currentTime = Integer.parseInt(calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE));
                    }

                    if (currentTime < openTime && currentTime > beforeOpenTime) {
                        // Opening soon
                        result = context.getString(R.string.opening_hours_open_soon);
                    } else if (currentTime < openTime) {
                        // Close
                        result = context.getString(R.string.opening_hours_close);
                    } else if (currentTime < closeTime) {
                        // Opening until
                        result = context.getString(R.string.opening_hours_open_until) + " " + getHoursFormat(String.valueOf(closeTime));
                    } else if (currentTime >= beforeCloseTime) {
                        // Closing soon
                        result = context.getString(R.string.opening_hours_close_soon);
                    }
                }

                if (result == null) {
                    // Closing today
                    result = context.getString(R.string.opening_hours_close_today);
                }
            }
        }

        return result;
    }

    /**
     * Get formatted hour
     * @param hour a hour
     * @return formatted hour
     */
    public String getHoursFormat(String hour) {
        String result;

        if (hour.length()==3) {
            result = hour.substring(0,1) + ":" + hour.substring(1);
        } else {
            result = hour.substring(0,2)+":"+ hour.substring(2);
        }

        return result;
    }
}
