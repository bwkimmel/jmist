/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Job;
import org.jmist.framework.PixelShader;
import org.jmist.framework.RasterWriter;
import org.jmist.framework.reporting.ProgressMonitor;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * @author bkimmel
 *
 */
public final class RasterJob implements Job {

	public RasterJob(PixelShader shader, RasterWriter writer) {
		this.shader = shader;
		this.writer = writer;
	}

	public boolean go(ProgressMonitor monitor) {

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
			return false;
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
					return false;
				}
			}

			bounds = new Box2(x0, y0, x1, y1);

			this.shader.shadePixel(bounds, pixel);
			this.writer.writePixel(pixel);

		}

		monitor.notifyProgress(writer.height(), writer.height());
		monitor.notifyComplete();
		return true;

	}

	private final PixelShader shader;
	private final RasterWriter writer;

}
