package com.example.attendencemanagement;

import com.example.attendencemanagement.utils.PostRequest;
import com.google.gson.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public ListView listView;
    public ComboBox departmentCM;
    public ProgressIndicator progressbar;

    String[] departments = {"CSE","BBA","English","Math","Physics"};
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
    private ArrayList<StudentDataModel> processData(String jsonData){
        String r = jsonData;
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
                    c++;

                }

            }
            model.setPresentMap(presentMap);
            model.setTotalPresent(p);
            model.setTotalDays(c);
            list.add(model);
        }
        return list;

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Home Initialize");
        comboSetup();
        initStudents(processData());
        progressbar.setVisible(true);
        new PostRequest("", "", new PostRequest.PostRequestListener() {
            @Override
            public void onSuccess(String response) {
                Platform.runLater(()->{
                    initStudents(processData(response));
                    progressbar.setVisible(false);
                });

            }

            @Override
            public void onFailed(String cause) {
                System.out.println("Failed");
                progressbar.setVisible(false);
            }
        });



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


            });
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

    //#endregion

    //#region : Mange top Menus [Combobox and Another Input
    private void comboSetup(){
        departmentCM.setItems(FXCollections.observableArrayList(departments));
        departmentCM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int p = departmentCM.getSelectionModel().getSelectedIndex();
                System.out.println("Selected : "+p);
            }
        });
    }
    //#endregion
}
