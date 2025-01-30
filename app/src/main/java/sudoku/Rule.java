package sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Rule {
    private HashMap<String, Set<String>> rules;
    private boolean printable;

    public Rule(HashMap<String, Set<String>> rulesMap, boolean printable) {
        this.rules = rulesMap;
        this.printable = printable;
    }

    public Rule(String[] rules, boolean printable) {
        this();
        for(String value : rules){
            Set<String> set = new HashSet<>();
            set.add(value);
            this.rules.put(value, set);
        }
        this.printable = printable;
    }

    public Rule(String[] rules){
        this(rules, true);
    }

    public Rule(){
        this.rules = new HashMap<>();
        this.printable = true;
    }

    public Set<String> get(String value){
        if (!this.isValid(value)){
            System.err.println("[Rule] The value isn't inside of the rules!");
        }
        return rules.get(value);
    }

    public HashMap<String, Set<String>> getRules() {
        return rules;
    }

    public Set<String> getPossibleMove(){
        return this.rules.keySet();
    }

    public void add(String key, Set<String> value){
        rules.put(key, value);
    }

    public void addRule(Rule rule){
        for(String key : rule.getPossibleMove()){
            this.add(key, rule.get(key));
        }
    }

    public void mergingRule(Rule rule){
        for(String key : rule.getPossibleMove()){
            if(this.isValid(key)) {
                this.rules.get(key).addAll(rule.get(key));
            } else {
                this.add(key, rule.get(key));
            }
        }
    }

    public boolean isValid(String value){
        return rules.containsKey(value);
    }

    public void handleInsertValue(String value){
        if (!this.isValid(value)){
            return;
        }

        for(String key : this.rules.get(value)){
            this.removeRule(key);
        }
    }

    public void removeRule(String value){
        if (!this.isValid(value)){
            System.err.println("[Rule] The value isn't inside of the rules!");
        }
        rules.remove(value);
    }

    public int size(){
        return rules.size();
    }
}
