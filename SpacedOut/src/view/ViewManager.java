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
        
        optionsSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(optionsSubScene);
        
        helpSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(helpSubScene);
        
        creditsSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(creditsSubScene);
    }
    
    private void createStartSubScene() {
        startSubScene = new MenuSubScene(600, 400, 226.75);
        root.getChildren().add(startSubScene);
        
        MenuLabel startLabel = new MenuLabel("CHOOSE LEVEL", 23, 380, 49);
        startLabel.setLayoutX(110);
        startLabel.setLayoutY(37.25);
        startSubScene.getPane().getChildren().add(startLabel);
        
        createLevel1Button();
        createLevel2Button();
        createLevel3Button();
    }
    
    private void createLevel1Button() {
        level1Button = new PanelButton("", 0, 147, 147);
        level1Button.setGraphic(new ImageView("view/assets/numeral1.png"));
        level1Button.setLayoutX(39.75);
        level1Button.setLayoutY(157.75);
        startSubScene.getPane().getChildren().add(level1Button);
        
        level1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                GameView level1 = new GameView();
                level1.createNewGame(mainStage, 1, level2Button);
            }
        });
    }
    
    private void createLevel2Button() {
        level2Button = new PanelButton("", 0, 147, 147);
        level2Button.setGraphic(new ImageView("view/assets/numeral2.png"));
        level2Button.setDisable(true);
        level2Button.setLayoutX(226.5);
        level2Button.setLayoutY(157.75);
        startSubScene.getPane().getChildren().add(level2Button);
    }
    
    private void createLevel3Button() {
        level3Button = new PanelButton("", 0, 147, 147);
        level3Button.setGraphic(new ImageView("view/assets/numeral3.png"));
        level3Button.setDisable(true);
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
