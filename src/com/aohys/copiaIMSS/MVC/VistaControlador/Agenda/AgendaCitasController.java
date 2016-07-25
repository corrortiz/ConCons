/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Agenda;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class AgendaCitasController implements Initializable {

    //Variables de escena
    public static PrincipalController cordi;
    private int tab;

    /**
     * Inicia la esecena 
     * @param cordi 
     * @param tab 
     */
    public void transmisor(PrincipalController cordi, int tab) {
        AgendaCitasController.cordi = cordi;
        this.tab = tab;
        tabPane.getSelectionModel().select(tab);
    }
    
    /**
     * le da formato a los a tabpane
     */
   private void formatoTabPane(){
       tbAgregar.selectedProperty().addListener((obs,vie,nue)->{
           if (nue) {
               lanzaBusquedaPaciente();
           }else
               if (!stackPane.getChildren().isEmpty()) {
                   stackPane.getChildren().remove(0);
               }
       });
       
       tbConsulta.selectedProperty().addListener((obs,vie,nue)->{
           if (nue) {
                lanzaConsultaCitas();
           }else
               if (!tercerdoStackPane.getChildren().isEmpty()) {
                   tercerdoStackPane.getChildren().remove(0);
               }
       });
       
       tabCancelacion.selectedProperty().addListener((obs,vie,nue)->{
           if (nue) {
               lanzaBusquedaBorrar();
           }else
               if (!segundoStackPane.getChildren().isEmpty()) {
                   segundoStackPane.getChildren().remove(0);
               }
       });
   }
    
    //Variables a utilizar
    Auxiliar aux = new Auxiliar();
    
    //Conexion base de datos
    Vitro dbConn = new Vitro();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatoTabPane();
    }    
    
    
    /*****************************************/
    /**
     * Lanza una nueva cita con el paciente seleccioando
     * @param paci
     */
    @FXML
    public void lanzaNuevoCita(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/NuevaCita.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            NuevaCitaController controller = loader.getController();
            controller.transmisor(this, paci, cordi);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /******************************************************************************/
    //Segunda pestaña
    public void lanzaBusquedaBorrar(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/ListaCancelacion.fxml"));
            BorderPane  unoAnchorPane = (BorderPane) loader.load();
            ListaCancelacionController controller = loader.getController();
            controller.transmisor(cordi, this);
            setScreenSegundo(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void lanzaBusquedaBorrarInterna(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/ListaCancelacion.fxml"));
            BorderPane  unoAnchorPane = (BorderPane) loader.load();
            ListaCancelacionController controller = loader.getController();
            controller.transmisor(cordi, this);
            tabPane.getSelectionModel().select(tabCancelacion);
            setScreenSegundo(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza cancela cita
     * @param paci 
     */
    public void lanzaCancelaCitas(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/CancelaCitas.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            CancelaCitasController controller = loader.getController();
            controller.transmisor(this, paci, cordi);
            tabPane.getSelectionModel().select(tabCancelacion);
            setScreenSegundo(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void lanzaConsultaCitas(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/ConsultaCita.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ConsultaCitaController controller = loader.getController();
            controller.transmisor(this, cordi);
            setScreenTercero(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza la busqueda de pacientes
     */
    public void lanzaBusquedaPaciente(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/BusquedaUsuario.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            BusquedaUsuarioController controller = loader.getController();
            controller.transmisor(cordi, this);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    //FXML de la clase
    @FXML private StackPane stackPane;
    @FXML private StackPane segundoStackPane;
    @FXML private StackPane tercerdoStackPane;
    @FXML private TabPane tabPane;
    @FXML private Tab tabCancelacion;
    @FXML private Tab tbConsulta;
    @FXML private Tab tbAgregar;

    /****************************************************************************/
    //Efecto    
    /**
    * Set Screen 
    * @param view
    * @return boolean
    */
    public boolean setScreen(Parent view) {       
       final DoubleProperty opacity = stackPane.opacityProperty();
       if (!stackPane.getChildren().isEmpty()) {    //if there is more than one screen
           Timeline fade = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                   new KeyFrame(new Duration(250), new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent t) {
                           stackPane.getChildren().remove(0);        //remove the displayed screen
                           stackPane.getChildren().add(0, view);     //add the screen
                           Timeline fadeIn = new Timeline(
                                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                           fadeIn.play();
                       }
                   }, new KeyValue(opacity, 0.0)));
           fade.play();
       } else {
           stackPane.setOpacity(0.0);
           stackPane.getChildren().add(view);       //no one else been displayed, then just show
           Timeline fadeIn = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
           fadeIn.play();
       }
       return true;
   }
    
    
    public boolean setScreenSegundo(Parent view) {       
       final DoubleProperty opacity = segundoStackPane.opacityProperty();
       if (!segundoStackPane.getChildren().isEmpty()) {    //if there is more than one screen
           Timeline fade = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                   new KeyFrame(new Duration(250), new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent t) {
                           segundoStackPane.getChildren().remove(0);        //remove the displayed screen
                           segundoStackPane.getChildren().add(0, view);     //add the screen
                           Timeline fadeIn = new Timeline(
                                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                           fadeIn.play();
                       }
                   }, new KeyValue(opacity, 0.0)));
           fade.play();
       } else {
           segundoStackPane.setOpacity(0.0);
           segundoStackPane.getChildren().add(view);       //no one else been displayed, then just show
           Timeline fadeIn = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
           fadeIn.play();
       }
       return true;
   }
    
    public boolean setScreenTercero(Parent view) {       
       final DoubleProperty opacity = tercerdoStackPane.opacityProperty();
       if (!tercerdoStackPane.getChildren().isEmpty()) {    //if there is more than one screen
           Timeline fade = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                   new KeyFrame(new Duration(250), new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent t) {
                           tercerdoStackPane.getChildren().remove(0);        //remove the displayed screen
                           tercerdoStackPane.getChildren().add(0, view);     //add the screen
                           Timeline fadeIn = new Timeline(
                                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                           fadeIn.play();
                       }
                   }, new KeyValue(opacity, 0.0)));
           fade.play();
       } else {
           tercerdoStackPane.setOpacity(0.0);
           tercerdoStackPane.getChildren().add(view);       //no one else been displayed, then just show
           Timeline fadeIn = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
           fadeIn.play();
       }
       return true;
   }
    
}
