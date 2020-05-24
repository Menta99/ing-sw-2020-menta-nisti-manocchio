package Model.Godcards;

import Model.Game;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChronusTest {

    @Before
    public void setUp(){
        Game.getInstance().setController(null);
        Game.getInstance().CleanGame();
        Game.getInstance().loadGame("athenaChronusTest");
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }


    @Test (expected = NullPointerException.class)
    public void myVictoryCondition() {
        Game.getInstance().getPlayer().get(1).getCard().myVictoryCondition();
        Assert.assertEquals(true, Game.getInstance().getPlayer().get(1).isWinner());
    }
}