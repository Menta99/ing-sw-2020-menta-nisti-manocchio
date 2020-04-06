package Model;

public class Atlas extends GodCard {

    public Atlas(){
        this.setName("Atlas");
    }

    @Override
    public boolean activeSubroutine() {
        boolean canDoSomething = false;
        for (Worker worker : getOwner().getWorkers()) {
            canDoSomething = canDoSomething || worker.CanMove();
        }
        if (canDoSomething) {
            getOwner().movePhase();
            canDoSomething = false;
        } else {
            //player loses
        }
        if (getOwner().getSelectedWorker().CanBuild()) {
            getOwner().buildPhase(); // DA CAMBIARE CON IMPLEMENTAZIONE BUILDPHASE BUILDDOME ANZICHé BUILD
        } else {
            //player loses
        }
        return true;
    }
}
