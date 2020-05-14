package Model.Godcards;

import Model.*;

/**
 * Class of GodCard Minotaur
 */
public class Minotaur extends GodCard {

    /**
     * Constructor of the god
     */
    public Minotaur(){
        this.setName("Minotaur");
        this.setPower("You can move in an enemy Worker position (according to normal rules) if the next position in the same direction is available and push him in there");
    }

    /**
     * Implement Minotaur's power
     * @param dest's the destination box
     * @return true or false
     */
    @Override
    public boolean moveOthers(Box dest) {
        Box position = getOwner().getSelectedWorker().getPosition();
        Worker worker = getOwner().getSelectedWorker();
        Worker enemyWorker;
        Box enemyDestination = PlayGround.getInstance().getBox((2*dest.getPosX())-worker.getPosition().getPosX(), (2*dest.getPosY())-worker.getPosition().getPosY());
        for(Player enemy : Game.getInstance().getPlayer()){
            if(!enemy.equals(getOwner()) && !enemy.isLoser()) {
                for (Worker workers : enemy.getWorkers()) {
                    if (workers.getPosition().equals(dest)) {
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
        if((position.getStructure().size()<4 && dest.getStructure().size()>=5) || worker.getOwner().getCard().myVictoryCondition()){
            boolean enemyWinCondition = false;
            for (Player enemy : Game.getInstance().getPlayer()) {
                if (!enemy.equals(worker.getOwner()) && !enemy.isLoser()) {
                    enemyWinCondition = enemyWinCondition || enemy.getCard().enemyVictoryCondition(dest);
                    if(!enemyWinCondition){
                        this.getOwner().setWinner(true);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if you can move an enemy worker
     * @param dest's the destination box
     * @return true or false
     */
    @Override
    public boolean canMoveOthers(Box dest){
        if (getOwner().getSelectedWorker()==null){
            return false;
        }
        Box myWorkerPosition = getOwner().getSelectedWorker().getPosition();
        int pushX = dest.getPosX() - myWorkerPosition.getPosX();
        int pushY = dest.getPosY() - myWorkerPosition.getPosY();
        if( dest.getPosX()+pushX >= 0 && dest.getPosX()+pushX <5 && dest.getPosY()+pushY >= 0 && dest.getPosY()+pushY <5){
            return PlayGround.getInstance().getBox(dest.getPosX() + pushX, dest.getPosY() + pushY).Playable();
        }
        return false;
    }
}
