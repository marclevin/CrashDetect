/**
 * @author 220001291
 * @version Practical X
 */

package com.crash;

import com.crash.api.ServerHandler;
import com.crash.file.FileManager;
import com.crash.gui.Monitor;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the main class for the Crash application.
 */
public class Main extends Application
{

    private Scene menu_scene;
    private Scene monitor_scene;
    private Scene instructions_scene;
    /**
     * The main method for the Crash application.
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        launch(args);
        // This will only run after the application is closed
        FileManager.cleanup_server_log_files();
        ServerHandler.get_instance().stop_server();
    }

    /**
     * JavaFX entry point.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FileManager.cleanup_process_files();


        VBox menu_box = new VBox(20);
        Label menu_title = new Label("CrashDetect V.1.0");
        menu_title.setFont(new Font("Arial", 25));
        menu_title.setTextFill(Color.ORANGERED);
        Button view_monitor_button = new Button("View Main Monitor");
        view_monitor_button.setFont(new Font("Arial", 20));
        Button view_instructions_button = new Button("View Instructions");
        view_instructions_button.setFont(new Font("Arial", 20));
        Button exit_button = new Button("Exit");
        exit_button.setFont(new Font("Arial", 20));
        Button monitor_back_button = new Button("Back");
        Button instructions_back_button = new Button("Back");
        Button clear_all_final_and_log = new Button("Clear All Final and Log Files");
        clear_all_final_and_log.setFont(new Font("Arial", 20));
        monitor_back_button.setFont(new Font("Arial", 20));
        instructions_back_button.setFont(new Font("Arial", 20));
        menu_box.getChildren().addAll(menu_title, view_monitor_button, view_instructions_button, clear_all_final_and_log, exit_button);
        menu_box.setAlignment(Pos.CENTER);
        menu_box.setSpacing(30);
        

        // Button handling.
        exit_button.setOnAction(e -> {
            FileManager.cleanup_server_log_files();
            ServerHandler.get_instance().stop_server();
            System.exit(0);
        });
        monitor_back_button.setOnAction(e -> {
            stage.setScene(menu_scene);
            clear_all_final_and_log.setText("Clear All Final and Log Files");
        });
        instructions_back_button.setOnAction(e -> {
            stage.setScene(menu_scene);
            clear_all_final_and_log.setText("Clear All Final and Log Files");
        });
        view_monitor_button.setOnAction(e -> {
            stage.setScene(monitor_scene);
        });
        view_instructions_button.setOnAction(e -> {
            stage.setScene(instructions_scene);
        });
        clear_all_final_and_log.setOnAction(e -> {
            FileManager.cleanup_server_log_files();
            FileManager.cleanup_process_files();
            FileManager.cleanup_final_img();
            clear_all_final_and_log.setText("All Final and Log Files Cleared");
        });


        Text instructions_text = new Text();
        Text instructions_header = new Text();
        instructions_text.setText("");
        String instruction_body = """
        This software allows a user to process an image of a car and detect
        if that car has been crashed or not.
        This tool was created to solve the problem of CCTV cameras and speed cameras being used for innate purposes,
        where they could save lives. Upon the detection of a crash, a future system could alert emergency services.
        
        Instructions for the tool:
        1. Start the image processing server with the button, wait until the green light.
        2. Load an image from your file system using the add file button.
        3. Click preview to preview the unprocessed image.
        4. Click process to process your image into the image the AI will use to predict crashes.
        5. Click predict to run the AI and return data from it.
        6. See the results window on the far right of the monitor to view the results of the AI's prediction.""";

        instructions_text.setText(instruction_body);
        instructions_text.setFill(Color.ORANGE);
        instructions_header.setText("Welcome to CrashDetect.\r\n");
        instructions_header.setFill(Color.BLACK);
        instructions_header.setFont(new Font("Arial", 25));
        instructions_text.setFont(new Font("Arial", 20));
        VBox instructions_box = new VBox(instructions_header,instructions_text,instructions_back_button);
        instructions_box.setAlignment(Pos.CENTER);
        instructions_box.setSpacing(30);
        Monitor m = new Monitor(800,0);
        monitor_scene = new Scene(new VBox(m,monitor_back_button),1080,720);
        menu_scene = new Scene(menu_box, 1080, 720);
        instructions_scene = new Scene(instructions_box, 1080, 720);
        
        stage.setTitle("CrashDetect");
        stage.setScene(menu_scene);
        stage.show();        
    }

}