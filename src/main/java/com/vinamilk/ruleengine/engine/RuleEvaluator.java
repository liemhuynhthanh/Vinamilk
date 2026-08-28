package com.vinamilk.ruleengine.engine;

public interface RuleEvaluator {

    boolean evaluate(Object dataValue);

    String failureMessage(String field, Object dataValue);
}
