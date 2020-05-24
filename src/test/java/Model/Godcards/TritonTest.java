package Model.Godcards;

import Client.ClientCli;
import Model.Game;
import Server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TritonTest {

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        System.gc();
    }

    @After
    public void tearDown(){
        Game.getInstance().setController(null);
        Game.getInstance().CleanGame();
        System.gc();
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
                String fileName0 = "temp/TritonPrometeoHephaestusPlayer0.txt";
                arguments0[0] = debug0;
                arguments0[1] = fileName0;
                ClientCli.main(arguments0);
            }).start();
            Thread.sleep(200);
            new Thread(() -> {
                String[] arguments1 = new String[2];
                String debug1 = "1";
                String fileName1 = "temp/TritonPrometeoHephaestusPlayer1.txt";
                arguments1[0] = debug1;
                arguments1[1] = fileName1;
                ClientCli.main(arguments1);
            }).start();
            Thread.sleep(200);
            new Thread(() -> {
                String[] arguments2 = new String[2];
                String debug2 = "1";
                String fileName2 = "temp/TritonPrometeoHephaestusPlayer2.txt";
                arguments2[0] = debug2;
                arguments2[1] = fileName2;
                ClientCli.main(arguments2);
            }).start();
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            System.out.println("interrupted");
        }
    }
}