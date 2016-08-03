/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares;

import com.aohys.copiaIMSS.BaseDatos.ListaProcedimientosRayos;
import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico.listaDiagnosticosMasSanoTask;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos.agregaProcedimientoTask;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos.listaRayosPacienteTask;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.aohys.copiaIMSS.Utilidades.Reportes.EstudioPDF;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class RayosXController implements Initializable {
    //Variables de escena
    private PrincipalController cordi;
    Paciente paci;
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param paci 
     */
    public void transmisor(PrincipalController cordi, Paciente paci) {
        this.cordi = cordi;
        this.paci = paci;
        // carga los componentes top
        cargaTop();
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
    AutoCompletionBinding<String> autoComplete;
    ListaProcedimientosRayos listaProcedimientosRayos = new ListaProcedimientosRayos();
    Diagnostico diag = new Diagnostico();
    Rayos rayos = new Rayos();
    
    //Conexion
    Vitro dbConn = new Vitro();
    @FXML private AnchorPane anchorPane;
    //FXML de arriba
    @FXML private Label lbNombre;
    //Parte inferior
    @FXML private ComboBox<String> cbbEstudio;
    @FXML private ComboBox<Diagnostico> cbbDiagnostico;
    @FXML private TextArea txaIndicaciones;
    //FXML buttons
    @FXML private Button bttAgregar;
    @FXML private Button bttSalir;
    //FXML de tabla
    @FXML private TableView<Rayos> tvRayos;
    @FXML private TableColumn<Rayos, String> colEstudio;
    @FXML private TableColumn<Rayos, String> colFecha;
    @FXML private TableColumn<Rayos, String> colIdicaciones;
    @FXML private TableColumn<Rayos, String> colAgregar;
    //Lista para imprimir
    ObservableList<Rayos> listRayos = FXCollections.observableArrayList();
    ObservableList<Diagnostico> listDiagnosticos = FXCollections.observableArrayList();
    
    //Imagenes Botones
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    Image imprimir = new Image("com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
        
    }    
   
    /**
     * formato de imagen
     */
    private void formatoImagen(){
        bttAgregar.setGraphic(new ImageView(guardar));
        bttSalir.setGraphic(new ImageView(aceptar));
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttSalir.setOnAction(evento->{
            cordi.lanzaHistoriaMedica(paci);
        });
        
        bttAgregar.setOnAction(evento->{
            if (continuaSINO()) {
                guardaProcedimiento();
            }
        });
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        txaIndicaciones.setTextFormatter(new TextFormatter (aux.formato(900, 4)));
        aux.toolTipSuperior(txaIndicaciones, "campo opcional");
    }
    
    /**
     * carga formato de los comboboxes
     */
    private void formatoComboboxes(){
        aux.comboboxFiltro(cbbEstudio, listaProcedimientosRayos.cargaProcedimientos());
            cbbDiagnostico.setItems(listDiagnosticos);
        cbbDiagnostico.setCellFactory((comboBox) -> {
            return new ListCell<Diagnostico>() {
                @Override
                protected void updateItem(Diagnostico item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getDiagnostico_diag());
                    }
                }
            };
        });
      
        cbbDiagnostico.setConverter(new StringConverter<Diagnostico>() {
            @Override
            public String toString(Diagnostico item) {
                if (item == null) {
                    return null;
                } else {
                    return item.getDiagnostico_diag();
                }
            }

            @Override
            public Diagnostico fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
    /**
     * actualiza la lista de diagnosticos 
     * @param idPaciente 
     */
    private void actualizaListadiagnosticoTask(String idPaciente){
        listaDiagnosticosMasSanoTask task = diag.new listaDiagnosticosMasSanoTask(idPaciente);
        task.setOnSucceeded(evento->{
            listDiagnosticos.clear();
            listDiagnosticos.addAll(task.getValue());
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbEstudio, "Procedimiento");
        errorMessage += aux.verificaValufield(cbbDiagnostico, "Diagnostico");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * limpia los cuadros despues de guardar
     */
    private void limpiaCuadros(){
        cbbDiagnostico.setValue(null);
        cbbEstudio.setValue(null);
        txaIndicaciones.setText("");
    }
    
    /**
     * actualiza la primera tabla
     * @param conex 
     */
    private void formatoTablaRayos(){
       
        //Dummy data para el boton
        colAgregar.setCellValueFactory(new PropertyValueFactory<> ("id_medico"));
        colEstudio.setCellValueFactory(new PropertyValueFactory<> ("nombre_rayos"));
        colIdicaciones.setCellValueFactory(new PropertyValueFactory<> ("indicaciones_rayos"));
        
        colFecha.setCellValueFactory(col->{
            Rayos ray = col.getValue();
            LocalDate lol = ray.getFecha_rayos().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String ageInYear = lol.format(kkk);
            return new ReadOnlyStringWrapper(ageInYear);
        });
        
        colAgregar.setCellFactory (col -> {
            TableCell<Rayos, String> cell = new TableCell<Rayos, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setGraphic(null);
                        this.setText(null);
                    }else{
                        Button ima = new Button("Imprimir estudio");
                        ima.setOnAction(evento->{
                            Rayos rec = getTableView().getItems().get(getIndex());
                            creaReporteEstudio(rec);
                        });
                        ima.setGraphic(new ImageView(imprimir));
                        this.setText(null);
                        this.setGraphic(ima);  
                    }
                    
                }
            };
            return cell;
        });
        tvRayos.setItems(listRayos);
    }
    
    /**
     * crea el reporte de los rayo x
     * @param rec 
     */
    private void creaReporteEstudio(Rayos rec){
       Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
                EstudioPDF epdf = new EstudioPDF();
                epdf.pasoPrincipal(paci, rec);
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
     * actualiza la lista de rayos que llena la tabla de rayos 
     * @param idPaciente 
     */
    private void actualizaTablaRayos(String idPaciente){
        listaRayosPacienteTask task = rayos.new listaRayosPacienteTask(idPaciente);
        task.setOnSucceeded((evento)->{
            listRayos.clear();
            listRayos.addAll(task.getValue());
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * borra en el fxml el medicamento seleccionado
     */
    @FXML
    private void borrar(){
        Rayos modulito = tvRayos.getSelectionModel().getSelectedItem();
        if (modulito != null) {
            try(Connection conex = dbConn.conectarBD()) {
                modulito.borrarProce(modulito.getId_rayos(), conex);
                listRayos.remove(modulito);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            aux.alertaError("Selecciona un estudio", "Selecciona un estudio", 
                    "Se debe seleccionar un estudio para ser borrado");
        }
    }
    
   
    /**
     * guarda el estudio 
     */
    private void guardaProcedimiento(){
        String id_rayos = aux.generaID();
        Date fecha_rayos = Date.valueOf(LocalDate.now());
        String diagnostico_rayos = cbbDiagnostico.getValue().getDiagnostico_diag();
        String nombre_rayos = cbbEstudio.getValue();
        String indicaciones_rayos = txaIndicaciones.getText();
        String id_usuario = paci.getId_paciente();
        String id_medico = IngresoController.usua.getId_medico();
        agregaProcedimientoTask task = rayos.new agregaProcedimientoTask(id_rayos, fecha_rayos, 
                diagnostico_rayos, nombre_rayos, indicaciones_rayos, id_usuario, id_medico);
        task.setOnSucceeded(evento->{
            Rayos ray = new Rayos(id_rayos, fecha_rayos, diagnostico_rayos, nombre_rayos, 
                indicaciones_rayos, id_usuario, id_medico);
            listRayos.add(ray);
            limpiaCuadros();
            aux.informacionUs("El procedimiento ha sido agregado", 
                        "El procedimiento ha sido agregado", 
                        "El procedimiento ha sido agregado exitosamente de la base de datos");
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Ejecutor de servicio
        ejecutorDeServicio();
        //formato combo
        formatoComboboxes();
        //formato tabla rayos
        formatoTablaRayos();
        //formato botton guardar
        formatoBotones();
        //Le da formato a los imagenes
        formatoImagen();
        //formato de los texbox
        formatoDeText();
        //formato de los comboboxesz
        Platform.runLater(()->{
            actualizaListadiagnosticoTask(paci.getId_paciente());
            actualizaTablaRayos(paci.getId_paciente());
        });
    } 
}
