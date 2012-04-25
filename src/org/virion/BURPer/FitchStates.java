package org.virion.BURPer;

import java.util.TreeSet;
import java.util.ArrayList;

public class FitchStates {
	private final TreeSet<String> states = new TreeSet<String>();
	private boolean intersection = false;
	
	public FitchStates(){
	}
	
	public FitchStates(String newState, boolean newIntersection){
		this.states.add(newState);
		this.intersection = newIntersection;
	}
	
	public FitchStates(TreeSet<String> initStates, boolean newIntersection){
		for(String newState:initStates){
			states.add(newState);
		}
		this.intersection = newIntersection;
	}
	
	public FitchStates combine(FitchStates candidateStates){
		TreeSet<String> newStates = new TreeSet<String>();
		TreeSet<String> candStates = candidateStates.getStates();
		
/*		method for comparison of characters on first (upwards) pass
 * 	
 * 		REM: 	a + b = _	 	intersection?
 * 				a + a = a		union
 * 				b + b = b		union
 * 				ab + ab = ab 	union
 * 				a + _ = a		intersection? - null
 * 				b + _ = b		intersection? - null
 * 				ab + _ = ab		intersection? - null
 * 				ab + a = a		intersection
 * 				ab + b = b		intersection
 */		
		boolean newIntersection = false;
		
		if(states.size() == candStates.size()){
			if (states.size() == 1) {
//				each only have one state: hold over or union					
//c				union
				if (states.contains(candStates.first())) {
//					{A} + {A} = {A}
					newStates = states;
				} else {
//					{A} + {B} = {}
					newIntersection = true;
//o					newStates.add(states.first());
//o					newStates.add(candStates.first());
				}
			} else {
//				both have either 0 or 2; in each case, unify
//				{AB} + {AB} = {AB}
				newStates = states;
			}			
		} else {
//			imbalanced sizes
//c			intersection
//-			System.out.println("here made an intersection call.\nstates " + states + "candStates " + candStates);
			if(states.size() == 0){
//				states is empty. use candStates
				newStates = candStates;
			} else if(candStates.size() == 0){
//				candstates is empty. use states
				newStates = states;
			} else if(states.size() > candStates.size()){
//				candStates is smaller, exclude spare state in states
				newStates = candStates;
//o				newIntersection = true;
			} else {
//				states is smaller, exclude candStates' spare state
				newStates = states;
//o				newIntersection = true;
			}
		}

//-		System.out.println("here made an intersection call (" + newIntersection + ").\nstates " + states + "candStates " + candStates + "\nnew states as entered: " + newStates);
		
		return new FitchStates(newStates, newIntersection);
	}

	public TreeSet<String> getStates() {
		return states;
	}
	
	public boolean isIntersection() {
		return intersection;
	}

	public void setIntersection(boolean intersection) {
		this.intersection = intersection;
	}

	public boolean isAmbiguous(){
		if(states.size() > 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean compare(FitchStates candidate){
		boolean areSame = false;
		if(this.states.containsAll(candidate.getStates())){
			areSame = true;
		}
		return areSame;
	}

	public boolean includes(FitchStates parentStates){
		boolean includes = false;
		for(String someState:parentStates.getStates()){
			if(states.contains(someState)){
				includes = true;
			} else {
				includes = false;
			}
		}
		return includes;
	}
	
	public void fitchSolve(FitchStates parentStates){
/*
 * 		here implemented the second step of the 
 * 		Fitch (1971) algorithm
 * 
 * 		actually, step V refers to parents and children of this node, 
 * 		so may need to do it in Node.... bummer
 * 
 */		
		if(this.includes(parentStates)){
//	2			
//			do some eliminating shit; then call supplementStates (4)
		} else {
//	3			
//			if this is a union of children{supplementStates}else{thickenStates}
		}
	}
	
	public void eliminateStates(FitchStates parentStates){
		for(int i=0; i<states.size(); i ++){
			if(!parentStates.getStates().contains(states.first())){
				states.remove(states.first());
			}
		}
	}
	
	public void supplementStates(FitchStates parentStates){
//	4
//		add to states any states in parent not in this one
		for(String parentState:parentStates.getStates()){
			if(!states.contains(parentState)){
				states.add(parentState);
			}
		}
	}
	
	public void thickenStates(FitchStates parentStates, ArrayList<FitchStates> childStates){
//	5
//		add to states any states present in both children AND parent
		for(FitchStates child:childStates){
			for(String childState:child.getStates()){
				if(parentStates.getStates().contains(childState)){
					if(!states.contains(childState)){
						states.add(childState);
					}
				}
			}
		}
	}
	
	public boolean isUnion(ArrayList<FitchStates> children){
		boolean isUnion = false;
		boolean childrenAreSingletons = false;
		for(FitchStates child:children){
			if(child.getStates().size() == 1){
				childrenAreSingletons = true;
			} else {
				childrenAreSingletons = false;
			}
		}
		if(states.size() == 2 && childrenAreSingletons){
//			fulfils dimensionality requirements for union.
//			check union
			if(children.get(0).getStates().contains(children.get(1).getStates().first())){
				isUnion = false;
			} else {
				isUnion = true;
			}
		}
		return isUnion;
	}
}