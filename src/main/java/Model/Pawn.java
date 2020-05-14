package Model;

/**
 * Represents the type of a single piece of the game
 */
public abstract class Pawn {
    private Box position;
    private PawnType type;

    public PawnType getType(){
           return type;
    }

    public void setType(PawnType type) {
        this.type = type;
    }

    public Box getPosition() {
        return position;
    }

    public void setPosition(Box position) {
        this.position = position;
    }
}
