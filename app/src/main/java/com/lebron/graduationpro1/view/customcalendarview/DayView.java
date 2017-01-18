package com.lebron.graduationpro1.view.customcalendarview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lebron.graduationpro1.R;

import java.util.Calendar;


public class DayView extends LinearLayout {

  private Calendar date;
  private boolean isInRange = true;
  private boolean isInMonth = true;
  private CheckedTextView tvDate;
  private TextView tvToday;
  private boolean isChecked;
  private Context mContext;

  public DayView(Context context, Calendar day) {
    super(context);
    mContext = context;
    setGravity(Gravity.CENTER);
    initView(day, context);
    setDay(day);
  }

  private void initView(Calendar date, Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.custom_calendar_item_day, this);
    tvDate = (CheckedTextView) view.findViewById(R.id.tv_date);
    tvToday = (TextView) view.findViewById(R.id.tv_today);
  }

  public void setDay(Calendar date) {
    this.date = date;
    tvDate.setText(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
    if (CalendarTools.sameDay(date, Calendar.getInstance())) {
      tvToday.setVisibility(VISIBLE);
      tvDate.setBackgroundResource(R.drawable.bg_custom_calendar_today);
    } else if (CalendarTools.isWeekend(date)) {
      tvToday.setVisibility(INVISIBLE);
    }

    if (CalendarTools.sameDay(date, Calendar.getInstance())) {
      tvDate.setTextColor(mContext.getResources().getColorStateList(R.color.custom_calendar_today));
    } else if (CalendarTools.isWeekend(date)) {
      tvDate.setTextColor(mContext.getResources().getColorStateList(R.color.custom_calendar_weekend));
    } else {
      tvDate.setTextColor(mContext.getResources().getColorStateList(R.color.custom_calendar_normal_day));
    }
  }

  public Calendar getDate() {
    return date;
  }

  protected void setupSelection(boolean inRange, boolean inMonth) {
    this.isInMonth = inMonth;
    this.isInRange = inRange;
    setEnabled();
  }

  private void setEnabled() {
    boolean enabled = isInMonth && isInRange;
    super.setEnabled(isInRange);
    tvDate.setEnabled(isInRange);

//    boolean showOtherMonths = showOtherMonths(showOtherDates);
//    boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;
//    boolean showDecoratedDisabled = showDecoratedDisabled(showOtherDates);
//
//    boolean shouldBeVisible = enabled;
//
//    if (!isInMonth && showOtherMonths) {
//      shouldBeVisible = true;
//    }
//
//    if (!isInRange && showOutOfRange) {
//      shouldBeVisible |= isInMonth;
//    }
//
//    if (isDecoratedDisabled && showDecoratedDisabled) {
//      shouldBeVisible |= isInMonth && isInRange;
//    }
//
    if (!isInMonth) {
      tvDate.setTextColor(mContext.getResources().getColorStateList(R.color.custom_calendar_weekend));
    }
    if (CalendarTools.sameDay(date, Calendar.getInstance())) {
      tvToday.setVisibility(VISIBLE);
    } else {
      tvToday.setVisibility(INVISIBLE);
    }

  }

  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
    tvDate.setChecked(isChecked);
  }

  public boolean isChecked() {
    return isChecked;
  }

}
