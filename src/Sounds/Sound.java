package Sounds;


import java.io.FileInputStream;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class Sound {
	private FileInputStream myStream;
	private AdvancedPlayer mp3;
	private Instance CurrentInstance;
	private boolean playing=false; // left unimplemented
	public Sound(String Path){
		try {
			myStream=new FileInputStream(Path);
			mp3=new AdvancedPlayer(myStream);
			myStream.close();
			} catch (Exception e) {e.printStackTrace();}
	}
	
	public void LoadDiffPath(String Path){
		try{
		myStream=new FileInputStream(Path);
		mp3=new AdvancedPlayer(myStream);
		myStream.close();
		}
		catch(Exception e){e.printStackTrace();}
		
	}
	
	public void play(){
		CurrentInstance=new Instance(mp3);
		playing=CurrentInstance.play();
		
	}
		
	
			
}
