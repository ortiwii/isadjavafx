package ehu.isad;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BigarrenAriketa  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("OpenLibrary APIa aztertzen");

        ComboBox comboBox = new ComboBox();
        String[] isbn = {"9781491920497", "1491910399", "1491946008", "1491978236", "9781491906187"};

        comboBox.getItems().add(0, "Blockchain: Blueprint for a New Economy");
        comboBox.getItems().add(1, "R for Data Science");
        comboBox.getItems().add(2, "Fluent Python");
        comboBox.getItems().add(3, "Natural Language Processing with PyTorch");
        comboBox.getItems().add(4, "Data Algorithms");

        comboBox.setEditable(true);
        Label labelIzena = new Label();
        Label labeOrrikop = new Label();
        Label labelIdazlea = new Label();
        Gson gson = new Gson();
        comboBox.setOnAction(e -> {
            String actIsbn = isbn[comboBox.getSelectionModel().getSelectedIndex()];
            Liburua liburua = this.getLiburua(this.getString(actIsbn),actIsbn);
            String publ = liburua.publishers[0];
            labelIzena.setText(liburua.title);
            labeOrrikop.setText(String.valueOf(liburua.number_of_pages));
            for (int i = 1; i < liburua.publishers.length; i++){
                publ = publ + ", " +liburua.publishers[i];
            }
            labelIdazlea.setText(publ);
        });
        VBox vbox = new VBox(comboBox, new Label(), labelIzena, labeOrrikop, labelIdazlea);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 150);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private String getString(String liburua) {
        String inputLine = "";
        URL coinmarket;

        try {
            coinmarket = new URL("https://openlibrary.org/api/books?bibkeys=ISBN:"+liburua+"&jscmd=details&format=json");
            URLConnection yc = coinmarket.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            inputLine = in.readLine();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputLine;
    }
    private Liburua getLiburua (String inputLine, String isbn){
        Liburua lib = null;
        String[] zatiak = inputLine.split("ISBN:"+isbn+"\":");
        String text = zatiak[1].substring(0, zatiak[1].length()-1);

        String[] details = text.split("details"+"\":");
        String detText = details[1].substring(0, details[1].length()-1);

        details = detText.split(", \"preview\":");
        String finalGson = details[details.length-2];

        Gson gson = new Gson();
        lib = gson.fromJson(finalGson, Liburua.class);
        return lib;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public class Liburua {
        String [] publishers;
        String title;
        int number_of_pages;
    }
}