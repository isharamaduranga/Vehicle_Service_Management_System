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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.ServiceComplete;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ServiceResultFormController {

    public JFXTextField txtSid;
    public JFXTextField txtVnum;
    public JFXTextField txtModel;
    public JFXTextField txtCid;
    public JFXTextField txtCname;
    public JFXTextField txtService;
    public JFXTextArea txtItemReplace;
    public JFXComboBox cmbResult;
    public JFXTextArea txtOther;
    public JFXDatePicker dtDate;
    public JFXTextField txtCharge;
    public JFXTextField txtSidSearch;

    public TableView<ServiceComplete> tblServiceResult;
    public TableColumn colServiceId;
    public TableColumn colVehicleNo;
    public TableColumn colModel;
    public TableColumn colCusId;
    public TableColumn colCusName;
    public TableColumn colServices;
    public TableColumn colItemReplace;
    public TableColumn colResult;
    public TableColumn colOther;
    public TableColumn colDate;
    public TableColumn colCharge;
    public JFXButton btnAdd;
    public JFXButton btnDelete;
    public JFXButton btnClear;
    public Label lblLstSid;

    /** Define Linked-HashMap for the validation  */
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();
    LinkedHashMap<JFXTextArea, Pattern> map2 = new LinkedHashMap<>();

    public void  initialize(){
        /** tabel zoom in feature */
        new ZoomIn(tblServiceResult).play();

        colServiceId.setCellValueFactory(new PropertyValueFactory("sid"));
        colVehicleNo.setCellValueFactory(new PropertyValueFactory("v_number"));
        colModel.setCellValueFactory(new PropertyValueFactory("model"));
        colCusId.setCellValueFactory(new PropertyValueFactory("cid"));
        colCusName.setCellValueFactory(new PropertyValueFactory("cname"));
        colServices.setCellValueFactory(new PropertyValueFactory("service"));
        colItemReplace.setCellValueFactory(new PropertyValueFactory("item_replace"));
        colResult.setCellValueFactory(new PropertyValueFactory("result"));
        colOther.setCellValueFactory(new PropertyValueFactory("other"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colCharge.setCellValueFactory(new PropertyValueFactory("charge"));

        btnAdd.setDisable(true);
        btnDelete.setDisable(true);

        txtVnum.setDisable(true);
        txtModel.setDisable(true);
        txtCid.setDisable(true);
        txtCname.setDisable(true);

        try{
            loadType();
            tableLoad();
            setlastSid();
        }catch(Exception  e){
            e.printStackTrace();
        }
        tblServiceResult.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);

            }else{
                clearFields();
            }
        });

        /** create validation pattern*/
        Pattern sIdPattern = Pattern.compile("^(SI)[0-9]{4,5}$");
        Pattern servicePattern = Pattern.compile("^[A-z ,/]{4,20}$");
        Pattern chargePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        map.put(txtSid,sIdPattern);
        map.put(txtService,servicePattern);
        map.put(txtCharge,chargePattern);

        Pattern itemReplacePattern = Pattern.compile("^[A-z0-9 ,-/]{4,50}$");
        Pattern otherPattern = Pattern.compile("^[A-z0-9 ,-/]{4,50}$");

        map2.put(txtItemReplace,itemReplacePattern);
        map2.put(txtOther,otherPattern);


    }

    /** set data to text fields after select the tabel */
    private void setData(ServiceComplete s) {
        txtSid.setText(s.getSid());
        txtVnum.setText(s.getV_number());
        txtModel.setText(s.getModel());
        txtCid.setText(s.getCid());
        txtCname.setText(s.getCname());
        txtService.setText(s.getService());
        txtItemReplace.setText(s.getItem_replace());
        cmbResult.setValue(s.getResult());
        txtOther.setText(s.getResult());
        dtDate.setValue(LocalDate.parse(s.getDate()));
        txtCharge.setText(s.getCharge());

        btnDelete.setDisable(false);
    }

    /** Function of clear process */
    private void clearFields() {
        txtSid.clear();
        txtVnum.clear();
        txtModel.clear();
        txtCid.clear();
        txtCname.clear();
        txtService.clear();
        txtItemReplace.clear();
        cmbResult.setValue(null);
        txtOther.clear();
        dtDate.setValue(null);
        txtCharge.clear();

        btnAdd.setDisable(true);

        txtVnum.setDisable(true);
        txtModel.setDisable(true);
        txtCid.setDisable(true);
        txtCname.setDisable(true);
    }

    /** find a last row value of Service id */
    public void setlastSid(){
        try{
            ResultSet result = CrudUtil.execute("SELECT sid FROM add_to_service  ORDER BY sid DESC LIMIT 1");

            if(result.next()){

                String rnno=result.getString("sid");

                lblLstSid.setText(rnno);
            }else{

            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /** Function of Disable Buttons */
    private void textFieldDisable(){
        txtVnum.setDisable(false);
        txtModel.setDisable(false);
        txtCid.setDisable(false);
        txtCname.setDisable(false);
    }

    /** Function of check satus and update data */
    private void update(){
        try{
            CrudUtil.execute("UPDATE `add_to_service` SET `status`= ? WHERE sid=?",0,txtSid.getText());

        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
    }

    /** Function of comboBox loading */
    private void loadType() {

        cmbResult.getItems().add("Complete");
        cmbResult.getItems().add("In-Complete");

    }

    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            ResultSet result = CrudUtil.execute
                    ("SELECT `sid`,`v_number`,`model`,`cid`,`cname`,`service`,`item_replace`,`result`,`other`,`date`,`charge` FROM service_complete");

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
            tblServiceResult.setItems(observableList);
            FilteredList<ServiceComplete> filterData = new FilteredList<ServiceComplete>(observableList, b -> true);

            txtSidSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(ServiceComplete -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (ServiceComplete.getSid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<ServiceComplete> sortedData = new SortedList<ServiceComplete>(filterData);
            sortedData.comparatorProperty().bind(tblServiceResult.comparatorProperty());
            tblServiceResult.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }
    }

    /** Function of Add Record */
    public void btnAddOnAction(ActionEvent actionEvent) {
try {
    if (!cmbResult.getSelectionModel().isEmpty() && !dtDate.getValue().equals("")) {
        try {
            ServiceComplete s = new ServiceComplete(
                    txtSid.getText(), txtVnum.getText(), txtModel.getText(), txtCid.getText(), txtCname.getText(),
                    txtService.getText(), txtItemReplace.getText(), cmbResult.getSelectionModel().getSelectedItem().toString(),
                    txtOther.getText(), dtDate.getValue().toString(), txtCharge.getText());

            if (CrudUtil.execute("INSERT INTO service_complete VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", s.getSid(), s.getV_number(), s.getModel(), s.getCid(),
                    s.getCname(), s.getService(), s.getItem_replace(), s.getResult(), s.getOther(), s.getDate(), s.getCharge(), 0)) {

                new Alert(Alert.AlertType.CONFIRMATION, "Saved...").showAndWait();

            } else {
                new Alert(Alert.AlertType.WARNING, "Customer Already exists !!!");
            }
        } catch (ClassNotFoundException | SQLException e) {

            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        update();
        clearFields();
        tableLoad();
    } else {
        new Alert(Alert.AlertType.WARNING, "Result Or Date Field Empty Please Check it !!!").show();
    }
        }catch (Exception e){
    new Alert(Alert.AlertType.WARNING, "Somthing went wrong !!!").show();
}
    }

    /** Function of Delete Record */
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try{

            if(CrudUtil.execute("DELETE FROM service_complete WHERE sid=?",txtSid.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted!").showAndWait();
                tableLoad();
                clearFields();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException |NullPointerException  e){
            e.printStackTrace();
        }

    }

    /** Auto fill some textFields After the key Released of sid textField */
    public void textFields_Key_Released(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute
                ("SELECT `sid`,`vnumber`,`model`,`modelname`,`nic`,`oname`,`stype`,`date`,`status` FROM add_to_service WHERE sid = ?",txtSid.getText());

        if(result.next()){
            txtSid.setText(result.getString("sid"));
            txtVnum.setText(result.getString("vnumber"));
            txtModel.setText(result.getString("modelname"));
            txtCid.setText(result.getString("nic"));
            txtCname.setText(result.getString("oname"));

            textFieldDisable();

        }else {

            txtVnum.setText("");
            txtModel.setText("");
            txtCid.setText("");
            txtCname.setText("");
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
        validate();
        if (keyEvent.getCode()== KeyCode.ENTER){
            Object response =  validate();
            if (response instanceof JFXTextArea){
                JFXTextArea textArea = (JFXTextArea) response;
                textArea.requestFocus();
            }else if (response instanceof Boolean){

            }
        }


    }

    /**  validate pattern above textFields*/
    private Object validate() {

        for (JFXTextArea key : map2.keySet()) {
            Pattern pattern = map2.get(key);
            if (!pattern.matcher(key.getText()).matches()) {
                addError(key);
                return key;
            }
            removeError(key);
        }
        return true;
    }

    /** Function of Remove error and texfield colur change */
    private void removeError(JFXTextArea textArea) {
        textArea.setStyle("-jfx-unfocus-color:green");
        textArea.setStyle("-jfx-focus-color:green");

        btnAdd.setDisable(false);
    }

    /** Function of Add error and texfield colur change */
    private void addError(JFXTextArea textArea) {
        if(textArea.getText().length()>0){
            textArea.setStyle("-jfx-unfocus-color:red");
            textArea.setStyle("-jfx-focus-color:red");
        }
        btnAdd.setDisable(true);
    }

    /** Function of clear process */
    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("ADD")){
            new Pulse(btnAdd).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("DELETE")) {
            new Pulse(btnDelete).play();

        }else{
            new Pulse(btnClear).play();
        }
    }
}
