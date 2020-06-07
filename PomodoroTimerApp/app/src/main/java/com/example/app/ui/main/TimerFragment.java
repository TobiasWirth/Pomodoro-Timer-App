package com.example.app.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.app.MainActivity;
import com.example.app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

/**
 * A fragment creating either a Pomodoro Timer, a Short Break Timer or a Long Break Timer.
 */
public class TimerFragment extends Fragment {

    private static final String ARG_INDEX_NUMBER = "index_number";

    private static final long[] vibratePattern = new long[]{0, 700, 300, 700, 500, 700, 300, 700};

    private TimerViewModel timerViewModel;

    private Button startPauseButton;
    private Button resetButton;
    private CountDownTimer countDownTimer;
    private TextView countDownTextView;

    private boolean timerRunning = false;

    static TimerFragment newInstance(int index) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX_NUMBER, index);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX_NUMBER);
        }

        long countdownTime;

        switch(index){
            case 2:
                countdownTime = ((MainActivity)getActivity()).getShortBreakCountdown();
                break;
            case 3:
                countdownTime = ((MainActivity)getActivity()).getLongBreakCountdown();
                break;
            default:
                countdownTime = ((MainActivity)getActivity()).getPomodoroCountdown();
                break;
        }

        timerViewModel.setIndex(index);
        timerViewModel.setCountdownInMillis(countdownTime);
        timerViewModel.setTimeLeftInMillis(countdownTime);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        countDownTextView = root.findViewById(R.id.timer_text_view);

        startPauseButton = root.findViewById(R.id.button_start);
        resetButton = root.findViewById(R.id.button_reset);

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timerRunning){
                    setUpTimer();
                    startTimer();
                }else {
                    pauseTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        setUpTimer();

        return root;
    }

    private void setUpTimer(){
        countDownTimer = new CountDownTimer(timerViewModel.getTimeLeftInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerViewModel.setTimeLeftInMillis(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startPauseButton.setText(R.string.start_timer_text);
                updateCountDownText();

                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(vibratePattern, -1);

                // increment pomodoro iterations by 1 if current timer is a pomodoro timer
                if(timerViewModel.getIndex() == 1) {
                    timerViewModel.setPomodoroIterations(timerViewModel.getPomodoroIterations() + 1);
                }

                TabLayout tabs = getActivity().findViewById(R.id.tabs);

                if(timerViewModel.getPomodoroIterations() <= 4){
                    //short pause
                    tabs.getTabAt(1).select();
                } else{
                    // long pause
                    tabs.getTabAt(2).select();
                    timerViewModel.setPomodoroIterations(0);
                }

                resetTimer();

            }
        };

        updateCountDownText();
    }

    private void startTimer() {
        if(countDownTimer != null) {

            resetButton.setVisibility(View.GONE);
            startPauseButton.setText(R.string.pause_timer_text);
            countDownTimer.start();
            timerRunning = true;
        }
    }

    private void pauseTimer(){

        countDownTimer.cancel();
        timerRunning = false;
        startPauseButton.setText(R.string.start_timer_text);
        resetButton.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        timerViewModel.setTimeLeftInMillis(timerViewModel.getCountdownInMillis());
        countDownTimer.cancel();
        setUpTimer();
        updateCountDownText();
        resetButton.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText(){
        int minutes = (int) (timerViewModel.getTimeLeftInMillis() / 1000) / 60;
        int seconds = (int) (timerViewModel.getTimeLeftInMillis() / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        countDownTextView.setText(timeFormatted);
    }
}