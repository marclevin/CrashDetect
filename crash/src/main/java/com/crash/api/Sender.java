/**
 * @author 220001291
 * @version Practical X
 */

package com.crash.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

import com.crash.file.FileManager;

/**
 * This class is used to send data to the server and recieve data from the server.
 */
public class Sender {
    private Socket server = null;
    private DataOutputStream dos = null;
    private BufferedReader br = null;
    private String address;
    private int port;
    private static String message = null;
    // This is the common command set used for processing images for the AI.
    public static Command[] process_commands = {Command.GRAYSCALE, Command.EROSION, Command.CANNY};


    private boolean open_streams() {
        try {
            server = new Socket(address, port);
            server.setSoTimeout(1000);
            dos = new DataOutputStream(new BufferedOutputStream(server.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(server.getInputStream()));
            message = "";
            return true;
        } catch (UnknownHostException e) {
            message = "Unknown host: " + address;
            return false;
        } catch (IOException e) {
            message = "Could not connect to server: " + address;
            return false;
        }
    }

    private void close_streams() {
        try {
            dos.close();
            br.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the constructor ofr the Sender class
     * @param address The address to send to.
     * @param port The port to send to.
     */
    public Sender(String address, int port) {
        this.address = address;
        this.port = port;
    };

    /**
     * This command sends a given file to the server and returns the processed file
     * @param file The file to send to the server
     * @param command The command to send to the server
     * @return
     */
    public File process(File file, Command command) {
        Command[] command_arr = { command };
        return this.process(file, command_arr);
    }

    /**
     * This command sends a given file to the server and returns the processed file
     * @param file The file to be sent to the server
     * @param commands The commands to be executed on the server
     * @return The processed file
     */
    public File process(File file, Command[] commands) {
        for (Command command : commands) {
            file = send(file, command);
            if (file == null)
            {
                return null;
            }
        }
        return FileManager.save_final_img(file);
    }

    /**
     * Gets the message from the Sender class if an issue was raised.
     * @return The message
     */
    public static String get_message()
    {
        return message;
    }

    // Sends file to server and returns the processed file
    private File send(File file, Command command) {
        if (!open_streams()) {
            return null;
        }
        try {
            byte[] bytes = FileManager.get_img_encoded_bytes(file);
            dos.write(("POST /api/" + command.label + " HTTP/1.1\r\n").getBytes());
            dos.write(("Content-Type: application/text\r\n").getBytes());
            dos.write(("Content-Length: " + bytes.length + "\r\n").getBytes());
            dos.write(("\r\n").getBytes());
            dos.write(bytes);
            dos.flush();
            dos.write(("\r\n").getBytes());

            String response = "";
            String line = "";
            // Reading
            while (!(line = br.readLine()).equals("")) {
                response += line + "\n";
            }
            // Guard
            if (!response.contains("200 OK")) {
                close_streams();
                // Bad request or otherwise.
                return null;
            }
            // Reading img data.
            String img_data = "";
            while ((line = br.readLine()) != null) {
                img_data += line;
            }

            String base = img_data.substring(img_data.indexOf('\'') + 1, img_data.lastIndexOf('}') - 1);
            byte[] decoded = Base64.getDecoder().decode(base);
            // Save file
            return FileManager.save_processed_img(decoded, file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        close_streams();
        return null;
    }

}
