package com.vinamilk.ruleengine.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InEvaluator extends AbstractRuleEvaluator {

    private final Set<Object> allowedValues;

    public InEvaluator(Object expectedValue) {
        super(expectedValue);
        if (!(expectedValue instanceof Collection<?> values)) {
            throw new IllegalArgumentException("IN value must be a collection");
        }
        this.allowedValues = new HashSet<>(values);
    }

    @Override
    public boolean evaluate(Object dataValue) {
        return dataValue != null && allowedValues.contains(dataValue);
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        return field + " must be one of " + allowedValues + ", got " + dataValue;
    }
}
