/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is used to display the image and the controls for the image.
 */
public class ImageControls extends VBox {
    private ImageView before_view = new ImageView();
    private ImageView after_view = new ImageView();
    private BorderPane before_pane = new BorderPane();
    private BorderPane after_pane = new BorderPane();
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            null, BorderWidths.DEFAULT));
    private Insets insets = new Insets(10, 10, 10, 10);
    private GridPane image_pane = new GridPane();
    private Label before_label = new Label("Preview");
    private Label after_label = new Label("Processed");

    public ImageControls() {
        // Before view
        before_view.fitWidthProperty().bind(before_pane.widthProperty());
        before_view.fitHeightProperty().bind(before_pane.heightProperty());
        before_view.setPreserveRatio(true);

        // After view
        after_view.fitWidthProperty().bind(after_pane.widthProperty());
        after_view.fitHeightProperty().bind(after_pane.heightProperty());
        after_view.setPreserveRatio(true);

        before_pane.setMinSize(250, 250);
        after_pane.setMinSize(250, 250);

        // Misc pane
        before_pane.setMaxSize(250, 250);
        after_pane.setMaxSize(250, 250);
        before_pane.setCenter(before_view);
        after_pane.setCenter(after_view);
        before_pane.setBorder(border);
        after_pane.setBorder(border);
        before_pane.setPadding(insets);
        after_pane.setPadding(insets);

        image_pane = new GridPane();
        image_pane.setHgap(10);

        image_pane.add(before_pane, 0, 0);
        image_pane.add(before_label, 0, 1);
        image_pane.add(after_pane, 1, 0);
        image_pane.add(after_label, 1, 1);

        before_label.setFont(new Font("Arial", 20));
        after_label.setFont(new Font("Arial", 20));
        before_label.setTextFill(Color.BLUE);
        after_label.setTextFill(Color.RED);


        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(image_pane);
    }

    /**
     * Sets the image to be displayed in the before view.
     * @param image The image to be displayed.
     */
    public void set_before_image(Image image) {
        before_view.setImage(image);
    }
    /**
     * Sets the image to be displayed in the after view.
     * @param image The image to be displayed.
     */
    public void set_after_image(Image image) {
        after_view.setImage(image);
    }

}
