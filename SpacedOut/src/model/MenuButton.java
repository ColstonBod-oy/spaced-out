/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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
public class MenuButton extends Button {
    
    private static final String FONT_PATH = "/model/assets/kenvector_future.ttf";
    private static final String PRESSED_SFX_PATH = "/model/assets/sfx/rollover1.wav";
    private static final String ENTERED_SFX_PATH = "/model/assets/sfx/rollover2.wav";
    private static final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/green_button05.png');";
    private static final String BUTTON_RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/green_button04.png');";
    private static final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/grey_button03.png');";
    private AudioClip rollover1;
    private AudioClip rollover2;
    public static boolean isAudioOn;
    private boolean isActive;
    private boolean isPressed;
    
    public MenuButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(BUTTON_DEFAULT_STYLE);
        isAudioOn = true;
        isActive = false;
        isPressed = false;
        createButtonSFX();
        initButtonListeners();
    }
    
    private void setButtonFont() {
        Font font = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 23);
        setFont(font);
    }
    
    private void setButtonPressed() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }
    
    private void setButtonReleased() {
        setStyle(BUTTON_RELEASED_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }
    
    private void setButtonExited() {
        setStyle(BUTTON_DEFAULT_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY() - 4);
    }
    
    private void createButtonSFX() {
        rollover1 = new AudioClip(getClass().getResource(PRESSED_SFX_PATH).toExternalForm());
        rollover2 = new AudioClip(getClass().getResource(ENTERED_SFX_PATH).toExternalForm());
    }
    
    private void initButtonListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressed();
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
                    setButtonReleased();
                    isPressed = false;  
                }
            }
        });
       
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setStyle(BUTTON_RELEASED_STYLE);
                
                if (isAudioOn) {
                    rollover2.play();
                }
            }
        });
        
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (!isActive && isPressed) {
                    setButtonExited();
                    isPressed = false;
                }
                
                else if (!isActive) {
                    setStyle(BUTTON_DEFAULT_STYLE);
                }
            }
        });
    }
    
    public void moveButton() {
        if (isActive) {
            setStyle(BUTTON_DEFAULT_STYLE);
            isActive = false;
        }
        
        else {
            setStyle(BUTTON_RELEASED_STYLE);
            isActive = true;
        }
    }
    
}
