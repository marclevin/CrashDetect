/**
 * @author 220001291
 * @version Practical X
 */


package com.crash.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

import javafx.util.Pair;

/**
 * This class is responsible for executing the Tensorflow model.
 */
public class TensorExecutor {
    private static SavedModelBundle model;
    private static TensorExecutor instance;

    private TensorExecutor() {
        model = SavedModelBundle.load(".\\crash\\model\\", "serve");
    }

    /**
     * This method is used to get the instance of the TensorExecutor class.
     * @return The instance of the TensorExecutor class.
     */
    public static TensorExecutor get_instance() {
        if (instance == null) {
            instance = new TensorExecutor();
        }
        return instance;
    }

    /**
     * This method is used to execute the Tensorflow model.
     * @param file The file to execute the model on.
     * @return The result of the model execution.
     */
    public Pair<Float,Float> get_prediction(File file) {
        try {
            BufferedImage image = FileManager.get_tensor_img(file.getPath());
            image = TensorUtils.get_scaled_tensor_img(image, 224, 224);
            Map<String, Tensor> input = new HashMap<>();
            input.put("sequential_1_input", TensorUtils.get_image_tensor(image, 224, 224));
            Tensor result = model.function("serving_default").call(input).get("sequential_3");
            Float[] result_float = new Float[2];
            
            result_float[0] = result.asRawTensor().data().asFloats().getFloat(0);
            result_float[1] = result.asRawTensor().data().asFloats().getFloat(1);
            return new Pair<Float, Float>(result_float[0], result_float[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
