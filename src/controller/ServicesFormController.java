package controller;

import animatefx.animation.ZoomIn;
import javafx.scene.image.ImageView;

public class ServicesFormController {
    public ImageView serviceChart;
    public void initialize(){
        /** png zoom in feature */
        new ZoomIn(serviceChart).play();
    }
}
