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


public class Matrix {
	/**
	 * X O O O
	 * O O O O
	 * O O O O
	*/
	public double n11;

	/**
	 * O X O O
	 * O O O O
	 * O O O O
	*/
	public double n12 ;

	/**
	 * O O X O
	 * O O O O
	 * O O O O
	*/
	public double n13 ;

	/**
	 * O O O X
	 * O O O O
	 * O O O O
	*/
	public double n14 ;


	/**
	 * O O O O
	 * X O O O
	 * O O O O
	*/
	public double n21 ;

	/**
	 * O O O O
	 * O X O O
	 * O O O O
	*/
	public double n22;

	/**
	 * O O O O
	 * O O X O
	 * O O O O
	*/
	public double n23;

	/**
	 * O O O O
	 * O O O X
	 * O O O O
	*/
	public double n24;


	/**
	 * O O O O
	 * O O O O
	 * X O O O
	*/
	public double n31;

	/**
	 * O O O O
	 * O O O O
	 * O X O O
	*/
	public double n32;

	/**
	 * O O O O
	 * O O O O
	 * O O X O
	*/
	public double n33;

	/**
	 * O O O O
	 * O O O O
	 * O O O X
	*/
	public double n34;

	/**
	 * O O O O
	 * O O O O
	 * O O O O
	 * X O O O
	*/
	public double n41;

	/**
	 * O O O O
	 * O O O O
	 * O O O O
	 * O X O O
	*/
	public double n42;

	/**
	 * O O O O
	 * O O O O
	 * O O O O
	 * O O X O
	*/
	public double n43;

	/**
	 * O O O O
	 * O O O O
	 * O O O O
	 * O O O X
	*/
	public double n44;

	// temp objects to save constant instantiation of objects. 

	static private Matrix temp = new Matrix(); 
	
	static private Vector3 n3Di = Vector3.ZERO(); 
	static private Vector3 n3Dj = Vector3.ZERO(); 
	static private Vector3 n3Dk = Vector3.ZERO(); 
	
	// _________________________________________________________________________________ Matrix3D

	/**
	* The Matrix3D constructor lets you create Matrix3D objects.
	*
	* @param	args	The values to populate the matrix with. Identity matrix is returned by default.
	*/
	public Matrix( double[] args )
	{
		 
		reset(args); 
		//trace("new matrix");
	}
	
	public Matrix()
	{
		reset();
	}
	
	// sets the properties of the Matrix without creating a new one. 
	public void reset( double[] args)
	{
		n11 = args[0];  n12 = args[1];  n13 = args[2];  n14 = args[3];
		n21 = args[4];  n22 = args[5];  n23 = args[6];  n24 = args[7];
		n31 = args[8];  n32 = args[9];  n33 = args[10]; n34 = args[11];
		
		if( args.length == 16 )
		{
			n41 = args[12];	 n42 = args[13]; n43 = args[14]; n44 = args[15];
		} 
		else
		{
			n41 = n42 = n43 = 0;
			n44 = 1;
		}	
	}
	
	public void reset()
	{
		n11 = n22 = n33 = n44 = 1;
		n12 = n13 = n14 = n21 = n23 = n24 = n31 = n32 = n34 = n41 = n42 = n43 = 0;
	}

	// _________________________________________________________________________________ IDENTITY

	public static Matrix IDENTITY()
	{
		//trace("Matrix.IDENTITY"); 
		double[] a = {
				1.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 1.0, 0.0,
				0.0, 0.0, 0.0, 1.0
			};
		return new Matrix(a);
	}

	// _________________________________________________________________________________ trace

