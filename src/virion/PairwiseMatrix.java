package org.virion;

import java.util.ArrayList;

public class PairwiseMatrix {
	
	private String contents;
	private PairwiseMatrix matrix;
	private String[] labels;
	private float[] originDistances;
	private float[][] distances;
	private int elements;
	public int[][]stateClusterLabels;
	
	public PairwiseMatrix(){
		contents = null;
	}
	
	public PairwiseMatrix(String newContents){
		contents = newContents;
	}
	
	public PairwiseMatrix(String[]labels,float[]originDistances,float[][]distances){
		this.labels = labels;
		this.originDistances = originDistances;
		this.distances = distances;
		this.elements = originDistances.length;
	}
	
	public void show(){
		System.out.println(contents);
	}
	
	public void showMatrix(){
		if(elements>0){
			for(String label:labels){
				System.out.print(label+"\t");
			}
			System.out.println("Origin");
			for(int i=0;i<elements;i++){
				for(int j=0;j<elements;j++){
					System.out.print(distances[j][i]+"\t");
				}
				System.out.println(originDistances[i]);
			}
		}else{
			System.out.println("no elements in matrix.");
		}
	}
	
	public PairwiseMatrix combine(PairwiseMatrix newMatrix, float[] newInputOriginDistances){
		String[] addLabels = newMatrix.getLabels();
		float[] addOriginDistances = newMatrix.getOriginDistances();
		float[][] addDistances = newMatrix.getDistances();
		
		int newElements = elements+newMatrix.getElements();
		String [] newLabels = new String [newElements];
		float[] newOriginDistances = new float [newElements];
		float[][] newDistances = new float[newElements][newElements];
//-		System.out.println("new elements: "+ newElements);
		
		for(int i=0;i<elements;i++){
			newLabels[i] = labels[i];
			newOriginDistances[i] = originDistances[i]+newInputOriginDistances[0];
			for(int j=0;j<elements;j++){
				newDistances[j][i] = distances[j][i];
			}
			for(int j=0;j<newMatrix.getElements();j++){
/*
 * 				REV 15/7/08
				newDistances[j+elements][i] = originDistances[i]+addOriginDistances[j]+newInputOriginDistances[0];
 */
 				newDistances[j+elements][i] = originDistances[i]+addOriginDistances[j]+newInputOriginDistances[0]+newInputOriginDistances[1];
			}
		}
		for(int i=0;i<newMatrix.getElements();i++){
			newLabels[i+elements] = addLabels[i];
			newOriginDistances[i+elements] = addOriginDistances[i]+newInputOriginDistances[1];
			for(int j=0;j<newMatrix.getElements();j++){
				newDistances[j+elements][i+elements] = addDistances[j][i];
			}
		}
		PairwiseMatrix newCombinedMatrix = new PairwiseMatrix(newLabels,newOriginDistances,newDistances);
		return newCombinedMatrix;
	}

	public PairwiseMatrix nodeCombine(PairwiseMatrix newMatrix, float[] newInputOriginDistances){
		String[] addLabels = newMatrix.getLabels();
		float[] addOriginDistances = newMatrix.getOriginDistances();
		float[][] addDistances = newMatrix.getDistances();
		
		int newElements = elements+newMatrix.getElements();
		String [] newLabels = new String [newElements];
		float[] newOriginDistances = new float [newElements];
		float[][] newDistances = new float[newElements][newElements];
//-		System.out.println("new elements: "+ newElements);
		
		for(int i=0;i<elements;i++){
			newLabels[i] = labels[i];
			newOriginDistances[i] = originDistances[i]+newInputOriginDistances[0];
			for(int j=0;j<elements;j++){
				newDistances[j][i] = distances[j][i];
			}
			for(int j=0;j<newMatrix.getElements();j++){
/*
 * 				REV 15/7/08
 				newDistances[j+elements][i] = originDistances[i]+addOriginDistances[j]+newInputOriginDistances[0]+newInputOriginDistances[1];
 */
//-				System.out.println("\tnewDist j:"+j+" i:"+i+" origin: "+originDistances[i]+" addO: "+addOriginDistances[i]+" new IO [0]: "+newInputOriginDistances[0]+" new IO [i]:"+newInputOriginDistances[i]);
				newDistances[j+elements][i] = originDistances[i]+addOriginDistances[j]+newInputOriginDistances[0];
			}
		}
		for(int i=0;i<newMatrix.getElements();i++){
			newLabels[i+elements] = addLabels[i];
			newOriginDistances[i+elements] = addOriginDistances[i]+newInputOriginDistances[1];
			for(int j=0;j<newMatrix.getElements();j++){
				newDistances[j+elements][i+elements] = addDistances[j][i];
			}
		}
		PairwiseMatrix newCombinedMatrix = new PairwiseMatrix(newLabels,newOriginDistances,newDistances);
		return newCombinedMatrix;
	}

