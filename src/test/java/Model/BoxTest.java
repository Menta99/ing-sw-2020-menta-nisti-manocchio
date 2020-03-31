package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

public class BoxTest {
    /*
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    */
    @Test
    public void getUpperLevel() {
        Box myBox = new Box(0,0);
        PawnType myPawn = null;
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.GROUND_LEVEL,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_1,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_2,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_3,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.DOME,myPawn);
    }

    @Test
    public void playable() {
        Box myBox = new Box(0,0);
        Boolean myBool = false;
        myBool = myBox.Playable();
        Assert.assertEquals(true,myBool);
        myBox.Build();
        myBox.Build();
        myBox.Build();
        myBox.Build();
        myBool = myBox.Playable();
        Assert.assertEquals(false,myBool);
    }

    @Test
    public void playableWorker(){
        //PlayGround myBoard = PlayGround.getInstance();
        //Box myBox = myBoard.getBox(0,0);
        Box myBox = new Box(0,0);
        Boolean myBool = false;
        myBool = myBox.Playable();
        Assert.assertEquals(true,myBool);
        Worker myWorker = new Worker();
        myWorker.setInitialPosition(myBox);
        myBool = myBox.Playable();
        Assert.assertEquals(false,myBool);
    }

    @Test
    public void borderBoxes() {
        PlayGround myBoard = PlayGround.getInstance();
        Box myBox = myBoard.getBox(2,2);
        ArrayList<Box> myNeight = null;
        myNeight = myBox.BorderBoxes();
        Iterator<Box> myIterator = myNeight.iterator();
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if (!(i == 0 && j == 0) && (myBox.getPosX() + i > -1) && (myBox.getPosX() + i < 5) && (myBox.getPosY() + j > 0) && (myBox.getPosY() + j < 5)){
                    Assert.assertEquals(myBoard.getBox(myBox.getPosX() + i,myBox.getPosY() + j),myIterator.next());
                }
            }
        }
    }

    @Test
    public void build() {
        Box myBox = new Box(0,0);
        PawnType myPawn = null;
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.GROUND_LEVEL,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_1,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_2,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_3,myPawn);
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.DOME,myPawn);
    }

    @Test
    public void destroy() {
        Box myBox = new Box(0,0);
        PawnType myPawn = null;
        Boolean myBool = false;
        myBox.Build();
        myBox.Build();
        myBox.Build();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_3,myPawn);
        myBool = myBox.Destroy();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_2,myPawn);
        Assert.assertEquals(true,myBool);
        myBool = myBox.Destroy();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.Level_1,myPawn);
        Assert.assertEquals(true,myBool);
        myBool = myBox.Destroy();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.GROUND_LEVEL,myPawn);
        Assert.assertEquals(true,myBool);
        myBool = myBox.Destroy();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.GROUND_LEVEL,myPawn);
        Assert.assertEquals(false,myBool);
        Worker myWorker = new Worker();
        myWorker.setInitialPosition(myBox);
        myBool = myBox.Destroy();
        myPawn = myBox.getUpperLevel();
        Assert.assertEquals(PawnType.WORKER,myPawn);
        Assert.assertEquals(false,myBool);
    }
}