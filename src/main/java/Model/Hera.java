package Model;

public class Hera extends GodCard {

    public Hera(){
        this.setName("Hera");
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