	public String toString()
	{
		String s = "";

		s += ((int)n11*1000)/1000 + "\t\t" + ((int)n12*1000)/1000 + "\t\t" + ((int)n13*1000)/1000 + "\t\t" + ((int)n14*1000)/1000 +"\n";
		s += ((int)n21*1000)/1000 + "\t\t" + ((int)n22*1000)/1000 + "\t\t" + ((int)n23*1000)/1000 + "\t\t" + ((int)n24*1000)/1000 + "\n";
		s += ((int)n31*1000)/1000 + "\t\t" + ((int)n32*1000)/1000 + "\t\t" + ((int)n33*1000)/1000 + "\t\t" + ((int)n34*1000)/1000 + "\n";
		s += ((int)n41*1000)/1000 + "\t\t" + ((int)n42*1000)/1000 + "\t\t" + ((int)n43*1000)/1000 + "\t\t" + ((int)n44*1000)/1000 + "\n";

		return s;
	}


	// _________________________________________________________________________________ OPERATIONS

	public void calculateMultiply( Matrix a, Matrix b )
	{
		double a11 = a.n11; double b11 = b.n11;
		double a21 = a.n21; double b21 = b.n21;
		double a31 = a.n31; double b31 = b.n31;
		double a12 = a.n12; double b12 = b.n12;
		double a22 = a.n22; double b22 = b.n22;
		double a32 = a.n32; double b32 = b.n32;
		double a13 = a.n13; double b13 = b.n13;
		double a23 = a.n23; double b23 = b.n23;
		double a33 = a.n33; double b33 = b.n33;
		double a14 = a.n14; double b14 = b.n14;
		double a24 = a.n24; double b24 = b.n24;
		double a34 = a.n34; double b34 = b.n34;

		this.n11 = a11 * b11 + a12 * b21 + a13 * b31;
		this.n12 = a11 * b12 + a12 * b22 + a13 * b32;
		this.n13 = a11 * b13 + a12 * b23 + a13 * b33;
		this.n14 = a11 * b14 + a12 * b24 + a13 * b34 + a14;

		this.n21 = a21 * b11 + a22 * b21 + a23 * b31;
		this.n22 = a21 * b12 + a22 * b22 + a23 * b32;
		this.n23 = a21 * b13 + a22 * b23 + a23 * b33;
		this.n24 = a21 * b14 + a22 * b24 + a23 * b34 + a24;

		this.n31 = a31 * b11 + a32 * b21 + a33 * b31;
		this.n32 = a31 * b12 + a32 * b22 + a33 * b32;
		this.n33 = a31 * b13 + a32 * b23 + a33 * b33;
		this.n34 = a31 * b14 + a32 * b24 + a33 * b34 + a34;
	}

	public static Matrix multiply( Matrix a, Matrix b )
	{
		//trace("matrix.multiply"); 
		Matrix m = new Matrix();
		
		m.calculateMultiply( a, b );
		
		return m;
	}


	public void calculateMultiply3x3( Matrix a, Matrix b )
	{
		double a11 = a.n11; double b11 = b.n11;
		double a21 = a.n21; double b21 = b.n21;
		double a31 = a.n31; double b31 = b.n31;
		double a12 = a.n12; double b12 = b.n12;
		double a22 = a.n22; double b22 = b.n22;
		double a32 = a.n32; double b32 = b.n32;
		double a13 = a.n13; double b13 = b.n13;
		double a23 = a.n23; double b23 = b.n23;
		double a33 = a.n33; double b33 = b.n33;

		this.n11 = a11 * b11 + a12 * b21 + a13 * b31;
		this.n12 = a11 * b12 + a12 * b22 + a13 * b32;
		this.n13 = a11 * b13 + a12 * b23 + a13 * b33;

		this.n21 = a21 * b11 + a22 * b21 + a23 * b31;
		this.n22 = a21 * b12 + a22 * b22 + a23 * b32;
		this.n23 = a21 * b13 + a22 * b23 + a23 * b33;

		this.n31 = a31 * b11 + a32 * b21 + a33 * b31;
		this.n32 = a31 * b12 + a32 * b22 + a33 * b32;
		this.n33 = a31 * b13 + a32 * b23 + a33 * b33;
	}

