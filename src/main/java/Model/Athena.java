package Model;

import java.util.ArrayList;

public class Athena extends GodCard{

    public Athena() {
        this.setName("Athena");
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
