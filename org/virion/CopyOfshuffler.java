package org.virion;

import java.util.ArrayList;

public class CopyOfshuffler{

	private final ArrayList<String> input;
	private ArrayList<ArrayList<String>> output;
	private int reps;
	
	public CopyOfshuffler(){
//c		default no-arg	
		this.input = null;
	}
	
	public CopyOfshuffler(ArrayList<String> input){
//c		input constructor
		this.input = input;
	}

	public CopyOfshuffler(ArrayList<String> input, int reps){
//c		input constructo where reps are specified
		this.input = input;
		this.reps = reps;
	}
	
	public ArrayList<ArrayList<String>> shuffle(){
//c		the shuffle method
		int inputSize = input.size();
		ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> flexiKeys = new ArrayList<Integer>();
		for(int i=0;i<input.size();i++){
			flexiKeys.add(i);
		}
		for(int i = 0; i < reps; i ++){
			ArrayList<String> inputCopy = new ArrayList<String>();
			ArrayList<String> newlyShuffled = new ArrayList<String>();
			for(String entry:input){
				inputCopy.add(entry);
			}
//-			System.out.println("starting ns " + newlyShuffled);
//-			System.out.println("input " + input);
//-			System.out.println("starting IC " + inputCopy);
			for(int j = 0; j < inputSize; j ++){
//-				System.out.println("input " + input);

				int inputCopySize = inputCopy.size();
				if(inputCopySize > 0){
//-					System.out.println("ics " + inputCopySize);
					int randomKey = (int) (Math.random()*inputCopySize);
//-					System.out.println(randomKey);
					newlyShuffled.add(inputCopy.get(randomKey));
					inputCopy.remove(randomKey);				
				}
			}
//-			System.out.println("ending ns " + newlyShuffled);
//-			System.out.println("input " + input);
//-			System.out.println("ending IC " + inputCopy);
			output.add(newlyShuffled);
//-			System.out.println(reps + " + " + i + "\n");
		}
		return output;
	}
	
	public void setReps(int newReps){
		this.reps = newReps;
	}
}