	public void calculateMultiply4x4( Matrix a, Matrix b )
	{
		double a11 = a.n11; double b11 = b.n11;
		double a21 = a.n21; double b21 = b.n21;
		double a31 = a.n31; double b31 = b.n31;
		double a41 = a.n41; double b41 = b.n41;
		
		double a12 = a.n12; double b12 = b.n12;
		double a22 = a.n22; double b22 = b.n22;
		double a32 = a.n32; double b32 = b.n32;
		double a42 = a.n42; double b42 = b.n42;
		
		double a13 = a.n13; double b13 = b.n13;
		double a23 = a.n23; double b23 = b.n23;
		double a33 = a.n33; double b33 = b.n33;
		double a43 = a.n43; double b43 = b.n43;
		
		double a14 = a.n14; double b14 = b.n14;
		double a24 = a.n24; double b24 = b.n24;
		double a34 = a.n34; double b34 = b.n34;
		double a44 = a.n44; double b44 = b.n44;

		this.n11 = a11 * b11 + a12 * b21 + a13 * b31;
		this.n12 = a11 * b12 + a12 * b22 + a13 * b32;
		this.n13 = a11 * b13 + a12 * b23 + a13 * b33;
		this.n14 = a11 * b14 + a12 * b24 + a13 * b34 + a14;

		this.n21 = a21 * b11 + a22 * b21 + a23 * b31;
		this.n22 = a21 * b12 + a22 * b22 + a23 * b32;
		this.n23 = a21 * b13 + a22 * b23 + a23 * b33;
		this.n24 = a21 * b14 + a22 * b24 + a23 * b34 + a24;

		this.n31 = a31 * b11 + a32 * b21 + a33 * b31;
		this.n32 = a31 * b12 + a32 * b22 + a33 * b32;
		this.n33 = a31 * b13 + a32 * b23 + a33 * b33;
		this.n34 = a31 * b14 + a32 * b24 + a33 * b34 + a34;
		
		this.n41 = a41 * b11 + a42 * b21 + a43 * b31;
		this.n42 = a41 * b12 + a42 * b22 + a43 * b32;
		this.n43 = a41 * b13 + a42 * b23 + a43 * b33;
		this.n44 = a41 * b14 + a42 * b24 + a43 * b34 + a44;
	}
	
	public static Matrix multiply3x3( Matrix a, Matrix b )
	{
		//trace("Multiply3x3"); 
		Matrix m = new Matrix();
		
		m.calculateMultiply3x3( a, b );
		
		return m;
	}


	public void calculateAdd( Matrix a, Matrix b )
	{
		this.n11 = a.n11 + b.n11;
		this.n12 = a.n12 + b.n12;
		this.n13 = a.n13 + b.n13;
		this.n14 = a.n14 + b.n14;

		this.n21 = a.n21 + b.n21;
		this.n22 = a.n22 + b.n22;
		this.n23 = a.n23 + b.n23;
		this.n24 = a.n24 + b.n24;

		this.n31 = a.n31 + b.n31;
		this.n32 = a.n32 + b.n32;
		this.n33 = a.n33 + b.n33;
		this.n34 = a.n34 + b.n34;
	}

	public static Matrix add( Matrix a, Matrix b )
	{
		//trace("matrix.add"); 
		Matrix m = new Matrix();
		
		m.calculateAdd( a, b );
		
		return m;
	}


	public void calculateInverse( Matrix m )
	{
		double d = m.det();

		if( Math.abs(d) > 0.001 )
		{
			d = 1/d;
	
			double m11 = m.n11; double m21 = m.n21; double m31 = m.n31;
			double m12 = m.n12; double m22 = m.n22; double m32 = m.n32;
			double m13 = m.n13; double m23 = m.n23; double m33 = m.n33;
			double m14 = m.n14; double m24 = m.n24; double m34 = m.n34;
	
			this.n11 =	 d * ( m22 * m33 - m32 * m23 );
			this.n12 =	-d * ( m12 * m33 - m32 * m13 );
			this.n13 =	 d * ( m12 * m23 - m22 * m13 );
			this.n14 =	-d * ( m12 * (m23*m34 - m33*m24) - m22 * (m13*m34 - m33*m14) + m32 * (m13*m24 - m23*m14) );
	
			this.n21 =	-d * ( m21 * m33 - m31 * m23 );
			this.n22 =	 d * ( m11 * m33 - m31 * m13 );
			this.n23 =	-d* ( m11 * m23 - m21 * m13 );
			this.n24 =	 d * ( m11 * (m23*m34 - m33*m24) - m21 * (m13*m34 - m33*m14) + m31 * (m13*m24 - m23*m14) );
	
			this.n31 =	 d * ( m21 * m32 - m31 * m22 );
			this.n32 =	-d* ( m11 * m32 - m31 * m12 );
			this.n33 =	 d * ( m11 * m22 - m21 * m12 );
			this.n34 =	-d* ( m11 * (m22*m34 - m32*m24) - m21 * (m12*m34 - m32*m14) + m31 * (m12*m24 - m22*m14) );
		}
	}

