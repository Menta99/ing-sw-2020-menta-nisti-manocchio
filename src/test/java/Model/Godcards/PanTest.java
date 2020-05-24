package Model.Godcards;

import Model.Game;
import Model.PlayGround;
import Model.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanTest {


    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void myVictoryCondition() {
        PlayGround.getInstance();
        Player owner = new Player("asd");
        GodCard pan = new Pan();
        pan.setOwner(owner);
        owner.setGod(pan);
        Assert.assertEquals(false, pan.myVictoryCondition());
        PlayGround.getInstance().getBox(1,1).Build();
        PlayGround.getInstance().getBox(1,1).Build();
        owner.setSelectedWorker(owner.getWorkers().get(0));
        owner.getWorkers().get(0).setPosition(PlayGround.getInstance().getBox(0,0));
        owner.getWorkers().get(0).setLastPosition(PlayGround.getInstance().getBox(1,1));
        owner.getWorkers().get(0).setMoved(true);
        Assert.assertEquals(true, pan.myVictoryCondition());
        PlayGround.getInstance().getBox(1,1).Build();
        Assert.assertEquals(true, pan.myVictoryCondition());
        PlayGround.getInstance().getBox(0,0).Build();
        Assert.assertEquals(true, pan.myVictoryCondition());
    }
}