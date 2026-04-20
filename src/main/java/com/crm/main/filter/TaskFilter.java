package com.crm.main.filter;

import com.crm.main.filter.predicate.TaskPredicateBuilder;
import com.crm.main.persistance.entity.Task;
import com.crm.sharedlib.core.filter.criteria.BaseCriteriaFilter;
import com.crm.sharedlib.core.filter.criteria.impl.DefaultCountableFilter;
import com.crm.sharedlib.core.filter.criteria.impl.DefaultOrderByApplier;
import com.crm.sharedlib.core.filter.criteria.impl.DefaultPageableFilter;
import com.crm.sharedlib.core.filter.criteria.interfaces.CountableFilter;
import com.crm.sharedlib.core.filter.criteria.interfaces.OrderByApplier;
import com.crm.sharedlib.core.filter.criteria.interfaces.PageableBuilder;
import com.crm.sharedlib.core.filter.criteria.interfaces.PredicateBuilder;
import org.springframework.stereotype.Component;

@Component
public class TaskFilter extends BaseCriteriaFilter<Task> {

    @Override
    protected Class<Task> getEntityClass() {
        return Task.class;
    }

    @Override
    protected String getDefaultSortField() {
        return "id";
    }

    @Override
    protected PredicateBuilder<Task> getPredicateBuilder() {
        return new TaskPredicateBuilder();
    }

    @Override
    protected CountableFilter<Task> getCountableFilter() {
        return new DefaultCountableFilter<>();
    }

    @Override
    protected PageableBuilder getPageableBuilder() {
        return new DefaultPageableFilter();
    }

    @Override
    protected OrderByApplier<Task> getOrderByApplier() {
        return new DefaultOrderByApplier<>();
    }

}
