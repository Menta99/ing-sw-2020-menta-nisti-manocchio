package Model.Godcards;

import Model.Worker;

import java.util.Scanner;

public class Triton extends GodCard {

    Scanner scanner = new Scanner(System.in);
    String msg;

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
            getOwner().selectWorkerPhase();
            getOwner().movePhase();
            canDoSomething = false;
        }
        else {
            getOwner().lose();
        }

        do {
            powerUse = false;
            if (getOwner().getSelectedWorker().getPosition().isBorder()) {
                powerUse = getOwner().getController().askForPower();
                if (powerUse){
                    getOwner().movePhase();
                }
            }
        }while (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder() && powerUse);

        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            getOwner().lose();
        }
        return true;
    }
}
