package Model.Godcards;

import Model.Worker;

public class Pan extends GodCard {

    public Pan(){
        this.setName("Pan");
        this.setPower("You win even if you go down of at least 2 levels");
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
