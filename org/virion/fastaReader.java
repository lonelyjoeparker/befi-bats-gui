package org.virion;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;

public class fastaReader{

	final private String filename;
	private File inputFile;
	private FileReader fr;
	private BufferedReader br;
	private int numberOfTaxa = 0;
	private int numberOfPositions = 0;
	private boolean isName;
	private ArrayList<ArrayList<Integer>> data;
	private ArrayList<String> taxaNames;
	
	
	public fastaReader(String filename){
		this.data = new ArrayList<ArrayList<Integer>>();
		this.taxaNames = new ArrayList<String>();
		this.filename = filename;
		
		try {
			this.inputFile = new File(filename);
			this.fr = new FileReader(inputFile);
			this.br = new BufferedReader(fr);
			String line = null;
			Pattern greaterThan = Pattern.compile(">+.*");
			int i = 0;
			
			while((line = br.readLine()) != null){
				Matcher gtMatch = greaterThan.matcher(line);

				if(gtMatch.matches()){
//o					System.out.println(line);
//o				}

//o				if((Pattern.matches(">+.*", line))){
//c					(a taxon name)				
//-					System.out.println("name: " + line);
					taxaNames.add(line);
				} else {
//c					(a sequence)				
//-					System.out.println("sequence: " + line);
					char [] sequence = line.toCharArray();
					ArrayList<Integer> codedSequence = new ArrayList<Integer>();
					for(char c: sequence){
						codedSequence.add(this.getCode(c));
					}
					data.add(codedSequence);
					numberOfPositions = sequence.length;
				}
				i++;
			}
			numberOfTaxa  = taxaNames.size();
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	
	}
	
	public ArrayList<ArrayList<Integer>> getData(){
		return data;
	}
	
	public int[] getSize(){
		System.out.println("dimensions= " + numberOfTaxa + " " + numberOfPositions);
		int size[] = new int[] {numberOfTaxa, numberOfPositions};
		return size;
	}
	
	
	public ArrayList<String> getNames(){
		return taxaNames;
	}

	public int getCode(char c){

		if(c == 'A' || c == 'a'){
			return 0;
		} else if(c == 'C' || c == 'c'){
			return 1;
		} else if(c == 'D' || c == 'd'){
			return 2;
		} else if(c == 'E' || c == 'e'){
			return 3;
		} else if(c == 'F' || c == 'f'){
			return 4;
		} else if(c == 'G' || c == 'g'){
			return 5;
		} else if(c == 'H' || c == 'h'){
			return 6;
		} else if(c == 'I' || c == 'i'){
			return 7;
		} else if(c == 'K' || c == 'k'){
			return 8;
		} else if(c == 'L' || c == 'l'){
			return 9;
		} else if(c == 'M' || c == 'm'){
			return 10;
		} else if(c == 'N' || c == 'n'){
			return 11;
		} else if(c == 'P' || c == 'p'){
			return 12;
		} else if(c == 'Q' || c == 'q'){
			return 13;
		} else if(c == 'R' || c == 'r'){
			return 14;
		} else if(c == 'S' || c == 's'){
			return 15;
		} else if(c == 'T' || c == 't'){
			return 16;
		} else if(c == 'V' || c == 'v'){
			return 17;
		} else if(c == 'W' || c == 'w'){
			return 18;
		} else if(c == 'Y' || c == 'y'){
			return 19;
		} else if(c == '*'){
			return 20;
		} else {
			return 99;
		}
	}
}