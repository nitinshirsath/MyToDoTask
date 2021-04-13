package com.example.mytodotask.roomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mytodotask.model.Task;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    public void createTask(Task task);

    @Query("Select * from Task where taskDate = :currentDate")
    List<Task> getTodaysTaskList(String currentDate);

    @Query("Select * from Task where taskDate > :currentDate")
    List<Task> getUpcomingTaskList(String currentDate);

    @Query("Select * from Task where finished = :finishedTask")
    List<Task> getFinishedTaskList(String finishedTask);

    @Query("Select * from Task where taskDate < :currentDate")
    List<Task> getAllTaskHistoryList(String currentDate);

    @Query("Delete from Task where taskId = :taskId")
    void deleteTaskById(int taskId);

    @Query("Update Task set finished= :finishStatus where taskId = :taskId")
    void updateFinishUnfinishStatus(int taskId, String finishStatus);

    @Update
    void updateTask(Task task);
}
