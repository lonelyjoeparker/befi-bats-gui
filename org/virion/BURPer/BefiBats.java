package org.virion.BURPer;

//ver 0.1 //


import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.virion.GeneralizedShuffler;
import org.virion.StatisticHandler;
import org.virion.BURPer.EnhancedTree;

public class BefiBats{

	private	String [] treefile;
	private ArrayList<String> taxaNames;
//	private ArrayList<String> taxaNames = new ArrayList<String>();
	private ArrayList<String> taxaStates = new ArrayList<String>();
	private ArrayList<ArrayList<String>> shuffledStates = new ArrayList<ArrayList<String>>();
	private float[][] obsVals;
	private float[][] nulVals;
	private String [] availableStates;
	private final int reps;
	private EnhancedTree lastTree;
	private boolean doSelfRandomisations = false;
	private double[] Summary;
	private int numStates;
	private int ncCols;
	
	public BefiBats(int reps){
		this.reps = reps;
	}

	public BefiBats(int reps, String filename){
		this.reps = reps;
		this.readTreeFile(filename);
		this.availableStates = this.makeStates(taxaStates);
		this.numStates = availableStates.length;
		this.ncCols = numStates * 3;
		this.Summary = new double[24 + ncCols];
		this.shuffledStates = new GeneralizedShuffler(taxaStates, reps).shuffle();
		this.obsVals = new float[8 + numStates][treefile.length];
	}
	
//
	public double[][] singleAnalyse(int maxStates){
		System.out.println("obs vals: "+obsVals.length);
		double[][] retArr = new double[8+maxStates][7];
		System.out.println("analysing... " + treefile.length + " trees, with " + availableStates.length + " states");
		if (reps > 0) {
			nulVals = new float[8 + numStates][reps];

			for (int replicate=0; replicate<reps; replicate ++) {
				int index = 0;
				for(String aTree:treefile){
					ArrayList<String> stateArrangement = shuffledStates.get(replicate);
					EnhancedTree nullTree = new EnhancedTree(aTree, taxaNames, stateArrangement, availableStates);
					float[] nullValsArr = nullTree.getAllStats();
/*
					nulVals[0][replicate] += nullTree.getAI();
					nulVals[1][replicate] += nullTree.getMigrationEvents();
					nulVals[2][replicate] += nullTree.getPD();
					nulVals[3][replicate] += nullTree.getNTI();
					nulVals[4][replicate] += nullTree.getNRI();
					nulVals[5][replicate] += nullTree.getNodalNTI();
					nulVals[6][replicate] += nullTree.getNodalNRI();
					nulVals[7][replicate] += nullTree.getUniFrac();
					for(int i = 0; i<8+numStates; i++){
						nulVals[i + 8][replicate] += nullTree.getBiggestExclusiveNodeOf(i);
					}
*/
					for(int i=0;i<8+numStates;i++){
						nulVals[i][replicate] += nullValsArr[i];
					}
					index ++;
				}
				nulVals[0][replicate] = nulVals[0][replicate]/treefile.length;
				nulVals[1][replicate] = nulVals[1][replicate]/treefile.length;
				nulVals[2][replicate] = nulVals[2][replicate]/treefile.length;
				nulVals[3][replicate] = nulVals[3][replicate]/treefile.length;
				nulVals[4][replicate] = nulVals[4][replicate]/treefile.length;
				nulVals[5][replicate] = nulVals[5][replicate]/treefile.length;
				nulVals[6][replicate] = nulVals[6][replicate]/treefile.length;
				nulVals[7][replicate] = nulVals[7][replicate]/treefile.length;
				for(int i = 0; i<numStates; i++){
					nulVals[i + 8][replicate] = nulVals[i + 8][replicate]/treefile.length;
				}
			}
		}
		int count = 0;
		System.out.println("analysing observed (using obs state data)");
		for(String aTree:treefile){
			EnhancedTree t = new EnhancedTree(aTree, taxaNames, taxaStates, availableStates);
/*
			obsVals [0][count] = t.getNodalNTI();
			obsVals [1][count] = t.getNodalNRI();
			obsVals [2][count] = (float)t.getAI();
			obsVals [3][count] = (float)t.getMigrationEvents();
			obsVals [4][count] = (float)t.getUniFrac();
			obsVals [5][count] = t.getNTI();
			obsVals [6][count] = t.getNRI();
			obsVals [7][count] = t.getPD();
			for(int i = 0; i<numStates; i++){
				obsVals[i + 8][count] = (float)t.getBiggestExclusiveNodeOf(i);
			}
	*/		
			float[] eResults = t.getAllStats();
			for(int i=0;i<numStates+8;i++){
//				System.out.println("stat i"+i+" : "+(eResults[i]-obsVals[i][count]));
				obsVals[i][count] = eResults[i];
			}
				
			count ++;
		}	


		StatisticHandler [] stats = new StatisticHandler[8 + numStates];
		stats[0] = new StatisticHandler(obsVals[0], nulVals[0], true);
		stats[1] = new StatisticHandler(obsVals[1], nulVals[1], true);
		stats[2] = new StatisticHandler(obsVals[2], nulVals[2], true);
		stats[3] = new StatisticHandler(obsVals[3], nulVals[3], true);
		stats[4] = new StatisticHandler(obsVals[4], nulVals[4], true);
		stats[5] = new StatisticHandler(obsVals[5], nulVals[5], true);
		stats[6] = new StatisticHandler(obsVals[6], nulVals[6], true);
		stats[7] = new StatisticHandler(obsVals[7], nulVals[7], true);
		for(int i = 0; i<numStates; i++){
			stats[i + 8] = new StatisticHandler(obsVals[i + 8], nulVals[i + 8], false);
		}

		for(int i = 0; i < (8 + maxStates); i ++){
			retArr[i][0] = stats[i].getMean();
			retArr[i][1] = stats[i].getLowerCI();
			retArr[i][2] = stats[i].getUpperCI();
			retArr[i][3] = stats[i].getNullMean();
			retArr[i][4] = stats[i].getLowerNullCI();
			retArr[i][5] = stats[i].getUpperNullCI();
			retArr[i][6] = stats[i].getSignificance();
		}

		return retArr;
	}
			
