package org.virion.BURPer;

// ver 1.0 //

import java.io.*;
import java.util.ArrayList;

public class LogWriter{

	private ArrayList<ArrayList> theLists = new ArrayList<ArrayList>();
	private ArrayList<double[]> theArrays = new ArrayList<double[]>();
	private int Columns;
	private String outFile;
	
	public void addList(ArrayList<String> newList){
		theLists.add(newList);
		this.Columns ++;
	}

	public void addArray(double [] newArray){
		theArrays.add(newArray);
		this.Columns ++;
	}
	
	public void write(String filename){
		outFile = filename + ".reconstructed.log";
		StringBuilder composed = new StringBuilder();
//-		System.out.println("--log file writer--");
//=		composed.append("--log file writer--\n");
		int Items = theLists.get(0).size();
//-		System.out.println("have " + Items + " items");
//=		composed.append("have " + Items + " items\n");
		composed.append("State\t");
		for(int i = 0; i < Columns; i ++){
//-			System.out.print("Stat" + i + "\t");
			composed.append("Statistic " + i + "\t");
		}
//-		System.out.println();
		composed.append("\n");
		for(int i = 0; i < Items; i ++){
			composed.append((i * 1000) + "\t");
			for(int j = 0; j < Columns; j ++){
//-				System.out.print(theLists.get(j).get(i) + "\t");
				composed.append(theLists.get(j).get(i) + "\t");
			}
//-			System.out.println();
			composed.append("\n");
		}
		System.out.println("composed:\n " + composed);
		
		try {
			FileWriter writer = new FileWriter(outFile);
			writer.write(composed.toString());
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void writeArrays(String filename, String [] headers){
		outFile = filename + ".reconstructed.log";
		StringBuilder composed = new StringBuilder();
		int Items = theArrays.get(0).length;
//-		System.out.println("have " + Items + " items");
//=		composed.append("have " + Items + " items\n");
		for(String header:headers){
			composed.append(header + "\t");
		}
//-		System.out.println();
		composed.append("\n");
		for(int i = 0; i < Items; i ++){
			composed.append(i + "\t");
			for(int j = 0; j < Columns; j ++){
//-				System.out.print(theLists.get(j).get(i) + "\t");
				composed.append(theArrays.get(j)[i] + "\t");
			}
//-			System.out.println();
			composed.append("\n");
		}
		System.out.println("composed:\n " + composed);
		
		try {
			FileWriter writer = new FileWriter(outFile);
			writer.write(composed.toString());
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getOutFile() {
		return outFile;
	}

	public ArrayList<ArrayList> getTheLists() {
		return theLists;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public void setTheLists(ArrayList<ArrayList> theLists) {
		this.theLists = theLists;
	}
}