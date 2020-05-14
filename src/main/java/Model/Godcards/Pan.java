package Model.Godcards;

import Model.Worker;

/**
 * Class of GodCard Pan
 */
public class Pan extends GodCard {

    /**
     * Constructor of the god
     */
    public Pan(){
        this.setName("Pan");
        this.setPower("You win even if you go down of at least 2 levels");
    }

    /**
     * Implement Pan's special power
     * @return true or false
     */
    @Override
    public boolean myVictoryCondition() {
        Worker actualWorker = this.getOwner().getSelectedWorker();
        if (actualWorker != null && actualWorker.isMoved()) {
            return (actualWorker.getPosition().getStructure().size() - actualWorker.getLastPosition().getStructure().size() < 0);
        }
        return false;
    }
}
