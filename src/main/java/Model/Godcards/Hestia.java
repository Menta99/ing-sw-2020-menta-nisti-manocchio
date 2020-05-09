package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Hestia
 */
public class Hestia extends GodCard {

    public Hestia(){
        this.setName("Hestia");
        this.setPower("You can build twice, but the second building must not be on a Border position");
        setActivePower(true);
    }

    /**
     * Implement Hestia's power
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
            Game.getInstance().getController().PlayerLose(getOwner());
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
            Game.getInstance().getController().PlayerLose(getOwner());
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
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
            //getOwner().lose();
        }
        return true;
    }

    @Override
    public boolean myBuild(){
        if(getOwner().getSelectedWorker()!=null){
            if (getOwner().getSelectedWorker().isDidBuild()){
                return true;
            }
        }
        return false;
    }

    /**
     * If built remove the border boxes
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        ArrayList<Box> toRemove = new ArrayList<>();
        if (getOwner().getSelectedWorker().isDidBuild()){
            for(Box box : adjacentBoxes){
                if(box.isBorder()) {
                    toRemove.add(box);
                }
            }
        }
        for (Box box : toRemove){
            if (adjacentBoxes.contains(box)){
                adjacentBoxes.remove(box);
            }
        }
        return adjacentBoxes;
    }
}


