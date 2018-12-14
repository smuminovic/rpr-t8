package ba.unsa.etf.rpr.tutorijal08;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    public SimpleStringProperty text;
    public SimpleListProperty<String> lista;
    public TextField uzorak;
    public Button searchBtn;
    public ListView list;
    public List<String> result1;
    public Button stopBtn;
    public ProgressBar progressBar;
    public boolean prekidanje = false;
    private Thread thread1, thread2;

    public Controller() {
        text = new SimpleStringProperty("");
        lista = new SimpleListProperty<>();
        result1 = Collections.synchronizedList(new ArrayList<>());
        progressBar = new ProgressBar(0);
    }

    @FXML
    public void initialize() {
        uzorak.textProperty().bindBidirectional(text);
        list.itemsProperty().bindBidirectional(lista);
        lista.set(FXCollections.observableArrayList(result1));
        stopBtn.setDisable(true);
        prekidanje = false;
        progressBar.setProgress(0);
    }

    public void traziFajlove(File f) {
        if (prekidanje) return;
        try {
            File[] files = f.listFiles();
            if (files == null) return;
            for (File file : files) {
                if (file.isDirectory()) {
                    traziFajlove(file);
                } else {
                    if (file.getCanonicalPath().toLowerCase().contains(uzorak.getText().toLowerCase())) {
                        String result = file.getCanonicalPath();
                        Platform.runLater(() -> {
                            list.getItems().add(result);
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnSearchBtn(ActionEvent actionEvent) {
        initialize();
        Runnable r1 = () -> {
            searchBtn.setDisable(true);
            uzorak.setDisable(true);
            stopBtn.setDisable(false);
            traziFajlove(new File(System.getProperty("user.home")));
        };
        Runnable r2 = () -> {
            for (int i = 1; i <= 1000; i++) {
                if (prekidanje) {
                  progressBar.setProgress(1000);
                break;
                }
                progressBar.setProgress(i / 1000.0);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1 = new Thread(r1);
        thread2 = new Thread(r2);
        thread1.start();
        thread2.start();
    }

    public void clickOnStopBtn(ActionEvent actionEvent) {
        if (thread1 == null || thread2 == null) {
            return;
        }
        prekidanje = true;
        searchBtn.setDisable(false);
        uzorak.setDisable(false);
        stopBtn.setDisable(true);
    }
}
