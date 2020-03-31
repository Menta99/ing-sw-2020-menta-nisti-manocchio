package Model;

public class Building extends Pawn{
    private PawnType type;
    private Box position;

     /**
     * Constructor method
     * @param type PawnType of the Building
     * @param position Box of Building
     */
    public Building(PawnType type, Box position){
        this.type = type;
        this.position = position;
    }

    /**
     * Getter of the building's type
     * @return type
     */
    public PawnType getType(){
        return type;
    }

    /**
     * Getter of the building's position
     * @return box
     */
    public Box getPosition(){
        return position;
    }
}
