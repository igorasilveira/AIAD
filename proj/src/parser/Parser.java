package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import agents.dataminer.DecisionAttack;
import agents.dataminer.DecisionDefend;
import agents.dataminer.DecisionFortify;

public class Parser {
	public enum Type {
		Attack, Defend, Fortify; 
	}
	
	public static void parseFile(Type type, String winnersPath, String filePath, String outputPath) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(outputPath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter writer = new BufferedWriter(outputStreamWriter); 
        
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        
        HashMap<Integer, Integer> winners = Parser.parseWinners(winnersPath);
        
        String line = null;
		while(!(line = reader.readLine()).startsWith("\"")) {
			writer.write(line+"\n");
		}
		
		switch(type) {
		case Attack:
			writer.write(DecisionAttack.header()+"\n");
			break;
		case Defend:
			writer.write(DecisionDefend.header()+"\n");
			break;
		case Fortify:
			writer.write(DecisionFortify.header()+"\n");
			break;
		default:
			break;
		}
		
		while(!(line = reader.readLine()).equals("")) {
			String[] elem = line.split(",");
			int run = Integer.parseInt(elem[0]);
			
			for(int i = 0; i < elem.length; i++) {
				if(i == 3) {
					int player = Integer.parseInt(elem[i]);
					
					if(winners.get(run) == player) {
						writer.write("\"win\"");
					}
					else {
						writer.write("\"lose\"");
					}
				}
				else {
					writer.write(elem[i]);
				}
				
				if(i != elem.length - 1) {
					writer.write(",");
				}
			}
			writer.write("\n");
		}
		
		writer.write("\n");
		
		while((line = reader.readLine()) != null) {
			writer.write('#' + line +"\n");
		}
		
		writer.close();
		reader.close();
	}
	
	public static HashMap<Integer, Integer> parseWinners(String winnersPath) throws IOException {
		HashMap<Integer, Integer> winners = new HashMap<>();
		
		BufferedReader winnersReader = new BufferedReader(new FileReader(new File(winnersPath)));
		
		while(!winnersReader.readLine().startsWith("\""));
		
		String line = null;
		while(!(line = winnersReader.readLine()).equals("")) {
			String[] elem = line.split(",");
			winners.put(Integer.parseInt(elem[0]), Integer.parseInt(elem[3]));
		}
		
		winnersReader.close();
		
		return winners;
	}
	
	public static void main(String[] args) throws IOException {
		Parser.parseFile(Type.Fortify, "sims/winners.txt","sims/fortify.txt","sims/outputs/fortify.txt");
		Parser.parseFile(Type.Attack, "sims/winners.txt","sims/attack.txt","sims/outputs/attack.txt");
		Parser.parseFile(Type.Defend, "sims/winners.txt","sims/defend.txt","sims/outputs/defend.txt");
	}
}
