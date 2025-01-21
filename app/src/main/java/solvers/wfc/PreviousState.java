package solvers.wfc;

import sudoku.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PreviousState {
    public Position position;
    public String value;
    public ArrayList<Set<String>> rules;

    public PreviousState(Position position, String value, ArrayList<Set<String>> rules){
        this.position = position;
        this.value = value;
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "PreviousState{" +
                "position=" + position +
                ", value='" + value + '\'' +
                ", rules=" + rules +
                '}';
    }
}
