package org.virion.BURPer;

// ver 4.2 //
/* really ought to implement some kind
 * of collation / p-value estimation method...
 */

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.virion.shuffler;
import org.virion.StatisticHandler;

public class BURPer{

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
	private ArrayList<String> topnodeState = new ArrayList<String>();
	private ArrayList<String> nullRootHeights = new ArrayList<String>();
	private ArrayList<String> nullStateOneCladeSize = new ArrayList<String>();
	private ArrayList<String> nullStateTwoCladeSize = new ArrayList<String>();
	private ArrayList<String> nullStateOneCladeHeight = new ArrayList<String>();
	private ArrayList<String> nullStateTwoCladeHeight = new ArrayList<String>();
	private ArrayList<String> nullAI = new ArrayList<String>();
	private ArrayList<String> nullMigrations = new ArrayList<String>();
	private ArrayList<String> nulltopnodeState = new ArrayList<String>();
	private float[][] obsVals;
	private float[][] nulVals;

	private LogWriter lW = new LogWriter();
	private String [] availableStates = new String[2];
	private shuffler s;
	private final int reps;
	private tree lastTree;

	public BURPer(int reps){
		this.reps = reps;
/*		rootHeights.add("tree root");
		stateOneCladeSize.add("n-c size clade 1");
		stateOneCladeHeight.add("n-c height clade 1");
		stateTwoCladeSize.add("n-c size clade 2");
		stateTwoCladeHeight.add("n-c height clade 2");
		nullRootHeights.add("null tree root");
		nullStateOneCladeSize.add("null n-c size clade 1");
		nullStateOneCladeHeight.add("null n-c height clade 1");
		nullStateTwoCladeSize.add("null n-c size clade 2");
		nullStateTwoCladeHeight.add("null n-c height clade 2");
		AI.add("AI");
		nullAI.add("null AI");
		migrations.add("migrations");
		nullMigrations.add("nullMigrations");
*/
	}
		
	public void generateNamesAndStates(){
		ArrayList<String> taxaNames = new ArrayList<String>();
		for(int i=1; i<11; i++){
			String nextTaxon = "" + i;
			taxaNames.add(nextTaxon);
		}

		ArrayList<String> taxaStates = new ArrayList<String>();
		for(int i=0; i<6; i++){
			String nextState = "black";
			taxaStates.add(nextState);
		}
		for(int i=0; i<4; i++){
			String nextState = "white";
			taxaStates.add(nextState);
		}

		for(int i=0; i<0; i++){
			String nextState = "black";
			taxaStates.add(nextState);
		}

//		String treefile = "((taxon8:0.5884114897967825,(taxon1:0.26134586148024586,(taxon2:0.17339490064933183,taxon4:0.17339490064933183):0.08795096083091403):0.3270656283165366):0.4749786152981885,((taxon6:0.6745410909486028,taxon5:0.09674049774760174):0.46393010869201545,((taxon9:0.3176241161518942,(taxon10:0.2559460124483275,taxon3:0.2559460124483275):0.06167810370356669):0.5767040541544235,taxon7:0.31652757710531654):0.24414302933430065):0.5027194986553538)";
//		String treefile = "(taxonA,taxonB,(taxonC,taxonD),(taxonE,taxonF),taxonZ)";
//		(an alternative treefile)
		
//		treefile.add("(1:0.5636765910046355,(((5:0.10495899696817945,6:0.6827595901691805):0.2549015574466058,(((3:0.2237339057219523,9:0.2237339057219523):0.12024159304779908,10:0.34397549876975136):0.4679878143946243,7:0.23416271996337457):0.1256978344514107):0.06032016161234732,((2:0.18067113851853944,4:0.18067113851853944):0.20191022976733908,8:0.3825813682858785):0.03759934774125406):0.14349587497750294)");
//		treefile.add("(((2:0.10637412416791103,4:0.10637412416791103):0.22897905586236156,(1:0.3149560537236302,8:0.3149560537236302):0.020397126306642366):0.30132313449866943,((((9:0.2897072273531875,3:0.2897072273531875):0.05764352807187184,10:0.34735075542505933):0.402133496670675,7:0.17168365889473325):0.13232460783735067,(5:0.13814005747927172,6:0.7159406506802728):0.1658682092528122):0.3326680477968581)");
//		treefile.add("((((7:0.23145505642720454,(5:0.04868512566300398,6:0.6264857188640051):0.18276993076420056):0.0035054144353949823,((10:0.21145985801642891,3:0.21145985801642891):0.4273002918286099,9:0.6387601498450388):0.1740009142185618):0.18493925495380714,8:0.41989972581640667):0.004787152780931003,((2:0.16015633811612062,4:0.16015633811612062):0.05716164363156273,1:0.21731798174768335):0.20736889684965432)");
//		treefile.add("(((5:0.2571790763828311,6:0.8349796695838322):0.8016470347777181,((8:0.2525235895643775,1:0.2525235895643775):0.1246218872456979,(2:0.21896680081204245,4:0.21896680081204245):0.15817867599803292):0.6816806343504739):0.07955580571683685,((3:0.44662297813244106,9:0.44662297813244106):0.6780871600216691,(10:0.8535456761080253,7:0.27574508290702426):0.2711644620460849):0.5914723719242769)");
//		treefile.add("((7:0.4953180413889593,((3:0.5302358034646466,10:0.5302358034646466):0.028777036877094764,9:0.5590128403417414):0.514105794248219):0.46072999291215444,(((2:0.08559340257221482,4:0.08559340257221482):0.28356555085126456,(8:0.0660411823663356,1:0.0660411823663356):0.30311777105714377):0.38197080934577743,(5:0.10529174123248486,6:0.6830923344334859):0.645838021536772):0.20491827153185693)");
//		(more alternative treefile lines)
	}
	
