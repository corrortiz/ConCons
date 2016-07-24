/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes;

import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.AdiccionesController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.AlergiasController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.MedicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.QuirúrgicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.TransfuncionalesController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos.TraumáticosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class AntecedentesMédicosController implements Initializable {
    //Variables de escena
    public static PrincipalController cordi;
    Auxiliar aux = new Auxiliar();
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param donde 
     */
    public void transmisor(PrincipalController cordi, int donde) {
        AntecedentesMédicosController.cordi = cordi;
        tabPane.getSelectionModel().select(donde);
    }
    
    //Stack pane de transacion
    @FXML private StackPane stkMedico;
    @FXML private StackPane stkQuir;
    @FXML private StackPane stkTrauma;
    @FXML private StackPane stkTransfu;
    @FXML private StackPane stkAlergias;
    @FXML private StackPane stkAdicciones;
    
    @FXML private TabPane tabPane;
    
    @FXML private Button bttRegresar;
    Image aceptar = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Carga Los componentes de la tapPersonal
        lanzaMedico();
        lanzaQuirc();
        lanzaTrauma();
        lanzaTransfunciones();
        lanzaAlergias();
        lanzaAddiciones();
        
        bttRegresar.setGraphic(new ImageView(aceptar));
        
        bttRegresar.setOnAction(evento->{
            cordi.lanzaHistoriaMedica(PrincipalController.pacienteAUsar);
        });
    }    
    
    /**
     * lanza pantalla personal
     */
    public void lanzaMedico(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Medicos.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            MedicosController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkMedico);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza pantalla quiruquico
     */
    public void lanzaQuirc(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Quirúrgicos.fxml"));
            AnchorPane unoAnchorPane = (AnchorPane) loader.load();
            QuirúrgicosController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkQuir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza el el traumatimos
     */
    public void lanzaTrauma(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Traumáticos.fxml"));
            AnchorPane unoAnchorPane = (AnchorPane) loader.load();
            TraumáticosController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkTrauma);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza transfuncionales
     */
    public void lanzaTransfunciones(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Transfuncionales.fxml"));
            AnchorPane unoAnchorPane = (AnchorPane) loader.load();
            TransfuncionalesController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkTransfu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * carga las alergias
     */
    public void lanzaAlergias(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Alergias.fxml"));
            AnchorPane unoAnchorPane = (AnchorPane) loader.load();
            AlergiasController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkAlergias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * cargar adicciones
     */
    public void lanzaAddiciones(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/Patologicos/Adicciones.fxml"));
            AnchorPane unoAnchorPane = (AnchorPane) loader.load();
            AdiccionesController controller = loader.getController();
            controller.transmisor(this);
            aux.setScreen(unoAnchorPane, stkAdicciones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
