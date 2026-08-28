package com.vinamilk.ruleengine.model;

import java.util.List;

public class EvaluationResult {

    private boolean passed;
    private List<RuleResult> results;

    public EvaluationResult() {}

    public EvaluationResult(boolean passed, List<RuleResult> results) {
        this.passed  = passed;
        this.results = results;
    }

    public boolean isPassed()             { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public List<RuleResult> getResults()             { return results; }
    public void setResults(List<RuleResult> results) { this.results = results; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ passed: ").append(passed).append(", results: [\n");
        for (RuleResult r : results) {
            sb.append("  ").append(r).append("\n");
        }
        sb.append("]}");
        return sb.toString();
    }
}
