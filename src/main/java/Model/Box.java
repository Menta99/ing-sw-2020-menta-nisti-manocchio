package Model;

import java.util.ArrayList;

/** Represents the box game */
public class Box {

    private int posX;
    private int posY;
    private ArrayList<Pawn> structure;
    private boolean border;
    private boolean occupied;

    /**
     * Constructor of Box Class
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Box(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.structure = new ArrayList<Pawn>();
        this.structure.add(new Building(PawnType.GROUND_LEVEL, this));
        if ((x == 0) || (x == 4) || (y == 0) || (y == 4)) {
            this.border = true;
        } else {
            this.border = false;
        }
        this.occupied = false;
    }

    /**
     * Getter of posX
     *
     * @return posX the x coordinate
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Getter of posY
     *
     * @return posY the y coordinate
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Getter of structure
     *
     * @return structure the Pawn Array on the Box
     */
    public ArrayList<Pawn> getStructure() {
        return structure;
    }

    /**
     * Getter of occupied
     *
     * @return occupied the boolean that indicates if there is a Worker on the Box
     */
    public boolean isOccupied() {
        return occupied;
    }


    /**
     * Setter of occupied
     *
     * @param occupied
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Getter of border
     *
     * @return border the boolean that indicated if the Box is a Border-Box
     */
    public boolean isBorder() {
        return border;
    }

    /**
     * Getter of the upper Pawn on this Box
     *
     * @return PawnType the upper Pawn on this Box
     * @throws NullPointerException if requested invalid action on a box
     */
    public PawnType getUpperLevel() {
        try {
            return (structure.get(structure.size() - 1).getType());
        } catch (NullPointerException e) {
            System.err.println("Void box");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Says if you can Move or Build on this Box
     *
     * @return true or false
     */
    public boolean Playable() {
        if ((getUpperLevel() == PawnType.DOME) || (getUpperLevel() == PawnType.WORKER))
            return false;
        else
            return true;
    }

    /**
     * Find the neighboring boxes
     *
     * @return an ArrayList of the neighboring boxes of this Box
     * @throws NullPointerException if requested invalid action on a box
     */
    public ArrayList<Box> BorderBoxes() {
        ArrayList<Box> neighbors = new ArrayList<Box>();
        try {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (!(i == 0 && j == 0) && (posX + i > -1) && (posX + i < 5) && (posY + j > -1) && (posY + j < 5)) {
                        neighbors.add(PlayGround.getInstance().getBox(posX + i, posY + j));
                    }
                }
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e2) {
            System.err.println("Index not valid");
            e2.printStackTrace();
        } finally {
            return neighbors;
        }
    }

    /**
     * You build a Dome in a playable box on the current level
     *
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
     *
     * @return true or false if the Building is created or not
     * @throws NullPointerException if you try to build from a non-initialized structure
     */
    public boolean Build() {

        if (isOccupied()) {
            Worker worker = (Worker) structure.remove(structure.size() - 1);
            structure.add(new Building(PawnType.values()[getUpperLevel().getValue() + 1], this));
            structure.add(worker);
            return true;
        }
        else{
            structure.add(new Building(PawnType.values()[getUpperLevel().getValue() + 1], this));
            return true;
        }
    }
}
