package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodDeckTest {
    /*
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    */
    @Test
    public void draw() {
        GodDeck myDeck = new GodDeck();
        GodCard myCard;
        myDeck.getCardList().add(new TestCard("Apollo"));
        myDeck.getCardList().add(new TestCard("Zeus"));
        myDeck.getCardList().add(new TestCard("Ade"));
        myDeck.getCardList().add(new TestCard("Era"));
        myCard = myDeck.Draw(0);
        Assert.assertEquals("Apollo",myCard.getName());
        myCard = myDeck.Draw(1);
        Assert.assertEquals("Zeus",myCard.getName());
        myCard = myDeck.Draw(2);
        Assert.assertEquals("Ade",myCard.getName());
        myCard = myDeck.Draw(3);
        Assert.assertEquals("Era",myCard.getName());
    }

    @Test
    public void invalidDraw() {
        GodDeck myDeck = new GodDeck();
        GodCard myCard;
        myDeck.getCardList().add(new TestCard("Apollo"));
        myCard = myDeck.Draw(0);
        Assert.assertEquals("Apollo",myCard.getName());
        myCard = myDeck.Draw(0);
        Assert.assertEquals(null ,myCard);
    }
}