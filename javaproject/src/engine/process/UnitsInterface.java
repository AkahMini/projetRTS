package engine.process;

import engine.map.Block;
import engine.mobile.MobileElement;
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
}
