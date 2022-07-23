package controller;

import animatefx.animation.Bounce;
import animatefx.animation.Pulse;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import model.ServiceComplete;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class VehicleReturnFormController {

    public TableView<ServiceComplete> tblServiceComplete;
    public TableColumn colSId;
    public TableColumn colVnum;
    public TableColumn colModel;
    public TableColumn colCusId;
    public TableColumn colCusName;
    public TableColumn colService;
    public TableColumn colItemReplace;
    public TableColumn colResult;
    public TableColumn colOther;
    public TableColumn colDate;
    public TableColumn colCharge;
    
    public JFXTextField txtSid;
    public JFXTextField txtModelNumber;
    public JFXComboBox<String> cmbVehicleNum;
    public JFXDatePicker dtDate;
    public JFXTextArea txtBill;
    public JFXTextField txtSearchCusNic;
    public JFXTextField txtSearchVnum;

    public String falt;
    public String rdate;
    public JFXButton btnSendToBill;
    public JFXButton btnPrint;
    public JFXButton returnToCustomer;
    public JFXButton btnClear;
    String sid;
     String vnum;
     String model;
     String cid;
     String cname;
     String service;
     String itemre;
     String result;
     String other;
     String date;
     String charge;

    public void initialize(){
        /** tabel zoom in feature */
        new ZoomIn(tblServiceComplete).play();

        btnSendToBill.setDisable(true);
        btnPrint.setDisable(true);
        returnToCustomer.setDisable(true);

        colSId.setCellValueFactory(new PropertyValueFactory("sid"));
        colVnum.setCellValueFactory(new PropertyValueFactory("v_number"));
        colModel.setCellValueFactory(new PropertyValueFactory("model"));
        colCusId.setCellValueFactory(new PropertyValueFactory("cid"));
        colCusName.setCellValueFactory(new PropertyValueFactory("cname"));
        colService.setCellValueFactory(new PropertyValueFactory("service"));
        colItemReplace.setCellValueFactory(new PropertyValueFactory("item_replace"));
        colResult.setCellValueFactory(new PropertyValueFactory("result"));
        colOther.setCellValueFactory(new PropertyValueFactory("other"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colCharge.setCellValueFactory(new PropertyValueFactory("charge"));

        try{

            tableLoad();
            ComboBoLoad();

        }catch(Exception e){
            e.printStackTrace();
        }
        tblServiceComplete.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                txtSid.setText(newValue.getSid());
                txtModelNumber.setText(newValue.getModel());
                cmbVehicleNum.setValue(newValue.getV_number());
                sid=newValue.getSid();
                vnum=newValue.getV_number();
                model=newValue.getModel();
                cid=newValue.getCid();
                cname=newValue.getCname();
                service=newValue.getService();
                itemre=newValue.getItem_replace();
                result=newValue.getResult();
                other=newValue.getOther();
                date=newValue.getDate();
                charge=newValue.getCharge();

                txtBill.setText("");

            }else{
            }
        });

    }

    /** Function of check satus and update data */
    private void update(){
        try{
            CrudUtil.execute("UPDATE service_complete SET `status`=? WHERE sid=?",1,txtSid.getText());

        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }

    /** function of print sample Recipt before to view jasper Report*/
    public  void recipt(){

        txtBill.appendText(" xxxxxxxxxxxxxxxxx \n" +
                "  No. C/195/Main Road,Colombo. \n" +
                "  0777-6127572 / 011- 1234567  \n" +
                "  Email: AutoModify@yahoo.com  \n" +

                "\n=================================\n\n" +
                "Service ID: \t" +sid+ "\n" +
                "Vehicle Number: " +vnum+ "\n" +
                "Model Name: \t" +model+ "\n" +
                "NIC: " +cid+ "\n" +
                "Service: " +service+ "\n" +
                "Item Replace:" +itemre+ "\n" +
                "Result:" +result+ "\n" +
                "Charge:" +charge+ "\n" +
                "Next Service Date:" +dtDate.getValue().toString()+ "\n" +

                "\n=================================\n"+
                "xxxxxxxxxxxxxxxxxxxxxxxxxxx"+"\n"
        );
    }

    /** Function of Return the customer record */
    public void btnReturnCustomerOnAction(ActionEvent actionEvent) throws ParseException, SQLException, ClassNotFoundException {
        int x=txtSid.getText().length();

        if(x>0 && (dtDate.getValue() != null)){
            nextService();
            update();
            tableLoad();
            clear();
        }else{
            new Alert(Alert.AlertType.WARNING,"Something Went Wrong.. Please  check to set date or sid").show();
        }
    }

    /** Function of add to Next Service Database */
   private void nextService(){
        try{
           CrudUtil.execute("INSERT INTO `next_service`(`v_number`,`ns_date`,`cid`) VALUES (?,?,?)",cmbVehicleNum.getValue(),dtDate.getValue(),cid);

        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }

    /** Function of clear textFields */
    private void clear() throws SQLException, ClassNotFoundException {
        txtSid.clear();
        txtModelNumber.clear();
        ComboBoLoad();
        dtDate.setValue(null);
        txtSearchCusNic.clear();
        txtSearchVnum.clear();
        btnSendToBill.setDisable(true);
        btnPrint.setDisable(true);
        returnToCustomer.setDisable(true);
    }

    /** Function of Print Jasper Report */
    public void btnBllPrintOnAction(ActionEvent actionEvent) {


        String sid = this.sid;
        String vnum = this.vnum;
        String model = this.model;
        String cid = this.cid;
        String service = this.service;
        String itemre = this.itemre;
        String result = this.result;
        String charge = this.charge;
        String date = dtDate.getValue().toString();

        HashMap paramMap=new HashMap();
        paramMap.put("sid",sid);
        paramMap.put("Vnum",vnum);
        paramMap.put("mName",model);
        paramMap.put("ownerId",cid);
        paramMap.put("service",service);
        paramMap.put("itemReplace",itemre);
        paramMap.put("result",result);
        paramMap.put("charge",charge);
        paramMap.put("ndate",date);

        try {
            /** NO COMPILATION -LOAD AND VIEW */

            //catch the report
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/VehicleReurn.jasper"));

            //Fill the Information which report needed
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));

            //then the report is ready let's view
            JasperViewer.viewReport(jasperPrint,false);

            int x=txtBill.getText().length();

            if(x>0){
                try{
                    txtBill.setText("");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"No data to print").show();
            }

        } catch (JRException e) {
            e.printStackTrace();
        }
        returnToCustomer.setDisable(false);
    }

    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            ResultSet result = CrudUtil.execute
                    ("SELECT `sid`,`v_number`,`model`,`cid`,`cname`,`service`,`item_replace`,`result`,`other`,`date`,`charge` FROM service_complete WHERE status=0");

            ObservableList<ServiceComplete> observableList = FXCollections.observableArrayList();

            while (result.next()){
                observableList.add(
                        new ServiceComplete(
                                result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                result.getString(9),
                                result.getString(10),
                                result.getString(11)
                        )
                );
            }
            tblServiceComplete.setItems(observableList);
            FilteredList<ServiceComplete> filterData = new FilteredList<ServiceComplete>(observableList, b -> true);

            txtSearchVnum.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(ServiceComplete -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (ServiceComplete.getV_number().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtSearchCusNic.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(ServiceComplete -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (ServiceComplete.getCid().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<ServiceComplete> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblServiceComplete.comparatorProperty());
            tblServiceComplete.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }
    }

    /** Function of clear process */
    public void btnClearOnaction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clear();
        tableLoad();
    }

    /** Function of comboBox loading */
    public void  ComboBoLoad() throws SQLException, ClassNotFoundException {

                try{ResultSet result = CrudUtil.execute
                        ("SELECT v_number from service_complete");

                    ObservableList<String> observableList = FXCollections.observableArrayList();
                    while (result.next()){
                       observableList.add(result.getString("v_number"));
                    }
                    cmbVehicleNum.setItems(observableList);
                }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
                }

    }

    /** funcion of btn  disable(send service ) */
    public void btnSendToServiceOnAction(ActionEvent actionEvent) {
        recipt();
        btnPrint.setDisable(false);
    }

    /** funcion of btn  disable (sendtobill)*/
    public void nextServiceMouseClicked(MouseEvent mouseEvent) {
            btnSendToBill.setDisable(false);
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("CLEAR")){
            new Bounce(btnClear).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("     SEND TO BILL")) {
            new Bounce(btnSendToBill).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("RETURN TO CUSTOMER")){
            new Bounce(returnToCustomer).play();
        }else{
            new Bounce(btnPrint).play();
        }


    }
}
