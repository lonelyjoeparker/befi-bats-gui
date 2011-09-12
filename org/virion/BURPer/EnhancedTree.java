package org.virion.BURPer;

// ver 4.1 //

import java.util.ArrayList;
import java.util.Random;
import org.virion.PairwiseMatrix;

public class EnhancedTree{

	public EnhancedNode topnode;
	private float treeHeight;
	private float NT;
	private float NR;
	private double AI;
	private String treefile;
	private String topnodeState;
	private String [] availableStates;
	private ArrayList<String> taxaNames = new ArrayList<String>();
	private ArrayList<String> taxaStates = new ArrayList<String>();
	private PairwiseMatrix topnode_distances;
	private PairwiseMatrix topnode_nodal_distances;

	public EnhancedTree(){
	}

	public EnhancedTree(String treefile, ArrayList<String> taxaNames, ArrayList<String> taxaStates, String [] availableStates){
//-		System.out.println("tips: " + taxaNames.size());
		this.availableStates = availableStates;
		try{
			topnode = new EnhancedNode(taxaNames, taxaStates, treefile, 1, availableStates);
			//-		topnode.printStateWeights();
			topnode.setTop();
			topnode.nameInternalNodes();
	/*
	 * 		NOTE - 04/JULY/2008
	 * 
	 * 		it is obviously desirable to perform as few node iterations, 
	 * 		or 'tree traversals' as possible.
	 * 
	 * 		however, because i don't have the time to unpick the code now, and potentially 
	 * 		revert all the fuckups i'm gonna make (no CVS), instead i'm gonna leave things as 
	 * 		they are, and deal with the speed consequences later.
	 * 
	 * 		HAVING SAID THAT: the new metrics rely on:
	 * 			PD - state weights & differences
	 * 			NTI/NRI - nodal distance matrix
	 * 			UniFrac - cumulative distances unique/total
	 * 
	 * 		so these can all be done in assignWeights()
	 * 		
	 * 		which is what i'll do.
	 * 
	 * 
	 * 		Additional: 07/JULY/2008
	 * 
	 * 		Order of stats in retVals:
	 * 
	 * 			PD (combined)
	 * 			NTI (combined)
	 * 			NRI (combined)
	 * 			UniFrac
	 */

			topnode.assignWeights();
	//-		topnode.printStateWeights();
	//-		topnode.simpleShowNodes();
	//-		System.out.println("topnode directly has " + topnode.getMyDaughters() + " nodes and " + topnode.getAllDaughters() + " total nodes. height " + topnode.getHeight());
			treeHeight = topnode.reSetHeight();
			
			boolean parsimonyReconstructedDown = false;
			int pRpasses = 1;

	/*		
	 * 		NOTE - IMPORTANT
	 * 
	 * 		Because of the reconstruction method, the parsimony down process hangs infinitely on
	 * 		totally balanced trees of equal numbers of 2 taxa (e.g., balanced trees of say 
	 * 		16 black & 16 white.)
	 * 
	 * 		Therefore the old parsimony down method, which allowed for more than one down pass, 
	 * 		has been killed for a simpler single call to parsimonyReconstructDown() and instead, if the 
	 * 		top node state remains empty it is randomly assigned (since a reconstruction failure 
	 * 		suggests it is equivocal.)
	 * 
	 * 		18/10/2006
	 */		
			
	/*
	 * 		The old loop:
	 * 
	 */
//			24/01/2007		while(!parsimonyReconstructedDown){
	//-			System.out.println();
//			24/01/2007			System.out.println("DOWN PASS "+pRpasses);
//			24/01/2007			pRpasses ++;
//			24/01/2007			topnode.parsimonyReconstructDown();
	//-			topnode.simpleShowNodes();
	//o			String topState = topnode.getState();
//			24/01/2007			if(!topnode.getState().equals("<empty state>")){parsimonyReconstructedDown=true;}
//			24/01/2007		}
			


	//-		System.out.println();		

	/*
	 * 		The new down method: single pass with top state verification 
	 * 		and assignment if neccessary.
	 */
	 		
//			24/01/2007		topnode.parsimonyReconstructDown();
	//-		topnode.printStateWeights();
//			24/01/2007		if(topnode.getState().equals("<empty state>")){
//			24/01/2007			int randomAssignKey;
	//-			System.out.println("need to break ties!");
	//-			topnode.printStateWeights();
	/*
	 * 			TODO: this was the old method to break ties in the case of ties.
	 * 			needs a method generalised to N now.
	 */
//			24/01/2007			if(Math.random()>0.5){randomAssignKey = 1;}else{randomAssignKey = 0;}
//			24/01/2007			topnode.setState(availableStates[randomAssignKey]);
	//-			System.out.println("set tied topnode state to: " + topnode.getState() + " key " + randomAssignKey);
//			24/01/2007		}

			if(topnode.getState().equals("<empty state>")){
				topnode.setState(availableStates[new Random().nextInt(availableStates.length)]);
			}
			parsimonyReconstructedDown = true;
			
			boolean reconstructionFinished = false;
			int tRpasses = 1;
			while(!reconstructionFinished){
	//-			System.out.println();
	//-			System.out.println("UP PASS "+tRpasses);
				tRpasses ++;

				topnode.parsimonyReconstructUp();
				reconstructionFinished = topnode.isDone();			
			}
	/*
	 * 		NOTE - 23/7/2008
	 * 		for some reason have been pulling the wrong value out of getAI
	 * 		(it returns AI of topnode, AI of daughters)
	 * 		now pulling 0 values, seems to work better (!!!!)
	 */		
	//-		System.out.println(topnode.getAI()[0]+" "+topnode.getAI()[1]);
			AI = topnode.getAI()[0];
			AI = topnode.getAI()[0];
			AI = topnode.getAI()[0];
			AI = topnode.getAI()[0];
	//-		System.out.println(reconstructionFinished);
	//-		topnode.simpleShowNodes();

	/*		for(String throwaway: topnode.getTaxaFitchStates().getStates()){
	 *			System.out.println(throwaway + " fitched");
	 *		}
	 */

	/*
	 * 		state weight reporting:
	 */

	//-		System.out.print("topnode ");
//			topnode.printStateWeights();
	//-		System.out.println("...All this on\t" + treefile + "\n");
	/*		if(topnode.getTaxaGeneralizedFitchStates().isIntersection()){
				System.out.println("topnode is intersection");
			}
			if(topnode.bothDaughtersIntersections()){
				System.out.println("both daughters intersections");
			}
			if(topnode.someDaughtersIntersections()){
				System.out.println("some daughters intersections");
			}
	*/	
	/*		try{
				int interested = 0;
				System.out.println("topnode state "+topnode.getAvailableStates()[interested]+" ("+interested+") size: "+topnode.getSizeOfLargestNodeOfState(interested));
			} catch(Exception ex){}
	*/	
			topnode_distances = topnode.buildMatrix();
	//-		System.out.println("here size: "+taxaStates.size()+" of poss: "+availableStates.length);
			topnode_distances.setClusterLabels(taxaNames, taxaStates, availableStates);
	//-		topnode_distances.showMatrix();
			topnode_nodal_distances = topnode.buildNodalMatrix();
			topnode_nodal_distances.setClusterLabels(taxaNames, taxaStates, availableStates);
	//-		topnode_nodal_distances.showMatrix();
		}catch (HardPolytomyException hex){
			hex.printStackTrace();
			topnode = null;
		}finally{}
	}

