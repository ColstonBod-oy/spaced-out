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
import javafx.scene.text.Font;

/**
 *
 * @author user
 */
public class PanelButton extends Button {
    
    private static final String FONT_PATH = "src/model/assets/kenvector_future.ttf";
    private String buttonPressedStyle;
    private String buttonReleasedStyle;
    private String buttonDefaultStyle;
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
        isPressed = false;
        initButtonListeners(prefHeight);
    }
    
    private void setButtonFont(int size) {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), size));
        } 
        
        catch (FileNotFoundException ex) {
            setFont(Font.font("Verdana", size));
        }
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
    
    private void initButtonListeners(int height) {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressed(height);
                    isPressed = true;
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
