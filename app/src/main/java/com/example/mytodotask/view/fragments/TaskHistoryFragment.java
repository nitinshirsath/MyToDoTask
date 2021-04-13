package com.example.mytodotask.view.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.adapters.AllTaskHistoryAdapter;
import com.example.mytodotask.databinding.TaskHistoryFragmentBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.roomDatabase.MyRoomDatabase;
import com.example.mytodotask.utilities.Constants;
import com.example.mytodotask.viewModel.FinishTaskViewModel;
import com.example.mytodotask.viewModel.TaskHistoryViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskHistoryFragment extends Fragment implements OnClickDeleteTask {
    private TaskHistoryViewModel mViewModel;
    private TaskHistoryFragmentBinding binding;
    private MyRoomDatabase roomDatabase;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.task_history_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TaskHistoryViewModel.class);
        setInitialization();
        onClickBack();
        getAllTaskList();
        return binding.getRoot();
    }

    private void setInitialization() {
        roomDatabase = Room.databaseBuilder(getActivity(), MyRoomDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries().build();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDateFormat.format(new Date());
    }

    private void onClickBack() {
        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getAllTaskList() {
        List<Task> taskList = roomDatabase.dao().getAllTaskHistoryList(currentDate);
        if (taskList != null && taskList.size() > 0)
            binding.tvNoTaskAvailable.setVisibility(View.GONE);
        else
            binding.tvNoTaskAvailable.setVisibility(View.VISIBLE);
        setAllHistoryAdapter(taskList);
    }

    private void setAllHistoryAdapter(List<Task> taskList) {
        binding.rvAllTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        AllTaskHistoryAdapter taskAdapter = new AllTaskHistoryAdapter(taskList, getActivity(), this);
        binding.rvAllTaskList.setAdapter(taskAdapter);
    }

    @Override
    public void onClickDeleteTaskListener(int taskId, boolean todaysTask) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do You Want To Delete This Task ?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Constants.showMessage(getActivity(), "Task Deleted Successfully");
                    roomDatabase.dao().deleteTaskById(taskId);
                    getAllTaskList();
                })
                .setNegativeButton("No", (arg0, arg1) -> {
                })
                .show();
    }

    @Override
    public void onClickFinishedTask(int taskId, Task task, boolean isChecked) {

    }

    @Override
    public void onClickUpdateTask(Task task) {

    }
}