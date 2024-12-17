package sudoku;

public class Constraint {
    private Rule rule;
    private final int id;

    public Constraint(Rule rule, int id) {
        this.rule = rule;
        this.id = id;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getId() {
        return id;
    }

}
