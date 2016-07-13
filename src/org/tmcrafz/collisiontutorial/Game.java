package org.tmcrafz.collisiontutorial;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
	
	private Scene m_scene;
	private GraphicsContext m_graphicsContext;
	private Font m_font;	
	private Rectangle2D m_rect;
	
	private int m_width;
	private int m_height;
	private long startTime;
	
	ArrayList<String> m_input;
	
	private Vector2f m_mousePos;
	private float m_fps;
	
	CollisionObject m_playersObject;
	
	
	public Game(Scene scene, GraphicsContext graphicsContext, int width, int height) {
		m_scene = scene;
		m_graphicsContext = graphicsContext;
		m_font = Font.font("Arial", FontWeight.BOLD, 12);		
		m_rect = new Rectangle2D(width / 2.0, height / 2.0, 40, 30);
		
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
		
		m_playersObject = new Circle(80.f, m_width / 2.f, m_height / 2.f);
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
	}
	
	private void render(float dt) {
		m_graphicsContext.clearRect(0, 0, m_width, m_height);				
		m_graphicsContext.fillRect(m_rect.getMinX(), m_rect.getMinY(), m_rect.getWidth(), m_rect.getHeight());		
		m_graphicsContext.setFill(Color.RED);
		m_graphicsContext.setFont(m_font);
		m_playersObject.draw(m_graphicsContext);
		m_graphicsContext.fillText("DET: " + dt, 10, 10);
		m_graphicsContext.fillText("FPS: " + m_fps, 10, 20);
	}
	
	// Gameloop
	@Override
	public void handle(long now) {
		float dt = (float) ((now - startTime) / 1000000000.0 );
		m_fps = 1.f / dt;                      
		startTime = System.nanoTime();
		
		update(dt);
		render(dt);
		
	}

}
