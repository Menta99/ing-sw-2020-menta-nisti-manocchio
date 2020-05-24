package Model.Godcards;

import Client.ClientCli;
import Model.Game;
import Model.PlayGround;
import Model.Player;
import Server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HestiaTest {


    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
    }

    @After
    public void tearDown(){
        Game.getInstance().setController(null);
        Game.getInstance().CleanGame();
    }

    @Test
    public void activeSubroutine() {
    }

    @Test
    public void myBuild() {
        GodCard card = new Hestia();
        Player player = new Player("asd");
        card.setOwner(player);
        player.setGod(card);
        Assert.assertEquals(false, card.myBuild()); //if there's no selected worker returns what it gets
    }

    @Test
    public void specialBuilding() {
        GodCard card = new Hestia();
        Player player = new Player("asd");
        card.setOwner(player);
        player.setGod(card);
        player.setSelectedWorker(player.getWorkers().get(0));
        player.getSelectedWorker().setPosition(PlayGround.getInstance().getBox(1,1));
        player.getSelectedWorker().Build(PlayGround.getInstance().getBox(1,2));
        Assert.assertEquals(true, !(card.specialBuilding(player.getSelectedWorker().getPosition().BorderBoxes()).contains(PlayGround.getInstance().getBox(0,0))));
        Assert.assertEquals(true, !(card.specialBuilding(player.getSelectedWorker().getPosition().BorderBoxes()).contains(PlayGround.getInstance().getBox(0,1))));
        Assert.assertEquals(true, !(card.specialBuilding(player.getSelectedWorker().getPosition().BorderBoxes()).contains(PlayGround.getInstance().getBox(1,0))));

    }

    @Test
    public void activeSubroutineArtemisDemeter()  {
        String[] serverarg = new String[1];
        serverarg[0] = "1";
        new Thread(() -> Server.main(serverarg)).start();
        try{
            Thread.sleep(200);
            new Thread(() -> {
                String[] arguments0 = new String[2];
                String debug0 = "1";
                String fileName0 = "temp/HestiaAtlasPlayer0.txt";
                arguments0[0] = debug0;
                arguments0[1] = fileName0;
                ClientCli.main(arguments0);
            }).start();
            Thread.sleep(200);
            new Thread(() -> {
                String[] arguments1 = new String[2];
                String debug1 = "1";
                String fileName1 = "temp/HestiaAtlasPlayer1.txt";
                arguments1[0] = debug1;
                arguments1[1] = fileName1;
                ClientCli.main(arguments1);
            }).start();
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            System.out.println("interrupted");
        }
    }
}