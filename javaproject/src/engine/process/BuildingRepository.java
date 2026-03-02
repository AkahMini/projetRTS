package engine.process;

import java.util.HashMap;
import engine.mobile.building.BuildingStats;

/**
 * This class manages the values of all known buildings in the game. The class
 * uses Singleton design pattern to maintain the consistency on values.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 */
public class BuildingRepository {
	
	/**
	 * This {@link HashMap} contains all registered buildings. The key is
	 * variable  and the value is the current variable's value.
	 */
    private HashMap<String, BuildingStats> stats = new HashMap<>();
    
    /**
	 * The unique instance of the class prepared in an eager way (object created
	 * at beginning).
	 */
    private static BuildingRepository instance = new BuildingRepository();

    private BuildingRepository() { 
    	
    }

    public static BuildingRepository getInstance() {
        return instance;
    }

    public void register(String key, BuildingStats data) {
    	stats.put(key, data);
    }

    public BuildingStats getStats(String key) {
        return stats.get(key);
    }
}
