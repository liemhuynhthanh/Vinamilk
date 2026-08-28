package com.vinamilk.ruleengine.engine;

import com.vinamilk.ruleengine.model.EvaluationResult;
import com.vinamilk.ruleengine.model.Rule;
import com.vinamilk.ruleengine.model.RuleResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
public class RuleEngine {

    private final Map<String, Function<Rule, RuleEvaluator>> evaluators = new HashMap<>();

    public RuleEngine() {
        evaluators.put("NOT_NULL", rule -> new NotNullEvaluator());
        evaluators.put("EQ", rule -> new ComparisonEvaluator(rule.getOperator(), rule.getValue()));
        evaluators.put("GT", rule -> new ComparisonEvaluator(rule.getOperator(), rule.getValue()));
        evaluators.put("LT", rule -> new ComparisonEvaluator(rule.getOperator(), rule.getValue()));
        evaluators.put("IN", rule -> new InEvaluator(rule.getValue()));
    }

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
        String field = rule.getField();
        Object dataValue = data.get(field);
        Function<Rule, RuleEvaluator> evaluatorFactory = evaluators.get(
            rule.getOperator().toUpperCase()
        );

        if (evaluatorFactory == null) {
            return new RuleResult(
                rule.getRuleId(),
                false,
                "Unknown operator: " + rule.getOperator()
            );
        }

        RuleEvaluator evaluator = evaluatorFactory.apply(rule);
        boolean passed = evaluator.evaluate(dataValue);
        String message = passed ? null : evaluator.failureMessage(field, dataValue);
        return new RuleResult(rule.getRuleId(), passed, message);
    }
}
