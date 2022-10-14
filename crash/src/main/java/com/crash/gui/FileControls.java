/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.gui;

import java.io.File;
import java.util.List;

import com.crash.file.FileManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

/**
 * This class is responsible for the File Controls on the Monitor.
 */
public class FileControls extends VBox {
    private Label file_list_label = new Label("Files");
    private ListView<String> file_list = new ListView<String>();
    private ObservableList<String> items = FXCollections.observableArrayList();
    private HBox file_button_box = new HBox();
    private Button add_file_button = new Button("Add File");
    private Button remove_file_button = new Button("Remove File");
    private Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            null, BorderWidths.DEFAULT));
    private File file_selected = null;
    

    /**
     * Constructor for the FileControls class.
     */
    public FileControls() {
        List<String> files = FileManager.get_instance().get_img_lib_files().keySet().stream().toList();
        items = FXCollections.observableArrayList(files);
        file_list = new ListView<String>(items);

        // Properties
        file_list_label.setFont(new Font("Arial", 20));
        file_list_label.setTextFill(Color.ORANGERED);
        file_list.setBorder(border);
        file_list.setMaxSize(150, 250);
        file_list.setMinSize(150, 150);
        file_button_box = new HBox(add_file_button, remove_file_button);
        file_button_box.setSpacing(10);

        this.setSpacing(10);

        file_list.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                file_selected = FileManager.get_instance().get_img_lib_files().get(newSelection);
            }
        });
        this.getChildren().addAll(file_list_label, file_list, file_button_box);

        add_file_button.setOnAction(e -> {
            // File Chooser dialog, copy the choosen file into the img_lib folder
            FileChooser file_chooser = new FileChooser();
            file_chooser.setTitle("Open Car Image File");
            file_chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png",
                    "*.jpg"));
            File file = file_chooser.showOpenDialog(null);
            if (file != null) {
                FileManager.get_instance().add_file(file);
            }
            items = FXCollections.observableArrayList(FileManager.get_instance().get_img_lib_files().keySet().stream().toList());
            file_list.setItems(items);
        });

        remove_file_button.setOnAction(e -> {
            if (file_selected != null) {
                FileManager.get_instance().delete_file(file_selected);
                items = FXCollections.observableArrayList(FileManager.get_instance().get_img_lib_files().keySet().stream().toList());
                file_list.setItems(items);
            }
        });
    }

    /**
     * This method returns the file selected in the file list.
     * @return File selected
     */
    public File get_selected_file()
    {
        return file_selected;
    }

}
