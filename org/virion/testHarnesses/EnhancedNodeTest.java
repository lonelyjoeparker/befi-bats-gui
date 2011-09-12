package org.virion.testHarnesses;

import java.util.ArrayList;

import org.virion.BURPer.EnhancedTree;

public class EnhancedNodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] availableStates = {"b","w"};
		String[] states_A = {"b","b","b","b","w","w","w","w"};
		String[] states_B = {"b","b","w","w","b","b","w","w"};
		String[] states_C = {"b","w","b","w","b","w","b","w"};
		String[] states_D = {"b","w","b","w","b","b","w","w"};
		int max_states = 2;
		ArrayList<String> taxaNames = new ArrayList<String>();
		ArrayList<String> taxaStates_A = new ArrayList<String>();
		ArrayList<String> taxaStates_B = new ArrayList<String>();
		ArrayList<String> taxaStates_C = new ArrayList<String>();
		ArrayList<String> taxaStates_D = new ArrayList<String>();
		String treefile;
		
		for(int i=1;i<9;i++){
			taxaNames.add(new String().valueOf(i));
			taxaStates_A.add(states_A[i-1]);
			taxaStates_B.add(states_B[i-1]);
			taxaStates_C.add(states_C[i-1]);
			taxaStates_D.add(states_D[i-1]);
		}
		ArrayList<ArrayList> testTaxa = new ArrayList<ArrayList>();
		testTaxa.add(taxaStates_A);
		testTaxa.add(taxaStates_B);
		testTaxa.add(taxaStates_C);
		testTaxa.add(taxaStates_D);

		System.out.println("Balanced biurificating tree:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		treefile = "(((1:1.5,2:1.5):1,(3:2,4:2):1):1,((5:1.5,6:1.5):1,(7:1,8:2):1.1):1.2)";
		float[][] normalCallsRes = new float[4][8];
		float[][] superCallsRes = new float[4][8];
		for (int i = 0; i < 4; i++) {
			EnhancedTree t = new EnhancedTree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float[] normalCallsData = new float[10];
			float size;
			float internalSize;
			float AI;
			float PS;
			float M1;
			float M2;
			float PD;
			float NT;
			float NR;
			float NT_n;
			float NR_n;
			float UF;
			try {
				size = t.getSize();
				internalSize = t.getInternalSize();
				AI = (float) t.getAI();
				PS = (float) t.getMigrationEvents();
				M1 = t.getBiggestExclusiveNodeOf(0);
				M2 = t.getBiggestExclusiveNodeOf(1);
				PD = t.getPD();
				NT = t.getNTI();
				NR = t.getNRI();
				NT_n = t.getNodalNTI();
				NR_n = t.getNodalNRI();
				UF = (float) t.getUniFrac();
				normalCallsData[0] = t.getSize();
				normalCallsData[1] = t.getInternalSize();
				normalCallsData[2] = (float) t.getAI();
				normalCallsData[3] = (float) t.getMigrationEvents();
				normalCallsData[4] = (float) t.getUniFrac();
				normalCallsData[5] = t.getNTI();
				normalCallsData[6] = t.getNRI();
				normalCallsData[7] = t.getPD();
				normalCallsData[8] = t.getBiggestExclusiveNodeOf(0);
				normalCallsData[9]  = t.getBiggestExclusiveNodeOf(1);
				System.out.println("Tree "+i+":\t"+AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + UF + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + PD);
				normalCallsRes[i] = normalCallsData;
				
				superCallsRes[i] = t.getAllStats();

			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("\tTree not constructed (hard polytomy present?)");
			}
//-			if(i==1){t.getTreeDetails();}
//-			System.out.println(t.topnode.getHeight());	
		}

		System.out.println("\nStat call function differences:\nsi\tiS\tAI\tPS\tUF\tNT\tNR\tPD\tM1\tM2");
		for(int i=0;i<4;i++){
			for(int j=0;j<10;j++){
				float diff = (float)superCallsRes[i][j] - (float)normalCallsRes[i][j];
				System.out.print(diff+"\t");
			}
			System.out.println();
		}
	}
}
