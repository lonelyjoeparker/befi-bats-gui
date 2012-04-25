package org.virion.BURPer;

import java.util.TreeSet;
import java.util.ArrayList;

public class GeneralizedFitchStates {
	private final TreeSet<String> states = new TreeSet<String>();
	private boolean intersection = false;
	
	public GeneralizedFitchStates(){
	}
	
	public GeneralizedFitchStates(String newState, boolean newIntersection){
		this.states.add(newState);
		this.intersection = newIntersection;
	}
	
	public GeneralizedFitchStates(TreeSet<String> initStates, boolean newIntersection){
		for(String newState:initStates){
			states.add(newState);
		}
		this.intersection = newIntersection;
	}
	
	public GeneralizedFitchStates combine(GeneralizedFitchStates candidateStates){
		TreeSet<String> newStates = new TreeSet<String>();
		TreeSet<String> candStates = candidateStates.getStates();
		boolean newIntersection = false;

/*
 * 		The revised method (24/01/2007)
 * 		(a generalization to n states)
 * 
 * 		Predicated on 3 rules:
 * 			1. if all and only F1 in F2 => union, F' = F1 = F2
 * 			2. if no F1 in F2 => intersection, F' = F1 + F2
 * 			3. if some F1 in F2 (or v-v) => intersection, F' = uF1 uF2 (i.e., only those elements common to both)
 *
 *		Rules 1) and 2) can be applied in a single loop:
 */		
		ArrayList<Boolean> ListUnion = new ArrayList<Boolean>();
		ArrayList<Boolean> ListIntersection = new ArrayList<Boolean>();
		boolean simpleUnion = false;
		boolean simpleIntersection = false;
		
		for(String F1state:states){
			if(candStates.contains(F1state)){
				ListUnion.add(true);
				ListIntersection.add(false);
			} else {
				ListUnion.add(false);
				ListIntersection.add(true);
			}
		}
		
		if(!ListUnion.contains(false)){
			simpleUnion = true;
			newStates = states;
			newIntersection = false;
		}
		
		if(!ListIntersection.contains(false)){
			simpleIntersection = true;
			newStates = states;
			newStates.addAll(candStates);
			newIntersection = true;
		}
		
/*
 * 		On the basis that simpleUnion and simpleIntersection initialise as both false,
 * 		but activate exclusively, if both false then run rule 3
 */		
		if(!simpleIntersection && !simpleUnion){
			for(String F1state:states){
				if(candStates.contains(F1state)){
					newStates.add(F1state);
				}
			}
			/*
			 * 		modification 22/07/2008
			 * 		
			 * 		not sure new intersection is adding properly, returning true now
			 */
//-			newIntersection = false;
			newIntersection = true;
		}
		
		return new GeneralizedFitchStates(newStates, newIntersection);
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
	
	public boolean compare(GeneralizedFitchStates candidate){
		boolean areSame = false;
		if(this.states.containsAll(candidate.getStates())){
			areSame = true;
		}
		return areSame;
	}

	public boolean includes(GeneralizedFitchStates parentStates){
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
	
	public void fitchSolve(GeneralizedFitchStates parentStates){
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
	
	public void eliminateStates(GeneralizedFitchStates parentStates){
		for(int i=0; i<states.size(); i ++){
			if(!parentStates.getStates().contains(states.first())){
				states.remove(states.first());
			}
		}
	}
	
	public void supplementStates(GeneralizedFitchStates parentStates){
//	4
//		add to states any states in parent not in this one
		for(String parentState:parentStates.getStates()){
			if(!states.contains(parentState)){
				states.add(parentState);
			}
		}
	}
	
	public void thickenStates(GeneralizedFitchStates parentStates, ArrayList<GeneralizedFitchStates> childStates){
//	5
//		add to states any states present in both children AND parent
		for(GeneralizedFitchStates child:childStates){
			for(String childState:child.getStates()){
				if(parentStates.getStates().contains(childState)){
					if(!states.contains(childState)){
						states.add(childState);
					}
				}
			}
		}
	}
	
	public boolean isUnion(ArrayList<GeneralizedFitchStates> children){
		boolean isUnion = false;
		boolean childrenAreSingletons = false;
		for(GeneralizedFitchStates child:children){
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