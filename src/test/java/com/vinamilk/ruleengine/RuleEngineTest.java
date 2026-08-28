package com.vinamilk.ruleengine;

import com.vinamilk.ruleengine.engine.RuleEngine;
import com.vinamilk.ruleengine.model.EvaluationResult;
import com.vinamilk.ruleengine.model.Rule;
import com.vinamilk.ruleengine.model.RuleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 *  Test 5 cases theo đề bài Vinamilk Graduate Talent Program 2026
 * ============================================================
 */
class RuleEngineTest {

    private RuleEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RuleEngine();
    }

    // ---------------------------------------------------------------
    // TC1: Data hợp lệ → tất cả rules pass
    // ---------------------------------------------------------------
    @Test
    @DisplayName("TC1: Data hợp lệ → passed=true, tất cả rules pass")
    void tc1_allRulesPass() {
        // Data
        Map<String, Object> data = new HashMap<>();
        data.put("quantity",     100);
        data.put("channel",      "BigC");
        data.put("customerName", "Nguyen Van A");

        // Rules
        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity",     "GT",       0,                                         "ERROR"),
            new Rule("R2", "channel",      "IN",       Arrays.asList("BigC", "CoopMart", "Lazada"), "ERROR"),
            new Rule("R3", "customerName", "NOT_NULL", null,                                       "WARNING")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println("TC1: " + result);

        assertTrue(result.isPassed(), "passed phải là true");
        result.getResults().forEach(r ->
            assertTrue(r.isPassed(), "Rule " + r.getRuleId() + " phải pass")
        );
    }

    // ---------------------------------------------------------------
    // TC2: Vi phạm 1 rule ERROR → passed = false
    // ---------------------------------------------------------------
    @Test
    @DisplayName("TC2: Vi phạm rule ERROR → passed=false (đơn bị block)")
    void tc2_errorRuleBlocks() {
        // Data: quantity âm → vi phạm rule GT 0
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", -5);
        data.put("channel",  "BigC");

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity", "GT", 0, "ERROR")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println("TC2: " + result);

        assertFalse(result.isPassed(),                      "passed phải là false");
        assertFalse(result.getResults().get(0).isPassed(),  "R1 phải fail");
        assertEquals(
            "quantity must be greater than 0, got -5",
            result.getResults().get(0).getMessage()
        );
    }

    // ---------------------------------------------------------------
    // TC3: Vi phạm 1 rule WARNING → passed = true, có cảnh báo
    // ---------------------------------------------------------------
    @Test
    @DisplayName("TC3: Vi phạm rule WARNING → passed=true nhưng có cảnh báo")
    void tc3_warningDoesNotBlock() {
        // Data: note = null → vi phạm NOT_NULL nhưng chỉ WARNING
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", 100);
        data.put("note",     null);

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity", "GT",       0,    "ERROR"),
            new Rule("R2", "note",     "NOT_NULL", null, "WARNING")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println("TC3: " + result);

        assertTrue(result.isPassed(),                       "WARNING không block → passed=true");
        assertTrue(result.getResults().get(0).isPassed(),   "R1 (ERROR) phải pass");
        assertFalse(result.getResults().get(1).isPassed(),  "R2 (WARNING) phải fail");
        assertEquals("note must not be null", result.getResults().get(1).getMessage());
    }

    // ---------------------------------------------------------------
    // TC4: Operator IN — value nằm trong danh sách cho phép
    // ---------------------------------------------------------------
    @Test
    @DisplayName("TC4: Operator IN — giá trị hợp lệ/không hợp lệ trong danh sách")
    void tc4_operatorIn() {
        List<Rule> rules = Arrays.asList(
            new Rule("R1", "channel", "IN",
                Arrays.asList("BigC", "CoopMart", "Lazada"), "ERROR")
        );

        // Hợp lệ: Lazada có trong danh sách
        Map<String, Object> dataValid = new HashMap<>();
        dataValid.put("channel", "Lazada");
        EvaluationResult r1 = engine.evaluate(dataValid, rules);
        System.out.println("TC4 (valid): " + r1);
        assertTrue(r1.isPassed(), "Lazada hợp lệ → passed=true");

        // Không hợp lệ: Tiki không có trong danh sách
        Map<String, Object> dataInvalid = new HashMap<>();
        dataInvalid.put("channel", "Tiki");
        EvaluationResult r2 = engine.evaluate(dataInvalid, rules);
        System.out.println("TC4 (invalid): " + r2);
        assertFalse(r2.isPassed(), "Tiki không hợp lệ → passed=false");
    }

    // ---------------------------------------------------------------
    // TC5: Field không tồn tại trên data → NOT_NULL fail
    // ---------------------------------------------------------------
    @Test
    @DisplayName("TC5: Field không tồn tại trên data → NOT_NULL fail")
    void tc5_missingFieldFailsNotNull() {
        // Data không có field "channel"
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", 50);

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "channel", "NOT_NULL", null, "ERROR")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println("TC5: " + result);

        assertFalse(result.isPassed(),                     "Field thiếu → NOT_NULL fail → passed=false");
        assertFalse(result.getResults().get(0).isPassed(), "R1 phải fail");
        assertEquals("channel must not be null", result.getResults().get(0).getMessage());
    }

    @Test
    @DisplayName("TC6: Các evaluator EQ và LT hoạt động độc lập")
    void tc6_equalsAndLessThanOperators() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "ACTIVE");
        data.put("price", 25);

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "status", "EQ", "ACTIVE", "ERROR"),
            new Rule("R2", "price", "LT", 30, "ERROR")
        );

        EvaluationResult result = engine.evaluate(data, rules);

        assertTrue(result.isPassed());
        result.getResults().forEach(ruleResult -> assertTrue(ruleResult.isPassed()));
    }
}
