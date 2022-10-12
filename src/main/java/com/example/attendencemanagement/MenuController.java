package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.DataSingleton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public ListView listView;
    public Button liveAttendance;
    public Label topHomTitle;
    public ImageView exitIV;
    @FXML
    BorderPane borderPane;
    ArrayList<String> arrayList;
    static MenuController menuController;
    DataSingleton data = DataSingleton.getInstance();
    public static MenuController getInstance(){
        return menuController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Loaded : "+url);
        arrayList = new ArrayList<>();
        arrayList.add("Kamal");
        arrayList.add("Jamal");
        arrayList.add("Namal");
        menuController = this;
        exitIV.setOnMouseClicked(mouseEvent -> {
            System.out.println("Exit App");
            Platform.exit();
            System.exit(0);
        });
//        ObservableList<String> observableList = FXCollections.observableArrayList();
//        observableList.addAll(arrayList);
//       listView.setCellFactory(s->new PresentAdapter());

       home(null);
    }

    public void benAffleck(ActionEvent actionEvent) {

        Parent parent = null;
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DashFX.fxml"));
        try {
            parent = FXMLLoader.load(getClass().getResource("DashFX.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);

    }

    public void home(ActionEvent actionEvent) {
        Parent parent = null;
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DashFX.fxml"));
        try {
            parent = FXMLLoader.load(getClass().getResource("home.fxml"));
            topHomTitle.setText("Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
    }
    public void loadStudentProfile(){
        loadView("student_profile.fxml",null);
    }
    private void loadView(String viewName,String title){
        Parent parent = null;
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DashFX.fxml"));
        try {
            parent = FXMLLoader.load(getClass().getResource(viewName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
        if (title!=null) topHomTitle.setText(title);

    }
    public void liveAttendance(ActionEvent actionEvent) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("live_attendance.fxml"));
            System.out.println("Process");
        } catch (IOException e) {
            System.out.println("Can not parse " +e);
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
        topHomTitle.setText("Live Attendance");
    }

    public void setupPage(ActionEvent actionEvent) {
        loadView("setup.fxml","Setup");
    }
}
