package Controller;

import Model.Box;
import Model.Game;
import Model.PlayGround;
import Model.Worker;
import View.cli.Cli;

import java.util.Scanner;

public class Controller {
    private boolean iscli=true;
    private boolean isgui;
    private Cli cli;

    public Controller(){
        iscli=true; //scelta gui
        cli = new Cli();
    }

    public boolean legalCoordinates(int i, int j){
        if (iscli){
           return MessageHandlerCLI.legalCoordinates(i, j);
        }
        return true; //else gui
    }

    public Box askForCoordinates() {
        if (iscli){
            return MessageHandlerCLI.askForCoordinates();
        }
        return null; //else gui
    }

    public boolean askForPower(){
        if (iscli){
            return MessageHandlerCLI.askForPower();
        }
        return true; //else gui
    }

    public Worker askForWorker(){
        if (iscli){
            return MessageHandlerCLI.askForWorker();
        }
        return null; //else gui
    }

    public Box askForMovement(){
        if (iscli){
            return MessageHandlerCLI.askForMovement();
        }
        return null; //else gui
    }


    public Box askForBuilding(){
        if (iscli){
            return MessageHandlerCLI.askForBuilding();
        }
        return null; //else gui
    }

    public void notValidDestination(){
        if (iscli){
            MessageHandlerCLI.notValidDestination();
            return;
        }
        return; //else gui
    }

    public Box askForPlacement(int workerNumber){
        if (iscli){
            return MessageHandlerCLI.askForPlacement(workerNumber);
        }
        return null; //else gui
    }

    public void lose(){
        if (iscli){
            MessageHandlerCLI.lose();
        }
        //else gui
    }

    public void gameFinished(){
        if (iscli){
            MessageHandlerCLI.gameFinished();
        }
    }

    public void Hall(Cli cli){
        MessageHandlerCLI.Hall(cli);
    }

    public static void main(String[] args) {
        Game myGame = Game.getInstance();
        Controller controller = new Controller();
        controller.Hall(controller.getCli());
    }


    public void ShowMapRequest(){
        if (iscli){
            cli.ShowMap();
        }
    }

    public void ShowLegalMovementRequest(){
        if (iscli){
            cli.ShowAvailableMovement();
        }
    }

    public void ShowLegalBuildingRequest(){
        if (iscli){
            cli.ShowAvailableBuilding();
        }
    }

    public void TurnStart(){
        if (iscli){
            MessageHandlerCLI.TurnStartMessage();
        }
    }

    public Cli getCli() {
        return cli;
    }
}
