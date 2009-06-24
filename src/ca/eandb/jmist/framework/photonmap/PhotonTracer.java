/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.eandb.jmist.framework.photonmap;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.RandomScatterRecorder;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Tuple;

/**
 * @author brad
 *
 */
public final class PhotonTracer {

	private final Light light;

	private final RayCaster caster;

	public PhotonTracer(Light light, RayCaster caster) {
		this.light = light;
		this.caster = caster;

	}

	public PhotonMap trace(Tuple wavelengths, int maxEmittedPhotons, int maxPhotons) {
		PhotonMap map = new PhotonMap(maxPhotons);
		double[] radiance = new double[wavelengths.size()];
		RandomScatterRecorder recorder = new RandomScatterRecorder();
		int storedPhotons = 0;
		int emittedPhotons = 0;
		for (; emittedPhotons < maxEmittedPhotons && storedPhotons < maxPhotons; emittedPhotons++) {
			Ray3 ray = light.emit(wavelengths, radiance);
			while (ray != null && storedPhotons < maxPhotons) {
				Intersection x = caster.castRay(ray, Interval.POSITIVE);
				if (x != null) {
					recorder.reset();
					x.material().scatter(x, wavelengths, recorder);
					ScatteredRay sr = recorder.getScatterResult();
					if (sr != null) {
						if (sr.specularity() < 0.99) {
							map.store(x.getPosition(), ray.direction(), sr.weightAt(0));
							storedPhotons++;
						}
						ray = sr.scatteredRay();
					} else {
						map.store(x.getPosition(), ray.direction(), sr.weightAt(0));
						storedPhotons++;
						ray = null;
					}
				}
			}
		}
		map.scalePhotons(1.0 / (double) emittedPhotons);
		map.balance();
		return map;
	}

}
