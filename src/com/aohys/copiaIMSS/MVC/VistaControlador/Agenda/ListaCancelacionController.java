/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Agenda;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ListaCancelacionController implements Initializable {

    //Variables de escena
    private PrincipalController cordi;
    private AgendaCitasController palnel;

    /**
     * Inicia la esecena 
     * @param cordi 
     * @param palnel 
     */
    public void transmisor(PrincipalController cordi, AgendaCitasController palnel) {
        this.cordi = cordi;
        this.palnel = palnel;
    }
    
    //Variables a utilizar
    Auxiliar aux = new Auxiliar();
    
    //Conexion base de datos
    Vitro dbConn = new Vitro();
    
    //Primer Panel
    //Variables a que utiliza el controlador
    Paciente pacc = new Paciente();
    
    //Variables de algo
    Boolean udateNote = false;
    
    //ObserblaeList
    ObservableList<Paciente> listaTa = FXCollections.observableArrayList();
    Image imag = new Image("com/aohys/copiaIMSS/Utilidades/Logos/busqueda.png");
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    //FXML tabla
    @FXML private TableView<Paciente> tbPaciente;
    @FXML private TableColumn<Paciente, String>  colCURP;
    @FXML private TableColumn<Paciente, String>  colNombre;
    //Filtro
    @FXML private TextField txtNombre;
    @FXML private TextField txtCURP;
  
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    //FXML private
    @FXML private Button bttBuscar;
    @FXML private Button bttNuevaCane;
    
    /**
     * Inica la tabla de la escena
     * @param resultadosBus
     */
    public void IniciaTabla(ObservableList<Paciente> resultadosBus){
        colCURP.setCellValueFactory       (new PropertyValueFactory<>  ("curp_paciente"));
        colNombre.setCellValueFactory(cellData -> {
            Paciente p = cellData.getValue();
            String regresaColumna = String.format("%s %s %s",
                    p.getNombre_paciente(), p.getApellido_paciente(), p.getApMaterno_paciente());
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        tbPaciente.setItems(resultadosBus);
        
        colNombre.prefWidthProperty().bind(
                    tbPaciente.widthProperty()
                    .subtract(colCURP.widthProperty())
                    .subtract(2)  // a border stroke?
                 );
    }
    
    
    /**
     * Cambia datos
     */
    public void cambiaDatos(){
        tbPaciente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            cargaDatos(newValue);
        });
    }
    
    /**
     * le da funcionalidad de buscar al boton paciente 
     */
    public void buscar(){
        try(Connection conex = dbConn.conectarBD()) {
            if(txtNombre.getText().equals("")
                    && txtCURP.getText().equals("")){
                aux.alertaError("Error de búsqueda", "Error de búsqueda", 
                        "Se debe ingresar ya sea un nombre o un CURP a buscar");
            }else{
               if(txtNombre.getText().trim().isEmpty()){
                    IniciaTabla(pacc.buscaCURP(conex, txtCURP.getText()));
                }else
                    IniciaTabla(pacc.buscaNombre(conex, txtNombre.getText())); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Carga los datos de la escena 
     * @param paci
     */
    public void cargaDatos(Paciente paci){
        if (paci != null) {
            String nombre = String.format("%s %s %s", 
                    paci.getNombre_paciente(), paci.getApellido_paciente(), paci.getApMaterno_paciente());
            lbNombre.setText(nombre);
            lbEdad.setText(aux.edadConMes(paci.getFechaNacimiento_paciente()));
            lbCURP.setText(paci.getCurp_paciente());
            lbSexo.setText(paci.getSexo_paciente());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    public void initializePrimerTab() {
        //Cambia datos
        cambiaDatos();
        //funcionalidad del botton buscar
        buscarButtonfun();
        //label por al inicio
        limpiart();
    }    
    
    /**
     * limpia datos inicialmente
     */
    public void limpiart(){
        lbNombre.setText("");
        lbEdad.setText("");
        lbCURP.setText("");
        lbSexo.setText("");
    }
    
    /**
     * le da funcionalidad al boton buscar y formato
     */
    public void buscarButtonfun(){
        bttBuscar.setGraphic(new ImageView(imag));
        bttNuevaCane.setGraphic(new ImageView(agregar));
        bttBuscar.setContentDisplay(ContentDisplay.LEFT);
        bttBuscar.setOnAction(evneto->{
            buscar();
        });
        bttNuevaCane.setOnAction((evento)->{
            Paciente paci = tbPaciente.getSelectionModel().getSelectedItem();
            if (tbPaciente.getSelectionModel().getSelectedItem()!= null) {
                palnel.lanzaCancelaCitas(paci); 
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Para poder cancelar una nueva cita es necesario seleccionar un paciente");
        });
    }

    /**
     * lanza lista de citas para el paciente para cancelar
     */
    @FXML
    public void lanzaCitaCancelar(){
        Paciente paci = tbPaciente.getSelectionModel().getSelectedItem();
        if (paci!=null) {
           palnel.lanzaCancelaCitas(paci); 
        }else{
             aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                    "Se debe seleccionar un paciente cancelar su cita");
        }
    }
    
       /**
     * le da formato a los cuadros de texto
     */
    private void formatoCuadrosBusqueda(){
        presionaEnterAccion(txtNombre);
        presionaEnterAccion(txtCURP);
    }
    /**
     * le da formato a key enter para buscar 
     * @param textField 
     */
    private void presionaEnterAccion(TextField textField){
        textField.setOnKeyPressed(evento->{
            if (evento.getCode() == KeyCode.ENTER) {
                buscar();
            }
        });
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializePrimerTab();
        //Formato de texfield de busqueda
        formatoCuadrosBusqueda();
    }    
    
}
