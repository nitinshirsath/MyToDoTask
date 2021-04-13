package com.example.mytodotask.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodotask.OnClickDeleteTask;
import com.example.mytodotask.R;
import com.example.mytodotask.databinding.AllTaskHistoryAdapterBinding;
import com.example.mytodotask.model.Task;
import com.example.mytodotask.utilities.Constants;

import java.util.List;

public class AllTaskHistoryAdapter extends RecyclerView.Adapter<AllTaskHistoryAdapter.AllTaskHistoryViewHolder> {
    private List<Task> taskList;
    private OnClickDeleteTask onClickDeleteTask;
    private Activity activity;

    public AllTaskHistoryAdapter(List<Task> taskList,
                                 Activity activity, OnClickDeleteTask onClickDeleteTask) {
        this.taskList = taskList;
        this.onClickDeleteTask = onClickDeleteTask;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllTaskHistoryAdapter.AllTaskHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AllTaskHistoryAdapterBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.all_task_history_adapter, parent, false);
        return new AllTaskHistoryAdapter.AllTaskHistoryViewHolder(binding, onClickDeleteTask);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTaskHistoryAdapter.AllTaskHistoryViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task != null) {
            if (!Constants.isEmptyOrNull(task.getTaskName()))
                holder.binding.tvTaskDesc.setText(task.getTaskName());
            if (!Constants.isEmptyOrNull(task.getTaskDate()))
                holder.binding.tvDate.setText(task.getTaskDate());
            if (!Constants.isEmptyOrNull(task.getTaskTime()))
                holder.binding.tvTime.setText(task.getTaskTime());
            if (!Constants.isEmptyOrNull(task.getFinished())) {
                if (task.getFinished().equalsIgnoreCase("true")) {
                    holder.binding.tvStatus.setText("Finished");
                    holder.binding.tvStatus.setBackground(activity.getResources().getDrawable(R.color.purple_500));
                    holder.binding.tvStatus.setTextColor(activity.getResources().getColor(R.color.white));
                } else {
                    holder.binding.tvStatus.setText("Unfinished");
                    holder.binding.tvStatus.setBackground(activity.getResources().getDrawable(R.color.white));
                    holder.binding.tvStatus.setTextColor(activity.getResources().getColor(R.color.purple_500));
                }
            }
            holder.binding.imgDeleteTask.setOnClickListener(v -> onClickDeleteTask.onClickDeleteTaskListener(task.getTaskId(), true));
        }
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public class AllTaskHistoryViewHolder extends RecyclerView.ViewHolder {
        AllTaskHistoryAdapterBinding binding;
        OnClickDeleteTask onClickDeleteTask;

        public AllTaskHistoryViewHolder(@NonNull AllTaskHistoryAdapterBinding binding,
                                        OnClickDeleteTask onClickDeleteTask) {
            super(binding.getRoot());
            this.binding = binding;
            this.onClickDeleteTask = onClickDeleteTask;
        }
    }
}

