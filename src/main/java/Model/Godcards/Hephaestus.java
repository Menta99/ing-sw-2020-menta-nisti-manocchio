package Model.Godcards;

import Model.Box;
import Model.Worker;

import java.util.ArrayList;

public class Hephaestus extends GodCard {

    public Hephaestus(){
        this.setName("Hephaestus");
        this.setPower("You can build again on the same position, but not a Dome");
        setActivePower(true);
    }
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
     * Se ha già costruito dà la possibilità di costruire nuovamente, a meno che non si voglia costruire un DOME
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        if(getOwner().getSelectedWorker().isDidBuild()){
            adjacentBoxes.removeAll(adjacentBoxes);
            if(getOwner().getSelectedWorker().getLastBuilding().getUpperLevel().getValue() < 3 ) //Costruisce nuovamente nella stessa Box a meno che non ci sia la possibilità di creare un DOME
            adjacentBoxes.add(getOwner().getSelectedWorker().getLastBuilding());
        }
        return adjacentBoxes;
    }
}
