package controller;

import animatefx.animation.Bounce;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import model.ServiceComplete;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ReportsUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class IncomeFormController {

    public TableView<ServiceComplete> tblIncomes;
    public TableColumn colSid;
    public TableColumn colDate;
    public TableColumn colAmount;
    public JFXComboBox monthLoadCombo;
    public Label lblMonthIncome;
    public Label lblTotalIncome;
    public JFXButton btnPrint;
    public ImageView totIncomeRectangle;
    public Label lblTotal;
    public Rectangle recMonth;



    public void initialize() {
        /** Define Animation of Zoom in */
        new ZoomIn(lblTotal).play();
        new ZoomIn(lblMonthIncome).play();
        new ZoomIn(recMonth).play();
        new ZoomIn(lblTotalIncome).play();
        new ZoomIn(totIncomeRectangle).play();

        colSid.setCellValueFactory(new PropertyValueFactory("sid"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory("charge"));


        try {
            calulateTotalIncome();
            tableLoad();
            comboLoad();

        } catch (Exception e) {

        }
    }
    String Month= new SimpleDateFormat("MM").format(new Date());
    String Year=new SimpleDateFormat("yyyy").format(new Date());



    /** Function of Calculate Total Income and Set To Label */
    private void calulateTotalIncome() {
        try {
            ResultSet result = CrudUtil.execute
                    ("SELECT SUM(charge) FROM service_complete");

                result.next();

            String s = result.getString(1);

            lblTotalIncome.setText(s);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** Function of comboBox loading */
    public void comboLoad() {
        ObservableList<String>obList=FXCollections.observableArrayList();
        obList.add("January");
        obList.add("February");
        obList.add("March");
        obList.add("April");
        obList.add("May");
        obList.add("June");
        obList.add("July");
        obList.add("August");
        obList.add("September");
        obList.add("October");
        obList.add("November");
        obList.add("December");
        monthLoadCombo.setItems(obList);
    }


    /** load the Data to the tabel */
    private void tableLoad() {
        try {
             ResultSet result = CrudUtil.execute("SELECT `sid`,`date`,`charge` FROM service_complete WHERE MONTH(date) =? And YEAR(date)=?",Month,Year);

            serviceCharge(result);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** Generate Monthley Income According to selected Month */
    public void ComboOnActionSelectMonth(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String value = String.valueOf(monthLoadCombo.getValue());

        ResultSet result = CrudUtil.execute("SELECT  `sid`,`date`,`charge` FROM service_complete WHERE MONTHNAME(date) =? And YEAR(date)=?",value,Year);

       ResultSet re = CrudUtil.execute("SELECT  SUM(`charge`) from service_complete WHERE MONTHNAME(date)=? AND YEAR(date)=? ",value,Year);

        serviceMonthIncome(re);
        serviceCharge(result);

    }

    /** Function of Calculate Monthley Income and Show As the Tabel Records */
    private void serviceMonthIncome(ResultSet result) throws SQLException {
        result.next();
        lblMonthIncome.setText(String.valueOf(result.getDouble(1)));
    }





    /** Function of Add Records(charges) to the Table */
    private void serviceCharge(ResultSet result) throws SQLException {
        ObservableList<ServiceComplete> income = FXCollections.observableArrayList();
        while (result.next()) {
            income.add(
                    new ServiceComplete(
                            result.getString(1),
                            result.getString(2),
                            result.getDouble(3)

            ));
        }
        tblIncomes.setItems(income);
    }



    /** Function of Print Jasper Report */
    public void printOnAction(ActionEvent actionEvent) {
        if(!tblIncomes.getItems().isEmpty()){
            String totalCgh=lblMonthIncome.getText();

            try {
                /** NO COMPILATION -LOAD AND VIEW */

                HashMap paramMap=new HashMap();
                paramMap.put("totalCharge",totalCgh);

                btnPrint.setDisable(false);

                //catch the report
                JasperReport compileReport = (JasperReport) JRLoader.loadObject(ReportsUtil.class.getResource("/view/reports/incomeDetails.jasper"));

                ObservableList items = tblIncomes.getItems();

                //Fill the Information which report needed
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblIncomes.getItems().toArray()));

                //then the report is ready let's view
                JasperViewer.viewReport(jasperPrint,false);

            } catch (JRException e) {
                e.printStackTrace();
            }

        }else{
            btnPrint.setDisable(true);
            tblIncomes.refresh();
        }
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("PRINT")){
            new Bounce(btnPrint).play();
        }
    }
}
