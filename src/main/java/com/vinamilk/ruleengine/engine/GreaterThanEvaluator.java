package com.vinamilk.ruleengine.engine;

public class GreaterThanEvaluator extends AbstractRuleEvaluator {

    public GreaterThanEvaluator(Object expectedValue) {
        super(expectedValue);
    }

    @Override
    public boolean evaluate(Object dataValue) {
        return compareNumbers(dataValue) > 0;
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        return field + " must be greater than " + getExpectedValue() + ", got " + dataValue;
    }
}