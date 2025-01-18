package sudoku;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Cell {
    private String value;
    private ArrayList<Integer> idRules;

    public Cell(ArrayList<Integer> idRules) {
        this.idRules = idRules;
    }

    public Cell(Integer idRule){
        this.idRules = new ArrayList<>(idRule);
    }

    public Cell() {
        this.idRules = new ArrayList<>();
    }

    public void addRule(Integer idRule) {
        this.idRules.add(idRule);
    }

    // Maybe we will never use it
    public void deleteRule(Integer idRule) {
        this.idRules.remove(idRule);
    }

    public void placeValue(String value) {
        if (this.value != null) {
            System.err.println("Cell: A value is already inside of the grid (" + this.value + ")");
        }
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public ArrayList<Integer> getIdRules() {
        return this.idRules;
    }

    public int getNumberOfRules(){
        return this.idRules.size();
    }
}
