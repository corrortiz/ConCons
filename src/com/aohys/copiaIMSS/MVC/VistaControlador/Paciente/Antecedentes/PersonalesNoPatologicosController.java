/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes;

import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.NoPatologicos.PersonalesController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class PersonalesNoPatologicosController implements Initializable {

    //Variables de escena
    public static PrincipalController cordi;
    Auxiliar aux = new Auxiliar();
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(PrincipalController cordi) {
        PersonalesNoPatologicosController.cordi = cordi;
    }
    
    @FXML private StackPane stkPesonal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Carga Los componentes de la tapPersonal
        lanzaPersonal();
    }    
    
    /**
     * lanza pantalla personal
     */
    public void lanzaPersonal(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/NoPatologicos/Personales.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            PersonalesController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkPesonal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