	public static Matrix inverse( Matrix m )
	{
		Matrix inv = new Matrix();
		inv.calculateInverse( m );
		return inv;
	}
	
	public void invert()
	{
		temp.copy(this); 	
		calculateInverse(temp); 
		
	}

	public double det()
	{
		return	(this.n11 * this.n22 - this.n21 * this.n12) * this.n33 - (this.n11 * this.n32 - this.n31 * this.n12) * this.n23 +
				(this.n21 * this.n32 - this.n31 * this.n22) * this.n13;
	}


	/*public function get trace():Number
	{
		return this.n11 + this.n22 + this.n33 + 1;
	}*/

	// _________________________________________________________________________________ COPY

	public Matrix copy( Matrix m )
	{
		this.n11 = m.n11;	this.n12 = m.n12;
		this.n13 = m.n13;	this.n14 = m.n14;

		this.n21 = m.n21;	this.n22 = m.n22;
		this.n23 = m.n23;	this.n24 = m.n24;

		this.n31 = m.n31;	this.n32 = m.n32;
		this.n33 = m.n33;	this.n34 = m.n34;

		return this;
	}


	public Matrix copy3x3( Matrix m )
	{
		this.n11 = m.n11;   this.n12 = m.n12;   this.n13 = m.n13;
		this.n21 = m.n21;   this.n22 = m.n22;   this.n23 = m.n23;
		this.n31 = m.n31;   this.n32 = m.n32;   this.n33 = m.n33;

		return this;
	}


	public static Matrix clone( Matrix m )
	{
		//trace("matrix3D.clone"); 
		double[] a = {
				m.n11, m.n12, m.n13, m.n14,
				m.n21, m.n22, m.n23, m.n24,
				m.n31, m.n32, m.n33, m.n34
			};
		return new Matrix(a);
	}

	// _________________________________________________________________________________ VECTOR

	public static void multiplyVector( Matrix m, Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;

		v.x = vx * m.n11 + vy * m.n12 + vz * m.n13 + m.n14;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23 + m.n24;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33 + m.n34;
	}

	public Vector3 multiplyVector( Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;

		v.x = vx * n11 + vy * n12 + vz * n13 + n14;
		v.y = vx * n21 + vy * n22 + vz * n23 + n24;
		v.z = vx * n31 + vy * n32 + vz * n33 + n34;
		
		return v;
	}

	public static void multiplyVector3x3( Matrix m, Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;

		v.x = vx * m.n11 + vy * m.n12 + vz * m.n13;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33;
	}
	
	public void multiplyVector3x3( Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;

		v.x = vx * n11 + vy * n12 + vz * n13;
		v.y = vx * n21 + vy * n22 + vz * n23;
		v.z = vx * n31 + vy * n32 + vz * n33;
	}

	public static void multiplyVector4x4( Matrix m, Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;
		double vw = 1.0 / (vx * m.n41 + vy * m.n42 + vz * m.n43 + m.n44);
		
		v.x = vx * m.n11 + vy * m.n12 + vz * m.n13 + m.n14;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23 + m.n24;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33 + m.n34;
		
		v.x *= vw;
		v.y *= vw;
		v.z *= vw;
	}
	
