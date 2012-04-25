package org.virion.BURPer;

import org.virion.shuffler;

public class BURPerLauncher {

	private shuffler s;
	private static int reps;

	/**
	 * @param args
	 */

	public static void main(String [] args){
			String filename = args[0];
			reps = Integer.parseInt(args[1]);
			
			BURPer burp = new BURPer(reps);
			System.out.print("\nreading file..\n");
			burp.readTreeFile(filename);
	//o		burp.generateNamesAndStates();  <--- uncomment this to use generated names & states
			shuffler s = new shuffler(burp.getTaxaStates(), reps);
			burp.analyse(filename);
		}

}
