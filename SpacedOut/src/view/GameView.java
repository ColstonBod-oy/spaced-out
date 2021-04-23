/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class GameView {
    
    private static final int GAME_WIDTH = 1024;
    private static final int GAME_HEIGHT = 768;
    private static final Image[] SHIP_SPRITE_IMAGES = {
        new Image("view/assets/sprites/sprite_ship0.png"),
        new Image("view/assets/sprites/sprite_ship1.png")
    };
    private static final Image[] ASTEROID_SPRITE_IMAGES = {
        new Image("view/assets/sprites/sprite_asteroid0.png"),
        new Image("view/assets/sprites/sprite_asteroid1.png"),
        new Image("view/assets/sprites/sprite_asteroid2.png"),
        new Image("view/assets/sprites/sprite_asteroid3.png")
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
    private static final Image[] EXPLOSION_SPRITE_IMAGES = {
        new Image("view/assets/sprites/sprite_explosion0.png"),
        new Image("view/assets/sprites/sprite_explosion1.png"),
        new Image("view/assets/sprites/sprite_explosion2.png"),
        new Image("view/assets/sprites/sprite_explosion3.png")
    };
    
    // {Radius, half-width, half-height, damage}
    private static final double[][] ASTEROID_SPRITE_VALUES = { {32.5, 37.5, 32.5, 10}, {15, 15, 15, 3}, {10, 10, 12.5, 2}, {10, 10, 10, 1} };
    private static final double SHIP_SPRITE_RADIUS = 37.5;
    private AnchorPane root;
    private Scene gameScene;
    private Stage gameStage;
    private AnimationTimer gameAnimationTimer;
    private Random gameRandom;
    private Timeline shipTimeline;
    private Timeline starsTimeline;
    private Timeline explosionsTimeline;
    private Collection<KeyFrame> shipFrames;
    private Collection<KeyFrame> starsFrames;
    private Collection<KeyFrame> explosionsFrames;
    private Duration frameTime;
    private Duration frameGap;
    private boolean isAKeyPressed;
    private boolean isSKeyPressed;
    private boolean isDKeyPressed;
    private boolean isFKeyPressed;
    private ImageView ship;
    private ImageView[] asteroidsLeft;
    private ImageView[] asteroidsLeftEdge;
    private ImageView[] asteroidsMiddle;
    private ImageView[] asteroidsRight;
    private ImageView[] asteroidsRightEdge;
    private double[][] asteroidsLeftValues;
    private double[][] asteroidsLeftEdgeValues;
    private double[][] asteroidsMiddleValues;
    private double[][] asteroidsRightValues;
    private double[][] asteroidsRightEdgeValues;
    private ImageView[][] stars;
    private ImageView[] explosions;
    private int shipAngle;
    private int asteroidsRandom;
    private int asteroidsLeftGap;
    private int asteroidsLeftEdgeGap;
    private int asteroidsRightGap;
    private int asteroidsRightEdgeGap;
    private int asteroidsRotationRandom;
    private int[] starsRandom;
    private int life;
    private int distanceTraveled;
    private Stage menuStage;
    private int chosenLevel;
    
    public GameView() {
        gameRandom = new Random();
        initStage();
        initKeyListeners();
    }
    
    private void initKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (null != t.getCode()) switch (t.getCode()) {
                    case A:
                        isAKeyPressed = true;
                        break;
                        
                    case S:
                        isSKeyPressed = true;
                        break;
                        
                    case D:
                        isDKeyPressed = true;
                        break;
                        
                    case F:
                        isFKeyPressed = true;
                }
            }
        });
        
        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (null != t.getCode()) switch (t.getCode()) {
                    case A:
                        isAKeyPressed = false;
                        break;
                        
                    case S:
                        isSKeyPressed = false;
                        break;
                        
                    case D:
                        isDKeyPressed = false;
                        break;
                        
                    case F:
                        isFKeyPressed = false;
                }
            }
        });
    }
    
    private void initStage() {
        root = new AnchorPane();
        root.setStyle("-fx-background-color: #000;");
        gameScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
    }
    
    public void createNewGame(Stage menuStage, int chosenLevel) {
        this.menuStage = menuStage;
        this.chosenLevel = chosenLevel;
        this.menuStage.hide();
        gameStage.show();
        animateShip();
        animateStars();
        animateExplosions();
        createGameElements();
        createGameLoop();
    }
    
    private void createGameElements() {
        life = 10;
        distanceTraveled = 0;
        
        createStars();
        createShip();
        createAsteroids();
        createExplosions();
    }
    
    private void createStars() {
        stars = new ImageView[20][2];
        starsRandom = new int[20];
        
        for (int i = 0; i < stars.length; i++) {
            stars[i][0] = new ImageView(BLUE_STAR_SPRITE_IMAGES[0]);
            stars[i][0].setLayoutY(-35);
            root.getChildren().add(stars[i][0]);
            
            stars[i][1] = new ImageView(WHITE_STAR_SPRITE_IMAGES[0]);
            stars[i][1].setLayoutY(-35);
            root.getChildren().add(stars[i][1]);
            
            starsRandom[i] = gameRandom.nextInt(stars[i].length);
            stars[i][starsRandom[i]].setLayoutX(gameRandom.nextInt(990));
            stars[i][starsRandom[i]].setLayoutY(gameRandom.nextInt(769) - 35);
        }
    }
    
    private void createShip() {
        ship.setLayoutX(GAME_WIDTH / 2 - 37.5);
        ship.setLayoutY(GAME_HEIGHT - 130);
        root.getChildren().add(ship);
    }
    
    private void createAsteroids() {
        asteroidsLeft = new ImageView[10];
        asteroidsLeftValues = new double[10][5];
        asteroidsLeftEdge = new ImageView[10];
        asteroidsLeftEdgeValues = new double[10][5];
        asteroidsMiddle = new ImageView[10];
        asteroidsMiddleValues = new double[10][5];
        asteroidsRight = new ImageView[10];
        asteroidsRightValues = new double[10][5];
        asteroidsRightEdge = new ImageView[10];
        asteroidsRightEdgeValues = new double[10][5];
        asteroidsLeftGap = -704;
        asteroidsLeftEdgeGap = -748;
        asteroidsRightGap = -704;
        asteroidsRightEdgeGap = -748;
        
        for (int i = 0; i < asteroidsLeft.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroidsRotationRandom = gameRandom.nextInt(2);
            asteroidsLeft[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsLeftValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsLeftValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsLeftValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsLeftValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            asteroidsLeftValues[i][4] = asteroidsRotationRandom;
            generateAsteroidsLeftPosition(asteroidsLeft[i]);
            root.getChildren().add(asteroidsLeft[i]);
            asteroidsLeftGap += 88;
        }
        
        for (int i = 0; i < asteroidsLeftEdge.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroidsRotationRandom = gameRandom.nextInt(2);
            asteroidsLeftEdge[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsLeftEdgeValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsLeftEdgeValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsLeftEdgeValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsLeftEdgeValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            asteroidsLeftEdgeValues[i][4] = asteroidsRotationRandom;
            generateAsteroidsLeftEdgePosition(asteroidsLeftEdge[i]);
            root.getChildren().add(asteroidsLeftEdge[i]);
            asteroidsLeftEdgeGap += 88;
        }
        
        for (int i = 0; i < asteroidsMiddle.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroidsRotationRandom = gameRandom.nextInt(2);
            asteroidsMiddle[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsMiddleValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsMiddleValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsMiddleValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsMiddleValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            asteroidsMiddleValues[i][4] = asteroidsRotationRandom;
            generateAsteroidsMiddlePosition(asteroidsMiddle[i], asteroidsMiddleValues[i][1]);
            root.getChildren().add(asteroidsMiddle[i]);
        }
        
        for (int i = 0; i < asteroidsRight.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroidsRotationRandom = gameRandom.nextInt(2);
            asteroidsRight[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsRightValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsRightValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsRightValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsRightValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            asteroidsRightValues[i][4] = asteroidsRotationRandom;
            generateAsteroidsRightPosition(asteroidsRight[i], asteroidsRightValues[i][1]);
            root.getChildren().add(asteroidsRight[i]);
            asteroidsRightGap += 88;
        }
        
        for (int i = 0; i < asteroidsRightEdge.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroidsRotationRandom = gameRandom.nextInt(2);
            asteroidsRightEdge[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsRightEdgeValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsRightEdgeValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsRightEdgeValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsRightEdgeValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            asteroidsRightEdgeValues[i][4] = asteroidsRotationRandom;
            generateAsteroidsRightEdgePosition(asteroidsRightEdge[i], asteroidsRightEdgeValues[i][1]);
            root.getChildren().add(asteroidsRightEdge[i]);
            asteroidsRightEdgeGap += 88;
        }
    }
    
    private void createExplosions() {
        explosions = new ImageView[4];
        
        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = new ImageView(EXPLOSION_SPRITE_IMAGES[0]);
            explosions[i].setLayoutY(-120);
            root.getChildren().add(explosions[i]);
        }
    }
    
    private void generateStars() {
        for (int i = 0; i < stars.length; i++) {
            if (stars[i][starsRandom[i]].getLayoutY() > 803) {
                starsRandom[i] = gameRandom.nextInt(stars[i].length);
                generateStarsPosition(stars[i][starsRandom[i]]);
            }
            
            else {
                stars[i][starsRandom[i]].setLayoutY(stars[i][starsRandom[i]].getLayoutY() + 2);
            }
        }
    }
    
    private void generateAsteroids() {
        asteroidsLeftGap = 44;
        asteroidsLeftEdgeGap = 88;
        asteroidsRightGap = 44;
        asteroidsRightEdgeGap = 88;
        
        for (int i = 0; i < asteroidsLeft.length; i++) {
            if (asteroidsLeft[i].getLayoutY() > 833) {
                respawnAsteroidsLeft(i);
            }
            
            else {
                asteroidsLeft[i].setLayoutY(asteroidsLeft[i].getLayoutY() + 7);
                
                if (asteroidsLeftValues[i][4] == 1) {
                    asteroidsLeft[i].setRotate(asteroidsLeft[i].getRotate() + 4);
                }
                
                else {
                    asteroidsLeft[i].setRotate(asteroidsLeft[i].getRotate() - 4);
                }
            }
        }
        
        for (int i = 0; i < asteroidsLeftEdge.length; i++) {
            if (asteroidsLeftEdge[i].getLayoutY() > 833) {
                respawnAsteroidsLeftEdge(i);
            }
            
            else {
                asteroidsLeftEdge[i].setLayoutY(asteroidsLeftEdge[i].getLayoutY() + 7);
                
                if (asteroidsLeftEdgeValues[i][4] == 1) {
                    asteroidsLeftEdge[i].setRotate(asteroidsLeftEdge[i].getRotate() + 4);
                }
                
                else {
                    asteroidsLeftEdge[i].setRotate(asteroidsLeftEdge[i].getRotate() - 4);
                }
            }
        }
        
        for (int i = 0; i < asteroidsMiddle.length; i++) {
            if (asteroidsMiddle[i].getLayoutY() > 833) {
                respawnAsteroidsMiddle(i);
            }
            
            else {
                asteroidsMiddle[i].setLayoutY(asteroidsMiddle[i].getLayoutY() + 7);
                
                if (asteroidsMiddleValues[i][4] == 1) {
                    asteroidsMiddle[i].setRotate(asteroidsMiddle[i].getRotate() + 4);
                }
                
                else {
                    asteroidsMiddle[i].setRotate(asteroidsMiddle[i].getRotate() - 4);
                }
            }
        }
        
        for (int i = 0; i < asteroidsRight.length; i++) {
            if (asteroidsRight[i].getLayoutY() > 833) {
                respawnAsteroidsRight(i);
            }
            
            else {
                asteroidsRight[i].setLayoutY(asteroidsRight[i].getLayoutY() + 7);
                
                if (asteroidsRightValues[i][4] == 1) {
                    asteroidsRight[i].setRotate(asteroidsRight[i].getRotate() + 4);
                }
                
                else {
                    asteroidsRight[i].setRotate(asteroidsRight[i].getRotate() - 4);
                }
            }
        }
        
        for (int i = 0; i < asteroidsRightEdge.length; i++) {
            if (asteroidsRightEdge[i].getLayoutY() > 833) {
                respawnAsteroidsRightEdge(i);
            }
            
            else {
                asteroidsRightEdge[i].setLayoutY(asteroidsRightEdge[i].getLayoutY() + 7);
                
                if (asteroidsRightEdgeValues[i][4] == 1) {
                    asteroidsRightEdge[i].setRotate(asteroidsRightEdge[i].getRotate() + 4);
                }
                
                else {
                    asteroidsRightEdge[i].setRotate(asteroidsRightEdge[i].getRotate() - 4);
                }
            }
        }
    }
    
    private void respawnAsteroidsLeft(int index) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroidsLeft[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsLeftValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsLeftValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsLeftValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsLeftValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsLeftValues[index][4] = asteroidsRotationRandom;
        generateAsteroidsLeftPosition(asteroidsLeft[index]);
        asteroidsLeftGap += 88;
    }
    
    private void respawnAsteroidsLeftEdge(int index) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroidsLeftEdge[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsLeftEdgeValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsLeftEdgeValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsLeftEdgeValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsLeftEdgeValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsLeftEdgeValues[index][4] = asteroidsRotationRandom;
        generateAsteroidsLeftEdgePosition(asteroidsLeftEdge[index]);
        asteroidsLeftEdgeGap += 88;
    }
    
    private void respawnAsteroidsMiddle(int index) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroidsMiddle[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsMiddleValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsMiddleValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsMiddleValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsMiddleValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsMiddleValues[index][4] = asteroidsRotationRandom;
        generateAsteroidsMiddlePosition(asteroidsMiddle[index], asteroidsMiddleValues[index][1]);
    }
    
    private void respawnAsteroidsRight(int index) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroidsRight[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsRightValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsRightValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsRightValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsRightValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsRightValues[index][4] = asteroidsRotationRandom;
        generateAsteroidsRightPosition(asteroidsRight[index], asteroidsRightValues[index][1]);
        asteroidsRightGap += 88;
    }
    
    private void respawnAsteroidsRightEdge(int index) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroidsRightEdge[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsRightEdgeValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsRightEdgeValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsRightEdgeValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsRightEdgeValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsRightEdgeValues[index][4] = asteroidsRotationRandom;
        generateAsteroidsRightEdgePosition(asteroidsRightEdge[index], asteroidsRightEdgeValues[index][1]);
        asteroidsRightEdgeGap += 88;
    }
    
    private void generateStarsPosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(990));
        image.setLayoutY(- (gameRandom.nextInt(769) + 35));
    }
    
    private void generateAsteroidsLeftPosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(241));
        image.setLayoutY(-asteroidsLeftGap);
    }
    
    private void generateAsteroidsLeftEdgePosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(121));
        image.setLayoutY(-asteroidsLeftEdgeGap);
    }
    
    private void generateAsteroidsMiddlePosition(ImageView image, double halfWidth) {
        image.setLayoutX(gameRandom.nextInt((int) (1025 - halfWidth * 2)));
        image.setLayoutY(- (gameRandom.nextInt(1601) + 65));
    }
    
    private void generateAsteroidsRightPosition(ImageView image, double halfWidth) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(709, (int) (1025 - halfWidth * 2)));
        image.setLayoutY(-asteroidsRightGap);
    }
    
    private void generateAsteroidsRightEdgePosition(ImageView image, double halfWidth) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(829, (int) (1025 - halfWidth * 2)));
        image.setLayoutY(-asteroidsRightEdgeGap);
    }
    
    private void animateShip() {
        ship = new ImageView(SHIP_SPRITE_IMAGES[0]);
        shipTimeline = new Timeline();
        shipFrames = shipTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(25);
        
        for (Image img : SHIP_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            shipFrames.add(new KeyFrame(frameTime, e -> ship.setImage(img)));
        }
        
        shipTimeline.setAutoReverse(true);
        shipTimeline.setCycleCount(Animation.INDEFINITE);
        shipTimeline.play();
    }
    
    private void animateStars() {
        starsTimeline = new Timeline();
        starsFrames = starsTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(100);
        
        for (Image img : BLUE_STAR_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            starsFrames.add(new KeyFrame(frameTime, e -> {
                for (ImageView[] star : stars) {
                    star[0].setImage(img);
                }
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : WHITE_STAR_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            starsFrames.add(new KeyFrame(frameTime, e -> {
                for (ImageView[] star : stars) {
                    star[1].setImage(img);
                }
            }));
        }
        
        starsTimeline.setAutoReverse(true);
        starsTimeline.setCycleCount(Animation.INDEFINITE);
        starsTimeline.play();
    }
    
    private void animateExplosions() {
        explosionsTimeline = new Timeline();
        explosionsFrames = explosionsTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(60);
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            explosionsFrames.add(new KeyFrame(frameTime, e -> {
                explosions[0].setImage(img);
                explosions[0].setFitWidth(120);
                explosions[0].setFitHeight(120);
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            explosionsFrames.add(new KeyFrame(frameTime, e -> explosions[1].setImage(img)));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            explosionsFrames.add(new KeyFrame(frameTime, e -> {
                explosions[2].setImage(img);
                explosions[2].setFitWidth(30);
                explosions[2].setFitHeight(30);
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            explosionsFrames.add(new KeyFrame(frameTime, e -> {
                explosions[3].setImage(img);
                explosions[3].setFitWidth(20);
                explosions[3].setFitHeight(20);
            }));
        }
        
        explosionsTimeline.setCycleCount(1);
        explosionsTimeline.play();
    }
    
    private void createGameLoop() {
        gameAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveShip();
                generateStars();
                generateAsteroids();
                checkCollisions();
            }
        };
        
        gameAnimationTimer.start();
    }
    
    private void moveShip() {
        if (isAKeyPressed && !isSKeyPressed && !isDKeyPressed && !isFKeyPressed) {
            if (shipAngle > -30) {
                shipAngle -= 5;
            }
            
            else if (shipAngle < -30) {
                shipAngle += 5;
            }
            
            ship.setRotate(shipAngle);
            
            if (ship.getLayoutX() > 0) {
                ship.setLayoutX(ship.getLayoutX() - 4);
            }
            
            if (ship.getLayoutY() > 0) {
                ship.setLayoutY(ship.getLayoutY() - 4);
            }
        }
        
        else if (!isAKeyPressed && isSKeyPressed && !isDKeyPressed && !isFKeyPressed) {
            if (shipAngle < 30) {
                shipAngle += 5;
            }
            
            else if (shipAngle > 30) {
                shipAngle -= 5;
            }
            
            ship.setRotate(shipAngle);
            
            if (ship.getLayoutX() < 949) {
                ship.setLayoutX(ship.getLayoutX() + 4);
            }
            
            if (ship.getLayoutY() > 0) {
                ship.setLayoutY(ship.getLayoutY() - 4);
            }
        }
        
        else if (!isAKeyPressed && !isSKeyPressed && isDKeyPressed && !isFKeyPressed) {
            if (shipAngle > -150) {
                shipAngle -= 5;
            }
            
            ship.setRotate(shipAngle);
            
            if (ship.getLayoutX() > 0) {
                ship.setLayoutX(ship.getLayoutX() - 4);
            }
            
            if (ship.getLayoutY() < 648) {
                ship.setLayoutY(ship.getLayoutY() + 4);
            }
        }
        
        else if (!isAKeyPressed && !isSKeyPressed && !isDKeyPressed && isFKeyPressed) {
            if (shipAngle < 150) {
                shipAngle += 5;
            }
            
            ship.setRotate(shipAngle);
            
            if (ship.getLayoutX() < 949) {
                ship.setLayoutX(ship.getLayoutX() + 4);
            }
            
            if (ship.getLayoutY() < 648) {
                ship.setLayoutY(ship.getLayoutY() + 4);
            }
        }
        
        else {
            if (shipAngle > 0) {
                shipAngle -= 5;
            }
            
            else if (shipAngle < 0) {
                shipAngle += 5;
            }
            
            ship.setRotate(shipAngle);
            
            if (ship.getLayoutY() < 648) {
                ship.setLayoutY(ship.getLayoutY() + 2);
            }
        }
    }
    
    private void checkCollisions() {
        for (int i = 0; i < asteroidsLeft.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsLeftValues[i][0] > calculateDistance(
                    asteroidsLeft[i].getLayoutX() + asteroidsLeftValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroidsLeft[i].getLayoutY() + asteroidsLeftValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsLeftValues[i][3]) {
                    case 3:
                        explode(explosions[2], asteroidsLeft[i].getLayoutX(), asteroidsLeft[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 10:
                        explode(explosions[1], asteroidsLeft[i].getLayoutX(), asteroidsLeft[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroidsLeft[i].getLayoutX(), asteroidsLeft[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsLeftValues[i][3]);
                respawnAsteroidsLeft(i);
            }
        }
        
        for (int i = 0; i < asteroidsLeftEdge.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsLeftEdgeValues[i][0] > calculateDistance(
                    asteroidsLeftEdge[i].getLayoutX() + asteroidsLeftEdgeValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroidsLeftEdge[i].getLayoutY() + asteroidsLeftEdgeValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsLeftEdgeValues[i][3]) {
                    case 3:
                        explode(explosions[2], asteroidsLeftEdge[i].getLayoutX(), asteroidsLeftEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 10:
                        explode(explosions[1], asteroidsLeftEdge[i].getLayoutX(), asteroidsLeftEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroidsLeftEdge[i].getLayoutX(), asteroidsLeftEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsLeftEdgeValues[i][3]);
                respawnAsteroidsLeftEdge(i);
            }
        }
        
        for (int i = 0; i < asteroidsMiddle.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsMiddleValues[i][0] > calculateDistance(
                    asteroidsMiddle[i].getLayoutX() + asteroidsMiddleValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroidsMiddle[i].getLayoutY() + asteroidsMiddleValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsMiddleValues[i][3]) {
                    case 3:
                        explode(explosions[2], asteroidsMiddle[i].getLayoutX(), asteroidsMiddle[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 10:
                        explode(explosions[1], asteroidsMiddle[i].getLayoutX(), asteroidsMiddle[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroidsMiddle[i].getLayoutX(), asteroidsMiddle[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsMiddleValues[i][3]);
                respawnAsteroidsMiddle(i);
            }
        }
        
        for (int i = 0; i < asteroidsRight.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsRightValues[i][0] > calculateDistance(
                    asteroidsRight[i].getLayoutX() + asteroidsRightValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroidsRight[i].getLayoutY() + asteroidsRightValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsRightValues[i][3]) {
                    case 3:
                        explode(explosions[2], asteroidsRight[i].getLayoutX(), asteroidsRight[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 10:
                        explode(explosions[1], asteroidsRight[i].getLayoutX(), asteroidsRight[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroidsRight[i].getLayoutX(), asteroidsRight[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsRightValues[i][3]);
                respawnAsteroidsRight(i);
            }
        }
        
        for (int i = 0; i < asteroidsRightEdge.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsRightEdgeValues[i][0] > calculateDistance(
                    asteroidsRightEdge[i].getLayoutX() + asteroidsRightEdgeValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroidsRightEdge[i].getLayoutY() + asteroidsRightEdgeValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsRightEdgeValues[i][3]) {
                    case 3:
                        explode(explosions[2], asteroidsRightEdge[i].getLayoutX(), asteroidsRightEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 10:
                        explode(explosions[1], asteroidsRightEdge[i].getLayoutX(), asteroidsRightEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroidsRightEdge[i].getLayoutX(), asteroidsRightEdge[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsRightEdgeValues[i][3]);
                respawnAsteroidsRightEdge(i);
            }
        }
    }
    
    private void explode(ImageView image, double x, double y) {
        image.setLayoutX(x);
        image.setLayoutY(y);
        explosionsTimeline.playFromStart();
    }
    
    private void dealDamage(double damage) {
        life -= damage;
        
        if (life <= 0) {
            root.getChildren().remove(ship);
            explode(explosions[0], ship.getLayoutX(), ship.getLayoutY());
            explosionsTimeline.setOnFinished(e -> {
                gameStage.close();
                shipTimeline.stop();
                starsTimeline.stop();
                gameAnimationTimer.stop();
                menuStage.show();
            });
        }
    }
    
    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)); // Pythagorean theorem
    }
    
}
