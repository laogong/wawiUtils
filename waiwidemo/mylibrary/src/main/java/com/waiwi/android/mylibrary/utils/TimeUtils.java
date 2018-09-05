package com.waiwi.android.mylibrary.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sdmt-gjw on 2015/6/3.
 */
public class TimeUtils {
    // 以友好的方式显示时间
    public static String friendly_time(String sdate) {
        Date time = null;

//        if (isInEasternEightZones())
        time = toDate(sdate);
//        else
//            time = transformTime(toDate(sdate),
//                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = "今天" + dateFormater3.get().format(time);
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = "今天" + dateFormater3.get().format(time);
        } else if (days == 1) {
            ftime = "昨天" + dateFormater3.get().format(time);
        } else if (days == 2) {
            ftime = "前天 " + dateFormater3.get().format(time);
        } else {
            ftime = dateFormater4.get().format(time);
        }
        return ftime;
    }

    //判断用户的设备时区是否为东八区（中国） 2014年7月31日
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        defaultVaule = TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
        return defaultVaule;
    }

    //根据不同时区，转换时间 2014年7月31日
    public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    // 将字符串转位日期类型
    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateString(Date date) {
        return dateFormater.get().format(date);
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    public static String getShortTime(String time) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            Date curDtae = new Date();
            long durTime = curDtae.getTime() - date.getTime();
            int dateStatus = calaulDayStatus(date, curDtae);
            if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
                str = "刚刚";
            } else if (durTime < ONE_HOUR_MILLIONS) {
                str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
            } else if (dateStatus == 0) {
                str = durTime / ONE_HOUR_MILLIONS + "小时前";
            } else if (dateStatus == -1) {
                str = "昨天" + DateFormat.format("HH:mm", date);
            } else if (isSameYear(date, curDtae) && dateStatus < -1) {
                str = DateFormat.format("MM:dd", date).toString();
            } else {
                str = DateFormat.format("yyyy:MM", date).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(targetTime);
        int tarYear = targetCal.get(Calendar.YEAR);

        Calendar compareCal = Calendar.getInstance();
        compareCal.setTime(compareTime);
        int compareYear = compareCal.get(Calendar.YEAR);

        return tarYear == compareYear;
    }

    public static int calaulDayStatus(Date targetTime, Date compareTime) {
        Calendar targetCal = Calendar.getInstance();
        targetCal.setTime(targetTime);
        int tarDayOfYear = targetCal.get(Calendar.DAY_OF_YEAR);

        Calendar compareCal = Calendar.getInstance();
        compareCal.setTime(compareTime);
        int compareDayOfYear = compareCal.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - compareDayOfYear;
    }


    /*时间戳转换成字符窜*/
    public static String getDateToString(long s, String type) {
//        if (s == 0) {
//            return "";
//        }
//        String result = "";
//        SimpleDateFormat format = new SimpleDateFormat(type);
//        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
//        result = format.format(new Date(s));
//        return result;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//        String format = dateFormat.format(new Date(s));
//        return format;
        Date date = new Date(s);
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    // 将字符串转为时间戳
    public static String getStringToDate(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    public String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        return String.valueOf(time);
    }

    public static int compare_date(String selectTime) {
        Calendar now = Calendar.getInstance();
        String now_time = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + "";
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(selectTime);
            Date dt2 = df.parse(now_time);
            if (dt1.getTime() >= dt2.getTime()) {
                System.out.println("选择时间大于当前时间");
                return 1;
            } else {
                System.out.println("选择时间小于当前时间");
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compareTime(String year, String month, String day) {
        Calendar now = Calendar.getInstance();
        System.out.println("年: " + now.get(Calendar.YEAR));
        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
        int select_time = Integer.parseInt(getStringToDate(year + "-" + month + "-" + day));
        int now_time = Integer.parseInt(getStringToDate(now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH) + ""));
        System.out.println("select_time ===== " + select_time);
        System.out.println("now_time ==== " + now_time);
        if (select_time >= now_time) {
            return 1;
        } else if (select_time < now_time) {
            return -1;
        }
        return 0;
    }
}


