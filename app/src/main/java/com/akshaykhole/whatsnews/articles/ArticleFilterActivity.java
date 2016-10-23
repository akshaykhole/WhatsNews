package com.akshaykhole.whatsnews.articles;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.akshaykhole.whatsnews.R;

import java.util.Calendar;

public class ArticleFilterActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private Calendar calendarForStartDate;
    private Calendar calendarForEndDate;
    private Calendar c;
    private Boolean selectingStartDate = true;

    public void showDatePicker(View v) {
        if(v.getId() == findViewById(R.id.btnStartDate).getId()) {
            selectingStartDate = true;
        } else {
            selectingStartDate = false;
        }

        Log.d("DEBUG", selectingStartDate.toString());
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_filter);

        c = Calendar.getInstance();
        calendarForStartDate = Calendar.getInstance();
        calendarForEndDate = Calendar.getInstance();

        Spinner spinnerSortOrder = (Spinner) findViewById(R.id.spnrSelectSortOrder);

        ArrayAdapter<CharSequence> adapterForSortOrderSpinner = ArrayAdapter.createFromResource(this,
                R.array.filter_sort_order, android.R.layout.simple_spinner_item);

        adapterForSortOrderSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortOrder.setAdapter(adapterForSortOrderSpinner);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(selectingStartDate) {
            c = calendarForStartDate;
        } else {
            c = calendarForEndDate;
        }

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void onSubmit(View v) {
        Integer dayOfMonth1 = calendarForStartDate.get(Calendar.DAY_OF_MONTH);
        Integer dayOfMonth2 = calendarForEndDate.get(Calendar.DAY_OF_MONTH);
        Log.d("DEBUG", dayOfMonth1.toString());
        Log.d("DEBUG", dayOfMonth2.toString());


        finish();
    }
}
