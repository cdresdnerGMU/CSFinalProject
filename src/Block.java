import java.awt.Color;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Block {
    private int x = 5;
    private int y = 5;
    private int width = 40;
    private int height = 20;
    Clip glass;
    
    public Block() {
//    	try {
//	    	AudioInputStream sample;
//	    	File soundFile = new File("/Users/cdresdner/git/CSFinalProject/GlassBreak.wav");
//	        //URL defaultSound = getClass().getResource("/Users/cdresdner/git/CSFinalProject/bin/GlassBreak.mp3");
//	        sample = AudioSystem.getAudioInputStream(soundFile);
//	        glass = AudioSystem.getClip();
//	        glass.open(sample);
//    	}
//    	catch (Exception e) {
//    		e.printStackTrace();
//    	}
    }
    
    int getX(){
        return x;
    }
    
    int getY(){
        return y;
    }
    
    int getWidth(){
        return width;
    }
    
    int getHeight(){
        return height;
    }
    
    // SETTERS
    void setCoordinates(int newX, int newY){
        x = newX;
        y = newY;
    }
    
    Color getFillColor() {
    	return Color.CYAN;
    }
    
    Color getBorderColor() {
    	return Color.black;
    }
    
    public void blockHit() {
    	this.width = 0;
    	this.height = 0;
    	this.x = 0;
    	this.y = 0;
    	playGlassBreak();
    }
    
    public void playGlassBreak() {
        //glass.stop();
        //glass.setFramePosition(0);
        glass.start(); 
    }
}