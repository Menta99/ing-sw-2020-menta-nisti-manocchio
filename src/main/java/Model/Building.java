package Model;

public class Building extends Pawn{

     /**
     * Constructor method
     * @param type PawnType of the Building
     * @param position Box of Building
     */
    public Building(PawnType type, Box position){
        setPosition(position);
        setType(type);
    }
}
