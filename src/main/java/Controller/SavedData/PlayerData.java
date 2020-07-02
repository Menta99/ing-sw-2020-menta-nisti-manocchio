package Controller.SavedData;

import Model.Game;
import Model.Player;

public class PlayerData {

    private String ID;
    private String godCard;
    private boolean loser;
    private WorkerData[] workers;

    public PlayerData(String ID, String godCard){
        this.ID = ID;
        this.godCard = godCard;
        workers = new WorkerData[2];
        workers[0] = new WorkerData(ID);
        workers[1] = new WorkerData(ID);
        loser = false;
    }

    public void update() {
        if (!loser) {
            for (Player player : Game.getInstance().getPlayer()) {
                if (player.getNickName().equals(ID)) {
                    loser = player.isLoser();
                    for(int i = 0; i < 2; i++){
                        workers[i].update(i);
                    }
                }
            }
        }
    }

    @Override
    public String toString(){
        String string = "";
        string = string.concat(((loser) ? "1\n" : "0\n") + ID + "\n" + godCard + "\n");
        string = string.concat(workers[0].toString() + workers[1].toString());
        return string;
    }

    public static int Size(){
        return 3;
    }
}
