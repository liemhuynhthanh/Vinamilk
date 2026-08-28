package com.vinamilk.ruleengine.engine;

public class EqualsEvaluator extends AbstractRuleEvaluator {

    public EqualsEvaluator(Object expectedValue) {
        super(expectedValue);
    }

    @Override
    public boolean evaluate(Object dataValue) {
        return dataValue != null && dataValue.equals(getExpectedValue());
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        return field + " must be equal " + getExpectedValue() + ", got " + dataValue;
    }
}