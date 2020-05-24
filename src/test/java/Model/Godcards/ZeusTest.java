package Model.Godcards;

import Model.Game;
import Model.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZeusTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void myBuild() {
        Assert.assertEquals(true, new Zeus().myBuild());
    }

    @Test
    public void specialBuilding() {
        Player player = new Player("asd");
        GodCard zeus = new Zeus();
        zeus.setOwner(player);
        player.setGod(zeus);
        Assert.assertEquals(null, zeus.specialBuilding(null)); //If there's no selected worker returns what sent
    }
}