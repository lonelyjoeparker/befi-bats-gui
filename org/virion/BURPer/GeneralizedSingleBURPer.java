package org.virion.BURPer;

public class GeneralizedSingleBURPer {

	/**
	 * @param args
	 */
	
	private final String filename;
	private final int reps;
	private final int maxStates;
	
	public GeneralizedSingleBURPer(){
		// default no-arg constructor
		filename = "";
		reps = 0;
		maxStates = 0;
	}

	public GeneralizedSingleBURPer(String one, int two, int three){
		// arg constructor
		filename = one;
		reps = two;
		maxStates = three;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GeneralizedSingleBURPer me = new GeneralizedSingleBURPer(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		me.go();
	}

	public void go(){
		MinimalMassBURPer burp = new MinimalMassBURPer(reps, filename);
		double[][] results = burp.singleAnalyse(maxStates);
		System.out.println("Stat order:\n\tAI\n\tPS\n\tPD (combined)\n\tNT (combined, distance-based)\n\tNR (combined, distance-based)\n\tNT (combined, nodal)\n\tNR (combined, nodal)\n\tUniFrac\n\tMC (states 1..n)\n\nobserved mean\tlower 95% CI\tupper 95% CU\tnull mean\tlower 95% CI\tupper 95% CI\tsignificance");
		for(double[] line:results){
			for(double val:line){
				System.out.print(val + "\t");
			}
			System.out.println();
		}
		System.out.println("done");
	}
}
