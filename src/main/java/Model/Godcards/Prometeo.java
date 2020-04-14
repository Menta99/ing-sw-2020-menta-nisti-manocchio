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
            getOwner().selectWorkerPhase();                 //Da fare per lui!
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
        return true;
    }

    @Override

    public ArrayList<Box> specialMovement(ArrayList<Box> adjacentBoxes){
        for (Box box : adjacentBoxes){
            if (box.getStructure().size()>=getOwner().getSelectedWorker().getPosition().getStructure().size()){
                adjacentBoxes.remove(box);
            }
        }
        return adjacentBoxes;
    }
}
