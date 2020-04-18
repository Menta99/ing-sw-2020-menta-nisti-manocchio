package Model.Godcards;

import Model.Box;
import Model.Worker;

import java.util.ArrayList;

public class Hestia extends GodCard {

    public Hestia(){
        this.setName("Hestia");
        this.setPower("You can build twice, but the second building must not be on a Border position");
        setActivePower(true);
    }

    /**
     * Implementa la funzione di Hestia
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
        if(getOwner().getSelectedWorker()!=null){
            if (getOwner().getSelectedWorker().isDidBuild()){
                return true;
            }
        }
        return false;
    }

    /**
     * Se ha già costruito rimuove le caselle perimetrali
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


