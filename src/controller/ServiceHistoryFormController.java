package controller;

import animatefx.animation.ZoomIn;
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
import model.AddVehicle;
import model.ServiceComplete;
import util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceHistoryFormController {

    public AnchorPane serviceHistoryContext;
    public JFXTextField vnumSearch;
    public JFXTextField clientIdSearch;
    public TableView tblServiceHistory;
    public TableColumn colSid;
    public TableColumn colVnum;
    public TableColumn colModel;
    public TableColumn colNic;
    public TableColumn colClientName;
    public TableColumn colServiceType;
    public TableColumn colItemReplace;
    public TableColumn colResult;
    public TableColumn colOther;
    public TableColumn colDate;
    public TableColumn colCharge;


    public void initialize(){


        colSid.setCellValueFactory(new PropertyValueFactory("sid"));
        colVnum.setCellValueFactory(new PropertyValueFactory("v_number"));
        colModel.setCellValueFactory(new PropertyValueFactory("model"));
        colNic.setCellValueFactory(new PropertyValueFactory("cid"));
        colClientName.setCellValueFactory(new PropertyValueFactory("cname"));
        colServiceType.setCellValueFactory(new PropertyValueFactory("service"));
        colItemReplace.setCellValueFactory(new PropertyValueFactory("item_replace"));
        colResult.setCellValueFactory(new PropertyValueFactory("result"));
        colOther.setCellValueFactory(new PropertyValueFactory("other"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colCharge.setCellValueFactory(new PropertyValueFactory("charge"));

        try{
            tabelLoad();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** load the Data to the tabel */
    private void tabelLoad() {
        try {
            ResultSet result = CrudUtil.execute("SELECT `sid`,`v_number`,`model`,`cid`,`cname`,`service`,`item_replace`,`result`,`other`,`date`,`charge` FROM `service_complete` WHERE status=1");

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

            tblServiceHistory.setItems(observableList);
            FilteredList<ServiceComplete> filterData = new FilteredList<ServiceComplete>(observableList, b -> true);

            vnumSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(ServiceComplete -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (ServiceComplete.getV_number().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            clientIdSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate(ServiceComplete -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (ServiceComplete.getCid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    }
                    else
                        return false;
                });
            });
            SortedList<ServiceComplete> sortedData = new SortedList<>(filterData);
            sortedData.comparatorProperty().bind(tblServiceHistory.comparatorProperty());
            tblServiceHistory.setItems(sortedData);
        }catch (ClassNotFoundException| SQLException e){
        }

    }

    /** Close currnt ui and Load the DashBoard UI */
    public void btnServiceHistoryLeft(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) serviceHistoryContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashBoardForm.fxml"))));
    }

    /** Function of clear process */
    public void clearSearchFields(ActionEvent actionEvent) {
        vnumSearch.clear();
        clientIdSearch.clear();
        tabelLoad();
    }
}
