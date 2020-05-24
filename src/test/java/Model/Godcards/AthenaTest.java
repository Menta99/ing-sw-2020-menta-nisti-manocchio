package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.PlayGround;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AthenaTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        Game.getInstance().loadGame("athenaChronusTest");
    }

    @After
    public void tearDown(){
        Game.getInstance().setController(null);
        Game.getInstance().CleanGame();
    }

    @Test
    public void specialMovement() {
        Game.getInstance().getPlayer().get(1).setSelectedWorker(Game.getInstance().getPlayer().get(1).getWorkers().get(0));
        ArrayList<Box> movements = new ArrayList<>();
        movements = Game.getInstance().getPlayer().get(1).getWorkers().get(0).getPosition().BorderBoxes();
        movements = Game.getInstance().getPlayer().get(0).getCard().specialMovement(movements);
        Assert.assertEquals(true, !movements.contains(PlayGround.getInstance().getBox(0,0)));
        Assert.assertEquals(true, !movements.contains(PlayGround.getInstance().getBox(1,0)));
        Game.getInstance().getPlayer().get(1).setSelectedWorker(null);
        movements = Game.getInstance().getPlayer().get(1).getWorkers().get(0).getPosition().BorderBoxes();
        Assert.assertEquals(movements, Game.getInstance().getPlayer().get(0).getCard().specialMovement(movements));
    }

}