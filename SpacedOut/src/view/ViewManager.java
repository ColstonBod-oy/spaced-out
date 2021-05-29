/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.AnchorPaneBackground;
import model.ControlsButton;
import model.MenuButton;
import model.MenuLabel;
import model.MenuSubScene;
import model.PanelButton;

/**
 *
 * @author user
 */
public class ViewManager {
    
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final String MUSIC_PATH = "/view/assets/music/constellation-upbeat-spacey-song.wav";
    private static final String SFX_PATH = "/view/assets/sfx/sfx_lose.wav";
    private AnchorPane root;
    private Scene mainScene;
    private Stage mainStage;
    private MediaPlayer menuMediaPlayer;
    private AnchorPaneBackground menuBackground;
    private List<MenuButton> menuButtons;
    private AudioClip invalidInput;
    private MenuButton startButton;
    private MenuButton toDeactivate;
    private PanelButton level1Button;
    private PanelButton level2Button;
    private PanelButton level3Button;
    private KeyCode[] keybinds;
    private ControlsButton toDeselect;
    private MenuSubScene startSubScene;
    private MenuSubScene optionsSubScene;
    private MenuSubScene helpSubScene;
    private MenuSubScene creditsSubScene;
    private MenuSubScene toHide;
    private boolean isAudioOn;
    
