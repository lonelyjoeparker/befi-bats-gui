package org.virion;

import java.util.Arrays;

public class StatisticHandler {
	final float[] oVals;
	final float[] nVals;
	final boolean lowValuesAreExtreme;
	
	public StatisticHandler(float[] oValsIn, float[] nValsIn, boolean direction){
		Arrays.sort(oValsIn);
		Arrays.sort(nValsIn);
		this.oVals = oValsIn;
		this.nVals = nValsIn;
		this.lowValuesAreExtreme = direction;
	}
	
	public StatisticHandler(){
		this.oVals = new float[0];
		this.nVals = new float[0];
		this.lowValuesAreExtreme = false;
	}

	public float getMean(){
		float mean = 0;
		for(float val:oVals){
			mean += val;
		}
		return mean / oVals.length;
	}

	public float getNullMean(){
		float mean = 0;
		for(float val:nVals){
			mean += val;
		}
		return mean / nVals.length;
	}

	public float getMedian(){
		float median=0;
		median = oVals[oVals.length/2];
		return median;
	}
	
	public float getNullMedian(){
		float median=0;
		median = nVals[nVals.length/2];
		return median;
	}

	public float getRangeInterval(){
		return oVals[oVals.length-1] - oVals[0];
	}
	
	public float getNullRangeInterval(){
		return nVals[nVals.length-1] - nVals[0];
	}
	
	public float getUpperCI(){
		System.out.println(oVals.length + " " + Math.round((float)(oVals.length*0.95)));
		return oVals[Math.round((float)(oVals.length*0.95))-1];
	}
	
	public float getLowerCI(){
		return oVals[Math.round((float)(oVals.length*0.05))];
	}
	
	public float getUpperNullCI(){
		return nVals[Math.round((float)(nVals.length*0.95))-1];
	}
	
	public float getLowerNullCI(){
		return nVals[Math.round((float)(nVals.length*0.05))];
	}
	
	public float getSignificance(){
		int index = 0;
		float median = oVals[oVals.length / 2];
//-		System.out.println(median);
		if(lowValuesAreExtreme){
/*
 * 			get the proportion of null trees
 * 			greater than the observed
 * 			median
 */
			for(int i = 0; i< nVals.length; i++){
				if(nVals[i] > median){
					index ++;
				}
			}
		} else {
/*
 * 			get the proportion of null trees
 * 			less than the observed
 * 			median
 */			
			for(int i = 0; i< nVals.length; i++){
				if(nVals[i] < median){
					index = i;
				}
			}
		}
//-		System.out.println(nVals.length+"\t"+index);
//-		System.out.println((float)index / (float)nVals.length);
		float pee = 1-((float)index / (float)nVals.length);
/*
 * 		HACKKKY AS SHIT!!! - 
 * 
 * 		-- in some cases of very low p, index creeps to length + 1
 * 		-- when this happens, index / length > 1, so p < 0
 * 		-- this is obviously dodgy, so conditional to limit 0=<p<1
 * 		
 * 		-- yes, i know, it's a bodge and sucks. whaddya gonna do about it?!??
 * 
 * 		-- oh, right; increment i differently. well, it's not gonna happen, tough.
 */
		if(pee < 0){
			pee = 0;
		}
		return pee;
	}
}
