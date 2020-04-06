package Model;

import java.util.ArrayList;

public class Zeus extends GodCard {

    public Zeus(){
        this.setName("Zeus");
    }

    @Override
    public boolean myBuild(){
        return true;
    }

    @Override
    public ArrayList<Box> specialBuilding(ArrayList<Box> legalBoxes) {
        ArrayList<Pawn> actualStructure = getOwner().getSelectedWorker().getPosition().getStructure();
        if (actualStructure.get(actualStructure.size() - 2).getType() != PawnType.Level_3) {
            legalBoxes.add(getOwner().getSelectedWorker().getPosition());
        }
        return legalBoxes;
    }

}
