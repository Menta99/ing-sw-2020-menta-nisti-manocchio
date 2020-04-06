package Model;

import java.util.ArrayList;

public class Triton extends GodCard {

    public Triton(){
        this.setName("Triton");
    }
    /**
     * Implementa il potere speciale di Triton
     * @return
     */
    @Override
    public boolean activeSubroutine(){
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
        while (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder() && getOwner().isUsePower()){ //Fintantoché sono sul bordo, posso muovermi e voglio muovermi, posso farlo
            getOwner().movePhase();
        }
        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            //player loses
        }
        return true;
    }
}
