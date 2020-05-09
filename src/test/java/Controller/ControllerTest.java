package Controller;

import ComunicationProtocol.CliCommandMsg;
import Model.*;
import View.CLI.Cli;
import VirtualView.VirtualView;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import Client.*;
import Server.*;

import java.util.ArrayList;
import java.util.Random;

public class ControllerTest {
    Game match;
    PlayGround field;
    VirtualView virtual;
    Controller controller;
    Cli cli;

    @Before
    public void setUp() throws Exception {
        controller = new Controller();
        match = Game.getInstance();
        match.setController(controller);
        field = PlayGround.getInstance();
        cli = new Cli();
        virtual = controller.getVirtual();
    }

    @After
    public void tearDown() throws Exception {
        match.CleanGame();
    }

    public void Initialise7(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/prova1");
        files.add("src/test/java/Files/prova2");
        files.add("src/test/java/Files/prova3");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run7() {
        Initialise7(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        for(int i=0;i<4;i++) {
            controller.TurnStart(match.getActualPlayer());
        }
        Assert.assertEquals(true,controller.getHandlers().get(0).getPlayer().isLoser());
        for(int i=4;i<20;i++) {
            controller.TurnStart(match.getActualPlayer());
        }
    }

    public void Initialise6(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/test1");
        files.add("src/test/java/Files/test2");
        files.add("src/test/java/Files/test3");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run6() {
        Initialise6(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        for(int i=0;i<11;i++) {
            controller.TurnStart(match.getActualPlayer());
        }
        Assert.assertEquals(true,field.getBox(4,3).getUpperLevel()==PawnType.WORKER && field.getBox(4,3).getStructure().size()==5);
        for(int i=11;i<17;i++) {
            controller.TurnStart(match.getActualPlayer());
        }
        Assert.assertEquals(match.getPlayer().get(1).getNickName(),match.getWinner().getNickName());
        Assert.assertEquals(true,match.isGameFinished());

    }


    public void Initialise5(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/read1");
        files.add("src/test/java/Files/read2");
        files.add("src/test/java/Files/read3");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run5(){
        Initialise5(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer()); //end initialise
        controller.TurnStart(match.getActualPlayer()); // 1 turn
        controller.TurnStart(match.getActualPlayer()); // 2 turn
        controller.TurnStart(match.getActualPlayer()); // 3 turn
        controller.TurnStart(match.getActualPlayer()); // 4 turn
        controller.TurnStart(match.getActualPlayer()); // 5 turn
        controller.TurnStart(match.getActualPlayer()); // 6 turn
        controller.TurnStart(match.getActualPlayer()); // 7 turn
        controller.TurnStart(match.getActualPlayer()); // 8 turn
        controller.TurnStart(match.getActualPlayer()); // 9 turn
        controller.TurnStart(match.getActualPlayer()); // 10 turn
        controller.TurnStart(match.getActualPlayer()); // 11 turn
        controller.TurnStart(match.getActualPlayer()); // 12 turn
        controller.TurnStart(match.getActualPlayer()); // 13 turn
        controller.TurnStart(match.getActualPlayer()); // 14 turn
        controller.TurnStart(match.getActualPlayer()); // 15 turn
        controller.TurnStart(match.getActualPlayer()); // 16 turn vittoria con pan verified
    }


    public void Initialise4(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/1");
        files.add("src/test/java/Files/2");
        files.add("src/test/java/Files/3");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run4(){
        Initialise4(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer()); //fine inizializzazione
        controller.TurnStart(match.getActualPlayer()); // 1 turn
        Assert.assertEquals(PawnType.WORKER,field.getBox(2,2).getUpperLevel());
        controller.TurnStart(match.getActualPlayer()); // 2 turn
        Assert.assertEquals(field.getBox(0,4),match.getPlayer().get(1).getWorkers().get(1).getPosition());
        controller.TurnStart(match.getActualPlayer());// 3 turn
        Assert.assertEquals(field.getBox(3,1),match.getPlayer().get(2).getWorkers().get(0).getPosition());
        Assert.assertEquals(field.getBox(3,0),match.getPlayer().get(0).getWorkers().get(1).getPosition());
    }


    public void Initialise3(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/play1");
        files.add("src/test/java/Files/play2");
        files.add("src/test/java/Files/play3");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run3(){
        Initialise3(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer()); //fine inizializzazione
        Assert.assertEquals(match.getPlayer().get(0).getCard().getName(),"Apollo");
        Assert.assertEquals(match.getPlayer().get(1).getCard().getName(),"Hephaestus");
        Assert.assertEquals(match.getPlayer().get(2).getCard().getName(),"Prometeo");
        controller.TurnStart(match.getActualPlayer()); // 1 turn
        controller.TurnStart(match.getActualPlayer()); // 2 turn
        Assert.assertEquals(field.getBox(1,3),match.getPlayer().get(0).getWorkers().get(0).getPosition());
        Assert.assertEquals(PawnType.Level_2,field.getBox(3,4).getUpperLevel());
        controller.TurnStart(match.getActualPlayer());// 3 turn
        Assert.assertEquals(PawnType.Level_1,field.getBox(3,3).getUpperLevel());
        Assert.assertEquals(PawnType.Level_1,field.getBox(3,1).getUpperLevel());
    }


    public void Initialise2(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/Player1.txt");
        files.add("src/test/java/Files/Player2.txt");
        files.add("src/test/java/Files/Player3.txt");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
    public void run2(){
        Initialise2(3);
        controller.setPlayerNum(3);
        controller.VirtualWelcome();
        controller.StartGame();
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer()); //fine inizializzazione
        Assert.assertEquals(match.getPlayer().get(0).getCard().getName(),"Artemis");
        Assert.assertEquals(match.getPlayer().get(1).getCard().getName(),"Athena");
        Assert.assertEquals(match.getPlayer().get(2).getCard().getName(),"Hestia");
        controller.TurnStart(match.getActualPlayer()); // 1 turn
        controller.TurnStart(match.getActualPlayer()); // 2 turn
        controller.TurnStart(match.getActualPlayer());// 3 turn
        controller.TurnStart(match.getActualPlayer()); // 4 turn to test athena's power
        Assert.assertEquals(PawnType.Level_1,field.getBox(0,4).getUpperLevel());
        Assert.assertEquals(PawnType.Level_1,field.getBox(3,4).getUpperLevel());
        Assert.assertEquals(PawnType.Level_1,field.getBox(3,2).getUpperLevel());
        Assert.assertEquals(PawnType.Level_1,field.getBox(3,0).getUpperLevel());
    }


   public void Initialise(int i){
        ArrayList<String> files=new ArrayList<>();
        files.add("src/test/java/Files/lettura.txt");
        files.add("src/test/java/Files/lettura2.txt");
        for (int j=0;j<i;j++){
            ClientHandlerTest handler = new ClientHandlerTest(j,files.get(j));
            controller.getHandlers().add(handler);
            new Thread(handler).start();
        }
    }

    @Test
     public void run() {
        Initialise(2);
        controller.VirtualWelcome();
        controller.StartGame();
        controller.TurnStart(match.getActualPlayer());
        Assert.assertEquals(field.getBox(2,2),match.getPlayer().get(0).getWorkers().get(0).getPosition());
        Assert.assertEquals(field.getBox(3,3),match.getPlayer().get(0).getWorkers().get(1).getPosition());
        controller.TurnStart(match.getActualPlayer());
        controller.TurnStart(match.getActualPlayer()); //primo turno
        Assert.assertEquals(field.getBox(3,2),match.getPlayer().get(0).getWorkers().get(0).getPosition());
        Assert.assertEquals(PawnType.Level_1,field.getBox(4,2).getUpperLevel());
        controller.TurnStart(match.getActualPlayer()); // 2 turno
        Assert.assertEquals(field.getBox(1,2),match.getPlayer().get(1).getWorkers().get(0).getPosition());
        Assert.assertEquals(PawnType.Level_1,field.getBox(1,3).getUpperLevel());
        Assert.assertEquals(PawnType.Level_1,field.getBox(0,2).getUpperLevel());

    }

    @Test
    public void clean(){
        controller.Clean();
        Assert.assertEquals(match.getController(),controller);

    }

}