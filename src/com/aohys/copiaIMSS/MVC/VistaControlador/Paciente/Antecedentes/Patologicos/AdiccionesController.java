/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAdicciones;
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
public class AdiccionesController implements Initializable {

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
    patoAdicciones pAdicciones = new patoAdicciones();
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML padecimientos previos
    @FXML private RadioButton rbSi;
    @FXML private RadioButton rbNo;
    @FXML private TextField txtEdadIncio;
    @FXML private TextField txtEdadFinal;
    @FXML private ComboBox<String> cbbDescripcion;
    @FXML private Button bttAgregar;
    //FXMl de tabla
    @FXML private TableView<patoAdicciones> tvAdiccion;
    @FXML private TableColumn<patoAdicciones, String> colAdicion;
    @FXML private TableColumn<patoAdicciones, String> colEdadInicio;
    @FXML private TableColumn<patoAdicciones, String> colEdadFinal;
    @FXML private TableColumn<patoAdicciones, String> colDependencia;
    
    //Lista para combobox
    ObservableList<String> listaAdicciones = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean dependeciaSiNo = false;
    Image guardar = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
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
        errorMessage += aux.verificaValufield(cbbDescripcion, "Descripción");
        errorMessage += aux.verificaTexField(txtEdadIncio, "Edad de inicio");
        errorMessage += aux.verificaTexField(txtEdadFinal, "Edad final");
        
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
        listaAdicciones.addAll("ALCOHOL","ALUCINÓGENOS(LSD)","ANALGÉSICOS NARCOTICOS","COCAÍNA","HEROÍNA","OPIO",
                            "INHALABLES","MARIHUANA","SEDANTES","TRANQUILIZANTES","TABACO","OTROS","NINGUNO","NO APLICA");
        cbbDescripcion.setItems(listaAdicciones);
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
                dependeciaSiNo = true;
            }else{
                dependeciaSiNo = false;
            }
        });
    }
    
    /**
     * formato de texfields 
     */
    private void formatoTexfields(){
        txtEdadIncio.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtEdadFinal.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        aux.toolTip(txtEdadIncio, "campo numérico");
        aux.toolTip(txtEdadFinal, "campo numérico");
    }
    
    /**
     * le da formato a los bottones
     */
    private void formatoButtons(){
        bttAgregar.setOnAction((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardaAnteAdiccion(conex);
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
    private void guardaAnteAdiccion(Connection conex){
        String id_pAdicc = aux.generaID();
        String adiccion_pAdicc = cbbDescripcion.getValue();
        int edInicio_pAdicc = Integer.valueOf(txtEdadIncio.getText());
        int edFinal_pAdicc = Integer.valueOf(txtEdadFinal.getText());
        boolean dependencia_pAdicc = dependeciaSiNo;
        String id_paciente = paci.getId_paciente();
        pAdicciones.agregaPatoAdiccion(id_pAdicc, adiccion_pAdicc, edInicio_pAdicc, 
                edFinal_pAdicc, dependencia_pAdicc, id_paciente, conex);
    }
    
    /**
     * actualiza la tabla 
     * @param conex 
     */
    private void actualizaTabla(Connection conex){
        colAdicion.setCellValueFactory(new PropertyValueFactory<> ("adiccion_pAdicc"));
        colEdadInicio.setCellValueFactory(informacionDeCelda->{
            patoAdicciones p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                    p.getEdInicio_pAdicc(), "Años");
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colEdadFinal.setCellValueFactory(informacionDeCelda->{
            patoAdicciones p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                    p.getEdFinal_pAdicc(), "Años");
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colDependencia.setCellValueFactory(informacionDeCelda->{
            patoAdicciones p = informacionDeCelda.getValue();
            String regresaFormato;
            if (p.getDependencia_pAdicc()) {
                regresaFormato = "SI";
            }else
                regresaFormato = "NO";
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        tvAdiccion.setItems(pAdicciones.listaAntePaAdicciones(conex, paci.getId_paciente()));
    }
    
    /**
     * limpia cuadros 
     */
    private void limpiaCuadros(){
        cbbDescripcion.setValue(null);
        txtEdadIncio.setText("");
        txtEdadFinal.setText("");
    }
    
    /**
     * borra el padecimiento selecionado
     */
    @FXML
    private void borrar(){
         patoAdicciones ant = tvAdiccion.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarAntePatoAdicciones(ant.getId_pAdicc(), conex);
                 actualizaTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona una adicción", "Selecciona una adicción", 
                     "Es necesario seleccionar una adicción para ser borrada");
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
      //formato de botones
      formatoButtons();
      //formato de texfields
      formatoTexfields();
    } 
}
