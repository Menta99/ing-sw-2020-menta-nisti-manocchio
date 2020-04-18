package Model.Godcards;

import Model.Box;
import Model.PlayGround;
import Model.Worker;

import java.util.ArrayList;

public class Prometeo extends GodCard {

    public Prometeo(){
        this.setName("Prometeo");
        this.setPower("You can build before and after moving, but, if you do so, you can't climb");
        setActivePower(true);
    }

    @Override
    public boolean activeSubroutine(){
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
        canDoSomething = canDoSomething || worker.CanBuild();
        }
        if (canDoSomething) {
            Worker candidate;
            while(getOwner().getSelectedWorker()==null) {  //Select Phase with condition canBuild instead of canMove
                candidate = getOwner().getController().askForWorker();
                if (candidate.CanBuild()) {
                    getOwner().setSelectedWorker(candidate);
                }
            }
            getOwner().buildPhase();
            canDoSomething = false;
        }
        else {
            getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanMove()){
            getOwner().movePhase();
        }
        else{
            getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            getOwner().lose();
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
