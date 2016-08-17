/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Agenda;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.Cita;
import com.aohys.copiaIMSS.MVC.Modelo.Cita.agregaCitaCsl;
import com.aohys.copiaIMSS.MVC.Modelo.Cita.cargaCitasFechaUsuarioTask;
import com.aohys.copiaIMSS.MVC.Modelo.Cita.horariosCitasFechaUsuarioTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos.listaDiasFestivosTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diaLibre;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diasConsulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diasConsulta.listaDiasConsultaMedicoTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.horario;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.horario.listaHorarioDisponibleTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.peridoVacaMedico;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.peridoVacaMedico.listaPeridoVacacionalTask;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario.cargaListaMedEspecialidadTask;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario.cargaListaMedTask;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario.listaEspeciTask;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class NuevaCitaController implements Initializable {

    //Variables de escena
    private AgendaCitasController cordi;
    private PrincipalController princi;

    //Conexion
    Hikari dbConn = new Hikari();
    
    //Variables de controlador
    private BooleanProperty primeraVez = new SimpleBooleanProperty(false);
    Auxiliar aux = new Auxiliar();
    Usuario usa = new Usuario();
    Usuario usuario;
    Paciente paciente;
    diasConsulta diasConsulta = new diasConsulta();
    peridoVacaMedico periVacaMedico = new peridoVacaMedico();
    DiasFestivos diasFestivos = new DiasFestivos();
    diaLibre diaLibre = new diaLibre();
    horario horaio = new horario();
    Cita cita = new Cita();
    Paciente paci = new Paciente();
    
    //integers que representan dias
    int lunes = 0;
    int martes = 0;
    int miercoles = 0;
    int jueves = 0;
    int viernes = 0;
    int sabado = 0;
    int domingo = 0;
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
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    //FXML de enmedio
    @FXML private RadioButton rbSiPriVist;
    @FXML private RadioButton rbNoPriVist;
    //Combobox de seleccion de medico
    @FXML private ComboBox<Usuario> cbbServicio;
    @FXML private ComboBox<Usuario> cbbMedico;
    //FXML de abajo
    @FXML private DatePicker dpFechaConsulta;
    @FXML private Button bttNuevaCitPac;
    @FXML private Button bttNuevaCit;
    //ComboBoxHora
    @FXML private ComboBox<LocalTime> cbbHoraConsul;
    //Tabla
    @FXML private TableView<Cita> tvCitas;
    @FXML private TableColumn<Cita, String> colNombre;
    @FXML private TableColumn<Cita, Time> colHoraCita;
    //label de fecha
    @FXML private Label lbFecha;
    @FXML private Label lbNoMedico;
    
    ObservableList<Integer> listaDiasConsulta = FXCollections.observableArrayList();
    ObservableList<peridoVacaMedico> listPeridoMedicos = FXCollections.observableArrayList();
    ObservableList<diaLibre> listDiasLibres = FXCollections.observableArrayList();
    ObservableList<DiasFestivos> listDiasFest = FXCollections.observableArrayList();
    ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
    ObservableList<Usuario> listaServ = FXCollections.observableArrayList();
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    ObservableList<LocalTime> listaHorasUsadas = FXCollections.observableArrayList();
    ObservableList<Cita> listaCitasMedicos = FXCollections.observableArrayList();
    ObservableList<LocalTime> listaHorasValidas = FXCollections.observableArrayList();
    ObservableList<LocalTime> listaResultado = FXCollections.observableArrayList();
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
     * carga la lista de dias festivos 
     */
    public void listaDiasFestivos(){
        listaDiasFestivosTask lTask = diasFestivos.new listaDiasFestivosTask();
        lTask.setOnSucceeded(evento->{
            listDiasFest.addAll(lTask.getValue());
        });
        dbExeccutor.submit(lTask);
    }
    
    /**
     * da formato al label del medico
     * @param medico 
     */
    private void nombreMedico(Usuario medico){
        lbNoMedico.setText(String.format("%s %s %s", 
                medico.getNombre_medico(), medico.getApellido_medico(), medico.getApMaterno_medico()));
    }
    
    /**
     * crea y da formato a los combobox servicio y medico
     */
    public void cargaComboBoxs(){
        cargaListaMedicos();
        cbbMedico.setItems(listaMed);
        cbbServicio.setItems(listaServ);
        formatoComboBox();
    }
    
    /**
     * cale que sirve para cargar la lista de medicos y de especialidades
     */
    private void cargaListaMedicos(){
        cargaListaMedTask ttas = usa.new cargaListaMedTask();
        ttas.setOnSucceeded(evento->{
            listaMed.clear();
            listaMed.addAll(ttas.getValue());
            revisaSiesMedicoYselecciona();
        });
        dbExeccutor.submit(ttas);
        
        listaEspeciTask lisTask = usa.new listaEspeciTask();
        lisTask.setOnSucceeded(evento->{
            listaServ.clear();
            listaServ.addAll(lisTask.getValue());
        });
        dbExeccutor.submit(lisTask);
    }
    
   
    
    /**
     * Formato de radio button
     */
    public void radioButtonFormato(){
        ToggleGroup primeraVez = new ToggleGroup();
        primeraVez.getToggles().addAll(rbSiPriVist,rbNoPriVist);
        rbSiPriVist.setUserData(true);
        rbNoPriVist.setUserData(false);
        primeraVez.selectedToggleProperty().addListener((valor,v,n)->{
            this.primeraVez.setValue(Boolean.valueOf(n.getUserData().toString()));
        });
        rbNoPriVist.setSelected(true);
    }
    
    /**
     * da formato a los dos combobox
     */
    public void formatoComboBox(){
        cbbMedico.setCellFactory((comboBox) -> {
            return new ListCell<Usuario>() {
                @Override
                protected void updateItem(Usuario item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(String.format("%s %s %s", 
                                item.getNombre_medico(), item.getApellido_medico(), item.getApMaterno_medico()));
                    }
                }
            };
        });
      
        cbbMedico.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario item) {
                if (item == null) {
                    return null;
                } else {
                    return String.format("%s %s %s", 
                                item.getNombre_medico(), item.getApellido_medico(), item.getApMaterno_medico());
                }
            }

            @Override
            public Usuario fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        cbbServicio.setCellFactory((comboBox) -> {
            return new ListCell<Usuario>() {
                @Override
                protected void updateItem(Usuario item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getEspecialidad_medico());
                    }
                }
            };
        });
      
        cbbServicio.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario item) {
                if (item == null) {
                    return null;
                } else {
                    return item.getEspecialidad_medico();
                }
            }

            @Override
            public Usuario fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        cbbServicio.valueProperty().addListener((ss,v,n)->{
            if (n!=null) {
                listaMedicosSegunEspecialidad(n.getEspecialidad_medico());
            }
        });
        
        cbbMedico.valueProperty().addListener((variable,v,n)->{
            if (n!=null) {
                listasDeDiasMed(n.getId_medico(), n);
            }
        });
        
        cbbHoraConsul.setItems(listaResultado);
    }
    
    /**
     * lista de consulta de medico mas dias de vacaciones junto con formato de date picker
     * @param idMedico 
     */
    private void listasDeDiasMed(String idMedico, Usuario usuarioDate){
        listaDiasConsultaMedicoTask taskUno = diasConsulta.new listaDiasConsultaMedicoTask(idMedico);
        taskUno.setOnSucceeded(evento->{
            listaDiasConsulta.clear();
            listaDiasConsulta.addAll(taskUno.getValue());
        });
        dbExeccutor.submit(taskUno);
        
        listaPeridoVacacionalTask taskDos = periVacaMedico.new listaPeridoVacacionalTask(idMedico);
        taskDos.setOnSucceeded(evento->{
            listPeridoMedicos.clear();
            listPeridoMedicos.addAll(taskDos.getValue());
            limpiaCuadros();
            listDiasLibres.clear();
            listDiasLibres.addAll(diaLibre.listaDiasLibresMedico(idMedico));
            datePickerFormato(usuarioDate);
            usuario = usuarioDate;
            nombreMedico(usuarioDate);
        });
        
        dbExeccutor.submit(taskDos);
    }
    
    /**
     * cambia la lista de medicos segun su especialidad
     * @param especialidad 
     */
    private void listaMedicosSegunEspecialidad(String especialidad){
        cargaListaMedEspecialidadTask task = usa.new cargaListaMedEspecialidadTask(especialidad);
        task.setOnSucceeded(evento->{
            listaMed.clear();
            listaMed.addAll(task.getValue());
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * Formato del datepicker para elegir la fecha de la cita quitando dias ya transcurridos
     * @param usuario
     */
    public void datePickerFormato(Usuario usuario){
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.isBefore(LocalDate.now())){
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
              
                if (usuario != null) {
                    confirDiasConsulta();
                    //dias validos de consulta
                    Integer entero = item.getDayOfWeek().getValue();
                    if (entero.equals(lunes)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(martes)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(miercoles)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(jueves)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(viernes)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(sabado)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }else if (entero.equals(domingo)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }
                    //perido de vacaciones
                    for(peridoVacaMedico per : listPeridoMedicos){
                         if (item.isAfter(per.getInicia_pvm().toLocalDate())
                             && item.isBefore(per.getTermina_pvm().toLocalDate())){
                             setStyle("-fx-background-color: #ffc0cb;");
                             setDisable(true);
                         }
                         if (item.equals(per.getInicia_pvm().toLocalDate())
                                 ||item.equals(per.getTermina_pvm().toLocalDate())) {
                             setStyle("-fx-background-color: #ffc0cb;");
                             setDisable(true);
                         }
                     }
                    
                    for(diaLibre dia : listDiasLibres){
                        if (dia.getFecha_diaLibre().toLocalDate().equals(item)){
                             setStyle("-fx-background-color: #ffc0cb;");
                             setDisable(true);
                         }
                    }
                 }
                //cancela los dias festivos
                for(DiasFestivos diafes: listDiasFest){
                    if (diafes.getFecha_DiasFes().toLocalDate().equals(item)) {
                        setStyle("-fx-background-color: #ffc0cb;");
                        setDisable(true);
                    }
                }
            }
        };
        dpFechaConsulta.setDayCellFactory(dayCellFactory);
        
        dpFechaConsulta.valueProperty().addListener((variable,v,n)->{
            if (n!=null) {
                if (cbbMedico.getValue()!=null) {
                    actualizaListasParaHoraValido(this.usuario.getId_medico(), Date.valueOf(n));
                    fecha(n);
                    actualizaTablaTask(Date.valueOf(n), this.usuario.getId_medico());
                }else{
                    aux.alertaError("Es necesario seleccionar un medico", "Es necesario seleccionar un medico", 
                    "Para poder seleccionar una fecha es necesario seleccionar un medico");
                    cbbMedico.requestFocus();
                }
                    
            }
           
        });
    }
    
    /**
     * le da valor a los dias de consulta para despues distingir
     */
    private void confirDiasConsulta(){
        lunes = listaDiasConsulta.contains(1)?0:1;
        martes = listaDiasConsulta.contains(2)?0:2;
        miercoles = listaDiasConsulta.contains(3)?0:3;
        jueves = listaDiasConsulta.contains(4)?0:4;
        viernes = listaDiasConsulta.contains(5)?0:5;
        sabado = listaDiasConsulta.contains(6)?0:6;
        domingo = listaDiasConsulta.contains(7)?0:7;
    }
    
    /**
     * carga la lista de horas de consulta del medico 
     */
    public void formatoHorario(){
        for(LocalTime horasUsadas : listaHorasUsadas){
            for (LocalTime horasValidas : listaHorasValidas) {
                if (horasUsadas.equals(horasValidas)) {
                    listaResultado.remove(horasUsadas);
                }
            }
        };
    }
    /**
     * actualiza las listas de horario valido y de resultado para poder dar formato al combobox de horario
     * @param idUsuario 
     */
    private void actualizaListasParaHoraValido(String idUsuario, Date fecha){
        listaHorarioDisponibleTask task = horaio.new listaHorarioDisponibleTask(idUsuario);
        task.setOnSucceeded(evento->{
            listaHorasValidas.clear();
            listaHorasValidas.addAll(task.getValue());
            listaResultado.clear();
            listaResultado.addAll(listaHorasValidas);
        });
        dbExeccutor.submit(task);
          
        horariosCitasFechaUsuarioTask horTask = cita.new horariosCitasFechaUsuarioTask(fecha, idUsuario);
        horTask.setOnSucceeded(evento->{
            listaHorasUsadas.clear();
            listaHorasUsadas.addAll(horTask.getValue());
            formatoHorario();
        });
        dbExeccutor.submit(horTask);
        
    }
    
    /**
     * limpia el dp y el combobox cuando se cambia de medico
     */
    private void limpiaCuadros(){
        dpFechaConsulta.setValue(null);
        cbbHoraConsul.setValue(null);
    }
    
    /**
     * da fortmato a la label de fecha;
     */
    private void fecha(LocalDate fechaCita){
        LocalDate curDateTime = fechaCita;
             lbFecha.setText(curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy")));
    }
    
    /**
     * le da formato a las citas de ese dia
     */
    public void formatoTablaCitas(){
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>  ("hora_cit"));
        formatoColumnaNombreHiloSeguro(colNombre);
        tvCitas.setItems(listaCitasMedicos);
        
        colNombre.prefWidthProperty().bind(
                    tvCitas.widthProperty()
                    .subtract(colHoraCita.widthProperty())
                    .subtract(2)  // a border stroke?
                 );
    }
    
    /**
     * le da formato a la columna de pacientes respetando hilos
     * @param tc 
     */
    private void formatoColumnaNombreHiloSeguro(TableColumn<Cita, String> tc){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                tc.setCellValueFactory(cellData -> {
                    Cita cita = cellData.getValue();
                    Paciente p = new Paciente();
                    p = paci.cargaSoloUno(cita.getId_Paciente());
                    String regresaColumna = String.format("%s %s %s", 
                            p.getNombre_paciente(), p.getApellido_paciente(), p.getApMaterno_paciente());
                    return new ReadOnlyStringWrapper(regresaColumna);
                });
            }
        };
        dbExeccutor.submit(task);
    }
   
    /**
     * formatos de botones inferiores derechos
     */
    private void formatoBottnesInferiores(){
        bttNuevaCit.setOnAction(evento->{
            cordi.lanzaBusquedaPaciente();
        });
        
        bttNuevaCitPac.setOnAction(evento->{
            if (continuaSINOTratamiento()) {
                try {
                    guardaCitaBD();
                    actualizaTablaTask(Date.valueOf(dpFechaConsulta.getValue()), usuario.getId_medico());
                    actualizaListasParaHoraValido(usuario.getId_medico(), Date.valueOf(dpFechaConsulta.getValue()));
                    aux.informacionUs("La cita ha sido agendada",
                            "La cita ha sido agendada",
                            "La cita ha sido agregada a la base de datos exitosamente");
                } catch (SQLException ex) {
                    Logger.getLogger(NuevaCitaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        bttNuevaCit.setGraphic(new ImageView(aceptar));
        bttNuevaCitPac.setGraphic(new ImageView(guardar));
    }

    /**
     * Guarda una cita en la base de datos 
     * @throws java.sql.SQLException
     */
    public void guardaCitaBD() throws SQLException {
        String id_cit = aux.generaID();
        Date fecha_cit = Date.valueOf(dpFechaConsulta.getValue());
        Time hora_cit = Time.valueOf(cbbHoraConsul.getValue());
        boolean primVis_cit = primeraVez.getValue();
        String id_Usuario = usuario.getId_medico();
        String id_Paciente = paciente.getId_paciente();
        Cita.agregaCitaCsl dbCsl = cita.new agregaCitaCsl(id_cit, fecha_cit, hora_cit, 
                 primVis_cit,  id_Usuario,  id_Paciente);
        
        dpFechaConsulta.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(dbCsl.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));

        dbExeccutor.submit(dbCsl);
    }
    
    /**
     * metodo para actualizar la tabla
     * @param fecha
     * @param idMed 
     */
    private void actualizaTablaTask(Date fecha, String idMed){
        cargaCitasFechaUsuarioTask actTask = cita.new cargaCitasFechaUsuarioTask(fecha, idMed);
        actTask.setOnSucceeded(evento->{
            listaCitasMedicos.clear();
            listaCitasMedicos.addAll(actTask.getValue());
        });
        dbExeccutor.submit(actTask);
      
    }
    
    /**
     * sirve para ver si un medico entra y si entra lo lleva directo a su agenda
     */
    private void revisaSiesMedicoYselecciona(){
        List<String> listaId = new LinkedList<>();
        for (Usuario usar : listaMed) {
            listaId.add(usar.getId_medico());
        }
        for (String string : listaId) {
            if (string.equals(IngresoController.usua.getId_medico())) {
                cbbMedico.setValue(IngresoController.usua);
            }
        }
    }
    
    /**
     * verifica si estan todos los campos llenos
     * @return 
     */
    private boolean continuaSINOTratamiento(){
        String errorMessage = "";
        if (dpFechaConsulta.getValue()==null) {
            errorMessage += "Día de consulta";
            dpFechaConsulta.requestFocus();
        }
        errorMessage += aux.verificaValufield(cbbHoraConsul, "Hora de consulta");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * Initializes the controltaler class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Carga formato de radiobuttons
        radioButtonFormato();
        //Carga formato de datePicker
        datePickerFormato(null);
        //formato de fecha
        fecha(LocalDate.now());
        //formato bottones
        formatoBottnesInferiores();
        //Forjecutor de servicio
        ejecutorDeServicio();
        //Formato de la tabla
        formatoTablaCitas();
        //formato de los combobox
        cargaComboBoxs();
        //carga la lista de dias festivos
        listaDiasFestivos();
    }    
    
}
