package org.virion.testHarnesses;

import org.virion.PairwiseMatrix;
import java.util.ArrayList;

public class PairwiseMatrixTest {

	public static void main(String [] args){
		PairwiseMatrix pw = new PairwiseMatrix();
		pw.show();
		pw = new PairwiseMatrix("boolocks");
		pw.show();
		String[] labels = {"A","B","C"};
		float[] originDistances = {1f,1f,1f};
		float[] d1 = {0f,0f,0f};
		float[] d2 = {1f,0f,0f};
		float[] d3 = {2f,2f,0f};
		float[][]distances = {d1,d2,d3};
		pw = new PairwiseMatrix(labels,originDistances,distances);
		pw.showMatrix();
		String[] new_labels = {"D","E","F"};
		float[] new_originDistances = {3f,3f,3f};
		float[] n_d1 = {0f,0f,0f};
		float[] n_d2 = {1f,0f,0f};
		float[] n_d3 = {6f,6f,0f};
		float[][] new_distances = {n_d1,n_d2,n_d3};
		PairwiseMatrix npw = new PairwiseMatrix(new_labels,new_originDistances,new_distances);
		npw.showMatrix();
		float[] newOriginDistances = {9f,7f};
		PairwiseMatrix xpw = pw.combine(npw,newOriginDistances);
		xpw.showMatrix();
		String[] nn_labels = {"G","H"};
		float[] nn_originDistances = {1f,2f};
		float[] nn_d1 = {0f,0f};
		float[] nn_d2 = {3f,0f};
		float[][] nn_distances = {nn_d1,nn_d2};
		PairwiseMatrix pw_3 = new PairwiseMatrix(nn_labels,nn_originDistances,nn_distances);
		float[] nn_newOriginDistances = {9f,9f};
		PairwiseMatrix pw_4 = pw.combine(pw_3,nn_newOriginDistances);
		pw_4.showMatrix();
		String[] term_label = {"terminal"};
		float[] term_distance = {3.21f};
		float[] term_d1={0f};
		float[][] term_distances = {term_d1};
		PairwiseMatrix pw_terminal = new PairwiseMatrix(term_label,term_distance,term_distances);
		pw_terminal.showMatrix();
		float[]newInputOriginDistances = {9f,6.79f};
		PairwiseMatrix buildTerm = pw.combine(pw_terminal, newInputOriginDistances);
		buildTerm.showMatrix();
		float[]fuckDists = {9f,7f,9f};
		PairwiseMatrix[] fuckMats = {npw,pw_3};
//		PairwiseMatrix behemoth = pw.complexCombine(fuckMats,fuckDists);
		PairwiseMatrix behemoth = xpw;
		behemoth.showMatrix();
//		String[] taxa = {"A","B","C","D","E","F","G","H"};
//		String[] states={"b","b","b","a","a","a","g","g"};
//		String[] availableStates = {"b","a","g"};
		String[] taxa = {"A","B","C","D","E","F"};
		String[] states={"b","b","b","a","a","a"};
		String[] availableStates = {"b","a"};
		ArrayList<String>taxaNames = new ArrayList<String>();
		ArrayList<String>taxaStates = new ArrayList<String>();
		for(int i=0;i<taxa.length;i++){
			taxaNames.add(taxa[i]);
			taxaStates.add(states[i]);
		}
		behemoth.setClusterLabels(taxaNames, taxaStates, availableStates);
		behemoth.getClusterLabels();
//		for(float dist:behemoth.getDistances()[behemoth.getElements()-1]){
//			System.out.println(dist);
//		}
		float[] expNRI = behemoth.getExpNRI(5);
		for(int i=0; i<2;i++){
			System.out.println("observed NRI ("+i+"): "+behemoth.getObsNRI(i));
//			System.out.println("expected NRI ("+i+"): "+(expNRI[behemoth.stateClusterLabels[i].length]/behemoth.stateClusterLabels[i].length));
			System.out.println("expected NRI ("+i+"): "+(expNRI[2]));
			System.out.println("observed NTI ("+i+"): "+behemoth.getObsNTI(i));
		}

		for(float ei:expNRI){
			System.out.println("e: "+ei);
		}

	}
}
