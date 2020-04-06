package Model;

public class Apollo extends  GodCard{

    public Apollo(){
        this.setName("Apollo");
        //this.setPower("La tua mossa: il tuo operaio può spostarsi nella casella di un operaio avversario (in base alle normali regole di movimento) e costringerlo a occupare la casella appena liberata, scambiando le posizioni.");
    }

    @Override
    public boolean moveOthers(Box dest) {
        Box position = getOwner().getSelectedWorker().getPosition();
        Worker worker = getOwner().getSelectedWorker();
        Worker enemyWorker = null;
        for(Player enemy : Game.getInstance().getPlayer()){
            for(Worker workers : enemy.getWorkers()){
                if (workers.getPosition().equals(dest)){  //Trova l'operaio nemico nella casella di destinazione
                    enemyWorker = workers;
                }
            }
        }
        if (dest.getStructure().size()>=position.getStructure().size()){
            worker.setDidClimb(true);
        }
        worker.setLastPosition(position);
        worker.setPosition(dest);
        worker.setMoved(true);
        enemyWorker.setPosition(position);
        if((position.getStructure().size()<=4 && dest.getStructure().size()>=5) || worker.getOwner().getCard().myVictoryCondition()){//Parto da un qualsiasi piano minore del terzo e arrivo in un terzo piano non occupato oppure occupato ma posso spingere l'avversario
            boolean enemyWinCondition = false;
            for (Player enemy : Game.getInstance().getPlayer()) {
                if (!enemy.equals(worker.getOwner())) {
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
