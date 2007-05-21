package org.jmist.toolkit;

import java.lang.reflect.InvocationTargetException;

import org.jmist.framework.event.*;

public class Test {

	static class TestSubject {

		public void raiseEventA(int arg) {
			onEventA.raise(this, arg);
		}

		public void raiseEventB() {
			onEventB.raise(this, null);
		}

		public IEvent<Integer> onEventA() {
			return onEventA;
		}

		public IEvent<Object> onEventB() {
			return onEventB;
		}

		private Event<Integer> onEventA = new Event<Integer>();
		private Event<Object> onEventB = new Event<Object>();

	}

	static class TestObserver {

		public TestObserver(TestSubject subject) {

			this.subject = subject;

			enableEventA();
			enableEventB();

		}

		public void enableEventA() {
			subject.onEventA().subscribe(eventAHandler);
		}

		public void disableEventA() {
			subject.onEventA().unsubscribe(eventAHandler);
		}

		public void enableEventB() {
			subject.onEventB().subscribe(eventBHandler);
		}

		public void disableEventB() {
			subject.onEventB().unsubscribe(eventBHandler);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#finalize()
		 */
		@Override
		protected void finalize() throws Throwable {
			// TODO Auto-generated method stub
			try {
				disableEventA();
				disableEventB();
			} finally {
				super.finalize();
			}
		}

		public void print() {
			System.out.println("last event A arg = " + Integer.toString(lastEventAArg));
		}

		private TestSubject subject;
		private int lastEventAArg = 0;

		private IEventHandler<Object> eventBHandler = new IEventHandler<Object>() {
			public void notify(Object sender, Object args) {
				System.out.println("event B raised");
			}
		};

		private IEventHandler<Integer> eventAHandler = new IEventHandler<Integer>() {
			public void notify(Object sender, Integer arg) {
				lastEventAArg = arg;
				System.out.println("event A raised, arg = " + arg.toString());
			}
		};

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(" " + LinearMatrix3.IDENTITY.at(i, j));
			}
			System.out.println();
		}

		TestSubject subject = new TestSubject();
		TestObserver observer = new TestObserver(subject);

		subject.raiseEventA(5);
		observer.print();
		observer.disableEventA();
		subject.raiseEventA(4);
		observer.print();
		observer.enableEventA();
		subject.raiseEventA(3);
		observer.print();
		subject.raiseEventB();
		observer.disableEventB();
		subject.raiseEventB();
		observer.enableEventB();
		subject.raiseEventB();

	}

}
