package org.tmcrafz.collisiontutorial;

import org.tmcrafz.collisiontutorial.Vector2f;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f subtract(Vector2f other) {
		float xNew = x - other.x;
		float yNew = y - other.y;
		return new Vector2f(xNew, yNew);		
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
}
