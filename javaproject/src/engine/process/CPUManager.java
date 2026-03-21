package engine.process;

import engine.mobile.CPU;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.unit.Unit;

public class CPUManager implements CPUinterface {
	
	private MobileInterface manager;

	public CPUManager(MobileInterface manager) {
		this.setManager(manager);
	}
	public void attackReaction(CPU c) {
		for(Unit u : c.getCreatedUnits()) {
			if(u.getIsInCombat()) {
				for(Unit otherUnits : c.getCreatedUnits()) {
					if(manager.getDistance(u.getPosition(), otherUnits.getPosition())<=(u.getVision()+otherUnits.getVision())*2) {
						otherUnits.setDestination(u.getPosition());
					}
				}
			}
		}
	}
	public void workerManagement(CPU c) {
		for(Building b: c.getBuiltBuilding()) {
			if(b instanceof HQ) {
				HQ hq=(HQ) b;
				if(hq.getWorkerProducer().getProductionQueue().size()<2) {
					manager.addQueue(hq.getWorkerProducer(), hq.getPosition(), "WORKER", c);
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
