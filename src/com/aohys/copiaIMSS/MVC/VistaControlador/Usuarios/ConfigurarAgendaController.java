/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Usuarios;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diaLibre;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diasConsulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.horario;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.peridoVacaMedico;
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
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ConfigurarAgendaController implements Initializable {
    //Variables de escena
    private ListaUsuariosController cordi;
    private PrincipalController princiCordi = new PrincipalController();
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    //Conexion
    Vitro dbConn = new Vitro();
    
    //Variables de controlador
    Auxiliar aux = new Auxiliar();
    Usuario usa = new Usuario();
    Usuario usuario;
    horario horario = new horario();
    horario horsss;
    diasConsulta diasConsulta = new diasConsulta();
    diasConsulta disssConsss;
    peridoVacaMedico peVacaMedico = new peridoVacaMedico();
    diaLibre diLibre = new diaLibre();
    diaLibre dialibre;
    
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param usuario 
     * @param princiCordi 
     */
    public void transmisor(ListaUsuariosController cordi, Usuario usuario, 
            PrincipalController princiCordi) {
        this.cordi = cordi;
        this.usuario = usuario;
        this.princiCordi = princiCordi;
        try(Connection conex = dbConn.conectarBD()) {
            horsss = horario.cargaSoloUno(usuario.getId_medico(), conex);
            disssConsss = diasConsulta.unSoloDiasConsultaMedico(conex, usuario.getId_medico());
            actualizaListaPeriodoVacacional(usuario.getId_medico());
            tablaDiaLibre(conex);
            cargaComponentes(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    
    //Variables de actualizacion
    private Boolean boolActualizaHorario = false;
    
    //FXML
    @FXML private Label lbNombreMedico;
    @FXML private Button bttAgregar;
    //Comboboxes
    @FXML private ComboBox<LocalTime> cbbHorarioEntrada;
    @FXML private ComboBox<LocalTime> cbbHorarioSalida;
    @FXML private ComboBox<Integer> cbbDuracionConsulta;
    //ChoiceBox
    @FXML private CheckBox chbLunes;
    @FXML private CheckBox chbMartes;
    @FXML private CheckBox chbMiercoles;
    @FXML private CheckBox chbJueves;
    @FXML private CheckBox chbViernes;
    @FXML private CheckBox chbSabado;
    @FXML private CheckBox chbDomingo;
    //DatePicker
    @FXML private DatePicker dpInicioVaca;
    @FXML private DatePicker dpTerminoVaca;
    @FXML private DatePicker dpAusenciaProgramada;
    //Tabla Periodo Vacaciones
    @FXML private TableView<peridoVacaMedico> tvPeriodoVacaMedico;
    @FXML private TableColumn<peridoVacaMedico, Date> colInicio;
    @FXML private TableColumn<peridoVacaMedico, Date> colTermino;
    @FXML private Button bttPeridoVacaMedico;
    //Tabla dia libre programado
    @FXML private TableView<diaLibre> tvDiaLibre;
    @FXML private TableColumn<diaLibre, Date> colDiaLibreProgramado;
    @FXML private Button bttDiaLibre;
    
    //Lista para componentes
    ObservableList<LocalTime> horas = FXCollections.observableArrayList();
    ObservableList<LocalTime> horSalida = FXCollections.observableArrayList();
    ObservableList<Integer> listaMinutos = FXCollections.observableArrayList();
    ObservableList<CheckBox> listaChecbox = FXCollections.observableArrayList();
    ObservableList<diaLibre> listDiLibres = FXCollections.observableArrayList();
    ObservableList<peridoVacaMedico> listPeridoVaca = FXCollections.observableArrayList();
    
    //Boleand
    private BooleanProperty lunes = new SimpleBooleanProperty(false);
    private BooleanProperty martes = new SimpleBooleanProperty(false);
    private BooleanProperty miercoles = new SimpleBooleanProperty(false);
    private BooleanProperty jueves = new SimpleBooleanProperty(false);
    private BooleanProperty viernes = new SimpleBooleanProperty(false);
    private BooleanProperty sabado = new SimpleBooleanProperty(false);
    private BooleanProperty domingo = new SimpleBooleanProperty(false);
    
    /**
     * carga los componentes del medico
     * @param conex
     */
    public void cargaComponentes(Connection conex){
        lbNombreMedico.setText(String.format("%s %s %s", 
                usuario.getNombre_medico(), usuario.getApellido_medico(), usuario.getApMaterno_medico()));
        if (horsss!= null || disssConsss != null) {
            horarioCarga();
            diasConsulCarga();
        }
    }
    
    /**
     * carga horario 
     * @param conex 
     */
    private void horarioCarga(){
        cbbHorarioEntrada.setValue(horsss.getEntra_horario().toLocalTime());
        cbbHorarioSalida.setValue(horsss.getSale_horario().toLocalTime());
        cbbDuracionConsulta.setValue(horsss.getDuacion_consul());
        boolActualizaHorario = true;
    }
    
    /**
     * carga dias de consulta
     */
    private void diasConsulCarga(){
        seleccionaChecbox(chbLunes, disssConsss.getLunes_c());
        seleccionaChecbox(chbMartes, disssConsss.getMartes_c());
        seleccionaChecbox(chbMiercoles, disssConsss.getMiercoles_c());
        seleccionaChecbox(chbJueves, disssConsss.getJueves_c());
        seleccionaChecbox(chbViernes, disssConsss.getViernes_c());
        seleccionaChecbox(chbSabado, disssConsss.getSabado_c());
        seleccionaChecbox(chbDomingo, disssConsss.getDomingo_c());
    }
    
    /**
     * si es verdadero selecciona el checkbox
     * @param checkBox
     * @param bool 
     */
    private void seleccionaChecbox(CheckBox checkBox, boolean bool){
        if (bool) {
            checkBox.fire();
        }
    }
    
    /**
     * le da formato a los combobox 
     */
    public void formatoComboBox(){
        LocalTime hora = LocalTime.of(7, 0);
        for (int i = 0; i < 15; i++) {
            horas.add(hora.plusHours(i));
        }
        cbbHorarioEntrada.setItems(horas);
        cbbHorarioEntrada.valueProperty().addListener((var,v,n)->{
            horSalida.clear();
            listaSalida(n);
        });
        listaMinutos.addAll(10,15,20,30,60);
        cbbDuracionConsulta.setItems(listaMinutos);
        
        cbbDuracionConsulta.setValue(10);
        cbbHorarioEntrada.setValue(LocalTime.of(7, 0));
        cbbHorarioSalida.setValue(LocalTime.of(8, 0));
    }
    
    /**
     * cambia horario a mostrar en horario de salida
     * @param horaEntrada
     */
    public void listaSalida(LocalTime horaEntrada){
        for (int i = 1; i < 15; i++) {
            if (!horaEntrada.plusHours(i).isAfter(LocalTime.of(22, 0))) {
                horSalida.add(horaEntrada.plusHours(i));
            }else
                break;
        }
        cbbHorarioSalida.setItems(horSalida);
        cbbHorarioSalida.setValue(horaEntrada.plusHours(1));
    }
    
    /**
     * le da formato a los date pickers
     */
    public void formatoDatePicker(){
       Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.isBefore(LocalDate.now())){
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
            }
        };
        dpInicioVaca.setDayCellFactory(dayCellFactory);
        dpTerminoVaca.setDayCellFactory(dayCellFactory);
        dpAusenciaProgramada.setDayCellFactory(dayCellFactory);
        
        dpInicioVaca.setValue(LocalDate.now());
        dpTerminoVaca.setValue(LocalDate.now().plusDays(1));
        
        datePickerForDina(LocalDate.now());
        
        //Cambios en fechas
        dpInicioVaca.valueProperty().addListener((evento, viejo, nuevo)->{
            if (dpTerminoVaca.getValue().isBefore(nuevo)) {
                dpTerminoVaca.setValue(nuevo.plusDays(1));
            }
            datePickerForDina(nuevo);
        });
    }
    
    /**
     * Formatea dinamico
     * @param localDate 
     */
    public void datePickerForDina(LocalDate localDate){
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.isBefore(localDate.plusDays(1))){
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
            }
        };
        dpTerminoVaca.setDayCellFactory(dayCellFactory);
    }
    
    /**
     * Le da formato a los choiceBoxes
     */
    public void formatoChoiceBoxes(){
        truFalseChecBox(chbLunes, lunes);
        truFalseChecBox(chbMartes, martes);
        truFalseChecBox(chbMiercoles, miercoles);
        truFalseChecBox(chbJueves, jueves);
        truFalseChecBox(chbViernes, viernes);
        truFalseChecBox(chbSabado, sabado);
        truFalseChecBox(chbDomingo, domingo);
        //lista para verificar que por lo menos alguno este chequeado
        listaChecbox.addAll(chbLunes,chbMartes,chbMiercoles,chbJueves,chbViernes,chbSabado,chbDomingo);
    }
    
    /**
     * checa que por lo menos uno este seleccionado
     * @return 
     */
    public boolean verificarPorlomenosuno(){
        boolean regresa = false;
        for (CheckBox checkBox : listaChecbox) {
            if (checkBox.isSelected()) {
                regresa = true;
                break;
            }
        }
        return regresa;
    }
    
    /**
     * le da formato a los chechBoxes
     * @param checkBox
     * @param bool 
     */
    public void truFalseChecBox(CheckBox checkBox, BooleanProperty bool){
        checkBox.selectedProperty().addListener((variable,v,n)->{
            if (checkBox.isSelected()) {
                bool.setValue(true);
            }else{
                bool.setValue(false);
            }
        });
    }
    
    /**
     * guarda el horario del horario del medico seleccionado
     * @param conex 
     */
    public void guardaHorario(Connection conex){
        String id_horario = aux.generaID();
        Time entra_horario = Time.valueOf(cbbHorarioEntrada.getValue());
        Time sale_horario = Time.valueOf(cbbHorarioSalida.getValue());
        Integer duacion_consul = cbbDuracionConsulta.getValue();
        String id_medico = usuario.getId_medico();
        
        horario.agregaHorario(id_horario, entra_horario, sale_horario, duacion_consul, id_medico, conex);
    }
    
    /**
     * Actualiza horario del medico seleccionado
     * @param conex 
     */
    public void actualizaHorario(Connection conex){
        String id_horario = horsss.getId_horario();
        Time entra_horario = Time.valueOf(cbbHorarioEntrada.getValue());
        Time sale_horario = Time.valueOf(cbbHorarioSalida.getValue());
        Integer duacion_consul = cbbDuracionConsulta.getValue();
        String id_medico = usuario.getId_medico();
        horario.actualizaHorario(id_horario, entra_horario, sale_horario, duacion_consul, id_medico, conex);
    }
    
    /**
     * guarda dias de consulta del medico seleccionado
     * @param conex 
     */
    private void guardaDiasConsulta(Connection conex){
        String id_diasConsul = aux.generaID();
        String id_medico = usuario.getId_medico();
        
        diasConsulta.agregaDiasConsulta(id_diasConsul, 
                lunes.getValue(), martes.getValue(), miercoles.getValue(), jueves.getValue(), 
                viernes.getValue(), sabado.getValue(), domingo.getValue(), id_medico, conex);
    }
    
    /**
     * actualiza el dia de consulta del medico seleccionado
     * @param conex 
     */
    private void actualizaDiasConsulta(Connection conex){
        String id_diasConsul = disssConsss.getId_diasConsul();
        String id_medico = usuario.getId_medico();
        
        diasConsulta.actualizaDiasConsulta(id_diasConsul, 
                lunes.getValue(), martes.getValue(), miercoles.getValue(), jueves.getValue(), 
                viernes.getValue(), sabado.getValue(), domingo.getValue(), id_medico, conex);
    }
    
    /**
     * agrega el perido de vacaciones del medico seleccionado
     * @param conex 
     */
    private void guardaPeridoVacaciones(Connection conex){
        String id_peridoVacaMedico = aux.generaID();
        Date inicia_pvm = Date.valueOf(dpInicioVaca.getValue());
        Date termina_pvm = Date.valueOf(dpTerminoVaca.getValue());
        String id_medico = usuario.getId_medico();
        
        peVacaMedico.agregaPeridoVacacional(id_peridoVacaMedico, inicia_pvm, termina_pvm, id_medico, conex);
    }
    
    /**
     * actualiza la tabla de perido de vacaciones
     */
    private void formatoTablaPeridoVacaciones(){
        colInicio.setCellValueFactory (new PropertyValueFactory<>  ("inicia_pvm"));
        colTermino.setCellValueFactory(new PropertyValueFactory<>  ("termina_pvm"));
        
        tvPeriodoVacaMedico.setItems(listPeridoVaca);
    }
    
    /**
     * actualiza la lista de periodos vacionales
     * @param idMedico 
     */
    private void actualizaListaPeriodoVacacional(String idMedico){
        peridoVacaMedico.listaPeridoVacacionalTask taskDos = peVacaMedico.new listaPeridoVacacionalTask(idMedico);
        taskDos.setOnSucceeded(evento->{
            listPeridoVaca.clear();
            listPeridoVaca.addAll(taskDos.getValue());
        });
        
        dbExeccutor.submit(taskDos);
    }
   
    /**
     * borra el perido vacacional seleccionado 
     */
    @FXML
    public void borrarPeriodoVacaciones(){
        peridoVacaMedico pvMedicoBorrar = tvPeriodoVacaMedico.getSelectionModel().getSelectedItem();
        if (pvMedicoBorrar!=null) {
            try(Connection conex = dbConn.conectarBD()) {
                peVacaMedico.borraPeridoVacacional(pvMedicoBorrar.getId_peridoVacaMedico(), conex);
                aux.informacionUs("Se borro el periodo vacacional", "Se borro el periodo vacacional", 
                        String.format("El periodo vacacional del %s al %s fue borrado exitosamente de la base de datos", 
                                pvMedicoBorrar.getInicia_pvm(), pvMedicoBorrar.getInicia_pvm()));
                listPeridoVaca.remove(pvMedicoBorrar);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else
            aux.alertaError("Selecciona un periodo vacacional", "Selecciona un periodo vacacional", 
                    "Se debe seleccionar un periodo vacacional para ser borrado de la base de datos");
    }
    
    /**
     * Agrega dia libre del medico seleccionado
     * @param conex 
     */
    private void agregarDiaLibre(Connection conex){
        String id_diaLibre = aux.generaID();
        Date fecha_diaLibre = Date.valueOf(dpAusenciaProgramada.getValue());
        String id_medico = usuario.getId_medico();
        
        diLibre.agregaDiaLibre(id_diaLibre, fecha_diaLibre, id_medico, conex);
    }
    
    /**
     * carga la tabla de dias libres programados del medico seleccionado
     * @param conex 
     */
    private void tablaDiaLibre(Connection conex){
        colDiaLibreProgramado.setCellValueFactory (new PropertyValueFactory<>  ("fecha_diaLibre"));
        listDiLibres.clear();
        listDiLibres.addAll(diLibre.listaDiasLibresMedico(conex, usuario.getId_medico()));
        tvDiaLibre.setItems(listDiLibres);
    }
    
    @FXML
    public void borrarDiaLibrePlaneado(){
        diaLibre  diap = tvDiaLibre.getSelectionModel().getSelectedItem();
        if (diap!=null) {
            try(Connection conex = dbConn.conectarBD()) {
                diap.borrarDiaLibre(diap.getId_diaLibre(), conex);
                aux.informacionUs("Se borro un día libre", "Se borro un día libre", 
                        String.format("El %s día libre se borro de la base de datos exitosamente", 
                                diap.getFecha_diaLibre()));
                listDiLibres.remove(diap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else
            aux.alertaError("Selecciona un día libre", "Selecciona un día libre", 
                    "Se debe seleccionar un día libre para ser borrado de la base de datos");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //On action de agregar
        bttAgregar.setOnAction(evento->{
            if (verificarPorlomenosuno()) {
               try(Connection conex = dbConn.conectarBD()) {
                    if (boolActualizaHorario || disssConsss != null) {
                        actualizaHorario(conex);
                        actualizaDiasConsulta(conex);
                        aux.informacionUs("Configuración de agenda modificada", "Configuración de agenda modificada", 
                                String.format("La configuración de la agenda del medico: %s %s %s a sido modificada exitosamente en la base de datos", 
                                        usuario.getNombre_medico(), usuario.getApellido_medico(), usuario.getApMaterno_medico()));
                    }else{
                        guardaHorario(conex);
                        guardaDiasConsulta(conex);
                        aux.informacionUs("Configuración de agenda guardada", "Configuración de agenda guardada", 
                                String.format("La configuración de la agenda del medico: %s %s %s a sido guardada exitosamente en la base de datos", 
                                        usuario.getNombre_medico(), usuario.getApellido_medico(), usuario.getApMaterno_medico()));
                    }
                    princiCordi.lanzaListaUsuarios();
                } catch (SQLException e) {
                    e.printStackTrace();
                } 
            }else
                aux.alertaError("Selecciona un día de consulta", "Selecciona un día de consulta", 
                        "Por lo menos se debe de seleccionar un día de consulta a la semana para poder guardar la configuración");
        });
        
        bttPeridoVacaMedico.setOnAction(evento->{
            try(Connection conex = dbConn.conectarBD()) {
                guardaPeridoVacaciones(conex);
                actualizaListaPeriodoVacacional(usuario.getId_medico());
                aux.informacionUs("Periodo vacacional guardado", "Periodo vacacional guardado", 
                        String.format("El periodo vacacional del %s al %s fue agregado exitosamente de la base de datos", 
                            dpInicioVaca.getValue(), dpTerminoVaca.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        bttDiaLibre.setOnAction(envento->{
            try(Connection conex = dbConn.conectarBD()) {
                agregarDiaLibre(conex);
                tablaDiaLibre(conex);
                aux.informacionUs("Día de descanso programado guardado", "Día de descanso programado guardado", 
                        String.format("El día %s de descanso programado fue guardado exitosamente en la base de datos",
                                dpAusenciaProgramada.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        bttDiaLibre.setGraphic(new ImageView(guardar));
        bttPeridoVacaMedico.setGraphic(new ImageView(guardar));
        bttAgregar.setGraphic(new ImageView(aceptar));
        
        //formato ComboBox
        formatoComboBox();
        //formato datePickers
        formatoDatePicker();
        //formato choicebocex
        formatoChoiceBoxes();
        //ejecutor de servicios
        ejecutorDeServicio();
        //agrega formato de tabla periodo vacacional
        formatoTablaPeridoVacaciones();
    }    
    
}