	public double[] generalizedAnalyse(){
		/*			
		 *			2006/12/07
		 * 
		 * 			perhaps where things are going wrong is in taking
		 * 			the mean of a single row's null trees - really ought 
		 * 			to translate all 'cols' of a treefile's stats and get their_ 
		 * 			means into a single vector to return significance against....
		 * 
		 * 			e.g., start each tree with a new row 'reps AIs' or something, 
		 * 			then at the end of the treefile translate down to get means, AImeanNulls
		 * 			and run new StatHandler(etc) on that.
		 */			

		/*
		 * 		summary of triplets in form {mean, null mean, significance}
		 * 		order:
		 * 			0-2: AI
		 * 			3-5: migrations
		 * 			THEN the n-c sizes, as many as there 
		 * 			are states, of the form (mean, null, significance)
		 * 			in triplets such that the indices for any
		 * 			nth state are given as {(n+1)*3, ((n+1)*3)+1, ((n+1)*3)+2}
		 * 			
		 */		

		System.out.println("analysing... " + treefile.length + " trees, with " + availableStates.length + " states");
		/*
		 * 			if doing reps, do as many as we have
		 */
		if (reps > 0) {
			nulVals = new float[6 + numStates][reps];

			for (int replicate=0; replicate<reps; replicate ++) {
//-				System.out.println("analysing null " + replicate);
//-				System.out.println("shuffled states:");
//-				for(String state:shuffledStates.get(replicate)){
//-					System.out.println("\t" + state);
//-				}
//o				float[] holder = new float[treefile.length];
				int index = 0;
				for(String aTree:treefile){
//-					System.out.println("\ttree " + index);
					ArrayList<String> stateArrangement = shuffledStates.get(replicate);
					EnhancedTree nullTree = new EnhancedTree(aTree, taxaNames, stateArrangement, availableStates);
					float[] nullVals = nullTree.getAllStats();
					for(int i=0;i<6+numStates;i++){
						nulVals[i][replicate] += nullVals[i+2];
					}
/*					
					nulVals[0][replicate] += nullTree.getAI();
					nulVals[1][replicate] += nullTree.getMigrationEvents();
					nulVals[2][replicate] += nullTree.getPD();
					nulVals[3][replicate] += nullTree.getNTI();
					nulVals[4][replicate] += nullTree.getNRI();
					nulVals[5][replicate] += nullTree.getUniFrac();
//-					System.out.println(nullTree.getUniFrac());
					for(int i = 0; i<numStates; i++){
						nulVals[i + 6][replicate] += nullTree.getBiggestExclusiveNodeOf(i);
					}
*/
//o					holder[index] = (float)nullAI;
					index ++;
				}
				nulVals[0][replicate] = nulVals[0][replicate]/treefile.length;
				nulVals[1][replicate] = nulVals[1][replicate]/treefile.length;
				nulVals[2][replicate] = nulVals[2][replicate]/treefile.length;
				nulVals[3][replicate] = nulVals[3][replicate]/treefile.length;
				nulVals[4][replicate] = nulVals[4][replicate]/treefile.length;
				nulVals[5][replicate] = nulVals[5][replicate]/treefile.length;
				for(int i = 0; i<numStates; i++){
					nulVals[i + 6][replicate] = nulVals[i + 6][replicate]/treefile.length;
				}
				/*				StatisticHandler tempHandler = new StatisticHandler(holder, nullarr, false);
				System.out.println("mean "+ tempHandler.getMean());
				System.out.println(" other mean "+ nulVals[0][replicate]);
				System.out.println("interval "+ tempHandler.getRangeInterval());
				 */			}
		}

		/*
		 * 			observed tree stats for actual tree in MCMC chain
		 */

		this.Summary = new double[18 + ncCols];
		this.obsVals = new float[6 + numStates][treefile.length];

		int count = 0;
		if (doSelfRandomisations) {
			System.out.println("analysing observed (self randomisations; discarding state data)");
			ArrayList<String> tempRearrangement = new GeneralizedShuffler(taxaStates,1).shuffle().get(0);
			for(String aTree:treefile){
//-				System.out.println("\ttree "+ count);
				EnhancedTree t = new EnhancedTree(aTree, taxaNames, tempRearrangement, availableStates);
				

				obsVals [0][count] = (float)t.getAI();
				obsVals [1][count] = (float)t.getMigrationEvents();
				obsVals [2][count] = t.getPD();
				obsVals [3][count] = t.getNTI();
				obsVals [4][count] = t.getNRI();
				obsVals [5][count] = (float)t.getUniFrac();
				for(int i = 0; i<numStates; i++){
					obsVals[i + 6][count] = (float)t.getBiggestExclusiveNodeOf(i);
				}
	
				count ++;
				if(count == treefile.length){
					this.lastTree = t;
				}
			}
		} else {	
			System.out.println("analysing observed (using obs state data)");
			for(String aTree:treefile){
//-				System.out.println("\ttree "+ count);
				EnhancedTree t = new EnhancedTree(aTree, taxaNames, taxaStates, availableStates);
/*
				obsVals [0][count] = (float)t.getAI();
				obsVals [1][count] = (float)t.getMigrationEvents();
				obsVals [2][count] = t.getPD();
				obsVals [3][count] = t.getNTI();
				obsVals [4][count] = t.getNRI();
				obsVals [5][count] = (float)t.getUniFrac();
				for(int i = 0; i<numStates; i++){
					obsVals[i + 6][count] = (float)t.getBiggestExclusiveNodeOf(i);
				}
*/

				float[] obsValsArr = t.getAllStats();
//				System.out.println("obs (holder arr) "+obsVals.length);
//				System.out.println("obs ret arr "+obsValsArr.length);
				for(int i=0;i<6+numStates;i++){
					obsVals[i][count] = obsValsArr[i+2];
				}
				count ++;
				if(count == treefile.length){
					lastTree = t;
				}
			}
		}	

		/*		for(int i=0;i<reps;i++){
		 *			
		 *			nulVals[0][i] = new StatisticHandler(nullValsHolder[i], nullarr, false).getMean();
		 *			System.out.println(new StatisticHandler(nullValsHolder[i], nullarr, false).getMedian());
		 * 			System.out.println(new StatisticHandler(nullValsHolder[i], nullarr, false).getMean());
		 *		}
		 */

		/*		System.out.println(nullValsHolder[0].length);
		 *		System.out.println(nullValsHolder[0][0]);
		 */		
//		-		System.out.println("printout of nVals holder:\n");
//		-		this.ShowMatrix(nullValsHolder);

		/*
		 *
		 * 		Uncommment to enable individual reporting of
		 * 		stats
		 * 
		 * 		System.out.println("printout of nVals:\n");
		 *		this.ShowMatrix(nulVals);
		 *		System.out.println("printout of oVals:\n");
		 *		this.ShowMatrix(obsVals);
		 *		this.ShowListMatrix(shuffledStates);
		 */		

		StatisticHandler [] stats = new StatisticHandler[6 + numStates];
		stats[0] = new StatisticHandler(obsVals[0], nulVals[0], true);
		stats[1] = new StatisticHandler(obsVals[1], nulVals[1], true);
		stats[2] = new StatisticHandler(obsVals[2], nulVals[2], true);
		stats[3] = new StatisticHandler(obsVals[3], nulVals[3], true);
		stats[4] = new StatisticHandler(obsVals[4], nulVals[4], true);
		stats[5] = new StatisticHandler(obsVals[5], nulVals[5], true);
		for(int i = 0; i<numStates; i++){
			stats[i + 6] = new StatisticHandler(obsVals[i + 6], nulVals[i + 6], false);
		}
//-		System.out.println(stats[8].getMean());
//-		System.out.println(Summary.length);
		for(int i = 0; i < (6 + numStates); i ++){
			Summary[i*3] = stats[i].getMean();
			Summary[(i*3)+1] = stats[i].getNullMean();
			Summary[(i*3)+2] = stats[i].getSignificance();
			
		}
/*		
		Summary[0] = stats[0].getMean();
		Summary[1] = stats[0].getNullMean();
		Summary[2] = stats[0].getSignificance();
		Summary[3] = stats[1].getMean();
		Summary[4] = stats[1].getNullMean();
		Summary[5] = stats[1].getSignificance();
		Summary[6] = stats[2].getMean();
		Summary[7] = stats[2].getNullMean();
		Summary[8] = stats[2].getSignificance();
		Summary[9] = stats[3].getMean();
		Summary[10] = stats[3].getNullMean();
		Summary[11] = stats[3].getSignificance();
*/
		/*
		 * 		System.out.println("mean: "+stats[0].getMean());
		 *		System.out.println("n-mean: "+stats[0].getNullMean());
		 *		System.out.println("median: "+stats[0].getMedian());
		 *		System.out.println("n-median: "+stats[0].getNullMedian());
		 *		System.out.println("p: "+stats[0].getSignificance());
		 *		System.out.println("range: "+stats[0].getRangeInterval());
		 *		System.out.println("n-range: "+stats[0].getNullRangeInterval());
		 */
		return Summary;
	}
	
//
	public double[] analyse(String filename){
			/*			
			 *			2006/12/07
			 * 
			 * 			perhaps where things are going wrong is in taking
			 * 			the mean of a single row's null trees - really ought 
			 * 			to translate all 'cols' of a treefile's stats and get their_ 
			 * 			means into a single vector to return significance against....
			 * 
			 * 			e.g., start each tree with a new row 'reps AIs' or something, 
			 * 			then at the end of the treefile translate down to get means, AImeanNulls
			 * 			and run new StatHandler(etc) on that.
			 */			
	
			/*
			 * 		summary of triplets in form {mean, null mean, significance}
			 * 		order:
			 * 			0-2: AI
			 * 			3-5: migrations
			 * 			6-8: nc1 max size
			 * 			9-11: nc2 max size
			 */		
			
			/*
			 * 		UPDATE 30/01/2007
			 * 	
			 * 		this method assumes 2 states
			 * 		for the generalized n-state analysis, you
			 * 		MUST use generalizedAnalyse()
			 * 
			 * 		note that although each treefile analysed
			 * 		in generalizedAnalyse can have different numbers
			 * 		of states, if doing a massBURP they must
			 * 		all have the same...
			 */
			this.availableStates = this.makeStates(taxaStates);
			double[] Summary = new double[12];
			shuffledStates = new GeneralizedShuffler(taxaStates, reps).shuffle();
			obsVals = new float[4][treefile.length];
			System.out.println("analysing... " + treefile.length + " trees, with " + availableStates.length + " states");
			/*
			 * 			if doing reps, do as many as we have
			 */
			if (reps > 0) {
				nulVals = new float[4][reps];
	
				for (int replicate=0; replicate<reps; replicate ++) {
					System.out.println("analysing null " + replicate);
					System.out.println("shuffled states:");
					for(String state:shuffledStates.get(replicate)){
						System.out.println("\t" + state);
					}
	//o				float[] holder = new float[treefile.length];
					int index = 0;
					for(String aTree:treefile){
						System.out.println("\ttree " + index);
						ArrayList<String> stateArrangement = shuffledStates.get(replicate);
						tree nullTree = new tree(aTree, taxaNames, stateArrangement, availableStates);
						double nullAI = nullTree.getAI();
						int nullMigrations = nullTree.getMigrationEvents();
						int nullNcOne = nullTree.getBiggestExclusiveNodeOf(0);
						int nullNcTwo = nullTree.getBiggestExclusiveNodeOf(0);
						nulVals[0][replicate] += nullAI;
						nulVals[1][replicate] += nullMigrations;
						nulVals[2][replicate] += nullNcOne;
						nulVals[3][replicate] += nullNcTwo;
	//					o					holder[index] = (float)nullAI;
						index ++;
					}
					nulVals[0][replicate] = nulVals[0][replicate]/treefile.length;
					nulVals[1][replicate] = nulVals[1][replicate]/treefile.length;
					nulVals[2][replicate] = nulVals[2][replicate]/treefile.length;
					nulVals[3][replicate] = nulVals[3][replicate]/treefile.length;
					/*				StatisticHandler tempHandler = new StatisticHandler(holder, nullarr, false);
					System.out.println("mean "+ tempHandler.getMean());
					System.out.println(" other mean "+ nulVals[0][replicate]);
					System.out.println("interval "+ tempHandler.getRangeInterval());
					 */			}
			}
	
			/*
			 * 			observed tree stats for actual tree in MCMC chain
			 */
	
			int count = 0;
			if (doSelfRandomisations) {
				System.out.println("analysing observed (self randomisations; discarding state data)");
				ArrayList<String> tempRearrangement = new GeneralizedShuffler(taxaStates,1).shuffle().get(0);
				for(String aTree:treefile){
					System.out.println("\ttree "+ count);
					EnhancedTree t = new EnhancedTree(aTree, taxaNames, tempRearrangement, availableStates);
	
					double ai = t.getAI();
					int migrations = t.getMigrationEvents();
					int ncOne = t.getBiggestExclusiveNodeOf(0);
					int ncTwo = t.getBiggestExclusiveNodeOf(0);
	
					obsVals [0][count] = (float)ai;
					obsVals [1][count] = (float)migrations;
					obsVals [2][count] = (float)ncOne;
					obsVals [3][count] = (float)ncTwo;
	
					count ++;
					if(count == treefile.length){
						lastTree = t;
					}
				}
			} else {	
				System.out.println("analysing observed (using obs state data)");
				for(String aTree:treefile){
					System.out.println("\ttree "+ count);
					EnhancedTree t = new EnhancedTree(aTree, taxaNames, taxaStates, availableStates);
	
					double ai = t.getAI();
					int migrations = t.getMigrationEvents();
					int ncOne = t.getBiggestExclusiveNodeOf(0);
					int ncTwo = t.getBiggestExclusiveNodeOf(1);
	
					obsVals [0][count] = (float)ai;
					obsVals [1][count] = (float)migrations;
					obsVals [2][count] = (float)ncOne;
					obsVals [3][count] = (float)ncTwo;
	
					count ++;
					if(count == treefile.length){
						lastTree = t;
					}
				}
			}	
	
			/*		for(int i=0;i<reps;i++){
			 *			
			 *			nulVals[0][i] = new StatisticHandler(nullValsHolder[i], nullarr, false).getMean();
			 *			System.out.println(new StatisticHandler(nullValsHolder[i], nullarr, false).getMedian());
			 * 			System.out.println(new StatisticHandler(nullValsHolder[i], nullarr, false).getMean());
			 *		}
			 */
	
			/*		System.out.println(nullValsHolder[0].length);
			 *		System.out.println(nullValsHolder[0][0]);
			 */		
	//		-		System.out.println("printout of nVals holder:\n");
	//		-		this.ShowMatrix(nullValsHolder);
	
			/*
			 *
			 * 		Uncommment to enable individual reporting of
			 * 		stats
			 * 
			 * 		System.out.println("printout of nVals:\n");
			 *		this.ShowMatrix(nulVals);
			 *		System.out.println("printout of oVals:\n");
			 *		this.ShowMatrix(obsVals);
			 *		this.ShowListMatrix(shuffledStates);
			 */		
	
			StatisticHandler [] stats = new StatisticHandler[4];
			stats[0] = new StatisticHandler(obsVals[0], nulVals[0], true);
			stats[1] = new StatisticHandler(obsVals[1], nulVals[1], true);
			stats[2] = new StatisticHandler(obsVals[2], nulVals[2], false);
			stats[3] = new StatisticHandler(obsVals[3], nulVals[3], false);
	
			Summary[0] = stats[0].getMean();
			Summary[1] = stats[0].getNullMean();
			Summary[2] = stats[0].getSignificance();
			Summary[3] = stats[1].getMean();
			Summary[4] = stats[1].getNullMean();
			Summary[5] = stats[1].getSignificance();
			Summary[6] = stats[2].getMean();
			Summary[7] = stats[2].getNullMean();
			Summary[8] = stats[2].getSignificance();
			Summary[9] = stats[3].getMean();
			Summary[10] = stats[3].getNullMean();
			Summary[11] = stats[3].getSignificance();
	
			/*
			 * 		System.out.println("mean: "+stats[0].getMean());
			 *		System.out.println("n-mean: "+stats[0].getNullMean());
			 *		System.out.println("median: "+stats[0].getMedian());
			 *		System.out.println("n-median: "+stats[0].getNullMedian());
			 *		System.out.println("p: "+stats[0].getSignificance());
			 *		System.out.println("range: "+stats[0].getRangeInterval());
			 *		System.out.println("n-range: "+stats[0].getNullRangeInterval());
			 */
			return Summary;
		}

