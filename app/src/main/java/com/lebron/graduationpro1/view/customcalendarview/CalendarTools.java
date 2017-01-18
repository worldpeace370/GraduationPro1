package com.lebron.graduationpro1.view.customcalendarview;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by lizhanzhan on 16/11/8.
 */

public class CalendarTools {

  private static final SimpleDateFormat SDF_Y_M_D = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

  private static CalendarTools ourInstance = new CalendarTools();

  public static CalendarTools getInstance() {
    return ourInstance;
  }

  private CalendarTools() {
  }

  /**
   * 根据传入的年月日生成新的Calendar
   */
  public static Calendar createCalendar(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(year, month, day);
    return calendar;
  }

  /**
   * 比较日历大小，cal1比cal2大则返回1，比cal2小返回-1，相等返回0
   */
  public static int compareCalendar(Calendar cal1, Calendar cal2) {
    if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
      return 1;
    } else if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
      return -1;
    } else if (cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH)) {
      return 1;
    } else if (cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH)) {
      return -1;
    } else if (cal1.get(Calendar.DAY_OF_MONTH) > cal2.get(Calendar.DAY_OF_MONTH)) {
      return 1;
    } else if (cal1.get(Calendar.DAY_OF_MONTH) < cal2.get(Calendar.DAY_OF_MONTH)) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * 判断日历1 的日期是否在日历min和日历max之中
   */
  public static boolean inRange(Calendar cal1, Calendar minDate, Calendar maxDate) {
    return !(minDate != null && compareCalendar(cal1, minDate) == -1)
            && !(maxDate != null && compareCalendar(cal1, maxDate) == 1);
  }

  /**
   * 判断两个calendar 是否是同一天
   */
  public static boolean sameDay(Calendar cal1, Calendar cal2) {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
  }

  /**
   *  判断某一天是否是周末
   */
  public static boolean isWeekend(Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
  }

  /**
   *
   * @param year 年
   * @param month 月
   * @param day 日
   * @return 返回类似 20161101 的字符串
   */
  public static String dateToString(int year, int month, int day) {
    Calendar calendar = createCalendar(year, month, day);
    String temp = SDF_Y_M_D.format(calendar.getTime());
    return temp;
  }

  /**
   * @return 返回日历中的周几
   */
  public static String getWeek(Calendar calendar) {
    switch (calendar.get(Calendar.DAY_OF_WEEK)) {
      case Calendar.SUNDAY:
        return "周日";
      case Calendar.MONDAY:
        return "周一";
      case Calendar.TUESDAY:
        return "周二";
      case Calendar.WEDNESDAY:
        return "周三";
      case Calendar.THURSDAY:
        return "周四";
      case Calendar.FRIDAY:
        return "周五";
      case Calendar.SATURDAY:
        return "周六";
      default:
        break;
    }
    return "";
  }
}