	public static void rotateAxis( Matrix m, Vector3 v )
	{
		double vx = v.x;
		double vy = v.y;
		double vz = v.z;

		v.x = vx * m.n11 + vy * m.n12 + vz * m.n13;
		v.y = vx * m.n21 + vy * m.n22 + vz * m.n23;
		v.z = vx * m.n31 + vy * m.n32 + vz * m.n33;

		v.normalise();
	}

/*
	public static function projectVector( m:Matrix3D, v:Number3D ):void
	{
		var c:Number = 1 / ( v.x * m.n41 + v.y * m.n42 + v.z * m.n43 + 1 );

		multiplyVector( m, v );

		v.x = v.x * c;
		v.y = v.y * c;
		v.z = 0;
	}
*/

	// _________________________________________________________________________________ EULER

/*
	public static function matrix2eulerOLD( m:Matrix3D ):Number3D
	{
		var angle:Number3D = new Number3D();

		var d :Number = -Math.asin( Math.max( -1, Math.min( 1, m.n13 ) ) ); // Calculate Y-axis angle
		var c :Number =  Math.cos( d );

		angle.y = d * toDEGREES;

		var trX:Number, trY:Number;

		if( Math.abs( c ) > 0.005 )  // Gimball lock?
		{
			trX =  m.n33 / c;  // No, so get X-axis angle
			trY = -m.n23 / c;

			angle.x = Math.atan2( trY, trX ) * toDEGREES;

			trX =  m.n11 / c;  // Get Z-axis angle
			trY = -m.n12 / c;

			angle.z = Math.atan2( trY, trX ) * toDEGREES;
		}
		else  // Gimball lock has occurred
		{
			angle.x = 0;  // Set X-axis angle to zero

			trX = m.n22;  // And calculate Z-axis angle
			trY = m.n21;

			angle.z = Math.atan2( trY, trX ) * toDEGREES;
		}

		// TODO: Clamp all angles to range

		return angle;
	}
*/

	public static Vector3 matrix2euler( Matrix t)
	{
		return matrix2euler(t, Vector3.ZERO());
	}
	
	public static Vector3 matrix2euler( Matrix t, Vector3 rot)
	{
		// Normalize the local x, y and z axes to remove scaling.
		n3Di.reset( t.n11, t.n21, t.n31 );
		n3Dj.reset( t.n12, t.n22, t.n32 );
		n3Dk.reset( t.n13, t.n23, t.n33 );

		n3Di.normalise();
		n3Dj.normalise();
		n3Dk.normalise();

		double[] a = {
		  			n3Di.x, n3Dj.x, n3Dk.x, 0,
					n3Di.y, n3Dj.y, n3Dk.y, 0,
					n3Di.z, n3Dj.z, n3Dk.z, 0
		};
		temp.reset(a);

		Matrix m = temp;

	    // Extract the first angle, rot.x
		rot.x = Math.atan2( m.n23, m.n33 ); // rot.x = Math<T>::atan2 (M[1][2], M[2][2]);
	
		// Remove the rot.x rotation from M, so that the remaining
		// rotation, N, is only around two axes, and gimbal lock
		// cannot occur.
		Matrix rx = Matrix.rotationX( -rot.x );
		Matrix n = Matrix.multiply( rx, m );

		// Extract the other two angles, rot.y and rot.z, from N.
		double cy = Math.sqrt( n.n11 * n.n11 + n.n21 * n.n21); // T cy = Math<T>::sqrt (N[0][0]*N[0][0] + N[0][1]*N[0][1]);
		rot.y = Math.atan2( -n.n31, cy ); // rot.y = Math<T>::atan2 (-N[0][2], cy);
		rot.z = Math.atan2( -n.n12, n.n11 ); //rot.z = Math<T>::atan2 (-N[1][0], N[1][1]);

		// Fix angles
		if( rot.x == Math.PI )
		{
			if( rot.y > 0 )
				rot.y -= Math.PI;
			else
				rot.y += Math.PI;

			rot.x = 0;
			rot.z += Math.PI;
		}

		// Convert to degrees if needed 
		// Shouldn't this have a check for Papervision3D.useDEGREES? 
		
		rot.x *= toDEGREES;
		rot.y *= toDEGREES;
		rot.z *= toDEGREES;
		
		return rot;
	}

	
	public static Matrix euler2matrix( Vector3 deg )
	{
		//trace("euler2matrix"); 
		
		temp.reset();
		Matrix m = temp;
		 
		m = temp; 

		double ax = deg.x * toRADIANS;
		double ay = deg.y * toRADIANS;
		double az = deg.z * toRADIANS;

		double a = Math.cos( ax );
		double b = Math.sin( ax );
		double c = Math.cos( ay );
		double d = Math.sin( ay );
		double e = Math.cos( az );
		double f = Math.sin( az );

		double ad = a * d;
		double bd = b * d;

		m.n11 =  c  * e;
		m.n12 = -c  * f;
		m.n13 =  d;
		m.n21 =  bd * e + a * f;
		m.n22 = -bd * f + a * e;
		m.n23 = -b  * c;
		m.n31 = -ad * e + b * f;
		m.n32 =  ad * f + b * e;
		m.n33 =  a  * c;

		return m;
	}

