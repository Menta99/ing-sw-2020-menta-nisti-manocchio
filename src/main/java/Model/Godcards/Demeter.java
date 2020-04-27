package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Demeter
 */
public class Demeter extends GodCard {

    /**
     * God's constructor
     */
    public Demeter(){
        this.setName("Demeter");
        this.setPower("You can build twice, but not on the same position");
        setActivePower(true);
    }

    /**
     * Implement Demeter's function
     * @return true or false
     */
    @Override
    public boolean activeSubroutine() {
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
            //getOwner().selectWorkerPhase();
            //getOwner().movePhase();
            canDoSomething = false;
        }
        else {
            Game.getInstance().getController().Lose(getOwner());
            return false;
            //getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
            //getOwner().buildPhase();
        }
        else {
            Game.getInstance().getController().Lose(getOwner());
            return false;
            //getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanBuild()){ //Costruisce una seconda volta
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
            //getOwner().buildPhase();
        }
        else {
            Game.getInstance().getController().Lose(getOwner());
            return false;
            //getOwner().lose();
        }
        return true;
    }


    @Override
    public boolean myBuild(){
        if(getOwner().getSelectedWorker()==null){
            return false;
        }
        if (getOwner().getSelectedWorker().isDidBuild()) {
            return true;
        }
        return false;
    }

    /**
     * If built, remove the box where did build
     * @param adjacentBoxes
     * @return adjacentBoxes without box where he built
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        if(getOwner().getSelectedWorker().isDidBuild()){
            adjacentBoxes.remove(getOwner().getSelectedWorker().getLastBuilding());
        }
        return adjacentBoxes;
    }
}
