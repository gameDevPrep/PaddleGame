package dev.game.project.menus.buttons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;

import dev.game.project.gameMechanics.PaddleGame;


public class NewGameButton extends Button {


	public NewGameButton(float coordX, float coordY, float dimX, float dimY) {
		super(coordX, coordY, dimX, dimY);
		try {
			myTexture=TextureLoader.getTexture("PNG", new FileInputStream(new File("res/newGameButton.png")));
			pressedTexture=TextureLoader.getTexture("PNG", new FileInputStream(new File("res/newGameButton.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void pressed() {
		PaddleGame.setCurrentGameState(0);
		
	}

}
