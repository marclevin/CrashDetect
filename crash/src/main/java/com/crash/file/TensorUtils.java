/**
 * @author 220001291
 * @version Practical X
 */


package com.crash.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;


import org.tensorflow.Tensor;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat32;

/**
 * This class is responsible for handling the Tensorflow image and provides utility.
 */
public class TensorUtils {
    /**
     * This method is used to get the Tensor for the Tensorflow model.
     * @param image The image to scale.
     * @param width The width of the image.
     * @param height The height of the image.
     * @return
     */
    public static Tensor get_image_tensor(BufferedImage image, int width, int height) {

        final int channels = 3;
        // Generate image file to array
        int index = 0;
        FloatBuffer fb = FloatBuffer.allocate(width * height * channels);
        // Convert image file to multi-dimension array

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                int pixel = image.getRGB(column, row);

                float red = (pixel >> 16) & 0xff;
                float green = (pixel >> 8) & 0xff;
                float blue = pixel & 0xff;
                red = (red - 127.5f) / 127.5f;
                green = (green-127.5f) / 127.5f;
                blue = (blue-127.5f) / 127.5f;
                fb.put(index++, red);
                fb.put(index++, green);
                fb.put(index++, blue);
            }
        }
        Shape shape = Shape.of(1, width, height, channels);
        Tensor data = Tensor.of(TFloat32.class, shape);
        data.asRawTensor().data().asFloats().write(fb.array());
        return data;
    }

    /**
     * This method is used to get the scaled image for the Tensorflow model.
     * @param img The image to scale.
     * @param width The desired width of the image.
     * @param height The desired height of the image.
     * @return The scaled image.
     */
    public static BufferedImage get_scaled_tensor_img(BufferedImage img, int width, int height) {
        if(img.getWidth() != width || img.getHeight() != height) {
            Image new_img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage new_buffered_img = new BufferedImage(new_img.getWidth(null),
                    new_img.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            new_buffered_img.getGraphics().drawImage(new_img, 0, 0, null);
            return new_buffered_img;
        }
        return img;
    }
}