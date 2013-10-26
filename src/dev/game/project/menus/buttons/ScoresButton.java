package dev.game.project.menus.buttons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;

import dev.game.project.menu.buttons.Button;

public class ScoresButton extends Button {

	public ScoresButton(float coordX, float coordY, float dimX, float dimY) {
		super(coordX, coordY, dimX, dimY);
		try {
			myTexture=TextureLoader.getTexture("PNG", new FileInputStream(new File("res/scoresButton.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}