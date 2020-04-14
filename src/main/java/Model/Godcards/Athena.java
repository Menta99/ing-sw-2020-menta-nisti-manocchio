package Model.Godcards;

import Model.Box;
import Model.Game;

import java.util.ArrayList;

public class Athena extends GodCard{

    public Athena() {
        this.setName("Athena");
        this.setPower("If in your last turn you did climb with one of your workers, in this turn enemy workers can't climb");
    }

    @Override
    public boolean enemyMovement(){
        return true;
    }

    @Override
    public ArrayList<Box> specialMovement(ArrayList<Box> legalMovs){
        if (getOwner().getSelectedWorker().getDidClimb()){
            Box position = Game.getInstance().getActualPlayer().getSelectedWorker().getPosition();
            for (Box box : legalMovs){
                if (position.getStructure().size()<=box.getStructure().size()){
                    legalMovs.remove(box);
                }
            }
        }
        return legalMovs;
    }

}
