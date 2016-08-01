/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoMedicos;
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
public class MedicosController implements Initializable {

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
    patoMedicos pMedicos = new patoMedicos();
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML padecimientos previos
    @FXML private RadioButton rbPadecimientos;
    @FXML private RadioButton rbOtroPade;
    @FXML private TextField txtEdad;
    @FXML private ComboBox<String> cbbFrecuencia;
    @FXML private ComboBox<String> cbbDescripcion;
    @FXML private Button bttAgregar;
    //FXMl de tabla
    @FXML private TableView<patoMedicos> tvPadecimientos;
    @FXML private TableColumn<patoMedicos, String> colPadecimiento;
    @FXML private TableColumn<patoMedicos, String> colEdad;
    
    //Lista para combobox
    ObservableList<String> listaVecesAL = FXCollections.observableArrayList();
    ObservableList<String> listaEnfermedades = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean actualizaSiNoMedico = false;
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
        errorMessage += aux.verificaValufield(cbbDescripcion, "Padecimiento");
        errorMessage += aux.verificaValufield(cbbFrecuencia, "Tiempo de padecimiento");
        errorMessage += aux.verificaTexField(txtEdad, "Edad");
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
        
        listaEnfermedades.addAll("Diabetes mellitus.", "Enfermedades del corazón.", "Enfermedades cerebrovasculares.",
                    "Cirrosis y otras enfermedades del hígado", "Enfermedad pulmonar.", 
                    "Hipertensión.", "Enfermedades respiratorias.", "Nefrosis.");
        cbbDescripcion.setItems(listaEnfermedades);
    }
    
    /**
     * formato de radios buttons
     */
    private void formatoRadioButtons(){
        ToggleGroup seleccion = new ToggleGroup();
        seleccion.getToggles().addAll(rbPadecimientos,rbOtroPade);
        rbPadecimientos.fire();
        seleccion.selectedToggleProperty().addListener((onservable,viejo,nuevo)->{
            if (rbOtroPade.isSelected()) {
                cbbDescripcion.setEditable(true);
            }else
                cbbDescripcion.setEditable(false);
        });
    }
    
    /**
     * formato de cirugia
     */
    private void formatoTexfields(){
        txtEdad.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        aux.toolTipSuperior(txtEdad, "campo numérico");
    }
    
    /**
     * le da formato a los bottones
     */
    private void formatoButtons(){
        bttAgregar.setOnAction((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardaAnteMedicos(conex);
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
    private void guardaAnteMedicos(Connection conex){
        String id_pMed = aux.generaID();
        int edad_pMed = Integer.valueOf(txtEdad.getText());
        String duracion_pMed = cbbFrecuencia.getValue();
        String enfermedad_pMed = cbbDescripcion.getValue();
        String id_paciente = paci.getId_paciente();
        pMedicos.agregaPatoMedico(id_pMed, edad_pMed, duracion_pMed, enfermedad_pMed, id_paciente, conex);
    }
    

    /**
     * actualiza la tabla 
     * @param conex 
    */
    private void actualizaTabla(Connection conex){
        colPadecimiento.setCellValueFactory(new PropertyValueFactory<> ("enfermedad_pMed"));
        colEdad.setCellValueFactory(informacionDeCelda->{
            patoMedicos p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getEdad_pMed(), p.getDuracion_pMed());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        tvPadecimientos.setItems(pMedicos.listaAntePatoMedico(conex, paci.getId_paciente()));
        
        
        colPadecimiento.prefWidthProperty().bind(
            tvPadecimientos.widthProperty()
            .subtract(colEdad.widthProperty()) // a border stroke?
            );

        colEdad.prefWidthProperty().bind(
            tvPadecimientos.widthProperty()
            .subtract(colEdad.getPrefWidth())
            .subtract(1) // a border stroke?
            );

    }
    
    /**
     * limpia cuadros
     */
    private void limpiaCuadros(){
        txtEdad.setText("");
        cbbDescripcion.setValue(null);
        cbbFrecuencia.setValue(null);
    }
    

    /**
     * borra el padecimiento selecionado
     */
    @FXML
    private void borrar(){
         patoMedicos ant = tvPadecimientos.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarAntePatoMedico(ant.getId_pMed(), conex);
                 actualizaTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona un padecimiento", "Selecciona un padecimiento", 
                     "Es necesario seleccionar un padecimiento para ser borrado;");
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
