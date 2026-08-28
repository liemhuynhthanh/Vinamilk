package com.vinamilk.ruleengine.engine;

import com.vinamilk.ruleengine.model.EvaluationResult;
import com.vinamilk.ruleengine.model.Rule;
import com.vinamilk.ruleengine.model.RuleResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class RuleEngine {

    public EvaluationResult evaluate(Map<String, Object> data, List<Rule> rules) {
        List<RuleResult> results = new ArrayList<>();
        boolean hasErrorFail = false;

        for (Rule rule : rules) {
            RuleResult result = evaluateSingleRule(data, rule);
            results.add(result);

            if (!result.isPassed() && "ERROR".equalsIgnoreCase(rule.getSeverity())) {
                hasErrorFail = true;
            }
        }

        return new EvaluationResult(!hasErrorFail, results);
    }

    private RuleResult evaluateSingleRule(Map<String, Object> data, Rule rule) {
        String field    = rule.getField();
        String operator = rule.getOperator();
        Object ruleVal  = rule.getValue();
        Object dataVal  = data.get(field);

        boolean passed;
        String  message = null;

        switch (operator.toUpperCase()) {

            case "NOT_NULL":
                passed = (dataVal != null);
                if (!passed) {
                    message = field + " must not be null";
                }
                break;

            case "EQ":
                passed = (dataVal != null && dataVal.equals(ruleVal));
                if (!passed) {
                    message = field + " must equal " + ruleVal + ", got " + dataVal;
                }
                break;

            case "GT":
                passed = compareNumbers(dataVal, ruleVal) > 0;
                if (!passed) {
                    message = field + " must be greater than " + ruleVal + ", got " + dataVal;
                }
                break;

            case "LT":
                passed = compareNumbers(dataVal, ruleVal) < 0;
                if (!passed) {
                    message = field + " must be less than " + ruleVal + ", got " + dataVal;
                }
                break;

            case "IN":
                @SuppressWarnings("unchecked")
                List<Object> allowedList = (List<Object>) ruleVal;
                passed = (dataVal != null && allowedList.contains(dataVal));
                if (!passed) {
                    message = field + " must be one of " + allowedList + ", got " + dataVal;
                }
                break;

            default:
                passed  = false;
                message = "Unknown operator: " + operator;
        }

        // Nếu pass thì message = null (không cần thông báo lỗi)
        return new RuleResult(rule.getRuleId(), passed, passed ? null : message);
    }

    /**
     * So sánh 2 số — hỗ trợ Integer, Long, Double, Float.
     * @return âm nếu dataVal < ruleVal, 0 nếu bằng, dương nếu lớn hơn
     */
    private int compareNumbers(Object dataVal, Object ruleVal) {
        if (dataVal == null) return -1;
        double d = ((Number) dataVal).doubleValue();
        double r = ((Number) ruleVal).doubleValue();
        return Double.compare(d, r);
    }
}