	public PairwiseMatrix complexCombine(PairwiseMatrix[] newMatrices, float[] newInputOriginDistances){
		PairwiseMatrix outputComplexMatrix = this;
		for(int i=0;i<newMatrices.length;i++){
			if(i==0){
/*
 * 				15/7/2008
 * 				removed first element to 0f from newInputOriginDistances[i]
 * 				float[] aggInputOriginDistances = {newInputOriginDistances[i],newInputOriginDistances[i+1]};

 */
				float[] aggInputOriginDistances = {newInputOriginDistances[i],newInputOriginDistances[i+1]};
				outputComplexMatrix = outputComplexMatrix.combine(newMatrices[i], aggInputOriginDistances);
			}else{
				float[] aggInputOriginDistances = {0f,newInputOriginDistances[i+1]};
				outputComplexMatrix = outputComplexMatrix.combine(newMatrices[i], aggInputOriginDistances);
			}
//-			outputComplexMatrix.showMatrix();
		}
		return outputComplexMatrix;
	}

	public PairwiseMatrix complexNodeCombine(PairwiseMatrix[] newMatrices, float[] newInputOriginDistances){
		PairwiseMatrix outputComplexMatrix = this;
		for(int i=0;i<newMatrices.length;i++){
			if(i==0){
/*
 * 				15/7/2008
 * 				removed first element to 0f from newInputOriginDistances[i]
 * 				float[] aggInputOriginDistances = {newInputOriginDistances[i],newInputOriginDistances[i+1]};

 */
//o				float[] aggInputOriginDistances = {0f,newInputOriginDistances[i+1]};
				float[] aggInputOriginDistances = {newInputOriginDistances[i],newInputOriginDistances[i+1]};
				outputComplexMatrix = outputComplexMatrix.nodeCombine(newMatrices[i], aggInputOriginDistances);
			}else{
				float[] aggInputOriginDistances = {0f,newInputOriginDistances[i+1]};
				outputComplexMatrix = outputComplexMatrix.nodeCombine(newMatrices[i], aggInputOriginDistances);
			}
//-			outputComplexMatrix.showMatrix();
		}
		return outputComplexMatrix;
	}

	public String getContents() {
		return contents;
	}

	public PairwiseMatrix getMatrix() {
		return matrix;
	}

	public String[] getLabels() {
		return labels;
	}

	public float[] getOriginDistances() {
		return originDistances;
	}

	public float[][] getDistances() {
		return distances;
	}

	public int getElements() {
		return elements;
	}
	
	public float getExpNTI(){
		return 0.0f;
	}
	
	public float getDirectNRI(ArrayList<Integer> pairs){
		float NRI = 0f;
		for(int i_coord:pairs){
			System.out.println("Abs.min:"+i_coord);
			float cand=99999f;
			float nri=cand;
			for(int j_coord:pairs){
				nri = distances[i_coord][j_coord];
				if(nri==0){
					nri = distances[j_coord][i_coord];
				}
				if(i_coord!=j_coord){
					System.out.println("i: "+i_coord+" j: "+j_coord+" nri "+nri);
					if((nri>0)&&(nri<cand)){
						cand = nri;
						System.out.println("i: "+i_coord+" j: "+j_coord+" nri "+nri);
					}
				}
			}
			if(cand != 99999){
				NRI += cand;
			}
		}
		return NRI;
	}
	
	public float[] getExpNRI(int maxSize){
		float [] retVector = new float[maxSize];
		retVector[0] = 0f;
//-		System.out.println("Get exp. max NRI to "+maxSize+" states");
		int start_i = 0;
		int start_j = 0;
		float NRI = 0f;
		for(int i=0;i<elements;i++){
			for(int j=0;j<elements;j++){
				if((i!=j)&&(distances[i][j]>NRI)){
					start_i = i;
					start_j = j;
					NRI = distances[i][j];
				}
			}
		}
		ArrayList<Integer> tipsPickList = new ArrayList<Integer>();
		ArrayList<Integer> tipsChosenList = new ArrayList<Integer>();
		for(int i=0;i<elements;i++){
			if((i!=start_i)&&(i!=start_j)){
				tipsPickList.add(i);
			}
		}
		tipsChosenList.add(start_i);
		tipsChosenList.add(start_j);
		retVector[0] = NRI;
		retVector[1] = NRI;
		System.out.println(NRI+", starting size: "+tipsChosenList.size()+" ("+tipsChosenList.get(0)+", "+tipsChosenList.get(1)+").");
		for(int i=2;i<maxSize;i++){
			float gain = 0f;
			int newPick = 0;
			for(int available:tipsChosenList){
				for(int possible:tipsPickList){
					if(distances[available][possible]>gain){
						gain = distances[available][possible];
						newPick = possible;
						System.out.println("gain: "+gain);
					}
				}
			}
			for(int j=0;j<tipsPickList.size();j++){
				if(tipsPickList.get(j)==newPick){
					tipsPickList.remove(j);
					tipsChosenList.add(newPick);
				}
			}
			retVector[i] = this.getDirectNRI(tipsChosenList);
		}
		for(int picked:tipsChosenList){
			System.out.println(picked+" picked");
		}
		return retVector;
	}
	
