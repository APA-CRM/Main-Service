package com.crm.main.service.task.updater;

import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskStatus;

public interface TaskStatusUpdater {

    Task update(Task task, TaskStatus taskStatus);

}
