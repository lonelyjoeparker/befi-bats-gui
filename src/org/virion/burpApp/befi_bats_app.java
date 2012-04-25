package org.virion.burpApp;

import org.virion.BURPer.GeneralizedSingleBURPer;
import org.virion.BURPer.*;

public class befi_bats_app {

	/**
	 * @param args
	 * 
	 * This is the first version (0.0.1) of the revised ('Befi')
	 * version of BaTS.
	 * 
	 * I'm re-writing the entry point, because the old one (bats_app_beta.java)
	 * got lost when I reformatted the MacBook in Jan 2008 (doh!)
	 * 
	 * 03 / July / 2008
	 * 
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String modeSwitch = null;
		String input = null;
		String reps = null;
		String max_s = null;

		try {
			modeSwitch = args[0];
			input = args[1];
			reps = args[2];
			max_s = args[3];
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Warning!\rNot all input arguments were specified: BaTS may run incorrectly, or not at all.\r\rUsage: [batch|single] [input file|input dir] [null replicates] [maximum observed states]");
		}
		if(modeSwitch.equals("legacy_batch")){
			// Legacy batch analysis
			System.out.println("Running a batch analysis on " + input + " with " + reps + " null replicates and " + max_s + " maximum discrete traits.\r");
			MiniMassBurpApp burp = new MiniMassBurpApp(input, Integer.parseInt(reps), Integer.parseInt(max_s));
			burp.go();
		}else if(modeSwitch.equals("batch")){
			// Batch analysis
			System.out.println("Running a batch analysis on " + input + " with " + reps + " null replicates and " + max_s + " maximum discrete traits.\r");
			BatchBefiBatsApp burp = new BatchBefiBatsApp(input, Integer.parseInt(reps), Integer.parseInt(max_s));
			burp.go();
		}else if(modeSwitch.equals("legacy_single")){
			// Legacy single analysis
			System.out.println("Running a single analysis on " + input + " with " + reps + " null replicates and " + max_s + " maximum discrete traits.\r");
			GeneralizedSingleBURPer burp = new GeneralizedSingleBURPer(input, Integer.parseInt(reps), Integer.parseInt(max_s));
			burp.go();
		}else if(modeSwitch.equals("single")){
			// Single analysis
			System.out.println("Running a single analysis on " + input + " with " + reps + " null replicates and " + max_s + " maximum discrete traits.\r");
			SingleBefiBatsApp burp = new SingleBefiBatsApp(input, Integer.parseInt(reps), Integer.parseInt(max_s));
			burp.go();
		}else{
			System.out.println("Sorry - no input mode switch specified, or not recognised");
			System.out.println("not all input arguments were specified: BaTS may run incorrectly, or not at all.\r\rUsage: [batch|single] [input file|input dir] [null replicates] [maximum observed states]");
		}
	}
}
