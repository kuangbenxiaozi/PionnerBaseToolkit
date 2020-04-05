package com.pioneer.base.toolkit.auxiliary;

import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 时间方面计算的工具类
 * @author yuankai
 * @version 1.0
 * @date 2011-6-7
 */
public class TimeUtils {
    private static final String TAG = "TimeUtils";
    public static long ONE_DAY_MS = 24 * 3600 * 1000;

    private static final String DAYSTR = "%d天";
    private static final String HOURSTR = "%d小时";
    private static final String MINSTR = "%d分钟";
    public final static long ONE_MINUTE = 60 * 1000;
    private final static long ONE_HOUR = 60 * ONE_MINUTE;
    private final static long ONE_DAY = 24 * ONE_HOUR;
    private final static long ONE_YEAR = 365 * ONE_DAY;
    private final static long SEVEN_DAY = 7 * 24 * 60 * 60 * 1000;

    private static long mTodayTime = getTodayStartTime();

    private static long offset = System.currentTimeMillis() - SystemClock.elapsedRealtime();

    public static void adjust(long serverTime) {
        offset = serverTime - SystemClock.elapsedRealtime();
    }

    public static long getAdjustedTime() {
        return SystemClock.elapsedRealtime() + offset;
    }

    public static long getAdjustedSecTime() {
        return (SystemClock.elapsedRealtime() + offset) / 1000;
    }

    /**
     * 获取今天开始的毫秒数
     * @return
     */
    public static long getTodayStartTime() {
        Date date = new Date(getAdjustedTime());
        return new Date(date.getYear(),date.getMonth(),date.getDate()).getTime();
    }

    /**
     * 判断是否是今天
     * @param ms
     * @return
     */
    public static boolean isToday(long ms) {
        long diff = ms - getTodayStartTime();
        return diff >= 0 && diff < ONE_DAY_MS;
    }

    /**
     * 判断是否是昨天
     * @param ms
     * @return
     */
    public static boolean isYesterday(long ms) {
        long diff = ms - getTodayStartTime() - ONE_DAY_MS;
        return diff >= 0 && diff < ONE_DAY_MS;
    }

    public static boolean isNear(String timeA,String timeB) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        long delta = 90000;
        try {
            delta = dateFormat.parse(timeA).getTime() - dateFormat.parse(timeB).getTime();
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(delta) < 90000;
    }

    public static String getTimeStr(long ms) {
        long diff = getAdjustedTime() - ms;
        long dayDiff = mTodayTime - ms;
        if(diff < ONE_MINUTE) {
            return "刚刚更新";
        } else if(diff < ONE_HOUR) {
            return (int) Math.ceil((diff / ONE_MINUTE)) + "分钟前";
        } else if(diff < ONE_DAY) {
            return (int) Math.ceil((diff / ONE_HOUR)) + "小时前";
        } else if(dayDiff < SEVEN_DAY) {
            return (int) Math.ceil(((double) dayDiff / ONE_DAY)) + "天前";
        } else {
            return DateFormat.format("yyyy-M-dd",ms).toString();
        }
    }

