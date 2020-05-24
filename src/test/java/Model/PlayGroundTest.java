package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayGroundTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().CleanGame();
    }

    @Test
    public void illegalGetBox() throws IndexOutOfBoundsException {
        Assert.assertEquals(true, PlayGround.getInstance().getBox(5,5) == null) ;
    }
}