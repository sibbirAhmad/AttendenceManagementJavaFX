package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.DataSingleton;
import com.example.attendencemanagement.utils.PostRequest;
import com.example.attendencemanagement.utils.Utils;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public ListView listView;
    public ComboBox departmentCM;
    public ProgressIndicator progressbar;
    public ComboBox sessionCM;
    public ComboBox batchCM;
    public ComboBox subjectCM;
    public DatePicker datePicker;
    public Label totalPresentLB;
    public Label totalAbsentLB;

    DataSingleton data = DataSingleton.getInstance();

    String[] departments = {"CSE","BBA","English","Math","Physics"};
    String[] sessions = {"FALL-22","SPRING-22","SUMMER-22","FALL-23"};
    String[] batch = {"46A","46B","49A","49B"};
    String[] subjects = {"MAT101","CSE215","CSE216","SE29","SE210"};
    private ArrayList<StudentDataModel> studentDataArrayList;
    private ArrayList<StudentDataModel> processData(){
        String r = "[{\"id\":173462140,\"name\":\"Jahid Khan\",\"p10092021\":1,\"p11092021\":1,\"p12092021\":0},{\"id\":173462141,\"name\":\"Roton Mia\",\"p10092021\":1,\"p11092021\":1,\"p12092021\":0},{\"id\":173462142,\"name\":\"Moftafzur Rahman\",\"p10092021\":1,\"p11092021\":1,\"p12092021\":0},{\"id\":173462143,\"name\":\"Noyon Mia\",\"p10092021\":1,\"p11092021\":1,\"p12092021\":0},{\"id\":173462144,\"name\":\"Liakot Ali\",\"p10092021\":0,\"p11092021\":1,\"p12092021\":0},{\"id\":173462145,\"name\":\"Babor Ali\",\"p10092021\":1,\"p11092021\":1,\"p12092021\":1},{\"id\":173462146,\"name\":\"Johir Ahmed\",\"p10092021\":0,\"p11092021\":0,\"p12092021\":0},{\"id\":173462130,\"name\":\"Monsur Ali\",\"p10092021\":0,\"p11092021\":1,\"p12092021\":1}]\n";


        JsonArray jsonObject = new JsonParser().parse(r).getAsJsonArray();
        ArrayList<StudentDataModel> list = new ArrayList<>();
        String today = "p12092021";
        for (JsonElement object: jsonObject) {
            JsonObject so = object.getAsJsonObject();
            Iterator<String> array = so.keySet().stream().iterator();
            System.out.println("Name : "+so.get("name"));
            //Todo : Adding value to array list
            StudentDataModel model = new StudentDataModel();
            model.setName(so.get("name").toString());
            model.setId(so.get("id").toString());
            HashMap<String,Integer> presentMap = new HashMap<>();
            int c = 0;
            int p = 0;
            while (array.hasNext()){
                String key = array.next();
                if (key.startsWith("p")){
                    int present = Integer.parseInt(so.get(key).toString());
                    if (present==1){
                        p++;
                    }
                    //Todo : Check todays present
                    if (key.contains(today)){
                        model.setPresent(present);
                    }
                    presentMap.put(key,present);
                    System.out.println(key+" --> "+present);
                }

                c++;
            }
            model.setPresentMap(presentMap);
            model.setTotalPresent(p);
            model.setTotalDays(c);
            list.add(model);
        }
        return list;

    }
    String selectedDate = "";
    private ArrayList<StudentDataModel> processData(String jsonData){
        String r = jsonData;
        ArrayList<StudentDataModel> list = new ArrayList<>();
        JsonObject o = (JsonObject) new JsonParser().parse(r);
        int responseCode = o.get("responseCode").getAsInt();
        if (responseCode==200){
            System.out.println("Object "+o.get("data"));
            r = o.get("data").getAsString();
            JsonArray jsonObject = new JsonParser().parse(r).getAsJsonArray();
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
                       // System.out.println(key+" --> "+present);
                        c++;
                    }
                }

                model.setPresentMap(presentMap);
                model.setTotalPresent(p);
                model.setTotalDays(c);
                list.add(model);
            }
            totalPresentLB.setText("Present : "+p);
            totalAbsentLB.setText("Absent : "+(list.size()-p));
        }else if (responseCode==201){
            //Show A alert Dialog
            Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to create", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                //Todo : Create new present
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("requestFrom","teacher");
                jsonObject.addProperty("request","createNewPresent");
                jsonObject.addProperty("date",selectedDate);
                jsonObject.addProperty("subjectName",formattedSubjectName);
                String newPresentReqJson = jsonObject.toString();
                progressbar.setVisible(true);
                new PostRequest("", newPresentReqJson, new PostRequest.PostRequestListener() {
                    @Override
                    public void onSuccess(String response) {
                        progressbar.setVisible(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Successfully created\nStart take present", ButtonType.OK);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {

                        }
                    }

                    @Override
                    public void onFailed(String cause) {
                        progressbar.setVisible(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create new present", ButtonType.OK);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {

                        }
                    }
                });
            }
            System.out.println("No data exist");

        }

        return list;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data.setProgressIndicator(progressbar);
        selectedDate = Utils.getFormatted();
        datePicker.setOnAction(actionEvent -> {
            LocalDate date = datePicker.getValue();
            System.err.println("Selected date: " + date);
        });
        comboSetup();
        studentDataArrayList = processData();
        initStudents(studentDataArrayList);



    }

    //#region : Init Student List
    String[] nameList = {"Jamal Hossain","Jahid Khan","Nabil Korim","Abdur Rohim","Kombelesh Babu","Faru Khan","Somchun Begum"};
    ObservableList<Pane> studentPaneList;

    private void initStudents(ArrayList<StudentDataModel> dataList){
        studentPaneList = FXCollections.observableArrayList();
        try {
            for (int i = 0; i < dataList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentCellModel.fxml"));
                Pane pane = fxmlLoader.load();
                StudentListController controller = fxmlLoader.getController();
                controller.setData(dataList.get(i));
                studentPaneList.add(pane);
            }
            //listView.getItems().addAll(list);
            listView.setItems(studentPaneList);
            progressbar.setVisible(false);
            listView.setOnMouseClicked(event->{
                int p = listView.getSelectionModel().getSelectedIndex();
                StudentDataModel student = studentDataArrayList.get(p);
                System.out.println("Clicked Student : "+student.getName());
                System.out.println("Opening student profile : "+p);
                Platform.runLater(()->{
                    data.setStudent(student);
                    TestController.getInstance().loadStudentProfile();
                });
            });

        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

    //#endregion

    //#region : Mange top Menus [Combobox and Another Input
    private void comboSetup(){
        departmentCM.setItems(FXCollections.observableArrayList(departments));
        sessionCM.setItems(FXCollections.observableArrayList(sessions));
        batchCM.setItems(FXCollections.observableArrayList(batch));
        subjectCM.setItems(FXCollections.observableArrayList(subjects));
        departmentCM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int p = departmentCM.getSelectionModel().getSelectedIndex();
                System.out.println("Selected : "+departmentCM.getValue());
            }
        });
    }
    String formattedSubjectName = "";
    public void check(ActionEvent actionEvent) {
       try {
           String department = departmentCM.getValue().toString().toUpperCase().replace(" ","");
           String session = sessionCM.getValue().toString().toUpperCase().replace("-","");
           String batch = batchCM.getValue().toString().toUpperCase().replace(" ","");
           String subject = subjectCM.getValue().toString().toUpperCase().replace(" ","");
           String date = datePicker.getValue().toString().replace("-","");
           formattedSubjectName = department+"_"+session+"_"+batch+"_"+subject;
           selectedDate = "p"+date;
           data.setFormattedSubjectName(formattedSubjectName);
           data.setDate(selectedDate);
           //formattedSubjectName = "CSE_SPRING22_49A_MAT101";
           System.err.println("Selected date: " + date);
           progressbar.setVisible(true);
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
                       studentDataArrayList = processData(response);
                       initStudents(studentDataArrayList);
                       progressbar.setVisible(false);
                   });

               }
               @Override
               public void onFailed(String cause) {
                   System.out.println("Failed "+cause);
                   progressbar.setVisible(false);
               }
           });

       }catch (Exception e){
           Alert alert = new Alert(Alert.AlertType.ERROR, "Please select required data", ButtonType.OK);
           alert.showAndWait();

           if (alert.getResult() == ButtonType.OK) {
               //Todo : Required a toast to re confirm
           }
           System.out.println("Error : "+e);
       }
    }
    //#endregion
}
