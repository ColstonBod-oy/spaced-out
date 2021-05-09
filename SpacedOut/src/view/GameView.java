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
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MenuLabel;
import model.MenuSubScene;
import model.PanelButton;

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
    private MenuSubScene gameOverSubScene;
    private MenuSubScene levelClearedSubScene;
    private AnimationTimer asteroidsAnimationTimer;
    private Timeline shipTimeline;
    private Timeline starsTimeline;
    private Timeline explosionsTimeline;
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
    private ImageView[][] stars;
    private ImageView[] explosions;
    private double[][] asteroidsLeftValues;
    private double[][] asteroidsLeftEdgeValues;
    private double[][] asteroidsMiddleValues;
    private double[][] asteroidsRightValues;
    private double[][] asteroidsRightEdgeValues;
    private int shipAngle;
    private int asteroidsLeftIndex;
    private int asteroidsLeftEdgeIndex;
    private int asteroidsRightIndex;
    private int asteroidsRightEdgeIndex;
    private int asteroidsRandom;
    private int asteroidsRotationRandom;
    private int[] starsRandom;
    private int life;
    private int distanceTraveled;
    private int distanceGoal;
    private int starsStep;
    private int asteroidsStep;
    private Stage menuStage;
    private int chosenLevel;
    private PanelButton nextLevelButton;
    
    public GameView() {
        gameRandom = new Random();
        initStage();
        initKeyListeners();
    }
    
    private void initStage() {
        root = new AnchorPane();
        root.setStyle("-fx-background-color: #000;");
        gameScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gameStage.setScene(gameScene);
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
    
    public void createNewGame(Stage menuStage, int chosenLevel, PanelButton nextLevelButton) {
        this.menuStage = menuStage;
        this.chosenLevel = chosenLevel;
        this.nextLevelButton = nextLevelButton;
        this.menuStage.hide();
        gameStage.show();
        animateShip();
        animateStars();
        animateExplosions();
        createGameElements();
        createGameLoop();
        createGameOverScreen();
        createLevelClearedScreen();
    }
    
    private void createGameElements() {
        life = 10;
        distanceTraveled = 0;
        
        if (chosenLevel == 1) {
            distanceGoal = 100;
            starsStep = 2;
            asteroidsStep = 7;
        }
        
        else if (chosenLevel == 2) {
            distanceGoal = 200;
            starsStep = 5;
            asteroidsStep = 10;
        }
        
        else {
            distanceGoal = 250;
            starsStep = 8;
            asteroidsStep = 13;
        }
        
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
        ship.setLayoutY(GAME_HEIGHT - 120);
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
        asteroidsLeftIndex = 0;
        asteroidsLeftEdgeIndex = 0;
        asteroidsRightIndex = 0;
        asteroidsRightEdgeIndex = 0;
        
        for (int i = 0; i < asteroidsLeft.length; i++) {
            initAsteroids(i, asteroidsLeft, asteroidsLeftValues);
        }
        
        asteroidsLeft[0].setLayoutX(ThreadLocalRandom.current().nextInt(121, 241));
        asteroidsLeft[0].setLayoutY(703);
        root.getChildren().add(asteroidsLeft[0]);
        
        for (int i = 1; i < asteroidsLeft.length; i++) {
            generateAsteroidsLeftPosition(asteroidsLeft[i], asteroidsLeft, asteroidsLeftValues[i][2]);
            root.getChildren().add(asteroidsLeft[i]);
            asteroidsLeftIndex = i;
        }
        
        for (int i = 0; i < asteroidsLeftEdge.length; i++) {
            initAsteroids(i, asteroidsLeftEdge, asteroidsLeftEdgeValues);
        }
        
        asteroidsLeftEdge[0].setLayoutX(gameRandom.nextInt(121));
        asteroidsLeftEdge[0].setLayoutY(626.2);
        root.getChildren().add(asteroidsLeftEdge[0]);
        
        for (int i = 1; i < asteroidsLeftEdge.length; i++) {
            generateAsteroidsLeftEdgePosition(asteroidsLeftEdge[i], asteroidsLeftEdge, asteroidsLeftEdgeValues[i][2]);
            root.getChildren().add(asteroidsLeftEdge[i]);
            asteroidsLeftEdgeIndex = i;
        }
        
        for (int i = 0; i < asteroidsMiddle.length; i++) {
            initAsteroids(i, asteroidsMiddle, asteroidsMiddleValues);
            generateAsteroidsMiddlePosition(asteroidsMiddle[i], asteroidsMiddleValues[i][1]);
            root.getChildren().add(asteroidsMiddle[i]);
        }
        
        for (int i = 0; i < asteroidsRight.length; i++) {
            initAsteroids(i, asteroidsRight, asteroidsRightValues);
        }
        
        asteroidsRight[0].setLayoutX(ThreadLocalRandom.current().nextInt(709, (int) (829 - asteroidsRightValues[0][1] * 2)));
        asteroidsRight[0].setLayoutY(703);
        root.getChildren().add(asteroidsRight[0]);
        
        for (int i = 1; i < asteroidsRight.length; i++) {
            generateAsteroidsRightPosition(asteroidsRight[i], asteroidsRight, asteroidsRightValues[i][1], asteroidsRightValues[i][2]);
            root.getChildren().add(asteroidsRight[i]);
            asteroidsRightIndex = i;
        }
        
        for (int i = 0; i < asteroidsRightEdge.length; i++) {
            initAsteroids(i, asteroidsRightEdge, asteroidsRightEdgeValues);
        }
        
        asteroidsRightEdge[0].setLayoutX(ThreadLocalRandom.current().nextInt(829, (int) (1025 - asteroidsRightEdgeValues[0][1] * 2)));
        asteroidsRightEdge[0].setLayoutY(626.2);
        root.getChildren().add(asteroidsRightEdge[0]);
        
        for (int i = 1; i < asteroidsRightEdge.length; i++) {
            generateAsteroidsRightEdgePosition(asteroidsRightEdge[i], asteroidsRightEdge, asteroidsRightEdgeValues[i][1], asteroidsRightEdgeValues[i][2]);
            root.getChildren().add(asteroidsRightEdge[i]);
            asteroidsRightEdgeIndex = i;
        }
    }
    
    private void initAsteroids(int index, ImageView[] asteroids, double[][] asteroidsValues) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroids[index] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsValues[index][4] = asteroidsRotationRandom;
    }
    
    private void createExplosions() {
        explosions = new ImageView[4];
        
        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = new ImageView(EXPLOSION_SPRITE_IMAGES[0]);
            explosions[i].setLayoutY(-120);
            root.getChildren().add(explosions[i]);
        }
    }
    
    private void createGameLoop() {
        gameAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveShip();
                spawnStars();
                spawnAsteroids();
                checkCollisions();
                checkLevelCleared();
            }
        };
        
        asteroidsAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (int i = 0; i < asteroidsLeft.length; i++) {
                    if (asteroidsLeftValues[i][4] == 1) {
                        asteroidsLeft[i].setRotate(asteroidsLeft[i].getRotate() + 2);
                    }
                
                    else {
                        asteroidsLeft[i].setRotate(asteroidsLeft[i].getRotate() - 2);
                    }
                }
                
                for (int i = 0; i < asteroidsLeftEdge.length; i++) {
                    if (asteroidsLeftEdgeValues[i][4] == 1) {
                        asteroidsLeftEdge[i].setRotate(asteroidsLeftEdge[i].getRotate() + 2);
                    }
                
                    else {
                        asteroidsLeftEdge[i].setRotate(asteroidsLeftEdge[i].getRotate() - 2);
                    }
                }
                
                for (int i = 0; i < asteroidsMiddle.length; i++) {
                    if (asteroidsMiddleValues[i][4] == 1) {
                        asteroidsMiddle[i].setRotate(asteroidsMiddle[i].getRotate() + 2);
                    }
                
                    else {
                        asteroidsMiddle[i].setRotate(asteroidsMiddle[i].getRotate() - 2);
                    }
                }
                
                for (int i = 0; i < asteroidsRight.length; i++) {
                    if (asteroidsRightValues[i][4] == 1) {
                        asteroidsRight[i].setRotate(asteroidsRight[i].getRotate() + 2);
                    }
                
                    else {
                        asteroidsRight[i].setRotate(asteroidsRight[i].getRotate() - 2);
                    }
                }
                
                for (int i = 0; i < asteroidsRightEdge.length; i++) {
                    if (asteroidsRightEdgeValues[i][4] == 1) {
                        asteroidsRightEdge[i].setRotate(asteroidsRightEdge[i].getRotate() + 2);
                    }
                
                    else {
                        asteroidsRightEdge[i].setRotate(asteroidsRightEdge[i].getRotate() - 2);
                    }
                }
            }
        };
        
        gameAnimationTimer.start();
        asteroidsAnimationTimer.start();
    }
    
    private void createGameOverScreen() {
        gameOverSubScene = new MenuSubScene(400, 250, 259);
        root.getChildren().add(gameOverSubScene);
        
        MenuLabel dialogLabel = new MenuLabel("GAME OVER", 20, 250, 40, -2);
        dialogLabel.setLayoutX(75);
        dialogLabel.setLayoutY(18.625);
        gameOverSubScene.getPane().getChildren().add(dialogLabel);
        
        createRestartButton();
        createQuitButton();
    }
    
    private void createRestartButton() {
        PanelButton dialogButton = new PanelButton("RESTART", 14, 108, 108);
        dialogButton.setLayoutX(62);
        dialogButton.setLayoutY(95);
        gameOverSubScene.getPane().getChildren().add(dialogButton);
        
        dialogButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                gameOverSubScene.moveSubScene(-712);
                respawnShip();
            }
        });
    }
    
    private void createQuitButton() {
        PanelButton dialogButton = new PanelButton("QUIT", 14, 108, 108);
        dialogButton.setLayoutX(230);
        dialogButton.setLayoutY(95);
        gameOverSubScene.getPane().getChildren().add(dialogButton);
        
        dialogButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                shipTimeline.stop();
                starsTimeline.stop();
                asteroidsAnimationTimer.stop();
                gameStage.hide();
                menuStage.show();
            }
        });
    }
    
    private void createLevelClearedScreen() {
        levelClearedSubScene = new MenuSubScene(400, 250, 259);
        root.getChildren().add(levelClearedSubScene);
        
        if (chosenLevel == 3) {
            MenuLabel dialogLabel = new MenuLabel("LEVEL CLEARED\n\nESCAPED ASTEROID BELT\nTHANKS FOR PLAYING!", 20, 350, 140, -10);
            dialogLabel.setLayoutX(25);
            dialogLabel.setLayoutY(55);
            levelClearedSubScene.getPane().getChildren().add(dialogLabel);
        }
        
        else {
            MenuLabel dialogLabel = new MenuLabel("LEVEL CLEARED", 20, 250, 40, -2);
            dialogLabel.setLayoutX(75);
            dialogLabel.setLayoutY(105);
            levelClearedSubScene.getPane().getChildren().add(dialogLabel);
        }
    }
    
    private void spawnStars() {
        for (int i = 0; i < stars.length; i++) {
            if (stars[i][starsRandom[i]].getLayoutY() > 803) {
                starsRandom[i] = gameRandom.nextInt(stars[i].length);
                generateStarsPosition(stars[i][starsRandom[i]]);
                distanceTraveled += 1;
            }
            
            else {
                stars[i][starsRandom[i]].setLayoutY(stars[i][starsRandom[i]].getLayoutY() + starsStep);
            }
        }
    }
    
    private void spawnAsteroids() {
        for (int i = 0; i < asteroidsLeft.length; i++) {
            if (asteroidsLeft[i].getLayoutY() > 833) {
                if (chosenLevel == 3 && distanceTraveled >= distanceGoal) {
                    asteroidsLeft[i].setLayoutY(-1666);
                }
                
                else {
                    generateAsteroids(i, asteroidsLeft, asteroidsLeftValues);
                    generateAsteroidsLeftPosition(asteroidsLeft[i], asteroidsLeft, asteroidsLeftValues[i][2]);
                    asteroidsLeftIndex = i;
                }
            }
            
            else if (asteroidsLeft[i].getLayoutY() != -1666) {
                asteroidsLeft[i].setLayoutY(asteroidsLeft[i].getLayoutY() + asteroidsStep);
            }
        }
        
        for (int i = 0; i < asteroidsLeftEdge.length; i++) {
            if (asteroidsLeftEdge[i].getLayoutY() > 833) {
                if (chosenLevel == 3 && distanceTraveled >= distanceGoal) {
                    asteroidsLeftEdge[i].setLayoutY(-1666);
                }
                
                else {
                    generateAsteroids(i, asteroidsLeftEdge, asteroidsLeftEdgeValues);
                    generateAsteroidsLeftEdgePosition(asteroidsLeftEdge[i], asteroidsLeftEdge, asteroidsLeftEdgeValues[i][2]);
                    asteroidsLeftEdgeIndex = i;
                }
            }
            
            else if (asteroidsLeftEdge[i].getLayoutY() != -1666) {
                asteroidsLeftEdge[i].setLayoutY(asteroidsLeftEdge[i].getLayoutY() + asteroidsStep);
            }
        }
        
        for (int i = 0; i < asteroidsMiddle.length; i++) {
            if (asteroidsMiddle[i].getLayoutY() > 833) {
                if (distanceTraveled >= distanceGoal) {
                    asteroidsMiddle[i].setLayoutY(-1666);
                }
                
                else {
                    generateAsteroids(i, asteroidsMiddle, asteroidsMiddleValues);
                    generateAsteroidsMiddlePosition(asteroidsMiddle[i], asteroidsMiddleValues[i][1]);
                }
            }
            
            else if (asteroidsMiddle[i].getLayoutY() != -1666) {
                asteroidsMiddle[i].setLayoutY(asteroidsMiddle[i].getLayoutY() + asteroidsStep);
            }
        }
        
        for (int i = 0; i < asteroidsRight.length; i++) {
            if (asteroidsRight[i].getLayoutY() > 833) {
                if (chosenLevel == 3 && distanceTraveled >= distanceGoal) {
                    asteroidsRight[i].setLayoutY(-1666);
                }
                
                else {
                    generateAsteroids(i, asteroidsRight, asteroidsRightValues);
                    generateAsteroidsRightPosition(asteroidsRight[i], asteroidsRight, asteroidsRightValues[i][1], asteroidsRightValues[i][2]);
                    asteroidsRightIndex = i;
                }
            }
            
            else if (asteroidsRight[i].getLayoutY() != -1666) {
                asteroidsRight[i].setLayoutY(asteroidsRight[i].getLayoutY() + asteroidsStep);
            }
        }
        
        for (int i = 0; i < asteroidsRightEdge.length; i++) {
            if (asteroidsRightEdge[i].getLayoutY() > 833) {
                if (chosenLevel == 3 && distanceTraveled >= distanceGoal) {
                    asteroidsRightEdge[i].setLayoutY(-1666);
                }
                
                else {
                    generateAsteroids(i, asteroidsRightEdge, asteroidsRightEdgeValues);
                    generateAsteroidsRightEdgePosition(asteroidsRightEdge[i], asteroidsRightEdge, asteroidsRightEdgeValues[i][1], asteroidsRightEdgeValues[i][2]);
                    asteroidsRightEdgeIndex = i;
                }
            }
            
            else if (asteroidsRightEdge[i].getLayoutY() != -1666) {
                asteroidsRightEdge[i].setLayoutY(asteroidsRightEdge[i].getLayoutY() + asteroidsStep);
            }
        }
    }
    
    private void respawnShip() {
        life = 10;
        distanceTraveled = 0;
        shipAngle = 0;
        
        ship.setRotate(0);
        ship.setLayoutX(GAME_WIDTH / 2 - 37.5);
        ship.setLayoutY(GAME_HEIGHT);
        ship.setVisible(true);
        
        TranslateTransition shipTranslateTransition = new TranslateTransition(Duration.millis(2500), ship);
        shipTranslateTransition.setToY(-120);
        shipTranslateTransition.setOnFinished(e -> {
            shipTranslateTransition.stop();
            ship.setLayoutY(ship.getLayoutY() + ship.getTranslateY());
            ship.setTranslateY(0);
            gameAnimationTimer.start();
        });
        shipTranslateTransition.play();
    }
    
    private void generateAsteroids(int index, ImageView[] asteroids, double[][] asteroidsValues) {
        asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
        asteroidsRotationRandom = gameRandom.nextInt(2);
        asteroids[index].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
        asteroidsValues[index][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
        asteroidsValues[index][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
        asteroidsValues[index][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
        asteroidsValues[index][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
        asteroidsValues[index][4] = asteroidsRotationRandom;
    }
    
    private void generateExplosions(ImageView image, int damage) {
        switch (damage) {
            case 3:
                explode(explosions[2], image.getLayoutX(), image.getLayoutY());
                break;
                        
            case 10:
                explode(explosions[1], image.getLayoutX(), image.getLayoutY());
                break;
                    
            default:
                explode(explosions[3], image.getLayoutX(), image.getLayoutY());
        }
        
        explosionsTimeline.setOnFinished(e -> {
            for (ImageView explosion : explosions) {
                explosion.setLayoutY(-120);
            }
        });
    }
    
    private void generateStarsPosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(990));
        image.setLayoutY(- (gameRandom.nextInt(769) + 35));
    }
    
    private void generateAsteroidsLeftPosition(ImageView image, ImageView[] images, double halfHeight) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(121, 241));
        image.setLayoutY(images[asteroidsLeftIndex].getLayoutY() - (ThreadLocalRandom.current().nextInt(56, 76) + halfHeight * 2));
    }
    
    private void generateAsteroidsLeftEdgePosition(ImageView image, ImageView[] images, double halfHeight) {
        image.setLayoutX(gameRandom.nextInt(121));
        image.setLayoutY(images[asteroidsLeftEdgeIndex].getLayoutY() - (ThreadLocalRandom.current().nextInt(56, 76) + halfHeight * 2));
    }
    
    private void generateAsteroidsMiddlePosition(ImageView image, double halfWidth) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(241, (int) (710 - halfWidth * 2)));
        image.setLayoutY(- (gameRandom.nextInt(1601) + 65));
    }
    
    private void generateAsteroidsRightPosition(ImageView image, ImageView[] images, double halfWidth, double halfHeight) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(709, (int) (829 - halfWidth * 2)));
        image.setLayoutY(images[asteroidsRightIndex].getLayoutY() - (ThreadLocalRandom.current().nextInt(56, 76) + halfHeight * 2));
    }
    
    private void generateAsteroidsRightEdgePosition(ImageView image, ImageView[] images, double halfWidth, double halfHeight) {
        image.setLayoutX(ThreadLocalRandom.current().nextInt(829, (int) (1025 - halfWidth * 2)));
        image.setLayoutY(images[asteroidsRightEdgeIndex].getLayoutY() - (ThreadLocalRandom.current().nextInt(56, 76) + halfHeight * 2));
    }
    
    private void animateShip() {
        ship = new ImageView(SHIP_SPRITE_IMAGES[0]);
        shipTimeline = new Timeline();
        Collection<KeyFrame> frames = shipTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(25);
        
        for (Image img : SHIP_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> ship.setImage(img)));
        }
        
        shipTimeline.setAutoReverse(true);
        shipTimeline.setCycleCount(Animation.INDEFINITE);
        shipTimeline.play();
    }
    
    private void animateStars() {
        starsTimeline = new Timeline();
        Collection<KeyFrame> frames = starsTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(100);
        
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
    
    private void animateExplosions() {
        explosionsTimeline = new Timeline();
        Collection<KeyFrame> frames = explosionsTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(60);
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                explosions[0].setImage(img);
                explosions[0].setFitWidth(120);
                explosions[0].setFitHeight(120);
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> explosions[1].setImage(img)));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                explosions[2].setImage(img);
                explosions[2].setFitWidth(30);
                explosions[2].setFitHeight(30);
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : EXPLOSION_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> {
                explosions[3].setImage(img);
                explosions[3].setFitWidth(20);
                explosions[3].setFitHeight(20);
            }));
        }
        
        explosionsTimeline.setCycleCount(1);
        explosionsTimeline.play();
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
                if (ship.getLayoutY() + starsStep > 648) {
                    ship.setLayoutY(648);
                }
                
                else {
                    ship.setLayoutY(ship.getLayoutY() + starsStep);
                }
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
                generateExplosions(asteroidsLeft[i], (int) asteroidsLeftValues[i][3]);
                dealDamage(asteroidsLeftValues[i][3]);
                
                generateAsteroids(i, asteroidsLeft, asteroidsLeftValues);
                generateAsteroidsLeftPosition(asteroidsLeft[i], asteroidsLeft, asteroidsLeftValues[i][2]);
                asteroidsLeftIndex = i;
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
                generateExplosions(asteroidsLeftEdge[i], (int) asteroidsLeftEdgeValues[i][3]);
                dealDamage(asteroidsLeftEdgeValues[i][3]);
                
                generateAsteroids(i, asteroidsLeftEdge, asteroidsLeftEdgeValues);
                generateAsteroidsLeftEdgePosition(asteroidsLeftEdge[i], asteroidsLeftEdge, asteroidsLeftEdgeValues[i][2]);
                asteroidsLeftEdgeIndex = i;
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
                generateExplosions(asteroidsMiddle[i], (int) asteroidsMiddleValues[i][3]);
                dealDamage(asteroidsMiddleValues[i][3]);
                
                generateAsteroids(i, asteroidsMiddle, asteroidsMiddleValues);
                generateAsteroidsMiddlePosition(asteroidsMiddle[i], asteroidsMiddleValues[i][1]);
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
                generateExplosions(asteroidsRight[i], (int) asteroidsRightValues[i][3]);
                dealDamage(asteroidsRightValues[i][3]);
                
                generateAsteroids(i, asteroidsRight, asteroidsRightValues);
                generateAsteroidsRightPosition(asteroidsRight[i], asteroidsRight, asteroidsRightValues[i][1], asteroidsRightValues[i][2]);
                asteroidsRightIndex = i;
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
                generateExplosions(asteroidsRightEdge[i], (int) asteroidsRightEdgeValues[i][3]);
                dealDamage(asteroidsRightEdgeValues[i][3]);
                
                generateAsteroids(i, asteroidsRightEdge, asteroidsRightEdgeValues);
                generateAsteroidsRightEdgePosition(asteroidsRightEdge[i], asteroidsRightEdge, asteroidsRightEdgeValues[i][1], asteroidsRightEdgeValues[i][2]);
                asteroidsRightEdgeIndex = i;
            }
        }
    }
    
    private void checkLevelCleared() {
        boolean levelCleared = true;
        
        for (ImageView asteroid : asteroidsMiddle) {
            if (asteroid.getLayoutY() != -1666) {
                levelCleared = false;
            }
        }
        
        if (chosenLevel == 3) {
            for (ImageView asteroid : asteroidsLeft) {
                if (asteroid.getLayoutY() != -1666) {
                    levelCleared = false;
                }
            }
            
            for (ImageView asteroid : asteroidsLeftEdge) {
                if (asteroid.getLayoutY() != -1666) {
                    levelCleared = false;
                }
            }
            
            for (ImageView asteroid : asteroidsRight) {
                if (asteroid.getLayoutY() != -1666) {
                    levelCleared = false;
                }
            }
            
            for (ImageView asteroid : asteroidsRightEdge) {
                if (asteroid.getLayoutY() != -1666) {
                    levelCleared = false;
                }
            }
        }
        
        if (levelCleared) {
            gameAnimationTimer.stop();
            
            PauseTransition delay = new PauseTransition(Duration.millis(1500));
            delay.setOnFinished(e -> {
                shipTimeline.stop();
                starsTimeline.stop();
                asteroidsAnimationTimer.stop();
                nextLevelButton.setDisable(false);
                gameStage.hide();
                menuStage.show();
            });
            
            levelClearedSubScene.moveSubScene(-712);
            levelClearedSubScene.getTranslateTransition().setOnFinished(e -> delay.play());
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
            ship.setVisible(false);
            gameAnimationTimer.stop();
            
            explode(explosions[0], ship.getLayoutX(), ship.getLayoutY());
            explosionsTimeline.setOnFinished(e -> {
                for (ImageView explosion : explosions) {
                    explosion.setLayoutY(-120);
                }
                
                gameOverSubScene.moveSubScene(-712);
            });
        }
    }
    
    private double calculateDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)); // Pythagorean theorem
    }
    
}
