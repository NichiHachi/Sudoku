package solvers.wfc;

import sudoku.Position;

import java.util.ArrayList;

public class CellsEntropy {
    private int entropy;
    private ArrayList<Position> positionCells;

    public CellsEntropy(){
        this.entropy = (int) Double.POSITIVE_INFINITY;
        this.positionCells = new ArrayList<>();
    }

    public CellsEntropy(int entropy, Position position){
        this();
        this.entropy = entropy;
        this.positionCells.add(position);
    }

    public void addCell(int cellEntropy, Position positionCell){
        if(cellEntropy == this.entropy){
            this.positionCells.add(positionCell);
        } else if (cellEntropy < this.entropy){
            this.entropy = cellEntropy;
            this.positionCells = new ArrayList<>();
            this.positionCells.add(positionCell);
        }
    }

    public int getEntropy(){
        return this.entropy;
    }

    public ArrayList<Position> getPositionCells(){
        return this.positionCells;
    }

    public void merge(CellsEntropy cellEntropy){
        if (this.entropy > cellEntropy.getEntropy()){
            this.entropy = cellEntropy.getEntropy();
            this.positionCells = cellEntropy.getPositionCells();
        } else if(this.entropy == cellEntropy.getEntropy()) {
            this.positionCells.addAll(cellEntropy.getPositionCells());
        }
    }
}