	public void setAI(double ai) {
		AI = ai;
	}

	public double getAI() {
		return AI;
	}

	public String getTopnodeState(){
		return topnode.getState();
	}
	
	public float getHighestNodeExcluding(int unwantedState){
		return topnode.getHighestNodeExcludingState(unwantedState);
	}
	
	public int getBiggestExclusiveNodeOf(int wantedState){
		return topnode.getSizeOfLargestNodeOfState(wantedState);
	}
	
	public float getHeightOfBiggestExclusiveNodeOf(int wantedState){
		return topnode.getHeightOfLargestNodeOfState(wantedState);
	}
	
	public float getHeight(){
//o		return topnode.getTotalHeight(); // old tree height method
		return treeHeight;
	}
	
	public int getMigrationEvents(){
//o		topnode.setParentState(topnode.getState());
		return topnode.getMigrationEvents();
	}
	
	public float getPD(){
/*
 * 		old method
 */
/*		float combinedPD = 0.0f;
		float treeSize = topnode.getTotalSize();
//		int stateRef = 0;
		for(float aPD:topnode.getPDoldMethod(topnode.getStateWeights())){
			float retPD = aPD/treeSize;
			System.out.println("\tPD: "+aPD+" / "+treeSize+" = "+retPD);
			if(!Float.isNaN(retPD)){
				combinedPD += (float)(retPD);
			}else{
				// do nothing
			}
//			System.out.println("\tPD_i: "+PD_i+" / "+treeSize);
//			stateRef++;
		}
//		topnode.getDirectPD();
		

		return combinedPD;
*/
		return topnode.getPD(0);
	}
	
