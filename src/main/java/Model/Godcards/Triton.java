package Model.Godcards;

import Model.Game;
import Model.Worker;

import java.util.Scanner;

public class Triton extends GodCard {

    public Triton(){
        this.setName("Triton");
        this.setPower("You can move once more every time you end up your movement in a Border position");
        setActivePower(true);
    }
    /**
     * Implementa il potere speciale di Triton
     * @return
     */
    @Override
    public boolean activeSubroutine(){
        boolean powerUse=false;
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

        do {
            powerUse = false;
            if (getOwner().getSelectedWorker().getPosition().isBorder()) {
                powerUse = Game.getInstance().getController().VirtualAskPower(getOwner().isView());
                //powerUse = getOwner().getController().askForPower(getOwner().isView());
                if (powerUse){
                    Game.getInstance().getController().MovePhase(getOwner());
                    if(!Game.getInstance().getController().getActive().get()){
                        return false;
                    }
                    //getOwner().movePhase();
                }
            }
        }while (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder() && powerUse);

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
