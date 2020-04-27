package ComunicationProtocol;

import Model.PawnType;
import View.Colors;

import java.io.Serializable;

/**
 * Class representing a box in an update map
 */
public class BoxInfo implements Serializable {
    private int height;
    private PawnType lastBuilding;
    private Colors workerColor;
    private Colors boxColor;

    /**
     * Constructor of the class
     * @param height
     * @param lastBuilding
     * @param workerColor
     * @param boxColor
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
