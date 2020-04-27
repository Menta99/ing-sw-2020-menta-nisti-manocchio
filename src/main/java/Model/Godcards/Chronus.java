package Model.Godcards;

import Model.Game;
import Model.PawnType;
import Model.PlayGround;

/**
 * Class of GodCard Chronus
 */
public class Chronus extends GodCard {

    /**
     * Constructor of the god
     */
    public Chronus() {
        this.setName("Chronus");
        this.setPower("You win even if there are 5 Completed Building on the map (1,2,3 floor and a Dome on top)");
    }

    /**
     * Implement Chronus' special power
     * @return true or false
     */
    @Override
    public boolean myVictoryCondition() {
        int counter = 0;
        PlayGround map = PlayGround.getInstance();
        for (int i = 0; i < map.getSIZE(); i++) {
            for (int j = 0; j < map.getSIZE(); j++) {
                if (map.getBox(i, j).getStructure().size() == 5 && map.getBox(i, j).getUpperLevel() == PawnType.DOME) {
                    counter++;
                }
            }
        }
        if (counter >= 5){
            getOwner().setWinner(true);
            Game.getInstance().getController().CheckGameFinished();
        }
    return false;
    }
}
