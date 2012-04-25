package org.virion.BURPer;

// ver 4.1 //

import java.util.ArrayList;
import org.virion.PairwiseMatrix;

public class node{

	private node lastNode;
	private String name;
	private String tree;
	private String state = "<empty state>";
	private String parentState;
	private String [] availableStates;
	private ArrayList<node> daughters;
	private ArrayList<String> taxaNames;
	private GeneralizedFitchStates taxaGeneralizedFitchStates;
	private ArrayList<String> possibleStates = new ArrayList<String>();
	private int position;
	private final int migrations;
	private int [] stateWeights;
	private int [] availableIntStates;
	private int lowerStateChanges = 0;
	private float distance = 0;
	private float height = 0;
	private double AI = 0;
	private double sumAI = 0;
	private float[] PD;
	private float NTI = 0.0f;
	private float NRI = 0.0f;
	public float uniFrac = 0.0f;
	private boolean isTerminal = false;
	private boolean isContested = true;
	private boolean isTop = false;
	private boolean weightsSet = false;
	private boolean lowerNodesComplete = false;
	
	private boolean aiCalculated = false;

	public node(){
//c		// default no-arg constructor
		this.migrations = 0;
	}

	public node(ArrayList<node> input){
//c		// internal node constructor
		daughters = input;
		System.out.println(daughters.size()+" daughters - by internal node constructor");
		this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
		if(this.taxaGeneralizedFitchStates.isIntersection()){
			this.migrations = 1;
		} else {
			this.migrations = 0;
		}
	}
	
	public node(ArrayList<String> taxaNames, ArrayList<String> taxaStates, String tree, int lastPos, String[] availableStatesIn){

//c		//
//c		// the TOPNODE CONSTRUCTOR
//c		//

		this.taxaNames = taxaNames;
		this.availableStates = availableStatesIn;
		this.availableIntStates = new int[availableStates.length];
		for(int i=0; i<availableStates.length; i++){
			availableIntStates[i] = i;
		}
//	addition to generalise to n states
//	19/01/2007
		this.stateWeights = new int[availableStates.length];
		for(int i=0; i<availableStates.length; i++){
			stateWeights[i] = 0;
		}
// end of addition
		name = "topnode";
		distance = 0;
		daughters = new ArrayList<node>();
		int treeLength = tree.length();
		int i = lastPos;
		boolean end = false;
		node lastNode = new node();
//o		state = "<empty state>";
		isContested = true;

//o		for(String aState:availableStates){
//-			System.out.println("received " + aState);
//o		}
		
//o		stateWeights = new int[2]; //<------ now initialised in instance variables
//o		stateWeights[0] = 0;
//o		stateWeights[1] = 0;

		while(!end){
			if(i >= treeLength){break;}
			int j = i + 1;
			String here = tree.substring(i,j);
//-			System.out.println(i	 + " TOP constructor = here " + here);
//o			for(int k = 0; k < i; k++){
//-				System.out.print(" ");
//o			}
//-			System.out.println(here);
//-			System.out.println(tree);
			if(here.equals(")")){
				end = true;
				break;
			} else if(here.equals(":")){
//c				// get a distance; give to last node
				ArrayList<String> nodeNameAndNewPos = returnItem(tree, j);
				lastNode.setDistance(Float.parseFloat(nodeNameAndNewPos.get(0)));
				int newPos = Integer.parseInt(nodeNameAndNewPos.get(1));
//-				System.out.println("what is distance up to? i and newpos are: " + i + " " + newPos);
				i = newPos;
//-				System.out.println("gave node " + lastNode.getName() + " distance " + lastNode.getDistance());
			} else if(here.equals("(")){
//c				// call the main internal constructor
//c				// set lastnode to this
//-				System.out.println("topnode constructor calls node constructor:");
//o				for(int k = 0; k < i; k++){
//-					System.out.print(" ");
//o				}
//-				System.out.println("X");
//-				System.out.println(tree + "\n");
				node n = new node(tree, i, taxaNames, taxaStates, availableStates, availableIntStates);
				lastNode = n;
				daughters.add(n);
//-				System.out.println("internal node constructed. node lastpos is " + n.getPosition());
				i = n.getPosition();
//o				for(int k = 0; k < i; k++){
//-					System.out.print(" ");
//o				}
//-				System.out.println("L");
//-				System.out.println(tree + "\n");

			} else if(here.equals(",")){
//c				// do nothing
			} else {
//c				// get a name
//c				// call external constructor on name
//c				// set lastnode to this
				ArrayList<String> nodeNameAndNewPos = returnItem(tree, i);
				String nodeName = nodeNameAndNewPos.get(0);
				int newPos = Integer.parseInt(nodeNameAndNewPos.get(1));
				i = newPos;
				node n = new node(newPos, nodeName, taxaNames, taxaStates, availableStates, availableIntStates);
				lastNode = n;
				daughters.add(n);
			}
			i ++;
		}
//-		System.out.println(daughters.size()+" daughters");

/*
 * 		TODO
 * 
 * 		generalize for polytomies
 * 		22/7/2008
 */		
		if(daughters.size()>2){
			// inserted check for polytomies
			// build first fitch states from 1+2
			this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
			for(int n = 2;n<daughters.size();n++){
				// add subsequent nodes to states
//-				System.out.println("\titerating over daughters to build states: "+n);
				this.taxaGeneralizedFitchStates = this.taxaGeneralizedFitchStates.combine(daughters.get(n).getTaxaGeneralizedFitchStates());
			}
		}else{
			this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
		}
		if(this.taxaGeneralizedFitchStates.isIntersection()){
//o		if(this.taxaGeneralizedFitchStates.isAmbiguous()){
			this.migrations = 1;
		} else {
			this.migrations = 0;
		}
//0		this.finalFitch();
	}
	
