package Model;

import Model.Godcards.Apollo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        Game.getInstance().loadGame("game0");
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void setUpWorkers() {
        Worker fullyMovedWorker = Game.getInstance().getPlayer().get(1).getWorkers().get(0);
        Game.getInstance().getPlayer().get(1).SetUpWorkers();
        Assert.assertEquals(true, !fullyMovedWorker.isMoved() && !fullyMovedWorker.getDidClimb() && !fullyMovedWorker.isDidBuild() && fullyMovedWorker.getPosition().equals(fullyMovedWorker.getLastPosition()));
    }

    @Test
    public void illegalDraw() throws Exception {
        Player player0 = new Player("asd");
        ArrayList<Integer> index = new ArrayList<>();
        index.add(1);
        Assert.assertEquals(null, player0.Draw(null, index));
    }

    @Test
    public void illegalChooseGod() throws Exception {
        Player player0 = new Player("asd");
        Assert.assertEquals(false , player0.ChooseGod(0));
    }

    @Test
    public void ChooseGod() {
        Player player = new Player("asd");
        player.setGod(new Apollo());
        Assert.assertEquals(false, player.ChooseGod(0));
    }

    @Test
    public void selectWorker() {
        Player player0 = Game.getInstance().getPlayer().get(0);
        player0.setSelectedWorker(null);
        Assert.assertEquals(true, player0.selectWorker(PlayGround.getInstance().getBox(4,4)) == null);
        Worker selected = player0.selectWorker(PlayGround.getInstance().getBox(0 , 0));
        Assert.assertEquals(true, selected.getOwner().equals(player0));
    }

    @Test
    public void illegalSelectWorker() throws Exception {
        Player player = new Player("asd");
        Assert.assertEquals(null, player.selectWorker(null));
    }

    @Test
    public void setWinner() {
        Game.getInstance();
        Player player0 = new Player("asd");
        player0.setWinner(true);
        Assert.assertEquals(true, player0.isWinner() && !player0.isLoser());

    }

    @Test
    public void setUsePower() {
        Game.getInstance();
        Player player0 = new Player("asd");
        player0.setUsePower(true);
        Assert.assertEquals(true, player0.isUsePower());
    }

}