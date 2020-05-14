package Model.Godcards;

import Model.Box;
import Model.Player;

import java.util.ArrayList;

/**
 * Abstract class representing a God
 */
public abstract class GodCard {
    private String name;
    private String power;
    private boolean activePower = false;
    private Player owner;
    private boolean chosen;
    private boolean picked;

    protected GodCard() {
    }

    public void setChosen(boolean state) {
        this.chosen = state;
    }

    public boolean isChosen(){
        return chosen;
    }

    public void setPicked(boolean state){
        this.picked = state;
    }

    public boolean isPicked(){
        return picked;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){ this.name=name;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean myMovement(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean enemyMovement(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean myBuild(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean myVictoryCondition(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean enemyVictoryCondition(Box dest){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean activeSubroutine(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return false
     */
    public boolean canMoveOthers(Box dest){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @param dest destination box
     * @return false
     */
    public boolean moveOthers(Box dest){return false;}

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Modifies the cells available for the movement
     * @param legalMoves ArrayList of valid boxes
     * @return Arraylist of updated boxes
     */
    public ArrayList<Box> specialMovement(ArrayList<Box> legalMoves){return legalMoves;}

    /**
     * Modifies the cells available for the building
     * @param legalBuilding ArrayList of valid boxes
     * @return ArrayList of updated boxes
     */
    public ArrayList<Box> specialBuilding(ArrayList<Box> legalBuilding){return legalBuilding;}

    public void setActivePower(boolean activePower) {
        this.activePower = activePower;
    }

    public boolean isActivePower() {
        return activePower;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
