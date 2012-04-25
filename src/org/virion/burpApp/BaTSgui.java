package org.virion.burpApp;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import org.virion.BURPer.GeneralizedSingleBURPer;

public class BaTSgui {

	/**
	 * @param args
	 */

	private int int1;
	private int int2;
	private JFrame frame = new JFrame("BaTS v.1.0");
	private JPanel paramsPanel = new JPanel();
	private JPanel filePanel = new JPanel();
	private JPanel runPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JTextArea reps = new JTextArea("100");
	private JTextArea maxStates = new JTextArea("4");
	private JTextArea fileText = new JTextArea("---");
	private JTextArea mainArea = new JTextArea("output");
	private JLabel label1 = new JLabel("enter number of replications for the null distribution");
	private JLabel label2 = new JLabel("enter max number of discrete states");
	private JLabel label3 = new JLabel("choose posterior trees file");
	private JLabel label4 = new JLabel("(this package can also be run, more efficiently, directly from a command-line such as MS-Dos, Terminal or any POSIX shell. See documentation for details)");
	private JButton fc = new JButton("choose");
	private JButton run = new JButton("Run");
	private JFileChooser jcf = new JFileChooser();
	private File file;
	private GeneralizedSingleBURPer burp;
		
	public BaTSgui(){
		// Default no-arg constructor
		int1 = 0;
		int2 = 0;
	}
	
	public BaTSgui(int aVar1, int aVar2){
		// Arg constructor
		int1 = aVar1;
		int2 = aVar2;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int var1;
		int var2;
		
		if(args.length > 0){
			var1 = Integer.parseInt(args[0]);
		}else{
			var1 = 0;
		}
		
		if(args.length > 1){
			var2 = Integer.parseInt(args[1]);
		}else{
			var2 = 0;
		}

		BaTSgui gui = new BaTSgui(var1, var2);
		gui.go();
	}

	public void go(){
		// Launches the gui
		System.out.println("wow. i can still program..");
		System.out.println(this.getInt1());
		System.out.println(this.getInt2());
		
		fc.addActionListener(new fileActionListener());
		run.addActionListener(new runActionListener());
		
		paramsPanel.add(label1);
		paramsPanel.add(reps);
		paramsPanel.add(label2);
		paramsPanel.add(maxStates);
		filePanel.add(label3);
		filePanel.add(fc);
		filePanel.add(fileText);
		runPanel.add(label4);
		runPanel.add(run);
		mainPanel.add(mainArea);
		frame.getContentPane().add(BorderLayout.EAST, paramsPanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.getContentPane().add(BorderLayout.WEST, filePanel);
		frame.getContentPane().add(BorderLayout.SOUTH, runPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200,200);
		frame.setVisible(true);
	}

	class fileActionListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			int retval = jcf.showOpenDialog(null);
			if(retval == JFileChooser.APPROVE_OPTION){
				file = jcf.getSelectedFile();
				fileText.setText(file.getName());
			}
		}
	}
	
	class runActionListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			mainArea.setText("running");
			burp = new GeneralizedSingleBURPer(file.getAbsolutePath(), Integer.parseInt(reps.getText()), Integer.parseInt(maxStates.getText()));
			burp.go();
		}
	}
	/**
	 * @return the int1
	 */
	public int getInt1() {
		return int1;
	}

	/**
	 * @return the int2
	 */
	public int getInt2() {
		return int2;
	}

	/**
	 * @param int1 the int1 to set
	 */
	public void setInt1(int int1) {
		this.int1 = int1;
	}

	/**
	 * @param int2 the int2 to set
	 */
	public void setInt2(int int2) {
		this.int2 = int2;
	}
}
