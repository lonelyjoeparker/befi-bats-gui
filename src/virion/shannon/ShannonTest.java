package org.virion.shannon;

import org.virion.shannon.ShannonScore;

public class ShannonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] scores1 = {1,2,3,4,5,6,7,8};
		System.out.println("entropy is "+ new ShannonScore(scores1).getEntropy());
		int[] scores2 = {3,3,3,3,3,3,3,3};
		System.out.println("entropy is "+ new ShannonScore(scores2).getEntropy());
		int[] scores3 = {11,11,11,11,3,3,3,3};
		System.out.println("entropy is "+ new ShannonScore(scores3).getEntropy());
		int[] scores4 = {1,2,3,2,5,3,3,3};
		System.out.println("entropy is "+ new ShannonScore(scores4).getEntropy());
	}

}
