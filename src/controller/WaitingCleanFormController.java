package controller;

import animatefx.animation.Bounce;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.MouseEvent;
import model.AddToService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ReportsUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WaitingCleanFormController {

    public TableView tblWaitingCleaning;
    public TableColumn colServiceId;
    public TableColumn colVehicleNo;
    public TableColumn colModelNum;
    public TableColumn colModel;
    public TableColumn colOnic;
    public TableColumn colOname;
    public TableColumn colSType;
    public TableColumn colDate;
    public JFXTextField txtSearchVnum;
    public JFXButton btnPrint;


    public void initialize(){

        /** tabel zoom in feature */
        new ZoomIn(tblWaitingCleaning).play();

        colServiceId.setCellValueFactory(new PropertyValueFactory("sid"));
        colVehicleNo.setCellValueFactory(new PropertyValueFactory("vnumber"));
        colModelNum.setCellValueFactory(new PropertyValueFactory("model"));
        colModel.setCellValueFactory(new PropertyValueFactory("modelname"));
        colOnic.setCellValueFactory(new PropertyValueFactory("nic"));
        colOname.setCellValueFactory(new PropertyValueFactory("oname"));
        colSType.setCellValueFactory(new PropertyValueFactory("stype"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));


        try {
        tableLoad();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** load the Data to the tabel */
    private void tableLoad() {
        try {
            ResultSet result = CrudUtil.execute
         ("SELECT `sid`,`vnumber`,`model`,`modelname`,`nic`,`oname`,`stype`,`date` FROM add_to_service WHERE status=1 AND stype='Cleanning'");

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
            tblWaitingCleaning.setItems(observableList);
            FilteredList<AddToService> filterData = new FilteredList<AddToService>(observableList, b -> true);

            txtSearchVnum.textProperty().addListener((observable, oldValue, newValue) -> {
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
            SortedList<AddToService> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblWaitingCleaning.comparatorProperty());
            tblWaitingCleaning.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }

    }

    /** Function of Print Jasper Report */
    public void tableDataPrintOnAction(ActionEvent actionEvent) {
        /** Generate Jasper report and pass paramititers to ReportsUtil class*/
        ReportsUtil.loadReport(tblWaitingCleaning,btnPrint,"waitingClean");

    }

    /** Define Animation of Buttons */
    public void btnMouseMovedOnAction(MouseEvent mouseEvent) {
        if(((JFXButton) mouseEvent.getSource()).getText().equals("PRINT")){
            new Bounce(btnPrint).play();

        }
    }
}
