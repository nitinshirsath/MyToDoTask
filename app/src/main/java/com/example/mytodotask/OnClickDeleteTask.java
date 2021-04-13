package com.example.mytodotask;

import android.view.View;

import com.example.mytodotask.model.Task;

public interface OnClickDeleteTask {
    void onClickDeleteTaskListener(int taskId, boolean todaysTask);

    void onClickFinishedTask(int taskId, Task task, boolean isChecked);

    void onClickUpdateTask(Task task);
}
