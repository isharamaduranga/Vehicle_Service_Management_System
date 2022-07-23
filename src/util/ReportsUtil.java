package util;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public  class ReportsUtil {


    /** Create Static Method in Util package for the view and Print jasper Reports And Access to Any place on project*/
    public static void loadReport(TableView view, JFXButton btn,String location){

            if(!view.getItems().isEmpty()){

                try {
                    /** NO COMPILATION -LOAD AND VIEW */

                    btn.setDisable(false);

                    /** catch the report */
                    JasperReport compileReport = (JasperReport) JRLoader.loadObject(ReportsUtil.class.getResource("/view/reports/"+location+".jasper"));

                    ObservableList items = view.getItems();

                    /** Fill the Information which report needed */
                    JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, new JRBeanArrayDataSource(view.getItems().toArray()));

                    /** then the report is ready let's view */
                    JasperViewer.viewReport(jasperPrint,false);

                } catch (JRException e) {
                    e.printStackTrace();
                }

            }else{

                btn.setDisable(true);
                view.refresh();
            }

    }
}
