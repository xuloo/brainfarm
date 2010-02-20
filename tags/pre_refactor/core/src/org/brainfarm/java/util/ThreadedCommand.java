package org.brainfarm.java.util;

/**
 * <p>Abstract class for creating threaded operations.</p>
 * 
 * <p>If an operation needs to be asynchronous it's logic can be put in a child class's <code>execute()</code> method.</p>
 * 
 * @author <a href="mailto:trevor@infrared5.com">Trevor Burton</a>
 *
 */
public abstract class ThreadedCommand implements Runnable {

	/**
	 * Empty Constructor.
	 */
	public ThreadedCommand() {
		
	}
	
	/**
	 * <code>Runnable</code> implementation. 
	 * Creates a new thread and calls <code>this.execute()</code> within that thread.
	 */
	protected abstract void execute();

	/**
	 * Implement this method in child classes. Code in this method will be executed in its own thread.
	 */
	public void run() {
		new Thread() {
			public void run() {
				execute();
			}
		}.run();
	}
}
