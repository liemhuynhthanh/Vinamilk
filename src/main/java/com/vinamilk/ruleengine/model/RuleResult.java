package com.vinamilk.ruleengine.model;


public class RuleResult {

    private String ruleId;
    private boolean passed;
    private String message;

    public RuleResult() {}

    public RuleResult(String ruleId, boolean passed, String message) {
        this.ruleId  = ruleId;
        this.passed  = passed;
        this.message = message;
    }

    public String getRuleId()  { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    public boolean isPassed()  { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "{ ruleId: " + ruleId
            + ", passed: " + passed
            + (message != null ? ", message: \"" + message + "\"" : "")
            + " }";
    }
}
