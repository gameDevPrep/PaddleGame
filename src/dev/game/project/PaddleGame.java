package dev.game.project;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class PaddleGame {
	
	ArrayList<GameObject> gameBlocks = new ArrayList<GameObject>();//List of all blocks used by the game
	
	private PlayerPaddle myPaddle= new PlayerPaddle(Display.getWidth()/2,50,100,20);//create player paddle
	private Ball myBall = new Ball(Display.getWidth()/2,100,10);//create the ball
	private Boundary left=new Boundary(0,Display.getHeight()/2,1,Display.getHeight());//add left boundary
	private Boundary right=new Boundary(Display.getWidth(),Display.getHeight()/2,1,Display.getHeight());//add right boundary
	private Boundary bottom =new Boundary(Display.getWidth()/2,0,Display.getWidth(),1);//add bottom boundary
	private Boundary top=new Boundary(Display.getWidth()/2,Display.getHeight(),Display.getWidth(),1);//add top boundary
	
	
	/*
	 * TODO
	 * move collision logic to appropriate classes in the interest of maintaining OO paradigm
	 * scale element sizes to resolution,
	 * bugchecking.
	 */
	/**
	 * The main game loop takes care of pretty much everything,
	 * from user input to collisions.
	 */
	public  void startGame() {
		int num = 16;//variable used for brick generation
		float coordx = 50;//first brick coordinate
		float coordy = 500;//first brick coordinate
		
		glClear(GL_COLOR_BUFFER_BIT);//GL init
		glColor3f(0.25f, 0.75f, 0.5f);
		glLoadIdentity();
		
		/*
		 * Add Boundaries to blocks to check for collisions
		 */
		gameBlocks.add(right);
		gameBlocks.add(top);
		gameBlocks.add(left);
		gameBlocks.add(bottom);
		gameBlocks.add(myPaddle);
		
		/*
		 * Brick generating loop
		 */
		for (int j = 0; j<4; j++) {
			for (int i = 0; i < num; i++) {
				gameBlocks.add(new Brick(coordx,coordy,40,20));
				coordx+=40;
				coordx+=3;
			}
			coordy-=22;
			coordx-=16*43;
		}
		while(!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);//for each frame clear the screen
			
			processInput();//read player input
			myBall.update();//draw the ball
			
			/*
			 * Collision detection loop
			 */
			collisionPhysics();
			
			Display.update();//refresh the display
		}
		
	}
	private void collisionPhysics() {
		for (int i=0; i<gameBlocks.size();i++) {
			GameObject o=gameBlocks.get(i);//store current element in var to avoid parsing array
			o.update();//draw the bricks and paddle
			
			if(GamePhysics.hit(myBall, o)) {//if a collision did occur
				o.collided(myBall);
				if(o==left || o==right){//with either of the sides
					myBall.speedX*=-1;//bounce the ball off
				}else
				if(o==bottom){//if the collision occurred with the bottom boundary reset the ball
					//decrement player lives
					myBall.coordX=myPaddle.coordX;
					myBall.coordY=myPaddle.coordY+myPaddle.dimY/2+myBall.radius;
					myBall.speedX=0;
					myBall.speedY=0.1f;
				}else
				if(o==top){//if the collision was with the top boundary
					myBall.speedY*=-1;// bounce the ball back down
				}else
				if(o==myPaddle){//if the collision was with the player paddle
				//	myBall.speedY*=-1;
					myBall.speedY*=((o.coordY-myBall.coordY<0)?-1:1);//bounce the ball back
					myBall.speedX+=(myBall.coordX-gameBlocks.get(i).coordX)*0.01;//taking the angle into account
					if(myBall.speedX>Ball.MAX_SPEED){//make sure ball speed doesn't exceed max
						myBall.speedX=Ball.MAX_SPEED;
					}
					if(myBall.speedX<-Ball.MAX_SPEED){
						myBall.speedX=-Ball.MAX_SPEED;
					}
				
				}else
				
				if(o.isBrick()){//if collision happened with a brick
					myBall.speedY*=-1;//if the ball hits something, bounce it back
					gameBlocks.remove(i);//destroy the brick
				}
				
			}
		}
		
	}
	/**
	 * Function used to evaluate user input and move the paddle accordingly
	 */
	private  void processInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)){//if left was pressed
			myPaddle.move(-1);//move left
			return;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){//else if right was pressed
			myPaddle.move(1);//move right
			return;
		}
		
	}
	public  GameObject getBall() {
		
		return myBall;
	}
	public  GameObject getRight() {
		
		return right;
	}
	public  GameObject getLeft() {
		
		return left;
	}
	
}
