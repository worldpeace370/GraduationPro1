package com.lebron.graduationpro1.view.customcalendarview;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by lizhanzhan on 16/11/7.
 */

public class CalendarPagerAdapter extends PagerAdapter {

  private final ArrayDeque<MonthView> currentViews;
  protected final CustomCalendarView cv;
  private final Calendar today = Calendar.getInstance();
  private Calendar minDate = null;
  private Calendar maxDate = null;
  private DateRangeIndex rangeIndex;
  private boolean selectionEnabled = true;
  private List<Calendar> selectedDates = new ArrayList<>();

  CalendarPagerAdapter(CustomCalendarView cv) {
    this.cv = cv;
    currentViews = new ArrayDeque<>();
    setRangeDates(null, null);
  }

  @Override
  public int getCount() {
    return rangeIndex.getCount();
  }

  public Calendar getItem(int position) {
    return rangeIndex.getItem(position);
  }

  @Override
  public int getItemPosition(Object object) {
    if (!(isInstanceOfView(object))) {
      return POSITION_NONE;
    }
    MonthView monthView = (MonthView) object;
    Calendar firstViewDay = monthView.getFirstViewDay();
    if (firstViewDay == null) {
      return POSITION_NONE;
    }
    int index = indexOf((MonthView) object);
    if (index < 0) {
      return POSITION_NONE;
    }
    return index;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    MonthView monthView = createView(position);
    monthView.setAlpha(0);
    monthView.setSelectionEnabled(selectionEnabled);
    monthView.setMinimumDate(minDate);
    monthView.setMaximumDate(maxDate);
    monthView.setSelectedDates(selectedDates);
    container.addView(monthView);
    currentViews.add(monthView);
    return monthView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    MonthView monthView = (MonthView) object;
    currentViews.remove(monthView);
    container.removeView(monthView);
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  protected MonthView createView(int position) {
    return new MonthView(cv, getItem(position));
  }

  protected int indexOf(MonthView view) {
    Calendar month = view.getMonth();
    return getRangeIndex().indexOf(month);
  }

  public int getIndexForDay(Calendar day) {
    if (minDate != null && CalendarTools.compareCalendar(day, minDate) < 0) {
      return 0;
    }
    if (maxDate != null && CalendarTools.compareCalendar(day, maxDate) > 0) {
      return getCount() - 1;
    }
    return rangeIndex.indexOf(day);
  }

  protected boolean isInstanceOfView(Object object) {
    return object instanceof MonthView;
  }

  protected DateRangeIndex createRangeIndex(Calendar min, Calendar max) {
    return new Monthly(min, max);
  }

  public DateRangeIndex getRangeIndex() {
    return rangeIndex;
  }

  public void setRangeDates(Calendar min, Calendar max) {
    this.minDate = min;
    this.maxDate = max;
    for (MonthView monthView : currentViews) {
      monthView.setMinimumDate(min);
      monthView.setMaximumDate(max);
    }

    if (min == null) {
      min = CalendarTools.createCalendar(today.get(Calendar.YEAR) - 20, today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    if (max == null) {
      max = CalendarTools.createCalendar(today.get(Calendar.YEAR) + 20, today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    rangeIndex = createRangeIndex(min, max);

    notifyDataSetChanged();
    invalidateSelectedDates();
  }

  public void clearSelections() {
    selectedDates.clear();
    invalidateSelectedDates();
  }

  public void setSelectionEnabled(boolean enabled) {
    selectionEnabled = enabled;
    for (MonthView monthView : currentViews) {
      monthView.setSelectionEnabled(selectionEnabled);
    }
  }

  public void setDateSelected(Calendar day, boolean selected) {
    if (selected) {
      if (!selectedDates.contains(day)) {
        selectedDates.add(day);
        invalidateSelectedDates();
      }
    } else {
      if (selectedDates.contains(day)) {
        selectedDates.remove(day);
        invalidateSelectedDates();
      }
    }
  }

  private void invalidateSelectedDates() {
    validateSelectedDates();
    for (MonthView monthView : currentViews) {
      monthView.setSelectedDates(selectedDates);
    }
  }

  private void validateSelectedDates() {
    for (int i = 0; i < selectedDates.size(); i++) {
      Calendar date = selectedDates.get(i);

      if (minDate != null && CalendarTools.compareCalendar(date, minDate) < 0 || (maxDate != null && CalendarTools.compareCalendar(date, maxDate) > 0)) {
        selectedDates.remove(i);
        i = i - 1;
      }
    }
  }

  public static class Monthly implements DateRangeIndex {

    private final Calendar min;
    private final int count;

    private SparseArrayCompat<Calendar> dayCache = new SparseArrayCompat<>();

    public Monthly(@NonNull Calendar min, @NonNull Calendar max) {
      this.min = CalendarTools.createCalendar(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
      max = CalendarTools.createCalendar(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 1);
      this.count = indexOf(max) + 1;
    }

    public int getCount() {
      return count;
    }

    public int indexOf(Calendar day) {
      int yDiff = day.get(Calendar.YEAR) - min.get(Calendar.YEAR);
      int mDiff = day.get(Calendar.MONTH) - min.get(Calendar.MONTH);
      int a = (yDiff * 12) + mDiff;
      return (yDiff * 12) + mDiff;
    }

    public Calendar getItem(int position) {

      Calendar re = dayCache.get(position);
      if (re != null) {
        return re;
      }

      int numY = position / 12;
      int numM = position % 12;

      int year = min.get(Calendar.YEAR)  + numY;
      int month = min.get(Calendar.MONTH) + numM;
      if (month >= 12) {
        year += 1;
        month -= 12;
      }

      re = CalendarTools.createCalendar(year, month, 1);
      dayCache.put(position, re);
      return re;
    }
  }

  public List<Calendar> getSelectedDates() {
    return Collections.unmodifiableList(selectedDates);
  }

  /**
   * Use math to calculate first days of months by postion from a minium date
   */
  interface DateRangeIndex {

    int getCount();

    int indexOf(Calendar day);

    Calendar getItem(int position);
  }
}
