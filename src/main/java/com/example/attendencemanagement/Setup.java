package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.PostRequest;
import com.example.attendencemanagement.utils.SampleData;
import com.example.attendencemanagement.utils.Utils;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;

public class Setup implements Initializable {

    public ComboBox yearCM;
    public ComboBox departmentCM;
    public ComboBox sessionCM;
    public TextField batchLB;
    public TextField subjectCodeLB;
    public ProgressIndicator progressbar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressbar.setVisible(false);
        initCompo();
    }

    public void onCreate(ActionEvent actionEvent) {
        try {
            String department = departmentCM.getValue().toString().toUpperCase().replace(" ","");
            String session = sessionCM.getValue().toString().toUpperCase().replace(" ","");
            String year = yearCM.getValue().toString().toUpperCase().replace("-","");
            String batch = batchLB.getText().toUpperCase().replace(" ","");
            String subjectCode = subjectCodeLB.getText().toUpperCase().replace(" ","");
            String formattedSubjectName = department+"_"+session+year+"_"+batch+"_"+subjectCode;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("requestFrom","teacher");
            jsonObject.addProperty("request","createSubject");
            jsonObject.addProperty("date", Utils.getFormatted());//Todo : Not required
            jsonObject.addProperty("subjectName",formattedSubjectName);
            String jsonData = jsonObject.toString();
            progressbar.setVisible(true);
            new PostRequest("", jsonData, new PostRequest.PostRequestListener() {
                @Override
                public void onSuccess(String response) {
                    Platform.runLater(()->{
                        System.out.println("Success : "+response);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Successful",ButtonType.OK);
                        alert.showAndWait();
                        progressbar.setVisible(false);
                    });

                }

                @Override
                public void onFailed(String cause) {
                    System.out.println("Failed "+cause);
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to creat.\nDo want to retry?", ButtonType.OK,ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK){
                            onCreate(null);
                        }
                        progressbar.setVisible(false);
                    });

                }
            });
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid data", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                
            }
        }
    }
    private void initCompo(){
        departmentCM.setItems(FXCollections.observableArrayList(SampleData.getDepartments()));
        sessionCM.setItems(FXCollections.observableArrayList(SampleData.getSessions()));
        yearCM.setItems(FXCollections.observableArrayList(SampleData.getYears()));
    }

    public void backToHome(ActionEvent actionEvent) {
        TestController.getInstance().home(null);
    }
}
