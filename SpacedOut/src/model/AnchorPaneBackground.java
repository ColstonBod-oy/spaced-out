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
import javafx.util.Duration;
import view.GameView;

/**
 *
 * @author user
 */
public class AnchorPaneBackground {
    
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
    private ImageView[][] stars;
    private int[] starsRandom;
    private GameView gameView;
    
    public AnchorPaneBackground() {
        backgroundRandom = new Random();
    }
    
    public void startBackgroundAnimationTimer() {
        backgroundAnimationTimer.start();
    }
    
    public void stopBackgroundAnimationTimer() {
        backgroundAnimationTimer.stop();
    }
    
    public void stopStarsTimeline() {
        starsTimeline.stop();
    }
    
    public void createNewBackground(GameView gameView) {
        this.gameView = gameView;
        createStars();
        createBackgroundLoop();
        animateStars();
    }
    
    private void createStars() {
        stars = new ImageView[20][2];
        starsRandom = new int[20];
        
        for (int i = 0; i < stars.length; i++) {
            stars[i][0] = new ImageView(BLUE_STAR_SPRITE_IMAGES[0]);
            stars[i][0].setLayoutY(-35);
            gameView.getPane().getChildren().add(stars[i][0]);
            
            stars[i][1] = new ImageView(WHITE_STAR_SPRITE_IMAGES[0]);
            stars[i][1].setLayoutY(-35);
            gameView.getPane().getChildren().add(stars[i][1]);
            
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
    
    private void createBackgroundLoop() {
        backgroundAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                spawnStars();
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
    
}
