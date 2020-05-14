package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Artemis
 */
public class Artemis extends GodCard {

    /**
     * Constructor of the god
     */
    public Artemis(){
        this.setName("Artemis");
        this.setPower("Your worker can move twice a turn, but can't go back to the first position");
        setActivePower(true);
    }

    /**
     * Modification marker in the move phase
     * @return true
     */
    @Override
    public boolean myMovement() {
        return true;
    }

    /**
     * If the worker moves twice, can't go back to the initial box
     * @param adjacentBoxes ArrayList of the valid boxes
     * @return adjacentBoxes without the initial one
     */
    @Override
    public ArrayList<Box> specialMovement(ArrayList <Box> adjacentBoxes){
        if (getOwner().getSelectedWorker()!=null) {
            if (getOwner().getSelectedWorker().isMoved()) {
                adjacentBoxes.remove(getOwner().getSelectedWorker().getLastPosition());
            }
        }
        return adjacentBoxes;
    }

    /**
     * Implement Artemis' special power
     * @return true or false
     */
    @Override
    public boolean activeSubroutine(){
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
            canDoSomething = canDoSomething || worker.CanMove();
        }
        if (canDoSomething) {
            Game.getInstance().getController().SelectWorkerPhase(getOwner());
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        if (getOwner().getSelectedWorker().CanMove()){
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        return true;
    }
}