    public ViewManager() {
        root = new AnchorPane();
        root.setStyle("-fx-background-color: #000;");
        mainScene = new Scene(root, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.setOnCloseRequest(e -> {
            e.consume();
            createJOptionPane();
        });
        menuBackground = new AnchorPaneBackground();
        menuButtons = new ArrayList<>();
        keybinds = new KeyCode[4];
        keybinds[0] = KeyCode.A;
        keybinds[1] = KeyCode.S;
        keybinds[2] = KeyCode.D;
        keybinds[3] = KeyCode.F;
        isAudioOn = true;
        createMenuMusic();
        createMenuSFX();
        createMenuBackground();
        createMenuButtons();
        createMenuSubScenes();
        createLogo();
        setActiveButton(startButton);
        setActiveSubScene(startSubScene);
    }
    
    private void setActiveButton(MenuButton button) {
        if (toDeactivate != null) {
            toDeactivate.moveButton();
        }
        
        button.moveButton();
        toDeactivate = button;
    }
    
    private void setActiveSubScene(MenuSubScene subScene) {
        if (toHide != null) {
            toHide.moveSubScene(-678);
        }
        
        if (toHide == optionsSubScene && toDeselect != null) {
            toDeselect.moveButton();
            toDeselect = null;
        }
        
        subScene.moveSubScene(-678);
        toHide = subScene;
    }
    
    private void setSelectedButton(ControlsButton button) {
        if (toDeselect != null) {
            toDeselect.moveButton();
        }
        
        button.moveButton();
        toDeselect = button;
    }
    
    public Stage getMainStage() {
        return mainStage;
    }
    
    private void addMenuButton(MenuButton button) {
        button.setLayoutX(78);
        button.setLayoutY(202.25 + menuButtons.size() * 100);
        menuButtons.add(button);
        root.getChildren().add(button);
    }
    
    private void createMenuMusic() {
        Media music = new Media(getClass().getResource(MUSIC_PATH).toExternalForm());
        menuMediaPlayer = new MediaPlayer(music);
        menuMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        menuMediaPlayer.play();
    }
    
    private void createMenuSFX() {
        invalidInput = new AudioClip(getClass().getResource(SFX_PATH).toExternalForm());
    }
    
    private void createMenuBackground() {
        int step = 1;
        menuBackground.createNewBackground(root, step);
    }
    
    private void createMenuButtons() {
        createStartButton();
        createOptionsButton();
        createHelpButton();
        createCreditsButton();
        createExitButton();
    }
    
    private void createStartButton() {
        startButton = new MenuButton("START");
        addMenuButton(startButton);
        
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(startButton);
                setActiveSubScene(startSubScene);
            }
        });
    }
    
    private void createOptionsButton() {
        MenuButton primaryButton = new MenuButton("OPTIONS");
        addMenuButton(primaryButton);
        
        primaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(primaryButton);
                setActiveSubScene(optionsSubScene);
            }
        });
    }
    
    private void createHelpButton() {
        MenuButton primaryButton = new MenuButton("HELP");
        addMenuButton(primaryButton);
        
        primaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(primaryButton);
                setActiveSubScene(helpSubScene);
            }
        });
    }
    
    private void createCreditsButton() {
        MenuButton primaryButton = new MenuButton("CREDITS");
        addMenuButton(primaryButton);
        
        primaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(primaryButton);
                setActiveSubScene(creditsSubScene);
            }
        });
    }
    
    private void createExitButton() {
        MenuButton primaryButton = new MenuButton("EXIT");
        addMenuButton(primaryButton);
        
        primaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(primaryButton);
                mainStage.close();
            }
        });
    }
    
    private void createMenuSubScenes() {
        createStartSubScene();
        createOptionsSubScene();
        createHelpSubScene();
        createCreditsSubScene();
    }
    
    private void createStartSubScene() {
        startSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(startSubScene);
        
        MenuLabel headerLabel = new MenuLabel("CHOOSE A LEVEL", 23, 380, 49, -2);
        headerLabel.setLayoutX(110);
        headerLabel.setLayoutY(37.25);
        startSubScene.getPane().getChildren().add(headerLabel);
        
        createLevel1Button();
        createLevel2Button();
        createLevel3Button();
    }
    
    private void createLevel1Button() {
        level1Button = new PanelButton("", 0, 147, 147);
        level1Button.setGraphic(new ImageView("view/assets/numeral1.png"));
        level1Button.setLayoutX(39.75);
        level1Button.setLayoutY(147.75);
        startSubScene.getPane().getChildren().add(level1Button);
        
        level1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                int currentLevel = 1;
                PanelButton nextLevel = level2Button;
                
                menuMediaPlayer.stop();
                menuBackground.stopBackgroundAnimationTimer();
                menuBackground.stopStarsTimeline();
                
                GameView level = new GameView();
                level.createNewGame(mainStage, menuMediaPlayer, menuBackground, keybinds, isAudioOn, currentLevel, nextLevel);
            }
        });
    }
    
    private void createLevel2Button() {
        level2Button = new PanelButton("", 0, 147, 147);
        level2Button.setGraphic(new ImageView("view/assets/numeral2.png"));
        level2Button.setDisable(true);
        level2Button.setLayoutX(226.5);
        level2Button.setLayoutY(147.75);
        startSubScene.getPane().getChildren().add(level2Button);
        
        level2Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                int currentLevel = 2;
                PanelButton nextLevel = level3Button;
                
                menuMediaPlayer.stop();
                menuBackground.stopBackgroundAnimationTimer();
                menuBackground.stopStarsTimeline();
                
                GameView level = new GameView();
                level.createNewGame(mainStage, menuMediaPlayer, menuBackground, keybinds, isAudioOn, currentLevel, nextLevel);
            }
        });
    }
    
    private void createLevel3Button() {
        level3Button = new PanelButton("", 0, 147, 147);
        level3Button.setGraphic(new ImageView("view/assets/numeral3.png"));
        level3Button.setDisable(true);
        level3Button.setLayoutX(413.25);
        level3Button.setLayoutY(147.75);
        startSubScene.getPane().getChildren().add(level3Button);
        
        level3Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                int currentLevel = 3;
                PanelButton nextLevel = level1Button;
                
                menuMediaPlayer.stop();
                menuBackground.stopBackgroundAnimationTimer();
                menuBackground.stopStarsTimeline();
                
                GameView level = new GameView();
                level.createNewGame(mainStage, menuMediaPlayer, menuBackground, keybinds, isAudioOn, currentLevel, nextLevel);
            }
        });
    }
    
    private void createOptionsSubScene() {
        optionsSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(optionsSubScene);
        
        MenuLabel headerLabel1 = new MenuLabel("AUDIO", 23, 210, 49, -2);
        headerLabel1.setLayoutX(60);
        headerLabel1.setLayoutY(37.25);
        optionsSubScene.getPane().getChildren().add(headerLabel1);
        
        MenuLabel headerLabel2 = new MenuLabel("CONTROLS", 23, 210, 49, -2);
        headerLabel2.setLayoutX(330);
        headerLabel2.setLayoutY(37.25);
        optionsSubScene.getPane().getChildren().add(headerLabel2);
        
        createMusicButton();
        createMovement1Button();
        createMovement2Button();
        createMovement3Button();
        createMovement4Button();
    }
    
    private void createMusicButton() {
        PanelButton largeButton = new PanelButton("ON", 34, 147, 147);
        largeButton.setLayoutX(91.5);
        largeButton.setLayoutY(147.75);
        optionsSubScene.getPane().getChildren().add(largeButton);
        
        largeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (isAudioOn) {
                    largeButton.setText("OFF");
                    menuMediaPlayer.pause();
                    MenuButton.isAudioOn = false;
                    PanelButton.isAudioOn = false;
                    ControlsButton.isAudioOn = false;
                    MenuSubScene.isAudioOn = false;
                    isAudioOn = false;
                }
                
                else {
                    largeButton.setText("ON");
                    menuMediaPlayer.play();
                    MenuButton.isAudioOn = true;
                    PanelButton.isAudioOn = true;
                    ControlsButton.isAudioOn = true;
                    MenuSubScene.isAudioOn = true;
                    isAudioOn = true;
                }
            }
        });
    }
    
    private void createMovement1Button() {
        ControlsButton mediumButton = new ControlsButton(keybinds[0].getName());
        mediumButton.setLayoutX(325);
        mediumButton.setLayoutY(122.75);
        optionsSubScene.getPane().getChildren().add(mediumButton);
        
        mediumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setSelectedButton(mediumButton);
            }
        });
        
        mediumButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (toDeselect == mediumButton && !Arrays.stream(keybinds).anyMatch(t.getCode()::equals)) {
                    if (t.getCode().isLetterKey() || t.getCode().isDigitKey()) {
                        keybinds[0] = t.getCode();
                        mediumButton.setText(t.getText());
                    }
                    
                    else {
                        invalidInput.play();
                    }
                }
                
                else {
                    invalidInput.play();
                }
            }
        });
    }
    
    private void createMovement2Button() {
        ControlsButton mediumButton = new ControlsButton(keybinds[1].getName());
        mediumButton.setLayoutX(447);
        mediumButton.setLayoutY(122.75);
        optionsSubScene.getPane().getChildren().add(mediumButton);
        
        mediumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setSelectedButton(mediumButton);
            }
        });
        
        mediumButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (toDeselect == mediumButton && !Arrays.stream(keybinds).anyMatch(t.getCode()::equals)) {
                    if (t.getCode().isLetterKey() || t.getCode().isDigitKey()) {
                        keybinds[1] = t.getCode();
                        mediumButton.setText(t.getText());
                    }
                    
                    else {
                        invalidInput.play();
                    }
                }
                
                else {
                    invalidInput.play();
                }
            }
        });
    }
    
    private void createMovement3Button() {
        ControlsButton mediumButton = new ControlsButton(keybinds[2].getName());
        mediumButton.setLayoutX(325);
        mediumButton.setLayoutY(244.75);
        optionsSubScene.getPane().getChildren().add(mediumButton);
        
        mediumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setSelectedButton(mediumButton);
            }
        });
        
        mediumButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (toDeselect == mediumButton && !Arrays.stream(keybinds).anyMatch(t.getCode()::equals)) {
                    if (t.getCode().isLetterKey() || t.getCode().isDigitKey()) {
                        keybinds[2] = t.getCode();
                        mediumButton.setText(t.getText());
                    }
                    
                    else {
                        invalidInput.play();
                    }
                }
                
                else {
                    invalidInput.play();
                }
            }
        });
    }
    
    private void createMovement4Button() {
        ControlsButton mediumButton = new ControlsButton(keybinds[3].getName());
        mediumButton.setLayoutX(447);
        mediumButton.setLayoutY(244.75);
        optionsSubScene.getPane().getChildren().add(mediumButton);
        
        mediumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setSelectedButton(mediumButton);
            }
        });
        
        mediumButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (toDeselect == mediumButton && !Arrays.stream(keybinds).anyMatch(t.getCode()::equals)) {
                    if (t.getCode().isLetterKey() || t.getCode().isDigitKey()) {
                        keybinds[3] = t.getCode();
                        mediumButton.setText(t.getText());
                    }
                    
                    else {
                        invalidInput.play();
                    }
                }
                
                else {
                    invalidInput.play();
                }
            }
        });
    }
    
    private void createHelpSubScene() {
        helpSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(helpSubScene);
        
        MenuLabel headerLabel = new MenuLabel("OBJECTIVE", 23, 380, 49, -2);
        headerLabel.setLayoutX(110);
        headerLabel.setLayoutY(37.25);
        helpSubScene.getPane().getChildren().add(headerLabel);
        
        MenuLabel bodyLabel = new MenuLabel("YOU'RE THE PILOT OF A SHIP TRAVELING IN SPACE.\n\nHOWEVER, YOU LATER FIND YOURSELF TRAPPED\nINSIDE AN ASTEROID BELT.\n\nAVOID THE ASTEROIDS AS YOU FIND YOUR WAY OUT.", 15, 516, 180, -16);
        bodyLabel.setLayoutX(42);
        bodyLabel.setLayoutY(123.5);
        helpSubScene.getPane().getChildren().add(bodyLabel);
    }
    
    private void createCreditsSubScene() {
        creditsSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(creditsSubScene);
        
        MenuLabel headerLabel1 = new MenuLabel("REFERENCES", 23, 210, 49, -2);
        headerLabel1.setLayoutX(60);
        headerLabel1.setLayoutY(37.25);
        creditsSubScene.getPane().getChildren().add(headerLabel1);
        
        MenuLabel headerLabel2 = new MenuLabel("LINKS", 23, 210, 49, -2);
        headerLabel2.setLayoutX(330);
        headerLabel2.setLayoutY(37.25);
        creditsSubScene.getPane().getChildren().add(headerLabel2);
        
        MenuLabel bodyLabel1 = new MenuLabel("THIS PROJECT WAS\nINSPIRED BY A GAME\nMADE BY JAVACRAVING.\n\nASSETS USED ARE\nOBTAINED FROM KENNEY\nASSETS, OPEN\nGAMEART.ORG AND FREESOUND.ORG.", 13, 240, 220, -20);
        bodyLabel1.setLayoutX(45);
        bodyLabel1.setLayoutY(112.75);
        creditsSubScene.getPane().getChildren().add(bodyLabel1);
        
        MenuLabel bodyLabel2 = new MenuLabel("youtu.be/DkIuA5ZEZ_U\n\nkenney.nl/assets?q=ui\nopengameart.org/\ncontent/i-are-\nspaceship-16x16-\nspace-sprites\nfreesound.org", 13, 240, 220, -20);
        bodyLabel2.setLayoutX(315);
        bodyLabel2.setLayoutY(112.75);
        creditsSubScene.getPane().getChildren().add(bodyLabel2);
    }
    
    private void createLogo() {
        ImageView logo = new ImageView("view/assets/cooltext381814223973078.png");
        logo.setLayoutX(436.5);
        logo.setLayoutY(96.75);
        root.getChildren().add(logo);
    }
    
    private void createJOptionPane() {
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        
        if (answer == 0) {
            mainStage.close();
        }
    }
    
}
