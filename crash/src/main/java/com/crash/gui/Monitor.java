/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.gui;

import java.io.File;

import com.crash.api.Sender;
import com.crash.file.FileManager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This is the defacto main class for the GUI. It contains the main menu and other controls.
 */
public class Monitor extends VBox {

    private Button process_button = new Button("Process");
    private Label preview_label = new Label("Preview selected.");
    private Label process_label = new Label("Process selected.");
    private Label predict_label = new Label("Predict.");
    private Label instructions_label = new Label("Instructions");
    private Text instructions = new Text(
            "Step 1. Click start server to start the server.\r\nStep 2. Click preview to load an image into the preview window.\r\nStep 3. Click process to process the image before prediction.\r\nStep 4.Click predict to predict the state of the car.");
    private Button predict_button = new Button("Predict");
    private Button preview_button = new Button("Preview");
    private ModelResults model_results = null;
    private ImageControls image_controls = null;
    private ServerPanel server_grid = null;
    FileControls file_controls = null;
    private Label message_label = new Label();
    private Label control_panel_label = new Label("Control Panel");
    private Label title_label = new Label("Crash Detector");
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            null, BorderWidths.DEFAULT));
    private Sender sender = null;
    private File processed_file = null;

    /**
     * This constructor is used to create the main menu.
     * @param width The width of the main menu.
     * @param height The height of the main menu.
     */
    public Monitor(int width, int height) {
        this.setPrefSize(width, height);
        // List view
        BorderPane title_pane = new BorderPane(title_label);
        // Setting properties
        this.setPadding(new Insets(10, 0, 0, 5));
        // Message Label
        message_label.setFont(new Font("Arial", 20));
        message_label.setTextFill(Color.RED);

        // Instruction Label
        instructions_label.setFont(new Font("Arial", 20));
        instructions_label.setTextFill(Color.ORANGERED);

        // Title Label
        title_label.setFont(new Font("Serif", 40));
        title_label.setTextFill(Color.ORANGE);
        title_label.setEffect(new DropShadow(2, 2, 2, Color.RED));

        // Custom GUI models
        image_controls = new ImageControls();
        server_grid = new ServerPanel();
        model_results = new ModelResults();
        file_controls = new FileControls();

        // Style boxes
        HBox file_image_result_box = new HBox(file_controls, image_controls, model_results);
        file_image_result_box.setSpacing(20);

        // Control buttons
        control_panel_label.setFont(new Font("Arial", 20));
        control_panel_label.setTextFill(Color.ORANGERED);
        GridPane control_panel_grid = new GridPane();
        control_panel_grid.setHgap(10);
        control_panel_grid.setVgap(10);
        control_panel_grid.add(preview_label, 0, 0);
        control_panel_grid.add(preview_button, 0, 1);
        control_panel_grid.add(process_label, 1, 0);
        control_panel_grid.add(process_button, 1, 1);
        control_panel_grid.add(predict_label, 2, 0);
        control_panel_grid.add(predict_button, 2, 1);
        control_panel_grid.setHgap(20);
        control_panel_grid.setMinSize(400, 100);
        control_panel_grid.setMaxSize(400, 100);
        HBox control_panel_inner_box = new HBox(control_panel_grid);
        control_panel_inner_box.setBorder(border);
        control_panel_inner_box.setPadding(new Insets(10, 10, 10, 10));
        VBox control_panel_outer_box = new VBox(control_panel_label, control_panel_inner_box);
        HBox bottom_level_box = new HBox(server_grid, control_panel_outer_box, message_label);
        bottom_level_box.setSpacing(50);

        instructions.setFill(Color.RED);
        instructions.setFont(new Font("Arial", 12));
        this.getChildren().addAll(title_pane, file_image_result_box, bottom_level_box, instructions_label,
                instructions);
        this.setSpacing(10);

        // Button Wiring
        preview_button.setOnAction(e -> {
            processed_file = file_controls.get_selected_file();
            if (processed_file == null) {
                message_label.setText("No file selected."); 
                return;
            } else {
                message_label.setText("");
            }
            image_controls.set_before_image(FileManager.get_fx_img(processed_file));
        });

        process_button.setOnAction(e -> {
            if (processed_file == null) {
                message_label.setText("No file selected.");
                return;
            } else {
                message_label.setText("");
            }
            sender = new Sender("localhost", 5000);
            processed_file = sender.process(processed_file, Sender.process_commands);
            if (processed_file == null) {
                message_label.setText(Sender.get_message());
                return;
            }
            image_controls.set_after_image(FileManager.get_fx_img(processed_file));
        });

        predict_button.setOnAction(e -> {
            if (processed_file == null) {
                message_label.setText("No file selected.");
                return;
            }
            model_results.set_file(processed_file);
            model_results.predict();
        });

    }
}
