package com.example.qrhunter.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Integer> count = new MutableLiveData<>(0);

    public ProfileViewModel() {
        // You can initialize repository classes here
    }

    public LiveData<Integer> getCount() {
        return count;
    }

    public void addCount() {
        Integer currentValue = count.getValue();
        count.setValue(currentValue + 1);
    }
}