package com.example.attendencemanagement;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.util.HashMap;

public class PresentCellController {
    public Label dateLB;
    public CheckBox presentCB;

    public void update(ActionEvent actionEvent) {
        System.out.println(dateLB.getText());
    }

    public void setData(String date,int present){
        dateLB.setText(date);
        presentCB.setSelected(present == 1? true:false );
    }

}
