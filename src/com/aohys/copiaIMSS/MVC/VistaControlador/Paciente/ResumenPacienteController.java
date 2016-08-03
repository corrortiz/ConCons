/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Consulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.antNoPato;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.ant_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.masAnt_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAdicciones;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAlergias;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoMedicos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoQuirugicos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTransfucion;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTraumaticos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.aohys.copiaIMSS.Utilidades.Reportes.EstudioPDF;
import com.aohys.copiaIMSS.Utilidades.Reportes.HistorialPDF;
import com.aohys.copiaIMSS.Utilidades.Reportes.NotaAtencionPDF;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ResumenPacienteController implements Initializable {

    //Variables de escena
    private PrincipalController cordi;
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
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param paci 
     */
    public void transmisor(PrincipalController cordi, Paciente paci) {
        Platform.runLater(()->{
            this.cordi = cordi;
            this.paci = paci;
            // carga los componentes top
            cargaTop();
            try(Connection conex = dbConn.conectarBD()) {
                iniciaTablaConsulta(conex);
                formatoCirculos(conex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ejecutorDeServicio();
        });
    }

    //Variables a que utiliza el controlador
    Paciente paci;
    Auxiliar aux = new Auxiliar();
    Consulta consul = new Consulta();
    Usuario usa = new Usuario();
    Image img = 
            new Image("com/aohys/copiaIMSS/Utilidades/Imagenes/cuadroResumen.png");
    Image impresora = new Image("com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    Image alerta = new Image("com/aohys/copiaIMSS/Utilidades/Logos/alarm.png");
    Image bien = new Image("com/aohys/copiaIMSS/Utilidades/Logos/favorite.png");
    //Variables de circulos
    antNoPato antNopato = new antNoPato();
    ant_Heredo_Familiar antHeredo_Familiar = new ant_Heredo_Familiar();
    patoAdicciones pAdicciones = new patoAdicciones();
    patoAlergias pAlergias = new patoAlergias();
    patoMedicos pMedicos = new patoMedicos();
    patoQuirugicos pQuirugicos = new patoQuirugicos();
    patoTransfucion pTransfucion = new patoTransfucion();
    patoTraumaticos pTraumaticos = new patoTraumaticos();
    masAnt_Heredo_Familiar masAnteHere = new masAnt_Heredo_Familiar();
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    @FXML private AnchorPane anchorPane;
    //FXML enmedio
    @FXML private ImageView imageView;
    //Tabla consultas
    @FXML private TableView<Consulta> tvConsulta;
    @FXML private TableColumn<Consulta, String> colFechaConsulta;
    @FXML private TableColumn<Consulta, String> colDiagnostico;
    @FXML private TableColumn<Consulta, String> colAgregar;
    @FXML private ComboBox<Usuario> cbbMedicos;
    //Hiperlink
    @FXML private Hyperlink hlHeredo;
    @FXML private Hyperlink hlNoPatologicos;
    @FXML private Hyperlink hlQuirugi;
    @FXML private Hyperlink hlAlergias;
    @FXML private Hyperlink hlPadMedicos;
    @FXML private Hyperlink hlTransfuncionales;
    @FXML private Hyperlink hlTraumaticos;
    @FXML private Hyperlink hlAdiciones;
    @FXML private Hyperlink hlImprimir;
    //Circulos
    @FXML private HBox hBox;
    
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
        
        imageView.setImage(img);
      
    }    
    
    /**
     * Inicia la tabla de consulta
     * @param conex
     */
    public void iniciaTablaConsulta(Connection conex){
        colDiagnostico.setCellValueFactory(cellData -> {
            Consulta p = cellData.getValue();
            Diagnostico diagnostico = new Diagnostico();
            try(Connection conexion = dbConn.conectarBD()) {
                diagnostico = diagnostico.cargaSoloUno(p.getId_cons(), conexion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String ageInYear = diagnostico.getDiagnostico_diag();
            return new ReadOnlyStringWrapper(ageInYear);
        });
        colFechaConsulta.setCellValueFactory(cellData -> {
            Consulta p = cellData.getValue();
            LocalDate lol = p.getFecha_cons().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String ageInYear = lol.format(kkk);
            return new ReadOnlyStringWrapper(ageInYear);
        });
        
        colAgregar.setCellValueFactory(new PropertyValueFactory<> ("motivo_cons"));
        colAgregar.setCellFactory (col -> {
            TableCell<Consulta, String> cell = new TableCell<Consulta, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setGraphic(null);
                        this.setText(null);
                    }else{
                        Button ima = new Button("Imprimir Nota");
                        ima.setOnAction(evento->{
                            Consulta rec = getTableView().getItems().get(getIndex());
                            creaNotaMedicaPDF(rec);
                        });
                        ima.setGraphic(new ImageView(impresora));
                        this.setText(null);
                        this.setGraphic(ima);  
                    }
                    
                }
            };
            return cell;
        });
        
        tvConsulta.setItems(consul.listaConsulPaciente(conex, paci.getId_paciente()));
        cbbMedicos.setItems(usa.cargaListaMedResumen(conex));
        combo();
    }
    
    /**
     * crea nota medica
     * @param consul 
     */
    private void creaNotaMedicaPDF(Consulta consul){
       Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
                NotaAtencionPDF ant = new NotaAtencionPDF();
                ant.pasoPrincipal(consul);
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
     * Cambia la tabla dependiendo del medico
     * @param conex
     * @param idMed 
     */
    public void iniciaTablaConsultaMedico(Connection conex, String idMed){
        colDiagnostico.setCellValueFactory(cellData -> {
            Consulta p = cellData.getValue();
            Diagnostico diagnostico = new Diagnostico();
            try(Connection conexion = dbConn.conectarBD()) {
                diagnostico = diagnostico.cargaSoloUno(p.getId_cons(), conexion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String ageInYear = diagnostico.getDiagnostico_diag();
            return new ReadOnlyStringWrapper(ageInYear);
        });
        colFechaConsulta.setCellValueFactory(cellData -> {
            Consulta p = cellData.getValue();
            LocalDate lol = p.getFecha_cons().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String ageInYear = lol.format(kkk);
            return new ReadOnlyStringWrapper(ageInYear);
        });
        
        
        colAgregar.setCellValueFactory(new PropertyValueFactory<> ("motivo_cons"));
        colAgregar.setCellFactory (col -> {
            TableCell<Consulta, String> cell = new TableCell<Consulta, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setGraphic(null);
                        this.setText(null);
                    }else{
                        Button ima = new Button("Imprimir Nota");
                        ima.setOnAction(evento->{
                            Consulta rec = getTableView().getItems().get(getIndex());
                            creaNotaMedicaPDF(rec);
                        });
                        ima.setGraphic(new ImageView(impresora));
                        this.setText(null);
                        this.setGraphic(ima);  
                    }
                    
                }
            };
            return cell;
        });
        
        tvConsulta.setItems(consul.listaConsulPacienteConMedico(conex, paci.getId_paciente(),idMed));
    }
    
    private void hiperlinks(){
        hlHeredo.setOnAction(evento->{
            cordi.lanzaAntHeredo(paci);
        });
        
        hlNoPatologicos.setOnAction(evento->{
            cordi.lanzaAntNoPatologico();
        });
        
        tab(hlPadMedicos, 1);
        tab(hlQuirugi, 2);
        tab(hlTraumaticos, 3);
        tab(hlTransfuncionales, 4);
        tab(hlAlergias, 5);
        tab(hlAdiciones, 6);
        
        hlImprimir.setGraphic(new ImageView(impresora));
        
        hlImprimir.setOnAction(evento->{
            creaHisotiralPDF();
        });
    }
    /**
     * crea el pdf del historial
     */
    private void creaHisotiralPDF(){
       Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
                HistorialPDF histo = new HistorialPDF();
                histo.pasoPrincipal();
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
     * formato de hiperlincks
     * @param hyperlink
     * @param donde 
     */
    private void tab(Hyperlink hyperlink, int donde){
        hyperlink.setOnAction(evento->{
            cordi.lanzaAntPatologico(donde);
        });
    }
    
    /**
     * formato de los circulos
     * @param conex 
     */
    private void formatoCirculos(Connection conex){
        circulos(hlQuirugi, pQuirugicos.listaAntePatoQuir(conex, paci.getId_paciente()));
        circulos(hlAlergias, pAlergias.listaAntePaAlergias(conex, paci.getId_paciente()));
        circulos(hlPadMedicos, pMedicos.listaAntePatoMedico(conex, paci.getId_paciente()));
        circulosObjetos(hlNoPatologicos, antNopato.cargaSoloUno(paci.getId_paciente(), conex));
        circulosObjetos(hlHeredo, masAnteHere.cargaSoloUno(paci.getId_paciente(), conex));
        circulos(hlTransfuncionales, pTransfucion.listaAntePaTransfucion(conex, paci.getId_paciente()));
        circulos(hlTraumaticos, pTraumaticos.listaAntePaTrauma(conex, paci.getId_paciente()));
        circulos(hlAdiciones, pAdicciones.listaAntePaAdicciones(conex, paci.getId_paciente()));
    }
    
    /**
     * formato de los hiperlincs
     * @param circle
     * @param object 
     */
    private void circulos(Hyperlink hyperlink, ObservableList object){
        if (object.isEmpty()) {
            hyperlink.setGraphic(new ImageView(alerta));
        }else
            hyperlink.setGraphic(new ImageView(bien));
    }
    /**
     * formato de circulos objetos
     * @param circle
     * @param object 
     */
    private void circulosObjetos(Hyperlink hyperlink, Object object){
        if (object == null) {
            hyperlink.setGraphic(new ImageView(alerta));
        }else
            hyperlink.setGraphic(new ImageView(bien));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbbMedicos.valueProperty().addListener((e,v,n)->{
            if (n!=null) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (n.getNombre_medico().equals("Mostrar todas las consultas")) {
                        iniciaTablaConsulta(conex);
                    }else
                        iniciaTablaConsultaMedico(conex, n.getId_medico());
                }catch(SQLException ee) {
                    ee.printStackTrace();
                }
            }
        });
        
       colDiagnostico.prefWidthProperty().bind(
                    tvConsulta.widthProperty()
                    .subtract(colFechaConsulta.widthProperty())
                    .subtract(colAgregar.widthProperty())  // a border stroke?
                    );
       
       //formato de hiperLinks
       hiperlinks();
       //escucha doble click
       escuchaDobleclik();
        
    }    
    
    /**
     * escucha el doble clik de la tabla
     */
    public void escuchaDobleclik(){
       tvConsulta.setOnMouseClicked((evento)->{
           if (evento.getClickCount() == 2 && tvConsulta.getSelectionModel().getSelectedItem()!= null) {
                Task<Void> task = new Task<Void>() {
                  @Override
                  protected Void call() throws Exception {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(new Runnable(){
                        public void run(){
                            creaNotaMedicaPDF(tvConsulta.getSelectionModel().getSelectedItem());
                        }
                    });
                    return null;
                  }
                };
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                  @Override
                  public void handle(WorkerStateEvent event) {
                  }
                });
                Thread tt = new Thread(task);
                tt.setDaemon(false);
                tt.start();
           }
       });
       
    }
    
    /**
     * Combo para lo que quiero
     */
    public void combo(){
        // Define rendering of the list of values in ComboBox drop down. 
        cbbMedicos.setCellFactory((comboBox) -> {
            return new ListCell<Usuario>() {
                @Override
                protected void updateItem(Usuario item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getNombre_medico()+ " " + item.getApellido_medico()+" "+item.getApMaterno_medico());
                    }
                }
            };
        });

        // Define rendering of selected value shown in ComboBox.
        cbbMedicos.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario item) {
                if (item == null) {
                    return null;
                } else {
                    return item.getNombre_medico()+ " " + item.getApellido_medico()+" "+item.getApMaterno_medico();
                }
            }

            @Override
            public Usuario fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
}
