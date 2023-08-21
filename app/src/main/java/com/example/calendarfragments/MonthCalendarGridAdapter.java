package com.example.calendarfragments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthCalendarGridAdapter extends ArrayAdapter {
    private static final String TAG = MonthCalendarGridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<EventObjects> allEvents;
    CalendarCustomView calendarCustomView;
    int[] eventColors;

    public MonthCalendarGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<EventObjects> allEvents) {
        super(context, R.layout.calendar_single_cell_adapter_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        this.calendarCustomView = calendarCustomView;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        eventColors = new int[]{getContext().getResources().getColor(R.color.event_color_01),
                getContext().getResources().getColor(R.color.event_color_02),
                getContext().getResources().getColor(R.color.event_color_03),
                getContext().getResources().getColor(R.color.event_color_04)};
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.calendar_single_cell_adapter_layout, parent, false);
        }
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#eaf3fc"));
        } else {
            view.setAlpha(0.4f);
        }
        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        TextView eventIndicator = (TextView) view.findViewById(R.id.event_id);
        Calendar eventCalendar = Calendar.getInstance();
        int colorIndex = 0;
        for (int i = 0; i < allEvents.size(); i++) {
            eventCalendar.setTime(allEvents.get(i).getDate());
            if (dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1) {
                eventIndicator.setText(allEvents.get(i).getMessage());
                Log.e(TAG, "Final Color " + eventColors[colorIndex]);
                // view.setBackgroundColor((allEvents.get(i).getColor() != 0) ? allEvents.get(i).getColor() : Color.parseColor("#FF0000"));
                view.setBackgroundColor(eventColors[colorIndex]);
            }
            //&& displayYear == eventCalendar.get(Calendar.YEAR)
            colorIndex++;
            if (colorIndex >= eventColors.length)
                colorIndex = 0;
        }
        return view;
    }


    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    public EventObjects getEvent(int position) {
        return allEvents.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}
