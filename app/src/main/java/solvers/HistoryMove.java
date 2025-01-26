package solvers;

import sudoku.Position;

import java.util.HashSet;
import java.util.Set;

public class HistoryMove {
    public Set<String> values;
    public Position position;
    public boolean isComplete;

    public HistoryMove() {
        this.values = new HashSet<>();
        this.isComplete = false;
    }

    public HistoryMove(Set<String> values, Position position){
        this();
        this.values = values;
        this.position = position;
    }

    public HistoryMove(String value, Position position){
        this();
        this.values.add(value);
        this.position = position;
    }

    public void add(String value){
        this.values.add(value);
    }

    public boolean equals(Set<String> values){
        return this.values.equals(values);
    }

    public void completed(){
        this.isComplete = true;
    }
}
