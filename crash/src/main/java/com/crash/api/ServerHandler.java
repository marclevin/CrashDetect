/**
 * @author 220001291
 * @version Practical X
 */


package com.crash.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.crash.file.FileManager;

/**
 * This handler class is responsible for running and killing an instance of the server.
 */
public class ServerHandler implements Runnable {
    private static ServerHandler instance = null;
    private ProcessBuilder pv;
    private Thread t;
    private Process prc;
    private boolean alive = false;
    private InputStreamReader isr;
    private BufferedInputStream bis;
    private Scanner sc;

    /**
     * This method returns true if the server is alive and false if it is not.
     * @return True if the server is alive and false if it is not.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * This is the method for aquiiring the instance of the ServerHandler class.
     * @return The instance of the ServerHandler class.
     */
    public static ServerHandler get_instance() {
        if (instance == null) {
            instance = new ServerHandler();
        }
        return instance;
    }

    private ServerHandler() {
    };

    private void init_server() {
        // Run the serverMain.exe in .\crash\api\serverMain.exe
        // This will start the server
        List<String> commands = new ArrayList<String>();
        commands.add("cmd.exe");
        commands.add("/K");
        commands.add("serverMain.exe");

        pv = new ProcessBuilder(commands);
        pv.directory(new File(".//crash//api"));
        try {
            prc = pv.start();
            init_streams();
            alive = true;
        } catch (IOException e) {
            System.err.println("Error starting server (ProcessBuilder Failure).");
        }

    }

    /**
     * This method is used to run the server instance.
     * @return if the server was started successfully, returns true, otherwise returns false.
     */
    public boolean run_server() {
        if (!alive) {
            try {
                File f = new File(".//crash//api//serverMain.exe");
                if (!f.exists()) {System.err.println("Error: serverMain.exe not found."); return false;}
                t = new Thread(this, "ServerHandler");
                t.start();
            } catch (Exception e) {
                System.err.println("Error starting server (Thread Failure).");
                return false;
            }
            return true;
        }
        return false;
    }

    private void init_streams() {
        isr = new InputStreamReader(prc.getInputStream());
        bis = new BufferedInputStream(prc.getInputStream());
        sc = new Scanner(bis);
    }

    private void close_streams() {
        try {
            if (isr != null) {
                isr.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (sc != null) {
                sc.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing streams.");
        }
    }

    /**
     * This method is used to kill the server instance.
     * @return if the server was killed successfully, returns true, otherwise returns false.
     */
    public boolean stop_server() {
        if (alive) {
            alive = false;
            try {
                Runtime.getRuntime().exec("taskkill /F /IM serverMain.exe");
            } catch (IOException e) {
                System.err.println("Unable to kill serverMain.exe, please confirm it is closed before continuing.");
            }
            prc.destroy();
            return true;
        }
        return false;
    }
    /**
     * Overridden run method from Runnable interface.
     */
    @Override
    public void run() {
        init_server();
        while (alive) {
            if (sc.hasNext()) {
                FileManager.write_server_log(sc.nextLine());
            }
        }
        close_streams();
    }

}
