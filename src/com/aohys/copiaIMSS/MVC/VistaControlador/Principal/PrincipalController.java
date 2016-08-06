/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Principal;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Agenda.AgendaCitasController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Agenda.DiasFestivosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares.LaboratorialesController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares.RayosXController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares.RecetaController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Consulta.NotaMedicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Consulta.SegundaParteController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.AntecedentesMédicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.HeredoFamiliaresController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.PersonalesNoPatologicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.ListaPacienteController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.ResumenPacienteController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.SomatometríaController;
import static com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController.usua;
import com.aohys.copiaIMSS.MVC.VistaControlador.Resultados.ResLabController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Resultados.ResRayController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Usuarios.ListaUsuariosController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.aohys.copiaIMSS.Utilidades.Reportes.HistorialPDF;
import com.aohys.rehabSys.Utilidades.ClasesAuxiliares.Efectos;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class PrincipalController implements Initializable {

    //Variables de escena
    private Coordinador cordi;
    private Stage stage;
    public static Paciente pacienteAUsar = new Paciente();
    //Variable auxiliar
    Auxiliar aux = new Auxiliar();
    
    //Variables pupOVer
    PopOver popOver = new PopOver();
    Button bttPopPaciente = new Button("Lista de Pacientes ");
    Button bttPopUsuario = new Button("Lista de Usuarios ");
    Button bttFestivos = new Button("Lista de días festivos ");
  
    //Base de datos
    Hikari dbConn = new Hikari();
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
     * @param stage 
     */
    public void pasoPrincipal(Coordinador cordi, Stage stage) {
        this.cordi = cordi;
        this.stage = stage;
        binding();
        ejecutorDeServicio();
    }
    
    //Variables de transicion 
    private static final double TRANSITION_TIMER = 200;   
    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private StringProperty nombreUS = new SimpleStringProperty();
    public static StringProperty nombrePaciente = new SimpleStringProperty("");
    
    //Variables varias
    Image img = 
            new Image("com/aohys/copiaIMSS/Utilidades/Imagenes/cuadrado.jpg");
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    Image imprimir = new Image("com/aohys/copiaIMSS/Utilidades/Logos/printer.png");

    
    //FXML
    @FXML private ImageView imageView;
    
    //BotnesMenu
    @FXML private SplitMenuButton splAgendaCitas;
    Menu mregCit = new Menu();
        MenuItem mIegCit = new MenuItem();
    //Segundo Menu
    @FXML private SplitMenuButton splAtencionIntegral;
    Menu mHistoriaClinica = new Menu();
        MenuItem mIAntecedentes = new MenuItem();
        MenuItem mIHeredo = new MenuItem();
        MenuItem mIPersoNoPato = new MenuItem();
        MenuItem mIPersoPato = new MenuItem();
    Menu mSomatometria = new Menu();
        MenuItem mISoma = new MenuItem();
    Menu mAtencionMedica = new Menu();
        MenuItem mINotaMedica = new MenuItem();
    //Tercer Menu
    @FXML private SplitMenuButton splAuxiliares;
    Menu mSolicitudLabora = new Menu();
        MenuItem mISoliLab = new MenuItem();
    Menu mReceta = new Menu();
        MenuItem mIReceta = new MenuItem();
    Menu mRayos = new Menu();
        MenuItem mIRayos = new MenuItem();
    //Cuarto menu
    @FXML private SplitMenuButton splResultados;
    Menu mResLabora = new Menu();
        MenuItem mIResLab = new MenuItem();
    Menu mResRay = new Menu();
        MenuItem mIResRay = new MenuItem();
    
    //Cabezera
    @FXML private Label lbFecha;
    @FXML private Label lbHora;
    @FXML private Label lbNombreUsuario;
    @FXML private Label lbNombrePaciente;
    
    //Botones Laterales
    @FXML private Button bttSalir;
    @FXML private Button bttPortada;
    @FXML private Button bttAndministracion;
    @FXML private Button bttResumen;

    //FXML de stacks
    @FXML private StackPane stackPane;
    
    /**
    * Initializes the controller class.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageView.setImage(img);
        stackPane.setAlignment(Pos.CENTER);
        
        pacienteAUsar = null;
        
        //CargaPopOver
        pupOver();
        
        //Carga reloct
        bindToTime();
        fecha();
        
        //LanzaLogin
        lanzaCitas(2);
        
        //Cargamenu citas
        menusAgendaCitas();
        //carga menu atencion integral
        menuAntencionIntegral();
        //carga menu auxiliares
        menusAuxiliares();
        //menu resultados
        menusResultados();
        //Formato de los bottos
        buttonFormats();

        bttSalir.setOnAction((evento)->{
            System.exit(0);
        });
        
        bttPortada.setOnAction(evento->{
            cordi.entrarUsuario();
            stage.hide();
        });
        
        bttAndministracion.setOnAction(evento->{
            popOver.show(bttAndministracion);
        });
        
        formatoIngresoUsuario();
    }    
    /**
     * formato de tipo de pacientes
     */
    private void formatoIngresoUsuario(){
        switch (IngresoController.usua.getTipo_medico()){
            case "Asistente":
                desactivaEscondeNodo(bttAndministracion);
                desactivaEscondeNodo(bttResumen);
                desactivaEscondeNodo(splAuxiliares);
                desactivaEscondeNodo(splAtencionIntegral);
                desactivaEscondeNodo(splResultados);
               break;
            case "Laboratorio":
                desactivaEscondeNodo(bttAndministracion);
                desactivaEscondeNodo(bttResumen);
                desactivaEscondeNodo(splAuxiliares);
                desactivaEscondeNodo(splAtencionIntegral);
                desactivaEscondeNodo(splAgendaCitas);
                lanzaListaPacientes();
                break;
        }
    }
    
    /**
     * clase que activa y esconde los botones que no se usa
     * @param node 
     */
    private void desactivaEscondeNodo(Node node){
        node.setDisable(true);
        node.opacityProperty().bind(new Efectos().bindgAModo(node));
        System.err.println(node.getId());
    }
    
    /**
     * recibe paciente para usuar
     * @param pacienteRecibido 
     */
    public static void recibePaciente(Paciente pacienteRecibido){
        if (pacienteRecibido!=null) {
            nombrePaciente.setValue(String.format("%s %s %s", 
                pacienteRecibido.getNombre_paciente(), pacienteRecibido.getApellido_paciente(), 
                pacienteRecibido.getApMaterno_paciente()));
        }
        pacienteAUsar = pacienteRecibido;
    }    
   
    /**
     * bindela el label
     */
    public void binding(){
        String nombre = usua.getNombre_medico()+" "+usua.getApellido_medico()+" "+usua.getApMaterno_medico();
        nombreUS.setValue(nombre);
        lbNombreUsuario.textProperty().bind(nombreUS);
        
        lbNombrePaciente.textProperty().bind(nombrePaciente);
    }
    
    /**
     * Crea un relock
     */
    private void bindToTime() {
        Timeline timeline = 
                new Timeline(new KeyFrame(Duration.seconds(0),
                             event -> lbHora.setText(LocalTime.now().format(formato))),
                             new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void fecha(){
        LocalDate curDateTime = LocalDate.now();
             lbFecha.setText(curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy")));
    }
    /****************************************************************************/
    //Lanzadores
    
    
    /**
     * Carga Lista de pacientes
     */
    public void lanzaListaPacientes(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/ListaPaciente.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ListaPacienteController controller = loader.getController();
            controller.transmisor(this);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Lanza historia de paciente
     * @param paci 
     */
     public void lanzaHistoriaMedica(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/ResumenPaciente.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ResumenPacienteController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    /**
    * lanza citas medicas
     * @param tab
    */ 
    public void lanzaCitas(int tab){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/AgendaCitas.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            AgendaCitasController controller = loader.getController();
            controller.transmisor(this, tab);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Lanza lista de usuarios
     */
    public void lanzaListaUsuarios(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Usuarios/ListaUsuarios.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ListaUsuariosController controller = loader.getController();
            controller.transmisor(this);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lanza lista de dias festivos
     */
    public void lanzaListaFestivos(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Agenda/DiasFestivos.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            DiasFestivosController controller = loader.getController();
            controller.pasoPrincipal(this);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza antecedentes escena pacientes heredo familiares del paciente seleccionado
     * @param paci 
     */
    public void lanzaAntHeredo(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/HeredoFamiliares.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            HeredoFamiliaresController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lanza antecedentes no patologicos
     */
    public void lanzaAntNoPatologico(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/PersonalesNoPatologicos.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            PersonalesNoPatologicosController controller = loader.getController();
            controller.transmisor(this);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza antecedentes patologicos
     */
    public void lanzaAntPatologico(int donde){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Antecedentes/AntecedentesMédicos.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            AntecedentesMédicosController controller = loader.getController();
            controller.transmisor(this, donde);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza somatrometria
     * @param paci 
     */
    public void lanzaSomatometria(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Paciente/Somatometría.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            SomatometríaController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Lanza nota medica
     * @param paci 
     */
    public void lanzaNotaMedica(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Consulta/NotaMedicos.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            NotaMedicosController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lansa la segunda parte de la nota medica
     * @param paci
     * @param motivo
     * @param exploracion 
     */
    public void lanzaNotaMedicaSegundaParte(Paciente paci, String motivo, String exploracion){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Consulta/SegundaParte.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            SegundaParteController controller = loader.getController();
            controller.transmisor(this, paci, motivo, exploracion);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza solicitud laboratorio
     * @param paci 
     */
    public void lanzaSolicitudLab(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Auxiliares/Laboratoriales.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            LaboratorialesController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza resultados de laboratoriales
     * @param paci 
     */
    public void lanzaResLab(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Resultados/ResLab.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ResLabController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza el la receta 
     * @param paci 
     */
    public void lanzaResceta(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Auxiliares/Receta.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            RecetaController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza rayos x y otros estudios
     * @param paci 
     */
    public void lanzaRayos(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Auxiliares/RayosX.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            RayosXController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * lanza la escena de las resultados de los rayos x
     * @param paci 
     */
    public void lanzaResRayos(Paciente paci){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Resultados/ResRay.fxml"));
            AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
            ResRayController controller = loader.getController();
            controller.transmisor(this, paci);
            setScreen(unoAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
                   new KeyFrame(new Duration(TRANSITION_TIMER), new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent t) {
                           stackPane.getChildren().remove(0);        //remove the displayed screen
                           stackPane.getChildren().add(0, view);     //add the screen
                           Timeline fadeIn = new Timeline(
                                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                   new KeyFrame(new Duration(TRANSITION_TIMER), new KeyValue(opacity, 1.0)));
                           fadeIn.play();
                       }
                   }, new KeyValue(opacity, 0.0)));
           fade.play();
       } else {
           stackPane.setOpacity(0.0);
           stackPane.getChildren().add(view);       //no one else been displayed, then just show
           Timeline fadeIn = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                   new KeyFrame(new Duration(TRANSITION_TIMER), new KeyValue(opacity, 1.0)));
           fadeIn.play();
       }
       return true;
   }
   
    /******************************************************************************/
    /**Menus**\*/
    
    private void prepareMenuItem(MenuItem menuItem, String text, MenuButton menuButton){
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(32));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
    }
    
    /**
     * Carga menu agenda Citas
     */
    public void menusAgendaCitas(){
        prepareMenuItem(mregCit, "Registro de Citas", splAgendaCitas);
            prepareMenuItem(mIegCit, "Registro de Citas", splAgendaCitas);
        
        mregCit.getItems().add(mIegCit);
        
        splAgendaCitas.getItems().addAll(mregCit);
        
        mregCit.setOnAction(evento->{
            lanzaCitas(2);
        });
        
        splAgendaCitas.setOnAction(evento->{
            splAgendaCitas.show();
        });
    }
    
    /**
     * agrega el menu de atnecion integral
     */
    public void menuAntencionIntegral(){
         prepareMenuItem(mHistoriaClinica, "Historia clínica", splAtencionIntegral);
            prepareMenuItem(mIAntecedentes, "Historia clínica", splAtencionIntegral);
            prepareMenuItem(mIHeredo, "Ant. Heredo - Familiares", splAtencionIntegral);
            prepareMenuItem(mIPersoNoPato, "Ant. Personales no patológicos", splAtencionIntegral);
            prepareMenuItem(mIPersoPato, "Ant. Personales patológicos", splAtencionIntegral);
        prepareMenuItem(mSomatometria, "Somatometría", splAtencionIntegral);
            prepareMenuItem(mISoma, "Somatometría", splAtencionIntegral);
        prepareMenuItem(mAtencionMedica, "Atención Médica", splAtencionIntegral);
            prepareMenuItem(mINotaMedica, "Nota Médica", splAtencionIntegral); 
            
        mHistoriaClinica.getItems().addAll(mIAntecedentes, mIHeredo, mIPersoNoPato, mIPersoPato);
        mSomatometria.getItems().addAll(mISoma);
        mAtencionMedica.getItems().addAll(mINotaMedica);
        splAtencionIntegral.getItems().addAll(mHistoriaClinica, mSomatometria, mAtencionMedica);
       
        
        mIAntecedentes.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaHistoriaMedica(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mIHeredo.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaAntHeredo(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mIPersoNoPato.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaAntNoPatologico();
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mIPersoPato.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaAntPatologico(1);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mISoma.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaSomatometria(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mINotaMedica.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaNotaMedica(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        splAtencionIntegral.setOnAction(evento->{
            splAtencionIntegral.show();
        });
        
    }
    
    /**
     * Carga menu de botones auxiliares
     */
    public void menusAuxiliares(){
        prepareMenuItem(mSolicitudLabora, "Solicitud de laboratorio", splAuxiliares);
            prepareMenuItem(mISoliLab, "Solicitud de laboratorio", splAuxiliares);
        prepareMenuItem(mReceta, "Receta", splAuxiliares);
            prepareMenuItem(mIReceta, "Receta", splAuxiliares);
        prepareMenuItem(mRayos, "Solicitud de Rayos X", splAuxiliares);
            prepareMenuItem(mIRayos, "Solicitud de Rayos X", splAuxiliares);
       
        
        mSolicitudLabora.getItems().add(mISoliLab);
        mReceta.getItems().add(mIReceta);
        mRayos.getItems().add(mIRayos);
        splAuxiliares.getItems().addAll(mSolicitudLabora, mReceta, mRayos);
        
        mISoliLab.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaSolicitudLab(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mIReceta.setOnAction(evento->{
           if (pacienteAUsar != null) {
               lanzaResceta(pacienteAUsar);
           }else
               aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                       "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
       });
        
        mIRayos.setOnAction(evento->{
           if (pacienteAUsar != null) {
               lanzaRayos(pacienteAUsar);
           }else
               aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                       "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        splAuxiliares.setOnAction(evento->{
            splAuxiliares.show();
        });
    }
    
    /**
     * le da formato a los bottos
     */
    private void buttonFormats(){
        imagenAA(bttSalir, aceptar);
        imagenA(bttPortada, guardar);
        imagenA(bttAndministracion, agregar);
        imagenA(bttResumen, imprimir);
        
        bttResumen.setOnAction(evento->{
            if (pacienteAUsar != null) {
               creaHisotiralPDF();
           }else
               aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                       "Es necesario haber seleccionado un paciente para poder imprimir la historia medica");
        });
    }
    /**
     * crea el pdf de la historia medica del paciente
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
        stackPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        
        dbExeccutor.submit(task);
    }
    
    /**
     * Formato de imagen
     * @param button
     * @param img 
     */
    private void imagenA(Button button, Image img){
        ImageView imageView = new ImageView(img);
        imageView.setRotate(90);
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
    }
    
    /**
     * formato de imagen sin darse la vuelta
     * @param button
     * @param img 
     */
    private void imagenAA(Button button, Image img){
        ImageView imageView = new ImageView(img);
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
    }
    
    /**
     * carga menu de resultados
     */
    public void menusResultados(){
        prepareMenuItem(mResLabora, "Resultado de laboratorio", splResultados);
            prepareMenuItem(mIResLab, "Resultado de laboratorio", splResultados);
        prepareMenuItem(mResRay, "Resultados de Rayos X", splResultados);
            prepareMenuItem(mIResRay, "Resultados de Rayos X", splResultados);
        
        mResLabora.getItems().add(mIResLab);
        mResRay.getItems().add(mIResRay);
        
        splResultados.getItems().addAll(mResLabora, mResRay);
        
        mIResLab.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaResLab(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        mResRay.setOnAction(evento->{
            if (pacienteAUsar != null) {
                lanzaResRayos(pacienteAUsar);
            }else
                aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                        "Es necesario haber seleccionado un paciente para poder entrar a esta sección");
        });
        
        
        splResultados.setOnAction(evento->{
            splResultados.show();
        });
    }
    
    /**
     * imagen de botton
     * @param button
     * @param img 
     */
    private void imagenButto(Button button, Image img){
        ImageView imageView = new ImageView(img);
        button.setGraphic(imageView);
    }
    
    public void pupOver(){
        VBox vbox2 = new VBox(20);
        vbox2.setPrefSize(300, 250);
        vbox2.getChildren().addAll(bttPopPaciente, bttPopUsuario,bttFestivos);
        vbox2.setAlignment(Pos.CENTER);
        bttPopPaciente.setPadding(new Insets(5));
        bttPopUsuario.setPadding(new Insets(5));
        bttFestivos.setPadding(new Insets(5));
        
        imagenButto(bttPopPaciente, agregar);
        imagenButto(bttPopUsuario, guardar);
        imagenButto(bttFestivos, aceptar);
        
        bttPopPaciente.setOnAction((evento)->{
            lanzaListaPacientes();
            popOver.hide();
        });
        
        bttPopUsuario.setOnAction(evento->{
            lanzaListaUsuarios();
            popOver.hide();
        });
        
        bttFestivos.setOnAction(evento->{
            lanzaListaFestivos();
            popOver.hide();
        });
        
        vbox2.setFillWidth(true);
        popOver.setContentNode(vbox2);
        popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
        popOver.setDetachable(false);
    }
}
