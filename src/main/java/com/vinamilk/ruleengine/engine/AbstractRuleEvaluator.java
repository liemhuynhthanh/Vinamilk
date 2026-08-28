package com.vinamilk.ruleengine.engine;

public abstract class AbstractRuleEvaluator implements RuleEvaluator {

    private final Object expectedValue;

    protected AbstractRuleEvaluator(Object expectedValue) {
        this.expectedValue = expectedValue;
    }

    protected Object getExpectedValue() {
        return expectedValue;
    }

    protected int compareNumbers(Object dataValue) {
        if (dataValue == null) {
            return -1;
        }
        double actual = ((Number) dataValue).doubleValue();
        double expected = ((Number) expectedValue).doubleValue();
        return Double.compare(actual, expected);
    }
}
