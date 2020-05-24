package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {
    Worker worker00;
    Worker worker01;
    Worker worker10;
    Worker worker11;

    @Before
    public void setUp() {
        Game.getInstance().CleanGame();
        Game.getInstance().loadGame("game0");
        worker00 = Game.getInstance().getPlayer().get(0).getWorkers().get(0);
        worker01 = Game.getInstance().getPlayer().get(0).getWorkers().get(1);
        worker10 = Game.getInstance().getPlayer().get(1).getWorkers().get(0);
        worker11 = Game.getInstance().getPlayer().get(1).getWorkers().get(1);
    }

    @After
    public void tearDown() {
        Game.getInstance().CleanGame();
    }

    @Test
    public void move() {
        worker00.Move(PlayGround.getInstance().getBox(0,1));
        Assert.assertEquals(true, worker00.getPosition().equals(PlayGround.getInstance().getBox(0,1)));
    }

    @Test
    public void build() {
        worker00.Build(PlayGround.getInstance().getBox(0,1));
        Assert.assertEquals(true, PlayGround.getInstance().getBox(0,1).getUpperLevel().equals(PawnType.Level_1));
    }

    @Test
    public void buildDome() {
        worker00.BuildDome(PlayGround.getInstance().getBox(0,1));
        Assert.assertEquals(true, PlayGround.getInstance().getBox(0,1).getUpperLevel().equals(PawnType.DOME));
    }

    @Test
    public void canMove() {
        Assert.assertEquals(true, worker00.CanMove() && worker01.CanMove() && worker10.CanMove() && worker11.CanMove());
    }

    @Test
    public void canBuild() {
        Assert.assertEquals(true, worker00.CanBuild() && worker01.CanBuild() && worker10.CanBuild() && worker11.CanBuild());
    }

}