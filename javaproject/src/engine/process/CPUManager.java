package engine.process;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import engine.map.Block;
import engine.mobile.CPU;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import log.LoggerUtility;
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;

public class CPUManager implements CPUinterface {
    
    private MobileInterface manager;
    private static Logger logger = LoggerUtility.getLogger(CPUManager.class, "html");
    private Block  pendingHQTarget= null;// used to store the block of the HQ that will be built there
    private RessourceDeposit pendingDeposit = null;// store the deposit that the worker will work on after having built hq
    private Worker assignedBuilder= null; // the worker that has a mission of building the hq

    // two different variable since the cup can build an hq far away while also building another type of building
    private Block  pendingBuildTarget = null; // block where the building will be built
    private String pendingBuildType   = null; // Type of the building
    private Worker pendingBuildWorker = null; // the worker who is building it 
	private int pendingTier;
	private String tier2Produced="CAVALRY";


    public CPUManager(MobileInterface manager) {
        this.setManager(manager);
    }

    /**
     * Method used to make sure the CPU doesn't expand too far away at some point
     * 
     * @param b  block that we want to check if he is not outside the designated zone
     * @return
     */
    private boolean isBlockInCPUZone(Block b, CPU c) {
        if (manager.getMode() == 1) { 
            Block hqPos = null;
            // looking for the hq of the cpu in question so that each cpu expand in a different zone at first
            for (Building build : c.getBuiltBuilding()) {
                if (build instanceof HQ) {
                    hqPos = build.getPosition();
                    break;
                }
            }

            if (hqPos != null) {
                if (hqPos.getColumn() < 50) { 
                    // allocated zone for cpu1
                    return b.getColumn() <= 50 && b.getLine() > 30; 
                } 
                else {
                    // allocated zone for cpu
                    return b.getColumn() > 50 && b.getLine() > 30; 
                }
            }
            return false;
        }
        
        // 1v1(mode==0)
        return b.getColumn() > 70 || (b.getColumn() > 45 && b.getLine() > 40); 
    }

