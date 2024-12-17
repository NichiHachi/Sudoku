package sudoku;

import java.util.HashMap;

public class Rule {
    private HashMap<String, String[]> rulesMap;

    public Rule(HashMap<String, String[]> rulesMap) {
        this.rulesMap = rulesMap;
    }

    public HashMap<String, String[]> getRulesMap() {
        return rulesMap;
    }

    public void setRulesMap(HashMap<String, String[]> rulesMap) {
        this.rulesMap = rulesMap;
    }
}
