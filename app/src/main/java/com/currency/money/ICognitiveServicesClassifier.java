package com.currency.money;

import android.graphics.Bitmap;

public interface ICognitiveServicesClassifier {
    com.currency.money.Classifier.Recognition classifyImage(Bitmap sourceImage, int orientation);
    void close();
}
