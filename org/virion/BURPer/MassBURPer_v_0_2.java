package org.virion.BURPer;

// ver 0.1 //


import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.virion.shuffler;
import org.virion.StatisticHandler;

public class MassBURPer_v_0_2{

	private	ArrayList<String> treefile = new ArrayList<String>();
	private ArrayList<String> taxaNames = new ArrayList<String>();
	private ArrayList<String> taxaStates = new ArrayList<String>();
	private ArrayList<ArrayList<String>> shuffledStates = new ArrayList<ArrayList<String>>();
	private ArrayList<String> rootHeights = new ArrayList<String>();
	private ArrayList<String> stateOneCladeSize = new ArrayList<String>();
	private ArrayList<String> stateTwoCladeSize = new ArrayList<String>();
	private ArrayList<String> stateOneCladeHeight = new ArrayList<String>();
	private ArrayList<String> stateTwoCladeHeight = new ArrayList<String>();
	private ArrayList<String> AI = new ArrayList<String>();
	private ArrayList<String> migrations = new ArrayList<String>();
	private ArrayList<String> nullRootHeights = new ArrayList<String>();
	private ArrayList<String> nullStateOneCladeSize = new ArrayList<String>();
	private ArrayList<String> nullStateTwoCladeSize = new ArrayList<String>();
	private ArrayList<String> nullStateOneCladeHeight = new ArrayList<String>();
	private ArrayList<String> nullStateTwoCladeHeight = new ArrayList<String>();
	private ArrayList<String> nullAI = new ArrayList<String>();
	private ArrayList<String> nullMigrations = new ArrayList<String>();
	private float[][] obsVals;
	private float[][] nulVals;

	private String [] availableStates = new String[2];
	private shuffler s;
	private final int reps;
	private tree lastTree;

	public MassBURPer_v_0_2(int reps){
		this.reps = reps;
	}
		
	public double[] analyse(String filename){
		double[] Summary = new double[21];
		availableStates = this.makeStates(taxaStates);
		shuffler s = new shuffler(taxaStates, reps);
		shuffledStates = s.shuffle();
		obsVals = new float[7][treefile.size()];
		if(reps > 0){
			nulVals = new float[7][treefile.size()];
		}
		System.out.println("analysing... with " + availableStates.length + " states");
		int count = 0;
		for(String aTree:treefile){
/*
 * 			if doing reps, do as many as we have
 */
			if (reps > 0) {
				float [] nullRootHeightsSum = new float[reps];
				float [] nullStateOneCladeHeightsSum = new float[reps];
				float [] nullStateTwoCladeHeightsSum = new float[reps];
				float [] nullStateOneCladeSizesSum = new float[reps];
				float [] nullStateTwoCladeSizesSum = new float[reps];
				float [] nullMigrationsSum = new float[reps];
				float [] nullAIsum = new float[reps];
				for (int replicate=0; replicate<reps; replicate ++) {
					ArrayList<String> stateArrangement = shuffledStates.get(replicate);
					tree nullTree = new tree(aTree, taxaNames,
							stateArrangement, availableStates);
					nullRootHeightsSum[replicate] = nullTree.getHeight();
					nullStateOneCladeSizesSum[replicate] = nullTree
							.getBiggestExclusiveNodeOf(0);
					nullStateOneCladeHeightsSum[replicate] = (nullTree.getHeight() - nullTree
							.getHeightOfBiggestExclusiveNodeOf(0));
					nullStateTwoCladeSizesSum[replicate] = nullTree
							.getBiggestExclusiveNodeOf(1);
					nullStateTwoCladeHeightsSum[replicate] = (nullTree.getHeight() - nullTree
							.getHeightOfBiggestExclusiveNodeOf(1));
					nullAIsum[replicate] = (float)nullTree.getAI();
					nullMigrationsSum[replicate] = (nullTree.getMigrationEvents());
				}
				float[] nullarr = new float[1];
				nullarr[0] = 0;
				float tHeight = new StatisticHandler(nullRootHeightsSum, nullarr, true).getMean();
				float maxOneSize = new StatisticHandler(nullStateOneCladeSizesSum, nullarr,true).getMean();
				float maxOneHeight = new StatisticHandler(nullStateOneCladeHeightsSum, nullarr,true).getMean();
				float maxTwoSize = new StatisticHandler(nullStateTwoCladeSizesSum, nullarr,true).getMean();
				float maxTwoHeight = new StatisticHandler(nullStateTwoCladeHeightsSum, nullarr,true).getMean();
				float ai = new StatisticHandler(nullAIsum, nullarr,true).getMean();
				float migs = new StatisticHandler(nullMigrationsSum, nullarr,true).getMean();

				nullRootHeights.add(Float.toString(tHeight));
				nulVals [0][count] = tHeight;
				nullStateOneCladeSize.add(Float.toString(maxOneSize));
				nulVals [1][count] = maxOneSize;
				nullStateOneCladeHeight.add(Float.toString(maxOneHeight));
				nulVals [2][count] = maxOneHeight;
				nullStateTwoCladeSize.add(Float.toString(maxTwoSize));
				nulVals [3][count] = maxTwoSize;
				nullStateTwoCladeHeight.add(Float.toString(maxTwoHeight));
				nulVals [4][count] = maxTwoHeight;
				nullAI.add(Float.toString(ai));
				nulVals [5][count] = ai;
				nullMigrations.add(Float.toString(migs));
				nulVals [6][count] = migs;
			}

/*
 * 			observed tree stats for actual tree in MCMC chain
 */

			tree t = new tree(aTree, taxaNames, taxaStates, availableStates);
			float tHeight = t.getHeight();
			int maxOneSize = t.getBiggestExclusiveNodeOf(0);
			float maxOneHeight = tHeight - t.getHeightOfBiggestExclusiveNodeOf(0);
			int maxTwoSize = t.getBiggestExclusiveNodeOf(1);
			float maxTwoHeight = tHeight - t.getHeightOfBiggestExclusiveNodeOf(1);
			double ai = t.getAI();
			int migs = t.getMigrationEvents();
			rootHeights.add(Float.toString(tHeight));
			obsVals [0][count] = tHeight;
			stateOneCladeSize.add(Integer.toString(maxOneSize));
			obsVals [1][count] = (float)maxOneSize;
			stateOneCladeHeight.add(Float.toString(maxOneHeight));
			obsVals [2][count] = maxOneHeight;
			stateTwoCladeSize.add(Integer.toString(maxTwoSize));
			obsVals [3][count] = (float)maxTwoSize;
			stateTwoCladeHeight.add(Float.toString(maxTwoHeight));
			obsVals [4][count] = maxTwoHeight;
			AI.add(Double.toString(ai));
			obsVals [5][count] = (float)ai;
			migrations.add(Integer.toString(migs));
			obsVals [6][count] = (float)migs;
			//c			get the average of null stats			
			System.out.println(count);
	//-		System.out.println("null rh mean " + (nullRootHeightsSum / reps));
			count ++;
			if(count == treefile.size()){
				lastTree = t;
			}
		}	

/*
 * 		will need to put a bit in here
 * 		where i collect the (reps) mean null observations in an array
 * 		and then do new stathnadler(obsVals, nullMeanVals).getsignificance
 */		
		
		Summary[0] = new StatisticHandler(obsVals[0], nulVals[0], false).getMean();
		Summary[1] = new StatisticHandler(obsVals[0], nulVals[0], false).getNullMean();
		Summary[2] = new StatisticHandler(obsVals[0], nulVals[0], false).getSignificance();
		Summary[3] = new StatisticHandler(obsVals[1], nulVals[1], false).getMean();
		Summary[4] = new StatisticHandler(obsVals[1], nulVals[1], false).getNullMean();
		Summary[5] = new StatisticHandler(obsVals[1], nulVals[1], false).getSignificance();
		Summary[6] = new StatisticHandler(obsVals[2], nulVals[2], false).getMean();
		Summary[7] = new StatisticHandler(obsVals[2], nulVals[2], false).getNullMean();
		Summary[8] = new StatisticHandler(obsVals[2], nulVals[2], false).getSignificance();
		Summary[9] = new StatisticHandler(obsVals[3], nulVals[3], false).getMean();
		Summary[10] = new StatisticHandler(obsVals[3], nulVals[3], false).getNullMean();
		Summary[11] = new StatisticHandler(obsVals[3], nulVals[3], false).getSignificance();
		Summary[12] = new StatisticHandler(obsVals[4], nulVals[4], false).getMean();
		Summary[13] = new StatisticHandler(obsVals[4], nulVals[4], false).getNullMean();
		Summary[14] = new StatisticHandler(obsVals[4], nulVals[4], false).getSignificance();
		Summary[15] = new StatisticHandler(obsVals[5], nulVals[5], true).getMean();
		Summary[16] = new StatisticHandler(obsVals[5], nulVals[5], true).getNullMean();
		Summary[17] = new StatisticHandler(obsVals[5], nulVals[5], true).getSignificance();
		Summary[18] = new StatisticHandler(obsVals[6], nulVals[6], true).getMean();
		Summary[19] = new StatisticHandler(obsVals[6], nulVals[6], true).getNullMean();
		Summary[20] = new StatisticHandler(obsVals[6], nulVals[6], true).getSignificance();

		return Summary;
	}
	
