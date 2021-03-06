package dev.game.project.gameMechanics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import Sounds.Sound;
import dev.game.project.engine.Drawable;
import dev.game.project.engine.Updateable;
import dev.game.project.menus.MainMenu;
import dev.game.project.menus.Menu;
import dev.game.project.menus.PauseMenu;
import dev.game.project.menus.SettingsMenu;

public class PaddleGame {
	private static boolean voodooMode=false;//@credit Jovan Davidovic\\
	private static long voodooTriggered=0l;//field used to keep track of when voodooMode was last triggered
	private static long escTriggered=0l;
	private static Level myLevel;//Level field containing all the game objects
	private static MainMenu myMainMenu;
	private static PauseMenu myPauseMenu; 
	private static SettingsMenu mySettingsMenu;
	//private static ScoresMenu myScoresMenu;//TODO
	private static boolean terminate=false;//variable used to check if the user hit ESCAPE
	private static int currentLevel=1;//variable used to keep track of the current level
	private static boolean drawTextures=getTextureDrawFromFile();//flag used to draw textures/colors
	private static int currentGameState=0;//variable used to keep track of the current game state.
	private static Sound music;
	static{
		
		myLevel=new Level(currentLevel);
		myMainMenu=new MainMenu();
		myPauseMenu=new PauseMenu();
		mySettingsMenu=new SettingsMenu();
		music=new Sound("./res/Trololo.mp3");
		//myScoresMenu=new ScoresMenu();//TODO probably have menus as static?
		
	}
	
