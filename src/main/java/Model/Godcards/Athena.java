package Model.Godcards;

import Model.Box;
import Model.Game;

import java.util.ArrayList;

/**
 * Class of GodCard Athena
 */
public class Athena extends GodCard{

    /**
     * Constructor of the god
     */
    public Athena() {
        this.setName("Athena");
        this.setPower("If in your last turn you did climb with one of your workers, in this turn enemy workers can't climb");
    }

    @Override
    public boolean enemyMovement(){
        return true;
    }

    /**
     * Implement Athena's special power
     * @param legalMoves  are the correct movements
     * @return all the legal position that you can get without climb
     */
    @Override
    public ArrayList<Box> specialMovement(ArrayList<Box> legalMoves){
        ArrayList<Box> toRemove = new ArrayList<>();
        if (getOwner().getSelectedWorker()==null){
            return legalMoves;
        }
        if (getOwner().getSelectedWorker().getDidClimb()){
            if (Game.getInstance().getActualPlayer().getSelectedWorker() == null){
                return legalMoves;
            }
            Box position = Game.getInstance().getActualPlayer().getSelectedWorker().getPosition();
            for (Box box : legalMoves){
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
            if (legalMoves.contains(box)){
                legalMoves.remove(box);
            }
        }
        return legalMoves;
    }

}
