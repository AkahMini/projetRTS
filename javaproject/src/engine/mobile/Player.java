package engine.mobile;

/**
 * 
 * @author LE RAY Yann
 *
 */

public class Player {

	private String pseudo;
	private String factionName;
	private int currentTier;
	private int faithStock;
	private int ambroisieStock;
	private int currentPopulation;
	private int maxPopulation;
	
	public Player(String pseudo, String factionName) {
		this.pseudo=pseudo;
		this.factionName=factionName;
		this.currentTier=1;
		this.faithStock=0;
		this.ambroisieStock=0;
		this.currentPopulation=0;
		this.maxPopulation=100;
	}
	
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getFactionName() {
		return factionName;
	}
	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}
	public int getCurrentTier() {
		return currentTier;
	}
	public void setCurrentTier(int currentTier) {
		this.currentTier = currentTier;
	}
	public int getFaithStock() {
		return faithStock;
	}
	public void setFaithStock(int faithStock) {
		this.faithStock = faithStock;
	}
	public int getAmbroisieStock() {
		return ambroisieStock;
	}
	public void setAmbroisieStock(int ambroisieStock) {
		this.ambroisieStock = ambroisieStock;
	}
	public int getCurrentPopulation() {
		return currentPopulation;
	}
	public void setCurrentPopulation(int currentPopulation) {
		this.currentPopulation = currentPopulation;
	}
	public int getMaxPopulation() {
		return maxPopulation;
	}
	public void setMaxPopulation(int maxPopulation) {
		this.maxPopulation = maxPopulation;
	}
}
