import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.Executors;

import javax.imageio.spi.IIORegistry;
import javax.security.auth.login.LoginException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.JobExecutionException;
import ca.eandb.jdcp.job.ParallelizableJob;
import ca.eandb.jdcp.job.ParallelizableJobRunner;
import ca.eandb.jdcp.job.TaskRandomizedJob;
import ca.eandb.jdcp.remote.ProtocolVersionException;
import ca.eandb.jdcp.remote.TaskService;
import ca.eandb.jdcp.server.TemporaryJobServer;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.accel.BoundingIntervalHierarchy;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.rgb.RGBColorModel;
import ca.eandb.jmist.framework.display.CompositeDisplay;
import ca.eandb.jmist.framework.display.ImageFileDisplay;
import ca.eandb.jmist.framework.display.JComponentDisplay;
import ca.eandb.jmist.framework.display.MatlabFileDisplay;
import ca.eandb.jmist.framework.geometry.primitive.RectangleGeometry;
import ca.eandb.jmist.framework.job.RasterJob;
import ca.eandb.jmist.framework.job.bidi.BidiPathStrategy;
import ca.eandb.jmist.framework.job.bidi.MeasurementContributionMeasure;
import ca.eandb.jmist.framework.job.bidi.MultipleImportanceSamplingStrategy;
import ca.eandb.jmist.framework.job.bidi.PathMeasure;
import ca.eandb.jmist.framework.job.mlt.BidirectionalPathMutator;
import ca.eandb.jmist.framework.job.mlt.PathMutator;
import ca.eandb.jmist.framework.lens.PinholeLens;
import ca.eandb.jmist.framework.lens.TransformableLens;
import ca.eandb.jmist.framework.loader.j3d.J3DSceneBuilder;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.modifier.ShaderModifier;
import ca.eandb.jmist.framework.random.NRooksRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.framework.scene.AbstractScene;
import ca.eandb.jmist.framework.scene.BranchSceneElement;
import ca.eandb.jmist.framework.scene.MaterialSceneElement;
import ca.eandb.jmist.framework.scene.ModifierSceneElement;
import ca.eandb.jmist.framework.shader.EmissionShader;
import ca.eandb.jmist.framework.shader.PathTracingShader;
import ca.eandb.jmist.framework.shader.StandardCompositeShader;
import ca.eandb.jmist.framework.shader.image.CameraImageShader;
import ca.eandb.jmist.framework.shader.pixel.AveragingPixelShader;
import ca.eandb.jmist.framework.shader.pixel.RandomPixelShader;
import ca.eandb.jmist.framework.shader.ray.SceneRayShader;
import ca.eandb.jmist.framework.shader.ray.UniformRayShader;
import ca.eandb.jmist.framework.tone.ToneMapperFactory;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.matlab.MatlabImageWriterSpi;
import ca.eandb.util.concurrent.BackgroundThreadFactory;
import ca.eandb.util.progress.ProgressPanel;

import com.sun.j3d.loaders.objectfile.ObjectFile;

/**
 * 
 */

/**
 * @author Brad
 *
 */
public final class RenderJ3DTest {
	
	private static final boolean LOCAL = false;
	
