/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Collection;
import java.util.Random;
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
    private static final double SHIP_SPRITE_RADIUS = 37.5;
    private static final double[][] ASTEROID_SPRITE_VALUES = { {32.5, 37.5, 32.5, 5}, {15, 15, 15, 4}, {10, 10, 12.5, 3}, {10, 10, 10, 2} };
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
    private ImageView[] asteroids;
    private double[][] asteroidsValues;
    private ImageView[] stars = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] starsCopy1 = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] starsCopy2 = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] starsCopy3 = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] starsCopy4 = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] starsCopy5 = {
        new ImageView(BLUE_STAR_SPRITE_IMAGES[0]),
        new ImageView(WHITE_STAR_SPRITE_IMAGES[0])
    };
    private ImageView[] explosions = {
        new ImageView(EXPLOSION_SPRITE_IMAGES[0]),
        new ImageView(EXPLOSION_SPRITE_IMAGES[0]),
        new ImageView(EXPLOSION_SPRITE_IMAGES[0]),
        new ImageView(EXPLOSION_SPRITE_IMAGES[0])
    };
    private int shipAngle;
    private int asteroidsRandom;
    private int starsRandom;
    private int starsCopy1Random;
    private int starsCopy2Random;
    private int starsCopy3Random;
    private int starsCopy4Random;
    private int starsCopy5Random;
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
        stars[0].setLayoutY(-35);
        root.getChildren().add(stars[0]);
        
        stars[1].setLayoutY(-35);
        root.getChildren().add(stars[1]);
        
        starsCopy1[0].setLayoutY(-35);
        root.getChildren().add(starsCopy1[0]);
        
        starsCopy1[1].setLayoutY(-35);
        root.getChildren().add(starsCopy1[1]);
        
        starsCopy2[0].setLayoutY(-35);
        root.getChildren().add(starsCopy2[0]);
        
        starsCopy2[1].setLayoutY(-35);
        root.getChildren().add(starsCopy2[1]);
        
        starsCopy3[0].setLayoutY(-35);
        root.getChildren().add(starsCopy3[0]);
        
        starsCopy3[1].setLayoutY(-35);
        root.getChildren().add(starsCopy3[1]);
        
        starsCopy4[0].setLayoutY(-35);
        root.getChildren().add(starsCopy4[0]);
        
        starsCopy4[1].setLayoutY(-35);
        root.getChildren().add(starsCopy4[1]);
        
        starsCopy5[0].setLayoutY(-35);
        root.getChildren().add(starsCopy5[0]);
        
        starsCopy5[1].setLayoutY(-35);
        root.getChildren().add(starsCopy5[1]);
        
        starsRandom = gameRandom.nextInt(stars.length);
        generateStarsPosition(stars[starsRandom]);
        
        starsCopy1Random = gameRandom.nextInt(starsCopy1.length);
        generateStarsPosition(starsCopy1[starsCopy1Random]);
        
        starsCopy2Random = gameRandom.nextInt(starsCopy2.length);
        generateStarsPosition(starsCopy2[starsCopy2Random]);
        
        starsCopy3Random = gameRandom.nextInt(starsCopy3.length);
        generateStarsPosition(starsCopy3[starsCopy3Random]);
        
        starsCopy4Random = gameRandom.nextInt(starsCopy4.length);
        generateStarsPosition(starsCopy4[starsCopy4Random]);
        
        starsCopy5Random = gameRandom.nextInt(starsCopy5.length);
        generateStarsPosition(starsCopy5[starsCopy5Random]);
    }
    
    private void createShip() {
        ship.setLayoutX(GAME_WIDTH / 2 - 37.5);
        ship.setLayoutY(GAME_HEIGHT - 130);
        root.getChildren().add(ship);
    }
    
    private void createAsteroids() {
        asteroids = new ImageView[3];
        asteroidsValues = new double[3][4];
        
        for (int i = 0; i < asteroids.length; i++) {
            asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
            asteroids[i] = new ImageView(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
            asteroidsValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
            asteroidsValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
            asteroidsValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
            asteroidsValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
            generateAsteroidsPosition(asteroids[i]);
            root.getChildren().add(asteroids[i]);
        }
    }
    
    private void createExplosions() {
        for (ImageView img : explosions) {
            img.setLayoutY(-120);
            root.getChildren().add(img);
        }
    }
    
    private void generateStars() {
        if (stars[starsRandom].getLayoutY() > 833) {
            starsRandom = gameRandom.nextInt(stars.length);
            generateStarsPosition(stars[starsRandom]);
        }
        
        else {
            stars[starsRandom].setLayoutY(stars[starsRandom].getLayoutY() + 2);
        }
        
        if (starsCopy1[starsCopy1Random].getLayoutY() > 833) {
            starsCopy1Random = gameRandom.nextInt(starsCopy1.length);
            generateStarsPosition(starsCopy1[starsCopy1Random]);
        }
        
        else {
            starsCopy1[starsCopy1Random].setLayoutY(starsCopy1[starsCopy1Random].getLayoutY() + 2);
        }
        
        if (starsCopy2[starsCopy2Random].getLayoutY() > 833) {
            starsCopy2Random = gameRandom.nextInt(starsCopy2.length);
            generateStarsPosition(starsCopy2[starsCopy2Random]);
        }
        
        else {
            starsCopy2[starsCopy2Random].setLayoutY(starsCopy2[starsCopy2Random].getLayoutY() + 2);
        }
        
        if (starsCopy3[starsCopy3Random].getLayoutY() > 833) {
            starsCopy3Random = gameRandom.nextInt(starsCopy3.length);
            generateStarsPosition(starsCopy3[starsCopy3Random]);
        }
        
        else {
            starsCopy3[starsCopy3Random].setLayoutY(starsCopy3[starsCopy3Random].getLayoutY() + 2);
        }
        
        if (starsCopy4[starsCopy4Random].getLayoutY() > 833) {
            starsCopy4Random = gameRandom.nextInt(starsCopy4.length);
            generateStarsPosition(starsCopy4[starsCopy4Random]);
        }
        
        else {
            starsCopy4[starsCopy4Random].setLayoutY(starsCopy4[starsCopy4Random].getLayoutY() + 2);
        }
        
        if (starsCopy5[starsCopy5Random].getLayoutY() > 833) {
            starsCopy5Random = gameRandom.nextInt(starsCopy5.length);
            generateStarsPosition(starsCopy5[starsCopy5Random]);
        }
        
        else {
            starsCopy5[starsCopy5Random].setLayoutY(starsCopy5[starsCopy5Random].getLayoutY() + 2);
        }
    }
    
    private void generateAsteroids() {
        for (int i = 0; i < asteroids.length; i++) {
            if (asteroids[i].getLayoutY() > 833) {
                asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
                asteroids[i].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
                asteroidsValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
                asteroidsValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
                asteroidsValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
                asteroidsValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
                generateAsteroidsPosition(asteroids[i]);
            }
            
            else {
                asteroids[i].setLayoutY(asteroids[i].getLayoutY() + 7);
                asteroids[i].setRotate(asteroids[i].getRotate() + 4);
            }
        }
    }
    
    private void generateStarsPosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(989));
        image.setLayoutY(- (gameRandom.nextInt(1600) + 35));
    }
    
    private void generateAsteroidsPosition(ImageView image) {
        image.setLayoutX(gameRandom.nextInt(949));
        image.setLayoutY(- (gameRandom.nextInt(1600) + 65));
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
                stars[0].setImage(img);
                starsCopy1[0].setImage(img);
                starsCopy2[0].setImage(img);
                starsCopy3[0].setImage(img);
                starsCopy4[0].setImage(img);
                starsCopy5[0].setImage(img);
            }));
        }
        
        frameTime = Duration.ZERO;
        
        for (Image img : WHITE_STAR_SPRITE_IMAGES) {
            frameTime = frameTime.add(frameGap);
            starsFrames.add(new KeyFrame(frameTime, e -> {
                stars[1].setImage(img);
                starsCopy1[1].setImage(img);
                starsCopy2[1].setImage(img);
                starsCopy3[1].setImage(img);
                starsCopy4[1].setImage(img);
                starsCopy5[1].setImage(img);
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
        for (int i = 0; i < asteroids.length; i++) {
            if (SHIP_SPRITE_RADIUS + asteroidsValues[i][0] > calculateDistance(
                    asteroids[i].getLayoutX() + asteroidsValues[i][1], 
                    ship.getLayoutX() + 37.5, 
                    asteroids[i].getLayoutY() + asteroidsValues[i][2], 
                    ship.getLayoutY() + 50
                )
            ) {
                switch ((int) asteroidsValues[i][3]) {
                    case 4:
                        explode(explosions[2], asteroids[i].getLayoutX(), asteroids[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[2].setLayoutY(-30));
                        break;
                        
                    case 5:
                        explode(explosions[1], asteroids[i].getLayoutX(), asteroids[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[1].setLayoutY(-75));
                        break;
                    
                    default:
                        explode(explosions[3], asteroids[i].getLayoutX(), asteroids[i].getLayoutY());
                        explosionsTimeline.setOnFinished(e -> explosions[3].setLayoutY(-20));
                }
                
                dealDamage(asteroidsValues[i][3]);
                asteroidsRandom = gameRandom.nextInt(ASTEROID_SPRITE_IMAGES.length);
                asteroids[i].setImage(ASTEROID_SPRITE_IMAGES[asteroidsRandom]);
                asteroidsValues[i][0] = ASTEROID_SPRITE_VALUES[asteroidsRandom][0];
                asteroidsValues[i][1] = ASTEROID_SPRITE_VALUES[asteroidsRandom][1];
                asteroidsValues[i][2] = ASTEROID_SPRITE_VALUES[asteroidsRandom][2];
                asteroidsValues[i][3] = ASTEROID_SPRITE_VALUES[asteroidsRandom][3];
                generateAsteroidsPosition(asteroids[i]);
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
