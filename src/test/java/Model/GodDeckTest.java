package Model;

import Model.Godcards.GodCard;
import Model.Godcards.GodDeck;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GodDeckTest {
    private GodDeck myDeck;
    private GodCard myCard;
    @Before
    public void setUp() throws Exception {
        myDeck = new GodDeck();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void draw() {
        myCard = myDeck.Draw(0);
        Assert.assertEquals("APOLLO",myCard.getName().toUpperCase());
        myCard = myDeck.Draw(1);
        Assert.assertEquals("ARTEMIS",myCard.getName().toUpperCase());
        myCard = myDeck.Draw(2);
        Assert.assertEquals("ATHENA",myCard.getName().toUpperCase());
        myCard = myDeck.Draw(3);
        Assert.assertEquals("ATLAS",myCard.getName().toUpperCase());
    }

    @Test
    public void invalidDraw() {
        myCard = myDeck.Draw(0);
        Assert.assertEquals("APOLLO",myCard.getName().toUpperCase());
        myCard = myDeck.Draw(0);
        Assert.assertEquals(null ,myCard);
    }

    @Test
    public void nonSpecialGod(){
        myCard=myDeck.getCardList().get(3);
        Assert.assertEquals(false,myCard.activeSubroutine());
        Assert.assertEquals(false,myCard.moveOthers(null));
        Assert.assertNull(myCard.specialMovement(null));
        Assert.assertNull(myCard.specialBuilding(null));

    }
}