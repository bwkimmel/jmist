package org.jmist.framework;

public interface IProgressMonitor {

	boolean notifyProgress(int value, int maximum);
	boolean notifyProgress(double progress);
	void notifyComplete();
	void notifyCancelled();
	void notifyStatusChanged(String key, String value);

}
