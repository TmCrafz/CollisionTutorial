package org.tmcrafz.collisiontutorial;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Game extends AnimationTimer {
	
	public static interface drawable {
		void draw(GraphicsContext graphicsContext);
	}
	
	static class CollisionObject implements drawable {
		private Vector2f m_position;
		
		public CollisionObject(float posX, float posY) {
			m_position = new Vector2f(posX, posY);
		}
		
		public Vector2f getPosition() { return m_position; }
		public void setPosition(float x, float y) { m_position.x = x; m_position.y = y; }
		public void setPosition(Vector2f pos) { m_position = pos; }

		@Override
		public void draw(GraphicsContext graphicsContext) {
			// Do nothing by default			
		}
	}
	
	static class Circle extends CollisionObject {
		
		private float m_radius;
		
		public Circle(float radius, float posX, float posY) {
			super(posX, posY);
			m_radius = radius;			
		}		
		
		public float getRadius() { return m_radius; }

		@Override
		public void draw(GraphicsContext graphicsContext) {
			graphicsContext.setFill(Color.RED);
			//graphicsContext.strokeOval(getPosition().x - m_radius, getPosition().y - m_radius, m_radius * 2, m_radius * 2);
			graphicsContext.fillOval(getPosition().x - m_radius, getPosition().y - m_radius, m_radius * 2, m_radius * 2);
		}
	}
	
	static class AABB extends CollisionObject {
		
		private float m_width;
		private float m_height;
		
		public AABB(float width, float height, float posX, float posY) {
			super(posX, posY);
			m_width = width;
			m_height = height;
		}		
		
		public float getLeft() { return getPosition().x - m_width / 2.f; }
		public float getRight() { return getPosition().x + m_width / 2.f; }
		public float getTop() { return getPosition().y - m_height / 2.f; }
		public float getBottom() { return getPosition().y + m_height / 2.f; }

		@Override
		public void draw(GraphicsContext graphicsContext) {
			graphicsContext.setFill(Color.RED);
			graphicsContext.fillRect
				(getLeft(), getTop(), m_width, m_height);
		}
	}
	
	private Scene m_scene;
	private GraphicsContext m_graphicsContext;
	private Font m_font;
	private Font m_fontCollision;
	
	private int m_width;
	private int m_height;
	private long startTime;
	
	private ArrayList<String> m_input;
	
	private Vector2f m_mousePos;
	private float m_fps;
	
	private CollisionObject m_playersObject;
	private ArrayList<CollisionObject> m_worldObjects;
	private boolean m_isCollision;
	
	public Game(Scene scene, GraphicsContext graphicsContext, int width, int height) {
		m_scene = scene;
		m_graphicsContext = graphicsContext;
		m_font = Font.font("Arial", FontWeight.BOLD, 12);		
		m_fontCollision = Font.font("Arial", FontWeight.BOLD, 20);
		
		m_width = width;
		m_height = height;
		m_input = new ArrayList<String>();
		
		m_mousePos = new Vector2f(0.f, 0.f);		
		
		m_scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				m_mousePos.x = (float) event.getX();
				m_mousePos.y = (float) event.getY();
			}
			
		});
		/*
		m_scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				m_mousePos.x = (float) event.getX();
				m_mousePos.y = (float) event.getY();
			}
			
		});
		*/
		m_scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String code = event.getCode().toString();
				if (!m_input.contains(code)) {
					m_input.add(code);
				}
			}
		});
		m_scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				String code = event.getCode().toString();
				m_input.remove(code);
			}
		});	
		
		m_worldObjects = new ArrayList<CollisionObject>();
		m_playersObject = new Circle(80.f, m_width / 2.f, m_height / 2.f);
		m_worldObjects.add(new Circle(60.f, m_width / 2.f - 60.f, m_height / 2.f + 120.f));
		m_worldObjects.add(m_playersObject);		
	}
	
	private boolean isColliding(Circle objA, Circle objB) {		
		Vector2f positionA = objA.getPosition();
		Vector2f positionB = objB.getPosition();
		// Ermittel den Richtungsvektor zwischen Kreis B und Kreis A
		Vector2f distanceVec = positionA.subtract(positionB);
		// Ermittel die länge des Richtungsvektors. 
		// Die länge ist der Abstand zwischen den beiden Kreis Mittelpukten
		float distance = distanceVec.length();
		// Wenn der gerade ermittelte Abstand kleiner ist als die Summe
		// der beiden Radien der Kreise, liegt eine Kollision vor		
		if (distance < objA.getRadius() + objB.getRadius()) {
			return true;
		}		
		return false;
	} 
	
	public void startGame() {
		startTime = System.nanoTime();
		start();		
	}
	
	private void update(float dt) {		
		/*
		if (m_input.contains("D")) {
			m_posX += (m_velX * dt);
		}
		if (m_input.contains("A")) {
			m_posX -= (m_velX * dt);
		}
		if (m_input.contains("W")) {
			m_posY -= (m_velY * dt);
		}
		if (m_input.contains("S")) {
			m_posY += (m_velY * dt);
		}
		*/
		m_playersObject.setPosition(m_mousePos.x, m_mousePos.y);
		// Prüfe ob eine Kollision statt findet
		m_isCollision = false;
		for (CollisionObject objA : m_worldObjects) {
			for (CollisionObject objB : m_worldObjects) {
				// Prüfe nur wenn es sich nicht um die selben Objekte handelt
				if (objA != objB && objA instanceof Circle && objB instanceof Circle) {
					if (isColliding((Circle) objA, (Circle) objB)) {
						m_isCollision = true;
						System.out.println("Colliding");
					}
				}				
			}	
		}	
	}
	
	
	private void render(float dt) {
		m_graphicsContext.clearRect(0, 0, m_width, m_height);				
		//m_graphicsContext.fillRect(m_rect.getMinX(), m_rect.getMinY(), m_rect.getWidth(), m_rect.getHeight());
				
		for (CollisionObject worldObj : m_worldObjects) {
			worldObj.draw(m_graphicsContext);
		}
		
		if (m_isCollision) {
			m_graphicsContext.setFill(Color.RED);
			m_graphicsContext.setFont(m_fontCollision);
			m_graphicsContext.fillText("COLLISION", m_width / 2 - 60, m_height - 10);
		}
		m_graphicsContext.setFont(m_font);
		m_graphicsContext.setFill(Color.BLACK);
		m_graphicsContext.fillText("DET: " + dt, 5, 10);
		m_graphicsContext.fillText("FPS: " + m_fps, 5, 20);
	}
	
	// Gameloop
	@Override
	public void handle(long now) {
		float dt = (float) ((now - startTime) / 1000000000.0 );
		startTime = System.nanoTime();
		m_fps = 1.f / dt;                      
		
		update(dt);
		render(dt);
		
	}

}
