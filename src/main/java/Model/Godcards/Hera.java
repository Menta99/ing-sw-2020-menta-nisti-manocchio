package Model.Godcards;

import Model.Box;

public class Hera extends GodCard {

    public Hera(){
        this.setName("Hera");
        this.setPower("Enemy players can't win moving on a Border position");
    }

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
