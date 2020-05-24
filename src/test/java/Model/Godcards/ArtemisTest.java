package Model.Godcards;

import Client.ClientCli;
import Model.Box;
import Model.Game;
import Model.Worker;
import Server.Server;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ArtemisTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        System.gc();
    }

    @After
    public void tearDown() {
        Game.getInstance().setController(null);
        Game.getInstance().CleanGame();
        System.gc();
    }

    @Test
    public void specialMovement() {
        Game.getInstance().loadGame("apolloArtemisTest");
        Worker worker = Game.getInstance().getPlayer().get(0).getWorkers().get(1);
        Game.getInstance().getPlayer().get(0).setSelectedWorker(worker);
        ArrayList<Box> boxes = new ArrayList<>();
        boxes = worker.getPosition().BorderBoxes();
        boxes = worker.getOwner().getCard().specialMovement(boxes);
        Assert.assertEquals(true, !boxes.contains(worker.getLastPosition()));
        Game.getInstance().CleanGame();
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
                String fileName0 = "temp/testArtemisDemeterPlayer0.txt";
                arguments0[0] = debug0;
                arguments0[1] = fileName0;
                ClientCli.main(arguments0);
            }).start();
            Thread.sleep(200);
            new Thread(() -> {
                String[] arguments1 = new String[2];
                String debug1 = "1";
                String fileName1 = "temp/testArtemisDemeterPlayer1.txt";
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