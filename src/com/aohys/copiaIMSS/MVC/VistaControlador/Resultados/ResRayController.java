/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Resultados;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados.imagenrayos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados.imagenrayos.cargaUnaImagen;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ResRayController implements Initializable {

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
        try(Connection conex = dbConn.conectarBD()) {
            actualizaTabla(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    Rayos ray = new Rayos();
    imagenrayos imagenrayos = new imagenrayos();
    imagenrayos rayosVacio = null;
    Rayos rayosSeleccionados = null;
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    //Tabla
    @FXML private TableView<Rayos> tvFechaLabo;
    @FXML private TableColumn<Rayos, String> colFecha;
    private ObservableList<Rayos> listRayos = FXCollections.observableArrayList();
    //FXML resultados
    @FXML private Label lbEstudio;
    @FXML private Label lbIndicaciones;
    @FXML private Label lbDiagnostico;
    @FXML private Button bttAgregar;
    @FXML private AnchorPane anchorPane;
    @FXML private ImageView imageView;
    @FXML private ProgressIndicator pi;
    //Variables de control
    private BooleanProperty activarButtonAgregar = new SimpleBooleanProperty(false);
    private ExecutorService databaseExecutor;
    //Imagenes Botones
    Image impresora = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    Image guardar = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    Image salir = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    Image Nohay = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Imagenes/OncoVera.png");
    
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
    }    
   
    
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAgregar.setOnAction(evento->{
            if (rayosSeleccionados!=null) {
                fileshowcer();
            }else
                aux.alertaError("Selecciona un estudio", "Selecciona un estudio", 
                        "Es necesario seleccionar un estudio para que se pueda agregar "
                                + "los resultados a la base de datos");
            
        });
        
        pi.setVisible(false);
        bttAgregar.setVisible(false);
        
        bttAgregar.disableProperty().bind(activarButtonAgregar);
        bttAgregar.visibleProperty().bind(bttAgregar.disableProperty());
        bttAgregar.setGraphic(new ImageView(guardar));
        
    }
    
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        //errorMessage += aux.verificaTexField(txtPeso, "Peso");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * carga la tabla
     * @param conex 
     */
    private void actualizaTabla(Connection conex){
        colFecha.setCellValueFactory(cellData -> {
            Rayos p = cellData.getValue();
            LocalDate lol = p.getFecha_rayos().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String ageInYear = lol.format(kkk);
            return new ReadOnlyStringWrapper(ageInYear);
        });
        tvFechaLabo.setItems(ray.listaRayosPaciente(conex, paci.getId_paciente()));
        
        databaseExecutor = Executors.newFixedThreadPool(
            1, 
            new DatabaseThreadFactory()
        ); 
        
        tvFechaLabo.getSelectionModel().selectedItemProperty().addListener((observable,viejo,nuevo)->{
            actualizaLabels(nuevo);
            rayosSeleccionados = nuevo;
            fetchNamesFromDatabaseToListView(pi, nuevo.getId_rayos());
        });
        
    }
    
    public void fetchNamesFromDatabaseToListView(
          final ProgressIndicator databaseActivityIndicator, 
           String dato) {
       cargaUnaImagen fetchNamesTask = imagenrayos.new cargaUnaImagen(dato);

        databaseActivityIndicator.visibleProperty().bind(
                fetchNamesTask.runningProperty()
        );
        databaseActivityIndicator.progressProperty().bind(
                fetchNamesTask.progressProperty()
        );

        fetchNamesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
           @Override
           public void handle(WorkerStateEvent t) {
                rayosVacio = fetchNamesTask.getValue();
                if (rayosVacio!=null) {
                    imageView.setImage(rayosVacio.getIma__imaRay());
                    activarButtonAgregar.set(true);
                }else{
                    imageView.setImage(Nohay);
                    activarButtonAgregar.set(false);
                }
           }
        }
        );
        
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(fetchNamesTask.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));

        databaseExecutor.submit(fetchNamesTask);
      }
    
    /**
     * file showcer
     */
    public void fileshowcer(){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("Archivos Imagen", "*.JPG","*.PNG","*.GIF","*.JPEG");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files   (*.jpg)",   "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files   (*.png)",   "*.PNG");
        FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files   (*.gif)",   "*.GIF");
        FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)",  "*.JPEG");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterPNG, extFilterGIF, extFilterJPEG);
        fileChooser.setTitle("Carga Imagen");
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file!=null) {
            databaseExecutor = Executors.newFixedThreadPool(
                1, 
                new DatabaseThreadFactory()
            );
            try {
                guarda(pi, file);
                activarButtonAgregar.set(false);
            } catch (IOException ex) {
                Logger.getLogger(ResRayController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void guarda(final ProgressIndicator databaseActivityIndicator, File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);
        String id_imaRay = aux.generaID();
        Image ima__imaRay = imageView.getImage();
        String id_rayos = rayosSeleccionados.getId_rayos();
        imagenrayos.subeImagen sub = imagenrayos.new subeImagen(id_imaRay, ima__imaRay, id_rayos);

        databaseActivityIndicator.visibleProperty().bind(
                sub.runningProperty()
        );
        databaseActivityIndicator.progressProperty().bind(
                sub.progressProperty()
        );

        sub.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
           @Override
           public void handle(WorkerStateEvent t) {
           }
        }
        );
        
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(sub.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));

        databaseExecutor.submit(sub);
      }
    
    
    static class DatabaseThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);

        @Override public Thread newThread(Runnable runnable) {
          Thread thread = new Thread(runnable, "Database-Connection-" + poolNumber.getAndIncrement() + "-thread");
          thread.setDaemon(true);
          return thread;
        }
    }  
    
    /**
     * carga los datos de los rayos selecionados
     * @param rayInt 
     */
    private void actualizaLabels(Rayos rayInt){
        if (rayInt!=null) {
            lbEstudio.setText(rayInt.getNombre_rayos());
            lbIndicaciones.setText(rayInt.getIndicaciones_rayos());
            lbDiagnostico.setText(rayInt.getDiagnostico_rayos());
        }else
            limpiaLabel();
    }
    
    /**
     * limpia los labels del estudio
     */
    private void limpiaLabel(){
        lbEstudio.setText("");
        lbIndicaciones.setText("");
        lbDiagnostico.setText("");
    }
    
    /**
     * le da formato al image view
     */
    private void formatoImagen(){
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());
        aux.toolTip(imageView, "Doble clik para ampliar la imagen");
        doubleclik();
    }

    /**
     * crea la vision
     */
    private void doubleclik(){
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (imageView.getImage()!=null) {
                        Image image = imageView.getImage();
                        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                            if(mouseEvent.getClickCount() == 2){
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                                imageView.setFitHeight(primaryScreenBounds.getHeight() - 100);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setFullScreen(true);
                                newStage.setTitle("Resultados");
                                Scene scene = new Scene(borderPane,Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            }
                        }
                    }
                }
            });
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        limpiaLabel();
        //formato botton guardar
        formatoBotones();
        //le carga el formato de las imagenes
        formatoImagen();
        
        
    } 
    
   
    
}