	public node(int lastPos, String input, ArrayList<String> taxaNames, ArrayList<String> taxaStates, String[] availableStates, int[] availableIntStates){

//c		// create a new terminal taxon,
//c		// name time
//c		// taxonName whatever

//-		System.out.println("   called external constructor");

		this.taxaNames = taxaNames;
		this.name = input;
		this.isTerminal = true;
		this.position = lastPos;
		this.availableIntStates = availableIntStates;
		this.migrations = 0;
		this.availableStates = availableStates;
//		addition to generalise to n states
//		19/01/2007
		this.stateWeights = new int[availableStates.length];
		for(int i=0; i<availableStates.length; i++){
			stateWeights[i] = 0;
		}
//	 end of addition
		int taxaNamesIndex = 0;
		for(String candidate: taxaNames){
			if(candidate.equals(name) && state.equals("<empty state>")){
				state = taxaStates.get(taxaNamesIndex);
				for(int avail=0;avail<availableStates.length; avail++){
					if(availableStates[avail].equals(state)){
						stateWeights[avail] = 1;
//-						System.out.println("set state "+ availableStates[avail] + " to " + state + " on " + name);
					}
				}
			}	
			taxaNamesIndex ++;
		}
		
/*		for(int i = 0; i< taxaNames.size(); i++){
			if(this.name.equals(taxaNames.get(i))){
				this.state = taxaStates.get(i);
				System.out.print(state);
			}
		}
*/
/*
 * 
 * 		old (pre 19/01/2007) state weight assignment based on fixed size stateWeights array
 * 
 * 
 * 		if(state.equals(availableStates[0])){
//-			System.out.println(availableStates[0]);
//o			stateWeights = new int[2];
			stateWeights[0] = 1;
			stateWeights[1] = 0;

		}
		if(state.equals(availableStates[1])){
//-			System.out.println(availableStates[1]);
//o			stateWeights = new int[2];
			stateWeights[0] = 0;
			stateWeights[1] = 1;
		}
*/
		
/*
 * 		new (19/01/2007) state weight assignment..
 * 
 */ 		
//		o		for(int i=0; i<stateWeights.length; i++){
//		o			if(state.equals(availableStates[i])){
//		o				stateWeights[i] = 1;
//		o
//		o/* 				state assignment reporting
//		o */
//		o//				System.out.println("terminal state assignment " + state + " " + stateWeights[i]);
//		o			}
//		o		}

		//	END new assignment
		
		isContested = false;
		lowerNodesComplete = true;		
		weightsSet = true;
//-		System.out.println();
//-		System.out.println(name + distance + state + stateWeights[0]+ stateWeights[1]);
//-		System.out.println();
		this.taxaGeneralizedFitchStates = new GeneralizedFitchStates(state, false);
//-		printStateWeights();

	}
	
	public node(String tree, int lastPos, ArrayList<String> taxaNames, ArrayList<String> taxaStates, String[] availableStates, int[] availableIntStates){
	
//c		// this should be where the real work would be done
//c		
//c		// would first be called from treeMaker with new node(treefile, treefileLength);
//c		// subsequently would self-call
//c		
//c		// take char as input string
//c		// update lastPos
//c		// if bracket call new node, current pos, treefile; add node to list of daughters
//c		// if closeBracket exit constructor
//c		// if else new string only node, add string opnly node to lost of taxa

		this.taxaNames = taxaNames;
		this.availableIntStates = availableIntStates;
		this.availableStates = availableStates;
		daughters = new ArrayList<node>();
		int treeLength = tree.length();
		boolean closeMe = false;
		boolean lastNodeExists = false;
		boolean doubleStep = false;
		position = lastPos;
		String lastHere;
		lastNode = new node();
		state = "<empty state>";
//		addition to generalise to n states
//		19/01/2007
			this.stateWeights = new int[availableStates.length];
			for(int i=0; i<availableStates.length; i++){
				stateWeights[i] = 0;
			}
//	 end of addition

//o		isContested = true;           //<---- all now initialised in instance variables section (as of v4.1)
//o		stateWeights = new int[2];
//o		stateWeights[0] = 0;
//o		stateWeights[1] = 0;


//-		System.out.println("   called internal constructor");
		
		
		int i = lastPos + 1;
		while(!closeMe){
			if(tree.substring(i-1,i).equals("(")){
				doubleStep = true;
//-				System.out.println("\t\t\t\tDOUBLE STEP BRACKET");
//c				// we have a double step bracket
//c				// make the new node with old pos
//c				// an alternative method commented out in v4.0 made a new node call here
			}

			if(lastNodeExists && (i < lastNode.getPosition())){
				i = lastNode.getPosition() + 1;
//-				System.out.println("a fiddle");
				if(i >= treeLength){
					break;
				}
			}
			int j = i + 1;
			String here = tree.substring(i,j);
//-			System.out.println("here " + here + " i count " + i);
			lastPos = j;			

//-			System.out.println("\tInternal node constructor");
//-			System.out.print("\t");
//o			for(int k = 0; k < i; k++){
//-				System.out.print(" ");
//o			}
//-			System.out.println(here);
//-			System.out.println("\t" + tree + "\n");
			
			if(here.equals(")")){
				int lastNodePos = lastNode.getPosition();
//-				System.out.println("last  node position " + lastNodePos + " current i " + i + " current pos " + position);
				position = i;
				closeMe = true;
//-				System.out.println("closing on:");
//-				System.out.print("\t");
//o				for(int k = 0; k < i; k++){
//-					System.out.print(" ");
//o				}
//-				System.out.println(here);
//-				System.out.println("\t" + tree + "\n");

//-				System.out.println("close ME NOW");
				}
			
			if(here.equals("(")){
//-				System.out.println("\t\t=====INTERNAL CALL=====");
				if(doubleStep){
					node n = new node(tree, lastPos - 1, taxaNames, taxaStates, availableStates, availableIntStates);  // rev 4.0
					lastNode = n;
				} else {
					node n = new node(tree, lastPos, taxaNames, taxaStates, availableStates, availableIntStates);  // rev 3.2 here -> tree..
					lastNode = n;
				}
				daughters.add(lastNode);
				lastNodeExists = true;
				i = lastNode.getPosition();
//-				System.out.println("\t\t=====(end int call)=====");
			} else if(here.equals(",")){
//c				// we know to expect a name next
				int k = j + 1;
				String next = tree.substring(j,k);
				if(next.equals("(")){
//-					System.out.println("the next char is " + next + " so we're ignoring it..");
				} else {
					ArrayList<String> nodeNameAndNewPos = returnItem(tree, j);
					String nodeName = nodeNameAndNewPos.get(0);
					int newPos = Integer.parseInt(nodeNameAndNewPos.get(1));
//o					lastPos = newPos;
					i = newPos;
					node n = new node(i, nodeName, taxaNames, taxaStates, availableStates, availableIntStates);
					lastNode = n;
					daughters.add(n);
					lastNodeExists = true;
				}
			} else if (here.equals(":")){
//c				// we know to expect a distance next
				ArrayList<String> nodeNameAndNewPos = returnItem(tree, j);
				distance = Float.parseFloat(nodeNameAndNewPos.get(0));				
//-				System.out.println("parsed distance == " + nodeNameAndNewPos.get(0) + ", or, " + distance);
				lastNode.setDistance(distance);
//o				lastNode.setHeight(distance);
				int newPos = Integer.parseInt(nodeNameAndNewPos.get(1));
				i = newPos;
			} else {
				if (!here.equals(")")){
					ArrayList<String> nodeNameAndNewPos = returnItem(tree, i);
					String nodeName = nodeNameAndNewPos.get(0);
					int newPos = Integer.parseInt(nodeNameAndNewPos.get(1));
//o					lastPos = newPos;
					i = newPos;
					node n = new node(newPos, nodeName, taxaNames, taxaStates, availableStates, availableIntStates);
					lastNode = n;
					daughters.add(n);
					lastNodeExists = true;

					}
			}
			i ++;
		}

//c		more reporting gumph..

//-		for(int l = 0; l <= i; l++){System.out.print("|");}
//-		System.out.print(tree.substring(i -1, i));
//-		System.out.println();
//-		System.out.println(tree);

//-		System.out.println();
//-		System.out.println(name + distance);
//-		System.out.println();

		for(node kid:daughters){
			kid.setHeight(kid.getDistance());
		}

//-		System.out.println("   closed internal constructor - daughters: " + daughters.size());

/*
 * 		TODO
 * 
 * 		generalize this for polytomies
 * 		22/7/2008
 * 
 */
		if(daughters.size()>2){
			// inserted check for polytomies
			// build first fitch states from 1+2
			int tempMigrations;
			this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
			if(this.taxaGeneralizedFitchStates.isIntersection()){
				//o		if(this.taxaGeneralizedFitchStates.isAmbiguous()){
				tempMigrations = 1;
			} else {
				tempMigrations = 0;
			}
			for(int n = 2;n<daughters.size();n++){
				// add subsequent nodes to states
//-				System.out.println("\titerating over daughters to build states: "+n);
				this.taxaGeneralizedFitchStates = this.taxaGeneralizedFitchStates.combine(daughters.get(n).getTaxaGeneralizedFitchStates());
				if(this.taxaGeneralizedFitchStates.isIntersection()){tempMigrations=1;}
			}
/*
			System.out.print("states for: "+name+" <");
			for(String fitchstate:this.taxaGeneralizedFitchStates.getStates()){
				System.out.print(fitchstate+"\t");
			}
			System.out.println(">");
*/
			this.migrations = tempMigrations;
		}else{
			this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
			if(this.taxaGeneralizedFitchStates.isIntersection()){
				//o		if(this.taxaGeneralizedFitchStates.isAmbiguous()){
				this.migrations = 1;
			} else {
				this.migrations = 0;
			}
		}
//		
//-		this.taxaGeneralizedFitchStates = daughters.get(0).getTaxaGeneralizedFitchStates().combine(daughters.get(1).getTaxaGeneralizedFitchStates());
//-		System.out.println(migrations + name + distance);
		
	}
	
