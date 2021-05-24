/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author user
 */
public class MenuLabel extends Label {
    
    private static final String FONT_PATH = "/model/assets/kenvector_future.ttf";
    private static final String BACKGROUND_IMAGE = "/view/assets/green_button13.png";
    
    public MenuLabel(String text, int fontSize, int prefWidth, int prefHeight, int topInset) {
        setText(text);
        setWrapText(true);
        setLabelFont(fontSize);
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
        setPadding(new Insets(topInset, 0, 0, 0));
        setAlignment(Pos.CENTER);
        setTextAlignment(TextAlignment.CENTER);
        
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, prefWidth, prefHeight, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        setBackground(new Background(image));
    }
    
    private void setLabelFont(int size) {
        Font font = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), size);
        setFont(font);
    }
    
}
