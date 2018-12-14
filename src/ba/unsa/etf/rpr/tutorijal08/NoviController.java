package ba.unsa.etf.rpr.tutorijal08;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NoviController {
    public TextField ime;
    public TextField prezime;
    public TextField grad;
    public TextField adresa;
    public TextField postanskiBroj;
    public SimpleStringProperty text = new SimpleStringProperty("");

    public NoviController() {}

    public boolean validacija() throws Exception {
        URL url = new URL("http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=" + postanskiBroj.getText());
        BufferedReader ulaz = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        String json = "", line = null;
        while ((line = ulaz.readLine()) != null)
            json = json + line;
        if (json.equals("OK")) return true;
        return false;
    }

    public void initialize() {
        
    }
}
