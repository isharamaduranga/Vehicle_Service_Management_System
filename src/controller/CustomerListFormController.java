package controller;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
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
import model.Customer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ReportsUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerListFormController {

    public AnchorPane customerListContext;
    public TableView tblCustomerList;
    public TableColumn colCid;
    public TableColumn colCname;
    public TableColumn colCaddress;
    public TableColumn colTPnum;
    public TableColumn colEmail;
    public TableColumn colRegDate;
    public JFXTextField txtCusName;
    public JFXTextField txtCusId;
    public JFXButton btnPrint;

    public void initialize() throws SQLException, ClassNotFoundException {

        colCid.setCellValueFactory(new PropertyValueFactory("cid"));
        colCname.setCellValueFactory(new PropertyValueFactory("name"));
        colCaddress.setCellValueFactory(new PropertyValueFactory("address"));
        colTPnum.setCellValueFactory(new PropertyValueFactory("mtp"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colRegDate.setCellValueFactory(new PropertyValueFactory("date"));
        try{
            tableLoad();
        }catch(Exception e){
        }
    }

    /** load the Data to the tabel */
    private void tableLoad() {

        try {
            ResultSet result = CrudUtil.execute
                    ("SELECT `cid`,`c_name`,`c_adress`,`c_mo_num`,c_email,c_date FROM customer");

            ObservableList<Customer> observableList = FXCollections.observableArrayList();

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
            tblCustomerList.setItems(observableList);
            FilteredList<Customer> filterData = new FilteredList<Customer>(observableList, b -> true);

            txtCusId.textProperty().addListener((observable, oldValue, newValue) -> {
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
            txtCusName.textProperty().addListener((observable, oldValue, newValue) -> {
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
            sortedData.comparatorProperty().bind(tblCustomerList.comparatorProperty());
            tblCustomerList.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }
    }

    /** Close currnt ui and Load the DashBoard UI */
    public void btnCloseOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) customerListContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));
    }

    /** Function of Print Jasper Report */
    public void btnPrintOnAction(ActionEvent actionEvent) {
        /** Generate Jasper report and pass paramititers to ReportsUtil class*/
        ReportsUtil.loadReport(tblCustomerList,btnPrint,"customerList");

    }

    /** Function of clear process */
    public void btnClearOnAction(ActionEvent actionEvent) {
        txtCusId.clear();
        txtCusName.clear();
    }
}