	/**
	 * The main game loop takes care of pretty much everything,
	 * from user input to collisions.
	 * @param voodoo Parameter for voodooMode. If set to true, the game objects will render in a different color each frame.
	 */
	public static void startGame(boolean voodoo) {
		setVoodooMode(voodoo);//read voodooMode param
		while((!Display.isCloseRequested())&&!isTerminate()) {//if ESCAPE hasn't been hit and the display hasn't been closed otherwise
			glClear(GL_COLOR_BUFFER_BIT);//for each frame clear the screen
			//displayFPS();//print framerate for debug purposes
			processInput();//read player input
			getUpdate().update();//update the level
			getDraw().render();//then render it
			Display.sync(60);//force the framerate to 60 FPS or thereabouts
			Display.update();//refresh the display
			
		}
		
	}
	
	
	private static boolean getTextureDrawFromFile() {
		Scanner s = null;
		boolean value;
		try {
			s = new Scanner(new BufferedReader(new FileReader("Settings.ini")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!s.next().contentEquals("Color="));
		while(!s.hasNextBoolean()){
			s.next();
		}
		value=s.nextBoolean();
		s.close();
		return !value;
	}


	/**
	 * Function used to determine which game state to draw.
	 * @return The game state to be drawn.
	 */
	static Drawable getDraw(){//TODO
		switch(getCurrentGameState()){
			case 0:
				return myMainMenu;
			case 1:
				return myLevel;
			case 2:
				return myPauseMenu;
			case 3:
//				return myScoresMenu;
			case 4:
				return mySettingsMenu;
			default:
				return null;
		}
	}
	/**
	 * Function used to determine which game state to update.
	 * @return The game state to be updated.
	 */
	static Updateable getUpdate(){//TODO
		switch(getCurrentGameState()){
		case 0:
			return myMainMenu;
		case 1:
			return myLevel;
		case 2:
			return myPauseMenu;
		case 3:
//			return myScoresMenu;
		case 4:
			return mySettingsMenu;
		default:
			return null;
	}
	}
	private static long frameStart=0;//Variable used to keep track of the frame start time
	private static int frames=1;//number of frames in the current second
	/**
	 * Function used to calculate and display the framerate.
	 */
	@SuppressWarnings("unused")
	private static void displayFPS() {
		long currentTime=System.nanoTime();//read current time
		if(currentTime-frameStart>=1000000000){//if more than a second has passed
			System.out.println(frames);//print framerate
			frames=1;//reset frames
			frameStart=currentTime;//set start time of new frame
			return;
		}
		frames++;//if not increment number of frames
		
	}
	/**
	 * Function used to evaluate user input and move the paddle accordingly.
	 */
	private static void processInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)){//if left was pressed
			myLevel.movePaddle(-1);//move left
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){//else if right was pressed
			myLevel.movePaddle(1);//move right
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			if((((long)System.nanoTime()-escTriggered)>250000000)){//if time passed
				escTriggered=System.nanoTime();
				goBack();//turn on voodooMode
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V)){
			if(!isVoodooMode()&&(((long)System.nanoTime()-voodooTriggered)>250000000)){//if voodooMode is off and the break time passed
				voodooTriggered=System.nanoTime();
				setVoodooMode(true);//turn on voodooMode
			}
			else{
				if(isVoodooMode()&&(((long)System.nanoTime()-voodooTriggered)>250000000)){//if voodooMode is on and break time passed
					voodooTriggered=System.nanoTime();
					setVoodooMode(false);//turn voodooMode off
				}
			}
		}
		
	}
	/**
	 * Sets the gamestate to be returned to when the esc key is hit. 
	 */
	private static void goBack() {
		switch(getCurrentGameState()){
			case 0:
				//display "Are You Sure You Want To Quit Dialogue".
				terminate=true;
				break;
			case 1:
				currentGameState=2;//display PauseMenu.
				break;
			case 2:
				currentGameState=1;//go back to Game.
				break;
			case 3:
				
				currentGameState=0;//go back to main menu.
				break;
			case 4:
				updateInGameSettings();
				currentGameState=0;//go back to main menu.
				break;
			default:
				currentGameState=0;
		}	
	}
	public static void updateInGameSettings() {
		drawTextures=getTextureDrawFromFile();
		
	}


	/**
	 * Getter for the paddle object.
	 * @return Player paddle.
	 */

	public static Level getLevel() {
		return myLevel;
	}
	/**
	 * Getter for voodooMode.
	 * @return is voodooMode turned on.
	 */
	public static boolean isVoodooMode() {
		return voodooMode;
	}
	/**
	 * Setter for voodooMode.
	 * @param voodooMode  value to set.
	 */
	public static void setVoodooMode(boolean voodooMode) {
		PaddleGame.voodooMode = voodooMode;
	}
	/**
	 * @return the terminate
	 */
	public static boolean isTerminate() {
		return terminate;
	}
	/**
	 * @param terminate the terminate to set
	 */
	public static void setTerminate(boolean terminate) {
		PaddleGame.terminate = terminate;
	}
	/**
	 * @return the currentGameState
	 */
	public static int getCurrentGameState() {
		return currentGameState;
	}
	/**
	 * @param currentGameState the currentGameState to set
	 */
	public static void setCurrentGameState(int currentGameState) {
		Menu.setMenuCalled(System.nanoTime());
		PaddleGame.currentGameState = currentGameState;
	}
	/**
	 * @return the drawTextures
	 */
	public static boolean isDrawTextures() {
		return drawTextures;
	}
	/**
	 * @param drawTextures the drawTextures to set
	 */
	public static void setDrawTextures(boolean drawTextures) {
		PaddleGame.drawTextures = drawTextures;
	}
	/**
	 * Function used to progress to next level.
	 */
	public static void beatLevel(){
		myLevel=new Level(++currentLevel);
	}


	/**
	 * @return the myLevel
	 */
	public static Level getMyLevel() {
		return myLevel;
	}


	/**
	 * @param myLevel the myLevel to set
	 */
	public static void setMyLevel(Level myLevel) {
		PaddleGame.myLevel = myLevel;
	}
	
	public static void playMusic(){music.play();}
	
	public static void stopMusic(){music.stop();}
	
}

