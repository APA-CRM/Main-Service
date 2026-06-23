package com.crm.main.service.jobs.handlers;

import com.crm.main.persistance.entity.Task;
import com.crm.main.service.TaskService;
import com.crm.main.service.jobs.requests.TaskReminderJobRequest;
import com.crm.main.service.producer.TaskReminderProducer;
import com.crm.main.service.wrapper.UserClientWrapper;
import com.crm.sharedlib.core.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class TaskReminderJobHandler implements JobRequestHandler<TaskReminderJobRequest> {

    private final static Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(TaskReminderJobHandler.class));

    private final TaskService taskService;
    private final UserClientWrapper userClient;

    private final TaskReminderProducer producer;

    @Override
    @Job(name = "Task's reminder job")
    public void run(TaskReminderJobRequest jobRequest) {
        Task task = taskService.getTaskOrThrowException(jobRequest.getTaskId());

        if (task.getIsReminded()) {
            log.debug("Task {} is already reminded", task.getId());
            return;
        }

        log.debug("Starting remind about task {}", task.getId());

        ArrayList<String> emails = new ArrayList<>(2);

        if (nonNull(task.getAssignedTo())) {
            UserResponse assignedToUser = userClient.getUserById(task.getAssignedTo());

            emails.add(assignedToUser.getEmail());
        }

        UserResponse createdByUser = userClient.getUserById(task.getCreatedBy());
        emails.add(createdByUser.getEmail());

        producer.remindAboutTask(task, emails);

        task.setIsReminded(true);
        task.setReminderAt(null);
        task.setReminderJobId(null);
        taskService.saveTask(task);

        log.debug("Task {} was sent to the queue about reminding the task", jobRequest.getTaskId());
    }
}
