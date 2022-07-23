package controller;

import animatefx.animation.ZoomIn;
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
import model.AddToService;
import util.CrudUtil;
import util.ReportsUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OngoingServiceFormController {
    public AnchorPane ongoingServiceHistoryContext;
    public TableView<AddToService> tblOnGoingService;
    public TableColumn colServiceId;
    public TableColumn colVehicleNo;
    public TableColumn colModelNum;
    public TableColumn colModel;
    public TableColumn colOnic;
    public TableColumn colOname;
    public TableColumn colSType;
    public TableColumn colDate;
    public JFXTextField txtVnumber;
    public JFXTextField txtOnic;
    public JFXButton btnPrint;

    public void initialize(){

        colServiceId.setCellValueFactory(new PropertyValueFactory("sid"));
        colVehicleNo.setCellValueFactory(new PropertyValueFactory("vnumber"));
        colModelNum.setCellValueFactory(new PropertyValueFactory("model"));
        colModel.setCellValueFactory(new PropertyValueFactory("modelname"));
        colOnic.setCellValueFactory(new PropertyValueFactory("nic"));
        colOname.setCellValueFactory(new PropertyValueFactory("oname"));
        colSType.setCellValueFactory(new PropertyValueFactory("stype"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));

        try{
            tableLoad();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            ResultSet result = CrudUtil.execute
                    ("SELECT `sid`,`vnumber`,`model`,`modelname`,`nic`,`oname`,`stype`,`date` FROM add_to_service WHERE status=? OR ?",0,1);

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
            tblOnGoingService.setItems(observableList);
            FilteredList<AddToService> filterData = new FilteredList<AddToService>(observableList, b -> true);

            txtVnumber.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(AddToService-> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (AddToService.getVnumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            txtOnic.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(AddToService-> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (AddToService.getNic().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<AddToService> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblOnGoingService.comparatorProperty());
            tblOnGoingService.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }

    }

    /** Close currnt ui and Load the DashBoard UI */
    public void btnOnTheServiceLeft(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) ongoingServiceHistoryContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));
    }

    /** Function of clear process */
    public void clearFieldOnAction(ActionEvent actionEvent) {
            clear();
    }

    /** Function of clear textFields */
    private void clear(){
        txtVnumber.clear();
        txtOnic.clear();
    }

    /** Function of Print Jasper Report */
    public void printOnAction(ActionEvent actionEvent) {
        /** Generate Jasper report and pass paramititers to ReportsUtil class*/
        ReportsUtil.loadReport(tblOnGoingService,btnPrint,"ongoingService");
    }
}
