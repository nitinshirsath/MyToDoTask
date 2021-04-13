package com.example.mytodotask.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.databinding.FinishedTaskAdapterBinding;
import com.example.mytodotask.databinding.TodaysTaskAdapterBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.utilities.Constants;

import java.util.List;

public class FinishedTaskAdapter extends RecyclerView.Adapter<FinishedTaskAdapter.FinishedTaskViewHolder> {
    private List<Task> taskList;
    private OnClickDeleteTask onClickDeleteTask;

    public FinishedTaskAdapter(List<Task> taskList, OnClickDeleteTask onClickDeleteTask) {
        this.taskList = taskList;
        this.onClickDeleteTask = onClickDeleteTask;
    }

    @NonNull
    @Override
    public FinishedTaskAdapter.FinishedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FinishedTaskAdapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.finished_task_adapter, parent, false);
        return new FinishedTaskAdapter.FinishedTaskViewHolder(binding, onClickDeleteTask);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedTaskAdapter.FinishedTaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task != null) {
            if (!Constants.isEmptyOrNull(task.getTaskName()))
                holder.binding.tvTaskDesc.setText(task.getTaskName());
            if (!Constants.isEmptyOrNull(task.getTaskDate()))
                holder.binding.tvDate.setText(task.getTaskDate());
            if (!Constants.isEmptyOrNull(task.getTaskTime()))
                holder.binding.tvTime.setText(task.getTaskTime());
            holder.binding.imgDeleteTask.setOnClickListener(v -> onClickDeleteTask.onClickDeleteTaskListener(task.getTaskId(), true));
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public class FinishedTaskViewHolder extends RecyclerView.ViewHolder {
        FinishedTaskAdapterBinding binding;
        OnClickDeleteTask onClickDeleteTask;

        public FinishedTaskViewHolder(@NonNull FinishedTaskAdapterBinding binding,
                                      OnClickDeleteTask onClickDeleteTask) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClickDeleteTask = onClickDeleteTask;
        }
    }
}