    /**
     * This method is used to decide if an hq has to be built and where based on multiple conditions
     * like the number of HQ, the resources of the CPU and if it has started to harvest it's other RessourceDepostit beforehand
     * 
     * @param c
     */
    public void buildManagement(CPU c) {
        ArrayList<HQ> cpuHQs = new ArrayList<>();
        synchronized(manager.getBuildings()) {
            for (Building b : c.getBuiltBuilding()) {
                if (b instanceof HQ) {
                	cpuHQs.add((HQ) b);
                }
            }
        }
        if (cpuHQs.isEmpty()) return;

        if (cpuHQs.size() >= 4) {
            return; 
        }

        for (HQ hq : cpuHQs) {
            ArrayList<RessourceDeposit> nearDeposits = getTwoNearestDeposits(hq.getPosition());
            int saturatedCount = 0;
            for (RessourceDeposit d : nearDeposits) {
                if (d.getCurrentWorkers() >= d.getMaxWorkers() - 1) saturatedCount++;
            }
            if (!nearDeposits.isEmpty() && saturatedCount == 0) return; 
        }

        if (c.getAmbroisieStock() < 600 || c.getFaithStock() < 500) return;

        if (pendingHQTarget != null && assignedBuilder != null) {
            if (!c.getCreatedUnits().contains(assignedBuilder)) {
                resetBuildMission(); return;
            }
            if (hasHQAtPosition(pendingHQTarget)) {
                resetBuildMission(); return;
            }

            Block wPos = assignedBuilder.getPosition();
            
            if (manager.getDistance(wPos, pendingHQTarget) <= 2) {
                //manager.selectBuilding(BuildingFactory.HQ_BUILDING);
                c.setBuildingToBuildID(BuildingFactory.HQ_BUILDING);
                int result = manager.buildBuilding(pendingHQTarget, 1, c.getFactionName(), c);

                if (result == 1) {
                    logger.info("[CPU] nouveau hq construit");
                    HQ newHQ = null;
                    
                    synchronized(manager.getBuildings()) {
                        for (Building b : manager.getBuildings()) {
                            if (b instanceof HQ 
                                && b.getPosition().getLine() == pendingHQTarget.getLine() 
                                && b.getPosition().getColumn() == pendingHQTarget.getColumn() 
                                && !c.getBuiltBuilding().contains(b)) {
                                
                                c.getBuiltBuilding().add(b);
                                newHQ = (HQ) b;
                                break;
                            }
                        }
                    }
                    if (pendingDeposit != null) {
                        if (newHQ != null) {
                            assignedBuilder.setCurrentHQ(newHQ);
                        }
                        
                        assignedBuilder.setCurrentDeposit(pendingDeposit);
                        assignedBuilder.setRessourceType(pendingDeposit.getType());
                        assignedBuilder.setDestination(pendingDeposit.getPosition());
                        pendingDeposit.setCurrentWorkers(pendingDeposit.getCurrentWorkers() + 1);
                    }
                    resetBuildMission();
                } else {
                    assignedBuilder.setIsWorking(false);
                    resetBuildMission();
                }
            }
            return;
        }

        ArrayList<RessourceDeposit> sortedDeposits = sortDepositsByDistance(manager.getRessourceDeposit(), cpuHQs.get(0).getPosition());
        Block targetHqPos = null;
        RessourceDeposit targetDeposit = null;

        for (RessourceDeposit deposit : sortedDeposits) {
            if (!isBlockInCPUZone(deposit.getPosition(),c)) continue;
            
            boolean isAlreadyClaimed = false;
            for (HQ hq : cpuHQs) {
                if (manager.getDistance(deposit.getPosition(), hq.getPosition()) < 15) {
                    isAlreadyClaimed = true;
                    break;
                }
            }
            if (isAlreadyClaimed) continue; 

            for (int r = 2; r <= 4; r++) {
                for (int dl = -r; dl <= r; dl++) {
                    for (int dc = -r; dc <= r; dc++) {
                        int tLine = deposit.getPosition().getLine() + dl;
                        int tCol  = deposit.getPosition().getColumn() + dc;
                        
                        if (!isInMapBounds(tLine, tCol)) continue;

                        Block candidate = manager.getMap().getBlock(tLine, tCol);
                        
                        if (!hasHQNearby(candidate, 20) && isBlockFreeForBuilding(candidate) && isBlockInCPUZone(candidate,c)) {
                            targetHqPos = candidate;
                            targetDeposit = deposit;
                            break; 
                        }
                    }
                    if (targetHqPos != null) break; 
                }
                if (targetHqPos != null) break; 
            }
            if (targetHqPos != null) break; 
        }
        
        if (targetHqPos == null) return;

        Worker builder = getAvailableWorker(c);
        if (builder == null) return;

        if (builder.getCurrentDeposit() != null) {
            builder.getCurrentDeposit().setCurrentWorkers(builder.getCurrentDeposit().getCurrentWorkers() - 1);
            builder.setCurrentDeposit(null);
        }
        builder.setDestination(targetHqPos);
        builder.setIsWorking(true);

        pendingHQTarget = targetHqPos;
        pendingDeposit  = targetDeposit;
        assignedBuilder = builder;
    }
    /**
    * Method used to search for an available worker to build a building
    * 
    * @return  A Worker that will be tasked with the next construction
    *
    */
    private Worker getAvailableWorker(CPU c) {
    	/* first for loop is used to search for worker who are not working(if they have just been created)
    	 * so that we don't pertub the ressource production of the CPU
    	 */
        for (Unit u : c.getCreatedUnits()) {
            if (u instanceof Worker && !((Worker) u).getIsWorking()) return (Worker) u;
        }
        // since the CPU has to built something anyway if every worker is working we use one that was harvesting resources
        for (Unit u : c.getCreatedUnits()) {
            if (u instanceof Worker) return (Worker) u;
        }
        return null; // return null if there is no worker on the map.
    }
    /**
	 * Method used to build any building that is not an HQ by either checking if a worker already has a mission or
	 * launching another mission for a worker.
	 * 
	 * @param c Instance of the CPU class which is currently used in the game
	 */
    public void otherBuildingsManagement(CPU c) {

        if (pendingBuildTarget != null && pendingBuildWorker != null) { // check if the CPU has a worker with a task

            if (!c.getCreatedUnits().contains(pendingBuildWorker)) {//if the assigned worker is dead we reset the mission
                resetOtherBuildMission();
                return;
            }

            if (manager.getDistance(pendingBuildWorker.getPosition(), pendingBuildTarget) <= 2) {
                c.setBuildingToBuildID(pendingBuildType);
            	manager.selectBuilding(pendingBuildType);//if the worker is close to the building block we choose the building
                
                
                int result = manager.buildBuilding(pendingBuildTarget, pendingTier, c.getFactionName(), c);
                if (pendingBuildType.equals(BuildingFactory.RESEARCH_BUILDING)) {
                	for(ResearchBuilding r : c.getResearchBuildings()) {
                		if(r.getPosition().getLine()==pendingBuildTarget.getLine() && r.getPosition().getColumn()==pendingBuildTarget.getColumn()) {
                			r.setResearchActive1(true);
                			r.setResearchActive2(true);
                		}
                	}
                }

                if (result == 1) {
                	logger.info("[CPU] " + pendingBuildType + " construit en X:" + pendingBuildTarget.getColumn() + " Y:" + pendingBuildTarget.getLine());
                    pendingBuildWorker.setIsWorking(false);
                    resetOtherBuildMission();
                } else { // we free the worker even if the building has not been made ( if he is dead for example)
                    pendingBuildWorker.setIsWorking(false);
                    resetOtherBuildMission();
                }
            }
            return; 
        }

        Worker builder = getAvailableWorker(c); // we try to find an available worker
        if (builder == null) return;

        ArrayList<HQ> cpuHQs = new ArrayList<>();
        synchronized(manager.getBuildings()) {
            for (Building b : c.getBuiltBuilding()) {
                if (b instanceof HQ) cpuHQs.add((HQ) b);//we create an arrayList of HQ so that we can check each HQ for expansion
            }
        }

        if (pendingHQTarget != null) return; // we return if an hq is beaing made at the moment

        for (HQ hq : cpuHQs) {
            int prodCount1 = 0, popCount = 0, towerCount = 0, labCount = 0, prodCount2=0, prodCount3=0;
            // we have to reset and check the number of building each time because one of them could have been destroyed
            
            synchronized(manager.getBuildings()) {
                for (Building b : c.getBuiltBuilding()) {
                    if (manager.getDistance(b.getPosition(), hq.getPosition()) <= 18) {
                    	if (b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==1) prodCount1++;
                    	else if (b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==2) prodCount2++;
                    	else if (b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==3) prodCount3++;
                        else if (b instanceof PopulationBuilding) popCount++;
                        else if (b instanceof DefenseTower) towerCount++;
                        else if (b instanceof ResearchBuilding) labCount++;
                    }
                }
            }
            // The next part of the code is a set of rule for each type building that will check resources 
            //and the number of said type so that the CPU doesn't built the same building infinitely
            if (prodCount1 < 1) {
                if (c.getAmbroisieStock() < 125 || c.getFaithStock() < 125) return; // we stop only if we lack resources
                Block pos = findBuildPositionSpecificallyNear(hq, 3, 9,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.PRODUCER_BUILDING,1);
                    return;
                }
            }
            if (prodCount2 < 2 && c.getCurrentTier()>=2) {
                if (c.getAmbroisieStock() < 250 || c.getFaithStock() < 250) return; // we stop only if we lack resources
                Block pos = findBuildPositionSpecificallyNear(hq, 3, 9,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.PRODUCER_BUILDING,2);
                    return;
                }
            }
            if (prodCount3 < 1 && c.getCurrentTier()>=3) {
                if (c.getAmbroisieStock() < 350 || c.getFaithStock() < 275) return; // we stop only if we lack resources
                Block pos = findBuildPositionSpecificallyNear(hq, 3, 9,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.PRODUCER_BUILDING,3);
                    return;
                }
            }

            if (popCount < 5) {
                if (c.getAmbroisieStock() < 100 || c.getFaithStock() < 100) return; 
                Block pos = findBuildPositionSpecificallyNear(hq, 4, 15,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.POPULATION_BUILDING,1);
                    return;
                }
            }

            if (labCount < 1) {
                if (c.getAmbroisieStock() < 150 || c.getFaithStock() < 150) return; 
                Block pos = findBuildPositionSpecificallyNear(hq, 4, 12,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.RESEARCH_BUILDING,2);
                    return;
                }
            }

            if (towerCount < 4) {
                if (c.getAmbroisieStock() < 150 || c.getFaithStock() < 150) return; 
                Block pos = findBuildPositionSpecificallyNear(hq, 5, 10,c);
                if (pos != null) {
                    launchOtherBuildMission(builder, pos, BuildingFactory.DEFENSE_BUILDING,2);
                    return;
                }
            }
        }
    }
    /**
    * method used to find a block to build a building based on the HQ position and a min and max distance from it
    * 
    * @param hq		  The HQ from which we want to expand
    * @param minDist  Minimum distance so that not every building is stacked againt the HQ
    * @param maxDist  Maximum distance away from the HQ where a building can be built
    * @return       The Block where the Building will be built 
    */
    private Block findBuildPositionSpecificallyNear(HQ hq, int minDist, int maxDist,CPU c) {
        int hqLine = hq.getPosition().getLine();
        int hqCol  = hq.getPosition().getColumn();

        for (int dist = minDist; dist <= maxDist; dist++) {//Used to increase the research zone little by little starting from the minimum distance from the HQ
            for (int dl = -dist; dl <= dist; dl++) {// search line by line
                for (int dc = -dist; dc <= dist; dc++) { // search column by column
                    int line = hqLine + dl;
                    int col  = hqCol  + dc;
                    if (!isInMapBounds(line, col)) continue; // if the cordinates are outside the map we check the next iteration

                    Block candidate = manager.getMap().getBlock(line, col);
                    
                    if (!isBlockInCPUZone(candidate,c)) continue; //if it's not in the expansion zone of the CPU
                    
                    if (isBlockFreeForBuilding(candidate)) { // if there is no other building on this block
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Method used to decide which worker to send on a mission and on what mission(what building to build and where)
     * @param builder
     * @param targetPos
     * @param buildingType
     */
    private void launchOtherBuildMission(Worker builder, Block targetPos, String buildingType, int tier) {
        if (builder.getCurrentDeposit() != null) {
            builder.getCurrentDeposit().setCurrentWorkers(
                    builder.getCurrentDeposit().getCurrentWorkers() - 1); // we remove a worker from the deposit if he has one
            builder.setCurrentDeposit(null);
        }
        builder.setDestination(targetPos);
        builder.setIsWorking(true);
        // we send the worker on a mission based on the Parameters
        pendingBuildTarget = targetPos;
        pendingBuildType   = buildingType;
        pendingBuildWorker = builder;
        pendingTier=tier;
    }

    /**
     * method used to free a worker and reset the mission
     */
    private void resetBuildMission() {
        pendingHQTarget = null;
        pendingDeposit  = null;
        assignedBuilder = null;
    }
    /**
     * method used to free a worker and reset the mission of building something else than an HQ
     */
    private void resetOtherBuildMission() {
        pendingBuildTarget = null;
        pendingBuildType   = null;
        pendingBuildWorker = null;
    }

    private boolean hasHQAtPosition(Block pos) {
        synchronized(manager.getBuildings()) {
            for (Building b : manager.getBuildings()) {
                if (b instanceof HQ
                 && b.getPosition().getLine()   == pos.getLine()
                 && b.getPosition().getColumn() == pos.getColumn()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasHQNearby(Block pos, double radius) {
        synchronized(manager.getBuildings()) {
            for (Building b : manager.getBuildings()) {
                if (b instanceof HQ) {
                    if (manager.getDistance(b.getPosition(), pos) <= radius) return true;
                }
            }
        }
        return false;
    }

    private ArrayList<RessourceDeposit> getTwoNearestDeposits(Block origin) {
        ArrayList<RessourceDeposit> sorted = sortDepositsByDistance( manager.getRessourceDeposit(), origin);
        ArrayList<RessourceDeposit> result = new ArrayList<>();
        for (int i = 0; i < Math.min(2, sorted.size()); i++) {
            result.add(sorted.get(i));
        }
        return result;
    }

    private ArrayList<RessourceDeposit> sortDepositsByDistance(ArrayList<RessourceDeposit> deposits, Block origin) {
        ArrayList<RessourceDeposit> sorted = new ArrayList<>(deposits);
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < sorted.size() - 1; i++) {
                if (manager.getDistance(origin, sorted.get(i).getPosition()) > manager.getDistance(origin, sorted.get(i + 1).getPosition())) {
                    RessourceDeposit tmp = sorted.get(i);
                    sorted.set(i, sorted.get(i + 1));
                    sorted.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
        return sorted;
    }

    private boolean isInMapBounds(int line, int col) {
        return line > 6 && col > 0
                && line < manager.getMap().getLineCount()
                && col < manager.getMap().getColumnCount() - 28;
    }

    private boolean isBlockFreeForBuilding(Block block) {
        synchronized(manager.getBuildings()) {
            for (Building b : manager.getBuildings()) {
                if (manager.getDistance(b.getPosition(), block) < 3) return false;
            }
        }
        
        for (RessourceDeposit r : manager.getRessourceDeposit()) {
            if (manager.getDistance(r.getPosition(), block) < 3) return false;
        }
        
        if (pendingHQTarget != null) {
            if (manager.getDistance(pendingHQTarget, block) < 3) return false;
        }
        if (pendingBuildTarget != null) {
            if (manager.getDistance(pendingBuildTarget, block) < 4) return false;
        }
        
        return true; 
    }

    public void attackReaction(CPU c) {
        for(Unit u : c.getCreatedUnits()) {
            if(u.getIsInCombat()) {
                for(Unit otherUnits : c.getCreatedUnits()) {
                    if(manager.getDistance(u.getPosition(), otherUnits.getPosition())<=(u.getVision()+otherUnits.getVision())*1.4 && !(otherUnits instanceof Worker)) {
                        otherUnits.setDestination(u.getPosition());
                    }
                }
            }
        }
    }

    public void workerProductionManagement(CPU c) {
        int workerCount = 0;
        int hqCount = 0;
        
        for (Unit u : c.getCreatedUnits()) {
            if (u instanceof Worker) workerCount++;
        }
        
        synchronized(manager.getBuildings()) {
            for (Building b : c.getBuiltBuilding()) {
                if (b instanceof HQ) hqCount++;
            }
        }
        
        int maxWorkers = hqCount * 10;
        if (workerCount >= maxWorkers) return;

        c.setWorkerProductionTime(c.getWorkerProductionTime()+1);

        if (c.getWorkerProductionTime() >= 30) {
            boolean workerQueued = false;
            synchronized(manager.getBuildings()) {
                for(Building b: c.getBuiltBuilding()) {
                    if(b instanceof HQ) {
                        HQ hq=(HQ) b;
                        if(hq.getWorkerProducer().getProductionQueue().size() <= 2 && !hq.getIsUnderConstruction()) {
                            manager.addQueue(hq.getWorkerProducer(), hq.getPosition(), "WORKER", c);
                            workerQueued = true;
                        }
                    }
                }
            }
            if (workerQueued) {
                c.setWorkerProductionTime(0);
            }
        }
    }

    public void workerManagement(CPU c) {
        for(Unit u: c.getCreatedUnits()) {
            if(u instanceof Worker) {
                Worker w=(Worker) u;
                if(w.getCurrentDeposit()==null && !w.getIsWorking()) {
                    double minDistance=Double.MAX_VALUE;
                    RessourceDeposit workerDeposit=null;
                    for(RessourceDeposit r: manager.getRessourceDeposit()) {
                        if (!isBlockInCPUZone(r.getPosition(),c)) continue;
                        
                        if(manager.getDistance(r.getPosition(), w.getPosition())<minDistance && r.getMaxWorkers()>r.getCurrentWorkers()) {
                            minDistance=manager.getDistance(r.getPosition(), u.getPosition());
                            workerDeposit=r;
                        }
                    }
                    if(workerDeposit!=null) {
                        w.setDestination(workerDeposit.getPosition());
                        w.setCurrentDeposit(workerDeposit);
                        workerDeposit.setCurrentWorkers(workerDeposit.getCurrentWorkers()+1);
                        w.setRessourceType(workerDeposit.getType());
                        w.setIsWorking(true);
                        
                        HQ nearestHQ = null;
                        double minHQDist = Double.MAX_VALUE;
                        synchronized(manager.getBuildings()) {
                            for (Building b : c.getBuiltBuilding()) {
                                if (b instanceof HQ) {
                                    double dist = manager.getDistance(b.getPosition(), workerDeposit.getPosition());
                                    if (dist < minHQDist) {
                                        minHQDist = dist;
                                        nearestHQ = (HQ) b;
                                    }
                                }
                            }
                        }
                        if (nearestHQ != null) {
                            w.setCurrentHQ(nearestHQ);
                        }
                    }
                }
            }
        }
    }

    public void militaryProductionManagement(CPU c) {               
        int armyCount = 0;
        for (Unit u : c.getCreatedUnits()) {
            if (!(u instanceof Worker)) {
                armyCount++;
            }
        }
        
        int hqCount = manager.countBuildingType(c, HQ.class);
        int maxArmy = 10 + (hqCount * 15); 
        
        if (armyCount >= maxArmy) { // it means we don't need to create more army.
            return;
        }

        int safeAmbroisie = 200; // check if we have enough resources to no block the other actions of the CPU
        int safeFaith = 200;
        if (hqCount < 4) {
            safeAmbroisie = 600; 
            safeFaith = 500;
        } 
        else if (pendingHQTarget != null) {
            safeAmbroisie = 650; 
            safeFaith = 550;
        }

        synchronized(manager.getBuildings()) {
            for(Building b : c.getBuiltBuilding()) {
                if(b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==1) {
                    UnitProducer producer = (UnitProducer) b;
                    
                    if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                        if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                            
                            if(c.getAmbroisieStock() >= safeAmbroisie && c.getFaithStock() >= safeFaith) {
                                String unitToProduce = "INFANTRY"; 
                                
                                if (c.getFactionName().equalsIgnoreCase("ZEUS")) {
                                    unitToProduce = "ARTILLERY"; 
                                }

                                manager.addQueue(producer, producer.getPosition(), unitToProduce, c); 
                                armyCount++; 
                                if (armyCount >= maxArmy) break;
                            }
                        }
                    }
                }if(b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==2) {
                	UnitProducer producer = (UnitProducer) b;
                	if(tier2Produced.equalsIgnoreCase("cavalry")) {
                		if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                			if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                				if(c.getAmbroisieStock() >= safeAmbroisie && c.getFaithStock() >= safeFaith) {
                					String unitToProduce = "ARTILLERY";
                					tier2Produced="ARTILLERY";

                					if (c.getFactionName().equalsIgnoreCase("ZEUS")) {
                						unitToProduce = "INFANTRY"; 
                						tier2Produced="INFANTRY";
                					}

                					manager.addQueue(producer, producer.getPosition(), unitToProduce, c); 
                					armyCount++; 
                					if (armyCount >= maxArmy) break;
                				}
                			}
                		}
                	}else if(tier2Produced.equalsIgnoreCase("INFANTRY") || tier2Produced.equalsIgnoreCase("ARTILLERY") ) {
                		if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                			if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                				if(c.getAmbroisieStock() >= safeAmbroisie && c.getFaithStock() >= safeFaith) {
                					String unitToProduce = "CAVALRY";
                					tier2Produced="CAVALRY";
                					manager.addQueue(producer, producer.getPosition(), unitToProduce, c); 
                					armyCount++; 
                					if (armyCount >= maxArmy) break;
                				}
                			}
                		}
                	}
                }
                if(b instanceof UnitProducer && !(b instanceof HQ) && b.getTierLevel()==3) {
                	UnitProducer producer = (UnitProducer) b;
                	if(tier2Produced.equalsIgnoreCase("cavalry")) {
                		if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                			if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                				if(c.getAmbroisieStock() >= safeAmbroisie && c.getFaithStock() >= safeFaith) {
                					String unitToProduce = "INFANTRY";
                					tier2Produced="INFANTRY";

                					if (c.getFactionName().equalsIgnoreCase("HADES")) {
                						unitToProduce = "ARTILLERY"; 
                						tier2Produced="ARTILLERY";
                					}

                					manager.addQueue(producer, producer.getPosition(), unitToProduce, c); 
                					armyCount++; 
                					if (armyCount >= maxArmy) break;
                				}
                			}
                		}
                	}else if(tier2Produced.equalsIgnoreCase("INFANTRY") || tier2Produced.equalsIgnoreCase("ARTILLERY") ) {
                		if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                			if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                				if(c.getAmbroisieStock() >= safeAmbroisie && c.getFaithStock() >= safeFaith) {
                					String unitToProduce = "CAVALRY";
                					tier2Produced="CAVALRY";
                					manager.addQueue(producer, producer.getPosition(), unitToProduce, c); 
                					armyCount++; 
                					if (armyCount >= maxArmy) break;
                				}
                			}
                		}
                	}
                }
            }
        }
    }
    public void attack(CPU c) {
    	double minDistance = Double.MAX_VALUE; 
    	Block attackTarget=null;
    	for( Building b : manager.getBuildings()) {
    		if(b instanceof HQ) {
    			if(manager.getPlayer().getBuiltBuilding().contains(b)) {
    				double distance=manager.getDistance(c.getBuiltBuilding().get(0).getPosition(), b.getPosition());
    				if(distance<minDistance){
    					minDistance=distance;
    					attackTarget=b.getPosition();
    				}
    			}
    		}
    	}if(attackTarget==null) {
    		for( Building b : manager.getBuildings()) {
    			if(manager.getPlayer().getBuiltBuilding().contains(b)) {
					attackTarget=b.getPosition();
    			}
    		}
    	}
    	for(Unit u : c.getCreatedUnits()) {
    		if(!(u instanceof Worker)) {
        		u.setDestination(attackTarget);
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