	// _________________________________________________________________________________ ROTATION

	public static Matrix rotationX( double rad )
	{
		//trace("rotationX"); 
		Matrix m = IDENTITY();
		
		double c = Math.cos( rad );
		double s = Math.sin( rad );

		m.n22 =  c;
		m.n23 = -s;
		m.n32 =  s;
		m.n33 =  c;

		return m;
	}

	public static Matrix rotationY( double rad )
	{
		//trace("rotationY"); 
		Matrix m = IDENTITY();
		double c = Math.cos( rad );
		double s = Math.sin( rad );

		m.n11 =  c;
		m.n13 = -s;
		m.n31 =  s;
		m.n33 =  c;

		return m;
	}

	public static Matrix rotationZ( double rad )
	{
		//trace("rotationZ"); 
		Matrix m = IDENTITY();
		double c = Math.cos( rad );
		double s = Math.sin( rad );

		m.n11 =  c;
		m.n12 = -s;
		m.n21 =  s;
		m.n22 =  c;

		return m;
	}
	
	public static Matrix rotationMatrix( double x, double y, double z, double rad)
	{
		return rotationMatrix(x, y, z, rad, IDENTITY());
	}

	public static Matrix rotationMatrix( double x, double y, double z, double rad, Matrix m )
	{		
		double nCos	= Math.cos( rad );
		double nSin	= Math.sin( rad );
		double scos	= 1 - nCos;

		double sxy	 = x * y * scos;
		double syz	 = y * z * scos;
		double sxz	 = x * z * scos;
		double sz	 = nSin * z;
		double sy	 = nSin * y;
		double sx	 = nSin * x;

		m.n11 =  nCos + x * x * scos;
		m.n12 = -sz   + sxy;
		m.n13 =  sy   + sxz;

		m.n21 =  sz   + sxy;
		m.n22 =  nCos + y * y * scos;
		m.n23 = -sx   + syz;

		m.n31 = -sy   + sxz;
		m.n32 =  sx   + syz;
		m.n33 =  nCos + z * z * scos;

		return m;
	}

	public static Matrix rotationMatrixWithReference( Vector3 axis, double rad, Vector3 ref )
	{
		Matrix m = Matrix.translationMatrix( ref.x, -ref.y, ref.z );

		m.calculateMultiply( m, Matrix.rotationMatrix( axis.x, axis.y, axis.z, rad ) );
		m.calculateMultiply( m, Matrix.translationMatrix ( -ref.x, ref.y, -ref.z ) );

		return m;
	}

	// _________________________________________________________________________________ TRANSFORM

	public static Matrix translationMatrix( double x, double y, double z )
	{
		//trace("translation matrix"); 
		Matrix m = IDENTITY();

		m.n14 = x;
		m.n24 = y;
		m.n34 = z;

		return m;
	}

