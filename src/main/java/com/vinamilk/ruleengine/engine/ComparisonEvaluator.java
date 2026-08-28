package com.vinamilk.ruleengine.engine;

public class ComparisonEvaluator extends AbstractRuleEvaluator {

    private final String operator;

    public ComparisonEvaluator(String operator, Object expectedValue) {
        super(expectedValue);
        this.operator = operator.toUpperCase();
    }

    @Override
    public boolean evaluate(Object dataValue) {
        if ("EQ".equals(operator)) {
            return dataValue != null && dataValue.equals(getExpectedValue());
        }
        if ("GT".equals(operator)) {
            return compareNumbers(dataValue) > 0;
        }
        if ("LT".equals(operator)) {
            return compareNumbers(dataValue) < 0;
        }
        return false;
    }

    @Override
    public String failureMessage(String field, Object dataValue) {
        String comparison = switch (operator) {
            case "EQ" -> "equal";
            case "GT" -> "greater than";
            case "LT" -> "less than";
            default -> operator;
        };
        return field + " must be " + comparison + " " + getExpectedValue()
            + ", got " + dataValue;
    }
}
