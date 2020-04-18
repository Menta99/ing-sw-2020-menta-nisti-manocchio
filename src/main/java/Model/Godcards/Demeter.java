package Model.Godcards;

import Model.Box;
import Model.Worker;

import java.util.ArrayList;

public class Demeter extends GodCard {

    public Demeter(){
        this.setName("Demeter");
        this.setPower("You can build twice, but not on the same position");
        setActivePower(true);
    }

    /**
     * Implementa la funzione di Demeter
     * @return
     */
    @Override
    public boolean activeSubroutine() {
        boolean canDoSomething=false;
        for (Worker worker : getOwner().getWorkers()) {
            canDoSomething = canDoSomething || worker.CanMove();
        }
        if (canDoSomething) {
            getOwner().movePhase();
            canDoSomething = false;
        }
        else {
            getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            getOwner().lose();
        }
        if (getOwner().getSelectedWorker().CanBuild()){ //Costruisce una seconda volta
            getOwner().buildPhase();
        }
        else {
            getOwner().lose();
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
     * Se ha già costruito rimuove la casella dove già ha costruito
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        if(getOwner().getSelectedWorker().isDidBuild()){
            adjacentBoxes.remove(getOwner().getSelectedWorker().getLastBuilding());
        }
        return adjacentBoxes;
    }
}