	public void analyse(String filename){
//o		ArrayList<tree> completedTrees = new ArrayList<tree>();
//o		availableStates [0] = "white";
//o		availableStates [1] = "black";
		availableStates = this.makeStates(taxaStates);
		shuffler s = new shuffler(taxaStates, reps);
		shuffledStates = s.shuffle();
		obsVals = new float[8][treefile.size()];
		if(reps > 0){
			nulVals = new float[8][treefile.size()];
		}
		System.out.println("analysing... with " + availableStates.length + " states");
		int count = 0;
		for(String aTree:treefile){
/*
 * 			if doing reps, do as many as we have
 */
			if (reps > 0) {
				float nullRootHeightsSum = 0;
				float nullStateOneCladeHeightsSum = 0;
				float nullStateTwoCladeHeightsSum = 0;
				int nullStateOneCladeSizesSum = 0;
				int nullStateTwoCladeSizesSum = 0;
				float nullMigrationsSum = 0;
				float nullAIsum = 0;
				int nullTopnodeStateSum = 0;
				for (ArrayList<String> stateArrangement : shuffledStates) {
//c					null tree stats for each of the replicate trees in the shuffled taxa state
//-					System.out.println("i'm here.."+taxaNames+availableStates[0]+availableStates[1]+stateArrangement);
					tree nullTree = new tree(aTree, taxaNames,
							stateArrangement, availableStates);
					nullRootHeightsSum += nullTree.getHeight();
					nullStateOneCladeSizesSum += nullTree
							.getBiggestExclusiveNodeOf(0);
					nullStateOneCladeHeightsSum += (nullTree.getHeight() - nullTree
							.getHeightOfBiggestExclusiveNodeOf(0));
					nullStateTwoCladeSizesSum += nullTree
							.getBiggestExclusiveNodeOf(1);
					nullStateTwoCladeHeightsSum += (nullTree.getHeight() - nullTree
							.getHeightOfBiggestExclusiveNodeOf(1));
					nullAIsum += (nullTree.getAI());
					nullMigrationsSum += (nullTree.getMigrationEvents());
					String nullTopnodeState = nullTree.getTopnodeState();
					if(nullTopnodeState.equals("<empty state>")){
						nullTopnodeStateSum += 0.5;
					}else if(nullTopnodeState.equals(taxaStates.get(0))){
						nullTopnodeStateSum ++;
					} else {
						// do nowt.
					}
				}
				float tHeight = nullRootHeightsSum / reps;
				float maxOneSize = nullStateOneCladeSizesSum / reps;
				float maxOneHeight = nullStateOneCladeHeightsSum / reps;
				float maxTwoSize = nullStateTwoCladeSizesSum / reps;
				float maxTwoHeight = nullStateTwoCladeHeightsSum / reps;
				float ai = nullAIsum / reps;
				float migs = nullMigrationsSum / reps;
				float tns = nullTopnodeStateSum / reps;
				
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
				nulltopnodeState.add(Float.toString(tns));
				nulVals [7][count] = tns;
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
			String actualTNS = t.getTopnodeState();
			if(actualTNS.equals("<empty state>")){
				obsVals [7][count] = (float)0.5;
				topnodeState.add("0.5");
			}else if(actualTNS.equals(taxaStates.get(0))){
				obsVals [7][count] = (float)0;
				topnodeState.add("0");
			}else{
				obsVals [7][count] = (float)1;
				topnodeState.add("1");
			}
			//c			get the average of null stats			
			System.out.println(count);
	//-		System.out.println("null rh mean " + (nullRootHeightsSum / reps));
			count ++;
			if(count == treefile.size()){
				lastTree = t;
			}
		}	
	
		lW.addList(rootHeights);
		lW.addList(stateOneCladeSize);
		lW.addList(stateOneCladeHeight);
		lW.addList(stateTwoCladeSize);
		lW.addList(stateTwoCladeHeight);
		lW.addList(AI);
		lW.addList(migrations);
		lW.addList(topnodeState);

//		-		System.out.println(rootHeights.size());
//		-		System.out.println(stateOneCladeSize.size());
//		-		System.out.println(stateOneCladeHeight.size());
//		-		System.out.println(stateTwoCladeSize.size());
//		-		System.out.println(stateTwoCladeHeight.size());

		if (reps > 0) {
			lW.addList(nullRootHeights);
			lW.addList(nullStateOneCladeSize);
			lW.addList(nullStateOneCladeHeight);
			lW.addList(nullStateTwoCladeSize);
			lW.addList(nullStateTwoCladeHeight);
			lW.addList(nullAI);
			lW.addList(nullMigrations);
			lW.addList(nulltopnodeState);
			//		-		System.out.println(nullRootHeights.size());
			//		-		System.out.println(nullStateOneCladeSize.size());
			//		-		System.out.println(nullStateOneCladeHeight.size());
			//		-		System.out.println(nullStateTwoCladeSize.size());
			//		-		System.out.println(nullStateTwoCladeHeight.size());
		}
		
		lW.write(filename);

 
 		System.out.println(lW.getOutFile());
/*		for(int i=0; i<7; i++){
			System.out.println(getMean(obsVals[i]));
			System.out.println(getMean(nulVals[i]));
		}
*/
		System.out.println("mean (observed) values:");
		System.out.println("mean (null) values:");
		System.out.println("p-values:");
		System.out.println(" OBS:tree root height\t" + new StatisticHandler(obsVals[0], nulVals[0], false).getMean());
		System.out.println(" EXP:tree root height\t" + new StatisticHandler(obsVals[0], nulVals[0], false).getNullMean());
		System.out.println(" P-v:tree root height\t" + new StatisticHandler(obsVals[0], nulVals[0], false).getSignificance());
		System.out.println(" OBS:state 1 max size\t" + new StatisticHandler(obsVals[1], nulVals[1], false).getMean());
		System.out.println(" EXP:state 1 max size\t" + new StatisticHandler(obsVals[1], nulVals[1], false).getNullMean());
		System.out.println(" P-v:state 1 max size\t" + new StatisticHandler(obsVals[1], nulVals[1], false).getSignificance());
		System.out.println(" OBS:state 1 max height\t" + new StatisticHandler(obsVals[2], nulVals[2], false).getMean());
		System.out.println(" EXP:state 1 max height\t" + new StatisticHandler(obsVals[2], nulVals[2], false).getNullMean());
		System.out.println(" P-v:state 1 max height\t" + new StatisticHandler(obsVals[2], nulVals[2], false).getSignificance());
		System.out.println(" OBS:state 2 max size\t" + new StatisticHandler(obsVals[3], nulVals[3], false).getMean());
		System.out.println(" EXP:state 2 max size\t" + new StatisticHandler(obsVals[3], nulVals[3], false).getNullMean());
		System.out.println(" P-v:state 2 max size\t" + new StatisticHandler(obsVals[3], nulVals[3], false).getSignificance());
		System.out.println(" OBS:state 2 max height\t" + new StatisticHandler(obsVals[4], nulVals[4], false).getMean());
		System.out.println(" EXP:state 2 max height\t" + new StatisticHandler(obsVals[4], nulVals[4], false).getNullMean());
		System.out.println(" P-v:state 2 max height\t" + new StatisticHandler(obsVals[4], nulVals[4], false).getSignificance());
		System.out.println(" OBS:association index\t" + new StatisticHandler(obsVals[5], nulVals[5], true).getMean());
		System.out.println(" EXP:association index\t" + new StatisticHandler(obsVals[5], nulVals[5], true).getNullMean());
		System.out.println(" P-v:association index\t" + new StatisticHandler(obsVals[5], nulVals[5], true).getSignificance());
		System.out.println(" OBS:migrations\t" + new StatisticHandler(obsVals[6], nulVals[6], true).getMean());
		System.out.println(" EXP:migrations\t" + new StatisticHandler(obsVals[6], nulVals[6], true).getNullMean());
		System.out.println(" P-v:migrations\t" + new StatisticHandler(obsVals[6], nulVals[6], true).getSignificance());
		System.out.println(" OBS:topnode\t" + new StatisticHandler(obsVals[7], nulVals[7], true).getMean());
		System.out.println(" EXP:topnode\t" + new StatisticHandler(obsVals[7], nulVals[7], true).getNullMean());
		System.out.println(" P-v:topnode\t" + new StatisticHandler(obsVals[7], nulVals[7], true).getSignificance());

		/*		float [] oT = new float[] {12,12,12,12,12,12};
		float [] nT = new float[] {4,4,63,4,3,6};
		System.out.println(new StatisticHandler(oT,nT,false).getSignificance());
*/		
//o	for(tree oneTree:completedTrees){
//-			System.out.println("RESULT:");
//-			oneTree.showResult();
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("hoo-fucking-ray! the top node state is " + oneTree.getTopnodeState());
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("umm... the top node excluding state 1 is " + oneTree.getHighestNodeExcluding(1));
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("umm... the top node excluding state 0 is " + oneTree.getHighestNodeExcluding(0));
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("tree height " + oneTree.getHeight());
//o		rootHeights.add(Float.toString(oneTree.getHeight()));
//o	}
		
//o	for(tree oneTree:completedTrees){
//-			System.out.println("biggest node of state 1 is " + oneTree.getBiggestExclusiveNodeOf(0));
//o		stateOneCladeSize.add(Integer.toString(oneTree.getBiggestExclusiveNodeOf(0)));
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("height of biggest node of state 1 is " + oneTree.getHeightOfBiggestExclusiveNodeOf(1) + " or from root is " + (oneTree.getHeight() - oneTree.getHeightOfBiggestExclusiveNodeOf(0)));
//o		stateOneCladeHeight.add(Float.toString(oneTree.getHeight() - oneTree.getHeightOfBiggestExclusiveNodeOf(0)));
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("biggest node of state 2 is " + oneTree.getBiggestExclusiveNodeOf(1));
//o		stateTwoCladeSize.add(Integer.toString(oneTree.getBiggestExclusiveNodeOf(1)));
//o	}

//o	for(tree oneTree:completedTrees){
//-			System.out.println("height of biggest node of state 2 is " + oneTree.getHeightOfBiggestExclusiveNodeOf(1) + " or from root is " + (oneTree.getHeight() - oneTree.getHeightOfBiggestExclusiveNodeOf(1)));
//o		stateTwoCladeHeight.add(Float.toString(oneTree.getHeight() - oneTree.getHeightOfBiggestExclusiveNodeOf(1)));
//o	}
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
		
//-				System.out.println(line + " " + inTreesBlock + " " + inAssignBlock);
				
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
//-		System.out.println(statesMade[0]);
//-		System.out.println(statesMade[1]);
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