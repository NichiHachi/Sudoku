package sudoku;

import java.util.HashMap;

public class Rule {
    private HashMap<Element, Element[]> rulesMap;

    public Rule(HashMap<Element, Element[]> rulesMap) {
        this.rulesMap = rulesMap;
    }

    public HashMap<Element, Element[]> getRulesMap() {
        return rulesMap;
    }

    public void setRulesMap(HashMap<Element, Element[]> rulesMap) {
        this.rulesMap = rulesMap;
    }
}
