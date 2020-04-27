package Model.Godcards;

import Model.Box;
import Model.Game;
import Model.Player;
import Model.Worker;

/**
 * Class of GodCard Apollo
 */
public class Apollo extends  GodCard{

    /**
     * Constructor of the god
     */
    public Apollo(){
        this.setName("Apollo");
        this.setPower("You can move in an enemy worker's position (according to normal rules) and force him to occupy your initial position");
    }

    /**
     * Change your position with an enemy one(if's legal)
     * @param dest's your destination box
     * @return true or false
     */
    @Override
    public boolean moveOthers(Box dest) {
        Box position = getOwner().getSelectedWorker().getPosition();
        Worker worker = getOwner().getSelectedWorker();
        Worker enemyWorker = null;
        for(Player enemy : Game.getInstance().getPlayer()){
            if (!enemy.isLoser()) {
                for (Worker workers : enemy.getWorkers()) {
                    if (workers.getPosition().equals(dest)) {  //Trova l'operaio nemico nella casella di destinazione
                        enemyWorker = workers;
                        dest.getStructure().remove(enemyWorker);
                    }
                }
            }
        }
        if (dest.getStructure().size()>=position.getStructure().size()){
            worker.setDidClimb(true);
        }
        worker.setLastPosition(position);
        worker.setPosition(dest);
        position.getStructure().remove(worker);
        dest.getStructure().add(worker);
        worker.setMoved(true);
        enemyWorker.setPosition(position);
        position.getStructure().add(enemyWorker);
        if((position.getStructure().size()<=4 && dest.getStructure().size()>=5) || worker.getOwner().getCard().myVictoryCondition()){//Parto da un qualsiasi piano minore del terzo e arrivo in un terzo piano non occupato oppure occupato ma posso spingere l'avversario
            boolean enemyWinCondition = false;
            for (Player enemy : Game.getInstance().getPlayer()) {
                if (!enemy.equals(worker.getOwner()) && !enemy.isLoser()) {
                    enemyWinCondition = enemyWinCondition || enemy.getCard().enemyVictoryCondition(dest); //CHECKS RESTRIZIONI GODS NEMICI
                    if(!enemyWinCondition){
                        this.getOwner().setWinner(true);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canMoveOthers(Box dest){ return true; }
}
