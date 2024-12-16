package sudoku;

public class Constraint {
    private Zone zone;
    private Rule rule;

    public Constraint(Zone zone, Rule rule) {
        this.zone = zone;
        this.rule = rule;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