	public float getNTI(){
		float sumNTI = 0f;
		for(int i=0; i<availableStates.length;i++){
			sumNTI += topnode_distances.getObsNTI(i);
		}
		return (float)sumNTI/(float)availableStates.length;
	}
	
	public float getNRI(){
		float sumNRI = 0f;
		for(int i=0; i<availableStates.length;i++){
			sumNRI += topnode_distances.getObsNRI(i);
//			System.out.println(topnode_distances.getObsNRI(i));
		}
		return (float)sumNRI/(float)availableStates.length;
	}
	
	public float getNodalNTI(){
		float sumNTI = 0f;
		for(int i=0; i<availableStates.length;i++){
			sumNTI += topnode_nodal_distances.getObsNTI(i);
//-			System.out.println("adding nodal NTI: "+topnode_nodal_distances.getObsNTI(i));
		}
//		return (float)sumNTI/(float)availableStates.length;
		return (float)sumNTI/(float)availableStates.length;
	}
	
	public float getNodalNRI(){
		float sumNRI = 0f;
		for(int i=0; i<availableStates.length;i++){
			sumNRI += topnode_nodal_distances.getObsNRI(i);
		}
		return (float)sumNRI/(float)availableStates.length;
	}

	public float getUniFrac(){
//-		System.out.println("uni frac: "+topnode.getUniFrac()+" height: "+topnode.getHeight());
		float uniFrac = 0.0f;
		if (!new Float(topnode.getUniFrac()[0]).isNaN()) {
			float[] uniFracPair = topnode.getUniFrac();
/*
 * 			NOTE on UniFrac
 * 			21/july/2008
 * 
 * 			unifrac is currently implemented as the total amount of internal branches leading FROM
 * 			monophyletic nodes over total internal branch lengths
 * 
 * 			this was old call:
 * 			uniFrac = (uniFracPair[0]/topnode.getInternalBranchSize())/topnode.getAvailableStates().length;
 *
 *			have done this so that it is distributed on [0,1]
 * 
 */
			uniFrac = (uniFracPair[0]/topnode.getInternalBranchSize());
//-			System.out.println(uniFracPair[0]+" ("+uniFracPair[1]+") / "+topnode.getInternalBranchSize());
//-			System.out.println("\tuniFrac is cool: "+topnode.getUniFrac());
		}else{
//-			System.out.println("\tuniFrac to zero");
			uniFrac = 0;
		}
//-		System.out.println(uniFrac);
		return uniFrac;
	}
	
	public void showResult(){
		System.out.println("SHOWING\nNODES");
		topnode.simpleShowNodes();
	}

	public PairwiseMatrix getTopnode_distances() {
		return topnode_distances;
	}

	public PairwiseMatrix getTopnode_nodal_distances() {
		return topnode_nodal_distances;
	}
	
	public void getTreeDetails(){
		topnode.nodeDetails();
	}
	
	public float[] getAllStats(){
/*
 * 		retorder:
 * 			0-total size
 * 			1-internal size
 * 			2-AI
 * 			3-PS
 * 			4-UF
 * 			5-NT
 * 			6-NR
 * 			7-PD
 * 			8-M1
 * 			9-....to M(n)
 */		
		float[] retArr = new float[7+(availableStates.length*2)];
		for(float val:retArr){
			val = 0f;
		}
		retArr = topnode.getAllStats();
		retArr[4] = retArr[4] / retArr[1];
		float tPD = 0f;
		float[] MCs = new float[availableStates.length];
		for(int i=0;i<availableStates.length;i++){
			retArr[5] += topnode_distances.getObsNTI(i);
			retArr[6] += topnode_distances.getObsNRI(i);
			tPD += retArr[7+i];
			MCs[i] = retArr[7+availableStates.length+i];
		}
		for(int i=0;i<availableStates.length;i++){
			retArr[8+i] = MCs[i];
		}
		retArr[5] = retArr[5]/availableStates.length;
		retArr[6] = retArr[6]/availableStates.length;
		retArr[7] = tPD;
		return retArr;
	}
	
	public float getSize(){
		return topnode.getSize();
	}
	
	public float getInternalSize(){
		return topnode.getInternalBranchSize();
	}
}