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
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos.listaDiasFestivosTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diasConsulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.horario;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.peridoVacaMedico;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario.cargaListaMedTask;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario.listaEspeciTask;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ConsultaCitaController implements Initializable {
    //Variables de escena
    private AgendaCitasController cordi;
    private PrincipalController princi;

    //Conexion
    Vitro dbConn = new Vitro();
    
    //Variables de controlador
    private BooleanProperty primeraVez = new SimpleBooleanProperty(false);
    Auxiliar aux = new Auxiliar();
    Usuario usa = new Usuario();
    Usuario usuario;
    //Paciente paciente;
    diasConsulta diasConsulta = new diasConsulta();
    peridoVacaMedico periVacaMedico = new peridoVacaMedico();
    DiasFestivos diasFestivos = new DiasFestivos();
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
     * @param princi 
     */
    public void transmisor(AgendaCitasController cordi, PrincipalController princi) {
        this.cordi = cordi;
        this.princi = princi;
        
        
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
    
    //Combobox de seleccion de medico
    @FXML private ComboBox<Usuario> cbbServicio;
    @FXML private ComboBox<Usuario> cbbMedico;
    //FXML de abajo
    @FXML private DatePicker dpFechaConsulta;
    //Tabla
    @FXML private TableView<Cita> tvCitas;
    @FXML private TableColumn<Cita, String> colNombre;
    @FXML private TableColumn<Cita, Time> colHoraCita;
    //label de fecha
    @FXML private Label lbFecha;
    @FXML private Label lbNombreMedico;
    
    ObservableList<Integer> listaDiasConsulta = FXCollections.observableArrayList();
    ObservableList<peridoVacaMedico> listPeridoMedicos = FXCollections.observableArrayList();
    ObservableList<DiasFestivos> listDiasFest = FXCollections.observableArrayList();
    ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
    ObservableList<Usuario> listaServ = FXCollections.observableArrayList();
    ObservableList<Cita> listaCitasMedicos = FXCollections.observableArrayList();
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
                dpFechaConsulta.setValue(LocalDate.now());
            }
        }
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
                        setText(item.getNombre_medico()+ " " + item.getApellido_medico()+" "+item.getApMaterno_medico());
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
                    return item.getNombre_medico()+ " " + item.getApellido_medico()+" "+item.getApMaterno_medico();
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
    }
    
    /**
     * lista de consulta de medico mas dias de vacaciones junto con formato de date picker
     * @param idMedico 
     */
    private void listasDeDiasMed(String idMedico, Usuario usuarioDate){
        diasConsulta.listaDiasConsultaMedicoTask taskUno = diasConsulta.new listaDiasConsultaMedicoTask(idMedico);
        taskUno.setOnSucceeded(evento->{
            listaDiasConsulta.clear();
            listaDiasConsulta.addAll(taskUno.getValue());
        });
        dbExeccutor.submit(taskUno);
        
        peridoVacaMedico.listaPeridoVacacionalTask taskDos = periVacaMedico.new listaPeridoVacacionalTask(idMedico);
        taskDos.setOnSucceeded(evento->{
            listPeridoMedicos.clear();
            listPeridoMedicos.addAll(taskDos.getValue());
            limpiaCuadros();
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
        Usuario.cargaListaMedEspecialidadTask task = usa.new cargaListaMedEspecialidadTask(especialidad);
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
            }

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
            if (n != null) {
                if (cbbMedico.getValue()!= null) {
                    fecha(n);
                    actualizaTablaTask(Date.valueOf(n), cbbMedico.getValue().getId_medico());
                    System.err.println(listaCitasMedicos);
                    formatoLabel();
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
     * limpia el dp y el combobox cuando se cambia de medico
     */
    private void limpiaCuadros(){
        dpFechaConsulta.setValue(null);
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
     * da formato al label del medico
     * @param medico 
     */
    private void nombreMedico(Usuario medico){
        lbNombreMedico.setText(String.format("%s %s %s", 
                medico.getNombre_medico(), medico.getApellido_medico(), medico.getApMaterno_medico()));
    }
    /**
     * le da formato a las citas de ese dia
     */
    public void formatoTablaCitas(){
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>  ("hora_cit"));
        formatoColumnaNombreHiloSeguro(colNombre);
        tvCitas.setItems(listaCitasMedicos);
        
        tvCitas.getSelectionModel().selectedItemProperty().addListener((valor,v,n)->{
             if (n!=null) {
                try(Connection conexInternaA = dbConn.conectarBD()) {
                    cargaDatosLabels(n, conexInternaA);
                    Paciente pacienteDentroTabla = paci.cargaSoloUno(n.getId_Paciente(),conexInternaA);
                    cargaDatos(pacienteDentroTabla);
                    PrincipalController.recibePaciente(pacienteDentroTabla);
                    cita = n;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
             }
        });
         
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
                    try(Connection conexInterna = dbConn.conectarBD()) {
                        p = paci.cargaSoloUno(cita.getId_Paciente(), conexInterna);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String regresaColumna = p.getNombre_paciente()+" "+p.getApellido_paciente()+" "+p.getApMaterno_paciente();
                    return new ReadOnlyStringWrapper(regresaColumna);
                });
            }
        };
        dbExeccutor.submit(task);
    }
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    
    //Labals de abajo
    @FXML private Label lbMedico;
    @FXML private Label lbEspecialidad;
    @FXML private Label lbFechaAbajo;
    @FXML private Label lbHora;
    @FXML private Label lbPrimeraVez;
    
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
     * Escucha Doble click
     */
    public void escuchaDobleclik(){
        tvCitas.setOnMouseClicked((evento)->{
            if (evento.getClickCount() == 2 && tvCitas.getSelectionModel().getSelectedItem()!= null) {
                Paciente algo = PrincipalController.pacienteAUsar;
                AgendaCitasController.cordi.lanzaHistoriaMedica(algo);
            }
        });
    }

    /**
     * le da formato a los labels de abajo 
     */
    public void formatoLabel(){
        lbMedico.setText("");
        lbEspecialidad.setText("");
        lbFechaAbajo.setText("");
        lbHora.setText("");
        lbPrimeraVez.setText("");
        
        lbNombre.setText("");
        lbEdad.setText("");
        lbCURP.setText("");
        lbSexo.setText("");
    }
   
    public void cargaDatosLabels(Cita cit, Connection conex){
        if (cit!=null) {
            Usuario usuarioLabel = usa.CargaSoloUno(cit.getId_Usuario(), conex);
            String medico = String.format("%s %s %s", 
                    usuarioLabel.getNombre_medico(),usuarioLabel.getApellido_medico(), usuarioLabel.getApMaterno_medico());
            lbMedico.setText(medico);
            lbEspecialidad.setText(usuarioLabel.getEspecialidad_medico());
            LocalDate curDateTime = cit.getFecha_cit().toLocalDate();
            lbFechaAbajo.setText(curDateTime.format(
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
     * @param fecha
     * @param idMed 
     */
    private void actualizaTablaTask(Date fecha, String idMed){
        if (fecha!=null) {
            Cita.cargaCitasFechaUsuarioTask actTask = cita.new cargaCitasFechaUsuarioTask(fecha, idMed);
            actTask.setOnSucceeded(evento->{
                listaCitasMedicos.clear();
                listaCitasMedicos.addAll(actTask.getValue());
                for (Cita integer : listaCitasMedicos) {
                    System.err.println(integer.getHora_cit()+" "+integer.getId_Paciente());
                }
            });
            dbExeccutor.submit(actTask);
        }
        
    }
    
    /**
     * actualiza la lista de los dias festivos 
     */
    private void actualizaListadiasFestivos(){
        listaDiasFestivosTask task = diasFestivos.new listaDiasFestivosTask();
        task.setOnSucceeded(evento->{
            listDiasFest.clear();
            listDiasFest.addAll(task.getValue());
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ejecutor de servicios
        ejecutorDeServicio();
        //carga comboboxes
        cargaComboBoxs();
        //carga la lista de dias festivos
        actualizaListadiasFestivos();
        //Carga formato de datePicker
        datePickerFormato(null);
        //formato de fecha
        fecha(LocalDate.now());
        //formato labels
        formatoLabel();
        //escucha doble clik en la tabala
        escuchaDobleclik();
        //formato de la tabla
        formatoTablaCitas();
        //CARGA EL DIA DE HOY
        dpFechaConsulta.setValue(LocalDate.now());
        
    }   
    
}
