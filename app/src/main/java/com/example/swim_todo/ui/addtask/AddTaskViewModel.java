package com.example.swim_todo.ui.addtask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTaskViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddTaskViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}