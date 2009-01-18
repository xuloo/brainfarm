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
package com.brainfarm.flash.data 
{
	import com.actionengine.flash.core.BaseClass;
	import com.brainfarm.flash.util.math.Matrix3D;
	import com.brainfarm.flash.util.math.Quaternion;
	import com.brainfarm.flash.util.math.Vector3;	

	/**
	 * @author Trevor Burton [worldofpaper@googlemail.com]
	 */
	public class State extends BaseClass
	{		
		public var transform : Matrix3D;

		public var position : Vector3;

		public var orientation : Quaternion;		

		public var velocity : Vector3;

		public var rotation : Number;

		public var forces : Vector3;

		public function State()
		{
			super( );
		}

		override public function initialise(...args) : void
		{			
			transform = Matrix3D.IDENTITY;
			position = new Vector3( );
			orientation = new Quaternion( );
			velocity = new Vector3( );	
			rotation = 0;
		}

		public function equals(other : State) : Boolean
		{
			return  transform == other.transform && position.equals( other.position ) && orientation.equals( other.orientation ) && velocity.equals( other.velocity ) && rotation == other.rotation; 	
		}

		public function notEquals(other : State) : Boolean
		{
			return !equals( other );
		}

		public function clone() : State
		{
			var state : State = new State( );
			
			state.transform = Matrix3D( transform.clone( ) );
			state.position = Vector3( position.clone( ) );
			state.orientation = Quaternion( orientation.clone( ) );
			state.velocity = Vector3( velocity.clone( ) );
			state.rotation = rotation;
			
			return state;	
		}

		public function compare(other : State) : Boolean
		{
			var threshold : Number = 0.1 * 0.1;
			
			//return other.position.returnSubtraction( position ).isMagnitudeGreaterThan( threshold );
			return true;
		}

		override public function destroy() : void
		{
			transform.destroy( );
			position.destroy( );
			orientation.destroy( );
			velocity.destroy( );	
			rotation = 0;
		}

		// GETTERS / SETTERS for sending this object over the wire.

		public function getTransform() : Matrix3D
		{
			return transform;
		}

		public function setTransform(transform : Matrix3D) : void
		{
			this.transform = transform;	
		}

		public function getPosition() : Vector3
		{
			return position;
		}

		public function setPosition(position : Vector3) : void
		{
			this.position = position;
		}

		public function getOrientation() : Quaternion
		{
			return orientation;
		}

		public function setOrientation(orientation : Quaternion) : void
		{
			this.orientation = orientation;
		}

		public function getVelocity() : Vector3
		{
			return velocity;
		}

		public function setVelocity(velocity : Vector3) : void
		{
			this.velocity = velocity;	
		}

		public function getRotation() : Number
		{
			return rotation;
		}

		public function setRotation(rotation : Number) : void
		{
			this.rotation = rotation;	
		}

		public function getForces() : Vector3
		{
			return forces;
		}

		public function setForces(forces : Vector3) : void 
		{
			this.forces = forces;
		}

		public function toString() : String
		{
			return 'State {\n' + '    position: ' + position + '\n    orientation: ' + orientation.w + '\n}';	
		}
	}
}