	public String[] readTreeFile(String filename){
		ArrayList<String> treeFileAL = new ArrayList<String>();
		try{
			File thetreeFileAL  = new File(filename);
			FileReader fr = new FileReader(thetreeFileAL);
			BufferedReader br  = new BufferedReader(fr);
			String line = null;
			boolean inAssignBlock = false;
			boolean inTreesBlock = false;
			Pattern assign = Pattern.compile("BEGIN STATES;");			
			Pattern trees = Pattern.compile("BEGIN TREES;");			
			Pattern end = Pattern.compile("END;");
			this.taxaNames = new ArrayList<String>();

			while((line = br.readLine())!= null){

				
				Matcher isAssign = assign.matcher(line.toUpperCase());
				Matcher isTrees = trees.matcher(line.toUpperCase());
				Matcher isEnd = end.matcher(line.toUpperCase());

				if(isEnd.matches()){
//					c					// have just left a block
					inAssignBlock = false;
					inTreesBlock = false;
				}

				if(inAssignBlock){
//					c					// collect names & states
					String [] result = line.split("\\ ");
					this.taxaNames.add(result[0]);
					this.taxaStates.add(result[1]);
				}

				if(inTreesBlock){
					String [] result = line.split("\\[&R\\]\\ ");
					/*					for(String token:result){
						System.out.println(token + " token of line " + line);
					}
					 */
					int penultimatechar = result[1].length() - 1;
					String atLast = result[1].substring(0,penultimatechar);
					treeFileAL.add(atLast);
				}

				if(isTrees.matches()){
//					c					// in trees block
					inTreesBlock = true;
				}

				if(isAssign.matches()){
					// in states block
					inAssignBlock = true;
				}
//				-				System.out.println(">>>>" + line + " " + inTreesBlock + " " + inAssignBlock);
			}
			br.close();
		} catch(Exception ex){
			System.out.println("Not read file " + filename);
			ex.printStackTrace();
		}
		String[] treeStringList = new String[treeFileAL.size()];
		for(int i=0;i<treeFileAL.size();i++){
			treeStringList[i] = treeFileAL.get(i);
		}
		this.treefile = treeStringList;
		return treeStringList;
	}

