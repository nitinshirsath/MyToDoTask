package com.example.mytodotask.view.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.adapters.FinishedTaskAdapter;
import com.example.mytodotask.adapters.TodaysTaskAdapter;
import com.example.mytodotask.databinding.FinishTaskFragmentBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.roomDatabase.MyRoomDatabase;
import com.example.mytodotask.utilities.Constants;
import com.example.mytodotask.viewModel.CreateUpdateTaskViewModel;
import com.example.mytodotask.viewModel.FinishTaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FinishTaskFragment extends Fragment implements OnClickDeleteTask {
    private FinishTaskViewModel mViewModel;
    private FinishTaskFragmentBinding binding;
    private MyRoomDatabase roomDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.finish_task_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(FinishTaskViewModel.class);
        setInitialization();
        onClickBack();
        getFinishedTaskList();
        return binding.getRoot();
    }

    private void setInitialization() {
        roomDatabase = Room.databaseBuilder(getActivity(), MyRoomDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries().build();
    }

    private void onClickBack() {
        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void getFinishedTaskList() {
        List<Task> taskList = roomDatabase.dao().getFinishedTaskList("true");
        if (taskList != null && taskList.size() > 0)
            binding.tvNoFinishedTaskAvailable.setVisibility(View.GONE);
        else
            binding.tvNoFinishedTaskAvailable.setVisibility(View.VISIBLE);
        setFinishedTaskListAdapter(taskList);
    }

    private void setFinishedTaskListAdapter(List<Task> taskList) {
        binding.rvFinishedTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        FinishedTaskAdapter taskAdapter = new FinishedTaskAdapter(taskList, this);
        binding.rvFinishedTaskList.setAdapter(taskAdapter);
    }

    @Override
    public void onClickDeleteTaskListener(int taskId, boolean todaysTask) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do You Want To Delete This Task ?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Constants.showMessage(getActivity(), "Task Deleted Successfully");
                    roomDatabase.dao().deleteTaskById(taskId);
                    getFinishedTaskList();
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