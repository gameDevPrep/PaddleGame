package dev.game.project;

import org.lwjgl.opengl.Display;

public class PlayerPaddle extends GameObject{
	public void update(){
		DrawObject.drawRect(coordX, coordY, dimX, dimY);
	}
	public PlayerPaddle(float cordX, float cordY, float dimX, float dimY) {
		this.coordX = cordX;
		this.coordY = cordY;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	public void move(int i) {
		coordX+=0.09*i;//move left or right
		if(coordX+dimX/2>Display.getWidth()){//check if boundary was hit
			coordX=Display.getWidth()-dimX/2;//if it was, stop
		}
		if(coordX-dimX/2<0){
			coordX=dimX/2;
		}
	}

}
