/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTransfucion;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.AntecedentesMédicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class TransfuncionalesController implements Initializable {

    //Variables de escena
    private AntecedentesMédicosController cordi;
    Paciente paci;
    
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(AntecedentesMédicosController cordi) {
        this.cordi = cordi;
        this.paci = PrincipalController.pacienteAUsar;
        // carga los componentes top
        cargaTop();
        try(Connection conex = dbConn.conectarBD()) {
            actualizaTabla(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    patoTransfucion pTransfucion = new patoTransfucion();
    
    //Conexion
    Hikari dbConn = new Hikari();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML padecimientos previos
    @FXML private RadioButton rbSi;
    @FXML private RadioButton rbNo;
    @FXML private TextField txtEdad;
    @FXML private TextField txtReaciones;
    @FXML private ComboBox<String> cbbFrecuencia;
    @FXML private ComboBox<String> cbbDescripcion;
    @FXML private Button bttAgregar;
    //FXMl de tabla
    @FXML private TableView<patoTransfucion> tvTransfuncion;
    @FXML private TableColumn<patoTransfucion, String> colReaciones;
    @FXML private TableColumn<patoTransfucion, String> colEdad;
    @FXML private TableColumn<patoTransfucion, String> colTipoTrans;
    
    //Lista para combobox
    ObservableList<String> listaVecesAL = FXCollections.observableArrayList();
    ObservableList<String> listaTransfuciones = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean reacionesSiNo = false;
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
        lbEdad.setText(aux.edadConMes(paci.getFechaNacimiento_paciente()));
        lbCURP.setText(paci.getCurp_paciente());
        lbSexo.setText(paci.getSexo_paciente());
        lbIdMedico.setText(paci.getId_paciente());
    }    

    /**
     * verifica los requisitos 
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbDescripcion, "Traumatismo");
        errorMessage += aux.verificaValufield(cbbFrecuencia, "Tiempo de traumatismo");
        errorMessage += aux.verificaTexField(txtEdad, "Edad");
        if (rbSi.isSelected()) {
            errorMessage += aux.verificaTexField(txtReaciones, "Cual reacción");
        }
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * formato de veces a la edad
     */
    private void formatoCombobox(){
        listaVecesAL.addAll("Hora(s)", "Día(s)", "Semana(s)", "Mes(es)", "Año(s)");
        cbbFrecuencia.setItems(listaVecesAL);
        
        listaTransfuciones.addAll("Transfusión total","Transfusión de plasma","Transfusión de glóbulos rojos",
                                    "Transfusión de glóbulos blancos","Transfusión de plaquetas","No lo sabe");
        cbbDescripcion.setItems(listaTransfuciones);
    }
    
    /**
     * radios de buttons formatos
     */
    private void formatoRadioButtons(){
        ToggleGroup seleccion = new ToggleGroup();
        seleccion.getToggles().addAll(rbSi,rbNo);
        rbNo.fire();
        seleccion.selectedToggleProperty().addListener((onservable,viejo,nuevo)->{
            if (rbSi.isSelected()) {
                txtReaciones.setDisable(false);
                reacionesSiNo = true;
            }else{
                txtReaciones.setDisable(true);
                reacionesSiNo = false;
            }
        });
    }
    
    /**
     * formato de texfields 
     */
    private void formatoTexfields(){
        txtEdad.setTextFormatter(new TextFormatter      (aux.formato(3, 3)));
        txtReaciones.setTextFormatter(new TextFormatter  (aux.formato(500, 4)));
        txtReaciones.setDisable(true);
        aux.toolTipSuperior(txtEdad, "campo numérico");
    }
    
    /**
     * le da formato a los bottones
     */
    private void formatoButtons(){
        bttAgregar.setOnAction((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardaAnteTransfunciones(conex);
                    actualizaTabla(conex);
                    limpiaCuadros();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        bttAgregar.setGraphic(new ImageView(guardar));
    }
    
    /**
     * agrega los antencedentes medicos
     * @param conex 
     */
    private void guardaAnteTransfunciones(Connection conex){
        String id_pTrans = aux.generaID();
        int edad_pTrans = Integer.valueOf(txtEdad.getText());
        String duracion_pTrans = cbbFrecuencia.getValue();
        String tipoTrasn_pTrans = cbbDescripcion.getValue();
        boolean reacciones_pTrans = reacionesSiNo;
        String tipoReacion_pTrans = txtReaciones.getText();
        String id_paciente = paci.getId_paciente();
        
        pTransfucion.agregaPatoTransfucion(id_pTrans, edad_pTrans, duracion_pTrans, 
                tipoTrasn_pTrans, reacciones_pTrans, tipoReacion_pTrans, id_paciente, conex);
    }
    
    /**
     * actualiza la tabla 
     * @param conex 
     */
    private void actualizaTabla(Connection conex){
        colTipoTrans.setCellValueFactory(new PropertyValueFactory<> ("tipoTrasn_pTrans"));
        colEdad.setCellValueFactory(informacionDeCelda->{
            patoTransfucion p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getEdad_pTrans(), p.getDuracion_pTrans());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colReaciones.setCellValueFactory(informacionDeCelda->{
            patoTransfucion p = informacionDeCelda.getValue();
            String regresaFormato;
            if (p.getTipoReacion_pTrans().equals("")) {
                regresaFormato = "NO";
            }else
                regresaFormato = p.getTipoReacion_pTrans();
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        tvTransfuncion.setItems(pTransfucion.listaAntePaTransfucion(conex, paci.getId_paciente()));
    }
    
    /**
     * limpia cuadros 
     */
    private void limpiaCuadros(){
        txtEdad.setText("");
        cbbDescripcion.setValue(null);
        cbbFrecuencia.setValue(null);
        txtReaciones.setText("");
    }
    
    /**
     * borra el padecimiento selecionado
     */
    @FXML
    private void borrar(){
         patoTransfucion ant = tvTransfuncion.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarAntePatoTrausnfuncion(ant.getId_pTrans(), conex);
                 actualizaTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona una transfusión", "Selecciona una transfusión", 
                     "Es necesario seleccionar una transfusión para ser borrado;");
    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //formato del combobox
      formatoCombobox();
      //formato de los radio buttons
      formatoRadioButtons();
      //formato de textfields
      formatoTexfields();
      //formato de botones
      formatoButtons();
    } 
    
}
