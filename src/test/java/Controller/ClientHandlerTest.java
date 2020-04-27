package Controller;

import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.CommandType;
import ComunicationProtocol.ServerMsg;
import Model.Game;
import Model.Player;
import Server.ClientHandler;
import Server.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandlerTest extends ClientHandler implements Runnable {
    private int playerNum=0;
    private String nickName;
    private Player player;
    Scanner help;
    CommandType lastmsg=null;
    FileReader file;
    BufferedReader reader;
    File test;
    ArrayList<String> list2;
    int indice=0;

    public ClientHandlerTest(int playerNum,String path){
        super();
        this.playerNum = playerNum;
        Scanner s = null;
        try {
            s = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        list2 = new ArrayList<String>();
        while (s.hasNextLine()){
            list2.add(s.nextLine());
        }
        s.close();
        /*for (String line : list2){
            System.out.println(line);
        }*/
        NickName();
    }

    @Override
    public void run() {
        while (true){
            //do stuff
        }
    }

    public void WriteMessage(CliCommandMsg msg){
            lastmsg=msg.getCommandType();
            for (String line : msg.getMsg()){
                System.out.println(line);
            }

    }

    public ServerMsg ReadMessage()  {
      ServerMsg msg=null;
            switch (lastmsg){
                case COMMUNICATION:
                    break;
                case CLOSE:
                    break;
                case NUMBER:
                    ArrayList<Integer> numbers =new ArrayList<>();
                    int x=Integer.parseInt(list2.get(indice));
                    indice++;
                    numbers.add(x);
                    msg= new ServerMsg(numbers);
                    break;
                case GOD:
                    int j=-1;
                    ArrayList<Integer> list=new ArrayList<>();
                   for (int i=0;i<Game.getInstance().getController().getHandlers().size();i++){
                           j=Integer.parseInt(list2.get(indice));
                           indice++;
                           list.add(j);
                   }
                   msg= new ServerMsg(list);
                   break;
                case NAME:
                    String a= null;
                    a = list2.get(indice);
                    indice++;
                    msg= new ServerMsg(a);
                    break;
                case ANSWER:
                    String b= null;
                    b=list2.get(indice);
                    indice++;
                    msg= new ServerMsg(b);
                    break;
                case UPDATE:
                    break;
                case COORDINATES:
                    int y=-1;
                    ArrayList<Integer> list3=new ArrayList<>();
                    for (int i=0;i<2;i++){
                        y=Integer.parseInt(list2.get(indice));
                        indice++;
                        list3.add(y);
                    }
                msg= new ServerMsg(list3);
                break;
            }
            return msg;
    }

    public void NickName(){
        CliCommandMsg msg = new CliCommandMsg(CommandType.NAME, "What's your NickName?");
        WriteMessage(msg);
        ServerMsg answer = null;
        answer = ReadMessage();
        nickName = answer.getMsg();
        player = new Player(nickName);
        player.setView(answer.isView());
    }

    public String getNickName() {
        return nickName;
    }

    public Player getPlayer() {
        return player;
    }



}


