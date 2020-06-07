package com.example.app.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    private long countdownInMillis;
    private long timeLeftInMillis;

    private int pomodoroIterations = 0;

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    void setIndex(int index) {
        mIndex.setValue(index);
    }

    int getIndex() { return mIndex.getValue(); }

    long getCountdownInMillis() {
        return countdownInMillis;
    }

    void setCountdownInMillis(long countdownInMillis) {
        this.countdownInMillis = countdownInMillis;
    }

    long getTimeLeftInMillis() {
        return timeLeftInMillis;
    }

    void setTimeLeftInMillis(long timeLeftInMillis) {
        this.timeLeftInMillis = timeLeftInMillis;
    }

    int getPomodoroIterations(){
        return pomodoroIterations;
    }

    void setPomodoroIterations(int pomodoroIterations){
        this.pomodoroIterations = pomodoroIterations;
    }
}