    public static String getFormatTime(long ms) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
        Date currentDate = new Date(ms);
        return simpleDateFormat.format(currentDate);
    }

    public static String getFormatHourMinute(long ms) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm",Locale.getDefault());
        Date currentDate = new Date(ms);
        return simpleDateFormat.format(currentDate);
    }

    public static long parseTime(String time) {
        if(TextUtils.isEmpty(time)) {
            return -1;
        }
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",Locale.getDefault()).parse(time));
            return c.getTimeInMillis();
        } catch(ParseException e) {
            return -1;
        }
    }

    public static String parseDate(String time) {

        if(TextUtils.isEmpty(time)) {
            return null;
        }
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",Locale.getDefault()).parse(time));
            long getMills = c.getTimeInMillis();

            Time t = new Time();
            t.set(getAdjustedTime());
            String yesterday = "";
            String state = "";
            // 判断一天当中是什么时候，凌晨，上午，中午，下午，晚上
            if(c.get(Calendar.HOUR_OF_DAY) < 6) {
                state = "凌晨";
            } else if(c.get(Calendar.HOUR_OF_DAY) < 12) {
                state = "上午";
            } else if(c.get(Calendar.HOUR_OF_DAY) < 13) {
                state = "中午";
            } else if(c.get(Calendar.HOUR_OF_DAY) < 18) {
                state = "下午";
            } else if(c.get(Calendar.HOUR_OF_DAY) < 24) {
                state = "晚上";
            }
            // 判断显示日期
            // 不是同一年，显示年 /月 /日/时间
            if(0 != t.year - c.get(Calendar.YEAR)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 hh:mm",Locale.getDefault());
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm",Locale.getDefault());
                return sdf1.format(new Date(getMills)) + " " + state + sdf2.format(new Date(getMills));
            } else if(c.get(Calendar.YEAR) == t.year) {
                // 表示为当前年分
                if(c.get(Calendar.DAY_OF_YEAR) < (t.yearDay + 1)) {
                    if(c.get(Calendar.DAY_OF_YEAR) == (t.yearDay)) {
                        // 昨天
                        yesterday = "昨天";
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm",Locale.getDefault());
                        return yesterday + " " + state + sdf.format(new Date(getMills));
                    } else if((t.yearDay - c.get(Calendar.DAY_OF_YEAR) < 6) && c.get(Calendar.DAY_OF_WEEK) < (t.weekDay)) {
                        // 一周以内
                        switch(c.get(Calendar.DAY_OF_WEEK)) {
                            case 1:
                                yesterday = "周日";
                                break;
                            case 2:
                                yesterday = "周一";
                                break;
                            case 3:
                                yesterday = "周二";
                                break;
                            case 4:
                                yesterday = "周三";
                                break;
                            case 5:
                                yesterday = "周四";
                                break;
                            case 6:
                                yesterday = "周五";
                                break;
                            case 7:
                                yesterday = "周六";
                                break;
                            default:
                                break;
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm",Locale.getDefault());
                        return yesterday + " " + state + sdf.format(new Date(getMills));
                    } else {
                        // 非当前发送信息
                        SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日",Locale.getDefault());
                        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm",Locale.getDefault());
                        return sdf1.format(new Date(getMills)) + " " + state + sdf2.format(new Date(getMills));
                    }
                } else if(c.get(Calendar.DAY_OF_YEAR) == (t.yearDay + 1)) {
                    // 当天发送的信息
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.getDefault());
                    return state + sdf.format(new Date(getMills));
                } else {
                    // 服务器提供的时间不对
                    return null;
                }
            } else {
                // 服务器提供的时间不对
                return null;
            }
        } catch(ParseException e) {
            return null;
        }
    }

    public static Date parseBirthday(String birthday) {
        Date date;
        if(TextUtils.isEmpty(birthday)) {
            date = new Date(TimeUtils.getAdjustedTime());
            return date;
        }
        try {
            date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(birthday);
        } catch(ParseException e) {
            date = new Date(TimeUtils.getAdjustedTime());
        }
        return date;
    }

    public static String formatBirthday(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(date);
    }

    public static int parseAge(String birth) {
        Calendar c = Calendar.getInstance();
        Date result = TimeUtils.parseBirthday(birth);
        c.setTime(result);
        return (int) ((getAdjustedTime() - c.getTimeInMillis()) / ONE_YEAR);
    }

    public static String getCountTime(long duration) {
        long nh = 60 * 60;
        long nm = 60;
        int hour = (int) (duration / nh);
        int min = (int) (duration % nh / nm);
        int second = (int) (duration % nm);
        StringBuilder builder = new StringBuilder();
        if(hour < 10) {
            builder.append("0");
        }
        builder.append(hour);
        builder.append(":");
        if(min < 10) {
            builder.append("0");
        }
        builder.append(min);
        builder.append(":");
        if(second < 10) {
            builder.append("0");
        }
        builder.append(second);
        return builder.toString();
    }

    public static String getLastTimeStrBySecond(long seconds) {
        if(seconds == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        long day = contain(seconds,ONE_DAY);
        seconds = remainder(seconds,ONE_DAY);
        long hour = contain(seconds,ONE_HOUR);
        seconds = remainder(seconds,ONE_HOUR);
        long min = contain(seconds,ONE_MINUTE);
        if(day > 0) {
            result.append(String.format(DAYSTR,day));
        }
        if(hour > 0) {
            result.append(String.format(HOURSTR,hour));
        }
        if(min > 0) {
            result.append(String.format(MINSTR,min));
        }
        return result.toString();
    }

    /**
     * 大于2个小时才显示为小时的格式
     * @param mins
     * @return
     */
    public static String getLastTimeStrByMin2Hour(long mins) {
        long seconds = mins * ONE_MINUTE;
        if(seconds == 0) {
            return "";
        }

        if(mins <= 120) {
            return String.format(MINSTR,mins);
        } else {
            return getLastTimeStrByMin(mins);
        }
    }


    public static String getLastTimeStrByMin(long mins) {
        return getLastTimeStrBySecond(mins * ONE_MINUTE);
    }

    private static long remainder(long sum,long per) {
        try {
            return sum % per;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static long contain(long sum,long per) {
        try {
            return sum / per;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getCostTimeString(int costTime) {
        if(costTime < 60) {
            return costTime + " 分钟送达";
        } else if(costTime >= 60 && costTime < 1440) {
            int hours = costTime / 60;
            int min = costTime - 60 * hours;
            if(min != 0) {
                return hours + " 小时 " + min + " 分钟送达";
            } else {
                return hours + " 小时送达";
            }
        } else if(costTime >= 1440) {
            int days = costTime / 1440;
            return days + " 天送达";
        }
        return "";
    }

    public static String getShopCommentReplyTimeString(String shopReplyTime,String commentCreateTime) {
        long timeGap = TimeUtils.parseTime(shopReplyTime) - TimeUtils.parseTime(commentCreateTime);
        if(timeGap < 0) {
            return "";
        }
        if(timeGap > 86400000) {
            int days = (int) (timeGap / 86400000);
            return "店家 " + days + " 天后回复:";
        } else {
            return "店家当天回复:";
        }
    }

    /**
     * 毫秒数转换为 * 天 * 小时 * 分钟 * 秒 的格式
     * @param mss
     * @return 毫秒数转换为 * 天 * 小时 * 分钟 * 秒 的格式
     */
    public static String formatMsToString(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒";
    }

    public static String getCurrentTime(String timeFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * @param time 单位，秒
     * @return 分，秒  eg:06:12
     */
    public static String formatMMss(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            return sdf.format(new Date(time * 1000L));
        } catch(Exception e) {
            return "";
        }
    }

    public static String formathhMM(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//kk:mm
            return sdf.format(new Date(Long.valueOf(time) * 1000L));
        } catch(Exception e) {
            return "";
        }
    }

    private static long lastClickTime;

    /**
     * 检测是否小于一定毫秒数疯狂点击
     * @return true表示小于，需要return处理 eg: if (WMUtils.isFastClick()){ return ; }
     * @times 希望的毫秒间隔，不传默认为600毫秒
     */
    public static synchronized boolean isFastClick(long... times) {
        long intervalTime = 600;
        if(null != times && times.length > 0) {
            intervalTime = times[0];
        }
        long time = System.currentTimeMillis();
        if(time - lastClickTime < intervalTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 转换为小时，分和秒
     **/
    public static String stringForTime(long timeMs) {
        if(timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder,Locale.getDefault());
        if(hours > 0) {
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }
}
