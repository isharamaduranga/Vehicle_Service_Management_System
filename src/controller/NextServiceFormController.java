package controller;

import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CrudUtil;
import util.ReportsUtil;
import view.tm.NextServiceTM;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class NextServiceFormController {

    public AnchorPane nextServiceContext;
    public TableView<NextServiceTM> tblNextService;
    public TableColumn colCusid;
    public TableColumn colVnum;
    public TableColumn colCusName;
    public TableColumn colAddress;
    public TableColumn colTPNumber;
    public TableColumn colEmail;
    public TableColumn colDate;
    public JFXTextField txtVnumberSearch;
    public JFXTextField txtOnicSearch;
    public JFXDatePicker txtNextServiceDate;
    public JFXButton btnPrint;


    public void initialize(){

        colCusid.setCellValueFactory(new PropertyValueFactory("cid"));
        colVnum.setCellValueFactory(new PropertyValueFactory("v_number"));
        colCusName.setCellValueFactory(new PropertyValueFactory("c_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("c_adress"));
        colTPNumber.setCellValueFactory(new PropertyValueFactory("c_mo_num"));
        colEmail.setCellValueFactory(new PropertyValueFactory("c_email"));
        colDate.setCellValueFactory(new PropertyValueFactory("ns_date"));

        try{
            tableLoad();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    String time;
    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            time=txtNextServiceDate.getValue().toString();
            ResultSet result = CrudUtil.execute("SELECT DISTINCT customer.`cid`,`v_number`,`c_name`,`c_adress`,`c_mo_num`,`c_email`,`c_date` FROM customer INNER JOIN `next_service` WHERE customer.cid = next_service.cid AND ns_date=?",time);

            ObservableList<NextServiceTM> observableList = FXCollections.observableArrayList();

            while (result.next()){
                observableList.add(
                        new NextServiceTM(
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
            tblNextService.setItems(observableList);
            FilteredList<NextServiceTM> filterData = new FilteredList<NextServiceTM>(observableList, b -> true);

            txtVnumberSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(NextServiceTM -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (NextServiceTM.getV_number().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtOnicSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(NextServiceTM -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (NextServiceTM.getCid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<NextServiceTM> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblNextService.comparatorProperty());
            tblNextService.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException|NullPointerException e){
        }

    }

    /** Function of Search next Service date */
    public void nextServiceDateSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT DISTINCT customer.`cid`,`v_number`,`c_name`,`c_adress`,`c_mo_num`,`c_email`,`c_date` FROM customer INNER JOIN `next_service` WHERE customer.cid = next_service.cid AND ns_date=? ",txtNextServiceDate.getValue());

            ObservableList<NextServiceTM> observableList = FXCollections.observableArrayList();

            while (result.next()){
                observableList.add(
                        new NextServiceTM(
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
            tblNextService.setItems(observableList);
    }catch (ClassNotFoundException | SQLException e){
        e.printStackTrace();
        }
    }

    /** Function of clear process */
    public void clearFieldOnAction(ActionEvent actionEvent) {
        txtVnumberSearch.clear();
        txtOnicSearch.clear();
        txtNextServiceDate.setValue(null);
        tableLoad();
    }

    /** Function of Print Jasper Report */
    public void printTableOnAction(ActionEvent actionEvent) {

        /** Generate Jasper report and pass paramititers to ReportsUtil class*/
        ReportsUtil.loadReport(tblNextService,btnPrint,"nextService");
        btnPrint.setDisable(false);

    }

    /** Close currnt ui and Load the DashBoard UI */
    public void btnNextServiceLeft(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) nextServiceContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));
    }

}
