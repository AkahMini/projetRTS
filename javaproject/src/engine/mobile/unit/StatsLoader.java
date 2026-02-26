package engine.mobile.unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import config.GameConfiguration;

public class StatsLoader {
	public static HashMap<String,float[]> loadUnitStats(){
		float[] Individualstats = {0};
		HashMap<String,float[]> globalStats = new HashMap<>();
		try {
		
			File f = new File(GameConfiguration.UNITS_STATS);
			FileReader fileReader = new FileReader(f);
			BufferedReader buffer = new BufferedReader(fileReader);
			String line;
			while((line=buffer.readLine())!=null) {
				System.out.println(line);
			}
			buffer.close();
			/*
			java.io.FileReader extends Reader
			permet une lecture caract`ere par caract`ere
			FileReader(File file)
			FileReader(string fileName)
			close()
			
			
			*/
			
			
			
			
			
		}
	catch(Exception e) {
		System.out.println("Error, impossible to open unitStats.csv");
		}
		return globalStats;
	}
}
