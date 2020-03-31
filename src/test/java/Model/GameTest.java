package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startGame() {
        Game myGame = Game.getInstance();
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        Assert.assertEquals(0, myGame.getPlayer().size());
        Assert.assertEquals(false, myGame.getGameFinished());
        Assert.assertEquals(0, myGame.getActualTurn());
        Assert.assertEquals(null, myGame.getWinner());
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        myGame.StartGame();
        Assert.assertEquals(3, myGame.getPlayer().size());
        Assert.assertEquals(myPlayers.get(0), myGame.getActualPlayer());
        Assert.assertEquals("0000",myGame.getId());
        myGame.getPlayer().removeAll(myPlayers);
    }

    @Test
    public void nextTurn() {
        Game myGame = Game.getInstance();
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        myGame.StartGame();
        Assert.assertEquals(3, myGame.getPlayer().size());
        Assert.assertEquals(myPlayers.get(0), myGame.getActualPlayer());
        myGame.NextTurn();
        Assert.assertEquals(myPlayers.get(1), myGame.getActualPlayer());
        myGame.NextTurn();
        Assert.assertEquals(myPlayers.get(2), myGame.getActualPlayer());
        myGame.NextTurn();
        Assert.assertEquals(myPlayers.get(0), myGame.getActualPlayer());
        myGame.getPlayer().removeAll(myPlayers);
    }

    @Test
    public void extractCards(){
        Game myGame = Game.getInstance();
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        myGame.StartGame();
        myGame.getDeck().getCardList().add(new TestCard("Apollo"));
        myGame.getDeck().getCardList().add(new TestCard("Zeus"));
        myGame.getDeck().getCardList().add(new TestCard("Ade"));
        myGame.getDeck().getCardList().add(new TestCard("Era"));
        myGame.getDeck().getCardList().add(new TestCard("Hermes"));
        myGame.getDeck().getCardList().add(new TestCard("Pan"));
        int list[] = {0,2,3};
        Boolean myBool = false;
        myBool = myGame.ExtractCard(list);
        Assert.assertEquals(true,myBool);
        Assert.assertEquals("Apollo",myGame.getActiveCards().get(0).getName());
        Assert.assertEquals("Ade",myGame.getActiveCards().get(1).getName());
        Assert.assertEquals("Era",myGame.getActiveCards().get(2).getName());
        myGame.getPlayer().removeAll(myPlayers);
    }
}