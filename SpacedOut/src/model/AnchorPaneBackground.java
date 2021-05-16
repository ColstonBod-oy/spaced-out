/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Collection;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import view.GameView;

/**
 *
 * @author user
 */
public class AnchorPaneBackground {
    
    private static final Image[] BACKGROUND_SPRITE_IMAGES = {
        new Image("view/assets/sprites/blue.png"),
        new Image("view/assets/sprites/black.png"),
        new Image("view/assets/sprites/darkPurple.png"),
        new Image("view/assets/sprites/purple.png")
    };
    private static final Image[] BLUE_STAR_SPRITE_IMAGES = {
        new Image("view/assets/sprites/sprite_blue_star0.png"),
        new Image("view/assets/sprites/sprite_blue_star1.png"),
        new Image("view/assets/sprites/sprite_blue_star2.png"),
        new Image("view/assets/sprites/sprite_blue_star3.png")
    };
    private static final Image[] WHITE_STAR_SPRITE_IMAGES = {
        new Image("view/assets/sprites/sprite_white_star0.png"),
        new Image("view/assets/sprites/sprite_white_star1.png"),
        new Image("view/assets/sprites/sprite_white_star2.png"),
        new Image("view/assets/sprites/sprite_white_star3.png")
    };
    private Random backgroundRandom;
    private AnimationTimer backgroundAnimationTimer;
    private Timeline starsTimeline;
    private ImageView[] backgroundImages1;
    private ImageView[] backgroundImages2;
    private ImageView[][] stars;
    private int[] starsRandom;
    private GridPane gridPane1;
    private GridPane gridPane2;
    private GameView gameView;
    
    public AnchorPaneBackground() {
        backgroundRandom = new Random();
    }
    
    public void setBackground(int index) {
        for (int i = 0; i < 12; i++) {
            backgroundImages1[i].setImage(BACKGROUND_SPRITE_IMAGES[index]);
            backgroundImages2[i].setImage(BACKGROUND_SPRITE_IMAGES[index]);
        }
    }
    
    public void startBackgroundAnimationTimer() {
        backgroundAnimationTimer.start();
    }
    
    public void stopBackgroundAnimationTimer() {
        backgroundAnimationTimer.stop();
    }
    
    public void playStarsTimeline() {
        starsTimeline.play();
    }
    
    public void stopStarsTimeline() {
        starsTimeline.stop();
    }
    
    public void createNewBackground(GameView gameView) {
        this.gameView = gameView;
        createBackground(gameView.getPane());
        createStars(gameView.getPane());
        createBackgroundLoop();
        animateStars();
    }
    
    public void createNewBackground(AnchorPane root, int step) {
        createBackground(root);
        createStars(root);
        createBackgroundLoop(step);
        animateStars();
    }
    
    private void createBackground(AnchorPane root) {
        backgroundImages1 = new ImageView[12];
        backgroundImages2 = new ImageView[12];
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();
        
        for (int i = 0; i < backgroundImages1.length; i++) {
            backgroundImages1[i] = new ImageView(BACKGROUND_SPRITE_IMAGES[0]);
            GridPane.setConstraints(backgroundImages1[i], i % 4, i / 4);
            gridPane1.getChildren().add(backgroundImages1[i]);
        }
        
        for (int i = 0; i < backgroundImages2.length; i++) {
            backgroundImages2[i] = new ImageView(BACKGROUND_SPRITE_IMAGES[0]);
            GridPane.setConstraints(backgroundImages2[i], i % 4, i / 4);
            gridPane2.getChildren().add(backgroundImages2[i]);
        }
        
        gridPane2.setLayoutY(-768);
        root.getChildren().addAll(gridPane1, gridPane2);
    }
    
