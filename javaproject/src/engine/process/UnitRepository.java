package engine.process;

import java.util.HashMap;

import engine.mobile.unit.UnitStats;

public class UnitRepository {
	/**
	 * This {@link HashMap} contains all registered Units. The key is
	 * variable  and the value is the current variable's value.
	 */
    private HashMap<String, UnitStats> stats = new HashMap<>();
    
    /**
	 * The unique instance of the class prepared in an eager way (object created
	 * at beginning).
	 */
    private static UnitRepository instance = new UnitRepository();

    private UnitRepository() { 
    	
    }

    public static UnitRepository getInstance() {
        return instance;
    }

    public void register(String key, UnitStats data) {
    	stats.put(key, data);
    }

    @Override
	public String toString() {
		return "UnitRepository [stats=" + stats + "]";
	}

	public UnitStats getStats(String key) {
        return stats.get(key);
    }
}
