package org.virion;

import java.util.ArrayList;
import java.util.Random;

public class shuffler{

	private final ArrayList<String> input;
	private ArrayList<ArrayList<String>> output;
	private int reps;
	
	public shuffler(){
//c		default no-arg	
		this.input = null;
	}
	
	public shuffler(ArrayList<String> input){
//c		input constructor
		this.input = input;
	}

	public shuffler(ArrayList<String> input, int reps){
//c		input constructor where reps are specified
		this.input = input;
		this.reps = reps;
	}
	
	public ArrayList<ArrayList<String>> shuffle(){
//c		the shuffle method
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> flexiKeys = new ArrayList<Integer>();
		int numFirstStates = 0;
		int inputSize = input.size();
		String firstState = input.get(0);
		String secondState = null;
		for(String state:input){
			if(state.equals(firstState)){
				numFirstStates ++;
			} else {
				secondState = state;
			}
		}
		for(int i=0;i<inputSize;i++){
			flexiKeys.add(i);
		}
		for(int i = 0; i < reps; i ++){
			Random generator = new Random();
			ArrayList<Integer> flexiKeysCopy = new ArrayList<Integer>();
			flexiKeysCopy.addAll(flexiKeys);
			ArrayList<String> newlyShuffled = new ArrayList<String>();
			for(int j=0;j<inputSize;j++){
				newlyShuffled.add(secondState);
			}
			for(int j=0;j<numFirstStates;j++){
/*				int randomKey = generator.nextInt(flexiKeysCopy.size());
				int key = flexiKeysCopy.remove(randomKey);
				System.out.println("j "+j+" numFistStates " + numFirstStates+" flexi copy size "+flexiKeysCopy.size()+" key "+ key + " random key " + randomKey);
*/				newlyShuffled.set(flexiKeysCopy.remove(generator.nextInt(flexiKeysCopy.size())), firstState);
			}
			output.add(newlyShuffled);
		}
		return output;
	}
	
	public void setReps(int newReps){
		this.reps = newReps;
	}
}