package engine.mobile.unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import config.GameConfiguration;

public class StatsLoader {
	public static final String[] UNITS_ID_LIST = {"ATHENIAN","DISKTHROWER","CAUCASIANS",
			"EAGLE","CYCLOP","CENTAUR","HYDRA","SPARTAN","HOPLITE","STYX_ARCHER","TARTARUS_WARDEN",
			"MEDUSA","HEADLESS_KNIGHT","ATLANTEAN","RETIARIUS","GIANT_SNAKE","HARPY","HIPPOCAMPUS_WARIOR",
			"GHOST_KRAKEN"};
	
	
	public static HashMap<String,ArrayList<Float>> loadUnitStats(){
		/*
		 * Loads the HashMap containing the units stats
		 * unitType: 0=Worker, 1=Infantry, 2=Cavalry, 3=Artillery
		 * faction: 1=Zeus, 2=Hades, 3=Poseidon
		 */
		HashMap<String,ArrayList<Float>> globalStats = new HashMap<>();
		try {
		
			File f = new File(GameConfiguration.UNITS_STATS);
			FileReader fileReader = new FileReader(f);
			
			
			char c;//the reading head
			while((c=(char)fileReader.read())!=(char)'\n') {
				//In order to skip the first Line of the css file
			}
			
			String localString="";//The string that creates the data corresponding to the text
			String localKey = "";
			ArrayList<Float> unitData= new ArrayList<Float>(); //The list
			while((c=(char)fileReader.read())!=(char)-1) { //-1: end of the text
				//While there are still characters
				if(c!=','&&c!='\n') {
					//We create the local Value
					localString+=c;
				}

				else {
					
					if(localKey.equals("")) {
						localKey=localString;
						localString="";//reinitialize localString
					}
					else if(c!='\n') {
						//if we are still on the same unit
						
						//encoding the text in floats
						if(localString.equals("WORKER")) {
							//unitData[unitData.length]=0;
							unitData.add((float) 0);
						}
						else if(localString.equals("INFANTRY")||localString.equals("ZEUS")) {
							
							unitData.add((float) 1);
						}
						else if(localString.equals("CAVALRY")||localString.equals("HADES")) {
							unitData.add((float) 2);
						}
						else if(localString.equals("ARTILLERY")||localString.equals("POSEIDON")) {
							unitData.add((float) 3);
						}
						else {
							unitData.add(Float.parseFloat(localString));//Converts the String into a float
						}
						localString="";
					}
					else {
						globalStats.put(localKey, unitData);
						unitData= new ArrayList<Float>();;
						localString="";
						localKey="";
					}
						
					}
					
					//localString="";//reinitialize the text for the next values
				}
			globalStats.put(localKey, unitData);//the last line of the csv file
			fileReader.close();
			}
		
	catch(Exception e) {
		System.out.println(e);
		}
		
		return globalStats;
	}
	public static void printUnitsValues(HashMap<String,ArrayList<Float>> map) {
		for (String name: map.keySet()) {
		    String key = name.toString();
		    ArrayList<Float> value = map.get(name);
		    System.out.println("Key: "+ key + " Value: " + value);
		}
	}
}
