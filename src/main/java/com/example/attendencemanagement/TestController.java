package com.example.attendencemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML
    public ListView listView;
    @FXML
    BorderPane borderPane;
    ArrayList<String> arrayList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Loaded : "+url);
        arrayList = new ArrayList<>();
        arrayList.add("Kamal");
        arrayList.add("Jamal");
        arrayList.add("Namal");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setCenter(parent);
    }
}