	public static Matrix scaleMatrix( double x, double y, double z )
	{
		//trace("scalematrix"); 
		Matrix m = IDENTITY();

		m.n11 = x;
		m.n22 = y;
		m.n33 = z;

		return m;
	}

	// _________________________________________________________________________________ QUATERNIONS

	public static double magnitudeQuaternion( Quaternion q )
    {
		return( Math.sqrt( q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z ) );
    }


	public static Quaternion normalizeQuaternion( Quaternion q )
	{
		double mag = magnitudeQuaternion( q );

		q.x /= mag;
		q.y /= mag;
		q.z /= mag;
		q.w /= mag;

		return q;
	}


	public static Quaternion axis2quaternion( double x, double y, double z, double angle )
	{
		double sin = Math.sin( angle / 2 );
		double cos = Math.cos( angle / 2 );

		Quaternion q = new Quaternion();

		q.x = x * sin;
		q.y = y * sin;
		q.z = z * sin;
		q.w = cos;

		return normalizeQuaternion( q );
	}

	public static Quaternion euler2quaternion( double ax, double ay, double az )
	{
		return euler2quaternion(ax, ay, az, new Quaternion());
	}
	
	public static Quaternion euler2quaternion( double ax, double ay, double az, Quaternion q  )
    {
		double fSinPitch        = Math.sin( ax * 0.5 );
		double fCosPitch        = Math.cos( ax * 0.5 );
		double fSinYaw          = Math.sin( ay * 0.5 );
		double fCosYaw          = Math.cos( ay * 0.5 );
		double fSinRoll         = Math.sin( az * 0.5 );
		double fCosRoll         = Math.cos( az * 0.5 );
		double fCosPitchCosYaw  = fCosPitch * fCosYaw;
		double fSinPitchSinYaw  = fSinPitch * fSinYaw;

		q.x = fSinRoll * fCosPitchCosYaw     - fCosRoll * fSinPitchSinYaw;
		q.y = fCosRoll * fSinPitch * fCosYaw + fSinRoll * fCosPitch * fSinYaw;
		q.z = fCosRoll * fCosPitch * fSinYaw - fSinRoll * fSinPitch * fCosYaw;
		q.w = fCosRoll * fCosPitchCosYaw     + fSinRoll * fSinPitchSinYaw;

		return q;
    }

	// TODO (LOW) rewrite so that this takes an actual Quaternion object

	public static Matrix quaternion2matrix( double x, double y, double z, double w)
	{
		return quaternion2matrix(x, y, z, w, IDENTITY());
	}
	
	public static Matrix quaternion2matrix( double x, double y, double z, double w, Matrix m)
	{
		double xx = x * x;
		double xy = x * y;
		double xz = x * z;
		double xw = x * w;

		double yy = y * y;
		double yz = y * z;
		double yw = y * w;

		double zz = z * z;
		double zw = z * w;
		
		m.n11 = 1 - 2 * ( yy + zz );
		m.n12 =     2 * ( xy - zw );
		m.n13 =     2 * ( xz + yw );

		m.n21 =     2 * ( xy + zw );
		m.n22 = 1 - 2 * ( xx + zz );
		m.n23 =     2 * ( yz - xw );

		m.n31 =     2 * ( xz - yw );
		m.n32 =     2 * ( yz + xw );
		m.n33 = 1 - 2 * ( xx + yy );

		return m;
	}


	public static Quaternion multiplyQuaternion( Quaternion a, Quaternion b )
    {
		double ax = a.x;  double ay = a.y;  double az = a.z;  double aw = a.w;
		double bx = b.x;  double by = b.y;  double bz = b.z;  double bw = b.w;

		Quaternion q = new Quaternion();

		q.x = aw*bx + ax*bw + ay*bz - az*by;
		q.y = aw*by + ay*bw + az*bx - ax*bz;
		q.z = aw*bz + az*bw + ax*by - ay*bx;
		q.w = aw*bw - ax*bx - ay*by - az*bz;

		return q;
    }


	// _________________________________________________________________________________ TRIG

	static private double toDEGREES = 180/Math.PI;
	static private double toRADIANS = Math.PI/180;
	}
	
