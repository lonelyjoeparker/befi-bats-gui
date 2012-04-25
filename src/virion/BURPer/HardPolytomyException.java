package org.virion.BURPer;

public class HardPolytomyException extends Exception {

	public HardPolytomyException() {
		// TODO Auto-generated constructor stub
		System.out.println("\tThis node represents a hard polytomy.\n\tThese are not supported by BaTS.");
	}

	public HardPolytomyException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		System.out.println("\tThis node represents a hard polytomy.\n\tThese are not supported by BaTS.");
	}

	public HardPolytomyException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		System.out.println("\tThis node represents a hard polytomy.\n\tThese are not supported by BaTS.");
	}

	public HardPolytomyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		System.out.println("\tThis node represents a hard polytomy.\n\tThese are not supported by BaTS.");
	}

}