	public float getObsNTI(int state){
		float obsNTI = 0f;
//-		System.out.println(state);
		for(int locs:stateClusterLabels[state]){
//-			System.out.println("\t"+locs);
			float best = 999999f;
			for(int i:stateClusterLabels[state]){
				float candidateDistance = distances[locs][i];
				if(candidateDistance < best && candidateDistance !=0){
//-					System.out.println(candidateDistance);
					best = candidateDistance;
				}
				candidateDistance = distances[i][locs];
				if(candidateDistance < best && candidateDistance !=0){
//-					System.out.println(candidateDistance);
					best = candidateDistance;
				}
			}
			if(best != 999999f){
				obsNTI += best;
			}
		}
//		return obsNTI/stateClusterLabels[state].length;
		return obsNTI;
	}
	
	public float getObsNRI(int state){
		float obsNRI = 0.0f;
		ArrayList<Float> cands = new ArrayList<Float>();
		for(int i=0;i<stateClusterLabels[state].length;i++){
//			float candNRI = 0f;
			for(int j=0;j<stateClusterLabels[state].length;j++){
				if(i!=j){
					float i_cand = distances[stateClusterLabels[state][j]][stateClusterLabels[state][i]];
//					float j_cand = distances[stateClusterLabels[state][i]][stateClusterLabels[state][j]];
//					System.out.print("i:"+i+" : "+i_cand);
//					System.out.println("\tj:"+j+" : "+j_cand);
					if(i_cand != 0){
//						candNRI = i_cand;
						cands.add(i_cand);
					}
//					if(j_cand != 0){
//						candNRI = j_cand;
//					}
				}
			}
		}
		for(float NRI:cands){
			obsNRI+=NRI;
		}
//		return obsNRI/stateClusterLabels[state].length;
		return obsNRI;
	}

	public void getClusterLabels(){
		for(int i=0;i<stateClusterLabels.length;i++){
			int[] statesIndices = stateClusterLabels[i];
			for(int j=0;j<statesIndices.length;j++){
				System.out.print(statesIndices[j]+"\t");
			}
			System.out.println("State "+i);
		}
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setMatrix(PairwiseMatrix matrix) {
		this.matrix = matrix;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public void setOriginDistances(float[] originDistances) {
		this.originDistances = originDistances;
	}

	public void setDistances(float[][] distances) {
		this.distances = distances;
	}

	public void setElements(int elements) {
		this.elements = elements;
	}
	
	public void setClusterLabels(ArrayList<String>taxaNames,ArrayList<String> states,String[] availableStates){
		// matrix of state references; 0 = refs in distances[][] for first state, etc
		// so that all columns of state i can be pulled out with distances[stateClusterLabels[i]] 
		stateClusterLabels = new int[availableStates.length][states.size()];
/*
		System.out.println("launching iterations:");
		System.out.println("\ti:"+availableStates.length);
		System.out.println("\tj:"+labels.length);
		System.out.println("\tk:"+taxaNames.size());
		System.out.println("\tk':"+states.size());
		System.out.println("\navail\tlabels\tnames\tstates");
		for(int i=0;i<taxaNames.size();i++){
			String av = null;
			String la = null;
			String tn = null;
			String ss = null;
			if(i<availableStates.length){
				av = availableStates[i];
			}
			if(i<labels.length){
				la = labels[i];
			}
			if(i<taxaNames.size()){
				tn = taxaNames.get(i);
			}
			if(i<states.size()){
				ss = states.get(i);
			}
			System.out.println(av+"\t"+la+"\t"+tn+"\t"+ss);
		}
*/
		for(int i=0;i<availableStates.length;i++){
			ArrayList<Integer> thisStateRefs = new ArrayList<Integer>();
			for(int j=0;j<labels.length;j++){
				for(int k=0;k<taxaNames.size();k++){
//-					System.out.println("labels i,j,k:"+i+", "+j+", "+k);
					if(labels[j].equals(taxaNames.get(k))&&states.get(k).equals(availableStates[i])){
						thisStateRefs.add(j);
					}
				}
			}
			int[] thisStateRefsVector = new int[thisStateRefs.size()];
			for(int j=0;j<thisStateRefs.size();j++){
				thisStateRefsVector[j] = thisStateRefs.get(j);
			}
			stateClusterLabels[i] = thisStateRefsVector;
		}
	}
}
