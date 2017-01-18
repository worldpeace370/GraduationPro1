package com.lebron.graduationpro1.view.customcalendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lebron.graduationpro1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 日历View
 */

public class CustomCalendarView extends ViewGroup {

  public static final int INVALID_TILE_DIMENSION = -10;
  private static final int DEFAULT_DAYS_IN_WEEK = 7;
  private static final int DEFAULT_MAX_WEEKS = 6;  //最大为6周
  private static final SimpleDateFormat SDF_Y_M = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
  private OnDateSelectedListener dateSelectedListener;
  private OnMonthChangedListener monthChangedListener;

  private final TextView title;
  private final ImageView buttonPast;
  private final ImageView buttonFuture;
  private final CalendarPager pager;
  private CalendarPagerAdapter adapter;
  private LinearLayout topbar;
  private LinearLayout weekLayout;
  private Drawable leftArrowMask;
  private Drawable rightArrowMask;
  private int tileHeight = INVALID_TILE_DIMENSION;
  private int tileWidth = INVALID_TILE_DIMENSION;
  private static final int TITLE_HEIGHT_DP = 30;
  private static final int WEEK_HEIGHT_DP = 54;

  private Calendar currentMonth = Calendar.getInstance();
  private Calendar minDate = null;
  private Calendar maxDate = null;
  private State state;
  public static final int DEFAULT_TILE_SIZE_DP = 44;

