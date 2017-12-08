/*
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 */

package com.maxst.ar.sample.util;

import android.graphics.Point;
import android.opengl.Matrix;

public class BoundingBox {

	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private float minZ;
	private float maxZ;
	private Vector3f [] points;

	public BoundingBox() {
		points = new Vector3f[8];
		for (int i = 0; i < 8; i++) {
			points[i] = new Vector3f();
		}
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

	public boolean isObjectTouched(float touchX, float touchY, float [] mvpMatrix) {
		Vector3f [] projectedPoints = new Vector3f[8];
		for (int i = 0; i < 8; i++) {
			projectedPoints[i] = new Vector3f();
		}

		float [] inputVector = new float[4];
		float [] outputVector = new float[4];
		for (int i = 0; i < 8; i++) {
			inputVector[0] = points[i].x;
			inputVector[1] = points[i].y;
			inputVector[2] = points[i].z;
			inputVector[3] = 1;

			Matrix.multiplyMV(outputVector, 0, mvpMatrix, 0, inputVector, 0);
			projectedPoints[i].x = outputVector[0] / outputVector[3];
			projectedPoints[i].y = outputVector[1] / outputVector[3];
			projectedPoints[i].z = outputVector[2] / outputVector[3];
		}

		float tempMinX = 0;
		float tempMaxX = 0;
		float tempMinY = 0;
		float tempMaxY = 0;
		float tempMinZ = 0;
		float tempMaxZ = 0;

		for (Vector3f point : projectedPoints) {
			if (point.x < tempMinX) {
				tempMinX = point.x;
			}

			if (point.x > tempMaxX) {
				tempMaxX = point.x;
			}

			if (point.y < tempMinY) {
				tempMinY = point.y;
			}

			if (point.y > tempMaxY) {
				tempMaxY = point.y;
			}

			if (point.z < tempMinZ) {
				tempMinZ = point.z;
			}

			if (point.z > tempMaxZ) {
				tempMaxZ = point.z;
			}
		}

		if (touchX < tempMinX || touchX > tempMaxX) {
			return false;
		}

		if (touchY < tempMinY || touchY > tempMaxY) {
			return false;
		}

		return true;
	}

	public void createBox() {
		points[0].x = minX;
		points[0].y = minY;
		points[0].z = minZ;

		points[1].x = maxX;
		points[1].y = minY;
		points[1].z = minZ;

		points[2].x = maxX;
		points[2].y = maxY;
		points[2].z = minZ;

		points[3].x = minX;
		points[3].y = maxY;
		points[3].z = minZ;

		points[4].x = minX;
		points[4].y = minY;
		points[4].z = maxZ;

		points[5].x = maxX;
		points[5].y = minY;
		points[5].z = maxZ;

		points[6].x = maxX;
		points[6].y = maxY;
		points[6].z = maxZ;

		points[7].x = minX;
		points[7].y = maxY;
		points[7].z = maxZ;
	}
}
