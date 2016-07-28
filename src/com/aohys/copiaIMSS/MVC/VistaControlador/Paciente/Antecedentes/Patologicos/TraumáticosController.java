/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTraumaticos;
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
public class TraumáticosController implements Initializable {

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
    patoTraumaticos pTraumaticos = new patoTraumaticos();
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML padecimientos previos
    @FXML private RadioButton rbTraumatismo;
    @FXML private RadioButton rbOtroTrauma;
    @FXML private TextField txtEdad;
    @FXML private TextField txtSecuelas;
    @FXML private ComboBox<String> cbbFrecuencia;
    @FXML private ComboBox<String> cbbDescripcion;
    @FXML private ComboBox<String> cbbLado;
    @FXML private Button bttAgregar;
    //FXMl de tabla
    @FXML private TableView<patoTraumaticos> tvTraumatismo;
    @FXML private TableColumn<patoTraumaticos, String> colNombreTrauma;
    @FXML private TableColumn<patoTraumaticos, String> colEdad;
    @FXML private TableColumn<patoTraumaticos, String> colSecuelas;
    @FXML private TableColumn<patoTraumaticos, String> colLado;
    
    //Lista para combobox
    ObservableList<String> listaVecesAL = FXCollections.observableArrayList();
    ObservableList<String> listaTrauma = FXCollections.observableArrayList();
    ObservableList<String> listaLadoLesion = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean actualizaSiNoMedico = false;
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
        errorMessage += aux.verificaValufield(cbbDescripcion, "Traumatismo");
        errorMessage += aux.verificaValufield(cbbFrecuencia, "Tiempo de traumatismo");
        errorMessage += aux.verificaTexField(txtEdad, "Edad");
        errorMessage += aux.verificaValufield(cbbLado, "Lado de la lesión");
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
        
        listaTrauma.addAll("Traumatismos en la cabeza","Traumatismos en el cuello","Traumatismos en el tórax",
                "Traumatismos en abdomen, parte inferior de la espalda, columna lumbar, pelvis y genitales externos",
                "Traumatismos en hombro y parte superior del brazo","Traumatismos en codo y antebrazo",
                "Traumatismos en muñeca, mano y dedos","Traumatismos en cadera y muslo","Traumatismos en rodilla y pierna",
                "Traumatismos en tobillo y pie","Traumatismos que afectan a múltiples regiones corporales",
                "Traumatismo de región anatómica no especificada","Efectos de cuerpo extraño que penetra a través de orificio natural",
                "Quemaduras y corrosiones","Quemaduras y corrosiones de la superficie externa del cuerpo, especificadas por localización",
                "Quemaduras y corrosiones limitadas a ojo y órganos internos","Quemaduras y corrosiones de regiones corporales múltiples y las no especificadas",
                "Congelación","Envenenamiento por, efectos adversos e infradosificación de fármacos, medicamentos y sustancias",
                "Efectos tóxicos de sustancias de procedencia principalmente no medicamentosa");
        cbbDescripcion.setItems(listaTrauma);
        
        listaLadoLesion.addAll("Derecho","Izquierdo","NO APLICA");
        cbbLado.setItems(listaLadoLesion);
    }
    
    /**
     * radios de buttons formatos
     */
    private void formatoRadioButtons(){
        ToggleGroup seleccion = new ToggleGroup();
        seleccion.getToggles().addAll(rbTraumatismo,rbOtroTrauma);
        rbTraumatismo.fire();
        seleccion.selectedToggleProperty().addListener((onservable,viejo,nuevo)->{
            if (rbOtroTrauma.isSelected()) {
                cbbDescripcion.setEditable(true);
            }else
                cbbDescripcion.setEditable(false);
        });
    }
    
    /**
     * formato de texfields 
     */
    private void formatoTexfields(){
        txtEdad.setTextFormatter(new TextFormatter      (aux.formato(3, 3)));
        txtSecuelas.setTextFormatter(new TextFormatter  (aux.formato(500, 4)));
        aux.toolTipSuperior(txtEdad, "campo numérico");
        aux.toolTipSuperior(txtSecuelas, "campo opcional");
    }
    
    /**
     * le da formato a los bottones
     */
    private void formatoButtons(){
        bttAgregar.setOnAction((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardaAnteTraumaticos(conex);
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
    private void guardaAnteTraumaticos(Connection conex){
        String id_pTrauma = aux.generaID();
        int edad_pTrauma = Integer.valueOf(txtEdad.getText());
        String duracion_pTrauma = cbbFrecuencia.getValue();
        String trauma_pTrauma = cbbDescripcion.getValue();
        String lado_pTrauma = cbbLado.getValue();
        String secuelas_pTrauma = txtSecuelas.getText();
        String id_paciente = paci.getId_paciente();
        pTraumaticos.agregaPatoTrauma(id_pTrauma, edad_pTrauma, duracion_pTrauma, 
                trauma_pTrauma, lado_pTrauma, secuelas_pTrauma, id_paciente, conex);
    }
    
    /**
     * actualiza la tabla 
     * @param conex 
     */
    private void actualizaTabla(Connection conex){
        colNombreTrauma.setCellValueFactory(new PropertyValueFactory<> ("trauma_pTrauma"));
        colLado.setCellValueFactory(new PropertyValueFactory<> ("lado_pTrauma"));
        colEdad.setCellValueFactory(informacionDeCelda->{
            patoTraumaticos p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getEdad_pTrauma(), p.getDuracion_pTrauma());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colSecuelas.setCellValueFactory(informacionDeCelda->{
            patoTraumaticos p = informacionDeCelda.getValue();
            String regresaFormato;
            if (p.getSecuelas_pTrauma().equals("")) {
                regresaFormato = "NO";
            }else
                regresaFormato = p.getSecuelas_pTrauma();
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        tvTraumatismo.setItems(pTraumaticos.listaAntePaTrauma(conex, paci.getId_paciente()));
        
        
    }
    
    
    /**
     * limpia cuadros 
     */
    private void limpiaCuadros(){
        txtEdad.setText("");
        cbbDescripcion.setValue(null);
        cbbFrecuencia.setValue(null);
        cbbLado.setValue(null);
        txtSecuelas.setText("");
    }
    
    /**
     * borra el padecimiento selecionado
     */
    @FXML
    private void borrar(){
         patoTraumaticos ant = tvTraumatismo.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarAntePatoTrauma(ant.getId_pTrauma(), conex);
                 actualizaTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona un traumatismo", "Selecciona un traumatismo", 
                     "Es necesario seleccionar un traumatismo para ser borrado;");
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
