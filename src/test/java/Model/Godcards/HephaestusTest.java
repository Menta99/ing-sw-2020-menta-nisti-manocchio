package Model.Godcards;

import Model.Game;
import Model.PlayGround;
import Model.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HephaestusTest {


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
        GodCard hephaestus = new Hephaestus();
        Player player = new Player("asd");
        player.setGod(hephaestus);
        hephaestus.setOwner(player);
        Assert.assertEquals(false, hephaestus.myBuild());
        player.setSelectedWorker(player.getWorkers().get(0));
        player.setUsePower(true);
        Assert.assertEquals(true, hephaestus.myBuild());
    }

    @Test
    public void specialBuilding() {
        GodCard hephaestus = new Hephaestus();
        Player player = new Player("asd");
        player.setGod(hephaestus);
        hephaestus.setOwner(player);
        player.setSelectedWorker(player.getWorkers().get(0));
        player.getSelectedWorker().setPosition(PlayGround.getInstance().getBox(1,1));
        player.getWorkers().get(0).Build(PlayGround.getInstance().getBox(0,0));
        player.getWorkers().get(0).Build(PlayGround.getInstance().getBox(0,0));
        player.getSelectedWorker().setDidBuild(false);
        Assert.assertEquals(true, !(hephaestus.specialBuilding(player.getSelectedWorker().getPosition().BorderBoxes()).contains(PlayGround.getInstance().getBox(0,0))));
    }
}