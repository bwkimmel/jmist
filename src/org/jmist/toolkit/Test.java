package org.jmist.toolkit;

import org.jmist.framework.event.*;
import org.jmist.util.*;
import org.jmist.packages.*;
import org.jmist.framework.*;
import org.jmist.toolkit.Grid3.Cell;

import java.io.IOException;
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

//		testLinearMatrix3Identity();
//		testReflect();
//		testWeakReference();
//		testObserver();
//		testTuple();
//		testGrid();
//		testRayCircleIntersection();
//		testSwap();
//		testPermutations();
//		testNRooks();
//		testPolynomial();
//		testRoots();

		testShade();

	}

	private static void testShade() {

		ILens lens = new FisheyeLens();
		IRayShader rayShader = new DirectionalTestRayShader();
		IImageShader imageShader = new CameraImageShader(lens, rayShader);
		IPixelShader pixelShader = new SimplePixelShader(imageShader);
		ImageRasterWriter rasterWriter = new ImageRasterWriter(2048, 2048);
		IJob job = new RasterJob(pixelShader, rasterWriter);
		IProgressMonitor monitor = new DialogProgressMonitor();

		job.go(monitor);

		try {
			rasterWriter.save("C:\\test.jpg", "jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 *
	 */
	private static void testRoots() {
		Polynomial[] roots = new Polynomial[4];
		Polynomial[] poly = new Polynomial[4];
		int i;
		java.util.Random rnd = new java.util.Random();

		System.out.print("All roots:");
		for (i = 0; i < roots.length; i++) {
			roots[i] = new Polynomial(-rnd.nextDouble(), 1.0);
			System.out.print(" ");
			System.out.print(-roots[i].coefficient(0));
			poly[i] = i > 0 ? poly[i - 1].times(roots[i]) : roots[i];
		}
		System.out.println();

		for (i = 0; i < poly.length; i++) {
			System.out.println(poly[i]);
			System.out.print("roots:");
			for (double root : poly[i].roots()) {
				System.out.print(" ");
				System.out.print(root);
			}
			System.out.println();
		}
	}

	/**
	 *
	 */
	private static void testPolynomial() {
		Polynomial p = new Polynomial(3, 2, 1);
		Polynomial q = new Polynomial(-4, 1, -1);

		Polynomial r = p.times(q);
		System.out.printf("(%s)(%s) = (%s)\n", p.toString(), q.toString(), r.toString());

		r = p.plus(q);
		System.out.printf("(%s) + (%s) = (%s)", p.toString(), q.toString(), r.toString());

		System.out.println("------------------------");
	}

	/**
	 *
	 */
	private static void testNRooks() {
		NRooksRandom rand = new NRooksRandom(10, 4);
		int i;
		int j;
		for (i = 0; i < 10; i++) {
			for (j = 0; j < 4; j++) {
				if (j > 0) {
					System.out.print(", ");
				}
				System.out.printf("%f", rand.next());
			}
			System.out.println();
		}

		System.out.println("------------------------");
	}

	/**
	 *
	 */
	private static void testPermutations() {
		Permutation Px = Permutation.random(8);
		Permutation Py = Permutation.random(8);

		int board[][] = new int[8][8];
		int i,j;

		for (i = 0; i < 8; i++) {
			board[Px.at(i)][Py.at(i)] = i + 1;
		}

		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (board[j][i] > 0) {
					System.out.printf("[%d]", board[j][i]);
				} else {
					System.out.print("[ ]");
				}
			}
			System.out.println();
		}

		System.out.println("------------------------");
	}

	/**
	 *
	 */
	private static void testSwap() {
		double[] arr = new double[2];

		arr[0] = 1.0;
		arr[1] = 2.0;

		System.out.println("arr=[" + arr[0] + ", " + arr[1] + "]");
		swapFirstTwoElements(arr);
		System.out.println("arr=[" + arr[0] + ", " + arr[1] + "]");

		System.out.println("------------------------");
	}

	/**
	 * @return
	 */
	private static void testRayCircleIntersection() {
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
			Point2		p0 = ray2.pointAt(J.minimum());
			Point2		p1 = ray2.pointAt(J.maximum());

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

	/**
	 *
	 */
	private static void testGrid() {
		Grid3 grid = new Grid3(new Box3(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), 4, 4, 4);
		Ray3 ray = new Ray3(new Point3(-1.0, -1.5, -1.0), new Vector3(1.4, 2.1, 1.3).unit());

		Grid3.IVisitor visitor = new Grid3.IVisitor() {

			/* (non-Javadoc)
			 * @see org.jmist.toolkit.Grid3.IVisitor#visit(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval, org.jmist.toolkit.Grid3.Cell)
			 */
			public boolean visit(Ray3 ray, Interval I, Cell cell) {

				System.out.println("I=[" + I.minimum() + ", " + I.maximum() + "]");
				System.out.println("cell=[" + cell.getX() + " " + cell.getY() + " " + cell.getZ() + "]");

				Point3		p0 = ray.pointAt(I.minimum());
				Point3		p1 = ray.pointAt(I.maximum());
				Vector3		N0 = cell.getBoundingBox().normalAt(p0);
				Vector3		N1 = cell.getBoundingBox().normalAt(p1);

				System.out.println("p0=(" + p0.x() + ", " + p0.y() + ", " + p0.z() + "); N0=(" + N0.x() + ", " + N0.y() + ", " + N0.z() + ")");
				System.out.println("p1=(" + p1.x() + ", " + p1.y() + ", " + p1.z() + "); N1=(" + N1.x() + ", " + N1.y() + ", " + N1.z() + ")");

				System.out.println("-----------------------------");

				return true;

			}

		};

		grid.intersect(ray, Interval.POSITIVE, visitor);
	}

	/**
	 *
	 */
	private static void testTuple() {
		double[] array = new double[]{ 1.0, 2.0, 3.0 };
		Tuple zzz = new Tuple(array);
		array[1] = -2.0;

		System.out.println("-------");
		System.out.println(zzz.filter(new Tuple.Filter() { public boolean apply(double value) { return value < 2.5; } }));
		System.out.println(zzz.map(new Tuple.Function() {

			public double apply(double value) {
				return value * value;
			}} ));
		System.out.println(zzz.reduce(Tuple.PRODUCT_OPERATOR));

		System.out.println(zzz.reverse());
		System.out.println(zzz.append(zzz.reverse()));
		System.out.println(zzz.append(4.0));
		System.out.println(zzz.append(new double[]{4.0, 5.0}));
		System.out.println(zzz.permute(new int[]{2,1,2,1,0}));
		System.out.println(zzz.right(2));
		System.out.println(zzz.left(2));
		System.out.println(zzz.append(zzz.reverse()).slice(2, 5));
		System.out.println(Tuple.zeros(5));
		System.out.println(Tuple.ones(7));
		System.out.println(zzz.combine(Tuple.ones(3), Tuple.SUM_OPERATOR));
				System.out.println("-------");
		System.out.print("zzz[1]=");
		System.out.println(zzz.at(1));
	}

	/**
	 *
	 */
	private static void testObserver() {
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

	/**
	 *
	 */
	private static void testReflect() {
		Vector3 I = new Vector3(1.0, 1.0, 1.0).unit();
		Vector3 R = Optics.reflect(I, Vector3.J);
	}

	/**
	 *
	 */
	private static void testWeakReference() {
		Object x = new Integer(5);
		Reference<Object> ref1 = new WeakReference<Object>(x);
		Reference<Object> ref2 = new WeakReference<Object>(x);

		System.out.println(ref1.get().equals(ref2.get()));
	}

	/**
	 *
	 */
	private static void testLinearMatrix3Identity() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(" " + LinearMatrix3.IDENTITY.at(i, j));
			}
			System.out.println();
		}
	}

	public static void swapFirstTwoElements(double[] arr) {
		double temp = arr[0];
		arr[0] = arr[1];
		arr[1] = temp;
	}

}
