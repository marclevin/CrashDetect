/**
 * @author 220001291
 * @version Practical X
 */


package com.crash.api;

/**
 * This enum represents the commands that can be sent to the server.
 */
public enum Command {

    GRAYSCALE("GrayScale"),
    CROP("Crop"),
    ORB("ORB"),
    DILATION("Dilation"),
    EROSION("Erosion"),
    CANNY("Canny"),
    FAST("Fast"),
    CANNYFEATURES("CannyFeatures"),
    FASTFEATURES("FastFeatures"),
    ORBFEATURES("ORBFeatures"),
    NONE("None");

    // The string representation of the command.
    public final String label;

    private Command(String label) {
        this.label = label;
    }
}
