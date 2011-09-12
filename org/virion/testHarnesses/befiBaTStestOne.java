package org.virion.testHarnesses;

import java.util.ArrayList;
import org.virion.BURPer.tree;

public class befiBaTStestOne {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*
 * 		balanced biurificating tree
 */
		System.out.println("Balanced biurificating tree:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		String treefile = "(((1:1.5,2:1.5):1,(3:2,4:2):1):1,((5:1.5,6:1.5):1,(7:1,8:2):1.1):1.2)";
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

		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float AI = (float) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			System.out.println(t.topnode.getHeight());
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();

/*
 * 		unbalanced tree with polytomy
 */

		treefile = "(((1:0.1,2:0.2,3:0.3):0.12,4:0.33):0.14,(((5:0.1,6:0.09):0.08,7:0.22):0.11,8:0.13):0.15)";
		System.out.println("\nUnbalanced tree with polytomy:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float AI = (float) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			if(i==1){t.getTreeDetails();}
//-			System.out.println(t.topnode.getHeight());			
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();

		treefile = "(1:0.1,(2:0.2,3:0.3,(4:0.4,(5:0.5,(6:0.6,7:0.7,8:0.8):1):1):1):1)";
		System.out.println("\nUnbalanced tree with polytomies:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float AI = (float) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			System.out.println(t.topnode.getHeight());			
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();

		treefile = "(1:0.1,(2:0.2,(3:0.3,(4:0.4,(5:0.5,(6:0.6,(7:0.7,8:0.8):1):1):1):1):1):1)";
		System.out.println("\nUnbalanced biurificating tree:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float AI = (float) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			System.out.println(t.topnode.getHeight());			
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();

		treefile = "(1:0.1,2:0.2,3:0.3,4:0.4,5:0.5,6:0.6,7:0.7,8:0.8)";
		System.out.println("\nStar tree:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			float AI = (float) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			System.out.println(t.topnode.getHeight());			
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();
/*
 * 		tree 2 represented as a soft polytomy
 */
		treefile = "((((1:0.1,2:0.2):0,3:0.3):0.12,4:0.33):0.14,(((5:0.1,6:0.09):0.08,7:0.22):0.11,8:0.13):0.15)";
		System.out.println("\nUnbalanced tree with polytomy:\nAI\tPS\tM1\tM2\tPD\tNT\tNR\tNT_n\tNR_n\tUF");
		for (int i = 0; i < 4; i++) {
			tree t = new tree(treefile, taxaNames, testTaxa.get(i),availableStates);
			double AI = (double) t.getAI();
			float PS = (float) t.getMigrationEvents();
			float M1 = t.getBiggestExclusiveNodeOf(0);
			float M2 = t.getBiggestExclusiveNodeOf(1);
			float PD = t.getPD();
			float NT = t.getNTI();
			float NR = t.getNRI();
			float NT_n = t.getNodalNTI();
			float NR_n = t.getNodalNRI();
			float UF = (float) t.getUniFrac();
			System.out.println(AI + "\t" + PS + "\t" + M1 + "\t" + M2 + "\t" + PD + "\t" + NT + "\t"+ NR + "\t" + NT_n + "\t" + NR_n + "\t" + UF);
//-			if(i==1){t.getTreeDetails();}
//-			System.out.println(t.topnode.getHeight());			
		}
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_nodal_distances().showMatrix();
//-		new tree(treefile,taxaNames, testTaxa.get(0), availableStates).getTopnode_distances().showMatrix();

	}
}
