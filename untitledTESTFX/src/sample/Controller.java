package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.util.Callback;
import javafx.util.converter.DateTimeStringConverter;


public class Controller extends Main{

    @FXML
    private ChoiceBox departureBox; //creating an instance of the choiceBox with ID departureBox
    @FXML
    private ChoiceBox arrivalsBox; //creating an instance of the choiceBox with ID arrivalBox
    @FXML
    private Button enter;
    @FXML
    private Label label;
    @FXML
    private TextField timeBox;

    TrainQuery TRAIN = new TrainQuery();

    @FXML
    private void initialize() {         //calling the initialize method to initialize our different choice boxes
        this.departureBox.setItems(TRAIN.cityNamesLIST()); //setting the arraylist: departureList as a parameter for the departureBox
        departureBox.setValue("la");
        arrivalsBox.setItems(TRAIN.cityNamesLIST()); //setting the arraylist: arrivalsList as a parameter for the departureBox
        arrivalsBox.setValue("ar");
    }


    public String getDept(){
        String deptCity = departureBox.getValue().toString();
        return deptCity;
    }

    public String getArr(){
        String arrCity = arrivalsBox.getValue().toString();
        return arrCity;
    }

  /* public Date getTime() throws ParseException {

        if (this.timeBox.getText().length() != 5) {

        }
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date time = format.parse(this.timeBox.getText());

        return time;
    }*/   // DISCARD ABOVE - NOT ENOUGH TIME (no pun intended).

   @FXML
       public void openRoute() throws ParseException {

       tableview = new TableView();
       buildData(getDept(),getArr());

       Scene scene = new Scene(tableview);

        Stage routeStage = new Stage();
        routeStage.setTitle("YOUR SCHEDULE: ");
        routeStage.setScene(scene);
        routeStage.show();
    }

    private ObservableList<ObservableList> data;
    private TableView tableview;

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:\\Users\\Lars\\Documents\\SQLtest\\train.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //CONNECTION DATABASE
    public void buildData(String dc, String ac){
        data = FXCollections.observableArrayList();
        Connection c ;
        try{
            // c = DBConnect.connect();
            c = this.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL =
                    "WITH DEPARTURE AS (\n" +
                            "select\n" +
                            "city.city DepartureCity,\n" +
                            "time.time DepartureTime,\n" +
                            "train.TrainId,\n" +
                            "train.TrainName\n" +
                            "from city \n" +
                            "join stop on stop.CityId = city.id\n" +
                            "join time on stop.TimeId = time.timeid\n" +
                            "join train on stop.TrainId = train.TrainId\n" +
                            "WHERE \n" +
                            "city.city = '"+ dc + "'\n" +
                            ")\n" +
                            "SELECT \n" +
                            "Departure.DepartureCity,\n" +
                            "Departure.DepartureTime,\n" +
                            "city.City ArrivalCity,\n" +
                            "time.time ArrivalTime,\n" +
                            "Departure.TrainName\n" +
                            "FROM city\n" +
                            "join stop on stop.CityId = city.id\n" +
                            "join time on time.TimeId = stop.TimeId\n" +
                            "JOIN Departure ON Departure.TrainId = stop.TrainId\n" +
                            "WHERE city.city = '"+ ac + "' AND ArrivalTime > DepartureTime";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){ // Looping though meta data(coulmn names) and creating coloums with them.

                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    row.add(rs.getString(i));
                }

                System.out.println("Row [1] added "+row );
                data.add(row); // Adding data to the coloumns

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