	public void readTreeFile(String filename){
		try{
			File theTreeFile  = new File(filename);
			FileReader fr = new FileReader(theTreeFile);
			BufferedReader br  = new BufferedReader(fr);
			String line = null;
			boolean inAssignBlock = false;
			boolean inTreesBlock = false;
			Pattern assign = Pattern.compile("begin states;");			
			Pattern trees = Pattern.compile("begin trees;");			
			Pattern end = Pattern.compile("End;");


			while((line = br.readLine())!= null){
		
				Matcher isAssign = assign.matcher(line);
				Matcher isTrees = trees.matcher(line);
				Matcher isEnd = end.matcher(line);

				if(isEnd.matches()){
//c					// have just left a block
					inAssignBlock = false;
					inTreesBlock = false;
				}
				
				if(inAssignBlock){
//c					// collect names & states
					String [] result = line.split("\\ ");
					taxaNames.add(result[0]);
					taxaStates.add(result[1]);
				}

				if(inTreesBlock){
					String [] result = line.split("\\[&R\\]\\ ");
/*					for(String token:result){
						System.out.println(token + " token of line " + line);
					}
*/
					int penultimatechar = result[1].length() - 1;
					String atLast = result[1].substring(0,penultimatechar);
					treefile.add(atLast);
				}

				if(isTrees.matches()){
//c					// in trees block
					inTreesBlock = true;
				}

				if(isAssign.matches()){
					// in states block
					inAssignBlock = true;
				}
//-				System.out.println(">>>>" + line + " " + inTreesBlock + " " + inAssignBlock);
			}
			br.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String [] makeStates(ArrayList<String>listOfStates){
		String [] statesMade = new String [2];
		statesMade[0] = listOfStates.get(0);
		for(String stateListItem : listOfStates){
			if(!stateListItem.equals(statesMade[0])){
				statesMade[1] = stateListItem;
				break;
			}
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
}