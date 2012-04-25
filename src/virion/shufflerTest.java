package org.virion;

import java.util.ArrayList;

class shufflerTest{
	
	public static void main(String [] args){
		shufflerTest sT = new shufflerTest();
		ArrayList<String> testIN = new ArrayList<String>();
		int testReps = 10;
		testIN.add("banana");
		testIN.add("is");
		testIN.add("nice.");
		shuffler s = new shuffler(testIN, testReps);
		ArrayList<ArrayList<String>> output = s.shuffle();
		for(ArrayList<String> oneRep:output){
			System.out.println(oneRep);
		}
	}
}