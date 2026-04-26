package com.crm.main.service.task.updater;

import com.crm.main.persistance.entity.TaskStatus;
import com.crm.main.service.task.updater.impl.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class TaskStatusUpdaterFactory {

    public TaskStatusUpdater getTaskStatusUpdater(TaskStatus taskStatus) {
        Assert.notNull(taskStatus, "Task status cannot be null");
        Assert.notNull(taskStatus.getType(), "Task status type cannot be null");

        return switch (taskStatus.getType()) {
            case TODO -> new ToDoTaskStatusUpdater();
            case IN_PROGRESS -> new InProgressTaskStatusUpdater();
            case DONE -> new DoneTaskStatusUpdater();
            case BLOCKED -> new BlockedTaskStatusUpdater();
            case CANCELED -> new CanceledTaskStatusUpdater();
        };
    }

}
