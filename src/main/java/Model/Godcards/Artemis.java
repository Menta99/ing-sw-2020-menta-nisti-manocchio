package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Worker;

import java.util.ArrayList;

public class Artemis extends GodCard {

    public Artemis(){
        this.setName("Artemis");
        this.setPower("Your worker can move twice a turn, but can't go back to the first position");
        setActivePower(true);
    }

    /**
     * Indicatore di modifiche nella fase di movimento
     * @return
     */
    @Override
    public boolean myMovement() {
        return true;
    }

    /**
     * Se il worker si muove nuovamente non può tornare nella casella iniziale
     * @param adjacentBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialMovement(ArrayList <Box> adjacentBoxes){
        if (getOwner().getSelectedWorker()!=null) {
            if (getOwner().getSelectedWorker().isMoved()) {
                adjacentBoxes.remove(getOwner().getSelectedWorker().getLastPosition());
            }
        }
        return adjacentBoxes;
    }

    /**
     * Implementa il potere speciale di Artemide
     * @return
     */
    @Override
    public boolean activeSubroutine(){
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
        if (getOwner().getSelectedWorker().CanMove()){ //Secondo movimento, se non riesce non perde
            Game.getInstance().getController().MovePhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
            //getOwner().movePhase();
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
        }
        else {
            Game.getInstance().getController().Lose(getOwner());
            return false;
            //getOwner().lose();
        }
        return true;
    }

}
