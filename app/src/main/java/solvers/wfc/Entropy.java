package solvers.wfc;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.Position;
import utils.Colors;

public class Entropy {

    private static final Logger logger = LoggerFactory.getLogger(Entropy.class);
    private int entropy;
    private Set<Position> positionCells;

    public Entropy() {
        this.entropy = (int) Double.POSITIVE_INFINITY;
        this.positionCells = new HashSet<>();
    }

    public Entropy(int entropy, Position position) {
        this();
        this.entropy = entropy;
        this.positionCells.add(position);
        logger.debug(
            Colors.DEBUG_COLOR +
            "Created new Entropy with value {} at position {}" +
            Colors.RESET,
            entropy,
            position
        );
    }

    public void addCell(int cellEntropy, Position positionCell) {
        if (cellEntropy == this.entropy) {
            this.positionCells.add(positionCell);
            logger.debug(
                Colors.DEBUG_COLOR +
                "Added cell at position {} with matching entropy {}" +
                Colors.RESET,
                positionCell,
                cellEntropy
            );
        } else if (cellEntropy < this.entropy) {
            this.entropy = cellEntropy;
            this.positionCells = new HashSet<>();
            this.positionCells.add(positionCell);
            logger.debug(
                Colors.DEBUG_COLOR +
                "Reset entropy to {} with new position {}" +
                Colors.RESET,
                cellEntropy,
                positionCell
            );
        }
    }

    public int getEntropy() {
        return this.entropy;
    }

    public Set<Position> getPositionCells() {
        return this.positionCells;
    }

    public void merge(Entropy cellEntropy) {
        if (this.entropy > cellEntropy.getEntropy()) {
            this.entropy = cellEntropy.getEntropy();
            this.positionCells = cellEntropy.getPositionCells();
            logger.debug(
                Colors.DEBUG_COLOR +
                "Merged entropy: updated to lower value {}" +
                Colors.RESET,
                this.entropy
            );
        } else if (this.entropy == cellEntropy.getEntropy()) {
            this.positionCells.addAll(cellEntropy.getPositionCells());
            logger.debug(
                Colors.DEBUG_COLOR +
                "Merged entropy: added positions for equal entropy {}" +
                Colors.RESET,
                this.entropy
            );
        }
    }
}
