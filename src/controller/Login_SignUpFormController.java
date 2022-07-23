
package controller;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;


public class Login_SignUpFormController implements Initializable {

    public AnchorPane layersignup;
    public AnchorPane layer2;
    public JFXButton signin;
    public Label l1;
    public Label l2;
    public Label l3;
    public Label s1;
    public Label s2;
    public Label s3;
    public JFXButton signup;
    public Label a2;
    public Label b2;
    public Label a1;
    public Label b1;
    public JFXButton btnsignup;
    public JFXButton btnsignin;
    public JFXTextField u1;
    public JFXTextField u2;
    public JFXTextField u3;
    public TextField n1;
    public TextField n2;
    public AnchorPane layer1;
    public ImageView emailIcon;
    public ImageView pwordIconA;
    public ImageView pwdiconB;
    public Circle pinkCircle;
    public Circle yellowCircle;
    public Circle blueCircle;
    public TextField pwdShowTextField;
    public JFXCheckBox p3;
    public StackPane loginContext;
    public Circle newCircle;
    public Text userNameValid;
    public Text passwordValid;
    public Text emailValid;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (SplashWindowFormController.isDone) {
            loadSplashScreen();
        }

        s1.setVisible(false);
        s2.setVisible(false);
        s3.setVisible(false);
        signup.setVisible(false);
        b1.setVisible(false);
        b2.setVisible(false);
        btnsignin.setVisible(false);
        n1.setVisible(false);
        n2.setVisible(false);
        p3.setVisible(false);
        pwdShowTextField.setVisible(false);
        emailIcon.setVisible(true);
        pwordIconA.setVisible(false);
        u1.setVisible(true);
        u2.setVisible(true);
        u3.setVisible(true);
    }

    /** Load below Transition function */
    public void btn(MouseEvent event) {
        loadTransition();

    }

    /** Process of Splash Screen Feature */
    private void loadSplashScreen() {
        SplashWindowFormController.isDone = false;

        try {

            /** Load splash screen view FXML */
            StackPane pane = FXMLLoader.load(getClass().getResource(("../view/SplashWindowForm.fxml")));
            //Add it to root container (Can be StackPane, AnchorPane etc)
            loginContext.getChildren().setAll(pane);

            /** Load splash screen with fade in effect */
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            /** Finish splash with fade out effect */
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            /** After fade in, start fade out */
            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            /** After fade out, load actual content  */
            fadeOut.setOnFinished((e) -> {
                try {
                   /* StackPane parentContent = FXMLLoader.load(getClass().getResource(("../view/Login_SignUpForm.fxml")));
                    loginContext.getChildren().setAll(parentContent);*/

                    Stage stage = (Stage) loginContext.getScene().getWindow();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Login_SignUpForm.Fxml"))));
                    stage.centerOnScreen();

                } catch (IOException ex) {
                    Logger.getLogger(Login_SignUpFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Login_SignUpFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Process of LOGIN UI Transition Feature */
    private void loadTransition() {

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);
        slide.setToX(691);
        slide.play();

        layer1.setTranslateX(-400);
        btnsignin.setVisible(true);
        b1.setVisible(true);
        b2.setVisible(true);
        s1.setVisible(true);
        s2.setVisible(true);
        s3.setVisible(true);
        signup.setVisible(true);
        l1.setVisible(false);
        l2.setVisible(false);
        l3.setVisible(false);
        signin.setVisible(false);
        a1.setVisible(false);
        a2.setVisible(false);
        btnsignup.setVisible(false);
        n1.setVisible(true);
        n2.setVisible(true);
        p3.setVisible(true);
        pwdShowTextField.setVisible(false);
        u1.setVisible(false);
        u2.setVisible(false);
        u3.setVisible(false);
        pwordIconA.setVisible(true);
        emailIcon.setVisible(false);
        pwdiconB.setVisible(false);
        pinkCircle.setVisible(false);
        newCircle.setVisible(false);

        slide.setOnFinished((e->{

        }));
    }

    public void btn2(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);
        slide.setToX(0);
        slide.play();
        
        layer1.setTranslateX(0);
        btnsignin.setVisible(false);
        b1.setVisible(false);
        b2.setVisible(false);
        s1.setVisible(false);
        s2.setVisible(false);
        s3.setVisible(false);
        signup.setVisible(false);
        l1.setVisible(true);
        l2.setVisible(true);
        l3.setVisible(true);
        signin.setVisible(true);
        a1.setVisible(true);
        a2.setVisible(true);
        btnsignup.setVisible(true);
        n1.setVisible(false);
        n2.setVisible(false);
        p3.setVisible(false);
        pwdShowTextField.setVisible(false);
        u1.setVisible(true);
        u2.setVisible(true);
        u3.setVisible(true);
        pwordIconA.setVisible(false);
        emailIcon.setVisible(true);
        pwdiconB.setVisible(true);
        newCircle.setVisible(true);
        
        slide.setOnFinished((e->{
        
        }));
    }

    /** Function of Add Records */
    public void btnsignup(MouseEvent event) throws SQLException, ClassNotFoundException {

        if(!(u1.getText().equals("")|| u2.getText().equals("") || u3.getText().equals(""))){

            User u = new User(
                    u1.getText(),u2.getText(),u3.getText());
            try {

                if(CrudUtil.execute("INSERT INTO user VALUES(?,?,?)",u.getUserName(),u.getEmail(),u.getPassword())){

                    String title = "Sign Up Successfully";
                    TrayNotification tray = new TrayNotification();
                    AnimationType type = AnimationType.POPUP;

                    tray.setAnimationType(type);
                    tray.setTitle(title);
                    tray.setNotificationType(NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(3000));
                }
            }catch (ClassNotFoundException | SQLException e){

                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            loadTransition();
        }else{
            String tilte = "Sign Up";
            String message = "Something Went Wrong  Check fields";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.WARNING);
            tray.showAndDismiss(Duration.millis(3000));
        }


    }

    /** Function of Sign In process and Define pop up Notifacation system */
    public void sign(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {

        String UserId=null;
        if (n1.getText().equals("")){
            String tilte = "Sign In";
            String message = "Blank Username Field";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));
        }else {
            String userName = null;
            boolean user = true;
            boolean next = false;
            boolean password=true;
            ResultSet resultset= CrudUtil.execute("SELECT * FROM User");
            while (resultset.next()){
                String username=resultset.getString(1);

                if (username.equals(n1.getText())) {
                    UserId=resultset.getString(1);
                    userName=username;
                    user=false;
                    next=true;
                }
            }
            if (user){
                String tilte = "Sign In";
                String message = "Error Username "+"'"+n1.getText()+"'"+" Wrong";
                TrayNotification tray = new TrayNotification();
                AnimationType type = AnimationType.POPUP;

                tray.setAnimationType(type);
                tray.setTitle(tilte);
                tray.setMessage(message);
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));


                password=false;
            }
            if (next){
                if (n2.getText().equals("")){
                    String tilte = "Sign In";
                    String message = "Blank Password Field";
                    TrayNotification tray = new TrayNotification();
                    AnimationType type = AnimationType.POPUP;

                    tray.setAnimationType(type);
                    tray.setTitle(tilte);
                    tray.setMessage(message);
                    tray.setNotificationType(NotificationType.ERROR);
                    tray.showAndDismiss(Duration.millis(3000));
                    password=false;
                }else {
                    ResultSet resultSet= CrudUtil.execute("SELECT * FROM user WHERE userName=?",n1.getText());
                    while (resultSet.next()){
                        String tilte = "Sign in ... ";
                        String message = "Sign In Successfully";
                        TrayNotification tray = new TrayNotification();
                        AnimationType type = AnimationType.POPUP;

                        tray.setAnimationType(type);
                        tray.setTitle(tilte);
                        tray.setMessage(message);
                        tray.setNotificationType(NotificationType.SUCCESS);
                        tray.showAndDismiss(Duration.millis(3000));


                        if (resultSet.getString(2).equals(n2.getText())){
                            Stage stage= (Stage)loginContext.getScene().getWindow();
                            stage.close();

                            URL resource = getClass().getResource("../view/DashBoardForm.fxml");
                            Parent load = FXMLLoader.load(resource);
                            Scene scene = new Scene(load);
                            Stage stage1= new Stage();
                            stage1.setScene(scene);
                            stage1.centerOnScreen();
                            stage1.show();

                            password=false;
                        }
                    }
                }
            }
            if (password){
                String tilte = "Password is Wrong... ";
                String message = "Error Password "+"'"+n2.getText()+"'"+" Wrong";
                TrayNotification tray = new TrayNotification();
                AnimationType type = AnimationType.POPUP;

                tray.setAnimationType(type);
                tray.setTitle(tilte);
                tray.setMessage(message);
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            }
        }
    }

    public void click(ActionEvent event) throws IOException {
    }

    public void signUpOnAction(ActionEvent actionEvent) {
    }

    /** Function of Show Password and Not */
    public void showToCheckPasswordOnAction(ActionEvent actionEvent) {
        if (p3.isSelected()){
            pwdShowTextField.setText(n2.getText());
            pwdShowTextField.setVisible(true);
            n2.setVisible(false);
        }else{
            pwdShowTextField.setVisible(false);
            n2.setVisible(true);
        }
    }

    /** Define the function of cross to among the textFields(use Enter press)  */
    public void textFields_Key_Released(KeyEvent keyEvent) {
        validate();
        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response =  validate();
            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }

    /** create validation pattern for the sign up*/
    public Object validate() {
        Pattern userNamePattern = Pattern.compile("^[A-z]{3,20}$");
        Pattern EmailPattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        Pattern passwordPattern = Pattern.compile("^[A-z0-9]{4,8}$");

        //get the   type value
        if(!userNamePattern.matcher(u1.getText()).matches()){
            //If the not matching
            addError(u1);
            addText(userNameValid,u1);
            return u1;
        }else {
            //If the input in matching
            removeError(u1);
            removeText(userNameValid);

            if (!EmailPattern.matcher(u2.getText()).matches()){
                addError(u2);
                addText(emailValid,u2);
                return u2;
            }else{
                removeError(u2);
                removeText(emailValid);

                if(!passwordPattern.matcher(u3.getText()).matches()){
                    addError(u3);
                    addText(passwordValid,u3);
                    return u3;
                }else{
                    removeError(u3);
                  removeText(passwordValid);
                }
            }
        }
        return true;
    }

    /** Function of Remove error and texfield colur change */
    private void removeError(JFXTextField text) {
        text.setStyle("-jfx-unfocus-color:green");
        text.setStyle("-jfx-focus-color:green");
    }

    /** Function of Add error and texfield colur change */
    private void addError(JFXTextField text) {
        if(text.getText().length()>0){
            text.setStyle("-jfx-unfocus-color:red");
            text.setStyle("-jfx-focus-color:red");
        }
    }

    /** Function of Remove error and BUTTON Visible process */
    private void removeText(Text text1) {
        text1.setVisible(false);
    }

    /** Function of Add error and BUTTON Visible process */
    private void addText(Text text2,JFXTextField text) {
        if(text.getText().length()>0){
           text2.setVisible(true);
        }
    }

}

