package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.PlayGround;
import Model.Worker;

import java.util.ArrayList;

/**
 * Class of GodCard Prometeo
 */
public class Prometeo extends GodCard {

    /**
     * Constructor of the god
     */
    public Prometeo(){
        this.setName("Prometeo");
        this.setPower("You can build before and after moving, but, if you do so, you can't climb");
        setActivePower(true);
    }

    /**
     * Implement Prometeo's function
     * @return true or false
     */
    @Override
    public boolean activeSubroutine(){
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
        canDoSomething = canDoSomething || worker.CanBuild();
        }
        if (canDoSomething) {
            Worker candidate;
            while(getOwner().getSelectedWorker()==null) {  //Select Phase with condition canBuild instead of canMove
                candidate = Game.getInstance().getController().VirtualAskWorker(getOwner().isView());
                //candidate = getOwner().getController().askForWorker(getOwner().isView());
                if (candidate.CanBuild()) {
                    getOwner().setSelectedWorker(candidate);
                }
            }
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
            //getOwner().buildPhase();
            canDoSomething = false;
        }
        else {
            Game.getInstance().getController().Lose(getOwner());
            return false;
            //getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanMove()){
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
            //getOwner().movePhase();
        }
        else{
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
        return true;
    }

    @Override
    public boolean myMovement(){
        if (getOwner().isUsePower()){
            return true;
        }
        return false;
    }

    /**
     * If you use the power you can't climb
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialMovement(ArrayList<Box> adjacentBoxes){
        if (getOwner().getSelectedWorker()==null){
            return adjacentBoxes;
        }
        ArrayList<Box> toRemove = new ArrayList<>();
        for (Box box : adjacentBoxes){
            if (box.getStructure().size()>=getOwner().getSelectedWorker().getPosition().getStructure().size()){
                toRemove.add(box);
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
