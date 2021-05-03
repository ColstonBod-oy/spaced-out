/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

/**
 *
 * @author user
 */
public class FXMain extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        ViewManager manager = new ViewManager();
        primaryStage = manager.getMainStage();
        
        primaryStage.setTitle("Hello World!");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
