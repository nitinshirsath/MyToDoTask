package com.example.mytodotask.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.adapters.TodaysTaskAdapter;
import com.example.mytodotask.adapters.UpcomingTaskAdapter;
import com.example.mytodotask.databinding.TodayTaskFragmentBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.roomDatabase.MyRoomDatabase;
import com.example.mytodotask.utilities.Constants;
import com.example.mytodotask.viewModel.TaskHistoryViewModel;
import com.example.mytodotask.viewModel.TodayTaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayTaskFragment extends Fragment implements OnClickDeleteTask {
    private TodayTaskViewModel mViewModel;
    private TodayTaskFragmentBinding binding;
    private MyRoomDatabase roomDatabase;
    private SimpleDateFormat simpleDateFormat;
    private String currentDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.today_task_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TodayTaskViewModel.class);
        setInitialization();
        onClickFinishedTaskList();
        onClickAllTaskHistory();
        onClickNewTaskList();
        getTodaysTaskList();
        getUpcomingTaskList();
        return binding.getRoot();
    }

    private void setInitialization() {
        roomDatabase = Room.databaseBuilder(getActivity(), MyRoomDatabase.class, Constants.DATABASE_NAME)
                .allowMainThreadQueries().build();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDateFormat.format(new Date());
    }

    public void getTodaysTaskList() {
        List<Task> taskList = roomDatabase.dao().getTodaysTaskList(currentDate);
        if (taskList != null && taskList.size() > 0)
            binding.tvNoTodaysTaskAvailable.setVisibility(View.GONE);
        else
            binding.tvNoTodaysTaskAvailable.setVisibility(View.VISIBLE);
        setTodaysTaskListAdapter(taskList);
    }

    private void setTodaysTaskListAdapter(List<Task> taskList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        binding.rvTodayTaskList.setLayoutManager(gridLayoutManager);
        TodaysTaskAdapter taskAdapter = new TodaysTaskAdapter(taskList, this);
        binding.rvTodayTaskList.setAdapter(taskAdapter);
    }

    private void getUpcomingTaskList() {
        List<Task> taskList = roomDatabase.dao().getUpcomingTaskList(currentDate);
        if (taskList != null && taskList.size() > 0)
            binding.tvNotUpcomingTaskAvailable.setVisibility(View.GONE);
        else
            binding.tvNotUpcomingTaskAvailable.setVisibility(View.VISIBLE);
        setUpcomingTaskListAdapter(taskList);
    }

    private void setUpcomingTaskListAdapter(List<Task> taskList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        binding.rvUpcomingTaskList.setLayoutManager(gridLayoutManager);
        UpcomingTaskAdapter taskAdapter = new UpcomingTaskAdapter(taskList, this);
        binding.rvUpcomingTaskList.setAdapter(taskAdapter);
    }

    private void onClickAllTaskHistory() {
        binding.ivAllTaskHistory.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_frag_todays_to_all_history));
    }

    private void onClickFinishedTaskList() {
        binding.ivPrevFinishedTask.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_frag_todays_to_prev));
    }

    private void onClickNewTaskList() {
        binding.ivNewTask.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.fragment_new_task));
    }

    @Override
    public void onClickDeleteTaskListener(int taskId, boolean todaysTask) {
        deleteConfirmation(taskId, todaysTask);
    }

    @Override
    public void onClickFinishedTask(int taskId, Task task, boolean isChecked) {
        task.setFinished(isChecked ? "true" : "false");
        roomDatabase.dao().updateFinishUnfinishStatus(taskId, task.getFinished());
        Constants.showMessage(getActivity(), "Status Changed Successfully");
    }

    @Override
    public void onClickUpdateTask(Task task) {
        if (task.getFinished().equalsIgnoreCase("true")) {
            Constants.showMessage(getActivity(), "You can not update Finished Task");
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("TASK", task);
            Navigation.findNavController(getView()).navigate(R.id.fragment_new_task, bundle);
        }
    }

    private void deleteConfirmation(int taskId, boolean todaysTask) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do You Want To Delete This Task ?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Constants.showMessage(getActivity(), "Task Deleted Successfully");
                    roomDatabase.dao().deleteTaskById(taskId);
                    if (todaysTask)
                        getTodaysTaskList();
                    else
                        getUpcomingTaskList();
                })
                .setNegativeButton("No", (arg0, arg1) -> {
                })
                .show();
    }
}