package Model;

import java.util.ArrayList;

/**
 * Class representing a game's worker
 */
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
     * Moves a worker if it is possible
     * @param dest box where to move
     */
    public void Move(Box dest){
        if (LegalMovement(dest)){
            if(owner.getCard().canMoveOthers(dest) && dest.isOccupied()){
                owner.getCard().moveOthers(dest);
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
            if(((this.lastPosition.getStructure().size()<4 && getPosition().getStructure().size()>4) || owner.getCard().myVictoryCondition())){
                boolean enemyWinCondition = false;
                for (Player enemy : Game.getInstance().getPlayer()) {
                    if (!enemy.equals(owner) && !enemy.isLoser()) {
                        enemyWinCondition = enemyWinCondition || enemy.getCard().enemyVictoryCondition(dest);
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
        Worker selected = owner.getSelectedWorker();
        owner.setSelectedWorker(this);
        ArrayList <Box> adjacentBoxes = this.getPosition().BorderBoxes();
        if (owner.getCard().myMovement()) {
            adjacentBoxes = owner.getCard().specialMovement(adjacentBoxes);
        }
        for (Player enemy : Game.getInstance().getPlayer()) {
            if (!enemy.equals(owner) && !enemy.isLoser()) {
                if (enemy.getCard().enemyMovement()) {
                    adjacentBoxes = enemy.getCard().specialMovement(adjacentBoxes);
                }
            }
        }
        for (Box box : adjacentBoxes){
            if (this.LegalMovement(box)) {
                owner.setSelectedWorker(selected);
                return true;
            }
        }
        owner.setSelectedWorker(selected);
        return false;
    }

    /**
     * Checks if any Legal Building action is possible
     * @return true or false if is possible to build
     */
    public boolean CanBuild(){
        ArrayList <Box> adjacentBoxes = this.getPosition().BorderBoxes();
        if (owner.getCard().myBuild()){
            adjacentBoxes = owner.getCard().specialBuilding(adjacentBoxes);
        }
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
            if (!enemy.equals(owner) && !enemy.isLoser()) {
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

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner){
        this.owner=owner;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isDidBuild() {
        return didBuild;
    }

    public void setDidBuild(boolean didBuild) {
        this.didBuild = didBuild;
    }

    public boolean getDidClimb() {
        return didClimb;
    }

    public void setDidClimb(boolean didClimb) {
        this.didClimb = didClimb;
    }

    public Box getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Box lastPos){
        lastPosition = lastPos;
    }

    public Box getLastBuilding() {
        return lastBuilding;
    }

    public void setState(boolean state){
        this.state=state;
        if (!state) {
            this.owner.getWorkers().remove(this);
        }
    }

    public boolean isState() {
        return state;
    }
}
