package com.example.securekey;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class MyViewModel extends ViewModel implements CoroutineScope {
    private MutableLiveData<String> editTextValueLiveData = new MutableLiveData<>();

    public LiveData<String> getEditTextValue() {
        return editTextValueLiveData;
    }

    public void setEditTextValue(String value) {
        editTextValueLiveData.setValue(value);
    }
    @Override
    public CoroutineContext getCoroutineContext() {
        return (CoroutineContext) Dispatchers.getIO(); // Return the IO dispatcher as the coroutine context
    }

    public CoroutineScope getCoroutineScope() {
        return this; // Return the current instance of the ViewModel as CoroutineScope
    }
}
