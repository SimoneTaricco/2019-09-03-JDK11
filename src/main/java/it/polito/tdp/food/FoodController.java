/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import it.polito.tdp.food.model.PortionAndWeight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<Portion> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
    		
    		String passiTxt = this.txtPassi.getText();
    		int passi = Integer.parseInt(passiTxt);	
    		Portion partenza = this.boxPorzioni.getValue();
    		
    		List<Portion> res = model.trovaPercorso(partenza, passi);
    		
    		if (res.size()<=1) {
    			txtResult.setText("Cammino di " + passi + " passi non trovato.");
    			return;
    		}
    		
    		this.txtResult.appendText("Cammino massimo di " + passi + " passi da nodo " + this.boxPorzioni.getValue() + ":\n\n");
    		
    		for (Portion p:model.trovaPercorso(this.boxPorzioni.getValue(), passi)) 
    			this.txtResult.appendText(p + "\n");
    		
    		this.txtResult.appendText("\nPeso totale: " + model.pesoMassimo());
    		
    		} catch (NullPointerException e) {
    			txtResult.setText("Porzione di partenza non selezionata!");
    			return;
    	} catch (NumberFormatException e) {
			txtResult.setText("Errore: il valore inserito non è un intero!");
			return;
	}
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();

    	try {
    		
    		Portion partenza = boxPorzioni.getValue();
    		List<PortionAndWeight> res = model.correlate(partenza);
    		
    		this.txtResult.appendText("Porzioni correlate a  " + partenza + ":\n\n");
    		
    		for (PortionAndWeight p:res) {
    			this.txtResult.appendText(p.getPortion() + " | peso: " + p.getWeight() + "\n");
    		}
    		
    		} catch (NullPointerException e) {
    			txtResult.setText("Porzione di partenza non selezionata!");
    			return;
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String annoS = txtCalorie.getText();
    	try {
    		int anno = Integer.parseInt(annoS);	
    		model.creaGrafo(anno);
    		this.boxPorzioni.getItems().addAll(model.vertici());
    		
    		} catch (NumberFormatException e) {
    			txtResult.setText("Errore: il valore inserito non è un intero!");
    			return;
    	}

        	
        this.txtResult.setText("Grafo creato!\nNumero vertici: " + model.vertici().size() +"\nNumero archi: " + model.numeroArchi());

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
