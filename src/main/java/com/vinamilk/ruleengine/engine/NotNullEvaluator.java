package com.vinamilk.ruleengine.engine;

public class NotNullEvaluator extends AbstractRuleEvaluator {

    public NotNullEvaluator() {
        super(null);
    }

    @Override
    public boolean evaluate(Object dataValue) {
        return dataValue != null;
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        return field + " must not be null";
    }
}
