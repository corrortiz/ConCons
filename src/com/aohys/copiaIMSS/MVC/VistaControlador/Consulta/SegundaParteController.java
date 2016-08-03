/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Consulta;

import com.aohys.copiaIMSS.BaseDatos.ListaPadecimientos;
import com.aohys.copiaIMSS.BaseDatos.ListaProcedimientos;
import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Consulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Tratamiento;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.aohys.copiaIMSS.Utilidades.Reportes.HistorialPDF;
import com.aohys.copiaIMSS.Utilidades.Reportes.NotaAtencionPDF;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class SegundaParteController implements Initializable {
    //Variables de escena
    private PrincipalController cordi;
    Paciente paci;
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param paci 
     * @param motivo 
     * @param exploracion 
     */
    public void transmisor(PrincipalController cordi, Paciente paci, String motivo, String exploracion) {
        this.cordi = cordi;
        this.paci = paci;
        this.motivo = motivo;
        this.exploracion = exploracion;
        id_cons = aux.generaID();
        // carga los componentes top
        cargaTop();
        
        ejecutorDeServicio();
    }
    //private ExecutorService dbExeccutor;
    private ExecutorService dbExeccutor;
    /**
     * metodo para pedir un hilo antes de una llamada a la bd
     */
    private void ejecutorDeServicio(){
        dbExeccutor = Executors.newFixedThreadPool(
            1, 
            new databaseThreadFactory()
        ); 
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    ListaPadecimientos listaCie10 = new ListaPadecimientos();
    ListaProcedimientos listaCie9 = new ListaProcedimientos();
    Diagnostico diagnostico = new Diagnostico();
    Tratamiento tratamiento = new Tratamiento();
    Consulta consulta = new Consulta();
    
    //Conexion
    Vitro dbConn = new Vitro();
    @FXML private AnchorPane anchorPane;
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    //FXML
    @FXML private Button bttAceptar;
    //Parte de arriba
    @FXML private RadioButton rbPrimera;
    @FXML private RadioButton rbSubsecuente;
    @FXML private RadioButton rbCIE10;
    @FXML private RadioButton rbFrecuentes;
    @FXML private Button bttAgregarDiagnostico;
    @FXML private ComboBox<String> cbbDiagnostico;
    @FXML private TextField txtComplemento;
    //Tabla diagnostico
    @FXML private TableView<Diagnostico> tvDiagnostioco;
    @FXML private TableColumn<Diagnostico, String> colDiagnostico;
    @FXML private TableColumn<Diagnostico, String> colComplentoDX;
    //Parte de abajo
    @FXML private ComboBox<String> cbbTratamiento;
    @FXML private Button bttAgregaTratamiento;
    @FXML private TextArea txaHigieDiete;
    //Tabla tratamiento
    @FXML private TableView<Tratamiento> tvTratamiento;
    @FXML private TableColumn<Tratamiento, String> colTratamiento;
    
    //Formato combobox
    ObservableList<String> listaEnfermedades = FXCollections.observableArrayList();
    AutoCompletionBinding<String> autoComplete;
    AutoCompletionBinding<String> autoCompleteTratamieto;
    
    //Actualizar si o no
    private String motivo;
    private String exploracion;
    private String id_cons;
    private boolean primeravez = false;
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
        lbEdad.setText(aux.edadConMes(paci.getFechaNacimiento_paciente()));
    }    
   
    /**
     * formato de imagen
     */
    private void formatoImagen(){
    }
    
    /**
     * crea nota medica del paciente
     * @param conex 
     */
    private void creaNotaMedicaPDF(){
       Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
                try(Connection conex = dbConn.conectarBD()){
                    NotaAtencionPDF ant = new NotaAtencionPDF();
                    ant.pasoPrincipal(consulta.cargaSoloUno(id_cons, conex)); 
                }
                return null;
           }
       };
        //Maouse en modo esperar
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        
        dbExeccutor.submit(task);
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAceptar.setOnAction(eventon->{
            
            try(Connection conex = dbConn.conectarBD()) {
                if(diagnostico.revisaDiagConsulta(id_cons, conex)){
                    guardaConsulta(conex);
                    cordi.lanzaHistoriaMedica(paci);
                    creaNotaMedicaPDF();
                }else{
                    aux.alertaError("Agrega un diagnostico", "Agrega un diagnostico", 
                            "Para poder guardar la nota medica es necesario por lo menos agregar un diagnostico ");
                    cbbDiagnostico.requestFocus();
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        bttAgregarDiagnostico.setOnAction(evento->{
            if (continuaSINODiagnostico()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardarDiagnostico(conex);
                    actualizaTablaDiagnosticos(conex);
                    limpiaDiagnostico();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        bttAgregaTratamiento.setOnAction(evento->{
            if (continuaSINOTratamiento()) { 
                try(Connection conex = dbConn.conectarBD()) {
                    guardaTratamiento(conex);
                    acutalizaTablaTratamientos(conex);
                    cbbTratamiento.setValue(null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        });
        
        bttAceptar.setGraphic(new ImageView(aceptar));
        bttAgregaTratamiento.setGraphic(new ImageView(agregar));
        bttAgregarDiagnostico.setGraphic(new ImageView(agregar));
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        txtComplemento.setTextFormatter(new TextFormatter(aux.formato(500, 4)));
        txaHigieDiete.setTextFormatter(new TextFormatter(aux.formato(500, 4)));
        aux.toolTipSuperior(txtComplemento, "campo opcional");
        aux.toolTipSuperior(txaHigieDiete, "campo opcional");
        
        txaHigieDiete.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.TAB)) {
                    Node node = (Node) event.getSource();
                    if (node instanceof TextField) {
                        TextFieldSkin skin = (TextFieldSkin) ((TextField)node).getSkin();
                        if (event.isShiftDown()) {
                            skin.getBehavior().traversePrevious();
                        }
                        else {
                            skin.getBehavior().traverseNext();
                        }               
                    }
                    else if (node instanceof TextArea) {
                        TextAreaSkin skin = (TextAreaSkin) ((TextArea)node).getSkin();
                        if (event.isShiftDown()) {
                            skin.getBehavior().traversePrevious();
                        }
                        else {
                            skin.getBehavior().traverseNext();
                        }
                    }
                    event.consume();
                }
            }
        });
        
    }
    
    /**
     * formato de radio buttons
     */
    private void radioButtons(){
        ToggleGroup primera = new ToggleGroup();
        primera.getToggles().addAll(rbPrimera, rbSubsecuente);
        rbPrimera.fire();
        primera.selectedToggleProperty().addListener((observable,viejo,nuevo)->{
            if (rbPrimera.isSelected()) {
                primeravez = true;
            }else
                primeravez = false;
        });
        
        ToggleGroup cie10 = new ToggleGroup();
        cie10.getToggles().addAll(rbCIE10, rbFrecuentes);
        rbFrecuentes.fire();
        cie10.selectedToggleProperty().addListener((observable,viejo,nuevo)->{
            if (rbCIE10.isSelected()) {
                formatoComboBoxCIE10();
            }else
                try(Connection conex = dbConn.conectarBD()) {
                    formatoComboBoxFrecuentes(conex);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
        });
        
    }
  
    /**
     * formato de combobox
     */
    private void formatoComboBoxFrecuentes(Connection conex){
        cbbDiagnostico.getItems().clear();
        cbbDiagnostico.setEditable(false);
        listaEnfermedades.addAll(diagnostico.listaDxFrecuentes(conex, IngresoController.usua.getId_medico()));
        cbbDiagnostico.setItems(listaEnfermedades);
    }
    
    /**
     * formato de combobox
     */
    private void formatoComboBoxCIE10(){
        cbbDiagnostico.getItems().clear();
        cbbDiagnostico.setItems(listaCie10.cargaTabla());
        cbbDiagnostico.setEditable(true);
        autoComplete = TextFields.bindAutoCompletion(cbbDiagnostico.getEditor(), cbbDiagnostico.getItems());
        autoComplete.setPrefWidth(1200);
    }
    
    /**
     * formato de combo tratamiento
     */
    private void formatoComboTratamiento(){
        cbbTratamiento.setEditable(true);
        cbbTratamiento.setItems(listaCie9.cargaProcedimientos());
        autoCompleteTratamieto = TextFields.bindAutoCompletion(cbbTratamiento.getEditor(), cbbTratamiento.getItems());
        autoCompleteTratamieto.setPrefWidth(1200);
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINODiagnostico(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbDiagnostico, "Diagnóstico");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * verifica si estan todos los campos llenos
     * @return 
     */
    private boolean continuaSINOTratamiento(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbTratamiento, "Tratamiento");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * acutaliza la tabla de diagnosticos
     * @param conex 
     */
    private void actualizaTablaDiagnosticos(Connection conex){
        colDiagnostico.setCellValueFactory(new PropertyValueFactory ("diagnostico_diag"));
        colComplentoDX.setCellValueFactory(new PropertyValueFactory ("complemento_diag"));
        tvDiagnostioco.setItems(diagnostico.listaDiagnosticosConsulta(conex, id_cons));
        
        
    }
    
    /**
     * actualiza la tabla de tratamientos
     * @param conex 
     */
    private void acutalizaTablaTratamientos(Connection conex){
        colTratamiento.setCellValueFactory(new PropertyValueFactory ("nombre_proce"));
        tvTratamiento.setItems(tratamiento.listaTratamientoConsulta(conex, id_cons));
    }
    
    /**
     * guarda el diagnostico 
     * @param conex 
     */
    private void guardarDiagnostico(Connection conex){
        String id_diag = aux.generaID();
        String diagnostico_diag = cbbDiagnostico.getValue();
        String complemento_diag = txtComplemento.getText();
        String id_cons = this.id_cons;
        
        diagnostico.agregaDiagnostico(id_diag, diagnostico_diag, complemento_diag, id_cons, conex);
    }
    
    /**
     * guarda tratamiento en la base de datos
     * @param conex 
     */
    private void guardaTratamiento(Connection conex){
        String id_proce = aux.generaID();
        String nombre_proce = cbbTratamiento.getValue();
        String id_cons = this.id_cons;
        
        tratamiento.agregaTratamiento(id_proce, nombre_proce, id_cons, conex);
    }
    
    /**
     * borra el diagnostico por medio de contex menu
     */
    @FXML
    private void borrarDiagnostico(){
         Diagnostico ant = tvDiagnostioco.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarDiagnosticos(ant.getId_diag(), conex);
                 actualizaTablaDiagnosticos(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona un diagnóstico", "Selecciona un diagnóstico", 
                     "Es necesario seleccionar un diagnóstico para ser borrado;");
    }
    
    /**
     * borra tratamiento por medio contex menu
     */
    @FXML
    private void borrarTratamientos(){
         Tratamiento ant = tvTratamiento.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarTratamiento(ant.getId_proce(), conex);
                 actualizaTablaDiagnosticos(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona un tratamiento", "Selecciona un tratamiento", 
                     "Es necesario seleccionar un tratamiento para ser borrado;");
    }
    
    /**
     * limpia diagnostico 
     */
    private void limpiaDiagnostico(){
        cbbDiagnostico.setValue(null);
        txtComplemento.setText("");
    }
    
    /**
     * guarda la consulta
     * @param conex 
     */
    private void guardaConsulta(Connection conex){
        String id_cons = this.id_cons;
        String motivo_cons = motivo;
        String exploracion_cons = exploracion;
        Date fecha_cons = Date.valueOf(LocalDate.now());
        Time hora_cons = Time.valueOf(LocalTime.now());
        boolean primeraVez_cons = primeravez;
        String higiDiete_cons = txaHigieDiete.getText();
        String id_paciente = paci.getId_paciente();
        String id_medico = IngresoController.usua.getId_medico();
        
        consulta.agregaConsulta(id_cons, motivo_cons, exploracion_cons, fecha_cons, hora_cons, 
                primeraVez_cons, higiDiete_cons, id_paciente, id_medico, conex);
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formato botton guardar
        formatoBotones();
        //Le da formato a los imagenes
        formatoImagen();
        //formato de los texbox
        formatoDeText();
        //formato de combobox
        try(Connection conex = dbConn.conectarBD()) {
            formatoComboBoxFrecuentes(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formatoComboTratamiento();
        //formato de radio buttons
        radioButtons();
        
        colComplentoDX.setPrefWidth(300);
        colComplentoDX.prefWidthProperty().bind(
           tvDiagnostioco.widthProperty()
           .subtract(colComplentoDX.getWidth())
           .subtract(3)
        );
        
        colDiagnostico.prefWidthProperty().bind(
            tvDiagnostioco.widthProperty()
            .subtract(colComplentoDX.widthProperty())
            .subtract(1)
        );
        
        
    } 
    
}
