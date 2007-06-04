package org.jmist.toolkit;

import org.jmist.framework.event.*;
import org.jmist.util.*;
import org.jmist.toolkit.Grid3.Cell;

import java.lang.ref.*;

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
			System.out.println("last event A arg = " + lastEventAArg.toString());
		}

		private TestSubject subject;
		private Object lastEventAArg = new Integer(0);

		private IEventHandler eventBHandler = new IEventHandler() {
			public void notify(Object sender, Object args) {
				System.out.println("event B raised");
			}
		};

		private IEventHandler eventAHandler = new IEventHandler() {
			public void notify(Object sender, Object arg) {
				lastEventAArg = arg;
				System.out.println("event A raised, arg = " + arg.toString());
			}
		};

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(" " + LinearMatrix3.IDENTITY.at(i, j));
			}
			System.out.println();
		}

		Vector3 I = new Vector3(1.0, 1.0, 1.0).unit();
		Vector3 R = Optics.reflect(I, Vector3.J);

		Object x = new Integer(5);
		Reference<Object> ref1 = new WeakReference<Object>(x);
		Reference<Object> ref2 = new WeakReference<Object>(x);

		System.out.println(ref1.get().equals(ref2.get()));

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

		Grid3 grid = new Grid3(new Box3(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), 4, 4, 4);
		Ray3 ray = new Ray3(new Point3(-1.0, -1.5, -1.0), new Vector3(1.4, 2.1, 1.3).unit());

		Grid3.IVisitor visitor = new Grid3.IVisitor() {

			/* (non-Javadoc)
			 * @see org.jmist.toolkit.Grid3.IVisitor#visit(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval, org.jmist.toolkit.Grid3.Cell)
			 */
			public boolean visit(Ray3 ray, Interval I, Cell cell) {

				System.out.println("I=[" + I.getMinimum() + ", " + I.getMaximum() + "]");
				System.out.println("cell=[" + cell.getX() + " " + cell.getY() + " " + cell.getZ() + "]");

				Point3		p0 = ray.pointAt(I.getMinimum());
				Point3		p1 = ray.pointAt(I.getMaximum());
				Vector3		N0 = cell.getBoundingBox().normalAt(p0);
				Vector3		N1 = cell.getBoundingBox().normalAt(p1);

				System.out.println("p0=(" + p0.x() + ", " + p0.y() + ", " + p0.z() + "); N0=(" + N0.x() + ", " + N0.y() + ", " + N0.z() + ")");
				System.out.println("p1=(" + p1.x() + ", " + p1.y() + ", " + p1.z() + "); N1=(" + N1.x() + ", " + N1.y() + ", " + N1.z() + ")");

				System.out.println("-----------------------------");

				return true;

			}

		};

		grid.intersect(ray, Interval.POSITIVE, visitor);

		boolean passed = true;
		java.util.Random rnd = new java.util.Random();

		for (int i = 0; i < 1000; i++) {
			Point2		center = new Point2(rnd.nextDouble() * 100.0 - 50.0, rnd.nextDouble() * 100.0 - 50.0);
			double		radius = rnd.nextDouble() * 10.0;
			Circle		circle = new Circle(center, radius);
			Point2		origin = new Point2(rnd.nextDouble() * 100.0 - 50.0, rnd.nextDouble() * 100.0 - 50.0);
			double		theta = rnd.nextDouble() * 2.0 * Math.PI;
			double		r = rnd.nextDouble() * radius;
			Vector2		centerToTarget = new Vector2(r * Math.cos(theta), r * Math.sin(theta));
			Point2		target = center.plus(centerToTarget);
			Vector2		direction = target.vectorFrom(origin).unit();
			Ray2		ray2 = new Ray2(origin, direction);
			Interval	J = circle.intersect(ray2);
			Point2		p0 = ray2.pointAt(J.getMinimum());
			Point2		p1 = ray2.pointAt(J.getMaximum());

			boolean		ok0 = circle.nearBoundary(p0) || circle.contains(p0);
			boolean		ok1 = circle.nearBoundary(p1);

			if (!ok0 || !ok1) {
				System.out.println("Error, ray-circle intersection point not on boundary of circle.");
				passed = false;
				break;
			}
		}

		if (passed) {
			System.out.println("ray-circle intersection test passed.");
		}

	}

}
