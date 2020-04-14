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
            if (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder()) {
                System.out.println("\n Would you like to use " + getName() + "Power? \n yes / no");
                while (msg == null || !msg.equals("yes") && !msg.equals("no")) {
                    msg = scanner.nextLine();
                }
                if (msg.equals("yes")){
                    getOwner().movePhase();
                    msg = null;
                }
            }
        }while (getOwner().getSelectedWorker().CanMove() && getOwner().getSelectedWorker().getPosition().isBorder() && msg.equals("yes"));

        if (getOwner().getSelectedWorker().CanBuild()){
            getOwner().buildPhase();
        }
        else {
            getOwner().lose();
        }
        return true;
    }
}
