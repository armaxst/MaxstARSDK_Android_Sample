package com.maxst.ar.sample.util;

/**
 * Created by Jack on 2017-12-08.
 */

public class BoundingBox {

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private float minZ;
	private float maxZ;
	private float [] box;

	public BoundingBox() {
		box = new float[24];
	}

	public void setPoint(float x, float y, float z) {
		if (x < minX) {
			minX = x;
		}

		if (x > maxX) {
			maxX = x;
		}

		if (y < minY) {
			minY = y;
		}

		if (y > maxY) {
			maxY = y;
		}

		if (z < minZ) {
			minZ = z;
		}

		if (z > maxZ) {
			maxZ = z;
		}
	}

	public float [] getPoints() {
		box[0] = minX;
		box[1] = minY;
		box[2] = minZ;

		box[3] = maxX;
		box[4] = minY;
		box[5] = minZ;

		box[6] = maxX;
		box[7] = maxY;
		box[8] = minZ;

		box[9] = minX;
		box[10] = maxY;
		box[11] = minZ;

		box[12] = minX;
		box[13] = minY;
		box[14] = maxZ;

		box[15] = maxX;
		box[16] = minY;
		box[17] = maxZ;

		box[18] = maxX;
		box[19] = maxY;
		box[20] = maxZ;

		box[21] = minX;
		box[22] = maxY;
		box[23] = maxZ;

		return box;
	}
}
