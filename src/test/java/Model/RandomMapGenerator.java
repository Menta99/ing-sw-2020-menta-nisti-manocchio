package Model;

import CommunicationProtocol.*;
import CommunicationProtocol.SantoriniInfo.*;
import Controller.Controller;
import View.CLI.Cli;
import VirtualView.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class RandomMapGenerator {
    Game match;
    PlayGround field;
    Player player_1, player_2, player_3;
    Worker worker_1_a, worker_1_b, worker_2_a, worker_2_b, worker_3_a, worker_3_b;
    VirtualView virtual;
    Controller controller;
    Cli cli;
    CommandMsg msg;
    Random rand = new Random();
    int i,j,k;

    @Before
    public void setUp(){
        Game.getInstance().CleanGame();
        controller = new Controller();
        match = Game.getInstance();
        field = PlayGround.getInstance();
        cli = new Cli();
        virtual = controller.getVirtual();
        ArrayList<Integer> list = new ArrayList<>();
        i = rand.nextInt(14);
        list.add(i);
        j = rand.nextInt(14);
        while (i == j){
            j = rand.nextInt(14);
        }
        list.add(j);
        k = rand.nextInt(14);
        while (i == k || j == k){
            k = rand.nextInt(14);
        }
        list.add(k);
        player_1 = new Player("MyName1");
        player_2 = new Player("MyName2");
        player_3 = new Player("MyName3");
        worker_1_a = player_1.getWorkers().get(0);
        worker_1_b = player_1.getWorkers().get(1);
        worker_2_a = player_2.getWorkers().get(0);
        worker_2_b = player_2.getWorkers().get(1);
        worker_3_a = player_3.getWorkers().get(0);
        worker_3_b = player_3.getWorkers().get(1);
        match.ExtractCard(list);
        player_1.ChooseGod(0);
        player_2.ChooseGod(1);
        player_3.ChooseGod(2);
    }

    @After
    public void tearDown(){
        match.CleanGame();
    }

    @Test
    public void Exe(){
        CommandMsg msg;
        for (int i = 0; i < 50; i++){
            GenerateRandomField();
            System.out.println("PLAYING WITH: \n" + match.getActiveCards().get(0).getName() + "\n" + match.getActiveCards().get(1).getName() + "\n" + match.getActiveCards().get(2).getName());
            msg = new CommandMsg(CommandType.UPDATE_ACTION, new Info(virtual.MapInfo(true, false)));
            cli.UpdateHandler(msg);
            msg = new CommandMsg(CommandType.UPDATE_ACTION, new Info(virtual.MapInfo(false, false)));
            cli.UpdateHandler(msg);
            msg = new CommandMsg(CommandType.UPDATE_ACTION, new Info(virtual.MapInfo(false, true)));
            cli.UpdateHandler(msg);
            tearDown();
            setUp();
        }
    }

    public void GenerateRandomField(){
        Box box;
        int j = 0;
        while (j < 20){
            box = field.getBox(rand.nextInt(5), rand.nextInt(5));
            if(!box.isOccupied()) {
                box.Build();
                j++;
            }
        }
        while (!worker_1_a.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        while (!worker_1_b.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        while (!worker_2_a.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        while (!worker_2_b.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        while (!worker_3_a.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        while (!worker_3_b.setInitialPosition(field.getBox(rand.nextInt(5), rand.nextInt(5)))){
        }
        match.setActualPlayer(match.getPlayer().get(rand.nextInt(3)));
        match.getActualPlayer().setSelectedWorker(match.getActualPlayer().getWorkers().get(rand.nextInt(2)));

    }
}