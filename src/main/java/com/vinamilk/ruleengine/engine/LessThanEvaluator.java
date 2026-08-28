package com.vinamilk.ruleengine.engine;

public class LessThanEvaluator extends AbstractRuleEvaluator {

    public LessThanEvaluator(Object expectedValue) {
        super(expectedValue);
    }

    @Override
    public boolean evaluate(Object dataValue) {
        return compareNumbers(dataValue) < 0;
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        return field + " must be less than " + getExpectedValue() + ", got " + dataValue;
    }
}