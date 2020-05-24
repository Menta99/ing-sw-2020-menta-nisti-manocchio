package Model.Godcards;

import Model.Game;
import Model.PlayGround;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeraTest {



    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void enemyVictoryCondition() {
        GodCard hera = new Hera();
        Assert.assertEquals(false, hera.enemyVictoryCondition(null));
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                Assert.assertEquals(PlayGround.getInstance().getBox(i,j).isBorder(), hera.enemyVictoryCondition(PlayGround.getInstance().getBox(i,j)));
            }
        }
    }
}