package Model.Godcards;

import java.util.ArrayList;

/**
 * Class representing a bunch of Godcards
 */
public class GodDeck {
    private ArrayList<GodCard> cardList;

    /**
     * Constructor of GodDeck
     */
    public GodDeck(){
        cardList = new ArrayList<GodCard>();
        GodFactory factory = new GodFactory();
        for (GodsEnum god : GodsEnum.values()){
            cardList.add(factory.create(god));
        }
    }

    /**
     * Getter of a GodCard from the Deck
     * @param index position of the GodCard
     * @return GodCard if the index is valid, else null
     * @throws NullPointerException if requested invalid action on the cards
     */
    public GodCard Draw(int index){
        try{
            if ((index > -1) && (index < cardList.size())){
                if(!cardList.get(index).isChosen()) {
                    cardList.get(index).setChosen(true);
                    return cardList.get(index);
                }
            }
            return null;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter of cardList
     * @return cardList
     */
    public ArrayList<GodCard> getCardList(){
        return cardList;
    }
}
