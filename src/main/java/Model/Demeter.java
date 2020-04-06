package Model;

import java.util.ArrayList;

public class Demeter extends GodCard {

    public Demeter(){
        this.setName("Demeter");
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
            //player loses
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            //player loses
        }
        if (getOwner().getSelectedWorker().CanBuild()){ //Costruisce una seconda volta
            getOwner().buildPhase();
        }
        return true;
    }

    @Override
    public boolean myBuild(){ return true; }

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
