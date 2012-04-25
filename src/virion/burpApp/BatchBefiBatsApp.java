package org.virion.burpApp;

import java.io.File;
import java.util.regex.Pattern;
import org.virion.*;
import org.virion.BURPer.*;

public class BatchBefiBatsApp {

	private static String[] filenames;
	private static int reps;
	private static int max_States;
	private static File workDir;
	private shuffler s;

	public BatchBefiBatsApp(){
		filenames = null;
		reps = 0;
		max_States = 0;
	}
	
	public BatchBefiBatsApp(String wDir, int nReps, int nStates){
		workDir = new File(wDir);
		filenames = workDir.list();
		reps = nReps;
		max_States = nStates;
	}

	public void go(){
		System.out.println("analysing files ");
		for(String filename:filenames){System.out.println(filename);}
		LogWriter lW = new LogWriter();
		String [] firstHeaders = {"File","AI Obs","AI Exp","p(AI)","Migration Obs","Migration Exp","p(Migration)","UniFrac Obs","UniFrac Exp","p(UniFrac)","NT(distance) Obs","NT(distance) Exp","p(NT(distance))","NR(distance) Obs","NR(distance) Exp","p(NR(distance))","PD Obs","PD Exp","p(PD)"};
		String [] headers = new String[19 + (max_States * 3)];
		for(int i=0; i<firstHeaders.length; i++){
			headers [i] = firstHeaders[i];
		}
		for(int i=0; i<max_States; i++){
			int blockKey = ((i+6)*3)+1;
			headers[blockKey] = "N-C size (" + i + ") Obs"; 
			headers[blockKey+1] = "N-C size (" + i + ") Exp"; 
			headers[blockKey+2] = "p(N-C size (" + i + "))"; 
		}
		double [][] data = new double [filenames.length][18 + (max_States * 3)];
		
		for(int i=0; i<filenames.length; i++){
			if(Pattern.compile("[0-9a-zA-Z-._]*trees").matcher(filenames[i]).matches()){
//				MinimalMassBURPer burp = new MinimalMassBURPer(reps);
				BefiBats gen = new BefiBats(reps, workDir.getAbsolutePath() + "/" + filenames[i]);
/*
 * 				FLAG:
 * 				an option to self-randomise; e.g. produce a true random distribution
 * 				by using internal randomisation of taxa states by java.util.Random.nextInt(n),
 * 				rather than using perlscripted states:
 * 
 * 				burp.setSelfRandomisation(true);
 *
 */				
//				burp.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				gen.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				System.out.println("\t"+filenames[i]);
				data[i] = new double[18 + (max_States * 3)];
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

		for(int i=0; i<18 + (max_States * 3); i++){
			double[] tempData = new double[filenames.length];
			for(int j=0; j<filenames.length; j++){
				tempData[j] = data[j][i];
			}
			lW.addArray(tempData);
		}
		
		lW.writeArrays(workDir.getParentFile().getAbsolutePath() + "/" + workDir.getName() + "_summary.log", headers);
	}
	
}
