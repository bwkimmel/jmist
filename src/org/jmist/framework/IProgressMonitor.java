package org.jmist.framework;

public interface IProgressMonitor {

	boolean notifyProgress(int value, int maximum);
	boolean notifyProgress(double progress);
	boolean notifyIndeterminantProgress();
	void notifyComplete();
	void notifyCancelled();
	void notifyStatusChanged(String status);

}
