package com.vinamilk.ruleengine;

import com.vinamilk.ruleengine.engine.RuleEngine;
import com.vinamilk.ruleengine.model.EvaluationResult;
import com.vinamilk.ruleengine.model.Rule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== RULE ENGINE TEST VIA MAIN ===\n");

        RuleEngine engine = new RuleEngine();

        // TC1: Data hợp lệ
        System.out.println("TC1: Data hợp lệ");
        testCase1(engine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // TC2: Vi phạm rule ERROR
        System.out.println("TC2: Vi phạm rule ERROR");
        testCase2(engine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // TC3: Vi phạm rule WARNING
        System.out.println("TC3: Vi phạm rule WARNING");
        testCase3(engine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // TC4: Operator IN
        System.out.println("TC4: Operator IN");
        testCase4(engine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // TC5: Field không tồn tại
        System.out.println("TC5: Field không tồn tại");
        testCase5(engine);
    }
    private static void testCase1(RuleEngine engine) {
        Map<String, Object> data = new HashMap<>();
        data.put("quantity",     100);
        data.put("channel",      "BigC");
        data.put("customerName", "Nguyen Van A");

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity",     "GT",       0,                                         "ERROR"),
            new Rule("R2", "channel",      "IN",       Arrays.asList("BigC", "CoopMart", "Lazada"), "ERROR"),
            new Rule("R3", "customerName", "NOT_NULL", null,                                       "WARNING")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println(result);
    }

    /**
     * TC2: Rule ERROR fail → block đơn
     */
    private static void testCase2(RuleEngine engine) {
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", -5);
        data.put("channel",  "BigC");

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity", "GT", 0, "ERROR")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println(result);

    }

    /**
     * TC3: Rule WARNING fail → không block, chỉ cảnh báo
     */
    private static void testCase3(RuleEngine engine) {
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", 100);
        data.put("note",     null);

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "quantity", "GT",       0,    "ERROR"),
            new Rule("R2", "note",     "NOT_NULL", null, "WARNING")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println(result);
    }

    /**
     * TC4: Operator IN
     */
    private static void testCase4(RuleEngine engine) {
        List<Rule> rules = Arrays.asList(
            new Rule("R1", "channel", "IN",
                Arrays.asList("BigC", "CoopMart", "Lazada"), "ERROR")
        );

        // Case 1: Giá trị hợp lệ
        System.out.println("  Case 1: channel=Lazada (trong danh sách)");
        Map<String, Object> dataValid = new HashMap<>();
        dataValid.put("channel", "Lazada");
        EvaluationResult r1 = engine.evaluate(dataValid, rules);
        System.out.println("  " + r1);


        System.out.println();

        // Case 2: Giá trị không hợp lệ
        System.out.println("  Case 2: channel=Tiki (không trong danh sách)");
        Map<String, Object> dataInvalid = new HashMap<>();
        dataInvalid.put("channel", "Tiki");
        EvaluationResult r2 = engine.evaluate(dataInvalid, rules);
        System.out.println("  " + r2);

    }

    /**
     * TC5: Field không tồn tại → NOT_NULL fail
     */
    private static void testCase5(RuleEngine engine) {
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", 50);

        List<Rule> rules = Arrays.asList(
            new Rule("R1", "channel", "NOT_NULL", null, "ERROR")
        );

        EvaluationResult result = engine.evaluate(data, rules);
        System.out.println(result);
    }
}