	public String [] makeStates(ArrayList<String>listOfStates){
		ArrayList<String> uniqueStates = new ArrayList<String>();
		for(String possible: listOfStates){
			if(!uniqueStates.contains(possible)){
				uniqueStates.add(possible);
//-				System.out.println(possible + " added");
			}
		}
		String [] statesMade = new String [uniqueStates.size()];
		for(int i = 0; i<uniqueStates.size(); i ++){
			statesMade[i] = uniqueStates.get(i);
		}
		return statesMade;
	}

	public ArrayList<String> getTaxaStates(){
		return taxaStates;
	}

	public void ShowLastTree(){
		lastTree.showResult();
	}

	public float getMean(float[] vals){
		float mean = 0;
		for(float aVal:vals){
			mean += aVal;
		}
		return mean / vals.length;
	}

	public void setSelfRandomisation(boolean newRandomMode){
		this.doSelfRandomisations = newRandomMode;
	}

	public void ShowMatrix(float[][] input){
		int hLen = input.length;
		int vLen = input[0].length;
		for(int h=0;h<hLen;h++){
			for(int v=0;v<vLen;v++){
				System.out.print(input[h][v]+"\t");
			}
			System.out.println();
		}
	}

	public void ShowListMatrix(ArrayList<ArrayList<String>> input){
		for(ArrayList<String> row:input){
			for(String val:row){
				System.out.print(val+"\t");
			}
			System.out.println();
		}
	}

	/**
	 * @return the ncCols
	 */
	public int getNcCols() {
		return ncCols;
	}

	/**
	 * @return the numStates
	 */
	public int getNumStates() {
		return numStates;
	}

	/**
	 * @param ncCols the ncCols to set
	 */
	public void setNcCols(int ncCols) {
		this.ncCols = ncCols;
	}

	/**
	 * @param numStates the numStates to set
	 */
	public void setNumStates(int numStates) {
		this.numStates = numStates;
	}

	/**
	 * @return the lastTree
	 */
	public EnhancedTree getLastTree() {
		return lastTree;
	}

	/**
	 * @param lastTree the lastTree to set
	 */
	public void setLastTree(EnhancedTree lastTree) {
		this.lastTree = lastTree;
	}
	
	public int getLastTreeMigrationEvents(){
		return lastTree.getMigrationEvents();
	}
}