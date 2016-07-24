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
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.diasConsulta;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.horario;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.peridoVacaMedico;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
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
        try(Connection conex = dbConn.conectarBD()) {
            cargaComboBoxs(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    
    /**
     * crea y da formato a los combobox servicio y medico
     * @param conex 
     */
    public void cargaComboBoxs(Connection conex){
        formatoComboBox();
        listaMed.clear();
        listaMed.addAll(usa.cargaListaMed(conex));
        cbbMedico.setItems(listaMed);
        cbbServicio.setItems(usa.listaEspeci(conex));
        revisaSiesMedicoYselecciona();
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
                try(Connection conex = dbConn.conectarBD()) {
                    cbbMedico.setItems(usa.cargaListaMedEspecialidad(conex,n.getEspecialidad_medico()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        cbbMedico.valueProperty().addListener((variable,v,n)->{
            if (n!=null) {
                try(Connection conex = dbConn.conectarBD()) {
                    //Dias de consulta
                    listaDiasConsulta.clear();
                    listaDiasConsulta.addAll(diasConsulta.listaDiasConsultaMedico(conex, n.getId_medico()));
                    //Perido de vacaciones
                    listPeridoMedicos.clear();
                    listPeridoMedicos.addAll((periVacaMedico.listaPeridoVacacional(conex, n.getId_medico())));
                    limpiaCuadros();
                    datePickerFormato(n);
                    usuario = n;
                    formatoLabel();
                    tvCitas.getItems().clear();
                    nombreMedico(n);
                    dpFechaConsulta.setValue(LocalDate.now());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
        });
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
                try(Connection conex = dbConn.conectarBD()) {
                    if (usuario != null) {
                    confirDiasConsulta(usuario, conex);
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
                    //cancela los dias festivos
                    listDiasFest = diasFestivos.listaDiasFestivos(conex);
                    for(DiasFestivos diafes: listDiasFest){
                        if (diafes.getFecha_DiasFes().toLocalDate().equals(item)) {
                            setStyle("-fx-background-color: #ffc0cb;");
                            setDisable(true);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
            }
        };
        dpFechaConsulta.setDayCellFactory(dayCellFactory);
        
        dpFechaConsulta.valueProperty().addListener((variable,v,n)->{
            try(Connection conex = dbConn.conectarBD()) {
                if (n!=null) {
                    fecha(n);
                    formatoTablaCitas(cita.cargaCitasFechaUsuario(
                            conex, Date.valueOf(n), this.usuario.getId_medico()));  
                    formatoLabel();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * le da valor a los dias de consulta para despues distingir
     */
    private void confirDiasConsulta(Usuario usariossss, Connection conex){
        diasConsulta dConsulta = diasConsulta.unSoloDiasConsultaMedico(conex, usariossss.getId_medico());    
        if (dConsulta.getLunes_c()) {
            lunes = 0;
        }else
            lunes = 1;
        if (dConsulta.getMartes_c()) {
            martes = 0;
        }else
            martes = 2;
        if (dConsulta.getMiercoles_c()) {
            miercoles = 0;
        }else
            miercoles = 3;
        if (dConsulta.getJueves_c()) {
            jueves = 0;
        }else
            jueves = 4;
        if (dConsulta.getViernes_c()) {
            viernes = 0;
        }else
            viernes = 5;
        if (dConsulta.getSabado_c()) {
            sabado = 0;
        }else
            sabado = 6;
        if (dConsulta.getDomingo_c()) {
            domingo = 0;
        }else
            domingo = 7;
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
     * @param listaCitas
     * @param conex 
     */
    public void formatoTablaCitas(ObservableList<Cita>listaCitas){
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>  ("hora_cit"));
        colNombre.setCellValueFactory(cellData -> {
            Cita cita = cellData.getValue();
            Paciente p = new Paciente();
            try(Connection conex = dbConn.conectarBD()) {
                p = paci.cargaSoloUno(cita.getId_Paciente(), conex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String regresaColumna = p.getNombre_paciente()+" "+p.getApellido_paciente()+" "+p.getApMaterno_paciente();
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        tvCitas.setItems(listaCitas);
        
         tvCitas.getSelectionModel().selectedItemProperty().addListener((valor,v,n)->{
             if (n!=null) {
                try(Connection conex = dbConn.conectarBD()) {
                    cargaDatosLabels(n, conex);
                    Paciente pacienteDentroTabla = paci.cargaSoloUno(n.getId_Paciente(),conex);
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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Carga formato de datePicker
        datePickerFormato(null);
        //formato de fecha
        fecha(LocalDate.now());
        //formato labels
        formatoLabel();
        //escucha doble clik en la tabala
        escuchaDobleclik();
    }   
    
}
