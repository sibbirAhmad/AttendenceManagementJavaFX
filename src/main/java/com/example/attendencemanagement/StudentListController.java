package com.example.attendencemanagement;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class StudentListController {
    public Label nameLB;
    public Button updateBTN;
    public Label idLB;
    public Label percentLB;
    public CheckBox presentCB;


    public void update(ActionEvent actionEvent) {
    }

    public void setName(String name){
        nameLB.setText(name);
    }
    public void setData(StudentDataModel model){
        nameLB.setText(model.getName().replace("\"",""));
        idLB.setText(model.getId());
        percentLB.setText(model.getTotalPresent()+"/"+model.getTotalDays()+ " ("+model.getTotalPresent()*100/model.getTotalDays()+"%)");
        //Todo : Set Todays present
        if (model.getPresent()==1){
            presentCB.setSelected(true);
        }
    }
}

