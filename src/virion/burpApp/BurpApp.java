package org.virion.burpApp;

import org.virion.*;
import org.virion.BURPer.*;

public class BurpApp {

	private static int reps;
	private shuffler s;

	/**
	 * @param args
	 */
	
	public static void main(String [] args){

		String filename = args[0];
		reps = Integer.parseInt(args[1]);
		
		BURPer burp = new BURPer(reps);
		System.out.print("\nreading file..\n...with " + reps + " reps\n");
		burp.readTreeFile(filename);
		shuffler s = new shuffler(burp.getTaxaStates(), reps);
		burp.analyse(filename);
//o			burp.ShowLastTree();
	}

}
