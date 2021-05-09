/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    private AnchorPane root;
    private Scene mainScene;
    private Stage mainStage;
    private List<MenuButton> menuButtons;
    private MenuButton startButton;
    private MenuButton toDeactivate;
    private PanelButton level1Button;
    private PanelButton level2Button;
    private PanelButton level3Button;
    private MenuSubScene startSubScene;
    private MenuSubScene optionsSubScene;
    private MenuSubScene helpSubScene;
    private MenuSubScene creditsSubScene;
    private MenuSubScene toHide;
    
    public ViewManager() {
        root = new AnchorPane();
        root.setStyle("-fx-background-color: #000;");
        mainScene = new Scene(root, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        menuButtons = new ArrayList<>();
        createMenuButtons();
        createLogo();
        createMenuSubScenes();
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
        
        subScene.moveSubScene(-678);
        toHide = subScene;
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
                GameView level = new GameView();
                level.createNewGame(mainStage, 1, level2Button);
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
                GameView level = new GameView();
                level.createNewGame(mainStage, 2, level3Button);
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
                GameView level = new GameView();
                level.createNewGame(mainStage, 3, level1Button);
            }
        });
    }
    
    private void createOptionsSubScene() {
        optionsSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(optionsSubScene);
        
        MenuLabel headerLabel1 = new MenuLabel("MUSIC", 23, 210, 49, -2);
        headerLabel1.setLayoutX(60);
        headerLabel1.setLayoutY(37.25);
        optionsSubScene.getPane().getChildren().add(headerLabel1);
        
        MenuLabel headerLabel2 = new MenuLabel("CONTROLS", 23, 210, 49, -2);
        headerLabel2.setLayoutX(330);
        headerLabel2.setLayoutY(37.25);
        optionsSubScene.getPane().getChildren().add(headerLabel2);
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
        
        MenuLabel bodyLabel1 = new MenuLabel("THIS PROJECT WAS\nINSPIRED BY A GAME\nMADE BY JAVACRAVING.\n\nASSETS USED ARE\nOBTAINED FROM KENNEY\nASSETS AND OPEN\nGAMEART.ORG.", 13, 240, 200, -20);
        bodyLabel1.setLayoutX(45);
        bodyLabel1.setLayoutY(112.75);
        creditsSubScene.getPane().getChildren().add(bodyLabel1);
        
        MenuLabel bodyLabel2 = new MenuLabel("youtu.be/DkIuA5ZEZ_U\n\nkenney.nl/assets?q=ui\n\nopengameart.org/\ncontent/i-are-\nspaceship-16x16-\nspace-sprites", 13, 240, 200, -20);
        bodyLabel2.setLayoutX(315);
        bodyLabel2.setLayoutY(112.75);
        creditsSubScene.getPane().getChildren().add(bodyLabel2);
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
    
    private void createLogo() {
        ImageView logo = new ImageView("view/assets/cooltext381814223973078.png");
        logo.setLayoutX(436.5);
        logo.setLayoutY(96.75);
        root.getChildren().add(logo);
    }
    
}
