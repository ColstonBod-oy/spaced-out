/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class MenuSubScene extends SubScene {
    
    private static final String BACKGROUND_IMAGE = "/model/assets/green_panel.png";
    private static final String SLIDE_SFX_PATH = "/model/assets/sfx/slide-electronic-02.wav";
    private AnchorPane root;
    private TranslateTransition transition;
    private AudioClip slide;
    public static boolean isAudioOn;
    private boolean isHidden;
    
    public MenuSubScene(int prefWidth, int prefHeight, double y) {
        super(new AnchorPane(), prefWidth, prefHeight);
        prefWidth(prefWidth);
        prefHeight(prefHeight);
        
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, prefWidth, prefHeight, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        root = (AnchorPane) this.getRoot();
        root.setBackground(new Background(image));
        isAudioOn = true;
        isHidden = true;
        createSubSceneSFX();
        setLayoutX(1024);
        setLayoutY(y);
    }
    
    private void createSubSceneSFX() {
        slide = new AudioClip(getClass().getResource(SLIDE_SFX_PATH).toExternalForm());
    }
    
    public AnchorPane getPane() {
        return root;
    }
    
    public void moveSubScene(int endpoint) {
        transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        
        if (isHidden) {
            transition.setToX(endpoint);
            isHidden = false;
            
            if (isAudioOn) {
                slide.play();
            }
        }
        
        else {
            transition.setToX(0);
            isHidden = true;
        }
        
        transition.play();
    }
    
    public TranslateTransition getTranslateTransition() {
        return transition;
    }
    
}
