package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.DataSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public class StudentProfile implements Initializable {

    public Label nameLB;
    public ListView listView;
    public LineChart lineChart;
    public NumberAxis yAxis;
    DataSingleton data = DataSingleton.getInstance();
    public Initializable initializable;
    StudentDataModel student ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Student Profile init");
        student = data.getStudent();
        nameLB.setText(student.getName());
        initGraph();
        initPresent();


    }
    XYChart.Series series;
    private void initGraph(){
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(2);
        yAxis.setTickUnit(1);
        series = new XYChart.Series();
        series.setName("Present Graph");
    }
    ObservableList<Pane> presentPaneList;
    private void initPresent() {
        presentPaneList = FXCollections.observableArrayList();
        HashMap<String,Integer> map = student.getPresentMap();
        System.out.println(map);
        try {

            for (Map.Entry<String,Integer> entry : map.entrySet()){
                System.out.println("Present : "+entry.getKey());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PresentCellModel.fxml"));
                Pane pane = fxmlLoader.load();
                PresentCellController controller = fxmlLoader.getController();
                controller.setData(entry.getKey(),entry.getValue());
                presentPaneList.add(pane);
                //Todo : Preparing graph
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            listView.setItems(presentPaneList);
            //Todo : Show Graph
            lineChart.getData().addAll(series);

        } catch (Exception e) {

        }
    }

    public void backToHome(ActionEvent actionEvent) {
        TestController.getInstance().home(null);
    }

}
