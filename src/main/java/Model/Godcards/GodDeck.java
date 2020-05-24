package Model.Godcards;

import java.util.ArrayList;

/**
 * Class representing a bunch of GodCard
 */
public class GodDeck {
    private final ArrayList<GodCard> cardList;

    /**
     * Constructor of GodDeck
     */
    public GodDeck(){
        cardList = new ArrayList<>();
        GodFactory factory = new GodFactory();
        for (GodsEnum god : GodsEnum.values()){
            cardList.add(factory.create(god));
        }
    }

    /**
     * Getter of a GodCard from the Deck
     * @param index position of the GodCard
     * @return GodCard if the index is valid, else null
     */
    public GodCard Draw(int index){
        if ((index > -1) && (index < cardList.size())){
            if(!cardList.get(index).isChosen()) {
                cardList.get(index).setChosen(true);
                return cardList.get(index);
            }
        }
        return null;
    }

    public ArrayList<GodCard> getCardList(){
        return cardList;
    }
}
