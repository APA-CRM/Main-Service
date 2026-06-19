package com.crm.main.service.jobs.handlers;

import com.crm.main.persistance.entity.Task;
import com.crm.main.service.TaskService;
import com.crm.main.service.jobs.requests.TaskReminderJobRequest;
import com.crm.main.service.producer.TaskReminderProducer;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskReminderJobHandler implements JobRequestHandler<TaskReminderJobRequest> {

    private final static Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(TaskReminderJobHandler.class));

    private final TaskService taskService;

    private final TaskReminderProducer producer;

    @Override
    @Job(name = "Task's reminder job")
    public void run(TaskReminderJobRequest jobRequest) throws Exception {
        log.debug("Starting remind about task {}", jobRequest.getTaskId());

        Task task = taskService.getTaskOrThrowException(jobRequest.getTaskId());

        producer.remindAboutTask(task);

        log.debug("Task {} was sent to the queue about reminding the task", jobRequest.getTaskId());
    }
}
