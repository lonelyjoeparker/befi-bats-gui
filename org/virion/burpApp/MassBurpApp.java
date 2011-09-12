package org.virion.burpApp;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.virion.*;
import org.virion.BURPer.*;

public class MassBurpApp {

	private static int reps;
	private shuffler s;

	/**
	 * @param args
	 */
	
	public static void main(String [] args){
		File workDir = new File(args[0]);
		reps = Integer.parseInt(args[1]);
		String[] filenames = workDir.list();
		System.out.println("analysing files ");
		for(String filename:filenames){System.out.println(filename);}
		LogWriter lW = new LogWriter();
		String [] headers = {"File","RH Obs","RH Exp","RH p","nc1S Obs","nc1S Exp","nc1S p","nc1H Obs","nc1H Exp","nc1H p","nc2S Obs","nc2S Exp","nc2S p","nc2H Obs","nc2H Exp","nc2H p","AI Obs","AI Exp","AI p","Mig Obs","Mig Exp","Mig p"};
		double [][] data = new double [filenames.length][21];
		
		for(int i=0; i<filenames.length; i++){
	
//o			Pattern trees = Pattern.compile("[0-9a-zA-Z._]*trees");
//o			Matcher isTre = trees.matcher(filenames[i]);
//o			if(new File(workDir.getAbsolutePath()+"/"+filenames[i]).isFile()){
//o			if(isTre.matches()){
			if(Pattern.compile("[0-9a-zA-Z._]*trees").matcher(filenames[i]).matches()){
				MassBURPer_v_0_2 burp = new MassBURPer_v_0_2(reps);
//-				System.out.print("\nreading file..\n...with " + reps + " reps\n");

				burp.readTreeFile(workDir.getAbsolutePath() + "/" + filenames[i]);
				shuffler s = new shuffler(burp.getTaxaStates(), reps);
				System.out.println(filenames[i]+burp.getTaxaStates());
				data[i] = burp.analyse(filenames[i]);
//o				burp.ShowLastTree();
			}
		}

		for(int i=0; i<21; i++){
			double[] tempData = new double[filenames.length];
			for(int j=0; j<filenames.length; j++){
				tempData[j] = data[j][i];
			}
			lW.addArray(tempData);
		}
		
		lW.writeArrays(workDir.getParentFile().getAbsolutePath() + "/" + workDir.getName() + "_summary.log", headers);
	}
}
