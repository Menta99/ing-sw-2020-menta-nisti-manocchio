package Model;

public class Pan extends GodCard {

    public Pan(){
        this.setName("Pan");
    }

    @Override
    public boolean myVictoryCondition() {
        Worker actualWorker = this.getOwner().getSelectedWorker();
        if (actualWorker != null) {
            if (actualWorker.getPosition().getStructure().size() - actualWorker.getLastPosition().getStructure().size() < 0) { //Se è sceso di almeno 2 piani return true
                return true;
            }
        }
        return false;
    }
}
