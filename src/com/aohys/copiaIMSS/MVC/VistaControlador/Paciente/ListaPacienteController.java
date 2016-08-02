/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente;


import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ListaPacienteController implements Initializable {
    //Variables de escena
    private PrincipalController cordi;

    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(PrincipalController cordi) {
        this.cordi = cordi;
    }

    //Variables a que utiliza el controlador
    Paciente pacc = new Paciente();
    Auxiliar aux = new Auxiliar();
    String vacia;
    
    //Variables de algo
    Boolean udateNote = false;
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //ObserblaeList
    ObservableList<Paciente> listaTa = FXCollections.observableArrayList();
    Image buscar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/busqueda.png");
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");

    //FXML tabla
    @FXML private TableView<Paciente> tbPaciente;
    @FXML private TableColumn<Paciente, String>  colCURP;
    @FXML private TableColumn<Paciente, String>  colNombre;
    
    //Filtro
    @FXML private TextField txtNombre;
    @FXML private TextField txtCURP;
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    
    //FXML private
    @FXML private Button bttBuscar;
    
    //FXML lado
    @FXML private Button bttAgregar;
    @FXML private BorderPane borderPane;
    
    /**
     * Inica la tabla de la escena
     * @param resultadosBus
     */
    public void IniciaTabla(ObservableList<Paciente> resultadosBus){
        colCURP.setCellValueFactory       (new PropertyValueFactory<>  ("curp_paciente"));
        colNombre.setCellValueFactory(cellData -> {
            Paciente p = cellData.getValue();
            String regresaColumna = String.format("%s %s %s", p.getNombre_paciente(), p.getApellido_paciente(), p.getApMaterno_paciente());
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        tbPaciente.setItems(resultadosBus);
        
        colNombre.prefWidthProperty().bind(
            tbPaciente.widthProperty()
            .subtract(colCURP.widthProperty())
            .subtract(5) // a border stroke?
        );
    }
    
    /**
     * Escucha Doble click
     */
    public void escuchaDobleclik(){
        tbPaciente.setOnMouseClicked((evento)->{
            if (evento.getClickCount() == 2 && tbPaciente.getSelectionModel().getSelectedItem()!= null) {
                Paciente algo = tbPaciente.getSelectionModel().getSelectedItem();
                cordi.lanzaHistoriaMedica(algo);
            }
        });
    }
    
    /**
     * Cambia datos
     */
    public void cambiaDatos(){
        tbPaciente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            cargaDatos(newValue);
            PrincipalController.recibePaciente(newValue);
        });
    }
    
    /**
     * le da funcionalidad de buscar al boton paciente 
     */
    public void buscar(){
        try(Connection conex = dbConn.conectarBD()) {
            if(txtNombre.getText().equals("")
                    && txtCURP.getText().equals("")){
                aux.alertaError("Error de búsqueda", "Error de búsqueda", 
                        "Se debe ingresar ya sea un nombre o un CURP a buscar");
            }else{
               if(txtNombre.getText().trim().isEmpty()){
                    IniciaTabla(pacc.buscaCURP(conex, txtCURP.getText()));
                }else
                    IniciaTabla(pacc.buscaNombre(conex, txtNombre.getText())); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
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
     * le da formato a los cuadros de texto
     */
    private void formatoCuadrosBusqueda(){
        presionaEnterAccion(txtNombre);
        presionaEnterAccion(txtCURP);
    }
    /**
     * le da formato a key enter para buscar 
     * @param textField 
     */
    private void presionaEnterAccion(TextField textField){
        textField.setOnKeyPressed(evento->{
            if (evento.getCode() == KeyCode.ENTER) {
                buscar();
            }
        });
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Cambia datos
        cambiaDatos();
        
        //Escucha dobleclik
        escuchaDobleclik();
        
        //funcionalidad del botton buscar
        buscarButtonfun();
        
        bttAgregar.setOnAction(evento->{
            agregaPaciente();
        });
        
        //limpia datos
        limpiart();
        
        //toltip
        aux.toolTipSuperior(txtNombre, 
                "Al presionar la barra espaciadora(ESP) + el botón buscar se obtiene una lista completa de pacientes");
        //Formato de texfield de busqueda
        formatoCuadrosBusqueda();
    
    }    
    
    /**
     * limpia datos inicialmente
     */
    public void limpiart(){
        lbNombre.setText("");
        lbEdad.setText("");
        lbCURP.setText("");
        lbSexo.setText("");
    }
    
    /**
     * le da funcionalidad al boton buscar y formato
     */
    public void buscarButtonfun(){
        bttBuscar.setGraphic(new ImageView(buscar));
        bttAgregar.setGraphic(new ImageView(agregar));
        bttBuscar.setContentDisplay(ContentDisplay.LEFT);
        bttBuscar.setOnAction(evneto->{
            buscar();
        });
    }
    
    /**************************************************************************/
    //Agregar quitar pacientes
    ObservableList<String> lisSexo = FXCollections.observableArrayList
        ("Masculino","Femenino");
    
    private Button bttagregar = new Button("Aceptar");
    
    private Label txtid_paciente = new Label();
    private TextField txtnombre_paciente = new TextField();
    private TextField txtapellido_paciente = new TextField();
    private TextField txtapMaterno_paciente = new TextField();
    private ComboBox<String> txtsexo_paciente = new ComboBox<>(lisSexo);
    private DatePicker txtfechaNacimiento_paciente = new DatePicker(LocalDate.now());
    private TextField txtcurp_paciente = new TextField();
    private Label txtedad_paciente = new Label();
    private TextField txttelefono_paciente = new TextField();
    private TextField txtcorreo_paciente = new TextField();
    
    private Label lbid_paciente = new Label("Numero de identificación");
    private Label lbnombre_paciente = new Label("Nombre");
    private Label lbapellido_paciente = new Label("Apellido paterno");
    private Label lbapMaterno_paciente = new Label("Apellido materno");
    private Label lbsexo_paciente = new Label("Sexo");
    private Label lbfechaNacimiento_paciente = new Label("Fecha de nacimiento");
    private Label lbcurp_paciente = new Label("CURP");
    private Label lbedad_paciente = new Label("Edad");
    private Label lbtelefono_paciente = new Label("Teléfono");
    private Label lbcorreo_paciente = new Label("Correo electrónico");
    
    public void agregaPaciente(){
        GridPane gridPane = new GridPane();
        gridPane.addRow(0, lbid_paciente, txtid_paciente);
        gridPane.addRow(1, lbnombre_paciente, txtnombre_paciente);
        gridPane.addRow(2, lbapellido_paciente, txtapellido_paciente);
        gridPane.addRow(3, lbapMaterno_paciente, txtapMaterno_paciente);
        gridPane.addRow(4, lbsexo_paciente, txtsexo_paciente);
        gridPane.addRow(5, lbfechaNacimiento_paciente, txtfechaNacimiento_paciente);
        gridPane.addRow(6, lbcurp_paciente, txtcurp_paciente);
        gridPane.addRow(7, lbedad_paciente, txtedad_paciente);
        gridPane.addRow(8, lbtelefono_paciente, txttelefono_paciente);
        gridPane.addRow(9, lbcorreo_paciente,txtcorreo_paciente);
        gridPane.add(bttagregar,0,10,2,1);
        
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        
        funcionBotones();
        formatoPop();
        
        borderPane.setRight(gridPane);
        borderPane.setMargin(gridPane, new Insets(10));
    }
    
    /**
     * Crea funcionalidad de botones
     */
    public void funcionBotones(){
        bttagregar.setGraphic(new ImageView(guardar));
        bttagregar.setPrefSize(150, 30);
        bttagregar.setAlignment(Pos.CENTER);
        bttagregar.setOnAction(evento->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (!udateNote) {
                        guardarPaciente(conex);
                        clean();
                        borderPane.setRight(null);
                    }else{
                        actualizaPaciente(conex);
                        clean();
                        borderPane.setRight(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * formatos y toltips
     */
    public void formatoPop(){
        //Comprobar TextField
        txtnombre_paciente.setTextFormatter(new TextFormatter(formato    (30, 4)));
        txtapellido_paciente.setTextFormatter(new TextFormatter(formato  (30, 4)));
        txtapMaterno_paciente.setTextFormatter(new TextFormatter(formato (30, 4)));
        txtcurp_paciente.setTextFormatter(new TextFormatter(formato      (30, 4)));
        txttelefono_paciente.setTextFormatter(new TextFormatter(formato  (30, 3)));
        txtcorreo_paciente.setTextFormatter(new TextFormatter(formato    (30, 4)));
        
        //DatePiCker
        datePickerFormato();
        
        //toltip
        aux.toolTip(txttelefono_paciente, "Campo opcional");
        aux.toolTip(txtcorreo_paciente, "Campo opcional");
        aux.toolTip(txtapMaterno_paciente, "Campo opcional");
        
        //label
        txtid_paciente.setText(aux.generaID());
        txtedad_paciente.setText(aux.edadConMes(Date.valueOf(LocalDate.now())));
        
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaTexField(txtnombre_paciente, "Nombre Paciente");
        errorMessage += aux.verificaTexField(txtapellido_paciente, "Apellido Paciente");
        errorMessage += aux.verificaTexField(txtcurp_paciente, "CURP");
        errorMessage += aux.verificaValufield(txtsexo_paciente, "Sexo");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * Formato al datepicker
     */
    public void datePickerFormato(){
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty){
                super.updateItem(item, empty);
                if(item.isAfter(LocalDate.now())){
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
            }
        };
        txtfechaNacimiento_paciente.setDayCellFactory(dayCellFactory);
        
        txtfechaNacimiento_paciente.valueProperty().addListener((ez,v,n)->{
            txtedad_paciente.setText(aux.edadConMes(Date.valueOf(n)));
        });
    }
    
    /**
     * guarda el paciente
     * @param conex
     */
    public void guardarPaciente(Connection conex){
        String id_paciente = txtid_paciente.getText();
        String nombre_paciente = txtnombre_paciente.getText();
        String apellido_paciente = txtapellido_paciente.getText();
        String apMaterno_paciente = txtapMaterno_paciente.getText();
        String sexo_paciente = txtsexo_paciente.getValue();
        Date fechaNacimiento_paciente = Date.valueOf(txtfechaNacimiento_paciente.getValue());
        String curp_paciente = txtcurp_paciente.getText();
        int edad_paciente = aux.edadNumerico(fechaNacimiento_paciente.toLocalDate());
        String telefono_paciente = txttelefono_paciente.getText();
        String correo_paciente  = txtcorreo_paciente.getText();
        
        pacc.agregarPacientes(id_paciente, nombre_paciente, 
                apellido_paciente, apMaterno_paciente, sexo_paciente, 
                fechaNacimiento_paciente, curp_paciente, edad_paciente, 
                telefono_paciente, correo_paciente, conex);
        
        IniciaTabla(pacc.buscaCURP(conex, curp_paciente));
    }
    
    /**
     * modifica el paciente en la base de datos
     * @param conex 
     */
    public void actualizaPaciente(Connection conex){
        String id_paciente = txtid_paciente.getText();
        String nombre_paciente = txtnombre_paciente.getText();
        String apellido_paciente = txtapellido_paciente.getText();
        String apMaterno_paciente = txtapMaterno_paciente.getText();
        String sexo_paciente = txtsexo_paciente.getValue();
        Date fechaNacimiento_paciente = Date.valueOf(txtfechaNacimiento_paciente.getValue());
        String curp_paciente = txtcurp_paciente.getText();
        int edad_paciente = aux.edadNumerico(fechaNacimiento_paciente.toLocalDate());
        String telefono_paciente = txttelefono_paciente.getText();
        String correo_paciente  = txtcorreo_paciente.getText();
        
        pacc.udatePaciente(id_paciente, nombre_paciente, 
                apellido_paciente, apMaterno_paciente, sexo_paciente, 
                fechaNacimiento_paciente, curp_paciente, edad_paciente, 
                telefono_paciente, correo_paciente, conex);
        
        IniciaTabla(pacc.buscaCURP(conex, curp_paciente));
        
        udateNote = false;
    }
    
    /**
     * Carga los datos del paciente
     * @param pac 
     */
    public void cargar(Paciente pac){
        agregaPaciente();
        txtid_paciente.setText(pac.getId_paciente());
        txtnombre_paciente.setText(pac.getNombre_paciente());
        txtapellido_paciente.setText(pac.getApellido_paciente());
        txtapMaterno_paciente.setText(pac.getApMaterno_paciente());
        txtsexo_paciente.setValue(pac.getSexo_paciente());
        txtfechaNacimiento_paciente.setValue(pac.getFechaNacimiento_paciente().toLocalDate());
        txtcurp_paciente.setText(pac.getCurp_paciente());
        txtedad_paciente.setText(aux.edadConMes(pac.getFechaNacimiento_paciente()));
        txttelefono_paciente.setText(pac.getTelefono_paciente());
        txtcorreo_paciente.setText(pac.getCorreo_paciente());
        udateNote = true;
    }
    
    /**
     * Modificar el paciente
     */
    @FXML
    public void modificaPaciente(){
        Paciente paciente = tbPaciente.getSelectionModel().getSelectedItem();
        if (paciente != null) {
            cargar(paciente);
        }else{
            aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                    "Se debe selecciona un paciente para ser modificado");
        }
    }
    
    /**
     * Borra el paciente
     */
    public void borrarPaciente(){
        Paciente paciente = tbPaciente.getSelectionModel().getSelectedItem();
        if (paciente != null) {
            try(Connection conex = dbConn.conectarBD()) {
                pacc.borrarPaciente(paciente.getId_paciente(), conex);
                IniciaTabla(pacc.buscaCURP(conex, " "));
                PrincipalController.pacienteAUsar = null;
                PrincipalController.nombrePaciente.setValue(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            aux.alertaError("Selecciona un paciente", "Selecciona un paciente", 
                    "Se debe selecciona un paciente para ser borrardo");
        }
    }
    
    
    /**
     * Formato de los texfield a x Characteres
     * @param nivel
     * @param marcador
     * @return formateo de 10
     */
    public UnaryOperator<TextFormatter.Change> formato(int nivel, int marcador){
        UnaryOperator<TextFormatter.Change> modifyChange = (cambio) -> {
            if (cambio.isContentChange()) {
                int newLength = cambio.getControlNewText().length();
                
                switch(marcador){
                    case 1:
                        cambio.setText(cambio.getText().replaceAll("[^a-zA-Z\\s]", ""));
                        break;
                    case 2:
                        cambio.setText(cambio.getText().replaceAll("[^a-zA-Z]", ""));
                        break;
                    case 3:
                        cambio.setText(cambio.getText().replaceAll("[^0-9]", ""));
                        break;
                    case 4:
                        cambio.setText(cambio.getText().replaceAll("[^a-zA-Z0-9ñÑ\\-\\s\\\\\\\\!\\\"#$%&()*+,./:;<=>?@\\\\[\\\\]^_{|}~]+", ""));
                        break;
                }
                
                if (newLength > nivel) {
                    cambio.setText("");
                    aux.alertaError("Solo pude tener "+nivel+" caracteres", 
                                "Solo pude tener "+nivel+" caracteres", 
                                "Este campo solo pude tener "+nivel+" caracteres");
                }
        }
            return cambio;
        };
        return modifyChange;
    }
    
    /**
     * limpia los datos
     */
    public void clean(){
        txtnombre_paciente.setText("");
        txtapellido_paciente.setText("");
        txtapMaterno_paciente.setText("");
        txtsexo_paciente.setValue(null);
        txtfechaNacimiento_paciente.setValue(LocalDate.now());
        txtcurp_paciente.setText("");
        txtedad_paciente.setText(aux.edadConMes(Date.valueOf(LocalDate.now())));
        txttelefono_paciente.setText("");
        txtcorreo_paciente.setText("");
    }
    
}
