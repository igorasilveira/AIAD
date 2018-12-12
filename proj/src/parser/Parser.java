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

public class Parser {
	public static void parseAttack(String winnersPath, String attackPath, String outputPath) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(outputPath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter writer = new BufferedWriter(outputStreamWriter); 
        
        BufferedReader attackReader = new BufferedReader(new FileReader(new File(attackPath)));
        
        HashMap<Integer, Integer> winners = Parser.parseWinners(winnersPath);
        
        String line = null;
		while(!(line = attackReader.readLine()).startsWith("\"")) {
			writer.write(line+"\n");
		}
		
		writer.write(DecisionAttack.header()+"\n");
		
		while(!(line = attackReader.readLine()).equals("")) {
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
		
		writer.close();
		attackReader.close();
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
		Parser.parseAttack("sims/winners.txt","sims/attack.txt","sims/output.txt");
	}
}