	private static final String FILENAME = "atria.dat";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			render();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void render() throws IOException {
		
		
		//final ColorModel cm = new PolychromeColorModel(ArrayUtil.range(380e-9, 780e-9, 30));
		//final ColorModel cm = XYZColorModel.getInstance();
		//final ColorModel cm = new MultiXYZColorModel(15, 25, 10);
		//final ColorModel cm = LuminanceColorModel.getInstance();
		final ColorModel cm = RGBColorModel.getInstance();
		//final ColorModel cm = new MonochromeColorModel(550e-9);

		String fn = "C:\\Users\\Brad\\Downloads\\DS02_obj\\DS02_obj.obj";
		ObjectFile loader = new ObjectFile();
		com.sun.j3d.loaders.Scene j3d;
		try {
			j3d = loader.load(fn);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		J3DSceneBuilder builder = new J3DSceneBuilder();
		final SceneElement rootElement = new MaterialSceneElement(
				new LambertianMaterial(cm.getGray(0.5)),
				builder.createScene(j3d));
		final SceneElement lightGeom = new MaterialSceneElement(
				new LambertianMaterial(null, cm.getGray(3e0)),
				new RectangleGeometry(new Point3(0, 0, 108), Basis3.fromWU(Vector3.NEGATIVE_K, Vector3.I), 3300, 2592, false));
		final Box3 bound = rootElement.boundingBox();
		final SceneElement floorGeom = new MaterialSceneElement(
				new LambertianMaterial(cm.getGray(0.5)),
				new RectangleGeometry(new Point3(0, 0, bound.minimumZ()), Basis3.fromWU(Vector3.K, Vector3.I), 3300, 2592, false));
		
		Scene scene = new AbstractScene() {
			private static final long serialVersionUID = 7591622149058453810L;

			private final SceneElement root = new BranchSceneElement()
				.addChild(new BoundingIntervalHierarchy(rootElement))
				.addChild(floorGeom)
				.addChild(lightGeom)
				;

			private final Lens lens;
			{
				TransformableLens tlens = new TransformableLens(PinholeLens.fromHfovAndAspect(Math.toRadians(54.43), 640.0/480.0));
				
				Point3 c = bound.center();
				Point3 corner = bound.corner(7);
				Point3 eye = corner.plus(c.vectorTo(corner));
				System.out.printf("Eye = (%f %f %f)", eye.x(), eye.y(), eye.z());
				System.out.println();
				tlens.transform(AffineMatrix3.lookAtMatrix(eye, c, Vector3.K));
				lens = tlens;
			}
			@Override
			public Lens getLens() {
				return lens;
			}

//			private final Light light = new PointLight(new Point3(1000, 1000, 1000), cm.getGray(2e8), false);
			private final Light light = lightGeom.createLight();
			
			//private final Light light = root.createLight();
			/*private final Light light = new SimpleCompositeLight()
					.addChild(new PointLight(new Point3(4,1,6), cm.getGray(1e2), true))
					.addChild(new PointLight(new Point3(-3.6,-4.4,6), cm.getGray(1e2), true))
					.addChild(new PointLight(new Point3(-4.8,4.9,6), cm.getGray(1e2), true))
					;*/
			@Override
			public Light getLight() {
				return light;
			}

			@Override
			public SceneElement getRoot() {
				return root;
			}
			
		};
		
		System.out.printf("Bounds [%f %f]x[%f %f]x[%f %f]", bound.minimumX(), bound.maximumX(), bound.minimumY(), bound.maximumY(), bound.minimumZ(), bound.maximumZ());
		System.out.println();
		
//
//		scene = new SceneDecorator(new BoxFurnaceScene(cm.getGray(0.5), cm.getGray(0.5))) {
//			private static final long serialVersionUID = -7301976864258778588L;
//			final TransformableLens lens = new TransformableLens(new PinholeLens(2, 1.5));
//			{
//				lens.transform(AffineMatrix3.lookAtMatrix(Point3.ORIGIN, new Point3(1,1,1), Vector3.J));
////				lens.translate(new Vector3(0,0,0.99));
////				lens.rotateX(Math.PI / 4.0);
//			}
//			public Lens getLens() {
//				return lens;
//			}
//			SceneElement root = null;
//			public SceneElement getRoot() {
//				synchronized (this) {
//					if (root == null) {
//						root = new BranchSceneElement()
//								.addChild(super.getRoot())
//								.addChild(new MaterialSceneElement(new LambertianMaterial(cm.getGray(0.5), cm.getGray(0.5)), new BoxGeometry(new Box3(0.45,0.45,0.45,0.55,0.55,0.55))));
//					}
//				}
//				return root;
//			}
//			
//			Light light = null;
//			public Light getLight() {
//				synchronized (this) {
//					if (light == null) {
//						light = getRoot().createLight();
//					}
//				}
//				return light;
//			}
//		};

		//Shader shader = new EdgeShader(cm.getWhite().times(255), cm.getGray(0.5).times(255), 0.5);
		int N = LOCAL ? 10 : 1000;
		//Random random = new ThreadLocalRandom(new NRooksRandom(N, 2));
		Random random = new ThreadLocalRandom(new NRooksRandom(N, 2));
		Shader shader = new StandardCompositeShader()
//				.addShader(new DistanceShader(2.0))
//				.addShader(new DirectLightingShader())
//				.addShader(new DirectEmissionShader())
				.addShader(new EmissionShader())
				.addShader(new PathTracingShader())
				;
		SceneElement root = new ModifierSceneElement(new ShaderModifier(shader), scene.getRoot());
		RayShader background = new UniformRayShader(cm.getBlack());//cm.fromRGB(128, 128, 255));
		RayShader rayShader = new SceneRayShader(root, scene.getLight(), background);
		Lens lens = scene.getLens();
		ImageShader imageShader = new CameraImageShader(lens, rayShader);
//		PixelShader pixelShader = new SimplePixelShader(imageShader, cm);
		PixelShader pixelShader = new AveragingPixelShader(N, new RandomPixelShader(random, imageShader, cm));
//		SampleModel sm = new PixelInterleavedSampleModel(DataBuffer.TYPE_DOUBLE, 512, 512, cm.getNumChannels(), 512*cm.getNumChannels(), ArrayUtil.range(0, cm.getNumChannels() - 1));
//		java.awt.image.ColorModel colmodel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), false, false, Transparency.OPAQUE, DataBuffer.TYPE_DOUBLE);
//		ByteBuffer buf = ByteBuffer.wrap(new byte[256 * 256 * cm.getNumChannels() * 8]);
//		//ch.close();
//		DoubleBuffer dbuf = buf.asDoubleBuffer();
	//	DataBuffer db = new DoubleDataBufferAdapter(dbuf);
	//	WritableRaster raster = Raster.createWritableRaster(sm, db, null);

		//BufferedImage image = new BufferedImage(cm, raster, false, null);
		//BufferedImage image = new BufferedImage(500, 500, BufferedImage.);
		IIORegistry.getDefaultInstance().registerServiceProvider(new MatlabImageWriterSpi());
//		Display display = new MatlabFileDisplay();
		CIExyY white = new CIExyY(1.0 / 3.0, 1.0 / 3.0, 1.0);
//		final ToneMapper toneMapper = new LinearToneMapper(white.toXYZ());
//		ToneMapper toneMapper = new ToneMapper() {
//			private static final long serialVersionUID = -3954016280215875249L;
//			final CIEXYZ high = new RGB(1,0,0).toXYZ();
//			final CIEXYZ low = new RGB(0,0,1).toXYZ();
//			final double max = 1.1;
//			final double min = 0.9;
//			public CIEXYZ apply(CIEXYZ hdr) {
//				if (hdr.Y() < min) {
//					return low;
//				} else if (hdr.Y() > max) {
//					return high;
//				} else {
//					return hdr.divide(max);
//				}
//			}
//		};

//		ToneMapperFactory toneMapperFactory = new ToneMapperFactory() {
//			private static final long serialVersionUID = 7298332093104882019L;
//			public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
//				double Yavg = 0.0;
//				int n = 0;
//				for (CIEXYZ sample : samples) {
//					if (sample != null) {
//						Yavg += sample.Y();
//						n++;
//					}
//				}
//				System.out.println(Yavg / (double) n);
//				return toneMapper;
//			}
//		};
//		ToneMapperFactory toneMapperFactory = new ConstantToneMapperFactory(new ToneMapper() {
//			private static final long serialVersionUID = 4624533037429987662L;
//			public CIEXYZ apply(CIEXYZ hdr) {
//				return hdr.divide(2.0);
//			}
//		});

//		ToneMapperFactory toneMapperFactory = new LinearToneMapperFactory();
//		ToneMapperFactory toneMapperFactory = new ReinhardToneMapperFactory();
		ToneMapperFactory toneMapperFactory = ToneMapperFactory.IDENTITY_FACTORY;
		
		

		String fileName = "J3DTest.png";
		Display componentDisplay = LOCAL || true ? new JComponentDisplay(toneMapperFactory) : null;
		Display display = LOCAL || true
				? new CompositeDisplay().addDisplay(componentDisplay).addDisplay(new MatlabFileDisplay()).addDisplay(new ImageFileDisplay(fileName.toString(), toneMapperFactory))
				: new CompositeDisplay().addDisplay(new MatlabFileDisplay()).addDisplay(new ImageFileDisplay(fileName.toString(), toneMapperFactory));

		// LightTracingJob(Scene scene, SampleModel imageSampleModel, java.awt.image.ColorModel imageColorModel, String formatName, ColorModel colorModel, Random random, int photons, int tasks) {
		int threads = Runtime.getRuntime().availableProcessors();
		//ParallelizableJob job = new LightTracerJob(scene, display, 512, 512, cm, Random.DEFAULT, 10000000, 100, true);
		BidiPathStrategy strategy = MultipleImportanceSamplingStrategy.usePowerHeuristic(10, 10);
		//BidiPathStrategy strategy = MultipleImportanceSamplingStrategy.useBalanceHeuristic(10, 10);
		//BidiPathStrategy strategy = new SingleContributionStrategy(1, 2);
//		BidiPathStrategy strategy = new PathTracingStrategy(2);
		//BidiPathStrategy strategy = new PurePathTracingStrategy(20);
		//BidiPathStrategy strategy = new LightTracingStrategy(10);
		//BidiPathStrategy strategy = new UniformWeightedStrategy(10, 10);

		PathMeasure measure = MeasurementContributionMeasure.getInstance();

		PathMutator mutator = new BidirectionalPathMutator();
//		ParallelizableJob job = new BidiPathTracerJob(scene, display, 256, 256, cm, Random.DEFAULT, strategy, measure, 1000, 1, 1000, true);

		int width = 1192;
		int height = 884;
		
		int cols = width / 16;
		int rows = height / 16;

//		ParallelizableJob job = new PoorMansMetropolisLightTransportJob(scene, display, width, height, cm, Random.DEFAULT, strategy, measure, width*height*mpp, 1000, 100, true);
//		ParallelizableJob job = new BidiPathTracerJob(scene, display, width, height, cm, Random.DEFAULT, strategy, measure, mpp, 1, 100, true);
		ParallelizableJob job = new RasterJob(cm, pixelShader, display, width, height, cols, rows);

		job = new TaskRandomizedJob(job);
//		public BidiPathTracerJob(Scene scene, Display display, int width,
//				int height, ColorModel colorModel, Random random,
//				int eyePathsPerPixel, int lightPathsPerEyePath, int tasks,
//				boolean displayPartialResults) {

		System.out.printf("Scene has %d primitives.", rootElement.getNumPrimitives());
		System.out.println();

		if (!LOCAL) {
			UUID id;
			try {
//				BasicConfigurator.configure();
				JFrame displayFrame = new JFrame();
				JScrollPane scroll = new JScrollPane((JComponent) componentDisplay);
				displayFrame.add(scroll, BorderLayout.CENTER);
				displayFrame.pack();
				displayFrame.setSize(400, 300);
				displayFrame.setVisible(true);

				ProgressPanel panel = new ProgressPanel();
				JFrame frame = new JFrame();
				frame.add(panel);
				frame.pack();
				frame.setVisible(true);
				
				TemporaryJobServer server = new TemporaryJobServer(panel);
				server.submitJob(job, "Path Tracing Atria");

				TaskService stub = (TaskService) UnicastRemoteObject.exportObject(server, JdcpUtil.DEFAULT_PORT+1);

				JdcpUtil.registerTaskService("temp", stub, "kirk", "admin", "admin");
				//JdcpUtil.registerTaskService("temp", stub, "localhost", "admin", "admin");
				while (!server.isComplete()) {
					try {
						server.waitForCompletion();
					} catch (InterruptedException e) {}
				}
				System.out.println("DONE");

								
//				String title = fileName.toString();
//				
//				Serialized<ParallelizableJob> x = new Serialized<ParallelizableJob>(job);
//				
//				ObjectOutputStream ar = new ObjectOutputStream(new FileOutputStream(FILENAME));
//				ar.writeObject(title);
//				ar.writeObject(x);
//				ar.flush();
//				ar.close();
//				System.exit(0);
//				
//				id = JdcpUtil.submitJob(job, title, "kirk", "admin", "admin");
//				System.out.println(id);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LoginException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolVersionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			JFrame displayFrame = new JFrame();
			JScrollPane scroll = new JScrollPane((JComponent) componentDisplay);
			displayFrame.add(scroll, BorderLayout.CENTER);
			displayFrame.pack();
			displayFrame.setSize(400, 300);
			displayFrame.setVisible(true);

			ProgressPanel panel = new ProgressPanel();
			JFrame frame = new JFrame();
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);

			File base = new File("C:\\Users\\Brad\\My Documents\\jmist");
			UUID id = UUID.randomUUID();
			File dir = new File(base, id.toString());

			Runnable runner = new ParallelizableJobRunner(job, dir, Executors.newFixedThreadPool(threads, new BackgroundThreadFactory()), threads, panel, panel.createProgressMonitor("Rendering Cornell Box"));//Runtime.getRuntime().availableProcessors());
			runner.run();
			//job.go(monitor);
	//
	//		File zipFile = new File(base, id.toString() + ".zip");
	//		try {
	//			FileUtil.zip(zipFile, dir);
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}

			frame.setVisible(false);
			frame.dispose();
		}

	}
	
}
