package Model.Godcards;

import Model.Box;
import Model.Player;

import java.util.ArrayList;

public abstract class GodCard {
    private String name;
    private String power;
    private boolean activePower = false;
    private Player owner;
    private boolean chosen; //indica se la carta è stat scelta per il Game, default false
    private boolean picked; //indica se la carta è stata scelta da un Giocatore, default false

    protected GodCard() {
    }

    /**
     * Setter of Chosen
     * @param state value to assign
     */
    public void setChosen(boolean state) {
        this.chosen = state;
    }

    /**
     * Getter of chosen
     * @return true or false if it has been chosen or not
     */
    public boolean isChosen(){
        return chosen;
    }

    /**
     * Setter of Picked
     * @param state value to assign
     */
    public void setPicked(boolean state){
        this.picked = state;
    }

    /**
     * Getter of picked
     * @return true or false if it has been picked or not
     */
    public boolean isPicked(){
        return picked;
    }

    /**
     * Getter of name
     * @return name of the card
     */
    public String getName(){
        return name;
    }

    /**
     * Setter of Name
     * @param name value to assign
     */
    public void setName(String name){ this.name=name;}

    /**
     * Standard implementetion of a GodCard Functionality
     * @return false
     */
    public boolean myMovement(){return false;}

    /**
     * Standard implementetion of a GodCard Functionality
     * @return false
     */
    public boolean enemyMovement(){return false;}

    /**
     * Standard implementetion of a GodCard Functionality
     * @return false
     */
    public boolean myBuild(){return false;}

    /**
     * Standard implementetion of a GodCard Functionality
     * @return false
     */
    public boolean myVictoryCondition(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return
     */
    public boolean enemyVictoryCondition(Box dest){return false;}

    /**
     * Standard implementetion of a GodCard Functionality
     * @return false
     */
    public boolean activeSubroutine(){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @return
     */
    public boolean canMoveOthers(Box dest){return false;}

    /**
     * Standard implementation of a GodCard Functionality
     * @param dest
     * @return
     */
    public boolean moveOthers(Box dest){return false;}

    /**
     * Setter of Owner
     * @param owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Getter of Owner
     * @return
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Modifica eventualmente celle disponibili al movimento
     * @param legalMovs
     * @return
     */
    public ArrayList<Box> specialMovement(ArrayList<Box> legalMovs){return legalMovs;};

    /**
     * Modifica eventualmente celle disponibili alla costruzione
     * @param legalBuilding
     * @return
     */
    public ArrayList<Box> specialBuilding(ArrayList<Box> legalBuilding){return legalBuilding;};

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
