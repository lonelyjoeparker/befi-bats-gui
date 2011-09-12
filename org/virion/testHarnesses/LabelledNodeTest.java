package org.virion.testHarnesses;

import java.util.ArrayList;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


import org.virion.BURPer.LabelledTree;

public class LabelledNodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Init class for LabelledNode 
		// Most utility methods cloned from EnhancedNodeTest
		// 28/9/09

		String[] availableStates = {"b","w"};
		String[] states_A = {"b","b","b","b","w","w","w","w"};
		int max_states = 2;
		ArrayList<String> taxaNames = new ArrayList<String>();
		ArrayList<String> taxaStates_A = new ArrayList<String>();
		String treefile;

		for(int i=1;i<9;i++){
			taxaNames.add(new String().valueOf(i));
			taxaStates_A.add(states_A[i-1]);
		}

//		-		System.out.println("Balanced biurificating tree:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
//		treefile = "(((1:1.5,2:1.5):1,(3:2,4:2):1):1,((5:1.5,6:1.5):1,(7:1,8:2):1.1):1.2)";

		treefile = args[0];

		LabelledNodeTest lnt = new LabelledNodeTest();
		System.out.println("reading "+treefile);
		String[] trees = lnt.readTreeFile(treefile);
		System.out.println("read: "+trees.length+" tree(s), reconstructing...");
		int count = 1;
		for(String aTree:trees){			
			System.out.println("\n\nREPORTING TREE: "+count+"\n\n");
			LabelledTree t = new LabelledTree(aTree, taxaNames, taxaStates_A,availableStates);
			count ++;
		}

//		float[] normalCallsData = new float[10];
//		float size;
//		float internalSize;
//		float AI;
//		float PS;
//		float M1;
//		float M2;
//		float PD;
//		float NT;
//		float NR;
//		float NT_n;
//		float NR_n;
//		float UF;
//		try {
//			size = t.getSize();
//			internalSize = t.getInternalSize();
//			AI = (float) t.getAI();
//			PS = (float) t.getMigrationEvents();
//			M1 = t.getBiggestExclusiveNodeOf(0);
//			M2 = t.getBiggestExclusiveNodeOf(1);
//			PD = t.getPD();
//			NT = t.getNTI();
//			NR = t.getNRI();
//			NT_n = t.getNodalNTI();
//			NR_n = t.getNodalNRI();
//			UF = (float) t.getUniFrac();
//			normalCallsData[0] = t.getSize();
//			normalCallsData[1] = t.getInternalSize();
//			normalCallsData[2] = (float) t.getAI();
//			normalCallsData[3] = (float) t.getMigrationEvents();
//			normalCallsData[4] = (float) t.getUniFrac();
//			normalCallsData[5] = t.getNTI();
//			normalCallsData[6] = t.getNRI();
//			normalCallsData[7] = t.getPD();
//			normalCallsData[8] = t.getBiggestExclusiveNodeOf(0);
//			normalCallsData[9]  = t.getBiggestExclusiveNodeOf(1);
////			-			System.out.println("Tree 0:\t"+AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + UF + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + PD);
//
//		} catch (NullPointerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("\tTree not constructed (hard polytomy present?)");
//		}
//		-			if(i==1){t.getTreeDetails();}
//		-			System.out.println(t.topnode.getHeight());	
		System.out.println("Done.");
	}

	public String[] readTreeFile(String filename){
			ArrayList<String> treeFileAL = new ArrayList<String>();
			try{
				File thetreeFileAL  = new File(filename);
				FileReader fr = new FileReader(thetreeFileAL);
				BufferedReader br  = new BufferedReader(fr);
				String line = null;
				boolean inAssignBlock = false;
				boolean inTreesBlock = false;
				Pattern assign = Pattern.compile("begin states;");			
				Pattern trees = Pattern.compile("begin trees;");			
				Pattern end = Pattern.compile("End;");
//				this.taxaNames = new ArrayList<String>();
	
				while((line = br.readLine())!= null){
	
					Matcher isAssign = assign.matcher(line);
					Matcher isTrees = trees.matcher(line);
					Matcher isEnd = end.matcher(line);
	
					if(isEnd.matches()){
	//					c					// have just left a block
						inAssignBlock = false;
						inTreesBlock = false;
					}
//	
//					if(inAssignBlock){
//	//					c					// collect names & states
//						String [] result = line.split("\\ ");
//						this.taxaNames.add(result[0]);
//						this.taxaStates.add(result[1]);
//					}
	
					if(inTreesBlock){
						String [] result = line.split("\\[&R\\]\\ ");
//						for(String token:result){
//							System.out.println(token + " token of line " + line);
//						}
						 
						int penultimatechar = result[1].length() - 1;
						String atLast = result[1].substring(0,penultimatechar);
						treeFileAL.add(atLast);
					}
	
					if(isTrees.matches()){
	//					c					// in trees block
						inTreesBlock = true;
					}
	
					if(isAssign.matches()){
						// in states block
						inAssignBlock = true;
					}
	//				-				System.out.println(">>>>" + line + " " + inTreesBlock + " " + inAssignBlock);
				}
				br.close();
			} catch(Exception ex){
				System.out.println("Not read file " + filename);
				ex.printStackTrace();
			}
			String[] treeStringList = new String[treeFileAL.size()];
			for(int i=0;i<treeFileAL.size();i++){
				treeStringList[i] = treeFileAL.get(i);
			}
			return treeStringList;
		}

	}

