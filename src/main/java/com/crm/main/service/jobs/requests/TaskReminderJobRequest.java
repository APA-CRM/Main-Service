package com.crm.main.service.jobs.requests;

import com.crm.main.service.jobs.handlers.TaskReminderJobHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReminderJobRequest implements JobRequest {

    private UUID taskId;

    @Override
    public Class<? extends JobRequestHandler<TaskReminderJobRequest>> getJobRequestHandler() {
        return TaskReminderJobHandler.class;
    }
}
