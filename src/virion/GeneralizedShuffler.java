package org.virion;

import java.util.ArrayList;
import java.util.Random;

public class GeneralizedShuffler{

	private final ArrayList<String> input;
	private ArrayList<ArrayList<String>> output;
	private int reps;
	
	public GeneralizedShuffler(){
//c		default no-arg	
		this.input = null;
	}
	
	public GeneralizedShuffler(ArrayList<String> input){
//c		input constructor
		this.input = input;
	}

	public GeneralizedShuffler(ArrayList<String> input, int reps){
//c		input constructo where reps are specified
		this.input = input;
		this.reps = reps;
	}
	
	public ArrayList<ArrayList<String>> shuffle(){
//c		the shuffle method
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		int inputSize = input.size();

		for(int i = 0; i < reps; i ++){
			Random generator = new Random();
			ArrayList<Integer> flexiKeys = new ArrayList<Integer>();
			for(int j=0;j<inputSize;j++){
				flexiKeys.add(j);
			}
			ArrayList<String> newlyShuffled = new ArrayList<String>();
			int[] keys = new int[inputSize];
			for(int j=inputSize;j>0;j--){
				int key = generator.nextInt(j);
//-				System.out.println("key: " + key + " j: " + j);
				keys[j-1] = flexiKeys.remove(key);
			}
			for(int key:keys){
				newlyShuffled.add(input.get(key));
			}
			output.add(newlyShuffled);
//-			System.out.println("shuffled set size " + newlyShuffled.size() + " from input size " + input.size());
		}
		return output;
	}
	
	public void setReps(int newReps){
		this.reps = newReps;
	}
}