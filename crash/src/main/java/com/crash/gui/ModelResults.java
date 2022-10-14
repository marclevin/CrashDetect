/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.gui;

import java.io.File;

import com.crash.file.TensorExecutor;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

/**
 * This class is used to display the results of the model.
 */
public class ModelResults extends StackPane {
    private static TensorExecutor tensor_executor = TensorExecutor.get_instance();

    private Label crashed_label = new Label("Crashed Prediction:");
    private Label title_label = new Label("Results:");
    private Label not_crashed_label = new Label("Safe Prediction:");
    private Label crashed_percentage_label = new Label("0%");
    private Label not_crashed_percentage_label = new Label("0%");
    private ProgressBar crashed_progress = new ProgressBar();
    private ProgressBar not_crashed_progress = new ProgressBar();
    private GridPane grid = new GridPane();
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            null, BorderWidths.DEFAULT));
    private File file = null;

    /**
     * This constructor is used to create the results pane.
     */
    public ModelResults() {
        crashed_progress.setProgress(0);
        crashed_progress.setStyle("-fx-accent: red;");
        not_crashed_progress.setProgress(0);
        not_crashed_progress.setStyle("-fx-accent: green;");
        title_label.setFont(new Font("Arial", 15));
        title_label.setTextFill(Color.ORANGERED);


        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(title_label, 0, 0);
        grid.add(crashed_label, 0, 1);
        grid.add(crashed_progress, 1, 1);
        grid.add(not_crashed_label, 0, 2);
        grid.add(not_crashed_progress, 1, 2);
        grid.add(crashed_percentage_label, 2, 1);
        grid.add(not_crashed_percentage_label, 2, 2);
        this.getChildren().add(grid);
        this.setBorder(border);
        this.setPadding(new Insets(10, 0, 10, 5));
        this.setMaxWidth(300);
        this.setMinWidth(300);
        this.setMinHeight(200);
        this.setMaxHeight(200);
    }

    /**
     * This method is used to set the file to be used for the prediction.
     * @param file The file to be used for the prediction.
     */
    public void set_file(File file) {
        this.file = file;
    }


    /**
     * This function is used to call the tensor exeuctor and get the results.
     */
    public void predict()
    {
        if (file == null) {
            return;
        }
        Pair<Float, Float> results = tensor_executor.get_prediction(file);
        crashed_progress.setProgress(results.getKey().doubleValue());
        not_crashed_progress.setProgress(results.getValue().doubleValue());
        crashed_percentage_label.setText(String.format("%.2f", results.getKey() * 100) + "%");
        not_crashed_percentage_label.setText(String.format("%.2f", results.getValue() * 100) + "%");
    }
}
