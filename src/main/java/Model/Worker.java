package Model;

import java.util.ArrayList;

public class Worker extends Pawn{
    private boolean state;
    private boolean moved;
    private boolean didBuild;
    private boolean didClimb;
    private Player owner;
    private Box lastPosition;
    private Box lastBuilding;


    /**
     * Worker Constructor
     */
    public Worker(){
        this.setType(PawnType.WORKER);
        moved=false;
        didBuild=false;
        didClimb=false;
        state=false;
    }

    /**
     * Moves a worker if able to
     * @param dest box where to move
     */
    public void Move(Box dest){
        if (LegalMovement(dest)){
            if(owner.getCard().canMoveOthers(dest) && dest.isOccupied()){
                owner.getCard().moveOthers(dest); //IMPLEMENTA FUNZIONALITA DELLE DIVINITA CHE POSSONO MUOVERE I NEMICI
                return;
            }
            if (dest.getStructure().size()>=getPosition().getStructure().size()){
                didClimb=true;
            }
            lastPosition=getPosition();
            getPosition().getStructure().remove(this);
            getPosition().setOccupied(false);
            setPosition(dest);
            getPosition().getStructure().add(this);
            dest.setOccupied(true);
            setMoved(true);
            if(((this.lastPosition.getStructure().size()<=4 && getPosition().getStructure().size()>=4) || owner.getCard().myVictoryCondition())){//Parto da un qualsiasi piano minore del terzo e arrivo in un terzo piano non occupato oppure occupato ma posso spingere l'avversario
                boolean enemyWinCondition = false;
                for (Player enemy : Game.getInstance().getPlayer()) {
                    if (!enemy.equals(owner)) {
                        enemyWinCondition = enemyWinCondition || enemy.getCard().enemyVictoryCondition(dest); //CHECKS RESTRIZIONI GODS NEMICI
                        if(!enemyWinCondition){
                            this.owner.setWinner(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Builds a structure if able to
     * @param dest Box where to build
     */
    public void Build(Box dest){
        if (LegalBuild(dest)){
            lastBuilding=dest;
            dest.Build();
            setDidBuild(true);
        }
    }

    /**
     * Builds a Dome
     */
    public void BuildDome(Box dest){
        if(LegalBuild(dest)){
            lastBuilding=dest;
            dest.BuildDome();
            setDidBuild(true);
        }
    }

    /**
     * Checks if any LegalMovement is possible
     * @return true or false if is possible to move
     */
    public boolean CanMove(){
        ArrayList <Box> adjacentBoxes = this.getPosition().BorderBoxes();
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
        ArrayList <Box> adjacentBoxes = this.getPosition().BorderBoxes();
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
    public boolean LegalMovement(Box destination) {
        ArrayList<Box> adjacentBoxes = this.getPosition().BorderBoxes();
        if (owner.getCard().myMovement()) {
            adjacentBoxes = owner.getCard().specialMovement(adjacentBoxes);
        }
        for (Player enemy : Game.getInstance().getPlayer()) {
            if (!enemy.equals(owner)) {
                if (enemy.getCard().enemyMovement()) {
                    adjacentBoxes = enemy.getCard().specialMovement(adjacentBoxes);
                }
            }
        }
        if (adjacentBoxes.contains(destination)) {
            if (!destination.isOccupied() && ((destination.getStructure().size() - getPosition().getStructure().size()) <= 0) && (destination.getUpperLevel() != PawnType.DOME)) {
                return true;
            }
            if (destination.isOccupied()) {
                boolean enemyPiece = true;
                for (Worker worker : owner.getWorkers()) {
                    if (worker.getPosition().equals(destination)) {
                        enemyPiece = false;
                    }
                }
                if (owner.getCard().canMoveOthers(destination) && enemyPiece && ((destination.getStructure().size() - getPosition().getStructure().size()) <= 1)) {
                    return true;
                }
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
        ArrayList<Box> adjacentBoxes = this.getPosition().BorderBoxes();
        if (owner.getCard().myBuild()){
            adjacentBoxes = owner.getCard().specialBuilding(adjacentBoxes);
        }
        if(adjacentBoxes.contains(destination)){
            if (destination.equals(getPosition())){
                return true;
            }
            if (!destination.isOccupied()){
                if(destination.getUpperLevel() != (PawnType.DOME)){
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
            setPosition(startingPoint);
            setState(true);
            startingPoint.getStructure().add(this);
            startingPoint.setOccupied(true);
            return true;
        }
        return false;
    }

    /**
     * Getter of the worker's Owner
     * @return
     */
    public Player getOwner() {
        return owner;
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

    /**
     * Returns true if the worker climbed
     * @return
     */
    public boolean getDidClimb() {
        return didClimb;
    }

    /**
     * Sets true if the worker climbed
     * @param didClimb
     */
    public void setDidClimb(boolean didClimb) {
        this.didClimb = didClimb;
    }

    /**
     * Returns last worker position
     * @return
     */
    public Box getLastPosition() {
        return lastPosition;
    }

    /**
     * Sets last worker's position
     * @param lastPos
     */
    public void setLastPosition(Box lastPos){
        lastPosition = lastPos;
    }

    /**
     * Getter of the worker's last building position
     * @return
     */
    public Box getLastBuilding() {
        return lastBuilding;
    }
}
