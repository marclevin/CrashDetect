/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;

/**
 * This class is used to manage files across this project.
 */
public final class FileManager {
    private static final String IMG_LIB_DIR = ".\\crash\\data\\images\\source";
    private static final String FINAL_IMG_LIB_DIR = ".\\crash\\data\\images\\final";
    private static final String SERVER_LOG_FILE = ".\\crash\\data\\logs\\server.log";
    private static File imgLibDir = new File(IMG_LIB_DIR);
    private static File imgFinalLibDir = new File(FINAL_IMG_LIB_DIR);
    private static HashMap<String, File> imgLibFiles = new HashMap<String, File>();
    private static HashMap<String, File> imgFinalLibFiles = new HashMap<String, File>();
    private static FileManager instance = null;

    // Private Constructor
    private FileManager() {
        for (File f : imgLibDir.listFiles()) {
            imgLibFiles.put(f.getName(), f);
        }
        for (File f : imgFinalLibDir.listFiles()) {
            imgFinalLibFiles.put(f.getName(), f);
        }
    }

    /**
     * This method is used to get the instance of the FileManager class.
     * @return The instance of the FileManager class.
     */
    public static FileManager get_instance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * This method is used to get the final image library directory HashMap.
     * @return The image library directory.
     */
    public HashMap<String, File> get_final_img_lib_files() {
        return imgFinalLibFiles;
    }

    /**
     * This method is used to get a BufferedImage from a file path.
     * @param file_path The file path of the image.
     * @return The BufferedImage of the image.
     * @throws IOException If the file path is invalid.
     */
    public static BufferedImage get_tensor_img(String file_path) throws IOException {
        FileInputStream fis = new FileInputStream(file_path);
        BufferedImage image = ImageIO.read(fis);
        return image;
    }

    /**
     * This method returns the inputstream of a given file path
     * @param file_path The file path
     * @return The inputstream of the file path
     */
    public static InputStream get_input_stream(String file_path) {
        return FileManager.class.getClassLoader().getResourceAsStream(file_path);
    }

    /**
     * This method is used to get the image library directory HashMap.
     * @return
     */
    public HashMap<String, File> get_img_lib_files() {
        return imgLibFiles;
    }

    /**
     * This function updates the image library directory HashMap.
     */
    public void update_img_lib_files() {
        imgLibFiles.clear();
        for (File f : imgLibDir.listFiles()) {
            imgLibFiles.put(f.getName(), f);
        }
    }
    /**
     * This function adds an image to the image library directory.
     * @param file The file to add.
     */
    public void add_file(File file) {
        Path old = file.toPath();
        Path new_path = Paths.get(IMG_LIB_DIR + "\\" + file.getName());
        try {
            Files.copy(old, new_path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        update_img_lib_files();
    }

    /**
     * This function gets an image in encoded byte form
     * @param f The file to encode (image)
     * @return The encoded byte form of the image
     */
    public static byte[] get_img_encoded_bytes(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            byte[] bytes = new byte[(int) f.length()];
            fis.read(bytes);
            fis.close();
            return new String(Base64.getEncoder().encodeToString(bytes)).getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function saves a given image using the byte data of the image and a file name.
     * @param data The byte data of the image
     * @param name The name of the file
     * @return The file that was saved
     */
    public static File save_processed_img(byte[] data, String name) {
        File test = new File(".\\crash\\data\\images\\process\\" + name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(test);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return test;

    }

    /**
     * This function gets a file by name from the imgLibFile map.
     * @param name The file to found.
     * @return The file that was found.
     */
    public File get_file_by_name(String name) {
        if (imgLibFiles.containsKey(name)) {
            return imgLibFiles.get(name);
        } else return null;
    }

    /**
     * Deletes a given file.
     * @param f The file to be deleted.
     */
    public void delete_file(File f) {
        f.delete();
        update_img_lib_files();
    }

    /**
     * This function saves the final image to the final image library directory.
     * @param file The image to be saved
     * @return The file that was saved.
     */
    public static File save_final_img(File file) {
        File result = new File(FINAL_IMG_LIB_DIR + "/" + file.getName());
        try (FileOutputStream fos = new FileOutputStream(result)) {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This function cleans up the process files of the image library directory.
     */
    public static void cleanup_process_files() {
        File dir = new File(".\\crash\\data\\images\\process");
        for (File f : dir.listFiles()) {
            f.delete();
        }
    }

    /**
     * This function cleans up the log file for the application.
     */
    public static void cleanup_server_log_files()
    {
        File dir = new File(".\\crash\\data\\logs");
        for (File f : dir.listFiles()) {
            f.delete();
        }
    }

    /**
     * This function returns a JavaFX image from a given file.
     * @param file The file to be converted.
     * @return The image that was converted.
     */
    public static Image get_fx_img(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            Image image = new Image(fis);
            fis.close();
            return image;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function is used to clean up the final img library directory.
     */
    public static void cleanup_final_img() {
        File dir = new File(".\\crash\\data\\images\\final");
        for (File f : dir.listFiles()) {
            f.delete();
        }
    }

    /**
     * This function is used to retrieve the server log as a string.
     * @return The server log as a string.
     */
    public static String get_server_log()
    {
        // Get the server log file from SERVER_LOG_FILE
        // Read the file and return the string
        FileReader fr = null;
        BufferedReader br = null;
        Scanner sc = null;
        String log = "";
        File f = new File(SERVER_LOG_FILE);
        if (!f.exists()) {return log;}
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            sc = new Scanner(br);
            while (sc.hasNextLine()) {
                log += sc.nextLine() + "\r\n";
            }
            br.close();
            fr.close();
            sc.close();
        } catch (IOException e) {
            System.err.println("Error reading server log file");
        }
        return log;
    }

    /**
     * This function is used to write to the server log.
     * @param message The message to be written.
     */
    public static void write_server_log(String message)
    {
        // Write to log file SERVER_LOG_FILE
        FileWriter fw = null;
        BufferedWriter bw = null;
        File f = new File(SERVER_LOG_FILE);
        if (f.exists())
        {
            // Write the content of message to the log file
            try
            {
                fw = new FileWriter(f, true);
                bw = new BufferedWriter(fw);
                bw.write(message);
                bw.newLine();
                bw.close();
                fw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        } else
        {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating log file");
                e.printStackTrace();
            }
            write_server_log(message);
        }
    }




}