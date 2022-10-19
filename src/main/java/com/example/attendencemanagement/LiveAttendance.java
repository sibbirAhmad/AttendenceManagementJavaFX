package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.PostRequest;
import com.example.attendencemanagement.utils.SampleData;
import com.example.attendencemanagement.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LiveAttendance implements Initializable {
    public ComboBox yearCM;
    public ComboBox departmentCM;
    public ComboBox sessionCM;
    public TextField batchLB;
    public TextField subjectCodeLB;
    public ProgressIndicator progressbar;
    public DatePicker datePicker;
    public Label remainingLB;
    public TextField durationTF;
    public ListView listView;
    public Label totalAbsentLB;
    public Label totalPresentLB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressbar.setVisible(false);
        initCompo();
        durationInMillis = System.currentTimeMillis()+(5*(60*1000));
        //myTimerMethod();
    }

    private void initCompo(){
        departmentCM.setItems(FXCollections.observableArrayList(SampleData.getDepartments()));
        sessionCM.setItems(FXCollections.observableArrayList(SampleData.getSessions()));
        yearCM.setItems(FXCollections.observableArrayList(SampleData.getYears()));
    }

    public void backToHome(ActionEvent actionEvent) {
        MenuController.getInstance().home(null);
    }
    long durationInMillis = 0;
    String formattedSubjectName;
    String selectedDate;
    int duration;
    public void startTimer(ActionEvent actionEvent) {
        String department = departmentCM.getValue().toString().toUpperCase().replace(" ","");
        String session = sessionCM.getValue().toString().toUpperCase().replace(" ","");
        String year = yearCM.getValue().toString().toUpperCase().replace("-","");
        String batch = batchLB.getText().toUpperCase().replace(" ","");
        String subjectCode = subjectCodeLB.getText().toUpperCase().replace(" ","");
        duration = Integer.parseInt(durationTF.getText());
        String date = datePicker.getValue().toString().replace("-","");
        selectedDate = "p"+date;
        formattedSubjectName = department+"_"+session+year+"_"+batch+"_"+subjectCode;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestFrom","teacher");
        jsonObject.addProperty("request","activePresentMode");
        jsonObject.addProperty("date",selectedDate);
        jsonObject.addProperty("duration",duration);
        jsonObject.addProperty("subjectName",formattedSubjectName);
        String json = jsonObject.toString();
        progressbar.setVisible(true);
        new PostRequest("", json, new PostRequest.PostRequestListener() {
            @Override
            public void onSuccess(String response) {
                Platform.runLater(()->{
                    progressbar.setVisible(false);
                    durationInMillis = System.currentTimeMillis()+(duration*(1000*60));
                    myTimerMethod();//Todo : Start Timer
                    String Link  = "https://sibbirahmad.github.io/attendance.html";
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"Successfully timer started\nAttendance URL : "+Link,ButtonType.OK);
                    alert.showAndWait();
                });
            }

            @Override
            public void onFailed(String cause) {
                Platform.runLater(()->{
                    progressbar.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Failed to start timer\nDo you want to retry?",ButtonType.OK,ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() ==ButtonType.OK){
                        startTimer(null);
                    }
                });
            }
        });
        System.out.println(jsonObject);
    }
    ObservableList<Pane> studentPaneList;
    boolean isPresentListActivated;
    private void initStudents(ArrayList<StudentDataModel> dataList){
        studentPaneList = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < dataList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentCellModel.fxml"));
                Pane pane = fxmlLoader.load();
                StudentListController controller = fxmlLoader.getController();
                controller.setData(dataList.get(i));
                if (dataList.get(i).getPresent()==1){
                    studentPaneList.add(pane);
                }else {
                    System.out.println("Absent ");
                }

            }
            //listView.getItems().addAll(list);
            listView.setItems(studentPaneList);
            progressbar.setVisible(false);
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }
    int c = 0;
    private void myTimerMethod(){
        final int[] secondsPassed = {0};
        Timer myTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed[0]++;
                long currentTime = System.currentTimeMillis();
                if (currentTime<durationInMillis){
                    int second = (int)(durationInMillis-currentTime)/1000*60;
                    String s1 = Utils.ConvertSecondToHHMMSSString(second).substring(0,5);
                    String str = "Remaining "+ s1;
                    System.out.println(secondsPassed[0]);
                    Platform.runLater(() -> remainingLB.setText(str));
                    if (c>5){
                        getUpdateFromServer();
                        c=0;
                    }
                    c++;
                }else {
                    Platform.runLater(() -> remainingLB.setText("Time UP"));
                    myTimer.cancel();
                    cancel();
                    System.out.println("Time Up");
                }
            }
        };

        myTimer.scheduleAtFixedRate(task,1000,1000);
    }
    private ArrayList<StudentDataModel> processData(String jsonData){
        String r = jsonData;
        ArrayList<StudentDataModel> list = new ArrayList<>();
        JsonObject o = (JsonObject) new JsonParser().parse(r);
        int responseCode = o.get("responseCode").getAsInt();
        if (responseCode==200){
            System.out.println("Object "+o.get("data"));
            r = o.get("data").getAsString();
            JsonArray jsonObject = new JsonParser().parse(r).getAsJsonArray ();
            String today = selectedDate;
            //Utils.saveAsText("jsonData.txt",jsonData);
            int c = 0;
            int p = 0;
            for (JsonElement object: jsonObject){
                JsonObject so = object.getAsJsonObject();
                Iterator<String> array = so.keySet().stream().iterator();
                System.out.println("Name : "+so.get("name"));
                //Todo : Adding value to array list
                StudentDataModel model = new StudentDataModel();
                model.setName(so.get("name").toString());
                model.setId(so.get("id").toString());
                HashMap<String,Integer> presentMap = new HashMap<>();
                while (array.hasNext()){
                    String key = array.next();
                    if (key.startsWith("p")){
                        int present = Integer.parseInt(so.get(key).toString());
                        //Todo : Check todays present
                        if (key.contains(today)){
                            model.setPresent(present);
                            if (present==1){
                                p++;
                            }
                        }
                        presentMap.put(key,present);
                        // System .out.println(key+" --> "+present);
                        c++;
                    }
                }

                model.setPresentMap(presentMap);
                model.setTotalPresent(p)  ;
                model.setTotalDays(c);
                list.add(model);
            }
            totalPresentLB.setText("Present : "+p);
            totalAbsentLB.setText("Absent : "+(list.size()-p));
        }else if (responseCode==201) {
            //Show A alert Dialog
            Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to create", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                //Todo : Create new present

                System.out.println("No data exist");

            }
        }
        return list;
    }
    private void getUpdateFromServer(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestFrom","teacher");
        jsonObject.addProperty("request","getAllPresent");
        jsonObject.addProperty("date",selectedDate);
        jsonObject.addProperty("subjectName",formattedSubjectName);
        String jsonData = jsonObject.toString();
        System.out.println(jsonObject);
        new PostRequest("", jsonData, new PostRequest.PostRequestListener() {
            @Override
            public void onSuccess(String response) {
                Platform.runLater(()->{
                    ArrayList<StudentDataModel> studentDataArrayList = processData(response);
                    initStudents(studentDataArrayList);
                  //  progressbar.setVisible(false);
                });

            }
            @Override
            public void onFailed(String cause) {
                System.out.println("Failed "+cause);
                //progressbar.setVisible(false);
            }
        });
    }
}
