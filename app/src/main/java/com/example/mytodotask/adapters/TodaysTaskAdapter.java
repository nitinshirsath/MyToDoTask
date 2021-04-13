package com.example.mytodotask.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.databinding.TodaysTaskAdapterBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.utilities.Constants;

import java.util.List;

public class TodaysTaskAdapter extends RecyclerView.Adapter<TodaysTaskAdapter.TodaysTaskViewHolder> {
    private List<Task> taskList;
    private OnClickDeleteTask onClickDeleteTask;

    public TodaysTaskAdapter(List<Task> taskList,
                             OnClickDeleteTask onClickDeleteTask) {
        this.taskList = taskList;
        this.onClickDeleteTask = onClickDeleteTask;
    }

    @NonNull
    @Override
    public TodaysTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TodaysTaskAdapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.todays_task_adapter, parent, false);
        return new TodaysTaskAdapter.TodaysTaskViewHolder(binding, onClickDeleteTask);
    }

    @Override
    public void onBindViewHolder(@NonNull TodaysTaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task != null) {
            if (!Constants.isEmptyOrNull(task.getTaskName()))
                holder.binding.tvTaskDesc.setText(task.getTaskName());
            if (!Constants.isEmptyOrNull(task.getTaskTime()))
                holder.binding.tvTime.setText(task.getTaskTime());
            holder.binding.cbFinished.setChecked(!Constants.isEmptyOrNull(task.getFinished()) && task.getFinished().equalsIgnoreCase("true"));
            holder.binding.imgDeleteTask.setOnClickListener(v -> onClickDeleteTask.onClickDeleteTaskListener(task.getTaskId(), true));
            holder.binding.cbFinished.setOnCheckedChangeListener((buttonView, isChecked) -> {
                onClickDeleteTask.onClickFinishedTask(task.getTaskId(), task, isChecked);
            });
            holder.binding.imgEditTask.setOnClickListener(v -> onClickDeleteTask.onClickUpdateTask(task));
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public class TodaysTaskViewHolder extends RecyclerView.ViewHolder {
        TodaysTaskAdapterBinding binding;
        OnClickDeleteTask onClickDeleteTask;

        public TodaysTaskViewHolder(@NonNull TodaysTaskAdapterBinding binding, OnClickDeleteTask onClickDeleteTask) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClickDeleteTask = onClickDeleteTask;
        }
    }
}
