/* --------------------------------------------------------------------------------------
 * PaperWorld3D - building better worlds
 * --------------------------------------------------------------------------------------
 * Real-Time Multi-User Application Framework for the Flash Platform.
 * --------------------------------------------------------------------------------------
 * Copyright (C) 2008 Trevor Burton [worldofpaper@googlemail.com]
 * --------------------------------------------------------------------------------------
 * 
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA 02111-1307 USA 
 * 
 * -------------------------------------------------------------------------------------- */
package com.brainfarm.java.util.math;

public class Quaternion {
	private Matrix _matrix;

	public static double EPSILON = 0.000001;
	public static double DEGTORAD = (Math.PI / 180.0);
	public static double RADTODEG = (180.0 / Math.PI);

	/** */
	public double x;

	/** */
	public double y;

	/** */
	public double z;

	/** */
	public double w;

	/**
	 * constructor.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return
	 */
	public Quaternion(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;

		_matrix = Matrix.IDENTITY();
	}

	public Quaternion() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
		w = 1.0;

		_matrix = Matrix.IDENTITY();
	}

	/**
	 * Clone.
	 * 
	 */
	public Quaternion clone() {
		return new Quaternion(x, y, z, w);
	}

	public void clear() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
		w = 1.0;
	}

	/**
	 * Multiply.
	 * 
	 * @param a
	 * @param b
	 */
	public void calculateMultiply(Quaternion a, Quaternion b) {
		x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
		y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x;
		z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w;
		w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
	}

	/**
	 * Creates a Quaternion from a axis and a angle.
	 * 
	 * @param x
	 *            X-axis
	 * @param y
	 *            Y-axis
	 * @param z
	 *            Z-axis
	 * @param angle
	 *            angle in radians.
	 * 
	 * @return
	 */
	public void setFromAxisAngle(double x, double y, double z, double angle) {
		double sin = Math.sin(angle / 2);
		double cos = Math.cos(angle / 2);

		this.x = x * sin;
		this.y = y * sin;
		this.z = z * sin;
		this.w = cos;

		normalise();
	}

	/**
	 * Sets this Quaternion from Euler angles.
	 * 
	 * @param ax
	 *            X-angle in radians.
	 * @param ay
	 *            Y-angle in radians.
	 * @param az
	 *            Z-angle in radians.
	 */
	public void setFromEuler(double ax, double ay, double az, boolean useDegrees) {
		if (useDegrees) {
			ax *= DEGTORAD;
			ay *= DEGTORAD;
			az *= DEGTORAD;
		}

		double fSinPitch = Math.sin(ax * 0.5);
		double fCosPitch = Math.cos(ax * 0.5);
		double fSinYaw = Math.sin(ay * 0.5);
		double fCosYaw = Math.cos(ay * 0.5);
		double fSinRoll = Math.sin(az * 0.5);
		double fCosRoll = Math.cos(az * 0.5);
		double fCosPitchCosYaw = fCosPitch * fCosYaw;
		double fSinPitchSinYaw = fSinPitch * fSinYaw;

		this.x = fSinRoll * fCosPitchCosYaw - fCosRoll * fSinPitchSinYaw;
		this.y = fCosRoll * fSinPitch * fCosYaw + fSinRoll * fCosPitch
				* fSinYaw;
		this.z = fCosRoll * fCosPitch * fSinYaw - fSinRoll * fSinPitch
				* fCosYaw;
		this.w = fCosRoll * fCosPitchCosYaw + fSinRoll * fSinPitchSinYaw;
	}

	/**
	 * Modulo.
	 * 
	 * @param a
	 * @return
	 */
	public double getModulo() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	/**
	 * Conjugate.
	 * 
	 * @param a
	 * @return
	 */
	public static Quaternion conjugate(Quaternion a) {
		Quaternion q = new Quaternion();

		q.x = -a.x;
		q.y = -a.y;
		q.z = -a.z;
		q.w = a.w;

		return q;
	}

	/**
	 * Creates a Quaternion from a axis and a angle.
	 * 
	 * @param x
	 *            X-axis
	 * @param y
	 *            Y-axis
	 * @param z
	 *            Z-axis
	 * @param angle
	 *            angle in radians.
	 * 
	 * @return
	 */
	public static Quaternion createFromAxisAngle(double x, double y, double z,
			double angle) {
		Quaternion q = new Quaternion();

		q.setFromAxisAngle(x, y, z, angle);

		return q;
	}

	/**
	 * Creates a Quaternion from Euler angles.
	 * 
	 * @param ax
	 *            X-angle in radians.
	 * @param ay
	 *            Y-angle in radians.
	 * @param az
	 *            Z-angle in radians.
	 * 
	 * @return
	 */
	public static Quaternion createFromEuler(double ax, double ay, double az,
			boolean useDegrees) {
		if (useDegrees) {
			ax *= DEGTORAD;
			ay *= DEGTORAD;
			az *= DEGTORAD;
		}

		double fSinPitch = Math.sin(ax * 0.5);
		double fCosPitch = Math.cos(ax * 0.5);
		double fSinYaw = Math.sin(ay * 0.5);
		double fCosYaw = Math.cos(ay * 0.5);
		double fSinRoll = Math.sin(az * 0.5);
		double fCosRoll = Math.cos(az * 0.5);
		double fCosPitchCosYaw = fCosPitch * fCosYaw;
		double fSinPitchSinYaw = fSinPitch * fSinYaw;

		Quaternion q = new Quaternion();

		q.x = fSinRoll * fCosPitchCosYaw - fCosRoll * fSinPitchSinYaw;
		q.y = fCosRoll * fSinPitch * fCosYaw + fSinRoll * fCosPitch * fSinYaw;
		q.z = fCosRoll * fCosPitch * fSinYaw - fSinRoll * fSinPitch * fCosYaw;
		q.w = fCosRoll * fCosPitchCosYaw + fSinRoll * fSinPitchSinYaw;

		return q;
	}

	/**
	 * Creates a Quaternion from a matrix.
	 * 
	 * @param matrix
	 *            a matrix. @see org.papervision3d.core.Matrix3D
	 * 
	 * @return the created Quaternion
	 */
	public static Quaternion createFromMatrix(Matrix matrix) {
		Quaternion quat = new Quaternion();

		double s;
		double[] q = new double[4];
		int i, j, k;

		double tr = matrix.n11 + matrix.n22 + matrix.n33;

		// check the diagonal
		if (tr > 0.0) {
			s = Math.sqrt(tr + 1.0);
			quat.w = s / 2.0;
			s = 0.5 / s;

			quat.x = (matrix.n32 - matrix.n23) * s;
			quat.y = (matrix.n13 - matrix.n31) * s;
			quat.z = (matrix.n21 - matrix.n12) * s;
		} else {
			// diagonal is negative
			int[] nxt = { 1, 2, 0 };

			double[][] m = {
					{ matrix.n11, matrix.n12, matrix.n13, matrix.n14 },
					{ matrix.n21, matrix.n22, matrix.n23, matrix.n24 },
					{ matrix.n31, matrix.n32, matrix.n33, matrix.n34 } };

			i = 0;

			if (m[1][1] > m[0][0])
				i = 1;
			if (m[2][2] > m[i][i])
				i = 2;

			j = nxt[i];
			k = nxt[j];
			s = Math.sqrt((m[i][i] - (m[j][j] + m[k][k])) + 1.0);

			q[i] = s * 0.5;

			if (s != 0.0)
				s = 0.5 / s;

			q[3] = (m[k][j] - m[j][k]) * s;
			q[j] = (m[j][i] + m[i][j]) * s;
			q[k] = (m[k][i] + m[i][k]) * s;

			quat.x = q[0];
			quat.y = q[1];
			quat.z = q[2];
			quat.w = q[3];
		}
		return quat;
	}

	/**
	 * Creates a Quaternion from a orthonormal matrix.
	 * 
	 * @param m
	 *            a orthonormal matrix. @see org.papervision3d.core.Matrix3D
	 * 
	 * @return the created Quaternion
	 */
	public static Quaternion createFromOrthoMatrix(Matrix m) {
		Quaternion q = new Quaternion();

		q.w = Math.sqrt(Math.max(0, 1 + m.n11 + m.n22 + m.n33)) / 2;
		q.x = Math.sqrt(Math.max(0, 1 + m.n11 - m.n22 - m.n33)) / 2;
		q.y = Math.sqrt(Math.max(0, 1 - m.n11 + m.n22 - m.n33)) / 2;
		q.z = Math.sqrt(Math.max(0, 1 - m.n11 - m.n22 + m.n33)) / 2;

		// recover signs
		q.x = m.n32 - m.n23 < 0 ? (q.x < 0 ? q.x : -q.x) : (q.x < 0 ? -q.x
				: q.x);
		q.y = m.n13 - m.n31 < 0 ? (q.y < 0 ? q.y : -q.y) : (q.y < 0 ? -q.y
				: q.y);
		q.z = m.n21 - m.n12 < 0 ? (q.z < 0 ? q.z : -q.z) : (q.z < 0 ? -q.z
				: q.z);

		return q;
	}

	/**
	 * Dot product.
	 * 
	 * @param a
	 * @param b
	 * 
	 * @return
	 */
	public static double dot(Quaternion a, Quaternion b) {
		return (a.x * b.x) + (a.y * b.y) + (a.z * b.z) + (a.w * b.w);
	}

	/**
	 * Multiply.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Quaternion multiply(Quaternion a, Quaternion b) {
		Quaternion c = new Quaternion();

		c.x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
		c.y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x;
		c.z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w;
		c.w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;

		return c;
	}

	/**
	 * Multiply by another Quaternion.
	 * 
	 * @param b
	 *            The Quaternion to multiply by.
	 */
	public void mult(Quaternion b) {
		double aw = this.w, ax = this.x, ay = this.y, az = this.z;

		x = aw * b.x + ax * b.w + ay * b.z - az * b.y;
		y = aw * b.y - ax * b.z + ay * b.w + az * b.x;
		z = aw * b.z + ax * b.y - ay * b.x + az * b.w;
		w = aw * b.w - ax * b.x - ay * b.y - az * b.z;
	}

	public Quaternion multiply(Quaternion b) {
		double aw = this.w, ax = this.x, ay = this.y, az = this.z;

		return new Quaternion(aw * b.x + ax * b.w + ay * b.z - az * b.y, aw
				* b.y - ax * b.z + ay * b.w + az * b.x, aw * b.z + ax * b.y
				- ay * b.x + az * b.w, aw * b.w - ax * b.x - ay * b.y - az
				* b.z);
	}

	public String toString() {
		return "Quaternion: x:" + this.x + " y:" + this.y + " z:" + this.z
				+ " w:" + this.w;
	}

	/**
	 * Normalise.
	 * 
	 * @param a
	 * 
	 * @return
	 */
	public void normalise() {
		double len = getModulo();

		if (Math.abs(len) < EPSILON) {
			x = y = z = 0.0;
			w = 1.0;
		} else {
			double m = 1 / len;
			x *= m;
			y *= m;
			z *= m;
			w *= m;
		}
	}

	/**
	 * SLERP (Spherical Linear intERPolation). @author Trevor Burton
	 * 
	 * @param qa
	 *            start quaternion
	 * @param qb
	 *            end quaternion
	 * @param alpha
	 *            a value between 0 and 1
	 * 
	 * @return the interpolated quaternion.
	 */
	public static Quaternion slerp(Quaternion qa, Quaternion qb, double alpha) {
		double angle = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;

		if (angle < 0.0) {
			qa.x *= -1.0;
			qa.y *= -1.0;
			qa.z *= -1.0;
			qa.w *= -1.0;
			angle *= -1.0;
		}

		double scale;
		double invscale;

		if ((angle + 1.0) > EPSILON) // Take the shortest path
		{
			if ((1.0 - angle) >= EPSILON) // spherical interpolation
			{
				double theta = Math.acos(angle);
				double invsintheta = 1.0 / Math.sin(theta);
				scale = Math.sin(theta * (1.0 - alpha)) * invsintheta;
				invscale = Math.sin(theta * alpha) * invsintheta;
			} else // linear interploation
			{
				scale = 1.0 - alpha;
				invscale = alpha;
			}
		} else // long way to go...
		{
			qb.y = -qa.y;
			qb.x = qa.x;
			qb.w = -qa.w;
			qb.z = qa.z;

			scale = Math.sin(Math.PI * (0.5 - alpha));
			invscale = Math.sin(Math.PI * alpha);
		}

		return new Quaternion(scale * qa.x + invscale * qb.x, scale * qa.y
				+ invscale * qb.y, scale * qa.z + invscale * qb.z, scale * qa.w
				+ invscale * qb.w);
	}

	public Vector3 toEuler() {
		Vector3 euler = new Vector3();
		Quaternion q1 = this;

		double test = q1.x * q1.y + q1.z * q1.w;
		if (test > 0.499) { // singularity at north pole
			euler.x = 2 * Math.atan2(q1.x, q1.w);
			euler.y = Math.PI / 2;
			euler.z = 0;
			return euler;
		}
		if (test < -0.499) { // singularity at south pole
			euler.x = -2 * Math.atan2(q1.x, q1.w);
			euler.y = -Math.PI / 2;
			euler.z = 0;
			return euler;
		}

		double sqx = q1.x * q1.x;
		double sqy = q1.y * q1.y;
		double sqz = q1.z * q1.z;

		euler.x = Math.atan2(2 * q1.y * q1.w - 2 * q1.x * q1.z, 1 - 2 * sqy - 2
				* sqz);
		euler.y = Math.asin(2 * test);
		euler.z = Math.atan2(2 * q1.x * q1.w - 2 * q1.y * q1.z, 1 - 2 * sqx - 2
				* sqz);

		return euler;
	}

	/**
	 * Gets the matrix representation of this Quaternion.
	 * 
	 * @return matrix. @see org.papervision3d.core.Matrix3D
	 */
	public Matrix getMatrix() {
		double xx = x * x;
		double xy = x * y;
		double xz = x * z;
		double xw = x * w;
		double yy = y * y;
		double yz = y * z;
		double yw = y * w;
		double zz = z * z;
		double zw = z * w;

		_matrix.n11 = 1 - 2 * (yy + zz);
		_matrix.n12 = 2 * (xy - zw);
		_matrix.n13 = 2 * (xz + yw);

		_matrix.n21 = 2 * (xy + zw);
		_matrix.n22 = 1 - 2 * (xx + zz);
		_matrix.n23 = 2 * (yz - xw);

		_matrix.n31 = 2 * (xz - yw);
		_matrix.n32 = 2 * (yz + xw);
		_matrix.n33 = 1 - 2 * (xx + yy);

		return _matrix;
	}

	public static Quaternion sub(Quaternion a, Quaternion b) {
		return new Quaternion(a.x - b.x, a.y - b.y, a.z - b.z, a.w - b.w);
	}

	public static Quaternion add(Quaternion a, Quaternion b) {
		return new Quaternion(a.x + b.x, a.y + b.y, a.z + b.z, a.w + b.w);
	}

	public Quaternion add(Quaternion b) {
		return new Quaternion(x + b.x, y + b.y, z + b.z, w + b.w);
	}

	public Quaternion scale(double s) {
		return new Quaternion(w * s, x * s, y * s, z * s);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}
}
