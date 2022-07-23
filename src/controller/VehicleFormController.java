package controller;

import animatefx.animation.Bounce;
import animatefx.animation.Pulse;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.AddVehicle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class VehicleFormController extends DashBoardFormController {

    public AnchorPane vehicleContext;
    public JFXTextField txtVnum;
    public JFXTextField txtNic;
    public JFXTextField txtOname;
    public JFXTextField txtModelNum;
    public JFXTextField txtModel;
    public JFXComboBox cmbType;
    public JFXTextField txtDate;
    public TableView<AddVehicle> tblVehicles;
    public TableColumn colVnum;
    public TableColumn colNic;
    public TableColumn colOname;
    public TableColumn colVmodel;
    public TableColumn colModelNum;
    public TableColumn colFuelType;
    public TableColumn colDate;
    public JFXTextField txtSearchVehicleId;
    public JFXTextArea txtbill;
    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public  JFXButton btnPrint;
    public JFXButton btnClear;
    public Label lblLstCusNic;

    /** Define Linked-HashMap for the validation  */
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
    /** tabel zoom in feature */
        new ZoomIn(tblVehicles).play();

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnPrint.setDisable(true);

        colVnum.setCellValueFactory(new PropertyValueFactory("v_num"));
        colNic.setCellValueFactory(new PropertyValueFactory("nic"));
        colOname.setCellValueFactory(new PropertyValueFactory("name"));
        colVmodel.setCellValueFactory(new PropertyValueFactory("model"));
        colModelNum.setCellValueFactory(new PropertyValueFactory("model_num"));
        colFuelType.setCellValueFactory(new PropertyValueFactory("fuelType"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));

    try {
        tableLoad();
        loadTime();
        loadType();
        setlastCusId();
        tblVehicles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
                btnPrint.setDisable(false);
            }
        });
    }catch (Exception e){
        e.printStackTrace();
    }

        /** create validation pattern*/
        Pattern vehicleNumPattern = Pattern.compile("^([a-zA-Z]{1,3}|((?!0*-)[0-9]{1,3}))-[0-9]{4}(?<!0{4})");
        Pattern cusIdPattern = Pattern.compile("^(CI)[0-9]{4,5}$");
        Pattern vehicleModelPattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern modelNumberPattern = Pattern.compile("^[A-z0-9 -/]{5,20}$");

        map.put(txtVnum,vehicleNumPattern);
        map.put(txtNic,cusIdPattern);
        map.put(txtModel,vehicleModelPattern);
        map.put(txtModelNum,modelNumberPattern);
    }

    /** Auto fill some textFields After the key Released of Cid textField */
    public void textFields_Key_Released(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute
                ("SELECT `cid`, `c_name`, `c_adress`,`c_mo_num`,`c_email`,`c_date` FROM customer WHERE cid = ?",txtNic.getText());

        if(result.next()){
            txtOname.setText(result.getString("c_name"));
        }else{
            txtOname.setText("");
        }

        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response =  ValidationUtil.validate(map,btnAdd);
            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }

    /** find a last row value of CUSTOMER ID */
    public void setlastCusId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT cid FROM customer ORDER BY cid DESC LIMIT 1");

            if(result.next()){

                String rnno=result.getString("cid");

                lblLstCusNic.setText(rnno);
            }else{

            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /** set data to text fields after select the tabel */
    private void setData(AddVehicle v) {
        txtVnum.setText(v.getV_num());
        txtNic.setText(v.getNic());
        txtOname.setText(v.getName());
        txtModel.setText(v.getModel());
        txtModelNum.setText(v.getModel_num());
        txtModelNum.setText(v.getModel_num());
        cmbType.setValue(v.getFuelType());
        txtDate.setText(v.getDate());
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    /** LOAD CURRENT TIME TO TEXTFIELD */
    private void loadTime(){
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

    }

    /** Function of comboBox loading */
    private void loadType() {
        cmbType.getItems().add("Diesel");
        cmbType.getItems().add("Petrol");
        cmbType.getItems().add("Electronic");
    }

    /** Function of Add Record */
    public void AddVehicleOnAction(ActionEvent actionEvent) {

        if(!cmbType.getSelectionModel().isEmpty()){
            try {
                AddVehicle v = new AddVehicle(
                        txtVnum.getText(), txtNic.getText(), txtOname.getText(), txtModel.getText(),txtModelNum.getText(), cmbType.getSelectionModel().getSelectedItem().toString(),txtDate.getText());

                if (CrudUtil.execute("INSERT INTO add_vehicle VALUES(?,?,?,?,?,?,?)", v.getV_num(), v.getNic(),v.getName(),
                        v.getModel(), v.getModel_num(), v.getFuelType(), v.getDate())) {

                    new Alert(Alert.AlertType.CONFIRMATION, "Saved...").showAndWait();

                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer Already exists !!!").show();
                }
            } catch (ClassNotFoundException | SQLException e) {

                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            recipt();
            btnPrint.setDisable(false);
            loadTime();
            tableLoad();
        }else{
            new Alert(Alert.AlertType.WARNING, "Fuel Type Field is empty !!!").show();
        }




    }

    /** Function of Update Record */
    public void UpdateVehicleOnAction(ActionEvent actionEvent) {
        AddVehicle v = new AddVehicle(
          txtVnum.getText(), txtNic.getText(), txtOname.getText(), txtModel.getText(),txtModelNum.getText(), cmbType.getSelectionModel().getSelectedItem().toString(),txtDate.getText());
        try{


            if(CrudUtil.execute("UPDATE add_vehicle SET nic=?,name=?,v_model=?,model_num=?,fuel_type=? WHERE v_num=?"
                    ,v.getNic(),v.getName(),v.getModel(),v.getModel_num(),v.getFuelType(),txtVnum.getText())){

                new Alert(Alert.AlertType.CONFIRMATION,"Updated!").showAndWait();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
            }
        }catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
        clear();
        tableLoad();
        loadTime();

    }

    /** Function of Delete Record */
    public void DeleteVehicleOnAction(ActionEvent actionEvent) {
        try{

            if(CrudUtil.execute("DELETE FROM add_vehicle WHERE v_num=?",txtVnum.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted!").showAndWait();
                tableLoad();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException |NullPointerException  e){
            e.printStackTrace();
        }
        clear();
        loadTime();
    }

    /** load the Data to the tabel */
    public void tableLoad(){
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM add_vehicle");

            ObservableList<AddVehicle> observableList = FXCollections.observableArrayList();

            while (result.next()){
                observableList.add(
                        new AddVehicle(
                                result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7)
                        )
                );
            }
            tblVehicles.setItems(observableList);
            FilteredList<AddVehicle> filterData = new FilteredList<AddVehicle>(observableList, b -> true);

            txtSearchVehicleId.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(AddVehicle -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (AddVehicle.getV_num().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<AddVehicle> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblVehicles.comparatorProperty());
            tblVehicles.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }
    }

    /** function of print sample Recipt before to view jasper Report*/
    private  void recipt() {
        try {
            txtbill.appendText
                    (
                     "\n\n\t\t\t WELCOME !!! \n\n" +
                     "\t~~AUTO MODIFY SERVICE  CENTER~~ \n" +
                    "\t\tNo. 195/1/A Main Street, Colombo. \n" +
                    "\t\t0777-000000 / 000000000  \n" +
                    "\t\tEmail: isha9702@gmail.com  \n" +

                    "\n===============================\n\n" +
                    "\t\t~~~~~ SERVICE DETAILS ~~~~~\n\n" +
                    "\t\tVehicle Number : " +txtVnum.getText()+ "\n" +
                    "\t\tCustomer Id    :" +txtNic.getText()+ "\n" +
                    "\t\tCustomer Name  :" +txtOname.getText()+ "\n" +
                    "\t\tVehicle Model  : \t" +txtModel.getText()+ "\n" +
                    "\t\tModel No       : \t" +txtModelNum.getText()+ "\n" +
                    "\t\tFuel Type      : \t" +cmbType.getSelectionModel().getSelectedItem()+ "\n" +
                    "\t\tDate           : \t" +txtDate.getText()+ "\n" +

                    "\n===============================\n\n"+
                    "\t\t\tThank You...."+ "\n\n\n"

            );
        } catch (Exception e) {

        }
    }

    /** Function of clear textFields */
    public void clear(){
        txtVnum.clear();
        txtNic.clear();
        txtOname.clear();
        txtModel.clear();
        txtModelNum.clear();
        cmbType.setValue(null);
        txtDate.clear();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.setDisable(true);
        btnPrint.setDisable(true);
        try{
            txtbill.setText("");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Function of Print Jasper Report */
    public void printOnAction(ActionEvent actionEvent) {
        String txtVnumText = txtVnum.getText();
        String txtNicText = txtNic.getText();
        String txtOnameText = txtOname.getText();
        String txtModelText = txtModel.getText();
        String txtModelNumText = txtModelNum.getText();
        String fuelType = cmbType.getSelectionModel().getSelectedItem().toString();
        String dateText = txtDate.getText();

        HashMap paramMap=new HashMap();
        paramMap.put("vehicleNum",txtVnumText);
        paramMap.put("ownerNic",txtNicText);
        paramMap.put("ownerName",txtOnameText);
        paramMap.put("vehicleModel",txtModelText);
        paramMap.put("modelNum",txtModelNumText);
        paramMap.put("fuelType",fuelType);
        paramMap.put("date",dateText);

        try {
            /** NO COMPILATION -LOAD AND VIEW */

        //catch the report
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/AddVehicle.jasper"));

            //Fill the Information which report needed
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));

            //then the report is ready let's view
            JasperViewer.viewReport(jasperPrint,false);

           try{
                txtbill.setText("");

            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (JRException e) {
            e.printStackTrace();
        }


    }

    /** Function of clear process */
    public void btnClearFieldsOnAction(ActionEvent actionEvent) {
        clear();
        loadTime();
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("ADD")){
            new Pulse(btnAdd).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("UPDATE")){
            new Pulse(btnUpdate).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("DELETE")){
            new Pulse(btnDelete).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("CLEAR")){
            new Pulse(btnClear).play();
        }else{
            new Bounce(btnPrint).play();
        }

    }
}

















