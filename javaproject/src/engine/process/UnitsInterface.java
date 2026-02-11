package engine.process;

import engine.map.Block;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;




/**
 * 
 * @author LE RAY Yann
 *
 */

public interface UnitsInterface {
	void selectUnit(String type);
	
	void spawnUnit(Block position);
	void spawnUnitEnnemy(Block position);
	
	void unitMovement(Unit displacedUnit);
	void workerMouvement(Worker displacedWorker);

	void workerRessourceDeposit(Worker worker);
	
	Unit scanForEnemy(Unit unit);
	
	void combatSystem(Unit unit1, Unit unit2);
	void calculDegats(Unit unit);
	
	void unitsInSelectedArea();
	void unitMoveOrder(Block destination);
	void moveAllUnits();
}
