package controller;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import util.CrudUtil;
import util.Utilities;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerFormController {
    public AnchorPane addCustomerContext;
    public JFXTextField txtCid;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtEmail;
    public JFXTextField txtMtp;
    public JFXDatePicker dtDate;
    public TableView<Customer> tblCustomers;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colTpNumber;
    public TableColumn colEmail;
    public TableColumn colDate;
    public JFXTextField txtSearchCusId;
    public JFXTextField txtSearchCusName;
    public JFXButton btnAdd;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnAddVehicle;

    /** Define Linked-HashMap for the validation  */
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        /** tabel zoom in feature */
        new ZoomIn(tblCustomers).play();

        btnAdd.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory("cid"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colTpNumber.setCellValueFactory(new PropertyValueFactory("mtp"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));


        try{
            tableLoad();
            autoId();
            tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue!=null){
                    setData(newValue);

                }
            });
            }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        /** create validation pattern*/
        //Create a pattern and compile it to use
        Pattern namePattern = Pattern.compile("^[A-z]{3,20}$");
        Pattern AddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern TPNumberPattern = Pattern.compile("^(?:7|0|(?:\\+94))[0-9]{9}$");
        Pattern EmailPattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
        //add pattern and text to the map
        map.put(txtName,namePattern);
        map.put(txtAddress,AddressPattern);
        map.put(txtMtp,TPNumberPattern);
        map.put(txtEmail,EmailPattern);

    }

    /** Define the function of cross to among the textFields(use Enter press)  */
    public void textFields_Key_Released(KeyEvent keyEvent) {

        ValidationUtil.validate(map,btnAdd);

        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response = ValidationUtil.validate(map,btnAdd);
            if (response instanceof JFXTextField){
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
            }else if (response instanceof Boolean){

            }
        }
    }

    /** set data to text fields after select the tabel */
    private void setData(Customer c) {
        txtCid.setText(c.getCid());
        txtName.setText(c.getName());
        txtAddress.setText(c.getAddress());
        txtMtp.setText(c.getMtp());
        txtEmail.setText(c.getEmail());
        dtDate.setValue(LocalDate.parse(c.getDate()));
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
    }

    /** Function of Add Record */
    public void btnAddCustomerOnAction(ActionEvent actionEvent) {

        try {
            String dates= String.valueOf(dtDate.getValue());
            Customer c = new Customer(
                    txtCid.getText(),txtName.getText(),txtAddress.getText(),txtMtp.getText(),txtEmail.getText(),dates);

            if(CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?,?,?)",c.getCid(),c.getName(),
                    c.getAddress(),c.getMtp(),c.getEmail(),c.getDate())){

                new Alert(Alert.AlertType.CONFIRMATION,"Saved...").showAndWait();

            }else{
                new Alert(Alert.AlertType.WARNING,"Customer Already exists !!!");
            }
        }catch (ClassNotFoundException | NullPointerException|SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        clear();
        autoId();
        tableLoad();

    }

    /** load the Data to the tabel */
    public void tableLoad(){
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM customer");

            ObservableList<Customer>observableList = FXCollections.observableArrayList();

            while (result.next()){
               observableList.add(
                       new Customer(
                       result.getString(1),
                       result.getString(2),
                       result.getString(3),
                       result.getString(4),
                       result.getString(5),
                       result.getString(6)
               )
            );
        }

            tblCustomers.setItems(observableList);
            FilteredList<Customer> filterData = new FilteredList<Customer>(observableList, b -> true);

            txtSearchCusId.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Customer.getCid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtSearchCusName.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(Customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (Customer.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<Customer> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblCustomers.comparatorProperty());
            tblCustomers.setItems(sortedData);
        }catch (ClassNotFoundException|SQLException e){
    }

}

    /** Function of Update Record */
    public void btnUpdateCustomerOnAction(ActionEvent actionEvent) {

        try{
            String dates= String.valueOf(dtDate.getValue());
            Customer c = new Customer(
                    txtCid.getText(),txtName.getText(),txtAddress.getText(),txtMtp.getText(),txtEmail.getText(),dates);

            if(CrudUtil.execute("UPDATE customer SET c_name=?,c_adress=?,c_mo_num=?,c_email=?,c_date=? WHERE Cid=?"
                    ,c.getName(),c.getAddress(),c.getMtp(),c.getEmail(),c.getDate(),c.getCid())){

                new Alert(Alert.AlertType.CONFIRMATION,"Updated!").showAndWait();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again").show();
                 }
        }catch (ClassNotFoundException | SQLException |NullPointerException e){e.printStackTrace();}

        tableLoad();
        clear();

    }

    /** create the process for the genarate AUTO ID function */
    public void autoId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT cid FROM customer ORDER BY cid DESC LIMIT 1");

            if(result.next()){

                String rnno=result.getString("cid");
                int co=rnno.length();
                String txt= rnno.substring(0,2);//mul deka  (CI)
                String num=rnno.substring(2,co);//aga deaka (1000)

                int n=Integer.parseInt(num);
                n++;
                String snum=Integer.toString(n);
                String ftxt=txt+snum;
                txtCid.setText(ftxt);
            }else{
                txtCid.setText("CI1000");
            }
        }catch (ClassNotFoundException | SQLException e){
          e.printStackTrace();
        }
    }

    /** Function of Delete Record */
    public void btnDeleteCustomerOnAction(ActionEvent actionEvent) {
        try{

            if(CrudUtil.execute("DELETE FROM customer WHERE cid=?",txtCid.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted!").showAndWait();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException |NullPointerException  e){
            e.printStackTrace();
        }
        tableLoad();
        clear();
    }

    /** Function of clear textFields */
    public void clear(){
        txtName.clear();
        txtAddress.clear();
        txtMtp.clear();
        txtEmail.clear();
        dtDate.setValue(null);
        txtSearchCusId.clear();
        txtSearchCusName.clear();
        autoId();
        ValidationUtil.validate(map,btnAdd);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        tblCustomers.refresh();
    }

    /** Function of clear process */
    public void btnClearFieldsOnAction(ActionEvent actionEvent) {
        clear();
    }

    /** Load the VehicleForm UI */
    public void AddVehicleOnAction(ActionEvent actionEvent) throws IOException {
        Utilities.setUiChildren(addCustomerContext,"VehicleForm");
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {

        if(((JFXButton) mouseEvent.getSource()).getText().equals("ADD")){
            new Pulse(btnAdd).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("UPDATE")){
            new Pulse(btnUpdate).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("ADD VEHICLE")){
            new Bounce(btnAddVehicle).play();

        }else{
            new Pulse(btnDelete).play();
        }
    }
}
