/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Collection;
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
    private AnchorPane root;
    private Scene gameScene;
    private Stage gameStage;
    private Timeline gameTimeline;
    private AnimationTimer gameAnimationTimer;
    private Collection<KeyFrame> frames;
    private Duration frameTime;
    private Duration frameGap;
    private boolean isAKeyPressed;
    private boolean isSKeyPressed;
    private boolean isDKeyPressed;
    private boolean isFKeyPressed;
    private Image[] shipSpriteImages;
    private ImageView ship;
    private int shipAngle;
    private Stage menuStage;
    private int chosenLevel;
    
    public GameView() {
        initStage();
        initKeyListeners();
    }
    
    private void initKeyListeners() {
        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.A) {
                    isAKeyPressed = true;
                }
                
                else if (t.getCode() == KeyCode.S) {
                    isSKeyPressed = true;
                }
                
                else if (t.getCode() == KeyCode.D) {
                    isDKeyPressed = true;
                }
                
                else if (t.getCode() == KeyCode.F) {
                    isFKeyPressed = true;
                }
            }
        });
        
        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.A) {
                    isAKeyPressed = false;
                }
                
                else if (t.getCode() == KeyCode.S) {
                    isSKeyPressed = false;
                }
                
                else if (t.getCode() == KeyCode.D) {
                    isDKeyPressed = false;
                }
                
                else if (t.getCode() == KeyCode.F) {
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
        createShip();
        createGameLoop();
    }
    
    private void createShip() {
        shipSpriteImages = new Image[2];
        shipSpriteImages[0] = new Image("view/assets/sprites/sprite_ship0.png");
        shipSpriteImages[1] = new Image("view/assets/sprites/sprite_ship1.png");
        
        ship = new ImageView(shipSpriteImages[0]);
        gameTimeline = new Timeline();
        frames = gameTimeline.getKeyFrames();
        frameTime = Duration.ZERO;
        frameGap = Duration.millis(25);
        
        for (Image img : shipSpriteImages) {
            frameTime = frameTime.add(frameGap);
            frames.add(new KeyFrame(frameTime, e -> ship.setImage(img)));
        }
        
        gameTimeline.setAutoReverse(true);
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.play();
        ship.setLayoutX(GAME_WIDTH / 2 - 37.5);
        ship.setLayoutY(GAME_HEIGHT - 130);
        root.getChildren().add(ship);
    }
    
    private void createGameLoop() {
        gameAnimationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                moveShip();
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
        }
    }
    
}
