package Model.Godcards;

import Model.Box;
import Model.Pawn;
import Model.PawnType;

import java.util.ArrayList;

/**
 * Class of GodCard Zeus
 */
public class Zeus extends GodCard {

    /**
     * Constructor of the god
     */
    public Zeus(){
        this.setName("Zeus");
        this.setPower("You can build in your own worker's position, if you reach 3rd level this way, you don't win");
    }

    @Override
    public boolean myBuild(){
        return true;
    }

    /**
     * Implement Zeus' power
     * @param legalBoxes
     * @return
     */
    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> legalBoxes) {
        if (getOwner().getSelectedWorker()==null){
            return legalBoxes;
        }
        ArrayList<Pawn> actualStructure = getOwner().getSelectedWorker().getPosition().getStructure();
        if (actualStructure.get(actualStructure.size() - 2).getType() != PawnType.Level_3) {
            legalBoxes.add(getOwner().getSelectedWorker().getPosition());
        }
        return legalBoxes;
    }

}
