package Model.Godcards;

import Model.Game;
import Model.PawnType;
import Model.PlayGround;

public class Chronus extends GodCard {

    public Chronus() {
        this.setName("Chronus");
        this.setPower("You win even if there are 5 Completed Building on the map (1,2,3 floor and a Dome on top)");
    }

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
            Game.getInstance().CheckGameFinished();
        }
    return false;
    }
}
