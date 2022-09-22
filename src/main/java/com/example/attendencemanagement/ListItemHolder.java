package com.example.attendencemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListItemHolder extends AnchorPane implements Initializable{
    @FXML
    public Label nameLB;
    public AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader =new  FXMLLoader(getClass().getResource("list_item.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setName(String name){
        nameLB.setText(name);
    }
    public AnchorPane getAnchorPane(){
        return anchorPane;
    }
}