	public void assignWeights(){
		if(isTerminal){
//c			// terminal
			uniFrac = height;
		}else{
			for(node thisnode:daughters){
				thisnode.assignWeights();
				this.uniFrac += thisnode.uniFrac;
				if(!weightsSet){
//-					if(isTop){System.out.println("setting topnode weights");}
					for (int i = 0; i < stateWeights.length; i++) {
						stateWeights[i] = stateWeights[i] + thisnode.getAstateWeight(i);
					}					
				}
			}
			weightsSet = true;
			if(this.isMonophyletic()){
				uniFrac += distance;
			}
/*
			if(height != 0){
				uniFrac = uniFrac/height;
			}
			uniFrac = uniFrac/daughters.size();
*/
		}
	}
	
	public void countMigrations(){
/*
 * 	explicit migration counting method
 *	defines a migration as "any change in state (colouration)"
 *	needs to be run only when reconstruction has happened
 */		
		if(!isTerminal){
		} else {
			//hmm...
		}
	}
	
	private void finalFitch(){
/*		given preliminaryFitch has run in constructor
 * 		prune the preliminary Fitch States
 * 		to give reconstructed states
 */		
		if(!isTerminal){
			for(node child:daughters){
/*
 * 				run the final Fitch phase here.
 * 				will end up with some ambiguous nodes
 * 				(GeneralizedFitchStates.size()>0). These should
 * 				later be counted in the migrations method.
 */
				/*
				 * 		here implemented the second step of the 
				 * 		Fitch (1971) algorithm
				 * 
				 * 		actually, step V refers to parents and children of this node, 
				 * 		so may need to do it in Node.... bummer
				 * 
				 */		
				if(child.getTaxaGeneralizedFitchStates().includes(this.taxaGeneralizedFitchStates)){
//					2			
//					do some eliminating shit; then call supplementStates (4)
					child.getTaxaGeneralizedFitchStates().eliminateStates(this.taxaGeneralizedFitchStates);
					child.supplementStates(this.taxaGeneralizedFitchStates);
				} else {
//					3			
//					if this is a union of children{supplementStates}else{thickenStates}
					if(child.isUnion()){
						child.supplementStates(this.taxaGeneralizedFitchStates);
					} else {
						child.thickenStates(this.taxaGeneralizedFitchStates);
					}
				}

		
				
				child.finalFitch();
			}
		}
	}
	
	public void supplementStates(GeneralizedFitchStates parentStates){
//		4
//		add to states any states in parent not in this one
		this.taxaGeneralizedFitchStates.supplementStates(parentStates);
	}

	public void thickenStates(GeneralizedFitchStates parentStates){
		if (!isTerminal) {
			//		5
			//		add to states any states present in both children AND parent
			ArrayList<GeneralizedFitchStates> childStates = new ArrayList<GeneralizedFitchStates>();
			for (node child : daughters) {
				childStates.add(child.getTaxaGeneralizedFitchStates());
			}
			this.taxaGeneralizedFitchStates.thickenStates(parentStates, childStates);
		}
	}

	public boolean isUnion(){
		if (!isTerminal){
			ArrayList<GeneralizedFitchStates> children = new ArrayList<GeneralizedFitchStates>();
			for (node child : daughters) {
				children.add(child.getTaxaGeneralizedFitchStates());
			}
			return this.getTaxaGeneralizedFitchStates().isUnion(children);
		} else {
			return false;
		}
	}
	
