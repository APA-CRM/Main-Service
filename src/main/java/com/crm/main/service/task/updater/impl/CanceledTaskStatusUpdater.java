package com.crm.main.service.task.updater.impl;

import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.service.task.updater.TaskStatusUpdater;

public class CanceledTaskStatusUpdater implements TaskStatusUpdater {

    @Override
    public Task update(Task task, TaskStatus taskStatus) {
        task.setStatus(taskStatus);

        task.setReminderAt(null);
        task.setCompletedAt(null);

        return task;
    }

}
