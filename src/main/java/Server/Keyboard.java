package Server;

import java.util.Scanner;

public class Keyboard implements Runnable{

    @Override
    public void run() {
        Scanner keyboard = new Scanner(System.in);
        String msg = keyboard.nextLine();
        while (!msg.equalsIgnoreCase("close")){
            msg = keyboard.nextLine();
        }
        Server.setActive(false);
        Server.UpdateServer();
    }
}
