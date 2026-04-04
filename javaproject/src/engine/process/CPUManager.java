package engine.process;

import java.util.ArrayList;

import engine.map.Block;
import engine.mobile.CPU;
import engine.mobile.RessourceDeposit;
import engine.mobile.building.Building;
import engine.mobile.building.DefenseTower;
import engine.mobile.building.HQ;
import engine.mobile.unit.Unit;
import engine.mobile.unit.Worker;
import engine.mobile.building.PopulationBuilding;
import engine.mobile.building.ResearchBuilding;
import engine.mobile.building.UnitProducer;

public class CPUManager implements CPUinterface {
    
    private MobileInterface manager;

    private Block  pendingHQTarget= null;// used to store the block of the HQ that will be built there
    private RessourceDeposit pendingDeposit = null;// store the deposit that the worker will work on after having built hq
    private Worker assignedBuilder= null; // the worker that has a mission of building the hq

    // two different variable since the cup can build an hq far away while also building another type of building
    private Block  pendingBuildTarget = null; // block where the building will be built
    private String pendingBuildType   = null; // Type of the building
    private Worker pendingBuildWorker = null; // the worker who is building it 

    public CPUManager(MobileInterface manager) {
        this.setManager(manager);
    }

    private boolean isBlockInCPUZone(Block b) {
        return b.getColumn() > 65 || (b.getColumn() > 40 && b.getLine() > 40); 
    }

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
                if (d.getCurrentWorkers() >= d.getMaxWorkers()) saturatedCount++;
            }
            if (saturatedCount < 2) return; 
        }

        if (c.getAmbroisieStock() < 500 || c.getFaithStock() < 500) return;

        if (pendingHQTarget != null && assignedBuilder != null) {
            if (!c.getCreatedUnits().contains(assignedBuilder)) {
                resetBuildMission(); return;
            }
            if (hasHQAtPosition(pendingHQTarget)) {
                resetBuildMission(); return;
            }

            Block wPos = assignedBuilder.getPosition();
            if (wPos.getLine() == pendingHQTarget.getLine() && wPos.getColumn() == pendingHQTarget.getColumn()) {
                manager.selectBuilding(BuildingFactory.HQ_BUILDING);
                int result = manager.buildBuilding(pendingHQTarget, 1, c.getFactionName(), c);

                if (result == 1) {
                    System.out.println("[CPU] nouveau hq construit");
                    synchronized(manager.getBuildings()) {
                        for (Building b : manager.getBuildings()) {
                            if (b instanceof HQ && b.getPosition().equals(pendingHQTarget) && !c.getBuiltBuilding().contains(b)) {
                                c.getBuiltBuilding().add(b);
                                break;
                            }
                        }
                    }
                    if (pendingDeposit != null) {
                        assignedBuilder.setCurrentDeposit(pendingDeposit);
                        assignedBuilder.setRessourceType(pendingDeposit.getType());
                        assignedBuilder.setDestination(pendingDeposit.getPosition());
                        pendingDeposit.setCurrentWorkers(pendingDeposit.getCurrentWorkers() + 1);
                    }
                    resetBuildMission();
                }
            }
            return;
        }

        ArrayList<RessourceDeposit> sortedDeposits = sortDepositsByDistance(manager.getRessourceDeposit(), cpuHQs.get(0).getPosition());
        Block targetHqPos = null;
        RessourceDeposit targetDeposit = null;

        for (RessourceDeposit deposit : sortedDeposits) {
            if (!isBlockInCPUZone(deposit.getPosition())) continue;
            
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
                        
                        if (!hasHQNearby(candidate, 20) && isBlockFreeForBuilding(candidate) && isBlockInCPUZone(candidate)) {
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

    private Worker getAvailableWorker(CPU c) {
        for (Unit u : c.getCreatedUnits()) {
            if (u instanceof Worker && !((Worker) u).getIsWorking()) return (Worker) u;
        }
        for (Unit u : c.getCreatedUnits()) {
            if (u instanceof Worker) return (Worker) u;
        }
        return null;
    }

    public void otherBuildingsManagement(CPU c) {

        if (pendingBuildTarget != null && pendingBuildWorker != null) {

            if (!c.getCreatedUnits().contains(pendingBuildWorker)) {
                resetOtherBuildMission();
                return;
            }

            if (pendingBuildWorker.getPosition().equals(pendingBuildTarget)) {
                manager.selectBuilding(pendingBuildType);
                
                int tierToBuild = 1; 
                if (pendingBuildType.equals(BuildingFactory.DEFENSE_BUILDING) || 
                    pendingBuildType.equals(BuildingFactory.RESEARCH_BUILDING)) {
                    tierToBuild = 2; 
                }
                
                int result = manager.buildBuilding(pendingBuildTarget, tierToBuild, c.getFactionName(), c);

                if (result == 1) {
                    System.out.println("[CPU] " + pendingBuildType + " construit en X:" + pendingBuildTarget.getColumn() + " Y:" + pendingBuildTarget.getLine());
                    
                    if (pendingBuildType.equals(BuildingFactory.POPULATION_BUILDING)) {
                        synchronized(manager.getBuildings()) {
                            for (Building b : manager.getBuildings()) {
                                if (b.getPosition().equals(pendingBuildTarget) && b instanceof PopulationBuilding) {
                                    PopulationBuilding popBuilding = (PopulationBuilding) b;
                                    c.setMaxPopulation(c.getMaxPopulation() + popBuilding.getPopulationProvided());
                                    System.out.println("[CPU] Population max augmentée à " + c.getMaxPopulation());
                                    break;
                                }
                            }
                        }
                    }
                    
                    pendingBuildWorker.setIsWorking(false);
                    resetOtherBuildMission();
                }
            }
            return; 
        }

        Worker builder = getAvailableWorker(c);
        if (builder == null) return;

        ArrayList<HQ> cpuHQs = new ArrayList<>();
        synchronized(manager.getBuildings()) {
            for (Building b : c.getBuiltBuilding()) {
                if (b instanceof HQ) cpuHQs.add((HQ) b);
            }
        }

        for (HQ hq : cpuHQs) {
            int prodCount = 0, popCount = 0, towerCount = 0, labCount = 0;
            
            synchronized(manager.getBuildings()) {
                for (Building b : c.getBuiltBuilding()) {
                    if (manager.getDistance(b.getPosition(), hq.getPosition()) <= 18) {
                        if (b instanceof UnitProducer && !(b instanceof HQ)) prodCount++;
                        else if (b instanceof PopulationBuilding) popCount++;
                        else if (b instanceof DefenseTower) towerCount++;
                        else if (b instanceof ResearchBuilding) labCount++;
                    }
                }
            }

            if (prodCount < 2) {
                if (c.getAmbroisieStock() >= 125 && c.getFaithStock() >= 125) { 
                    Block pos = findBuildPositionSpecificallyNear(hq, 4, 8);
                    if (pos != null) {
                        launchOtherBuildMission(builder, pos, BuildingFactory.PRODUCER_BUILDING);
                    }
                }
                return; 
            }

            if (popCount < 3) {
                if (c.getAmbroisieStock() >= 100 && c.getFaithStock() >= 100) { 
                    Block pos = findBuildPositionSpecificallyNear(hq, 4, 9);
                    if (pos != null) {
                        launchOtherBuildMission(builder, pos, BuildingFactory.POPULATION_BUILDING);
                    }
                }
                return;
            }

            if (labCount < 1) {
                if (c.getAmbroisieStock() >= 150 && c.getFaithStock() >= 150) { 
                    Block pos = findBuildPositionSpecificallyNear(hq, 5, 10);
                    if (pos != null) {
                        launchOtherBuildMission(builder, pos, BuildingFactory.RESEARCH_BUILDING);
                    }
                }
                return;
            }

            if (towerCount < 4) {
                if (c.getAmbroisieStock() >= 150 && c.getFaithStock() >= 150) { 
                    Block pos = findBuildPositionSpecificallyNear(hq, 7, 13);
                    if (pos != null) {
                        launchOtherBuildMission(builder, pos, BuildingFactory.DEFENSE_BUILDING);
                    }
                }
                return;
            }
        }
    }

    private Block findBuildPositionSpecificallyNear(HQ hq, int minDist, int maxDist) {
        int hqLine = hq.getPosition().getLine();
        int hqCol  = hq.getPosition().getColumn();

        for (int dist = minDist; dist <= maxDist; dist++) {
            for (int dl = -dist; dl <= dist; dl++) {
                for (int dc = -dist; dc <= dist; dc++) {
                    int line = hqLine + dl;
                    int col  = hqCol  + dc;
                    if (!isInMapBounds(line, col)) continue;

                    Block candidate = manager.getMap().getBlock(line, col);
                    
                    if (!isBlockInCPUZone(candidate)) continue;
                    
                    if (isBlockFreeForBuilding(candidate)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private void launchOtherBuildMission(Worker builder, Block targetPos, String buildingType) {
        if (builder.getCurrentDeposit() != null) {
            builder.getCurrentDeposit().setCurrentWorkers(
                    builder.getCurrentDeposit().getCurrentWorkers() - 1);
            builder.setCurrentDeposit(null);
        }
        builder.setDestination(targetPos);
        builder.setIsWorking(true);

        pendingBuildTarget = targetPos;
        pendingBuildType   = buildingType;
        pendingBuildWorker = builder;

        System.out.println("[CPU] Worker envoyé vers " + buildingType + " en X:" + targetPos.getColumn() + " Y:" + targetPos.getLine());
    }

    private void resetBuildMission() {
        pendingHQTarget = null;
        pendingDeposit  = null;
        assignedBuilder = null;
    }

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
            if (manager.getDistance(pendingBuildTarget, block) < 3) return false;
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

        synchronized(manager.getBuildings()) {
            for(Building b: c.getBuiltBuilding()) {
                if(b instanceof HQ) {
                    HQ hq=(HQ) b;
                    c.setWorkerProductionTime(c.getWorkerProductionTime()+1);
                    if(hq.getWorkerProducer().getProductionQueue().size() <= 1 && !hq.getIsUnderConstruction() && c.getWorkerProductionTime() >= 40) {
                        manager.addQueue(hq.getWorkerProducer(), hq.getPosition(), "WORKER", c);
                        c.setWorkerProductionTime(0);
                    }
                }
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
                        if (!isBlockInCPUZone(r.getPosition())) continue;
                        
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
        
        int hqCount = countBuildingType(c, HQ.class);
        int maxArmy = 30 + (hqCount * 15); 
        
        if (armyCount >= maxArmy) {
            return;
        }

        synchronized(manager.getBuildings()) {
            for(Building b : c.getBuiltBuilding()) {
                if(b instanceof UnitProducer && !(b instanceof HQ)) {
                    UnitProducer producer = (UnitProducer) b;
                    
                    if(producer.getProductionQueue().isEmpty() && !producer.getIsUnderConstruction()) {
                        if(c.getCurrentPopulation() < c.getMaxPopulation()) {
                            if(c.getAmbroisieStock() >= 20 && c.getFaithStock() >= 20) {
                                String unitToProduce = "INFANTRY"; 
                                
                                if (c.getFactionName().equalsIgnoreCase("ZEUS")) {
                                    unitToProduce = "ARTILLERY"; 
                                }

                                manager.addQueue(producer, producer.getPosition(), unitToProduce, c);
                                
                                c.setAmbroisieStock(c.getAmbroisieStock() - 15);
                                c.setFaithStock(c.getFaithStock() - 15);
                                
                                armyCount++; 
                                if (armyCount >= maxArmy) break;
                            }
                        }
                    }
                }
            }
        }
    }

    public MobileInterface getManager() {
        return manager;
    }

    private int countBuildingType(CPU c, Class<?> clazz) {
        int count = 0;
        synchronized(manager.getBuildings()) {
            for (Building b : c.getBuiltBuilding()) {
                if (clazz.isInstance(b)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setManager(MobileInterface manager) {
        this.manager = manager;
    }
}