/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

/**
 *
 * @author user
 */
public class PanelButton extends Button {
    
    private static final String FONT_PATH = "/model/assets/kenvector_future.ttf";
    private static final String PRESSED_SFX_PATH = "/model/assets/sfx/rollover1.wav";
    private static final String ENTERED_SFX_PATH = "/model/assets/sfx/rollover2.wav";
    private String buttonPressedStyle;
    private String buttonReleasedStyle;
    private String buttonDefaultStyle;
    private AudioClip rollover1;
    private AudioClip rollover2;
    public static boolean isAudioOn;
    private boolean isPressed;
    
    public PanelButton(String text, int fontSize, int prefWidth, int prefHeight) {
        setText(text);
        setButtonFont(fontSize);
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
        buttonPressedStyle = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button12.png'); -fx-background-size: " + prefWidth + " " + prefHeight + "; -fx-background-repeat: no-repeat;";
        buttonReleasedStyle = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button11.png'); -fx-background-size: " + prefWidth + " " + prefHeight + "; -fx-background-repeat: no-repeat;";
        buttonDefaultStyle = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button06.png'); -fx-background-size: " + prefWidth + " " + prefHeight + "; -fx-background-repeat: no-repeat;";
        setStyle(buttonDefaultStyle);
        isAudioOn = true;
        isPressed = false;
        createButtonSFX();
        initButtonListeners(prefHeight);
    }
    
    private void setButtonFont(int size) {
        Font font = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), size);
        setFont(font);
    }
    
    private void setButtonPressed(int offset) {
        setStyle(buttonPressedStyle);
        setLayoutY(getLayoutY() + (double) 4 * offset / 49);
    }
    
    private void setButtonReleased(int offset) {
        setStyle(buttonReleasedStyle);
        setLayoutY(getLayoutY() - (double) 4 * offset / 49);
    }
    
    private void setButtonExited(int offset) {
        setStyle(buttonDefaultStyle);
        setLayoutY(getLayoutY() - (double) 4 * offset / 49);
    }
    
    private void createButtonSFX() {
        rollover1 = new AudioClip(getClass().getResource(PRESSED_SFX_PATH).toExternalForm());
        rollover2 = new AudioClip(getClass().getResource(ENTERED_SFX_PATH).toExternalForm());
    }
    
    private void initButtonListeners(int height) {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressed(height);
                    isPressed = true;
                    
                    if (isAudioOn) {
                        rollover1.play();
                    }
                }
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY) && isPressed) {
                    setButtonReleased(height);
                    isPressed = false;
                }
            }
        });
       
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setStyle(buttonReleasedStyle);
                
                if (isAudioOn) {
                    rollover2.play();
                }
            }
        });
        
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (isPressed) {
                    setButtonExited(height);
                    isPressed = false;
                }
                
                else {
                    setStyle(buttonDefaultStyle);
                }
            }
        });
    }
    
}
