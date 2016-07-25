/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Agenda;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Cita;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class CancelaCitasController implements Initializable {
    //Variables de escena
    private AgendaCitasController cordi;
    private PrincipalController princi;

    //Conexion
    Vitro dbConn = new Vitro();
    
    //Variables de controlador
    Auxiliar aux = new Auxiliar();
    Usuario usa = new Usuario();
    Usuario usuario;
    Paciente paciente;
    Cita cita = new Cita();
    Paciente paci = new Paciente();
    //private ExecutorService dbExeccutor;
    private ExecutorService dbExeccutor;
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param paci 
     * @param princi 
     */
    public void transmisor(AgendaCitasController cordi, Paciente paci, 
            PrincipalController princi) {
        this.cordi = cordi;
        this.princi = princi;
        this.paciente = paci;
        cargaDatos(paci);
        //formato de tabla
        formatoTablaCitas();
        //actualiza la tabla 
        actualizaTablaTask(paci.getId_paciente());
    }
    /**
     * metodo para pedir un hilo antes de una llamada a la bd
     */
    private void ejecutorDeServicio(){
        dbExeccutor = Executors.newFixedThreadPool(
            1, 
            new databaseThreadFactory()
        ); 
    }
    
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    //Tabla
    @FXML private TableView<Cita> tvCitas;
    @FXML private TableColumn<Cita, Date> colFecha;
    @FXML private TableColumn<Cita, Time> colHoraCita;
    //Botones borrado
    @FXML private Button bttAceptar;
    @FXML private Button bttNuevaCancelacion;
    //Labals de abajo
    @FXML private Label lbMedico;
    @FXML private Label lbEspecialidad;
    @FXML private Label lbFecha;
    @FXML private Label lbHora;
    @FXML private Label lbPrimeraVez;
    
    Image guardar = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    ObservableList<Cita> listaCitasMedicos = FXCollections.observableArrayList();
    /**
     * Carga los datos de la escena 
     * @param paci
     */
    public void cargaDatos(Paciente paci){
        if (paci != null) {
            String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
            lbNombre.setText(nombre);
            lbEdad.setText(aux.edadConMes(paci.getFechaNacimiento_paciente()));
            lbCURP.setText(paci.getCurp_paciente());
            lbSexo.setText(paci.getSexo_paciente());
        }
    }
    
    /**
     * le da formato a las citas de ese dia
     */
    public void formatoTablaCitas(){
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>  ("hora_cit"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>  ("fecha_cit"));
        tvCitas.setItems(listaCitasMedicos);
        
        tvCitas.getSelectionModel().selectedItemProperty().addListener((valor,v,n)->{
            try(Connection conexInterna = dbConn.conectarBD()) {
                cargaDatosLabels(n, conexInterna);
                cita = n;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * borra la cita
     * @param conex 
     */
    public void borraCita(Connection conex){
        cita.borrarCita(cita.getId_cit(), conex);
        listaCitasMedicos.remove(cita);
        //formatoTablaCitas(conex, paciente.getId_paciente());
    }
    
    /**
     * formatos de botones inferiores derechos
     */
    private void formatoBottnesInferiores(){
        bttAceptar.setOnAction(evento->{
            if (cita != null) {
                try(Connection conex = dbConn.conectarBD()) {
                    borraCita(conex);
                    formatoLabel();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else
                aux.alertaError("Selecciona una cita", "Selecciona una cita", 
                        "Se debe seleccionar una cita para ser borrada de la base de datos");
        });
        
        bttNuevaCancelacion.setOnAction(evento->{
            princi.lanzaCitas(1);
        });
        
        bttNuevaCancelacion.setGraphic(new ImageView(aceptar));
        bttAceptar.setGraphic(new ImageView(guardar));
    }

    /**
     * le da formato a los labels de abajo 
     */
    public void formatoLabel(){
        lbMedico.setText("");
        lbEspecialidad.setText("");
        lbFecha.setText("");
        lbHora.setText("");
        lbPrimeraVez.setText("");
    }
   
    public void cargaDatosLabels(Cita cit, Connection conex){
        if (cit!=null) {
            Usuario usuarioLabel = usa.CargaSoloUno(cit.getId_Usuario(), conex);
            String medico = String.format("%s %s %s", 
                    usuarioLabel.getNombre_medico(),usuarioLabel.getApellido_medico(), usuarioLabel.getApMaterno_medico());
            lbMedico.setText(medico);
            lbEspecialidad.setText(usuarioLabel.getEspecialidad_medico());
            LocalDate curDateTime = cit.getFecha_cit().toLocalDate();
            lbFecha.setText(curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy")));
            LocalTime localTimeCita = cit.getHora_cit().toLocalTime();
            lbHora.setText(localTimeCita.format(
                DateTimeFormatter.ofPattern("hh:mm a")));
            if (cit.getPrimVis_cit()) {
                lbPrimeraVez.setText("SI");
            }else
                lbPrimeraVez.setText("NO");
        }
    }
    
    /**
     * metodo para actualizar la tabla
     * @param idMed 
     */
    private void actualizaTablaTask(String idMed){
        Cita.listaCitasTotalUsuarioTask actTask = cita.new listaCitasTotalUsuarioTask(idMed);
        actTask.setOnSucceeded(evento->{
            listaCitasMedicos.clear();
            listaCitasMedicos.addAll(actTask.getValue());
        });
        dbExeccutor.submit(actTask);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formato bottones
        formatoBottnesInferiores();
        //fomarto labels
        formatoLabel();
        //ejecutor del servicio
        ejecutorDeServicio();
        
    }   
}
