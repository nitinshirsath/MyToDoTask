package com.example.mytodotask.view.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.mytodotask.R;
import com.example.mytodotask.databinding.NewTaskFragmentBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.roomDatabase.MyRoomDatabase;
import com.example.mytodotask.utilities.Constants;
import com.example.mytodotask.viewModel.CreateUpdateTaskViewModel;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

public class CreateUpdateTaskFragment extends Fragment {
    private CreateUpdateTaskViewModel mViewModel;
    private NewTaskFragmentBinding binding;
    private DatePickerDialog datePickerDialog;
    private int date_Year, date_Month, date_Day, mHour, mMinute;
    private Calendar calendar;
    private String taskDate, taskTime, taskInfo;
    private MyRoomDatabase roomDatabase;
    private boolean isForTaskUpdate;
    private Task task;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.new_task_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CreateUpdateTaskViewModel.class);
        setInitialization();
        onClickBack();
        getDateFrom();
        getTime();
        onClickAddTask();
        getArgumentsData();
        onClickDeleteTask();
        return binding.getRoot();
    }

    private void setInitialization() {
        roomDatabase = Room.databaseBuilder(getActivity(), MyRoomDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries().build();
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        calendar.add(Calendar.DATE, 1);
        date_Day = calendar.get(Calendar.DATE);
    }

    private void getDateFrom() {
        binding.llSelectDate.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(
                    getActivity(), R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
                month = month + 1;
                datePickerDialog.setCancelable(true);
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                taskDate = year + "-" + mFormat.format(Double.valueOf(month)) + "-" + mFormat.format(Double.valueOf(dayOfMonth));
                binding.tvDate.setText(taskDate);
            }, date_Year, date_Month, date_Day
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    private void getTime() {
        binding.llSelectTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
                String am_pm = "AM";
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                if (hourOfDay >= 12) {
                    if (hourOfDay >= 13)
                        hourOfDay = hourOfDay - 12;
                    am_pm = "PM";
                } else if (hourOfDay == 0)
                    hourOfDay = 12;
                taskTime = mFormat.format(hourOfDay) + ":" + mFormat.format(minute) + " " + am_pm;
                binding.tvTime.setText(taskTime);
            }, mHour, mMinute, false);
            timePickerDialog.show();
        });
    }

    private void onClickDeleteTask() {
        binding.ivDelete.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setMessage("Do You Want To Delete This Task ?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Constants.showMessage(getActivity(), "Task Deleted Successfully");
                    roomDatabase.dao().deleteTaskById(task.getTaskId());
                    getActivity().onBackPressed();
                })
                .setNegativeButton("No", (arg0, arg1) -> {
                })
                .show());
    }

    private void getArgumentsData() {
        if (getArguments() != null)
            task = (Task) getArguments().getSerializable("TASK");
        if (task != null) {
            binding.tvToolbarText.setText("Update Task");
            binding.btnAddTask.setText("Update Task");
            binding.ivDelete.setVisibility(View.VISIBLE);
            isForTaskUpdate = true;
            setTaskValues(task);
        }
    }

    private void setTaskValues(Task task) {
        taskInfo = task.getTaskName();
        taskDate = task.getTaskDate();
        taskTime = task.getTaskTime();
        if (!Constants.isEmptyOrNull(taskInfo))
            binding.edtTaskDetails.setText(taskInfo);
        if (!Constants.isEmptyOrNull(taskDate))
            binding.tvDate.setText(taskDate);
        if (!Constants.isEmptyOrNull(taskTime))
            binding.tvTime.setText(taskTime);
    }

    private void onClickBack() {
        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private boolean isValidData() {
        boolean isValid = true;
        taskInfo = binding.edtTaskDetails.getText().toString().trim();
        if (Constants.isEmptyOrNull(taskInfo)) {
            isValid = false;
            Constants.showMessage(getActivity(), "Task Details Should Not Be Empty");
        }
        if (Constants.isEmptyOrNull(taskDate)) {
            isValid = false;
            Constants.showMessage(getActivity(), "Task Date Should Not Be Empty");
        }
        if (Constants.isEmptyOrNull(taskTime)) {
            isValid = false;
            Constants.showMessage(getActivity(), "Task Time Should Not Be Empty");
        }
        return isValid;
    }

    private void onClickAddTask() {
        binding.btnAddTask.setOnClickListener(v -> {
            Constants.hideKeyboard(getActivity());
            if (isValidData()) {
                if (task == null)
                    task = new Task();
                task.setTaskName(taskInfo);
                task.setTaskDate(taskDate);
                task.setTaskTime(taskTime);
                task.setFinished(Constants.isEmptyOrNull(task.getFinished()) ? "false" : task.getFinished());
                if (!isForTaskUpdate) {
                    roomDatabase.dao().createTask(task);
                    Constants.showMessage(getActivity(), "New Task Added Successfully");
                } else {
                    roomDatabase.dao().updateTask(task);
                    Constants.showMessage(getActivity(), "Updated Added Successfully");
                }
                getActivity().onBackPressed();
            }
        });
    }
}