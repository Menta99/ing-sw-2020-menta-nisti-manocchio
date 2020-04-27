package Model.Godcards;

import Model.Box;

/**
 * Class of GodCard Hera
 */
public class Hera extends GodCard {

    /**
     * Constructor of the god
     */
    public Hera(){
        this.setName("Hera");
        this.setPower("Enemy players can't win moving on a Border position");
    }

    /**
     * Implement Hera's special power
     * @param dest's the destination box
     * @return true or false
     */
    @Override
    public boolean enemyVictoryCondition(Box dest){
        if(dest!=null){
            if (dest.isBorder()){
                return true;
            }
        }
        return false;
    }
}
