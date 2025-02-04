package solvers.wfc;

import sudoku.Position;

import java.util.HashSet;
import java.util.Set;

public class Entropy {
    private int entropy;
    private Set<Position> positionCells;

    public Entropy(){
        this.entropy = (int) Double.POSITIVE_INFINITY;
        this.positionCells = new HashSet<>();
    }

    public Entropy(int entropy, Position position){
        this();
        this.entropy = entropy;
        this.positionCells.add(position);
    }

    public void addCell(int cellEntropy, Position positionCell){
        if(cellEntropy == this.entropy){
            this.positionCells.add(positionCell);
        } else if (cellEntropy < this.entropy){
            this.entropy = cellEntropy;
            this.positionCells = new HashSet<>();
            this.positionCells.add(positionCell);
        }
    }

    public int getEntropy(){
        return this.entropy;
    }

    public Set<Position> getPositionCells(){
        return this.positionCells;
    }

    public void merge(Entropy cellEntropy){
        if (this.entropy > cellEntropy.getEntropy()){
            this.entropy = cellEntropy.getEntropy();
            this.positionCells = cellEntropy.getPositionCells();
        } else if(this.entropy == cellEntropy.getEntropy()) {
            this.positionCells.addAll(cellEntropy.getPositionCells());
        }
    }
}
