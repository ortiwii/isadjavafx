package ehu.isad;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Clock;

public class ComboBoxExperiments extends Application  {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Txanpon Birtualen Prezioa");

        ComboBox comboBox = new ComboBox();

        comboBox.getItems().add("btc");
        comboBox.getItems().add("eth");
        comboBox.getItems().add("ltc");

        comboBox.setEditable(true);
        Label label = new Label();
        Gson gson = new Gson();
        comboBox.setOnAction(e -> {
            String emaitza = this.getString((String)comboBox.getValue());
            Txanpona txanpona = gson.fromJson(emaitza, Txanpona.class);
            label.setText("1 "+(String)comboBox.getValue()+"= "+txanpona.price);
        });
        VBox vbox = new VBox(label, comboBox);
        Scene scene = new Scene(vbox, 200, 120);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private String getString (String txanpona){
        String inputLine = "";
        URL coinmarket;

        try {
            coinmarket = new URL("https://api.gdax.com/products/"+txanpona+"-eur/ticker");
            URLConnection yc = coinmarket.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            inputLine = in.readLine();
            in.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return inputLine;
    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    public class Txanpona {
        float price;
    }

}
