/**
 * 
 */
package engine.mobile;

/**
 * 
 */
public class CPU extends Player {
	
	private int workerProductionTime;

	private int agressivity;
	private int intelligence;
	private int adaptibility;

	/**
	 * @param pseudo
	 * @param factionName
	 * @param agressivity
	 * @param intelligence
	 * @param adaptibility
	 */
	public CPU(String pseudo, String factionName, int agressivity, int intelligence, int adaptibility) {
		super(pseudo, factionName);
		this.agressivity = agressivity;
		this.intelligence = intelligence;
		this.adaptibility = adaptibility;
		this.workerProductionTime=0;
	}
	public int getAgressivity() {
		return agressivity;
	}

	public void setAgressivity(int agressivity) {
		this.agressivity = agressivity;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getAdaptibility() {
		return adaptibility;
	}

	public void setAdaptibility(int adaptibility) {
		this.adaptibility = adaptibility;
	}
	public int getWorkerProductionTime() {
		return workerProductionTime;
	}
	public void setWorkerProductionTime(int workerProductionTime) {
		this.workerProductionTime = workerProductionTime;
	}

}