    private void createStars(AnchorPane root) {
        stars = new ImageView[20][2];
        starsRandom = new int[20];
        
        for (int i = 0; i < stars.length; i++) {
            stars[i][0] = new ImageView(BLUE_STAR_SPRITE_IMAGES[0]);
            stars[i][0].setLayoutY(-35);
            root.getChildren().add(stars[i][0]);
            
            stars[i][1] = new ImageView(WHITE_STAR_SPRITE_IMAGES[0]);
            stars[i][1].setLayoutY(-35);
            root.getChildren().add(stars[i][1]);
            
            starsRandom[i] = backgroundRandom.nextInt(stars[i].length);
            stars[i][starsRandom[i]].setLayoutX(backgroundRandom.nextInt(990));
            stars[i][starsRandom[i]].setLayoutY(backgroundRandom.nextInt(769) - 35);
        }
    }
    
    private void spawnStars() {
        for (int i = 0; i < stars.length; i++) {
            if (stars[i][starsRandom[i]].getLayoutY() > 803) {
                starsRandom[i] = backgroundRandom.nextInt(stars[i].length);
                generateStarsPosition(stars[i][starsRandom[i]]);
                gameView.addDistanceTraveled();
            }
            
            else {
                stars[i][starsRandom[i]].setLayoutY(stars[i][starsRandom[i]].getLayoutY() + gameView.getBackgroundStep());
            }
        }
    }
    
    private void spawnStars(int backgroundStep) {
        for (int i = 0; i < stars.length; i++) {
            if (stars[i][starsRandom[i]].getLayoutY() > 803) {
                starsRandom[i] = backgroundRandom.nextInt(stars[i].length);
                generateStarsPosition(stars[i][starsRandom[i]]);
            }
            
            else {
                stars[i][starsRandom[i]].setLayoutY(stars[i][starsRandom[i]].getLayoutY() + backgroundStep);
            }
        }
    }
    
    private void createBackgroundLoop() {
        backgroundAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveBackground();
                spawnStars();
            }
        };
        
        backgroundAnimationTimer.start();
    }
    
    private void createBackgroundLoop(int backgroundStep) {
        backgroundAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveBackground(backgroundStep);
                spawnStars(backgroundStep);
            }
        };
        
        backgroundAnimationTimer.start();
    }
    
    private void generateStarsPosition(ImageView image) {
        image.setLayoutX(backgroundRandom.nextInt(990));
        image.setLayoutY(-(backgroundRandom.nextInt(769) + 35));
    }
    
    private void animateStars() {
        starsTimeline = new Timeline();
        Collection<KeyFrame> frames = starsTimeline.getKeyFrames();
        Duration frameTime = Duration.ZERO;
        Duration frameGap = Duration.millis(100);
        
        for (Image img : BLUE_STAR_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                for (ImageView[] star : stars) {
                    star[0].setImage(img);
                }
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : WHITE_STAR_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                for (ImageView[] star : stars) {
                    star[1].setImage(img);
                }
            }));
        }
        
        starsTimeline.setAutoReverse(true);
        starsTimeline.setCycleCount(Animation.INDEFINITE);
        starsTimeline.play();
    }
    
    private void moveBackground() {
        gridPane1.setLayoutY(gridPane1.getLayoutY() + gameView.getBackgroundStep());
        gridPane2.setLayoutY(gridPane2.getLayoutY() + gameView.getBackgroundStep());
        
        if (gridPane1.getLayoutY() == 768) {
            gridPane1.setLayoutY(-768);
        }
        
        if (gridPane2.getLayoutY() == 768) {
            gridPane2.setLayoutY(-768);
        }
    }
    
    private void moveBackground(int backgroundStep) {
        gridPane1.setLayoutY(gridPane1.getLayoutY() + backgroundStep);
        gridPane2.setLayoutY(gridPane2.getLayoutY() + backgroundStep);
        
        if (gridPane1.getLayoutY() == 768) {
            gridPane1.setLayoutY(-768);
        }
        
        if (gridPane2.getLayoutY() == 768) {
            gridPane2.setLayoutY(-768);
        }
    }
    
}
