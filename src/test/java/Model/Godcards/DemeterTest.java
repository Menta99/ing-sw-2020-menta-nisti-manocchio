package Model.Godcards;

import Model.Game;
import Model.PlayGround;
import Model.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DemeterTest {



    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }
    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void activeSubroutine() {
    }

    @Test
    public void myBuild() {
        GodCard demeter = new Demeter();
        Player player = new Player("asd");
        player.setGod(demeter);
        demeter.setOwner(player);
        Assert.assertEquals(false, demeter.myBuild());
        player.setSelectedWorker(player.getWorkers().get(0));
        player.getWorkers().get(0).setDidBuild(true);
        Assert.assertEquals(true, demeter.myBuild());
    }

    @Test
    public void specialBuilding() {
        GodCard demeter = new Demeter();
        Player player = new Player("asd");
        player.setGod(demeter);
        demeter.setOwner(player);
        player.setSelectedWorker(player.getWorkers().get(0));
        player.getSelectedWorker().setPosition(PlayGround.getInstance().getBox(1,1));
        player.getWorkers().get(0).Build(PlayGround.getInstance().getBox(0,0));
        Assert.assertEquals(true, !(demeter.specialBuilding(player.getSelectedWorker().getPosition().BorderBoxes()).contains(player.getSelectedWorker().getLastBuilding())));
    }
}