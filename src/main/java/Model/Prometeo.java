package Model;

import java.util.ArrayList;

public class Prometeo extends GodCard {

    public Prometeo(){
        this.setName("Prometeo");
    }

    @Override
    public boolean activeSubroutine(){
            boolean canDoSomething=false;
            for (Worker worker : getOwner().getWorkers()) {
        canDoSomething = canDoSomething || worker.CanBuild();
        }
        if (canDoSomething) {
            getOwner().selectWorker(/*Input da mettere nella view*/null);
            getOwner().buildPhase();
            canDoSomething = false;
        }
        else {
        //player loses
        }
        if (getOwner().getSelectedWorker().CanMove()){ //Secondo movimento, se non riesce non perde
        getOwner().movePhase();
        }
        else{
            //Player loses
        }
        if (getOwner().getSelectedWorker().CanBuild()){
        getOwner().buildPhase();
        }
        else {
        //player loses
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
