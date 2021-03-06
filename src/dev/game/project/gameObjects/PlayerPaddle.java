package dev.game.project.gameObjects;

import static org.lwjgl.opengl.GL11.glColor3f;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.TextureLoader;
import dev.game.project.engine.Collidable;
import dev.game.project.engine.DrawObject;
import dev.game.project.engine.Movable;
import dev.game.project.gameMechanics.PaddleGame;

public class PlayerPaddle extends GameObject implements Movable,Collidable{
	
	private boolean widened;//flag for the widen bonus.
	private boolean narrowed;//flag for the narrow bonus.
	private int inverted=1;//flag and variable for the invert bonus.
	private float paddleSpeedUp=1;//flag and variable for paddleSpeed bonus.
	private static final float PADDLE_SPEED=7.8f;//paddle speed constant.
	

	/* (non-Javadoc)
	 * @see dev.game.project.GameObject#update()
	 */
	public void update(){//left unimplemented, all the paddle updating is done in input processing.
		
		
	}
	/**
	 * The Constructor for the player paddle.
	 * @param cordX The x coordinate of the paddle center.
	 * @param cordY The y coordinate of the paddle center.
	 * @param dimX The x dimension of the paddle.
	 * @param dimY The y dimension of the paddle.
	 */
	public PlayerPaddle(float cordX, float cordY, float dimX, float dimY) {
		this.setCoordX(cordX);//set coordX to passed value.
		this.setCoordY(cordY);//set coordY to passed value.
		this.setDimX(dimX);//set dimX to passed value.
		this.setDimY(dimY);//set dimY to passed value.
		try {
			setTexture(TextureLoader.getTexture("PNG", new FileInputStream(new File("res/playerPaddle.png"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see dev.game.project.Movable#move(int)
	 */
	public void move(int i) {
		if(PaddleGame.getCurrentGameState()==1){
			setCoordX(getCoordX() + PADDLE_SPEED*i*getInverted()*getPaddleSpeedUp());//move left or right
			if(getCoordX()+getDimX()/2>Display.getWidth()){//check if boundary was hit,
				setCoordX(Display.getWidth()-getDimX()/2);//if it was, stop.
			}
			if(getCoordX()-getDimX()/2<0){//check if boundary was hit,
				setCoordX(getDimX()/2);//if it was, stop.
			}
		}
	}
	/* (non-Javadoc)
	 * @see dev.game.project.Collidable#collided(dev.game.project.GameObject)
	 */
	@Override
	public void collided(GameObject o) {
		((Ball)o).setDirection((((Ball)o).getDirection() * ((getCoordY()-o.getCoordY()<0)?-1:1)));//bounce the ball back,
		((Ball)o).setSpeedX((float) (((Ball)o).getSpeedX() + (o.getCoordX()-getCoordX())*0.8));//taking the angle into account.
		if(((Ball)o).getSpeedX()>Ball.MAX_SPEED){//make sure ball speed doesn't exceed max,
			((Ball)o).setSpeedX(Ball.MAX_SPEED);//if it does, set speed to max.
		}
		if(((Ball)o).getSpeedX()<-Ball.MAX_SPEED){//make sure ball speed doesn't exceed max,
			((Ball)o).setSpeedX(-Ball.MAX_SPEED);//if it does, set speed to max.
		}

		
	}
	/* (non-Javadoc)
	 * @see dev.game.project.GameObject#render()
	 */
	@Override
	public void render() {
		if(!PaddleGame.isDrawTextures()){
			if(!PaddleGame.isVoodooMode()){//if voodooMode is off,
				glColor3f(0.25f, 0.75f, 0.5f);//set drawing color to cyan.
			}
			DrawObject.drawColoredRect(getCoordX(), getCoordY(), getDimX(), getDimY());//draw the rectangle
		}else
		DrawObject.draw(this);
	}
	/**
	 * Function used to trigger the widen bonus.
	 */
	public void widen() {
		if(!isWidened()){//if paddle isn't already widened,
			setWidened(true);//set the flag,
			this.setDimX(this.getDimX() * 1.5f);//widen the paddle.
			}
	}
	
	
	
	/**
	 * Function used to trigger the widen bonus.
	 */
	public void invert() {
		if(getInverted()==1){//if paddle isn't already inverted,
			setInverted(-1);//invert the paddle.
		}
		
	}
	/**
	 * Function used to trigger the paddle speed bonus.
	 */
	public void speedUp() {
		if(getPaddleSpeedUp()==1){//if the paddle isn't already sped up,
			setPaddleSpeedUp(1.5f);//speed it up.
		}
	}
	/**
	 * Function used to trigger the narrow bonus.
	 */
	public void narrow() {
		if(!isNarrowed()){//if the paddle isn't already narrowed,
			setNarrowed(true);//set the flag,
			this.setDimX(this.getDimX() / 1.5f);//narrow the paddle.
			}
	}
	/**
	 * Getter for inverted.
	 * @return The value of inverted.
	 */
	public int getInverted() {
		return inverted;
	}
	/**
	 * Setter for inverted.
	 * @param inverted The value of inverted to set.
	 */
	public void setInverted(int inverted) {
		this.inverted = inverted;
	}
	/**
	 * Getter for narrowed.
	 * @return The value of narrowed.
	 */
	public boolean isNarrowed() {
		return narrowed;
	}
	/**
	 * Setter for narrowed.
	 * @param narrowed The value of narrowed to set.
	 */
	public void setNarrowed(boolean narrowed) {
		this.narrowed = narrowed;
	}
	/**
	 * Getter for paddleSpeedUp.
	 * @return The value of paddleSpeedUp.
	 */
	public float getPaddleSpeedUp() {
		return paddleSpeedUp;
	}
	/**
	 * Setter for paddleSpeedUp.
	 * @param paddleSpeedUp The value of paddleSpeedUp to set.
	 */
	public void setPaddleSpeedUp(float paddleSpeedUp) {
		this.paddleSpeedUp = paddleSpeedUp;
	}
	/**
	 * Getter for widened.
	 * @return The value of widened.
	 */
	public boolean isWidened() {
		return widened;
	}
	/**
	 * Setter for widened.
	 * @param widened The value of widened to set
	 */
	public void setWidened(boolean widened) {
		this.widened = widened;
	}
	
}
