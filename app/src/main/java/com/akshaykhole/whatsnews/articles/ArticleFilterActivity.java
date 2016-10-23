package com.akshaykhole.whatsnews.articles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.akshaykhole.whatsnews.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ArticleFilterActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private Calendar calendarForStartDate;
    private Calendar calendarForEndDate;
    private Calendar c;
    private Boolean selectingStartDate = true;
    private Boolean selectedStartDate = false;
    private Boolean selectedEndDate = false;
    TextView tvStartDate;
    TextView tvEndDate;
    Spinner spinnerSortOrder;
    CheckBox cbArt;
    CheckBox cbFashion;
    CheckBox cbSports;

    public void showDatePicker(View v) {
        if(v.getId() == findViewById(R.id.btnStartDate).getId()) {
            selectingStartDate = true;
        } else {
            selectingStartDate = false;
        }

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_filter);

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);

        c = Calendar.getInstance();
        calendarForStartDate = Calendar.getInstance();
        calendarForEndDate = Calendar.getInstance();

        spinnerSortOrder = (Spinner) findViewById(R.id.spnrSelectSortOrder);

        ArrayAdapter<CharSequence> adapterForSortOrderSpinner = ArrayAdapter.createFromResource(this,
                R.array.filter_sort_order, android.R.layout.simple_spinner_item);

        adapterForSortOrderSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortOrder.setAdapter(adapterForSortOrderSpinner);

        cbArt = (CheckBox) findViewById(R.id.checkbox_arts);
        cbFashion = (CheckBox) findViewById(R.id.checkbox_fashion_and_style);
        cbSports = (CheckBox) findViewById(R.id.checkbox_sports);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(selectingStartDate) {
            c = calendarForStartDate;
            selectedStartDate = true;
        } else {
            selectedEndDate = true;
            c = calendarForEndDate;
        }

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat formatter = new SimpleDateFormat("MM / dd / yyyy");

        String date = formatter.format(c.getTime());

        if(selectingStartDate) {
            tvStartDate.setText(date);
        } else {
            tvEndDate.setText(date);
        }
    }

    public void onSubmit(View v) {
        Intent intent = new Intent();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        if (selectedStartDate) {
            intent.putExtra("startDate", formatter.format(calendarForStartDate.getTime()));
        }

        if (selectedEndDate) {
            intent.putExtra("endDate", formatter.format(calendarForEndDate.getTime()));
        }

        if(spinnerSortOrder.getSelectedItemPosition() == 0) {
            intent.putExtra("sortOrder", "newest");
        } else if (spinnerSortOrder.getSelectedItemPosition() == 1) {
            intent.putExtra("sortOrder", "oldest");
        }

        ArrayList<String> selectedCheckboxes = new ArrayList<>();

        // TODO: DRY this up.
        if(cbArt.isChecked()) {
            selectedCheckboxes.add("Art");
        }

        if(cbFashion.isChecked()) {
            selectedCheckboxes.add("Fashion & Style");
        }

        if(cbSports.isChecked()) {
            selectedCheckboxes.add("Sports");
        }

        if (selectedCheckboxes.size() > 0) {
            String newsDeskParamString;
            newsDeskParamString = "news_desk:(";

            for(int i = 0; i < selectedCheckboxes.size(); ++i) {
                newsDeskParamString += "\"" + selectedCheckboxes.get(i).toString() + "\" ";
            }

            newsDeskParamString += ")";
            intent.putExtra("newsDesk", newsDeskParamString);
        }

        setResult(1, intent);
        finish();
    }
}
