package Model;

import java.util.ArrayList;

/**
 * Class that represents the box of the playground
 */
public class Box {
    private final int posX;
    private final int posY;
    private final ArrayList<Pawn> structure;
    private boolean border;
    private boolean occupied;

    /**
     * Constructor of Box Class
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Box(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.structure = new ArrayList<>();
        this.structure.add(new Building(PawnType.GROUND_LEVEL, this));
        this.border = (x == 0) || (x == 4) || (y == 0) || (y == 4);
        this.occupied = false;
    }

    /**
     * Getter of the upper Pawn on this Box
     * @return PawnType the upper Pawn on this Box
     */
    public PawnType getUpperLevel() {
        return (structure.get(structure.size() - 1).getType());
    }

    /**
     * Says if you can Move or Build on this Box
     * @return true or false
     */
    public boolean Playable() {
        return (getUpperLevel() != PawnType.DOME) && (getUpperLevel() != PawnType.WORKER);
    }

    /**
     * Find the neighboring boxes
     * @return an ArrayList of the neighboring boxes of this Box
     */
    public ArrayList<Box> BorderBoxes() {
        ArrayList<Box> neighbors = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0) && (posX + i > -1) && (posX + i < 5) && (posY + j > -1) && (posY + j < 5)) {
                    neighbors.add(PlayGround.getInstance().getBox(posX + i, posY + j));
                }
            }
        }
        return neighbors;
    }

    /**
     * You build a Dome in a playable box on the current level
     * @return true or false if the Dome is created or not
     */
    public boolean BuildDome() {
        if (Playable()) {
            structure.add(new Building(PawnType.DOME, this));
            return true;
        } else {
            return false;
        }
    }

    /**
     * You can build a new level in a playable box
     * @return true or false if the Building is created or not
     */
    public boolean Build() {
        if (isOccupied()) {
            Worker worker = (Worker) structure.remove(structure.size() - 1);
            structure.add(new Building(PawnType.values()[getUpperLevel().getValue() + 1], this));
            structure.add(worker);
        }
        else{
            structure.add(new Building(PawnType.values()[getUpperLevel().getValue() + 1], this));
        }
        return true;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public ArrayList<Pawn> getStructure() {
        return structure;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isBorder() {
        return border;
    }
}
