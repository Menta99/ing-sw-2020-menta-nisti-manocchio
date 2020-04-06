package Model;

public class Chronus extends GodCard {

    public Chronus() {
        this.setName("Chronus");
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
        }
    return false;
    }
}
