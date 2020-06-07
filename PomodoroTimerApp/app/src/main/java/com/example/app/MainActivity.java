package com.example.app;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.app.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    //TODO: restore timer if activity destroyed
    //TODO: account for orientation changes

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_POMODORO = "keyPomodoro";
    public static final String KEY_SHORT_BREAK = "keyShortBreak";
    public static final String KEY_LONG_BREAK = "keyLongBreak";

    // Countdown values in ms
    private long pomodoroCountdownInMillis;
    private long shortBreakCountdownInMillis;
    private long longBreakCountdownInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        loadCountdownValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadCountdownValues(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        pomodoroCountdownInMillis = prefs.getLong(KEY_POMODORO, 1500000);
        shortBreakCountdownInMillis = prefs.getLong(KEY_SHORT_BREAK, 300000);
        longBreakCountdownInMillis = prefs.getLong(KEY_LONG_BREAK, 900000);
    }

    private void updateCountdownValues(long newPomodoroTime, long newShortBreakTime, long newLongBreakTime) {
        //TODO: let values be changed through Options Menu

        pomodoroCountdownInMillis = newPomodoroTime;
        shortBreakCountdownInMillis = newShortBreakTime;
        longBreakCountdownInMillis = newLongBreakTime;

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_POMODORO, newPomodoroTime);
        editor.putLong(KEY_SHORT_BREAK, newShortBreakTime);
        editor.putLong(KEY_LONG_BREAK, newLongBreakTime);
        editor.apply();
    }

    public long getPomodoroCountdown(){
        return pomodoroCountdownInMillis;
    }

    public long getShortBreakCountdown(){
        return shortBreakCountdownInMillis;
    }

    public long getLongBreakCountdown(){
        return longBreakCountdownInMillis;
    }
}