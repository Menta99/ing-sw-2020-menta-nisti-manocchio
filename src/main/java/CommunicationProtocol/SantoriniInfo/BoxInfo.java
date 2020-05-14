package CommunicationProtocol.SantoriniInfo;

import Model.PawnType;
import View.Colors;

import java.io.Serializable;

/**
 * Immutable Class containing box information
 */
public class BoxInfo implements Serializable {
    private final int height;
    private final PawnType lastBuilding;
    private final Colors workerColor;
    private final Colors boxColor;

    /**
     * Constructor of the class
     * @param height the size of the box
     * @param lastBuilding the last building in the box
     * @param workerColor the color of the worker, if present
     * @param boxColor the color of the box, for the different phases
     */
    public BoxInfo(int height, PawnType lastBuilding, Colors workerColor, Colors boxColor){
        this.height = height;
        this.lastBuilding = lastBuilding;
        this.workerColor = workerColor;
        this.boxColor = boxColor;
    }

    public int getHeight() {
        return height;
    }

    public PawnType getLastBuilding() {
        return lastBuilding;
    }

    public Colors getWorkerColor() {
        return workerColor;
    }

    public Colors getBoxColor() {
        return boxColor;
    }
}
