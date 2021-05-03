/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

/**
 *
 * @author user
 */
public class MenuButton extends Button {
    
    private static final String FONT_PATH = "src/model/assets/kenvector_future.ttf";
    private static final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/green_button05.png');";
    private static final String BUTTON_RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/green_button04.png');";
    private static final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/assets/grey_button03.png');";
    private boolean isActive;
    private boolean isPressed;
    
    public MenuButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(BUTTON_DEFAULT_STYLE);
        isActive = false;
        isPressed = false;
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
    
    private void initButtonListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressed();
                    isPressed = true;
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
