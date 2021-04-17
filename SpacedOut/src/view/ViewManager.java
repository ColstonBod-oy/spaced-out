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
    private MenuButton optionsButton;
    private MenuButton helpButton;
    private MenuButton creditsButton;
    private MenuButton exitButton;
    private MenuButton toDeactivate;
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
            toHide.moveSubScene();
        }
        
        subScene.moveSubScene();
        toHide = subScene;
    }
    
    private void createMenuSubScenes() {
        createStartSubScene();
        
        optionsSubScene = new MenuSubScene();
        root.getChildren().add(optionsSubScene);
        
        helpSubScene = new MenuSubScene();
        root.getChildren().add(helpSubScene);
        
        creditsSubScene = new MenuSubScene();
        root.getChildren().add(creditsSubScene);
    }
    
    private void createStartSubScene() {
        startSubScene = new MenuSubScene();
        root.getChildren().add(startSubScene);
        
        MenuLabel startLabel = new MenuLabel("CHOOSE LEVEL");
        startLabel.setLayoutX(110);
        startLabel.setLayoutY(37.25);
        startSubScene.getPane().getChildren().add(startLabel);
        
        createLevel1Button();
        createLevel2Button();
        createLevel3Button();
    }
    
    private void createLevel1Button() {
        PanelButton level1Button = new PanelButton("LEVEL1");
        level1Button.setLayoutX(39.75);
        level1Button.setLayoutY(157.75);
        startSubScene.getPane().getChildren().add(level1Button);
        
        level1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                GameView level1 = new GameView();
                level1.createNewGame(mainStage, 1);
            }
        });
    }
    
    private void createLevel2Button() {
        PanelButton level2Button = new PanelButton("LEVEL2");
        level2Button.setLayoutX(226.5);
        level2Button.setLayoutY(157.75);
        startSubScene.getPane().getChildren().add(level2Button);
    }
    
    private void createLevel3Button() {
        PanelButton level3Button = new PanelButton("LEVEL3");
        level3Button.setLayoutX(413.25);
        level3Button.setLayoutY(157.75);
        startSubScene.getPane().getChildren().add(level3Button);
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
        optionsButton = new MenuButton("OPTIONS");
        addMenuButton(optionsButton);
        
        optionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(optionsButton);
                setActiveSubScene(optionsSubScene);
            }
        });
    }
    
    private void createHelpButton() {
        helpButton = new MenuButton("HELP");
        addMenuButton(helpButton);
        
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(helpButton);
                setActiveSubScene(helpSubScene);
            }
        });
    }
    
    private void createCreditsButton() {
        creditsButton = new MenuButton("CREDITS");
        addMenuButton(creditsButton);
        
        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(creditsButton);
                setActiveSubScene(creditsSubScene);
            }
        });
    }
    
    private void createExitButton() {
        exitButton = new MenuButton("EXIT");
        addMenuButton(exitButton);
        
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setActiveButton(exitButton);
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
