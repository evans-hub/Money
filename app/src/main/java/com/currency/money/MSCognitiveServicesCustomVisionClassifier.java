package com.currency.money;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import ai.customvision.CustomVisionManager;
import ai.customvision.tflite.ImageClassifier;

public class MSCognitiveServicesCustomVisionClassifier implements ICognitiveServicesClassifier, AutoCloseable {
    private static String TAG = MSCognitiveServicesCustomVisionClassifier.class.getSimpleName();


    private static String ModelManifestPath = "sample-tflite.cvmodel/cvexport.manifest";  // TensorFlow Lite model (.tflite)

    private ImageClassifier classifierRuntime;

    public MSCognitiveServicesCustomVisionClassifier(final Context context) {

        CustomVisionManager.setAppContext(context);

        ImageClassifier.Configuration config = ImageClassifier.ConfigurationBuilder()
                .setModelFile(ModelManifestPath).build();

        classifierRuntime = new ImageClassifier(config);
    }

    @Override
    public com.currency.money.Classifier.Recognition classifyImage(Bitmap sourceImage, int orientation) {

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight(), matrix, true);


        classifierRuntime.setImage(rotatedBitmap);
        classifierRuntime.run();


        rotatedBitmap.recycle();

        final float[] confidences = classifierRuntime.Confidences.getFloatVector();
        final String[] labels = classifierRuntime.Identifiers.getStringVector();
        for (int i = 0; i < confidences.length; i++) {
            final float confidence = confidences[i];
            final String label = labels[i];
            Log.i(TAG, String.format("Confidence: %.1f (%s)", confidence * 100f, label));
        }

        final float highestConfidence = confidences[0];
        final String labelForHighestConfidence = labels[0];

        return new com.currency.money.Classifier.Recognition("0", labelForHighestConfidence, highestConfidence, null);
    }

    @Override
    public void close() {

        classifierRuntime.close();
    }
}
