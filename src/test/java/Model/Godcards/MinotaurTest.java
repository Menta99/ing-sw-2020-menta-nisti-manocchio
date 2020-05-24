package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.PlayGround;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinotaurTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        Game.getInstance().loadGame("minotaurArtemisTest");
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void moveOthers() {
        Game.getInstance().getPlayer().get(1).setSelectedWorker(Game.getInstance().getPlayer().get(1).getWorkers().get(1));
        Assert.assertEquals(true, Game.getInstance().getPlayer().get(1).getCard().moveOthers(PlayGround.getInstance().getBox(2,2)));
        Assert.assertEquals(true, Game.getInstance().getPlayer().get(1).isWinner());
    }

    @Test
    public void canMoveOthers() {
        Game.getInstance().getPlayer().get(1).setSelectedWorker(Game.getInstance().getPlayer().get(1).getWorkers().get(1));
        Assert.assertEquals(true, Game.getInstance().getPlayer().get(1).getCard().canMoveOthers(PlayGround.getInstance().getBox(2,2)));
        Game.getInstance().getPlayer().get(1).setSelectedWorker(null);
        Assert.assertEquals(false, Game.getInstance().getPlayer().get(1).getCard().canMoveOthers(PlayGround.getInstance().getBox(2,2)));
    }
}