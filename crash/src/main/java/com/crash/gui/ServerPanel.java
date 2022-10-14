/**
 * @author 220001291
 * @version Practical X
 */


package com.crash.gui;

import com.crash.api.ServerHandler;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * This class is used to display the server information and controls.
 */
public class ServerPanel extends VBox {


    private Button start_server_button = new Button("Start Server");
    private Button stop_server_button = new Button("Stop Server");
    private Button view_logs_button = new Button("View Logs");
    private Label server_status_label = new Label("Server Status:");
    private Label server_status_output_label = new Label("Stopped");
    private Label server_panel_label = new Label("Server Controls");
    private Glow server_status_glow = new Glow();
    private Circle server_status_indicator = new Circle(10);
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
    null, BorderWidths.DEFAULT));
    private VBox inner_box = new VBox();

    // Server Handler
    private ServerHandler server_handler = ServerHandler.get_instance();

    private GridPane server_grid = new GridPane();

    /**
     * This is the constructor for the ServerPanel class.
     */
    public ServerPanel() {
        // Super
        super();
        // Setting properties
        set_server_status(false);
        server_grid.setHgap(10);
        server_grid.setVgap(10);

        // Light Properties
        server_status_indicator.setEffect(server_status_glow);
        server_status_indicator.setRadius(10);

        // Adding to grid
        server_grid.add(server_status_label, 0, 0);
        server_grid.add(server_status_indicator, 1, 0);
        server_grid.add(server_status_output_label, 2, 0);
        server_grid.add(start_server_button, 0, 1);
        server_grid.add(stop_server_button, 2, 1);
        server_grid.add(view_logs_button, 1, 2);

        server_panel_label.setFont(new Font("Arial", 20));
        server_panel_label.setTextFill(Color.ORANGERED);


        inner_box.getChildren().addAll(server_grid);
        this.getChildren().addAll(server_panel_label, inner_box);
        inner_box.setBorder(border);
        inner_box.setPadding(new Insets(10, 0, 10, 5));
        this.setSpacing(10);
        this.setMaxWidth(260);
        this.setMinWidth(260);
        this.setMinHeight(150);
        this.setMaxHeight(150);
        // Event Handling

        // Start Server Button
        start_server_button.setOnAction(e -> {

            if (server_handler.run_server()) {
                while (!server_handler.isAlive()) {
                    try {
                        Thread.sleep(100);
                        server_status_output_label.setText("Starting");
                    } catch (InterruptedException f) {
                        f.printStackTrace();
                    }
                }
                server_status_output_label.setText("Running");
                set_server_status(true);
            }
            ;
        });

        // Stop Server Button
        stop_server_button.setOnAction(e -> {
            if (server_handler.stop_server()) {
                while (server_handler.isAlive()) {
                    try {
                        Thread.sleep(100);
                        server_status_output_label.setText("Stopping");
                    } catch (InterruptedException f) {
                        f.printStackTrace();
                    }
                }
                server_status_output_label.setText("Stopped");
                set_server_status(false);
            }
            ;
        });

        // View logs button
        view_logs_button.setOnAction(e -> {
           new ServerLogs(this.getScene().getWindow());
        });
    }

    private void set_server_status(boolean status) {
        if (status) {
            server_status_glow.setLevel(1);
            server_status_indicator.setFill(Color.GREEN);
            start_server_button.setDisable(true);
            stop_server_button.setDisable(false);
        } else {
            server_status_glow.setLevel(0.5);
            server_status_indicator.setFill(Color.RED);
            start_server_button.setDisable(false);
            stop_server_button.setDisable(true);
        }
    }

    /**
     * This method is used to get the server status.
     * @return if the server is running or not.
     */
    public boolean get_server_status()
    {
        return server_handler.isAlive();
    }



}
