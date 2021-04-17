/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author user
 */
public class PanelButton extends Button {
    
    private final String FONT_PATH = "src/model/assets/kenvector_future.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button12.png'); -fx-background-size: 147 147; -fx-background-repeat: no-repeat;";
    private final String BUTTON_RELEASED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/green_button11.png'); -fx-background-size: 147 147; -fx-background-repeat: no-repeat;";
    private final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/view/assets/yellow_button11.png'); -fx-background-size: 147 147; -fx-background-repeat: no-repeat;";
    private boolean isPressed;
    
    public PanelButton(String text) {
        setText(text);
        setButtonFont();
        setPrefWidth(147);
        setPrefHeight(147);
        setStyle(BUTTON_DEFAULT_STYLE);
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
        setLayoutY(getLayoutY() + 12);
    }
    
    private void setButtonReleased() {
        setStyle(BUTTON_RELEASED_STYLE);
        setLayoutY(getLayoutY() - 12);
    }
    
    private void setButtonExited() {
        setStyle(BUTTON_DEFAULT_STYLE);
        setLayoutY(getLayoutY() - 12);
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
                if (isPressed) {
                    setButtonExited();
                    isPressed = false;
                }
                
                else {
                    setStyle(BUTTON_DEFAULT_STYLE);
                }
            }
        });
    }
    
}
