package Model.Godcards;

import Model.Game;
import Model.Worker;

/**
 * Class of GodCard Triton
 */
public class Triton extends GodCard {

    /**
     * Constructor of the god
     */
    public Triton(){
        this.setName("Triton");
        this.setPower("You can move once more every time you end up your movement in a Border position");
        setActivePower(true);
    }

    /**
     * Implement Triton's power
     * @return true or false
     */
    @Override
    public boolean activeSubroutine(){
        boolean powerUse;
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
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }

        do {
            powerUse = false;
            if (getOwner().getSelectedWorker().getPosition().isBorder()) {
                powerUse = Game.getInstance().getController().VirtualAskPower();
                if (powerUse){
                    Game.getInstance().getController().MovePhase(getOwner());
                    if(!Game.getInstance().getController().getActive().get()){
                        return false;
                    }
                }
            }
        }while (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder() && powerUse);

        if (getOwner().getSelectedWorker().CanBuild()){
            Game.getInstance().getController().BuildPhase(getOwner());
            if(!Game.getInstance().getController().getActive().get()){
                return false;
            }
        }
        else {
            Game.getInstance().getController().PlayerLose(getOwner());
            return false;
        }
        return true;
    }
}
