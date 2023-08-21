package com.example.calendarfragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MonthViewFragment extends Fragment {
    List<Date> selectedDates;
    Date start, end;
    LinearLayout layoutCalender;
    View custom_view;
    Date initialDate, lastDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.month_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        custom_view = view.findViewById(R.id.custom_view);
        layoutCalender = view.findViewById(R.id.layoutCalender);

        setCalenderView();

        //TODO STEP 5 - Set an OnClickListener, using Navigation.createNavigateOnClickListener()
        TextView day_tv = view.findViewById(R.id.day_tv_three);
        day_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.dayViewFragment, null);
            }
        });

        TextView week_tv = view.findViewById(R.id.week_tv_three);
        week_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.weekViewFragment, null);
            }
        });

        //TODO STEP 6 - Set NavOptions

        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder();
//        navOptionsBuilder.setEnterAnim(R.anim.slide_in_right);
//        navOptionsBuilder.setExitAnim(R.anim.slide_out_left);
//        navOptionsBuilder.setPopEnterAnim(R.anim.slide_in_left);
//        navOptionsBuilder.setPopExitAnim(R.anim.slide_out_right);
        final NavOptions options = navOptionsBuilder.build();

    }

    public void setCalenderView() {
        //Custom Events

        Date dt1 = new Date();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dt1);
        c1.add(Calendar.DATE, 1);
        dt1 = c1.getTime();
        EventObjects eventObjects1 = new EventObjects(1, "Event 1", dt1);

        Date dt2 = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dt2);
        c2.add(Calendar.DATE, 3);
        dt2 = c2.getTime();
        EventObjects eventObjects2 = new EventObjects(1, "Event 2", dt2);

        Date dt3 = new Date();
        Calendar c3 = Calendar.getInstance();
        c3.setTime(dt3);
        c3.add(Calendar.DATE, 4);
        dt3 = c3.getTime();
        EventObjects eventObjects3 = new EventObjects(1, "Event 3", dt3);

        Date dt4 = new Date();
        Calendar c4 = Calendar.getInstance();
        c4.setTime(dt4);
        c4.add(Calendar.DATE, 5);
        dt4 = c4.getTime();
        EventObjects eventObjects4 = new EventObjects(1, "Event 4", dt4);

        List<EventObjects> mEvents = new ArrayList<>();
        //  mEvents.add(eventObjects);
        mEvents.add(eventObjects1);
        mEvents.add(eventObjects2);
        mEvents.add(eventObjects3);
        mEvents.add(eventObjects4);

        ViewGroup parent = (ViewGroup) custom_view.getParent();
        parent.removeView(custom_view);
        layoutCalender.removeAllViews();
        layoutCalender.setOrientation(LinearLayout.VERTICAL);

        final CalendarCustomView calendarCustomView = new CalendarCustomView(getContext(), mEvents);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        calendarCustomView.setLayoutParams(layoutParams);
        layoutCalender.addView(calendarCustomView);

        calendarCustomView.calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getAdapter().getView((int) l, null, null).getAlpha() == 0.4f) {
                    Log.d("hello", "hello");
                } else {
                    Calendar today = Calendar.getInstance();
                    today.setTime(new java.util.Date());

                    Calendar tapedDay = Calendar.getInstance();
                    tapedDay.setTime((Date) adapterView.getAdapter().getItem((int) l));
                    boolean sameDay = tapedDay.get(Calendar.YEAR) == tapedDay.get(Calendar.YEAR) &&
                            today.get(Calendar.DAY_OF_YEAR) == tapedDay.get(Calendar.DAY_OF_YEAR);
                    if (today.after(tapedDay) && !sameDay) {
                        Toast.makeText(getContext(), "You can't select previous date.", Toast.LENGTH_LONG).show();
                    } else {
                        if (initialDate == null && lastDate == null) {
                            initialDate = lastDate = (Date) adapterView.getAdapter().getItem((int) l);
                        } else {
                            initialDate = lastDate;
                            lastDate = (Date) adapterView.getAdapter().getItem((int) l);
                        }
                        if (initialDate != null && lastDate != null)
                            calendarCustomView.setRangesOfDate(makeDateRanges());
                    }
                }
                try {
                    Toast.makeText(getContext(), "Start Date: " + initialDate.toString() + "\n End Date: " + lastDate.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public List<EventObjects> makeDateRanges() {
        if (lastDate.after(initialDate)) {
            start = initialDate;
            end = lastDate;
        } else {
            start = lastDate;
            end = initialDate;
        }
        List<EventObjects> eventObjectses = new ArrayList<>();
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(start);

        while (!gcal.getTime().after(end)) {
            Date d = gcal.getTime();
            EventObjects eventObject = new EventObjects("", d);
            eventObject.setColor(getResources().getColor(R.color.accent));
            eventObjectses.add(eventObject);
            gcal.add(Calendar.DATE, 1);
        }
        return eventObjectses;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //    inflater.inflate(R.menu.main_menu, menu);
    }

}
