package com.example.attendencemanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PresentAdapter extends ListCell<String> {

    @FXML
    private Label nameLB;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private FXMLLoader fxmlLoader;
    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
       if (b || s==null){
           System.out.println("This is null");
           setText(null);
           setGraphic(null);
       }else {
           if (fxmlLoader==null){
               fxmlLoader =  new FXMLLoader(getClass().getResource("list_item.fxml"));
               fxmlLoader.setController(this);
               try {
                   fxmlLoader.load();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               System.out.println("Setting");
               setText(null);
               setGraphic(anchorPane);
           }

       }
    }
}
