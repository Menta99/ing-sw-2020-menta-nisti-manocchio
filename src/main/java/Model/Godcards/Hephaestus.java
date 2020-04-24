package Model.Godcards;

import Model.Box;
import Model.Game;
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
            getOwner().getSelectedWorker().Build(getOwner().getSelectedWorker().getLastBuilding());
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
     * Se ha già costruito dà la possibilità di costruire nuovamente, a meno che non si voglia costruire un DOME
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> adjacentBoxes){
        ArrayList<Box> toRemove = new ArrayList<>();
        if(!getOwner().getSelectedWorker().isDidBuild()){ //Costruisce nuovamente nella stessa Box a meno che non ci sia la possibilità di creare un DOME
            for (Box box : adjacentBoxes){
                if (box.getUpperLevel().getValue()>1){
                    toRemove.add(box);
                }
            }
            adjacentBoxes.removeAll(toRemove);
        }
        else{
            adjacentBoxes.removeAll(adjacentBoxes);
            adjacentBoxes.add(getOwner().getSelectedWorker().getLastBuilding());
        }
        return adjacentBoxes;
    }
}
