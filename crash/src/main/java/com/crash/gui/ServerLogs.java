/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.gui;

import com.crash.file.FileManager;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This class is used to display the server logs in a new window.
 */
public class ServerLogs extends StackPane {
    
    /**
     * The constructor for this class.
     * @param parent The parent window.
     */
    public ServerLogs(Window parent) {
        TextArea log_area = new TextArea(FileManager.get_server_log());
        log_area.setMaxWidth(300);
        log_area.setEditable(false);
        Label log_area_label = new Label("Server Logs:");
        log_area_label.setFont(new Font("Arial", 25));
        Button close_button = new Button("Close");
        final Stage log_stage = new Stage();
        log_stage.initModality(Modality.APPLICATION_MODAL);
        log_stage.initOwner(parent);
        VBox log_vbox = new VBox(20);
        log_vbox.setPadding(new Insets(5, 0, 0, 10));
        log_vbox.getChildren().addAll(log_area_label,log_area,close_button);
        Scene log_scene = new Scene(log_vbox, 350, 250);

        log_stage.setScene(log_scene);
        log_stage.setTitle("Server Logs");
        log_stage.show();

        close_button.setOnAction(e -> {
            log_stage.close();
        });
    }

}
