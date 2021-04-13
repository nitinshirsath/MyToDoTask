package com.example.mytodotask.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.example.mytodotask.R;
import com.example.mytodotask.databinding.ActivityToDoMainBinding;

public class ToDoMainActivity extends AppCompatActivity {

    private ActivityToDoMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_to_do_main);
    }
}