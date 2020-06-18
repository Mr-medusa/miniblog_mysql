package red.medusa.miniblog.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");

    public static String getDate(Date date) {
        return date != null ? simpleDateFormat.format(date) : "";
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
