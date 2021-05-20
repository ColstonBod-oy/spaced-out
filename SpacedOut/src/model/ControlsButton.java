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
public class ControlsButton extends Button {
    
    private static final String FONT_PATH = "src/model/assets/kenvector_future.ttf";
    private static final String PRESSED_SFX_PATH = "/model/assets/sfx/rollover1.wav";
    private static final String ENTERED_SFX_PATH = "/model/assets/sfx/rollover2.wav";
    private static final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button12.png'); -fx-background-size: 98 98; -fx-background-repeat: no-repeat;";
    private static final String BUTTON_RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button11.png'); -fx-background-size: 98 98; -fx-background-repeat: no-repeat;";
    private static final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button06.png'); -fx-background-size: 98 98; -fx-background-repeat: no-repeat;";
    private AudioClip rollover1;
    private AudioClip rollover2;
    public static boolean isAudioOn;
    private boolean isSelected;
    private boolean isPressed;
    
    public ControlsButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(98);
        setPrefHeight(98);
        setStyle(BUTTON_DEFAULT_STYLE);
        isAudioOn = true;
        isSelected = false;
        isPressed = false;
        createButtonSFX();
        initButtonListeners();
    }
    
    private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), 23));
        } 
        
        catch (FileNotFoundException ex) {
            setFont(Font.font("Verdana", 23));
        }
    }
    
    private void setButtonPressed() {
        setStyle(BUTTON_PRESSED_STYLE);
        setLayoutY(getLayoutY() + 8);
    }
    
    private void setButtonReleased() {
        setStyle(BUTTON_RELEASED_STYLE);
        setLayoutY(getLayoutY() - 8);
    }
    
    private void setButtonExited() {
        setStyle(BUTTON_DEFAULT_STYLE);
        setLayoutY(getLayoutY() - 8);
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
                if (!isSelected && isPressed) {
                    setButtonExited();
                    isPressed = false;
                }
                
                else if (!isSelected) {
                    setStyle(BUTTON_DEFAULT_STYLE);
                }
            }
        });
    }
    
    public void moveButton() {
        if (isSelected) {
            setStyle(BUTTON_DEFAULT_STYLE);
            isSelected = false;
        }
        
        else {
            setStyle(BUTTON_RELEASED_STYLE);
            isSelected = true;
        }
    }
    
}
