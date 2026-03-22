package engine.process;


import java.util.ArrayList;
import java.util.Iterator;

import engine.map.Block;
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
	public void buildManagement(CPU c) {
		int closeDeposit=0;
		RessourceDeposit r=null;
		Iterator<RessourceDeposit> it= manager.getRessourceDeposit().iterator();
		while(it.hasNext()) {
			r=it.next();
			if(r.getCurrentWorkers()==r.getMaxWorkers()) {
				closeDeposit+=1;
				if(closeDeposit>=2 && it.hasNext()) {
					RessourceDeposit dest=it.next();
					int i=0;
					ArrayList<Unit> array=c.getCreatedUnits();
					Unit w=null;
					while(array.get(i)!=null & w==null && i<array.size()-1) {
						i+=1;
						if(array.get(i) instanceof Worker) {
							if(((Worker) array.get(i)).getIsWorking()==false) {
								w=array.get(i);
							}
						}
					}
					if(w!=null) {
						Block destination=new Block(dest.getPosition().getLine()+1,dest.getPosition().getColumn()+1);
						w.setDestination(destination);;
						if(w.getDestination()==w.getPosition()) {
							manager.selectBuilding(BuildingFactory.HQ_BUILDING);
							manager.buildBuilding(w.getPosition(), 1, c.getFactionName(), c);
							continue;
						}
					}

				}
			}
		}
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
						w.setCurrentDeposit(workerDeposit);
						workerDeposit.setCurrentWorkers(workerDeposit.getCurrentWorkers()+1);
						w.setRessourceType(workerDeposit.getType());
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
