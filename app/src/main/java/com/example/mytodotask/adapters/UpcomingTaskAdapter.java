package com.example.mytodotask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.databinding.UpcommingTaskAdapterBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.utilities.Constants;

import java.util.List;

public class UpcomingTaskAdapter extends RecyclerView.Adapter<UpcomingTaskAdapter.UpcomingTaskViewHolder> {
    private List<Task> taskList;
    private OnClickDeleteTask onClickDeleteTask;

    public UpcomingTaskAdapter(List<Task> taskList, OnClickDeleteTask onClickDeleteTask) {
        this.taskList = taskList;
        this.onClickDeleteTask = onClickDeleteTask;
    }

    @NonNull
    @Override
    public UpcomingTaskAdapter.UpcomingTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UpcommingTaskAdapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.upcomming_task_adapter, parent, false);
        return new UpcomingTaskAdapter.UpcomingTaskViewHolder(binding, onClickDeleteTask);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingTaskAdapter.UpcomingTaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task != null) {
            if (!Constants.isEmptyOrNull(task.getTaskName()))
                holder.binding.tvTaskDesc.setText(task.getTaskName());
            if (!Constants.isEmptyOrNull(task.getTaskTime()))
                holder.binding.tvTime.setText(task.getTaskTime());
            if (!Constants.isEmptyOrNull(task.getTaskDate()))
                holder.binding.tvDate.setText(task.getTaskDate());
            holder.binding.imgDeleteTask.setOnClickListener(v -> onClickDeleteTask.onClickDeleteTaskListener(task.getTaskId(), false));
            holder.binding.imgEditTask.setOnClickListener(v -> onClickDeleteTask.onClickUpdateTask(task));
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public class UpcomingTaskViewHolder extends RecyclerView.ViewHolder {
        UpcommingTaskAdapterBinding binding;
        OnClickDeleteTask onClickDeleteTask;

        public UpcomingTaskViewHolder(@NonNull UpcommingTaskAdapterBinding binding,
                                      OnClickDeleteTask onClickDeleteTask) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClickDeleteTask = onClickDeleteTask;
        }
    }
}

