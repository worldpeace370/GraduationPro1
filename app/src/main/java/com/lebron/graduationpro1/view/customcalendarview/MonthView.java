package com.lebron.graduationpro1.view.customcalendarview;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import static java.util.Calendar.DATE;


public class MonthView extends ViewGroup implements View.OnClickListener{

  protected static final int DEFAULT_DAYS_IN_WEEK = 7;
  protected static final int DEFAULT_MAX_WEEKS = 6;
  private static final Calendar TEMP_WORKING_CALENDAR = Calendar.getInstance();
  private Calendar firstViewDay;
  private CustomCalendarView cv;
  private Calendar minDate = null;
  private Calendar maxDate = null;
  private final int firstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
  private final Collection<DayView> dayViews = new ArrayList<>();

  public MonthView(CustomCalendarView view, Calendar firstViewDay) {
    super(view.getContext());
    this.cv = view;
    this.firstViewDay = firstViewDay;

    setClipChildren(false);
    setClipToPadding(false);

    buildDayViews(dayViews, resetAndGetWorkingCalendar());
  }

  protected void buildDayViews(Collection<DayView> dayViews, Calendar calendar) {
    for (int r = 0; r < DEFAULT_MAX_WEEKS; r++) {
      for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
        addDayView(dayViews, calendar);
      }
    }

  }

  protected void addDayView(Collection<DayView> dayViews, Calendar calendar) {
    Calendar day = CalendarTools.createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    DayView dayView = new DayView(getContext(), day);
    dayView.setOnClickListener(this);
    dayViews.add(dayView);
    addView(dayView, new LayoutParams());

    calendar.add(DATE, 1);
  }

  protected Calendar resetAndGetWorkingCalendar() {
    TEMP_WORKING_CALENDAR.clear();
    TEMP_WORKING_CALENDAR.set(firstViewDay.get(Calendar.YEAR), firstViewDay.get(Calendar.MONTH), firstViewDay.get(Calendar.DAY_OF_MONTH));
    //noinspection ResourceType
//    TEMP_WORKING_CALENDAR.setFirstDayOfWeek(getFirstDayOfWeek());
    int dow = TEMP_WORKING_CALENDAR.get(Calendar.DAY_OF_WEEK);
    int delta = firstDayOfWeek - dow;
    //If the delta is positive, we want to remove a week
    boolean removeRow = (delta >= 0);
    if (removeRow) {
      delta -= DEFAULT_DAYS_IN_WEEK;
    }
    TEMP_WORKING_CALENDAR.add(DATE, delta);
    return TEMP_WORKING_CALENDAR;
  }

  public Calendar getMonth() {
    return getFirstViewDay();
  }

  protected Calendar getFirstViewDay() {
    return firstViewDay;
  }

  public void setSelectionEnabled(boolean selectionEnabled) {
    for (DayView dayView : dayViews) {
      dayView.setOnClickListener(selectionEnabled ? this : null);
      dayView.setClickable(selectionEnabled);
    }
  }

  public void setMinimumDate(Calendar minDate) {
    this.minDate = minDate;
    updateUi();
  }

  public void setMaximumDate(Calendar maxDate) {
    this.maxDate = maxDate;
    updateUi();
  }

  public void setSelectedDates(Collection<Calendar> dates) {
    for (DayView dayView : dayViews) {
      Calendar day = dayView.getDate();
      dayView.setChecked(dates != null && dates.contains(day));
    }
    postInvalidate();
  }

  protected void updateUi() {
    for (DayView dayView : dayViews) {
      Calendar day = dayView.getDate();
      dayView.setupSelection(CalendarTools.inRange(day, minDate,maxDate), isDayEnabled(day));
    }
    postInvalidate();
  }

  @Override
  public void onClick(View v) {
    if (v instanceof DayView) {
      final DayView dayView = (DayView) v;
      cv.onDateClicked(dayView);
    }
  }

  protected int getRows() {
    return DEFAULT_MAX_WEEKS;
  }

  protected boolean isDayEnabled(Calendar day) {
    return day.get(Calendar.MONTH) == getFirstViewDay().get(Calendar.MONTH);
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams();
  }

  @Override
  protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
    final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
    final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
    final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

    //We expect to be somewhere inside a MaterialCalendarView, which should measure EXACTLY
    if (specHeightMode == MeasureSpec.UNSPECIFIED || specWidthMode == MeasureSpec.UNSPECIFIED) {
      throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
    }

    //The spec width should be a correct multiple
    final int measureTileWidth = specWidthSize / DEFAULT_DAYS_IN_WEEK;
    final int measureTileHeight = specHeightSize / getRows();

    //Just use the spec sizes
    setMeasuredDimension(specWidthSize, specHeightSize);

    int count = getChildCount();

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
          measureTileWidth,
          MeasureSpec.EXACTLY
      );

      int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
          measureTileHeight,
          MeasureSpec.EXACTLY
      );

      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams();
  }

  @Override
  public boolean shouldDelayChildPressedState() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return p instanceof LayoutParams;
  }

  @Override
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final int count = getChildCount();

    final int parentLeft = 0;

    int childTop = 0;
    int childLeft = parentLeft;

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);

      final int width = child.getMeasuredWidth();
      final int height = child.getMeasuredHeight();

      child.layout(childLeft, childTop, childLeft + width, childTop + height);

      childLeft += width;

      //We should warp every so many children
      if (i % DEFAULT_DAYS_IN_WEEK == (DEFAULT_DAYS_IN_WEEK - 1)) {
        childLeft = parentLeft;
        childTop += height;
      }

    }
  }

  /**
   * Simple layout params class for MonthView, since every child is the same size
   */
  protected static class LayoutParams extends MarginLayoutParams {

    /**
     * {@inheritDoc}
     */
    public LayoutParams() {
      super(WRAP_CONTENT, WRAP_CONTENT);
    }
  }
}
