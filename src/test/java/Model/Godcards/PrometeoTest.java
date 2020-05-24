package Model.Godcards;

import Model.Game;
import Model.PlayGround;
import Model.Player;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometeoTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void specialMovement() {
        Player player = new Player("asd");
        GodCard card = new Prometeo();
        player.setGod(card);
        card.setOwner(player);
        Assert.assertEquals(null, card.specialMovement(null)); //if there's no selected worker returns what it gets
        player.setSelectedWorker(player.getWorkers().get(0));
        player.getSelectedWorker().setPosition(PlayGround.getInstance().getBox(0,0));
        PlayGround.getInstance().getBox(1,1).Build();
        Assert.assertEquals(true, !(card.specialMovement(player.getSelectedWorker().getPosition().BorderBoxes())).contains(PlayGround.getInstance().getBox(1,1)));
        PlayGround.getInstance().getBox(0,0).Build();
        PlayGround.getInstance().getBox(1,1).Build();
        Assert.assertEquals(true, !(card.specialMovement(player.getSelectedWorker().getPosition().BorderBoxes())).contains(PlayGround.getInstance().getBox(1,1)));
        PlayGround.getInstance().getBox(0,0).Build();
        PlayGround.getInstance().getBox(1,1).Build();
        Assert.assertEquals(true, !(card.specialMovement(player.getSelectedWorker().getPosition().BorderBoxes())).contains(PlayGround.getInstance().getBox(1,1)));
    }
}