  private final OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      if (v == buttonFuture) {
        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
      } else if (v == buttonPast) {
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
      }
    }
  };

  private final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageSelected(int position) {
      title.setText(SDF_Y_M.format(currentMonth.getTime()));
      currentMonth = adapter.getItem(position);
      updateUi();
      dispatchOnMonthChanged(currentMonth);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
  };

  public CustomCalendarView(Context context) {
    this(context, null);
  }

  public CustomCalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    buttonPast = new ImageView(context);
    buttonFuture = new ImageView(context);
    title = new TextView(context);
    pager = new CalendarPager(getContext());

    title.setTextColor(Color.parseColor("#1485F0"));
    title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.custom_small_text_size));
    title.setOnClickListener(onClickListener);
    buttonPast.setOnClickListener(onClickListener);
    buttonFuture.setOnClickListener(onClickListener);
    pager.setOnPageChangeListener(pageChangeListener);
    pager.setPageTransformer(false, new ViewPager.PageTransformer() {
      @Override
      public void transformPage(View page, float position) {
        position = (float) Math.sqrt(1 - Math.abs(position));
        page.setAlpha(position);
      }
    });
    TypedArray a = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.CustomCalendarView, 0, 0);

    newState().commit();

    try {
      final int tileWidth = a.getLayoutDimension(R.styleable.CustomCalendarView_cv_tileWidth, INVALID_TILE_DIMENSION);
      if(tileWidth > INVALID_TILE_DIMENSION){
        setTileWidth(tileWidth);
      }

      final int tileHeight = a.getLayoutDimension(R.styleable.CustomCalendarView_cv_tileHeight, INVALID_TILE_DIMENSION);
      if(tileHeight > INVALID_TILE_DIMENSION){
        setTileHeight(tileHeight);
      }

      Drawable leftMask = a.getDrawable(
          R.styleable.CustomCalendarView_cv_leftArrowMask
      );
      if (leftMask == null) {
        leftMask = getResources().getDrawable(R.mipmap.cv_action_previous);
      }
      setLeftArrowMask(leftMask);
      Drawable rightMask = a.getDrawable(
          R.styleable.CustomCalendarView_cv_rightArrowMask
      );
      if (rightMask == null) {
        rightMask = getResources().getDrawable(R.mipmap.cv_action_next);
      }
      setRightArrowMask(rightMask);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      a.recycle();
    }


    setupChildren();

    currentMonth = Calendar.getInstance();
    setCurrentDate(currentMonth);
  }

  private void setupChildren() {
    topbar = new LinearLayout(getContext());
    topbar.setOrientation(LinearLayout.HORIZONTAL);
    topbar.setClipChildren(false);
    topbar.setClipToPadding(false);
    addView(topbar, new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(TITLE_HEIGHT_DP)));

    buttonPast.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    topbar.addView(buttonPast, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));

    title.setGravity(Gravity.CENTER);
    topbar.addView(title, new LinearLayout.LayoutParams(
        0, LayoutParams.MATCH_PARENT, DEFAULT_DAYS_IN_WEEK - 2
    ));

    buttonFuture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    topbar.addView(buttonFuture, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));

    weekLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_calendar_week_view, null);
    weekLayout.setOnClickListener(null);
    addView(weekLayout, new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(WEEK_HEIGHT_DP)));

    pager.setId(R.id.cv_pager);
    pager.setOffscreenPageLimit(1);
    addView(pager, new LayoutParams(DEFAULT_MAX_WEEKS));
  }

  private void updateUi() {
    title.setText(SDF_Y_M.format(currentMonth.getTime()));
    buttonPast.setEnabled(canGoBack());
    buttonFuture.setEnabled(canGoForward());
    if (canGoBack()) {
      buttonPast.setVisibility(VISIBLE);
    } else {
      buttonPast.setVisibility(INVISIBLE);
    }
    if (canGoForward()) {
      buttonFuture.setVisibility(VISIBLE);
    } else {
      buttonFuture.setVisibility(INVISIBLE);
    }
  }


  public void setTileHeight(int height) {
    this.tileHeight = height;
    requestLayout();
  }

  public void setTileHeightDp(int tileHeightDp) {
    setTileHeight(dpToPx(tileHeightDp));
  }

  public void setTileWidth(int width) {
    this.tileWidth = width;
    requestLayout();
  }

  public void setTileWidthDp(int tileWidthDp) {
    setTileWidth(dpToPx(tileWidthDp));
  }

  public boolean canGoForward() {
    return pager.getCurrentItem() < (adapter.getCount() - 1);
  }

  public boolean canGoBack() {
    return pager.getCurrentItem() > 0;
  }

  protected void onDateClicked(final DayView dayView) {
    final Calendar currentDate = getCurrentDate();
    final Calendar selectedDate = dayView.getDate();
    final int currentMonth = currentDate.get(Calendar.MONTH);
    final int selectedMonth = selectedDate.get(Calendar.MONTH);

    if (currentMonth != selectedMonth) {
      if (CalendarTools.compareCalendar(currentDate, selectedDate) > 0) {
        goToPrevious();
      } else if (CalendarTools.compareCalendar(currentDate, selectedDate) < 0) {
        goToNext();
      }
    }
    onDateClicked(dayView.getDate(), !dayView.isChecked());

  }

  public void goToPrevious() {
    if (canGoBack()) {
      pager.setCurrentItem(pager.getCurrentItem() - 1, true);
    }
  }

  public void goToNext() {
    if (canGoForward()) {
      pager.setCurrentItem(pager.getCurrentItem() + 1, true);
    }
  }

  public void setLeftArrowMask(Drawable icon) {
    leftArrowMask = icon;
    buttonPast.setImageDrawable(icon);
  }

  public void setRightArrowMask(Drawable icon) {
    rightArrowMask = icon;
    buttonFuture.setImageDrawable(icon);
  }

  public Calendar getCurrentDate() {
    return adapter.getItem(pager.getCurrentItem());
  }

  private void setRangeDates(Calendar min, Calendar max) {
    Calendar c = currentMonth;
    adapter.setRangeDates(min, max);
    currentMonth = c;
    if (min != null) {
      currentMonth = CalendarTools.compareCalendar(min, currentMonth) > 0 ? min : currentMonth;
    }
    int position = adapter.getIndexForDay(c);
    pager.setCurrentItem(position, false);
    updateUi();
  }

  /**
   * @param day a CalendarDay to focus the calendar on. Null will do nothing
   */
  public void setCurrentDate(@Nullable Calendar day) {
    setCurrentDate(day, true);
  }

  /**
   * @param day             a CalendarDay to focus the calendar on. Null will do nothing
   * @param useSmoothScroll use smooth scroll when changing months.
   */
  public void setCurrentDate(@Nullable Calendar day, boolean useSmoothScroll) {
    if (day == null) {
      return;
    }
    int index = adapter.getIndexForDay(day);
    pager.setCurrentItem(index, useSmoothScroll);
    updateUi();
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()
    );
  }

  /**
   * Call by {@link MonthView} to indicate that a day was clicked and we should handle it.
   * This method will always process the click to the selected date.
   *
   * @param date        date of the day that was clicked
   * @param nowSelected true if the date is now selected, false otherwise
   */
  protected void onDateClicked(@NonNull Calendar date, boolean nowSelected) {
    adapter.clearSelections();
    adapter.setDateSelected(date, true);
    dispatchOnDateSelected(date, true);
  }

  protected void dispatchOnMonthChanged(final Calendar day) {
    OnMonthChangedListener l = monthChangedListener;
    if (l != null) {
      l.onMonthChanged(this, day);
    }
  }

  protected void dispatchOnDateSelected(final Calendar day, final boolean selected) {
    OnDateSelectedListener l = dateSelectedListener;
    if (l != null) {
      l.onDateSelected(this, day);
    }
  }

  public void setSelectedDate(Calendar date) {
    clearSelection();
    if (date != null) {
      setDateSelected(date, true);
    }
  }

  public void setDateSelected(@Nullable Calendar day, boolean selected) {
    if (day == null) {
      return;
    }
    adapter.setDateSelected(day, selected);
  }

  public void clearSelection() {
    List<Calendar> dates = adapter.getSelectedDates();
    adapter.clearSelections();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return pager.dispatchTouchEvent(event);
  }

  protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
    final int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
    final int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
    final int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
    final int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

    //We need to disregard padding for a while. This will be added back later
    final int desiredWidth = specWidthSize - getPaddingLeft() - getPaddingRight();
    final int desiredHeight = specHeightSize - getPaddingTop() - getPaddingBottom();

    final int weekCount = 6;

    final int viewTileHeight = weekCount ;

    //Calculate independent tile sizes for later
    int desiredTileWidth = desiredWidth / DEFAULT_DAYS_IN_WEEK;
    int desiredTileHeight = (desiredHeight - dpToPx(TITLE_HEIGHT_DP + WEEK_HEIGHT_DP)) / viewTileHeight;

    int measureTileSize = -1;
    int measureTileWidth = -1;
    int measureTileHeight = -1;

    if (this.tileWidth != INVALID_TILE_DIMENSION || this.tileHeight != INVALID_TILE_DIMENSION) {
      if (this.tileWidth > 0) {
        //We have a tileWidth set, we should use that
        measureTileWidth = this.tileWidth;
      } else {
        measureTileWidth = desiredTileWidth;
      }
      if (this.tileHeight > 0) {
        //We have a tileHeight set, we should use that
        measureTileHeight = this.tileHeight;
      } else {
        measureTileHeight = desiredTileHeight;
      }
    } else if (specWidthMode == MeasureSpec.EXACTLY) {
      if (specHeightMode == MeasureSpec.EXACTLY) {
        //Pick the larger of the two explicit sizes
        measureTileSize = Math.max(desiredTileWidth, desiredTileHeight);
      } else {
        //Be the width size the user wants
        measureTileSize = desiredTileWidth;
      }
    } else if (specHeightMode == MeasureSpec.EXACTLY) {
      //Be the height size the user wants
      measureTileSize = desiredTileHeight;
    }

    if (measureTileSize > 0) {
      //Use measureTileSize if set
      measureTileHeight = measureTileSize;
      measureTileWidth = measureTileSize;
    } else if (measureTileSize <= 0) {
      if (measureTileWidth <= 0) {
        //Set width to default if no value were set
        measureTileWidth = dpToPx(DEFAULT_TILE_SIZE_DP);
      }
      if (measureTileHeight <= 0) {
        //Set height to default if no value were set
        measureTileHeight = dpToPx(DEFAULT_TILE_SIZE_DP);
      }
    }

    //Calculate our size based off our measured tile size
    int measuredWidth = specWidthSize;
    int measuredHeight = measureTileHeight * viewTileHeight + dpToPx(TITLE_HEIGHT_DP + WEEK_HEIGHT_DP);

    //Put padding back in from when we took it away
    measuredWidth += getPaddingLeft() + getPaddingRight();
    measuredHeight += getPaddingTop() + getPaddingBottom();

    //Contract fulfilled, setting out measurements
    setMeasuredDimension(
        //We clamp inline because we want to use un-clamped versions on the children
        clampSize(measuredWidth, widthMeasureSpec),
        clampSize(measuredHeight, heightMeasureSpec)
    );

    int count = getChildCount();

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (i == 0 || i == 1) {
        MarginLayoutParams p = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidthSize, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
      } else if (i == 2) {
        LayoutParams p = (LayoutParams) child.getLayoutParams();

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            specWidthSize,
            MeasureSpec.EXACTLY
        );

        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            p.height * measureTileHeight,
            MeasureSpec.EXACTLY
        );

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final int count = getChildCount();

    final int parentLeft = getPaddingLeft();
    final int parentWidth = right - left - parentLeft - getPaddingRight();

    int childTop = getPaddingTop();

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() == View.GONE) {
        continue;
      }

      final int width = child.getMeasuredWidth();
      final int height = child.getMeasuredHeight();

      int delta = (parentWidth - width) / 2;
      int childLeft = parentLeft + delta;

      child.layout(childLeft, childTop, childLeft + width, childTop + height);

      childTop += height;
    }
  }

  private static int clampSize(int size, int spec) {
    int specMode = MeasureSpec.getMode(spec);
    int specSize = MeasureSpec.getSize(spec);
    switch (specMode) {
      case MeasureSpec.EXACTLY: {
        return specSize;
      }
      case MeasureSpec.AT_MOST: {
        return Math.min(size, specSize);
      }
      case MeasureSpec.UNSPECIFIED:
      default: {
        return size;
      }
    }
  }

  public void setDateSelectedListener(OnDateSelectedListener dateSelectedListener) {
    this.dateSelectedListener = dateSelectedListener;
  }

  public void setMonthChangedListener(OnMonthChangedListener monthChangedListener) {
    this.monthChangedListener = monthChangedListener;
  }

  public State state() {
    return state;
  }

  /**
   * Initialize the parameters from scratch.
   */
  public StateBuilder newState() {
    return new StateBuilder();
  }

  public class State {
    public final Calendar minDate;
    public final Calendar maxDate;

    public State(StateBuilder builder) {
      minDate = builder.minDate;
      maxDate = builder.maxDate;
    }

    /**
     * Modify parameters from current state.
     */
    public StateBuilder edit() {
      return new StateBuilder(this);
    }

  }

  public class StateBuilder {
    public Calendar minDate = null;
    public Calendar maxDate = null;

    public StateBuilder() {
    }

    private StateBuilder(final State state) {
      minDate = state.minDate;
      maxDate = state.maxDate;
    }


    public StateBuilder setMinimumDate(@Nullable Calendar calendar) {
      minDate = calendar;
      return this;
    }

    /**
     * @param calendar set the maximum selectable date, null for no maximum
     */
    public StateBuilder setMaximumDate(@Nullable Calendar calendar) {
      maxDate = calendar;
      return this;
    }

    public void commit() {
      CustomCalendarView.this.commit(new State(this));
    }
  }

  private void commit(State state) {
    this.state = state;
    // Save states parameters
    minDate = state.minDate;
    maxDate = state.maxDate;

    // Recreate adapter
    adapter = new CalendarPagerAdapter(this);
    pager.setAdapter(adapter);
    setRangeDates(minDate, maxDate);

    // Reset height params after mode change
    pager.setLayoutParams(new LayoutParams(DEFAULT_MAX_WEEKS));
    setCurrentDate(!adapter.getSelectedDates().isEmpty() ? adapter.getSelectedDates().get(0) : Calendar.getInstance());
    updateUi();
  }


  /**
   * Simple layout params for MaterialCalendarView. The only variation for layout is height.
   */
  protected static class LayoutParams extends MarginLayoutParams {

    public LayoutParams(int tileHeight) {
      super(MATCH_PARENT, tileHeight);
    }

  }

  /**
   * 监听选择某一天的回调
   */
  public interface OnDateSelectedListener {

    /**
     * 当日历某一天被点击时调用
     */
    void onDateSelected(@NonNull CustomCalendarView widget, @NonNull Calendar calendar);
  }

  /**
   * The callback used to indicate the user changes the displayed month
   */
  public interface OnMonthChangedListener {

    /**
     * Called upon change of the selected day
     */
    void onMonthChanged(CustomCalendarView widget, Calendar calendar);
  }
}
