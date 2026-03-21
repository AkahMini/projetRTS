package engine.process;


import engine.mobile.CPU;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;

public class CPUManager implements CPUinterface {
	
	private MobileInterface manager;

	public CPUManager(MobileInterface manager) {
		this.setManager(manager);
	}
	public void attackReaction(CPU c) {
		for(Unit u : c.getCreatedUnits()) {
			if(u.getIsInCombat()) {
				for(Unit otherUnits : c.getCreatedUnits()) {
					if(manager.getDistance(u.getPosition(), otherUnits.getPosition())<=(u.getVision()+otherUnits.getVision())*1.4 & !(otherUnits instanceof Worker)) {
						otherUnits.setDestination(u.getPosition());
					}
				}
			}
		}
	}
	public void workerProductionManagement(CPU c) {
		for(Building b: c.getBuiltBuilding()) {
			if(b instanceof HQ) {
				HQ hq=(HQ) b;
				c.setWorkerProductionTime(c.getWorkerProductionTime()+1);
				if(hq.getWorkerProducer().getProductionQueue().size()<=2 & !hq.getIsUnderConstruction() & c.getWorkerProductionTime()>=9) {
					manager.addQueue(hq.getWorkerProducer(), hq.getPosition(), "WORKER", c);
					c.setWorkerProductionTime(0);
				}
			}
		}
	}
	public void workerManagement(CPU c) {
		for(Unit u: c.getCreatedUnits()) {
			if(u instanceof Worker) {
				Worker w=(Worker) u;
				if(w.getCurrentDeposit()==null) {
					double minDistance=Double.MAX_VALUE;
					RessourceDeposit workerDeposit=null;
					for(RessourceDeposit r: manager.getRessourceDeposit()) {
						if(manager.getDistance(r.getPosition(), w.getPosition())<minDistance & r.getMaxWorkers()>r.getCurrentWorkers()) {
							minDistance=manager.getDistance(r.getPosition(), u.getPosition());
							workerDeposit=r;
						}
					}
					if(workerDeposit!=null) {
						w.setDestination(workerDeposit.getPosition());
					}
				}
			}
		}
	}
	public MobileInterface getManager() {
		return manager;
	}

	public void setManager(MobileInterface manager) {
		this.manager = manager;
	}
}
