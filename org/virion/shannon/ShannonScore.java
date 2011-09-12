package org.virion.shannon;

import java.util.ArrayList;

public class ShannonScore {
	private final int[] scores;
	private final int[] uniqueItems;
	private final int size;
	
	public ShannonScore(int[] input){
		this.scores = input;
		this.size = input.length;

//		make a list of sites that occur		
		ArrayList<Integer> uniqueItemsList = new ArrayList<Integer>();
		for(int val:scores){
			if(!uniqueItemsList.contains(val)){
				uniqueItemsList.add(val);
			}
		}
		uniqueItems = new int[uniqueItemsList.size()];
		for(int i=0; i<uniqueItems.length; i++){
			uniqueItems[i] = uniqueItemsList.get(i);
		}
		
	}
	
	public double getEntropy(){
		double entropy = 0.0;

//		iterate through each site that occurs @ get frequency
		for(int occurring:uniqueItems){
//-			System.out.println(occurring);
			int count = 0;
			for(int obs:scores){
				if(obs == occurring){
					count ++;
				}
			}
			double likelihood = ((double)count)/((double)size);
			Double eventEntropy = -(likelihood * (Math.log(likelihood) / Math.log(uniqueItems.length)));
			if (!eventEntropy.isNaN()) {
				entropy += eventEntropy;
			}					
		}
		return entropy;
	}

	/**
	 * @return the scores
	 */
	public int[] getScores() {
		return scores;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the uniqueItems
	 */
	public int[] getUniqueItems() {
		return uniqueItems;
	}
}
