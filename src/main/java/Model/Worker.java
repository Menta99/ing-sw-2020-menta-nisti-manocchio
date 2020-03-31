package Model;

import java.util.ArrayList;

public class Worker extends Pawn{
    private PawnType type = PawnType.WORKER;
    private Box position;
    private boolean gender;
    private boolean state;
    private boolean moved;
    private boolean didBuild;
    private Player owner;

    /**
     * Moves a worker if able to
     * @param dest box where to move
     */
    public void Move(Box dest){
        if (LegalMovement(dest)){
            if(this.position.getStructure().size()<=4 && dest.getStructure().size()>=4){//Parto da un qualsiasi piano minore del terzo e arrivo in un terzo piano non occupato oppure occupato ma posso spingere l'avversario
                this.owner.setWinner(true);
            }
            position.getStructure().remove(this);
            position = dest;
            position.getStructure().add(this);
            setMoved(true);
        }
    }

    /**
     * Builds a structure if able to
     * @param dest Box where to build
     */
    public void Build(Box dest){
        if (LegalBuild(dest)){
            dest.Build();
            setDidBuild(true);
        }
    }

    /**
     * Checks if any LegalMovement is possible
     * @return true or false if is possible to move
     */
    public boolean CanMove(){
        ArrayList <Box> adjacentBoxes = this.position.BorderBoxes();
        for (Box box : adjacentBoxes){
            if (this.LegalMovement(box)) {return true;}
        }
        return false;
    }

    /**
     * Checks if any Legal Building action is possible
     * @return true or false if is possible to build
     */
    public boolean CanBuild(){
        ArrayList <Box> adjacentBoxes = this.position.BorderBoxes();
        for (Box box : adjacentBoxes){
            if (this.LegalBuild(box)) {return true;}
        }
        return false;
    }


    /**
     * Checks if the worker can Move in the selected box
     * @param destination Box to check
     * @return true or false if the movement is legal
     */
    public boolean LegalMovement(Box destination){
        ArrayList<Box> adjacentBoxes = this.position.BorderBoxes();
        if (adjacentBoxes.contains(destination)){
            if (!destination.isOccupied()/* || GodPower */ && (destination.getStructure().size() - position.getStructure().size()<= 0) && destination.getStructure().get(destination.getStructure().size()-1).getType()!=PawnType.DOME){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the worker can Build in the selected box
     * @param destination Box to check
     * @return true or false if is the building is legal
     */
    public boolean LegalBuild(Box destination){
        ArrayList<Box> adjacentBoxes = this.position.BorderBoxes();
        /* if (GodPower) {adjacentBoxes.add(godpower) TO IMPLEMENT W/ GODCARDS*/
        if(adjacentBoxes.contains(destination)){
            if (!destination.isOccupied()){
                if(destination.getStructure().get(destination.getStructure().size()-1).getType() != (PawnType.DOME)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Setter method, if the worker is removed from the game, remove it from the owners list
     * @param state state of the Worker
     */
    public void setState(boolean state){
        this.state=state;
        if (!state) {
            this.owner.getWorkers().remove(this);
        }
    }

    /**
     * Used in game initialization to set a starting point for the worker
     * @param startingPoint Box where to place the Worker
     * @return true or false if the positioning succeed
     */
    public boolean setInitialPosition(Box startingPoint){
        if (!startingPoint.isOccupied()) {
            position = startingPoint;
            setState(true);
            startingPoint.getStructure().add(this);
            return true;
        }
        return false;
    }

    /**
     * Getter of the Worker's type
     * @return type
     */
    public PawnType getType(){
        return type;
    }

    /**
     * Getter of Worker's position
     * @return position of the Worker
     */
    public Box getPosition(){
        return this.position;
    }


    /**
     * Set a worker's owner
     * @param owner Player that owns the Worker
     */
    public void setOwner(Player owner){
        this.owner=owner;
    }

    /**
     * Getter of moved
     * @return true or false if the worker moved or not
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Setter of moved
     * @param moved true or false if the worker moved or not
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * Getter of didBuild
     * @return true or false if the worker built or not
     */
    public boolean isDidBuild() {
        return didBuild;
    }

    /**
     * Getter of moved
     * @param didBuild true or false if the worker built or not
     */
    public void setDidBuild(boolean didBuild) {
        this.didBuild = didBuild;
    }


}
