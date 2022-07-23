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
import model.AddToService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ValidationUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddToServiceFormController {

    public JFXTextField txtSid;
    public JFXTextField txtVnumber;
    public JFXTextField txtMnumber;
    public JFXTextField txtModel;
    public JFXTextField txtNic;
    public JFXTextField txtName;
    public JFXComboBox cmbSType;
    public JFXTextField txtSearch;
    public TableView<AddToService> tblServices;
    public TableColumn colSid;
    public TableColumn colVnum;
    public TableColumn colModelNum;
    public TableColumn colModel;
    public TableColumn colOnic;
    public TableColumn colOname;
    public TableColumn colStype;
    public TableColumn colDate;
    public JFXTextArea txtBill;
    public JFXButton btnPrint;
    public JFXButton btnDelete;
    public JFXButton btnAdd;
    public JFXButton btnClear;
    public Label lblLstVnum;

    /** Define Linked-HashMap for the validation  */
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();


    public void initialize(){

        /** tabel zoom in feature */
        new ZoomIn(tblServices).play();

        colSid.setCellValueFactory(new PropertyValueFactory("sid"));
        colVnum.setCellValueFactory(new PropertyValueFactory("vnumber"));
        colModelNum.setCellValueFactory(new PropertyValueFactory("model"));
        colModel.setCellValueFactory(new PropertyValueFactory("modelname"));
        colOnic.setCellValueFactory(new PropertyValueFactory("nic"));
        colOname.setCellValueFactory(new PropertyValueFactory("oname"));
        colStype.setCellValueFactory(new PropertyValueFactory("stype"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));

        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnPrint.setDisable(true);
        txtMnumber.setDisable(true);
        txtModel.setDisable(true);
        txtNic.setDisable(true);
        txtName.setDisable(true);

        try {
            loadType();
            tableLoad();
            autoId();
            setlastVnum();
        }catch (Exception e){
            e.printStackTrace();
        }
        tblServices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
                btnPrint.setDisable(false);
            }else{
                clear();
            }
        });

        /** create validation pattern*/
        Pattern vehicleNumPattern = Pattern.compile("^([a-zA-Z]{1,3}|((?!0*-)[0-9]{1,3}))-[0-9]{4}(?<!0{4})");

        map.put(txtVnumber,vehicleNumPattern);
    }

    /** set data to text fields after select the tabel */
    private void setData(AddToService s) {
        txtSid.setText(s.getSid());
        txtVnumber.setText(s.getVnumber());
        txtMnumber.setText(s.getModel());
        txtModel.setText(s.getModelname());
        txtNic.setText(s.getNic());
        txtName.setText(s.getOname());
        cmbSType.setValue(s.getStype());
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);

    }

    /** find a last row value of vehicle no */
    public void setlastVnum(){
        try{
            ResultSet result = CrudUtil.execute("SELECT v_num FROM add_vehicle  ORDER BY v_num DESC LIMIT 1");

            if(result.next()){

                String rnno=result.getString("v_num");

                lblLstVnum.setText(rnno);
            }else{

            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            ResultSet result = CrudUtil.execute("SELECT sid, `vnumber`,`model`,`modelname`,`nic`,`oname`,`stype`,`date` FROM add_to_service");

            ObservableList<AddToService> observableList = FXCollections.observableArrayList();

            while (result.next()){
                observableList.add(
                        new AddToService(
                                result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8)
                        )
                );
            }
            tblServices.setItems(observableList);
            FilteredList<AddToService> filterData = new FilteredList<AddToService>(observableList, b -> true);

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(add_to_service-> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (add_to_service.getSid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<AddToService> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblServices.comparatorProperty());
            tblServices.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }
    }

    /** create the process for the genarate AUTO ID function */
    public void autoId(){
        try{
            ResultSet result = CrudUtil.execute("SELECT sid FROM add_to_service ORDER BY sid DESC LIMIT 1");

            if(result.next()){

                String rnno=result.getString("sid");
                int co=rnno.length();
                String txt= rnno.substring(0,2);//mul deka
                String num=rnno.substring(2,co);//aga tika
                int n=Integer.parseInt(num);
                n++;
                String snum=Integer.toString(n);
                String ftxt=txt+snum;
                txtSid.setText(ftxt);
            }else{
                txtSid.setText("SI1000");
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /** Function of comboBox loading */
    private void loadType() {

        cmbSType.getItems().add("Cleanning");
        cmbSType.getItems().add("Repairing");

    }

    /** Function of clear textFields */
    private void  clear(){
        txtSid.clear();
        txtVnumber.clear();
        txtMnumber.clear();
        txtModel.clear();
        txtNic.clear();
        txtName.clear();
        cmbSType.setValue(null);
        txtSearch.clear();
        btnPrint.setDisable(true);
        btnDelete.setDisable(true);


        txtMnumber.setDisable(true);
        txtModel.setDisable(true);
        txtNic.setDisable(true);
        txtName.setDisable(true);
    }

    /** function of print sample Recipt before to view jasper Report*/
    private void recipt() {
        try {
            txtBill.appendText(" Vehicle Service Center \n" +
                    "  No.000 xxxxxxxxxxxxxxxxxx. \n" +
                    "  0777-000000 / 0810000000  \n" +
                    "  Email: AutoModify@gmail.com  \n" +

                    "\n===============================\n\n" +
                    "Service ID: " +txtSid.getText()+ "\n" +
                    "Vehicle Number: " +txtVnumber.getText()+ "\n" +
                    "Vehicle Model: \t" +txtModel.getText()+ "\n" +
                    "Model Number: \t" +txtMnumber.getText()+ "\n" +
                    "Owner NIC: \t" +txtNic.getText()+ "\n" +
                    "Owner Name: \t" +txtName.getText()+ "\n" +
                    "Service Type: \t" +cmbSType.getSelectionModel().getSelectedItem()+ "\n" +
                    "Date: \t" +dateNow+ "\n" +


                    "\n===============================\n\n"+
                    "Thank You"+ "\n\n\n\n\n"


            );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    String dateNow=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /** Function of Delete Record */
    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try{

            if(CrudUtil.execute("DELETE FROM add_to_service WHERE sid=?",txtSid.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted!").showAndWait();
                tableLoad();

            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException |NullPointerException  e){
            e.printStackTrace();
        }
        clear();
        autoId();
    }

    /** Function of Add Record */
    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(!cmbSType.getSelectionModel().isEmpty()) {
            try {
                String type = String.valueOf(cmbSType.getSelectionModel().getSelectedItem());

                AddToService s = new AddToService(
                        txtSid.getText(), txtVnumber.getText(), txtMnumber.getText(), txtModel.getText(), txtNic.getText(), txtName.getText(), type, dateNow);

                if (CrudUtil.execute("INSERT INTO add_to_service (`sid`, `vnumber`, `model`, `modelname`, `nic`, `oname`, `stype`, `date`, `status`)VALUES(?,?,?,?,?,?,?,?,?)", s.getSid(), s.getVnumber(),
                        s.getModel(), s.getModelname(), s.getNic(), s.getOname(), s.getStype(), s.getDate(), 1)) {

                    new Alert(Alert.AlertType.CONFIRMATION, "Saved...").showAndWait();

                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer Already exists !!!");
                }
                serviceUpdate();
                recipt();
                btnPrint.setDisable(false);
                autoId();
                tableLoad();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Service Type Field is empty !!!").show();
        }

    }

    /** Function of Disable Buttons */
    private void textFieldDisable(){
        txtMnumber.setDisable(false);
        txtModel.setDisable(false);
        txtNic.setDisable(false);
        txtName.setDisable(false);
    }

    /** Auto fill some textFields After the key Released of Vehicle No textField */
    public void txtVnumberKeyRleased(KeyEvent keyEvent) throws SQLException, ClassNotFoundException,InvocationTargetException{
        ResultSet result = CrudUtil.execute
                ("SELECT `v_num`, `nic`, `name`,`v_model`, `model_num`,`fuel_type`,`date` FROM add_vehicle WHERE v_num = ?",txtVnumber.getText());

        if(result.next()){
            txtModel.setText(result.getString("v_model"));
            txtMnumber.setText(result.getString("model_num"));
            txtNic.setText(result.getString("nic"));
            txtName.setText(result.getString("name"));

            textFieldDisable();

        }else{
            txtModel.setText("");
            txtMnumber.setText("");
            txtNic.setText("");
            txtName.setText("");

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

    /** Function of Service Update for remove to record of next Service database tabel */
    private void serviceUpdate() throws SQLException, ClassNotFoundException {
        try {
            CrudUtil.execute("DELETE FROM next_service WHERE v_number = ? ",txtVnumber.getText());
        }catch (ClassNotFoundException|SQLException  e){
            e.printStackTrace();
        }
    }

    /** Function of Print Jasper Report */
    public void printOnAction(ActionEvent actionEvent) {

        String txtSidText = txtSid.getText();
        String txtVnumberText = txtVnumber.getText();
        String txtModelText = txtModel.getText();
        String txtMnumberText = txtMnumber.getText();
        String txtNicText = txtNic.getText();
        String txtNameText = txtName.getText();
        String txtStype = String.valueOf(cmbSType.getSelectionModel().getSelectedItem());
        String dateNow=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        HashMap paramMap=new HashMap();
        paramMap.put("sid",txtSidText);
        paramMap.put("vnum",txtVnumberText);
        paramMap.put("vModel",txtModelText);
        paramMap.put("vModelNum",txtMnumberText);
        paramMap.put("ownerNic",txtNicText);
        paramMap.put("ownerName",txtNameText);
        paramMap.put("sType",txtStype);
        paramMap.put("date",dateNow);

        try {
            /** NO COMPILATION -LOAD AND VIEW */

            //catch the report
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/AddToService.jasper"));

            //Fill the Information which report needed
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));

            //then the report is ready let's view
            JasperViewer.viewReport(jasperPrint,false);

            try{
                txtBill.setText("");

            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    /** Function of clear process */
    public void btnClearOnAction(ActionEvent actionEvent) {
        clear();
        autoId();
    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("ADD")){
            new Pulse(btnAdd).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("DELETE")) {
            new Pulse(btnDelete).play();

        }else if(((JFXButton) mouseEvent.getSource()).getText().equals("CLEAR")){
                new Pulse(btnClear).play();
        }else{
            new Bounce(btnPrint).play();
        }

    }
}
