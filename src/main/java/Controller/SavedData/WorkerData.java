package Controller.SavedData;

import Model.Box;
import Model.Game;
import Model.Player;
import Model.Worker;
import View.Colors;

public class WorkerData {
    private String owner;
    private int ownerIndex;
    private boolean didClimb;
    private boolean didBuild;
    private boolean didMove;
    private int posX;
    private int posY;
    private int lastX;
    private int lastY;

    public WorkerData(String owner){
        this.owner=owner;
        int i=0;
        for (Player player : Game.getInstance().getPlayer()){
            if (player.getNickName().equals(owner)){
                ownerIndex = i;
            }
            i++;
        }
    }

    public void update(int workerNumber){
        Player owner = Game.getInstance().getPlayer().get(ownerIndex);
        Worker worker = owner.getWorkers().get(workerNumber);
        if (worker.isState()) {
            Box position = worker.getPosition();
            posX = position.getPosX();
            posY = position.getPosY();
            Box lastPosition = worker.getLastPosition();
            if (lastPosition != null){
                lastX = lastPosition.getPosX();
                lastY = lastPosition.getPosY();
            }
            else {
                lastX=-1;
                lastY=-1;
            }
            didBuild = worker.isDidBuild();
            didClimb = worker.getDidClimb();
            didMove = worker.isMoved();
        }
        else {
            posX = -1;
            posY = -1;
            lastX = -1;
            lastY = -1;
            didBuild = false;
            didClimb = false;
            didMove = false;
        }
    }

    @Override
    public String toString(){
        String string = "";
        string = string.concat(didMove ? "1\n" : "0\n");
        string = string.concat(didClimb ? "1\n" : "0\n");
        string = string.concat(didBuild ? "1\n" : "0\n");
        string = string.concat(posX + "\n" + posY + "\n" + lastX + "\n" + lastY + "\n");
        return string;
    }

    public static int Size(){
        return 7;
    }
}
