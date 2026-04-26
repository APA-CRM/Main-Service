package com.crm.main.filter.predicate;

import com.crm.main.dto.request.TaskFilterRequest;
import com.crm.main.persistance.entity.Task;
import com.crm.main.persistance.entity.TaskPriority;
import com.crm.main.persistance.entity.TaskStatus;
import com.crm.sharedlib.core.dto.request.BaseFilterRequest;
import com.crm.sharedlib.core.filter.criteria.interfaces.PredicateBuilder;
import com.crm.sharedlib.core.utils.PredicateFilterUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class TaskPredicateBuilder implements PredicateBuilder<Task> {

    @Override
    public List<Predicate> buildPredicates(
            BaseFilterRequest request,
            CriteriaBuilder builder, Root<Task> root
    ) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if (request instanceof TaskFilterRequest filterRequest) {
            filterByTitle(filterRequest, predicates, builder, root);
            filterByPriorityId(filterRequest, predicates, builder, root);
            filterByStatusId(filterRequest, predicates, builder, root);
            filterByAssignedTo(filterRequest, predicates, builder, root);
            filterByCreatedBy(filterRequest, predicates, builder, root);
            filterByDueDate(filterRequest, predicates, builder, root);
            filterByCreatedAt(filterRequest, predicates, builder, root);
            filterByUpdatedAt(filterRequest, predicates, builder, root);
        }

        return predicates;
    }

    private void filterByTitle(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        PredicateFilterUtils.filterBySymbolContains(
                request.getTitle(), "title",
                predicates, builder, root
        );
    }

    private void filterByPriorityId(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        if (isNull(request.getPriorityId()))
            return;

        Join<Task, TaskPriority> priority = root.join("priority");

        predicates.add(
                builder.equal(priority.get("id"), request.getPriorityId())
        );
    }

    private void filterByStatusId(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        if (isNull(request.getStatusId()))
            return;

        Join<Task, TaskStatus> status = root.join("status");

        predicates.add(
                builder.equal(status.get("id"), request.getStatusId())
        );
    }

    private void filterByAssignedTo(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        if (isNull(request.getAssignedTo()))
            return;

        predicates.add(
                builder.equal(
                        root.get("assignedTo"),
                        request.getAssignedTo()
                )
        );
    }

    private void filterByCreatedBy(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        if (isNull(request.getCreatedBy()))
            return;

        predicates.add(
                builder.equal(
                        root.get("createdBy"),
                        request.getCreatedBy()
                )
        );
    }

    private void filterByDueDate(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {

        PredicateFilterUtils.filterByDateRange(
                request.getDueDate(), "dueDate",
                predicates, builder, root
        );
    }

    private void filterByCreatedAt(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        PredicateFilterUtils.filterByDateRange(
                request.getCreatedAt(), "createdAt",
                predicates, builder, root
        );
    }

    private void filterByUpdatedAt(
            TaskFilterRequest request, List<Predicate> predicates,
            CriteriaBuilder builder, Root<Task> root
    ) {
        PredicateFilterUtils.filterByDateRange(
                request.getUpdatedAt(), "updatedAt",
                predicates, builder, root
        );
    }

}
