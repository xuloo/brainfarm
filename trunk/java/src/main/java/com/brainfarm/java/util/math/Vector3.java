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

/**
 * The Number3D class represents a value in a three-dimensional coordinate
 * system.
 * 
 * Properties x, y and z represent the horizontal, vertical and z the depth axes
 * respectively.
 * 
 */
public class Vector3 /* implements IExternalizable */
{
	/**
	 * The horizontal coordinate value.
	 */
	public double x;

	/**
	 * The vertical coordinate value.
	 */
	public double y;

	/**
	 * The depth coordinate value.
	 */
	public double z;

	/**
	 * pre-made Number3D : used by various methods as a way to temporarily store
	 * Number3Ds.
	 */
	static private Vector3 temp = Vector3.ZERO();

	static public double toDEGREES = 180 / Math.PI;

	static public double toRADIANS = Math.PI / 180;

	/**
	 * Creates a new Number3D object whose three-dimensional values are
	 * specified by the x, y and z parameters. If you call this constructor
	 * function without parameters, a Number3D with x, y and z properties set to
	 * zero is created.
	 * 
	 * @param x
	 *            The horizontal coordinate value. The default value is zero.
	 * @param y
	 *            The vertical coordinate value. The default value is zero.
	 * @param z
	 *            The depth coordinate value. The default value is zero.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}

	/**
	 * Returns a new Number3D object that is a clone of the original instance
	 * with the same three-dimensional values.
	 * 
	 * @return A new Number3D instance with the same three-dimensional values as
	 *         the original Number3D instance.
	 */
	public Vector3 clone() {
		return new Vector3(this.x, this.y, this.z);
	}

	/**
	 * Copies the values of this Number3d to the passed Number3d.
	 * 
	 */
	public void copyTo(Vector3 n) {
		n.x = x;
		n.y = y;
		n.z = z;
	}

	/**
	 * Copies the values of this Number3d to the passed Number3d.
	 * 
	 */
	public void copyFrom(Vector3 n) {
		x = n.x;
		y = n.y;
		z = n.z;
	}

	/**
	 * Quick way to set the properties of the Number3D
	 * 
	 */
	public void clear(double newx, double newy, double newz) {
		x = newx;
		y = newy;
		z = newz;
	}

	public void clear() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}

	public void reset(double newx, double newy, double newz) {
		x = newx;
		y = newy;
		z = newz;
	}

	public void reset() {
		reset(0, 0, 0);
	}

	// ______________________________________________________________________
	// MATH

	/**
	 * Modulo
	 */
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Add
	 */
	public Vector3 add(Vector3 w) {
		return new Vector3(x + w.x, y + w.y, z + w.z);
	}

	/**
	 * Subtract.
	 */
	public static Vector3 sub(Vector3 v, Vector3 w) {
		return new Vector3(v.x - w.x, v.y - w.y, v.z - w.z);
	}

	public Vector3 returnSubtraction(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Dot product.
	 */
	public static double dot(Vector3 v, Vector3 w) {
		return (v.x * w.x + v.y * w.y + w.z * v.z);
	}

	public double dot(Vector3 w) {
		return (x * w.x + y * w.y + w.z * z);
	}

	/**
	 * Cross product. Now optionally takes a target Number3D to put the change
	 * into. So we're not constantly making new number3Ds. Maybe make a crossEq
	 * function?
	 */
	public static Vector3 cross(Vector3 v, Vector3 w, Vector3 targetN) {
		targetN.clear((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z),
				(w.x * v.y) - (w.y * v.x));
		return targetN;
	}

	public static Vector3 cross(Vector3 v, Vector3 w) {
		return cross(v, w, ZERO());
	}

	public Vector3 cross(Vector3 w) {
		return cross(this, w, ZERO());
	}

	/**
	 * Normalise.
	 */
	public void normalise() {
		double mod = getMagnitude();

		if (mod != 0 && mod != 1) {
			x /= mod;
			y /= mod;
			z /= mod;
		}
	}

	/**
	 * Multiplies the vector by a number. The same as the *= operator
	 */
	public void multiplyEq(double n) {
		x *= n;
		y *= n;
		z *= n;
	}

	public Vector3 returnScale(double s) {
		return new Vector3(x *= s, y *= s, z *= s);
	}

	public Vector3 returnDivision(double s) {
		return new Vector3(x /= s, y /= s, z /= s);
	}

	/**
	 * Adds the vector passed to this vector. The same as the += operator.
	 */
	public void plusEq(Vector3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public Vector3 returnAddition(Vector3 v) {
		return new Vector3(x += v.x, y += v.y, z += v.z);
	}

	/**
	 * Subtracts the vector passed to this vector. The same as the -= operator.
	 */
	public void minusEq(Vector3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}

	public Vector3 returnNegative() {
		return new Vector3(-x, -y, -z);
	}

	// ______________________________________________________________________

	/**
	 * Super fast modulo(length, magnitude) comparisons.
	 * 
	 * 
	 */
	public boolean isMagnitudeLessThan(double v) {

		return (getSquareMagnitude() < (v * v));
	}

	public boolean isMagnitudeGreaterThan(double v) {

		return (getSquareMagnitude() > (v * v));
	}

	public boolean isMagnitudeEqualTo(double v) {

		return (getSquareMagnitude() == (v * v));
	}

	public double getSquareMagnitude() {
		return (x * x + y * y + z * z);
	}

	// ______________________________________________________________________

	/**
	 * Returns a Number3D object with x, y and z properties set to zero.
	 * 
	 * @return A Number3D object.
	 */
	static public Vector3 ZERO() {
		return new Vector3();
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	/**
	 * Returns a string value representing the three-dimensional values in the
	 * specified Number3D object.
	 * 
	 * @return A string.
	 */
	public String toString() {
		return "x: " + Math.round(x * 100) / 100 + " y: " + Math.round(y * 100)
				/ 100 + " z: " + Math.round(z * 100) / 100;
	}

	// ------- TRIG FUNCTIONS
	public void rotateX(double angle) {
		angle *= toRADIANS;

		double cosRY = Math.cos(angle);
		double sinRY = Math.sin(angle);

		temp.copyFrom(this);

		this.y = (temp.y * cosRY) - (temp.z * sinRY);
		this.z = (temp.y * sinRY) + (temp.z * cosRY);
	}

	public void rotateY(double angle) {
		angle *= toRADIANS;

		double cosRY = Math.cos(angle);
		double sinRY = Math.sin(angle);

		temp.copyFrom(this);

		this.x = (temp.x * cosRY) + (temp.z * sinRY);
		this.z = (temp.x * -sinRY) + (temp.z * cosRY);
	}

	public void rotateZ(double angle) {
		angle *= toRADIANS;

		double cosRY = Math.cos(angle);
		double sinRY = Math.sin(angle);

		temp.copyFrom(this);

		x = (temp.x * cosRY) - (temp.y * sinRY);
		y = (temp.x * sinRY) + (temp.y * cosRY);
	}

	/** Creates a vector between the given two points. */
	static public Vector3 vectorBetween(Vector3 from, Vector3 to) {
		return new Vector3(to.x - from.x, to.y - from.y, to.z - from.z);
	}

	public double distance(Vector3 other) {
		return vectorBetween(this, other).getMagnitude();
	}

	public void setMagnitude(double magnitude) {
		normalise();
		multiplyEq(magnitude);
	}

	public void destroy() {
	}

	public boolean equals(Vector3 other) {
		return x == other.x && y == other.y && z == other.z;
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
}