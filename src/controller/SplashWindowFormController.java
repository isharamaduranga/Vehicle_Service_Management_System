package controller;


import javafx.animation.RotateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class SplashWindowFormController {
    public static boolean isDone=true;

    public StackPane paneOfFirst;
    public Circle c11;
    public Circle c22;
    public Circle c1;
    public Circle c2;



    public void initialize(){
        setRotate(c1, true, -180,15 );
        setRotate(c2, false, -180, 15);
        setRotate(c11, true, -180,15 );
        setRotate(c22, false, -180, 15);



    }

    /** Define the process od splasg window animation effect */
    public void setRotate(Circle c, boolean reverse, int angle, int duration){
        RotateTransition r1 = new RotateTransition(Duration.seconds(duration),c);
        r1.setAutoReverse(reverse);

        r1.setByAngle(angle);
        r1.setDelay(Duration.seconds(0));
        r1.setRate(500);
        r1.setCycleCount(400);
        r1.play();
    }


}
