/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IJob;
import org.jmist.framework.IPixelShader;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.IRasterWriter;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * @author bkimmel
 *
 */
public final class RasterJob implements IJob {

	public RasterJob(IPixelShader shader, IRasterWriter writer) {
		this.shader = shader;
		this.writer = writer;
	}

	public void go(IProgressMonitor monitor) {

		int		px			= -1;		// previous x
		int		py			= -1;		// previous y
		double	w			= writer.width();
		double	h			= writer.height();
		double	x0			= 0.0;
		double	y0			= 0.0;
		double	x1			= 0.0;
		double	y1			= 0.0;
		int		x, y;
		Box2	bounds;
		Pixel	pixel		= this.shader.createPixel();

		if (!monitor.notifyProgress(writer.y(), writer.height())) {
			monitor.notifyCancelled();
			return;
		}

		while (!writer.done()) {

			if ((x = writer.x()) != px) {
				px = x;
				x0 = (double) x / w;
				x1 = (double) (x + 1) / w;
			}

			if ((y = writer.y()) != py) {
				py = y;
				y0 = (double) y / h;
				y1 = (double) (y + 1) / h;

				if (!monitor.notifyProgress(y, writer.height())) {
					monitor.notifyCancelled();
					return;
				}
			}

			bounds = new Box2(x0, y0, x1, y1);

			this.shader.shadePixel(bounds, pixel);
			this.writer.writePixel(pixel);

		}

		monitor.notifyProgress(writer.height(), writer.height());
		monitor.notifyComplete();

	}

	private final IPixelShader shader;
	private final IRasterWriter writer;

}
