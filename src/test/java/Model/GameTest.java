package Model;

import Model.Godcards.GodsEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class GameTest {
    Game myGame;

    @Before
    public void setUp() throws Exception {
        myGame = Game.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        myGame.CleanGame();
    }

    @Test
    public void startGame() {
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        Assert.assertEquals(0, myGame.getPlayer().size());
        Assert.assertEquals(0, myGame.getActualTurn());
        Assert.assertNull(myGame.getWinner());
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        Assert.assertEquals(3, myGame.getPlayer().size());
    }

    @Test
    public void deckCompositionTest(){
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        for (GodsEnum god : GodsEnum.values()){
            Assert.assertEquals(god.toString(), myGame.getDeck().getCardList().get(god.getIndex()).getName().toUpperCase());
        }
    }
}