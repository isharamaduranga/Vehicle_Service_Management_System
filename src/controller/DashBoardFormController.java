package controller;

import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CrudUtil;
import util.Utilities;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


public class DashBoardFormController extends IncomeFormController {


    public Label lblDate;
    public Label lblDay;
    public Label lblTime;
    public AnchorPane dashBoardContext;
    public AnchorPane fullDashBoardContext;
    public Circle d1;
    public Circle d2;
    public JFXButton btnCustomerMenu;
    public JFXButton btnOnGoingServiceMenu;
    public JFXButton btnServiceHistoryMenu;
    public JFXButton btnNextServiceMenu;
    public Label lblCustomers;
    public Label lblPendingRepairing;
    public Label lblPendingCleaning;
    public Label lblPendingServices;
    public Label lblVehicles;
    public Label lblOngoinServices;
    public Label lblInvoices;
    public Label lblInvoiceDetails;
    public Text lblText;
    public PieChart incomePieChart;


    boolean runThread=true;

    public void initialize() {
        /** tabel zoom in feature */
        Label [] labels= {lblCustomers,lblPendingRepairing,lblPendingCleaning,lblPendingServices,lblVehicles,
                lblOngoinServices,lblInvoices,lblInvoiceDetails};

        for (int i = 0; i < labels.length; i++) {
            new ZoomIn(labels[i]).play();
            new ZoomIn(incomePieChart).play();
        }

        bannerText();
        setDataPicChart();
        curDateTime();

        setRotate(d1, true, -180,15 );
        setRotate(d2, true, -180, 15);


        try {
            loadAllDashLabels();


        } catch (ClassNotFoundException |SQLException e) {
            e.printStackTrace();
        }


    }

    /** Define the Pie Chart for the income pogress */
    private void setDataPicChart() {
        // pie chart data create
        ObservableList<PieChart.Data> productData = FXCollections.observableArrayList();
        String [] month={"January","February","March","April","May","June","July","August","September","October","November","December"};
        try {

            for (int i = 1;i <= month.length ; i++) {
                ResultSet rs = CrudUtil.execute("SELECT SUM(charge) FROM service_complete WHERE MONTH(date) =?",i);
                while (rs.next()){
                    productData.add(new PieChart.Data(month[i-1],rs.getDouble(1)));
                }
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        productData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(data.getName()," ",data.pieValueProperty())
                )
        );
        // add data to the chart
        incomePieChart.setData(productData);
        incomePieChart.setTitle("MONTHLEY INCOME");
    }

    String[] bannerText ={"BODY WASH","REPAIRING","WHEEL ALIGNMENT","BATTERY SERVICES",
            "BRAKE SYSTEM","COOLING & HEATING SYSTEM","AIR CONDITIONS","OIL FILTER CHANGE","WIPER BLADES"};


