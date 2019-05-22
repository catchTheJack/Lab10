package it.polito.tdp.porto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	Model model = new Model();
	
	public void setModel(Model model) {
		this.model = model;
		
		//creata arrayList per non dover eseguire due volte la query sql e la creazione del grafo
		//(anche se è un operazione da pochi millisecondi)
		ArrayList<Author> autori = new ArrayList<Author>(model.getAuthors());
		boxPrimo.getItems().addAll(autori);
		boxSecondo.getItems().addAll(autori);
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	txtResult.clear();
    	List<Author> collaborazioni = new ArrayList<Author>(model.collab(boxPrimo.getValue()));
    	for(Author temp: collaborazioni) {
    		txtResult.appendText(temp.toString()+"\n");
    	}

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	txtResult.clear();
    	if(model.thereIsCollab(boxPrimo.getValue(), boxSecondo.getValue())==true) {
    		txtResult.appendText("I due autori hanno una collaborazione diretta \n Inserisci altri un altra combinazione");
    	}
    	else {
    		List<String> minimi = new ArrayList<String>(model.trovaCamminoMinimo(boxPrimo.getValue(), boxSecondo.getValue()));
    		for(String temp: minimi) {
    			txtResult.appendText(temp+"\n");
    		}
    		
    	}

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
}
