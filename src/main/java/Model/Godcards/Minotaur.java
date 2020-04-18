package Model.Godcards;

import Model.*;

public class Minotaur extends GodCard {

    public Minotaur(){
        this.setName("Minotaur");
        this.setPower("You can move in an enemy Worker position (according to normal rules) if the next position in the same direction is available and push him in there");
    }
    @Override
    public boolean moveOthers(Box dest) {
        Box position = getOwner().getSelectedWorker().getPosition();
        Worker worker = getOwner().getSelectedWorker();
        Worker enemyWorker = null;
        Box enemyDestination = PlayGround.getInstance().getBox((2*dest.getPosX())-worker.getPosition().getPosX(), (2*dest.getPosY())-worker.getPosition().getPosY());
        for(Player enemy : Game.getInstance().getPlayer()){
            if(!enemy.equals(getOwner()) && !enemy.isLoser()) {
                for (Worker workers : enemy.getWorkers()) {
                    if (workers.getPosition().equals(dest)) {  //Trova l'operaio nemico nella casella di destinazione
                        enemyWorker = workers;
                        dest.getStructure().remove(enemyWorker);
                        enemyDestination.getStructure().add(enemyWorker);
                        enemyWorker.setPosition(enemyDestination);
                        enemyDestination.setOccupied(true);
                    }
                }
            }
        }
        if (dest.getStructure().size()>=position.getStructure().size()){
            worker.setDidClimb(true);
        }
        worker.setLastPosition(position);
        worker.setPosition(dest);
        worker.setMoved(true);
        position.getStructure().remove(worker);
        position.setOccupied(false);
        dest.getStructure().add(worker);
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
    public boolean canMoveOthers(Box dest){
        if (getOwner().getSelectedWorker()==null){
            return false;
        }
        Box myWorkerPosition = getOwner().getSelectedWorker().getPosition();
        int pushX = dest.getPosX() - myWorkerPosition.getPosX();
        int pushY = dest.getPosY() - myWorkerPosition.getPosY();
        if( dest.getPosX()+pushX >= 0 && dest.getPosY()+pushX <5 && dest.getPosY()+pushY >= 0 && dest.getPosY()+pushY <5){
            if( PlayGround.getInstance().getBox(dest.getPosX()+pushX, dest.getPosY()+pushY).Playable()){
                return true;
            }
        }
        return false;
    }
}
