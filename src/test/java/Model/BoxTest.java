package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class BoxTest {

    @Before
    public void setUp() throws Exception {
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown() throws Exception {
        PlayGround.getInstance().Clean();
    }

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
        Assert.assertEquals(false, myBool);
    }

    @Test
    public void playableWorker(){
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
        Worker worker=new Worker();
        Box myBox2 = new Box(1,3);
        Assert.assertEquals(1,myBox2.getPosX());
        Assert.assertEquals(3,myBox2.getPosY());
        worker.setInitialPosition(myBox2);
        myBox2.Build();
        Assert.assertEquals(PawnType.WORKER,myBox2.getUpperLevel());
        Assert.assertEquals(3,myBox2.getStructure().size());
        Box myBox = new Box(0,0);
        Assert.assertEquals(true,myBox.isBorder());
        Assert.assertEquals(true,myBox.Playable());
        Assert.assertEquals(false,myBox.isOccupied());
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
    public void BuildDome() {
      PlayGround field=PlayGround.getInstance();
      field.getBox(1,1).BuildDome();
      Assert.assertEquals(PawnType.DOME,field.getBox(1,1).getUpperLevel());
      Assert.assertEquals(2,field.getBox(1,1).getStructure().size());
      Assert.assertEquals(false,field.getBox(1,1).BuildDome());
    }

    @Test
    public void cleanPlayground(){
        PlayGround field=PlayGround.getInstance();
        field.getBox(0,0).Build();
        field.Clean();
        Assert.assertEquals(PawnType.GROUND_LEVEL,field.getBox(0,0).getUpperLevel());
    }

    @Test
    public void illegalBorderBoxes() throws Exception {
        Box box = new Box(10, 10);
        ArrayList<Box> vuoto = new ArrayList<>();
        Assert.assertEquals(vuoto, box.BorderBoxes());
        Box box1 = null;
        Assert.assertEquals(null, box1);
    }

}