    /** Function of Run to thread in DashBoard for the Print of banners*/
    public void bannerText() {

        Thread thread = new Thread(()->{
            while (runThread){
                try {
                    for (int i = 0; i < 9; i++) {
                        lblText.setText(bannerText[i]);
                        Thread.sleep(2000);
                    }

                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        thread.start();
    }

    /** Function of Load count of tabels records to the DashBoard */
    private void loadAllDashLabels() throws SQLException, ClassNotFoundException {


        ResultSet result1 = CrudUtil.execute("SELECT COUNT(*)  FROM customer");
        result1.next();
       lblCustomers.setText(String.valueOf(result1.getString(1)));

        ResultSet result2 = CrudUtil.execute("SELECT COUNT(*)  FROM add_vehicle");
        result2.next();
        lblVehicles.setText(String.valueOf(result2.getString(1)));

        ResultSet result3 = CrudUtil.execute("SELECT COUNT(cid)  FROM customer");
        result3.next();
        lblPendingServices.setText(String.valueOf(result3.getString(1)));

        ResultSet result4 = CrudUtil.execute("SELECT COUNT(*) FROM add_to_service WHERE status=1 AND stype='Cleanning'");
        result4.next();
        lblPendingCleaning.setText(String.valueOf(result4.getString(1)));

        ResultSet result5 = CrudUtil.execute("SELECT COUNT(*) FROM add_to_service WHERE status=1 AND stype='Repairing'");
        result5.next();
        lblPendingRepairing.setText(String.valueOf(result5.getString(1)));

        ResultSet result6 = CrudUtil.execute("SELECT COUNT(*) FROM add_to_service WHERE status=1 AND stype='Cleanning'AND 'Repairing'");
        result6.next();
        lblOngoinServices.setText(String.valueOf(result6.getString(1)));

        ResultSet result7 = CrudUtil.execute("SELECT COUNT(*) FROM service_complete WHERE status=1 OR 0");
        result7.next();
        lblInvoices.setText(String.valueOf(result7.getString(1)));

        ResultSet result8 = CrudUtil.execute("SELECT COUNT(*) FROM service_complete WHERE status=1 OR 0");
        result8.next();
        lblInvoiceDetails.setText(String.valueOf(result8.getString(1)));


    }

    /** Define the process of Dashboard car Animation */
    public void setRotate(Circle c, boolean reverse, int angle, int duration){
        RotateTransition r1 = new RotateTransition(Duration.seconds(duration),c);
        r1.setAutoReverse(reverse);

        r1.setByAngle(angle);
        r1.setDelay(Duration.seconds(0));
        r1.setRate(500);
        r1.setCycleCount(500);
        r1.play();
    }

    /** Display curent date and time in to the Dashboard*/
    public void curDateTime() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline clock=new Timeline(new KeyFrame(Duration.ZERO, event -> {

            LocalTime currentTime = LocalTime.now();

            lblTime.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());

            lblDay.setText(LocalDate.now().getDayOfWeek().toString());
        }),
                new KeyFrame(Duration.seconds(1))

        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /** Load the DashBoardForm  UI */
    public void dashBoardOnAction(ActionEvent actionEvent) throws IOException {

        Stage stage=(Stage) dashBoardContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));

    }

    /** Load the DashBoardForm  UI */
    public void addCustomerOnAction(ActionEvent actionEvent) throws IOException {
       Utilities.setUiChildren(dashBoardContext,"CustomerForm");
    }

    /** Load the addVehicle  UI */
    public void addVehicleOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"VehicleForm");
    }

    /** Load the AddToService  UI */
    public void AddToServiceOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"AddToServiceForm");
    }

    /** Load the ServiceResult  UI */
    public void ServiceResultOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"ServiceResultForm");
    }

    /** Load the VehicleReturn  UI */
    public void VehicleReturnOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"VehicleReturnForm");
    }

    /** Load the WaitingRepair  UI */
    public void WaitingRepairOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"WaitingRepairForm");
    }

    /** Load the WaitingClean  UI */
    public void WaitingCleanOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"WaitingCleanForm");
    }

    /** Load the ServicesForm  UI */
    public void ServicesOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"ServicesForm");
    }

    /** Load the IncomeForm  UI */
    public void IncomeOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(dashBoardContext,"IncomeForm");
    }

    /** Log out and Load the Login_SignUpForm  UI */
    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.close(fullDashBoardContext);
        Utilities.setUiNew("Login_SignUpForm","Log In Form");

    }

    /** USE Left Transition and Load the CustomerListForm  UI */
    public void customerListOnAction(ActionEvent actionEvent) throws IOException {

        Utilities.leftTransition("CustomerListForm",btnCustomerMenu,dashBoardContext);
    }

    /** USE Left Transition and Load the onGoingService  UI */
    public void onGoingServiceOnAction(ActionEvent actionEvent) throws IOException {

        Utilities.leftTransition("OngoingServiceForm",btnOnGoingServiceMenu,dashBoardContext);
    }

    /** USE Left Transition and Load the serviceHistoryForm  UI */
    public void serviceHistoryOnAction(ActionEvent actionEvent) throws IOException {
        //Utilities.setUiChildren(dashBoardContext,"ServiceHistoryForm");
        Utilities.leftTransition("ServiceHistoryForm",btnServiceHistoryMenu,dashBoardContext);
    }

    /** USE Left Transition and Load the NextServiceForm  UI */
    public void nextServiceOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.leftTransition("NextServiceForm",btnNextServiceMenu,dashBoardContext);
    }

    /** Function of Close the Program and Stop the Thread */
    public void btnShutDownOnAction(ActionEvent actionEvent) {
        runThread=false;
        System.exit(0);
    }
}
