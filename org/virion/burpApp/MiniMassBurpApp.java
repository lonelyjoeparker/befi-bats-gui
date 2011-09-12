package org.virion.burpApp;

import java.io.File;
import java.util.regex.Pattern;
import org.virion.*;
import org.virion.BURPer.*;

public class MiniMassBurpApp {

	private static String[] filenames;
	private static int reps;
	private static int max_States;
	private static File workDir;
	private shuffler s;

	/**
	 * @param args
	 */

	public MiniMassBurpApp(){
		filenames = null;
		reps = 0;
		max_States = 0;
	}
	
	public MiniMassBurpApp(String wDir, int nReps, int nStates){
		workDir = new File(wDir);
		filenames = workDir.list();
		reps = nReps;
		max_States = nStates;
	}

	public void go(){
		System.out.println("analysing files ");
		for(String filename:filenames){System.out.println(filename);}
		LogWriter lW = new LogWriter();
		String [] firstHeaders = {"File","AI Obs","AI Exp","p(AI)","Migration Obs","Migration Exp","p(Migration)","PD Obs","PD Exp","p(PD)","NT(distance) Obs","NT(distance) Exp","p(NT(distance))","NR(distance) Obs","NR(distance) Exp","p(NR(distance))","NT(nodal) Obs","NT(nodal) Exp","p(NT(nodal))","NR(nodal) Obs","NR(nodal) Exp","p(NR(nodal))","UniFrac Obs","UniFrac Exp","p(UniFrac)"};
		String [] headers = new String[25 + (max_States * 3)];
		for(int i=0; i<firstHeaders.length; i++){
			headers [i] = firstHeaders[i];
		}
		for(int i=0; i<max_States; i++){
			int blockKey = ((i+8)*3)+1;
			headers[blockKey] = "N-C size (" + i + ") Obs"; 
			headers[blockKey+1] = "N-C size (" + i + ") Exp"; 
			headers[blockKey+2] = "p(N-C size (" + i + "))"; 
		}
		double [][] data = new double [filenames.length][24 + (max_States * 3)];
		
		for(int i=0; i<filenames.length; i++){
			if(Pattern.compile("[0-9a-zA-Z-._]*trees").matcher(filenames[i]).matches()){
				MinimalMassBURPer burp = new MinimalMassBURPer(reps);
				MinimalMassBURPer gen = new MinimalMassBURPer(reps, workDir.getAbsolutePath() + "/" + filenames[i]);
/*
 * 				FLAG:
 * 				an option to self-randomise; e.g. produce a true random distribution
 * 				by using internal randomisation of taxa states by java.util.Random.nextInt(n),
 * 				rather than using perlscripted states:
 * 
 * 				burp.setSelfRandomisation(true);
 *
 */				
				burp.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				gen.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				System.out.println("\t"+filenames[i]);
				data[i] = new double[24 + (max_States * 3)];
				for(double val:data[i]){
					val = 0.0;
				}
				double[] holder = gen.generalizedAnalyse();
				for(int j=0; j<holder.length; j++){
					data[i][j] = holder[j];
				}
				System.out.println("\t"+filenames[i]+" last tree migrations: "+String.valueOf(gen.getLastTreeMigrationEvents()));
			}
		}

		for(int i=0; i<24 + (max_States * 3); i++){
			double[] tempData = new double[filenames.length];
			for(int j=0; j<filenames.length; j++){
				tempData[j] = data[j][i];
			}
			lW.addArray(tempData);
		}
		
		lW.writeArrays(workDir.getParentFile().getAbsolutePath() + "/" + workDir.getName() + "_summary.log", headers);
	}
	
	public static void main(String [] args){
		File workDir = new File(args[0]);
		try{
			reps = Integer.parseInt(args[1]);
			System.out.println("null reps: " + reps);
		}catch(Exception ex){
			reps = 0;
			System.out.println("null reps: default [0]");
		}
		try{
			max_States = Integer.parseInt(args[2]);
			System.out.println("max states: " + max_States);
		}catch(Exception ex){
			max_States = 0;
			System.out.println("max states: default [0]");
		}
		String[] filenames = workDir.list();
		System.out.println("analysing files ");
		for(String filename:filenames){System.out.println(filename);}
		LogWriter lW = new LogWriter();
		String [] firstHeaders = {"File","AI Obs","AI Exp","AI p","Migration Obs","Migration Exp","Migration p"};
		String [] headers = new String[7 + (max_States * 3)];
		for(int i=0; i<firstHeaders.length; i++){
			headers [i] = firstHeaders[i];
		}
		for(int i=0; i<max_States; i++){
			int blockKey = ((i+2)*3)+1;
			headers[blockKey] = "N-C size (" + i + ") Obs"; 
			headers[blockKey+1] = "N-C size (" + i + ") Exp"; 
			headers[blockKey+2] = "N-C size (" + i + ") p"; 
		}
		double [][] data = new double [filenames.length][6 + (max_States * 3)];
		
		for(int i=0; i<filenames.length; i++){
	
//o			Pattern trees = Pattern.compile("[0-9a-zA-Z._]*trees");
//o			Matcher isTre = trees.matcher(filenames[i]);
//o			if(new File(workDir.getAbsolutePath()+"/"+filenames[i]).isFile()){
//o			if(isTre.matches()){
			if(Pattern.compile("[0-9a-zA-Z-._]*trees").matcher(filenames[i]).matches()){
				MinimalMassBURPer burp = new MinimalMassBURPer(reps);
				MinimalMassBURPer gen = new MinimalMassBURPer(reps, workDir.getAbsolutePath() + "/" + filenames[i]);
/*
 * 				FLAG:
 * 				an option to self-randomise; e.g. produce a true random distribution
 * 				by using internal randomisation of taxa states by java.util.Random.nextInt(n),
 * 				rather than using perlscripted states:
 * 
 * 				burp.setSelfRandomisation(true);
 *
 */				

				
//-				System.out.print("\nreading file..\n...with " + reps + " reps\n");

				burp.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				gen.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				System.out.println("\t"+filenames[i]);
//-				System.out.println(burp.getTaxaStates());
				data[i] = new double[6 + (max_States * 3)];
				for(double val:data[i]){
					val = 0.0;
				}
//o				double[] holder = burp.analyse(filenames[i]);
//o				for(int j=0; j<holder.length; j++){
//o					data[i][j] = holder[j];
//o				}
				double[] holder = gen.generalizedAnalyse();
//-				System.out.println("holder size " + holder.length);
//-				System.out.println("dataAr size " + data[i].length);
				for(int j=0; j<holder.length; j++){
					data[i][j] = holder[j];
				}
//o				burp.ShowLastTree();
				System.out.println("\t"+filenames[i]+" last tree migrations: "+String.valueOf(gen.getLastTreeMigrationEvents()));
				
			}
		}

		for(int i=0; i<6 + (max_States * 3); i++){
			double[] tempData = new double[filenames.length];
			for(int j=0; j<filenames.length; j++){
				tempData[j] = data[j][i];
			}
			lW.addArray(tempData);
		}
		
		lW.writeArrays(workDir.getParentFile().getAbsolutePath() + "/" + workDir.getName() + "_summary.log", headers);
	}
}
