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
        ArrayList<Box> toRemove = new ArrayList<>();
        if (getOwner().getSelectedWorker()==null){
            return legalMovs;
        }
        if (getOwner().getSelectedWorker().getDidClimb()){
            if (Game.getInstance().getActualPlayer().getSelectedWorker() == null){
                return legalMovs;
            }
            Box position = Game.getInstance().getActualPlayer().getSelectedWorker().getPosition();
            for (Box box : legalMovs){
                if (box.isOccupied()){
                    if(position.getStructure().size()<box.getStructure().size()){ // Se è occupato c'è comunque la possibilità che alcune divinità muovano le altre
                        toRemove.add(box);
                    }
                }
                else if (position.getStructure().size()<=box.getStructure().size()){
                    toRemove.add(box);
                }
            }
        }
        for (Box box : toRemove){
            if (legalMovs.contains(box)){
                legalMovs.remove(box);
            }
        }
        return legalMovs;
    }

}
