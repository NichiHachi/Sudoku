package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Rule {
    private HashMap<String, Set<String>> rules;

    public Rule(HashMap<String, Set<String>> rulesMap) {
        this.rules = rulesMap;
    }

    public Rule(String[] rules){
        HashMap<String, Set<String>> ruleMap = new HashMap<>();
        for(String value : rules){
            Set<String> set = new HashSet<>();
            set.add(value);
            ruleMap.put(value, set);
        }

        this.rules = ruleMap;
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

    private boolean isValid(String key){
        return rules.containsKey(key);
    }

    public boolean placeValue(String value){
        if (!this.isValid(value)){
            System.err.println("[Rule] The value can't be placed inside of the grid!");
            return false;
        }

        for(String key : this.rules.get(value)){
            this.removeRule(key);
        }
        return true;
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
