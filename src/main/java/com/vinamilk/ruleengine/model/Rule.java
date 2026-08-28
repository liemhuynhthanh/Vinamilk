package com.vinamilk.ruleengine.model;

public class Rule {

    private String ruleId;
    private String field;
    private String operator;
    private Object value;
    private String severity;

    public Rule() {}

    public Rule(String ruleId, String field, String operator, Object value, String severity) {
        this.ruleId   = ruleId;
        this.field    = field;
        this.operator = operator;
        this.value    = value;
        this.severity = severity;
    }

    public String getRuleId()   { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    public String getField()    { return field; }
    public void setField(String field) { this.field = field; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Object getValue()    { return value; }
    public void setValue(Object value) { this.value = value; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

}