	public boolean isMonophyletic(){
		int representedStates = 0;
		if(stateWeights == null){
			this.assignWeights();
		}
		for(int state:stateWeights){
			if(state>0){
				representedStates ++;
			}
		}
		if(representedStates == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public double[] getAI() {
		if (!aiCalculated) {
//			if (!isTop) {
			if (1==1) {
				if (isTerminal) {
					AI = 0;
				} else {
					// TODO: get AI for this node by:
					/*			calling getAStateWeight(both states)
					 deciding which is bigger
					 evaluate this node AI ai as (1-max n)/(2pow(n-1))
					 pass up cumulative AI to higher node
					 */
					int MaxN = 0;
					double numDaughters = 0;
					for (int state : availableIntStates) {
						numDaughters += getAstateWeight(state);
						if (getAstateWeight(state) > MaxN) {
							MaxN = getAstateWeight(state);
						}
//-					System.out.println(name + " "+ MaxN);
					}
					double ai = (1 - (MaxN / numDaughters))
							/ (Math.pow(2, (numDaughters - 1)));
//-					System.out.println(ai);
					for (node daughter : daughters) {
						sumAI += daughter.getAI()[0];
					}
//-					System.out.println("\t"+name+" ai: ["+ai+"] + sumAI ["+sumAI+"] = "+(ai+sumAI));
					AI = ai + sumAI;
				}
			} else {
				//			the top node ... treat differently (don't need AI for redundant top node)		
				AI = 0;
//-				System.out.println("topnode");
				for (node daughter : daughters) {
					sumAI += daughter.getAI()[0];
				}
			}
			aiCalculated = true;
		}		
		double [] AIarray = new double[]{AI,sumAI};
		return AIarray;
	}
	
	public ArrayList<node> getImmediateDaughters(){
		return daughters;
	}
	
	public int getAllDaughters(){
		if(isTerminal){
			return 0;
		} else {
			int granDaughters = 0;
			for(node daughter:daughters){
				granDaughters = granDaughters + daughter.getAllDaughters();
			}
			return granDaughters + daughters.size();
		}
	}
	
	public int getAstateWeight(int index){
		return stateWeights[index];
	}
	
	public float getBestHeight(){
		ArrayList<Float> possibleHeights = new ArrayList<Float>();
		float bestHeight = 0;
		if(isTerminal){
			bestHeight = height;
		} else {
			for(node contender:daughters){
				possibleHeights.add(contender.getBestHeight());
			}
			for(float contenderHeight:possibleHeights){
				if(contenderHeight >= bestHeight){
					bestHeight = contenderHeight;
				}
			}
		}
		return bestHeight;
	}
	
	public float getDistance(){
		return distance;
	}

	public float getHeight(){
		if(!isTop){
		} else {
			for(node daughter:daughters){
				if(daughter.getHeight() >= height){
					height = daughter.getHeight();
				}
			}
		}
		return height;
	}

	public float getHeightOfLargestNodeOfState(int stateToBeExclusive){
/*
 * 		NOTE - 8/july/2008
 * 
 * 		From looking at this it seems quite possible that there
 * 		may be a problem with the MC statistic as implemented using this 
 * 		method to find the highest node including only a certain state;
 * 		because it was written with only binary (2) state sytems in mind, 
 * 		the method initialises a 'unwanted' state and then tests to see
 * 		if lower nodes _don't_ have that state represented.
 * 
 * 		Problem is, if a node has none of state 0 or 1, it might still 
 * 		have other states, i.e., not be an MC node. Leads to over-estimations of MC sizes.
 * 		
 * 		The solution is to iterate through all possible states.
 * 
 * 		IMPLEMENTED: 8/7/2008.
 * 		Tested: 8/7/2008 on Salemi data set - gives same results.
 */
		ArrayList<Float> possibleHeights = new ArrayList<Float>();
		float highestExclusiveNodeHeight = 0;
		int [] otherStates = new int[availableIntStates.length-2];
		int osi = 0;
		for(int av_i_s :availableIntStates){
			if(av_i_s != stateToBeExclusive){
				otherStates[osi] = av_i_s;
				osi ++;
			}
		}
		
		if(isTerminal){
			highestExclusiveNodeHeight = height;
		} else {
			for(node contender:daughters){
				boolean thisIsMC = true;
				for(int dontWant:otherStates){
					if(contender.getAstateWeight(dontWant) != 0){
						thisIsMC = false;
					}
				}
				if(thisIsMC){
//c					// use its height directly
					possibleHeights.add(contender.getHeight());
				} else {
//c					// find the height of some exclusive lower node
					possibleHeights.add(contender.getHeightOfLargestNodeOfState(stateToBeExclusive));
				}	
			}
			float previousHeight = possibleHeights.get(0);
			for(float contenderHeight:possibleHeights){
				if(contenderHeight <= previousHeight){
					highestExclusiveNodeHeight = contenderHeight;
				}
			}

		}
		return highestExclusiveNodeHeight;
	}

	public float getHighestNodeExcludingState(int stateToIgnore){
		ArrayList<Float> exclusiveNodeDistances = new ArrayList<Float>();
		if(isTerminal){
			exclusiveNodeDistances.add(distance);
		} else {
			int numberOfDaughters = daughters.size();
			float cumulativeDistances = 0;
			for(node contender:daughters){
				if(contender.getAstateWeight(stateToIgnore) ==0){
					cumulativeDistances = cumulativeDistances + contender.getDistance();
				} else {
					cumulativeDistances = cumulativeDistances + contender.getHighestNodeExcludingState(stateToIgnore);
				}
			}
			float meanHighestExclusiveDistanceOfAllNodes = cumulativeDistances / numberOfDaughters;
			exclusiveNodeDistances.add(meanHighestExclusiveDistanceOfAllNodes);
		}
		float highestExclusiveNodeDistance = 0;
		for(float contender:exclusiveNodeDistances){
			if(contender>highestExclusiveNodeDistance){
				highestExclusiveNodeDistance = contender;
			}
		}
		return highestExclusiveNodeDistance;
	}

	public boolean getIsContested(){
		return isContested;
	}

	public float getLongestBranch(){
		ArrayList<Float> possibleHeights = new ArrayList<Float>();
		float bestHeight = 0;
		if(isTerminal){
			bestHeight = height;
		} else {
			for(node contender:daughters){
				possibleHeights.add(contender.getLongestBranch());
			}
			for(float contenderHeight:possibleHeights){
				if(contenderHeight >= bestHeight){
					bestHeight = contenderHeight;
				}
			}
		}
		return bestHeight;
	}

	public int getLowerStateChanges() {
		return lowerStateChanges;
	}

	/*	public int getMigrationEvents(){
		if(isTerminal){
//			// do nothing; no migrations
//		} else if(isTop){
//			for(node thisNode:daughters){
//				migrations += thisNode.getMigrationEvents();
//			}
		} else {
//			// collect migrations of lower  nodes and also this node
			for(node thisNode:daughters){
				if(!thisNode.getParentState().equals(thisNode.getState())){
					thisNode.incrementMigrationEvents();
				}
				migrations += thisNode.getMigrationEvents();
			}
		}
		return migrations;
	}
*/
	
	public int getMigrationEvents(){
/*
 * 	MAJOR NOTE
 * 	22/JULY/2008
 * 
 * 
 * 	ERRORS in PARSIMONY MODE
 * 
 * 	When presented with a hard polytomy (e.g. ((a,b,c),d);)
 *  BaTS reconstructs the polytomy with too many migrations.
 *  Ideally need a complexCombine() method for FitchStates 
 *  that would combine properly and calculate migrations
 *  accordingly.
 *  
 *  However, no time to do this now, so for now will only
 *  implement as current and give users limitation warning.
 *  
 *  Found out that most or all common software outputs polytomies
 *  as 'soft', e.g. no branchlengths but biurificating, 
 *  so users are not likely to meet this limitation practically
 *  nonetheless, will need to check the behaviour separately.
 *  
 *  Note also that such 'soft' polytomies may cause problems when building
 *  the nodal distance matrices for NT/NR
 * 
 */
		
/*		
 * 		gets the number of migrations below node for each
 * 		daughter node.
 * 
 * 		have to ignore teminal nodes
 */
		int lowerMigrations = 0;
		if(isTerminal){
/*		} else if(isTop){
			for(node thisNode: daughters){
				if(thisNode.getTaxaGeneralizedFitchStates().isIntersection()){
					migrations ++;
				}
			}
*/		} else {
			for(node thisNode: daughters){
/*
 * 				if(!state.equals(thisNode.getState())){
 *					migrations ++;
 *				}
 */
/*				boolean stateMismatch  = false;
				if (!thisNode.isTerminal) {
					for (node itsNode : thisNode.getImmediateDaughters()) {
						if (!thisNode.getTaxaGeneralizedFitchStates().includes(itsNode.getTaxaGeneralizedFitchStates())) {
							stateMismatch = true;
						} else {
//o							stateMismatch = false;
						}
					}
				}
*/				
//o				if((thisNode.getTaxaGeneralizedFitchStates().isAmbiguous()) || stateMismatch){
//o				if(thisNode.getTaxaGeneralizedFitchStates().isIntersection()){
//o					thisNode.incrementMigrationEvents();
//o					this.migrations ++;
//o				}
				
				lowerMigrations += thisNode.getMigrationEvents();
//				if(!this.state.equals(thisNode.getState())){
//					lowerMigrations ++;
//					System.out.println(name+" state "+state+" daughter "+thisNode.name+" state: "+thisNode.state);
//				}
			}
			lowerMigrations += migrations;
		}

		return lowerMigrations;
	}
	
	
	public int getMyDaughters(){
		return daughters.size();
	}

	public String getName(){
		return name;
	}

	public String getParentState() {
		return parentState;
	}
	
	public int getPosition(){
		return position;
	}
	
	public int getSizeOfLargestNodeOfState(int stateToBeExclusive){
/*
 * 		old method
 * 		ArrayList<Integer> unwantedStates = new ArrayList<Integer>();
		for(int i = 0; i<availableStates.length; i ++){
			if(!(i == stateToBeExclusive)){
				unwantedStates.add(i);
			}
		}
		ArrayList<Integer> exclusiveNodeSizes = new ArrayList<Integer>();
		if(isTerminal){
			exclusiveNodeSizes.add(getAstateWeight(stateToBeExclusive));
		} else {
			for(node contender:daughters){
				boolean allExclusive = true;
				for (int otherState:unwantedStates) {
					if (contender.getAstateWeight(otherState) == 0) {} else {
						allExclusive = false;
					}
				}
				if(allExclusive){
					exclusiveNodeSizes.add(contender.getSizeOfLargestNodeOfState(stateToBeExclusive));
				}
			}
		}
//		exclusiveNodeSizes.add(contender.getSizeOfLargestNodeOfState(stateToBeExclusive));

		int highestExclusiveNodeSize = 0;
		for(int contender:exclusiveNodeSizes){
			if(contender>highestExclusiveNodeSize){
				highestExclusiveNodeSize = contender;
			}
		}

 */
		int highestExclusiveNodeSize = 0;

		if(isTerminal){
			highestExclusiveNodeSize = 1;
		} else {
			// not a terminal node - need to work out if this is exclusive and call this method on daughters if not
			boolean allExclusive = true;
 			ArrayList<Integer> unwantedStates = new ArrayList<Integer>();
 			for(int cand:availableIntStates){
 				if(!(cand==stateToBeExclusive)){
 					unwantedStates.add(cand);	
 				}			
 			}
			for(int unwanted:unwantedStates){
				if(this.getAstateWeight(unwanted) == 0){
					// that's cool
				}else{
					allExclusive = false;
				}
			}
			if(allExclusive){
				// this node is exclusive so it's size is the n-c size
				highestExclusiveNodeSize = this.getAstateWeight(stateToBeExclusive);
			}else{
				// this node isn't exclusive, need to call this method on daughters
				int[] daughterNCsizes = new int[daughters.size()];
				int daughterIndex = 0;
				for(node contender:daughters){
					daughterNCsizes[daughterIndex] = contender.getSizeOfLargestNodeOfState(stateToBeExclusive);
					daughterIndex ++;
				}
				for(int poss:daughterNCsizes){
					if(poss > highestExclusiveNodeSize){
						highestExclusiveNodeSize = poss;
					}
				}
			}
/*			try{
				int veryinterested = 1;
				if(stateToBeExclusive==veryinterested && allExclusive){
					System.out.println("\t"+name);
					System.out.println("\tHnc"+highestExclusiveNodeSize);
					System.out.println("\tSWc"+this.getAstateWeight(veryinterested));
					System.out.println("\tD1c"+daughters.get(0).getAstateWeight(veryinterested));
					System.out.println("\tD2c"+daughters.get(1).getAstateWeight(veryinterested));
				}
			}catch(Exception ex){}
*/		}
		return highestExclusiveNodeSize;
	}
	
	public String getState(){
		return state;
	}
	
	public GeneralizedFitchStates getTaxaGeneralizedFitchStates() {
		return taxaGeneralizedFitchStates;
	}
	
	public float getTotalHeight(){
		float totalDistance = 0;
		if(isTerminal){
			totalDistance = distance;
		} else {
			totalDistance += daughters.get(0).getTotalHeight();
		}
		return totalDistance;
	}
	
	public float getTotalSize(){
		float totalSize = 0.0f;
		if(isTerminal){
			totalSize = distance;
		}else{
			totalSize = distance;
			for(node contender:daughters){
				totalSize+=contender.getTotalSize();
			}
		}
		return totalSize;
	}
	public float getTotalSizeFeaturingNode(int desiredState){
		float retTotalSize = 0.0f;
		if(isTerminal){
			if(stateWeights[desiredState]>1){
				retTotalSize = distance;
			}
		}else{
			for(node contender:daughters){
				if(contender.isTerminal&&contender.getAstateWeight(desiredState)>0){
					retTotalSize += contender.distance;
				}else if(contender.getAstateWeight(desiredState)>0){
					retTotalSize += contender.getTotalSizeFeaturingNode(desiredState)+contender.distance;
				}else{}
			}
		}
		return retTotalSize;
	}
	
	public void getDirectPD(){
		for(int i=0; i<PD.length;i++){
			System.out.println("\tPD ("+i+"): "+PD[i]);
		}
	}
	
	public float getPD(int aStateWeight){
		float PD = 0f;
		if (!isTop) {
			if (this.getAstateWeight(aStateWeight) > 0) {
				if (this.isMonophyletic()) {
					//				PD += this.getTotalSize();
				} else {
					//				PD += distance;
					for (node daughter : daughters) {
						PD += daughter.getPD(aStateWeight);
					}
				}
			} else {
				PD += (-1 * this.getTotalSize());
			}
		}else{
			for(int i=0;i<stateWeights.length;i++){
//**			PD (state) = total tree size - subtree that isn't right state
				PD += this.getTotalSize();
				for(node daughter:daughters){
					PD += daughter.getPD(i);
				}
			}
//			System.out.println("topnode PD:"+PD);
		}
		//		System.out.println("node:"+name+" PDi: "+PD+" size "+this.getTotalSize());
		return PD;
	}
	
	public float[] getPDoldMethod(int[] stateTallies) {
		PD = new float[stateWeights.length];
		for(int i=0; i<PD.length; i++){
			if(stateWeights[i]==stateTallies[i]){
				// pass on PD for this state for any node that has it
				for(node contender:daughters){
					PD[i]+=contender.getTotalSizeFeaturingNode(i);
				}
			}else{
				// pass on PD for any node that has it, plus add my distance
				for(node contender:daughters){
					PD[i]+=contender.getTotalSizeFeaturingNode(i);
				}
				PD[i]+=distance;
			}
		}
/*
		if(isTerminal){
			//nothing
		}else{
			for(node contender:daughters){
				float[] contenderPD = contender.getPD(stateTallies);
				for(int i=0; i<PD.length; i++){
					if(stateWeights[i]==stateTallies[i]){
						PD[i] = contenderPD[i];
					}else{
						// add PD for unequivocal nodes as node.height
						// can i use add/remove offsets?
						int contenderState = contender.getAstateWeight(i);
						float contenderHeight = contender.getHeight();
						if(contender.isMonophyletic()&&(contenderState>0)&&contenderHeight>PD[i]){
							PD[i] = contenderHeight;
						}else if(contenderState>0){
							// this node is represented both ways 
							PD[i] += contenderHeight;
						}else{
							// this node doesn't have desired state, deduct height
							PD[i] -= contenderHeight;
						}
						// but how to remove for lower nodes?!?!?
						// consider multiple conditionals
					}
				}
			}
		}
*/
		return PD;
	}

	public float getNTI() {
		return NTI;
	}

	public float getNRI() {
		return NRI;
	}

	public float[] getUniFrac(){
		float[] retArr = new float[2];
		if(isTerminal){
			uniFrac = distance;
			retArr[0] = 0;
			retArr[1] = 1;
		}else if(this.isMonophyletic()){
			uniFrac = distance;
			retArr[0] = distance;
			for(node daughter:daughters){
				float[] daughterArr = daughter.getUniFrac();
				retArr[0] += daughterArr[0];
				retArr[1] += daughterArr[1];
			}
		}else{
			uniFrac = 0f;
			for(node aNode:daughters){
				float[] daughterArr = aNode.getUniFrac();
				retArr[0] += daughterArr[0];
				retArr[1] += daughterArr[1];
			}
		}
		return retArr;
	}
/*	public void incrementMigrationEvents(){
 *		migrations ++;
 *	}
 */
	public int[] getStateWeights(){
		return stateWeights;
	}
	
	public float getSize(){
		if(isTerminal){
			return distance;
		}else{
			float size = distance;
			for(node daughter:daughters){
				size += daughter.getSize();
			}
			return size;
		}
	}
	
	public float getInternalBranchSize(){
		if(isTerminal){
			return 0f;
		}else{
			float size = distance;
			for(node daughter:daughters){
				size += daughter.getInternalBranchSize();
			}
			return size;
		}
	}
	
	public boolean isDone(){
		if(isTerminal){
		} else {
			ArrayList<Boolean> daughterCompletion = new ArrayList<Boolean>();
			for(node thisnode:daughters){
//-				System.out.println(thisnode.getState());
				if(!thisnode.getState().equals("<empty state>")){
					daughterCompletion.add(thisnode.isDone());
				} else {
					daughterCompletion.add(false);
				}
			}
//-			System.out.println("node " + name + " state " + state + ":");
			for(boolean wellThenHmm:daughterCompletion){
//-				System.out.println(wellThenHmm);
			}
			if(!daughterCompletion.contains(false)){
//-				System.out.println("no incompletely reconstructed nodes");
				lowerNodesComplete = true;
			} else {
//-				System.out.println("some unreconstructed nodes");
			}
		}
		return lowerNodesComplete;
	}
	
	public String nameInternalNodes(){
		if(isTerminal){
		} else {
			if(isTop){
				for(node thisnode:daughters){
					String daughterNodeName = thisnode.nameInternalNodes();
				}
			} else {
				String newName = "internal[";
				for(node thisnode:daughters){
					String daughterNodeName = thisnode.nameInternalNodes();
					newName = newName + daughterNodeName;
				}
				newName = newName + "]";
				name = newName;
			}
		}
		return name;	
	}

	public void parsimonyReconstructDown(){
		if(isTerminal){
//c			// terminal
		}else{
//c			// having got the weightings for nodes below me, am i contested still?
			if(stateWeights[0] > stateWeights[1]){
//c				// set state
//c				// node not contested
//-				System.out.println(name);
//-				System.out.println(availableStates.length);
				state = availableStates[0];
//o				System.out.println(state);
				isContested = false;
			} else if(stateWeights[1] > stateWeights[0]){
//c				// set state
//c				// node not contested
//-				System.out.println(name);
//-				System.out.println(availableStates.length);
				state = availableStates[1];
//o				System.out.println(state);
				isContested = false;
			} else {
//c			// the node is still contested
			}
			for(node thisNode:daughters){
				thisNode.parsimonyReconstructDown();
			}
		}
	}
	
	public void parsimonyReconstructUp(){
		if(isTerminal){
		} else {
			for(node thisnode:daughters){
				if(thisnode.isContested){
//-					System.out.println("}-me- " + stateWeights[0] + " " + stateWeights[1] + " " + state);
//-					System.out.println("}-dA- " + thisnode.getAstateWeight(0) + " " + thisnode.getAstateWeight(1) + " " + thisnode.getState());
//-					System.out.println(thisnode.getState() + " before");
					thisnode.setState(state);
					thisnode.setContested(false);
//-					System.out.println(state + " after");
				}
				thisnode.parsimonyReconstructUp();
			}
		}
	}
	
	public float reSetHeight(){

//-		System.out.println("current node " + name + " d: " + distance + " h: " + height);
		height = 0;
		if(isTerminal){
			height = distance;
		} else {
			float possibleBase = 0;
			for(node contender:daughters){
				float contenderHeight = contender.reSetHeight();
				if(contenderHeight >= possibleBase){
					possibleBase = contenderHeight;
				}
			}
			height = possibleBase + distance;
		}
//-		System.out.println("current node now " + name + " d: " + distance + " h: " + height);

		return height;
	}
	
	public ArrayList<String> returnItem(String input, int pos){
//-		System.out.println(">------called returnItem");
		ArrayList<String> details = new ArrayList<String>();
		StringBuilder item = new StringBuilder();
		int inputLength = input.length();
		for(int i=pos; i<inputLength; i++){
			int j = i + 1;
			pos = j;
			String candidate = input.substring(i,j);
			if((!candidate.equals("(") && !candidate.equals(")") && !candidate.equals(":") && !candidate.equals(","))){
				item.append(candidate);
//-				System.out.print("item [" + item + "] candidate " + candidate);
			} else {
				break;
			}
		}
		pos = pos -2; //random fudge to make sure we don't miss the next character in treefile on return to constructor
		String Position = Integer.toString(pos, 10);
		String itemString = item.toString();
		details.add(itemString);
		details.add(Position);
//-		System.out.println(">------" + item + Position);
		return details;
	}
	
	public void setAI(float ai) {
		AI = ai;
	}
	
	public void setContested(boolean newContestedStatus){
		isContested = newContestedStatus;
	}
	
	public void setDistance(float newDistance){
		distance  = newDistance;
	}
	
	public void setHeight(float newHeight){
		if(isTerminal){
			height = newHeight;
		} else {
			ArrayList<Float> lowerHeights = new ArrayList<Float>();
			for(node lowerNode:daughters){
				lowerHeights.add(lowerNode.getHeight());
			}
			float baseHeight = 0;
			for(float lowerNodeHeight:lowerHeights){
				if(lowerNodeHeight >= baseHeight){
					baseHeight  = lowerNodeHeight;
				}
			}
			height = baseHeight + newHeight;
		}
	}

	public void setLowerStateChanges(int lowerStateChanges) {
		this.lowerStateChanges = lowerStateChanges;
	}
	
//c	// typical method to step through nodes in a tree

	// public void typicalStepThroughNodes{
	//	
	//	private node thisnode;
	//
	//	if(){
	//		do terminal node stuff;
	//	} else {
	//		do internal node things;
	//		for(node thisnode:daughters){thisnode.typicalStepThroughNodes();}
	//	}
	// }
//c

	public void setParentState(String newParentState) {
		if(isTop){
			this.parentState = state;
			for(node thisNode:daughters){
				thisNode.setParentState(state);
			}
		} else if(isTerminal){
			this.parentState = newParentState;
		} else {
			this.parentState = newParentState;
			for(node thisNode:daughters){
				thisNode.setParentState(state);
			}
		}
	}
	

//c	(tree points method)
//c	(relevant to tree drawing; not developed much since v3.0)
//
//	public ArrayList<Integer> getTreePoints(){
//		
//		
//		ArrayList<Integer> points = new ArrayList<Integer>();
//		int lastNodeX = 0;
//		int lastNodeY = 0;
//		points.add(lastNodeX);
//		points.add(lastNodeY);
//		// to start with
//		// need to step through and add a point for each node going
//		
//		boolean getDistancesDone = false;
//		
//		while(!getDistancesDone){
//			if(isTerminal){
//				// things terminal
//			} else {
//				// thins internal
//				for(node thisnode:daughters){}
//			}
//			getDistancesDone = true;
//		}
//		
//		// later
//		// think about spatial arrangement
//
//		return points;
//	}
//c

////////////////////////////////////////////////
//	now some parsimony state reconstructions  //
////////////////////////////////////////////////

	public void setPosition(int newPos){
		position  = newPos;
	}
	
	public void setState(String newState){
		state = newState;
	}
	
//	public ArrayList<String> preliminaryFitch(){
///*		obtain the initial fitch sets
// * 		for nodes
// */
//		if(isTerminal){
//			this.taxaGeneralizedFitchStates.add(this.state);
//		} else {
///*			some more complicated fiddling to follow the 
// * 			Fitch (1971) 'preliminary' algorithm, 
// * 			viz. if both different	:union
// * 					lopsided		:majority
// * 					both doubles	:doubled
// */			
//
//			for(node thisNode:daughters){
//				ArrayList<String> tmplist = thisNode.preliminaryFitch();
//				for(String tempGeneralizedFitchStates : tmplist){
//					if(!taxaGeneralizedFitchStates.contains(tempGeneralizedFitchStates)){
////c						(there was nothing there or a singleton)
//						taxaGeneralizedFitchStates.add(tempGeneralizedFitchStates);
//					} else if(taxaGeneralizedFitchStates.size() == tmplist.size()){
////c						(taxalists are the same size (prob. 2) and so a union is desired
//						taxaGeneralizedFitchStates = tmplist;
//					} else {
////c						(tricky - implies one bigger than the other, so need to work
////c						out how to split off redundant set
//						if(taxaGeneralizedFitchStates.size() < tmplist.size()){
////c							thisnode is bigger; do nothing as the unified state must be already here
//						}else{
////c							thisnode is smaller; search the taxafitchState list and throw out redundant one
//							int index = 0;
//							for(int i=0; i<taxaGeneralizedFitchStates.size(); i++){
//								if(tmplist.contains(taxaGeneralizedFitchStates.get(i))){
//									index = i;
//								}
//							}
//							taxaGeneralizedFitchStates.remove(index);
//						}
//					}
//				}
//			}
//		}
//		return taxaGeneralizedFitchStates;
//	}
	
	public void setTaxaGeneralizedFitchStates(GeneralizedFitchStates taxaGeneralizedFitchStates) {
		this.taxaGeneralizedFitchStates = taxaGeneralizedFitchStates;
	}
	
	public void setTop(){
		isTop = true;
	}

	public void showNodes(){
		if(isTerminal){
//-			System.out.println("a terminal node");
			String thisNodeName = name;
//-			System.out.println("   node " + name + " distance: " + distance);

		} else {
//-			System.out.println("an internal node");
//-			System.out.println("   node " + name + " distance: " + distance + " with " + daughters.size() + " daughter nodes");

			for(node thisNode:daughters){
//o				String thisNodeName = thisNode.getName();
//o				System.out.println("   node " + thisNodeName);
				thisNode.showNodes();
			}

		}
	}

	public void simpleParsimonyReconstruct(){
		if(isTerminal){
//c			// things terminal
		} else {			
//c			// things internal
			for(node thisnode:daughters){
				if(thisnode.getState().equals("<empty state>")){
//-					System.out.println("a null");
				} else {
					possibleStates.add(thisnode.getState());
					state = state + thisnode.getState();
				}
				thisnode.simpleParsimonyReconstruct();
			}	
		}	
	}
	
//	public boolean areLowerNodesContested(){
//		boolean lowerNodesAREcontested = daughterHappiness.contains(false);
//		System.out.println(name + " " + state + " lowers contested... " + lowerNodesAREcontested);
//		return lowerNodesAREcontested;
//	}
//	
//	public void checkLowerNodeHappinesses(){
//		if(isTerminal){
//		} else {
//			for(node thisnode:daughters){
//				String thisnodeState = thisnode.getState();
//				if(thisnodeState.equals("<empty node>")){
//					daughterHappiness.add(false);
//				} else {
//					daughterHappiness.add(true);
//				}
//				thisnode.checkLowerNodeHappinesses();
//
//			}
//		}
//	}
//	
//	public void showLowerNodeHappinesses(){
//		System.out.println("for node " + null + " " + state);
//		if(isTerminal){
//		} else {
//			for(node thisnode:daughters){
//				System.out.println(thisnode.getState() + " " + thisnode.areLowerNodesContested());
//				thisnode.showLowerNodeHappinesses();
//			}
//		}
//	}

	public void simpleShowNodes(){
		if(isTerminal){
			System.out.println(stateWeights[0] + " " + stateWeights[1] + " " + state + " " + name + " d:" + distance + " h:" + height);
		}else{
			System.out.println(" - ");
			System.out.println(stateWeights[0] + " " + stateWeights[1] + " " + state + " " + name + " d:" + distance + " h:" + height + taxaGeneralizedFitchStates.isIntersection() + " states: " + taxaGeneralizedFitchStates.getStates());
			for(node thisnode:daughters){thisnode.simpleShowNodes();}
		}
	}
	
	public String[] getAvailableStates(){
		return availableStates;
	}
	
	public String getAvailableStatesAsString(){
		String retVal = "";
		for(String aState:availableStates){
			retVal += aState + ", ";
		}
		return retVal;
	}
	
	public void printStateWeights(){
		System.out.println("state weights for " + name + " (possible: " + getAvailableStatesAsString() + ")");
		for(int stateKey = 0; stateKey<availableStates.length; stateKey ++){
			System.out.println("state " + stateKey + " state: " + this.getAstateWeight(stateKey));
		}
	}
	
	public boolean bothDaughtersIntersections(){
		if(daughters.get(0).getTaxaGeneralizedFitchStates().isIntersection() && daughters.get(1).getTaxaGeneralizedFitchStates().isIntersection()){
			return true;
		} else {
			return false;
		}
	}

	public boolean someDaughtersIntersections(){
		if(daughters.get(0).getTaxaGeneralizedFitchStates().isIntersection() || daughters.get(1).getTaxaGeneralizedFitchStates().isIntersection()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTipCountTop(){
		int totalWeights = 0;
		for(int weight:stateWeights){
			totalWeights += weight;
		}
		if(totalWeights == taxaNames.size()){
			return true;
		}else{
			return false;
		}

	}
	
	public PairwiseMatrix buildMatrix(){
		PairwiseMatrix retMatrix;
		if(isTerminal){
			// new terminal taxon matrix
			String[] term_label = {name};
			float[] term_distance = {0f};
			float[] term_d1={0f};
			float[][] term_distances = {term_d1};
			retMatrix = new PairwiseMatrix(term_label,term_distance,term_distances);
		}else{
			// get matrices for all daughters, combine
			PairwiseMatrix[] daughtersMatrices = new PairwiseMatrix[daughters.size()];
			float[] daughtersDistances = new float[daughters.size()];
			int i=0;
			for(node contender:daughters){
				daughtersMatrices[i] = contender.buildMatrix();
//-				daughtersMatrices[i].showMatrix();
				daughtersDistances[i] = contender.getDistance();
				i++;
			}
			PairwiseMatrix[] otherDaughterMatrices = new PairwiseMatrix[daughters.size()-1];
			for(i=0;i<daughtersMatrices.length-1;i++){
				otherDaughterMatrices[i] = daughtersMatrices[i+1];
			}
			retMatrix = daughtersMatrices[0].complexCombine(otherDaughterMatrices, daughtersDistances);
		}
		return retMatrix;
	}

	public PairwiseMatrix buildNodalMatrix(){
		PairwiseMatrix retMatrix;
		if(isTerminal){
			// new terminal taxon matrix
			String[] term_label = {name};
			float[] term_distance = {0f};
			float[] term_d1={0f};
			float[][] term_distances = {term_d1};
			retMatrix = new PairwiseMatrix(term_label,term_distance,term_distances);
		}else{
			// get matrices for all daughters, combine
			PairwiseMatrix[] daughtersMatrices = new PairwiseMatrix[daughters.size()];
			float[] daughtersDistances = new float[daughters.size()];
			int i=0;
			for(node contender:daughters){
				daughtersMatrices[i] = contender.buildNodalMatrix();
//-				daughtersMatrices[i].showMatrix();
//				if(this.isTop){
//					daughtersDistances[i] = 0f;
//				}else{
					daughtersDistances[i] = 1f;
//				}
				i++;
			}
			PairwiseMatrix[] otherDaughterMatrices = new PairwiseMatrix[daughters.size()-1];
			for(i=0;i<daughtersMatrices.length-1;i++){
				otherDaughterMatrices[i] = daughtersMatrices[i+1];
			}
			retMatrix = daughtersMatrices[0].complexNodeCombine(otherDaughterMatrices, daughtersDistances);
		}
		return retMatrix;
	}
	
	public void nodeDetails(){
		if(!isTerminal){
			for(node daughter:daughters){
				daughter.nodeDetails();
			}
			System.out.println("+++++++\nnode: "+name);
			System.out.println("daughters "+daughters.size()+"\nstate weights");
			for(int aState:stateWeights){
				System.out.print("["+aState+"]");
			}
			System.out.println("\nfitch States:");
			for(String aState:this.taxaGeneralizedFitchStates.getStates()){
				System.out.print("<"+aState+">");
			}
			System.out.println("\nfinal state:"+state+"\nmigrations:"+migrations);
		}
	}
}