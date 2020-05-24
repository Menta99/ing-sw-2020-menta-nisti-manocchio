package Model.Godcards;

/**
 * Class implementing factory pattern to create gods
 */
public class GodFactory {
    public GodCard create(GodsEnum god){
        GodCard toReturn = null;
        switch (god) {
            case APOLLO:
                toReturn = new Apollo();
                break;
            case ARTEMIS:
                toReturn = new Artemis();
                break;
            case ATHENA:
                toReturn = new Athena();
                break;
            case ATLAS:
                toReturn = new Atlas();
                break;
            case CHRONUS:
                toReturn = new Chronus();
                break;
            case DEMETER:
                toReturn = new Demeter();
                break;
            case HEPHAESTUS:
                toReturn = new Hephaestus();
                break;
            case HERA:
                toReturn = new Hera();
                break;
            case HESTIA:
                toReturn = new Hestia();
                break;
            case MINOTAUR:
                toReturn = new Minotaur();
                break;
            case PAN:
                toReturn = new Pan();
                break;
            case PROMETEO:
                toReturn = new Prometeo();
                break;
            case TRITON:
                toReturn = new Triton();
                break;
            case ZEUS:
                toReturn = new Zeus();
                break;
        }
        return toReturn;
    }
}
