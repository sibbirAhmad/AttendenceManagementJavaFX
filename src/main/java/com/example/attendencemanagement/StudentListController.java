package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.DataSingleton;
import com.example.attendencemanagement.utils.PostRequest;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class StudentListController {
    public Label nameLB;
    public Button updateBTN;
    public Label idLB;
    public Label percentLB;
    public CheckBox presentCB;
    DataSingleton data = DataSingleton.getInstance();

    public void update(ActionEvent actionEvent) {
        String id = idLB.getText();
        int present = presentCB.isSelected()==true?1:0;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestFrom","teacher");
        jsonObject.addProperty("request","updateSinglePresent");
        jsonObject.addProperty("date",data.getDate());
        jsonObject.addProperty("subjectName",data.getFormattedSubjectName());
        jsonObject.addProperty("studentId",id);
        jsonObject.addProperty("present",present);
        String updateSinglePresentJson = jsonObject.toString();
        data.getProgressIndicator().setVisible(true);
        new PostRequest("", updateSinglePresentJson, new PostRequest.PostRequestListener() {
            @Override
            public void onSuccess(String response) {
                data.getProgressIndicator().setVisible(false);
            }

            @Override
            public void onFailed(String cause) {
                data.getProgressIndicator().setVisible(false);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update.\nDo you want to retry?", ButtonType.OK,ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.OK) {
                    //Todo : Required a toast to re confirm
                    update(null);
                }
            }
        });

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

