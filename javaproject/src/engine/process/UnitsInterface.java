package engine.process;

import engine.map.Block;
<<<<<<< HEAD
import engine.mobile.Player;
=======
import engine.mobile.MobileElement;
>>>>>>> 8c15c020996e37655fdafc20c352c6f10328c56c
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;




/**
 * 
 * Interface used for the UnitManager Class.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */

public interface UnitsInterface {
	void selectUnit(String type);
	
	void spawnUnit(Block position);
	void spawnUnitEnnemy(Block position);
	
	void unitMovement(Unit displacedUnit);
	void workerMouvement(Worker displacedWorker);

	void workerRessourceDeposit(Worker worker);
	
	MobileElement scanForEnemy(Unit unit);
	
	void combatSystem(Unit unit1, MobileElement target);
	void damageCalculation(Unit unit);
	
	void unitsInSelectedArea();
	void unitMoveOrder(Block destination);
	void moveAllUnits();
	
	void workerConstructionction(String button, Player p, Worker worker);
}
