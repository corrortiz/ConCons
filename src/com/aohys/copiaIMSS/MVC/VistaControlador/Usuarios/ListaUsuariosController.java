/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Usuarios;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ListaUsuariosController implements Initializable {

    //Variables de escena
    private PrincipalController cordi;
    private Usuario usa = new Usuario();
    private Usuario paraCargar;
    Auxiliar aux = new Auxiliar();
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(PrincipalController cordi) {
        this.cordi = cordi;
        try(Connection conex = dbConn.conectarBD()) {
            iniciaTabla(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Lista usuarios
    ObservableList<Usuario> listaMedicos = FXCollections.observableArrayList();
    
    //Objetos tabla
    @FXML private TableView<Usuario> tablaMed;

    @FXML private TableColumn<Usuario,String> clmnNombre_medico;
    @FXML private TableColumn<Usuario,String> clmnCedulaProfecional_medico;
    @FXML private TableColumn<Usuario,String> clmnEspecialidad_medico;
    @FXML private TableColumn<Usuario,String> clmnTelefono_medico;
    @FXML private TableColumn<Usuario,String> clmnCorreo_medico;
    @FXML private TableColumn<Usuario,String> clmnTipo_medico;
    
    //FXML buscar
    @FXML private TextField txtFiltrar;
    @FXML private StackPane stackPane;
 
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    /**
     * Inicia la tabla
     */
    public void iniciaTabla(Connection conex){
        clmnTelefono_medico.setCellValueFactory     (new PropertyValueFactory<>("telefono_medico"));
        clmnCorreo_medico.setCellValueFactory       (new PropertyValueFactory<>("correo_medico"));
        clmnTipo_medico.setCellValueFactory       (new PropertyValueFactory<>("tipo_medico"));
        
        clmnNombre_medico.setCellValueFactory(cellData -> {
            Usuario p = cellData.getValue();
            String regresaColumna = p.getNombre_medico()+" "+p.getApellido_medico()+" "+p.getApMaterno_medico();
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        
        clmnEspecialidad_medico.setCellValueFactory(cellData -> {
            Usuario p = cellData.getValue();
            String regresaColumna;
            if (!p.getTipo_medico().equals("Medico")) {
                regresaColumna = "N/A";
            }else
                regresaColumna = p.getEspecialidad_medico();
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        
        clmnCedulaProfecional_medico.setCellValueFactory(cellData -> {
            Usuario p = cellData.getValue();
            String regresaColumna;
            if (!p.getTipo_medico().equals("Medico")) {
                regresaColumna = "N/A";
            }else
                regresaColumna = p.getCedulaProfecional_medico();
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        
        tablaMed.setItems(usa.cargaTabla(conex));
        listaMedicos = tablaMed.getItems();
        
        FilteredList<Usuario> filtro = new FilteredList<>(listaMedicos, cambio -> true);
        txtFiltrar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtro.setPredicate(us -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (us.getTipo_medico().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (clmnNombre_medico.getCellData(us).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (clmnEspecialidad_medico.getCellData(us).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }else if (clmnCedulaProfecional_medico.getCellData(us).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Usuario> sortedData = new SortedList<>(filtro);
        
        sortedData.comparatorProperty().bind(tablaMed.comparatorProperty());
        tablaMed.setItems(sortedData);
    }
        
    /**
    * Initializes the controller class.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciaLadoIzq();
        
        tablaMed.getSelectionModel().selectedItemProperty().addListener(
            ( mm , old, nuevo)->{
                if (nuevo!=null) {
                    cargaDatosLabel(nuevo);
                }
        });
    }    
    
    /**
     * manda a editar la agenda si es un nuevo medico
     */
    private void lanzaEditarAgenda(){
        if (paraCargar != null) {
            if (paraCargar.getTipo_medico().equals("Medico")) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Coordinador.class.getResource(
                            "VistaControlador/Usuarios/ConfigurarAgenda.fxml"));
                    AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
                    ConfigurarAgendaController controller = loader.getController();
                    controller.transmisor(this, paraCargar, cordi);
                    setScreen(unoAnchorPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    
    /************************************************************************************/
    private Usuario medico = new Usuario();
    private boolean estabien = false;
    
    //Observable List
    private ObservableList<String> tipoMed = FXCollections.observableArrayList();
    
    //Objetos FXML
    @FXML private Button bttGuardar;
    
    //txfields
    @FXML private TextField     txtusuario;
    @FXML private TextField     txtnombre;
    @FXML private TextField     txtpaterno;
    @FXML private TextField     txtmaterno;
    @FXML private TextField     txtcedula;
    @FXML private TextField     txtespecilidad;
    @FXML private TextField     txttelefono;
    @FXML private TextField     txtcorreo;
    @FXML private PasswordField pswcontraseña;
    @FXML private PasswordField pswcontraseña2;
    @FXML private ComboBox<String> cbbTipo;

    /**
     * Limpiar los txfields
     */
    public void limpiarTXT(){
        txtusuario.clear();
        txtnombre.clear();
        txtpaterno.clear();
        txtmaterno.clear();
        txtcedula.clear();
        txtespecilidad.clear();
        txttelefono.clear();
        txtcorreo.clear();
        pswcontraseña.clear();
        pswcontraseña2.clear();
    }
    
    /**
     * Agarra datos
     * @param conex
     * @return 
     */
    public boolean obtenerDatos(Connection conex){
        if (esValido()) {
            String usuario =        txtusuario.getText();
            String nombre =         txtnombre.getText();
            String paterno =        txtpaterno.getText();
            String materno =        txtmaterno.getText();
            String cedula =         txtcedula.getText();
            String especialidad =   txtespecilidad.getText();
            String telefono =       txttelefono.getText();
            String correo =         txtcorreo.getText();
            String contraseña =     pswcontraseña.getText();
            String contraseña1 =    pswcontraseña2.getText();
            String tipo =           cbbTipo.getValue();
            
            if (!medico.verificarID(usuario, conex)) {
                    medico.agregarMedico(  usuario, contraseña, nombre, 
                                        paterno, materno, cedula, 
                                        especialidad, telefono, correo, tipo, conex);
                    limpiarTXT();
                estabien = true;
                   paraCargar = new Usuario(usuario, contraseña, nombre, 
                                        paterno, materno, cedula, 
                                        especialidad, telefono, correo, tipo);
            }else{
                aux.alertaError("ID de Usuario Duplicado",
                                "ID de Usuario Duplicado", 
                                "El ID del usuario ya ha sido "
                            +   "utilizado antes y no es posible volver a utilizarlo");
                txtusuario.requestFocus();
                estabien = false;
            }
        }
        return estabien;
    }
    
    /**
     * Carga los datos del medico que se va a modificar
     * @param usaa
     */
    public void cargaDatos(Usuario usaa){
        txtusuario.setText      (usaa.getId_medico());
        txtnombre.setText       (usaa.getNombre_medico());
        txtpaterno.setText      (usaa.getApellido_medico());
        txtmaterno.setText      (usaa.getApMaterno_medico());
        txtcedula.setText       (usaa.getCedulaProfecional_medico());
        txtespecilidad.setText  (usaa.getEspecialidad_medico());
        txttelefono.setText     (usaa.getTelefono_medico());
        txtcorreo.setText       (usaa.getCorreo_medico());
        pswcontraseña.setText   (usaa.getContraseña_medico());
        pswcontraseña2.setText  (usaa.getContraseña_medico());
        cbbTipo.setValue        (usaa.getTipo_medico());
    }
    
    /**
     * Modificar el usuario
     */
    public void modificaUsuario(){
        Usuario usuario = tablaMed.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            cargaDatos(usuario);
            medico = usuario;
            estabien = true;
        }else{
            aux.alertaError("Selecciona un usuario", "Selecciona un usuario", 
                    "Se debe seleccionar un usuario para ser modificado");
        }
    }
    
    /**
     * Borrar de el usuario seleccionado
     */
    public void borraUsuario(){
        Usuario usuario = tablaMed.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            try(Connection conex = dbConn.conectarBD()) {
                limpiaLB();
                usuario.BorrarMedico(usuario.getId_medico(), conex);
                iniciaTabla(conex); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            aux.alertaError("Selecciona un usuario", "Selecciona un usuario", 
                    "Se debe seleccionar un usuario para ser borrado");
        }
    }

    /**
     * Actualiza Datos
     * @param conex
     * @return 
     */
    public boolean udateDatos(Connection conex){
        if (esValido()) {
            String usuario =        txtusuario.getText();
            String nombre =         txtnombre.getText();
            String paterno =        txtpaterno.getText();
            String materno =        txtmaterno.getText();
            String cedula =         txtcedula.getText();
            String especialidad =   txtespecilidad.getText();
            String telefono =       txttelefono.getText();
            String correo =         txtcorreo.getText();
            String contraseña =     pswcontraseña.getText();
            String idAborrar=       medico.getId_medico();
            String tipo =           cbbTipo.getValue();

            medico.udateMedico
                (   usuario, contraseña, nombre, 
                    paterno, materno, cedula, 
                    especialidad, telefono, correo, idAborrar, tipo, conex);
            paraCargar = new Usuario(usuario, contraseña, nombre, 
                                        paterno, materno, cedula, 
                                        especialidad, telefono, correo, tipo);
            limpiarTXT();
        }
        return true;
    }
    
    /**
     * Initializes the controller class.
     */
    public void iniciaLadoIzq(){
        //Comprobar TextField
        txtusuario.setTextFormatter(new TextFormatter(formato       (10, 4)));
        txtnombre.setTextFormatter(new TextFormatter(formato        (30, 4)));
        txtpaterno.setTextFormatter(new TextFormatter(formato       (30, 4)));
        txtmaterno.setTextFormatter(new TextFormatter(formato       (30, 4)));
        txtcedula.setTextFormatter(new TextFormatter(formato        (20, 4)));
        txtespecilidad.setTextFormatter(new TextFormatter(formato   (30, 4)));
        txttelefono.setTextFormatter(new TextFormatter(formato      (10, 3)));
        txtcorreo.setTextFormatter(new TextFormatter(formato        (100, 4)));
        pswcontraseña.setTextFormatter(new TextFormatter(formato    (10, 4)));
        pswcontraseña2.setTextFormatter(new TextFormatter(formato   (10, 4)));
        
        //toltip
        aux.toolTip(txttelefono, "Campo numérico");
        aux.toolTip(pswcontraseña2, "Repite la contraseña");
        aux.toolTip(txtmaterno, "Campo opcional");
        
        //Carga Combo
        cbbTipo.setItems(cargandoListas());
        
        //Imagen botton
        bttGuardar.setGraphic(new ImageView(agregar));
        
        // On action Botones
        bttGuardar.setOnAction((evento)->{
            try(Connection conex = dbConn.conectarBD()) {
                if (estabien) {
                    if (udateDatos(conex)){
                        iniciaTabla(conex);
                        estabien = false;
                        lanzaEditarAgenda();
                    }
                }else{
                    if (obtenerDatos(conex)) {
                        iniciaTabla(conex);
                        lanzaEditarAgenda();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
    }    
    
    /**
     * Formato de los texfield a x Characteres
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
     * Verifica es los campos
     * @return 
     */
    public boolean esValido(){
        String errorMessage = "";

        if (txtusuario.getText() == null || txtusuario.getText().length() == 0) {
            txtusuario.requestFocus();
            errorMessage += "Usuario!\n"; 
        }
        if (txtnombre.getText() == null || txtnombre.getText().length() == 0) {
            txtnombre.requestFocus();
            errorMessage += "Nombre!\n"; 
        }
        if (txtpaterno.getText() == null || txtpaterno.getText().length() == 0) {
            txtpaterno.requestFocus();
            errorMessage += "Apellido Paterno!\n"; 
        }

        if (txtcedula.getText() == null || txtcedula.getText().length() == 0) {
            txtcedula.requestFocus();
            errorMessage += "Cedula!\n"; 
        }

        if (txtespecilidad.getText() == null || txtespecilidad.getText().length() == 0) {
            txtespecilidad.requestFocus();
            errorMessage += "Especialidad!\n"; 
        }
        
        if (txttelefono.getText() == null || txttelefono.getText().length() == 0) {
            txttelefono.requestFocus();
            errorMessage += "Telefono!\n"; 
        }

        if (txtcorreo.getText() == null || txtcorreo.getText().length() == 0) {
            txtcorreo.requestFocus();
            errorMessage += "Correo!\n";
        }
        
        if (pswcontraseña.getText() == null || pswcontraseña.getText().length() == 0) {
            pswcontraseña.requestFocus();
            errorMessage += "Contraseña!\n";
        }
        
        if (cbbTipo.getValue() == null || cbbTipo.getValue().length() == 0) {
            cbbTipo.requestFocus();
            errorMessage += "Tipo de Usuario!\n";
        }
        
        if (pswcontraseña2.getText() == null || pswcontraseña2.getText().length() == 0) {
            pswcontraseña2.requestFocus();
            errorMessage += "Confirmacion de contraseña!\n";
        }
        
        if (!pswcontraseña.getText().equals(pswcontraseña2.getText())) {
            pswcontraseña.requestFocus();
            errorMessage += "las contraseñas no concuerdan";
        }
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos vacíos", 
                    "Campos vacíos", "Agregue los siguientes campos:"+errorMessage);
            return false;
        }
    }
    
    /**
     * Tipos de Estados Civil
     * @return Lista de tipos de estado civil
     */
    public ObservableList<String> cargandoListas(){
        tipoMed.addAll( "Medico",
                        "Asistente", 
                        "Laboratorio");
        return tipoMed;
    }
    
    
    /**********************************************/
    //txfields
    @FXML private Label lbusuario;
    @FXML private Label lbtnombre;
    @FXML private Label lbtcedula;
    @FXML private Label lbtespecilidad;
    @FXML private Label lbttelefono;
    @FXML private Label lbtcorreo;
    @FXML private Label lbtTipo;
    
    
    /**
     * Cargar datos en las labels
     * @param usaa 
     */
    public void cargaDatosLabel(Usuario usaa){
        lbusuario.setText(usaa.getId_medico());
        lbtnombre.setText(usaa.getNombre_medico()+" "+usaa.getApellido_medico()+" "+usaa.getApMaterno_medico());
        lbtcedula.setText(usaa.getCedulaProfecional_medico());
        lbtespecilidad.setText(usaa.getEspecialidad_medico());
        lbttelefono.setText(usaa.getTelefono_medico());
        lbtcorreo.setText(usaa.getCorreo_medico());
        lbtTipo.setText(usaa.getTipo_medico());
    }
    
    /**
     * limpia los labels antes del borrado
     */
    private void limpiaLB(){
        lbusuario.setText("");
        lbtnombre.setText("");
        lbtcedula.setText("");  
        lbtespecilidad.setText("");
        lbttelefono.setText("");
        lbtcorreo.setText("");
        lbtTipo.setText("");
    }
    
    /**
     * agrega configuarcion consulta 
     */
    @FXML
    public void lanzaConfiguracionAgenda(){
        Usuario usuario = tablaMed.getSelectionModel().getSelectedItem();
        if (usuario.getTipo_medico().equals("Medico")) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Coordinador.class.getResource(
                        "VistaControlador/Usuarios/ConfigurarAgenda.fxml"));
                AnchorPane  unoAnchorPane = (AnchorPane) loader.load();
                ConfigurarAgendaController controller = loader.getController();
                controller.transmisor(this, usuario, cordi);
                setScreen(unoAnchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            aux.alertaError("Selecciona un medico", "Selecciona un medico", 
                    "Se debe seleccionar un medico para poder configurar su agenda");
        
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
                   new KeyFrame(new Duration(250), new EventHandler<ActionEvent>() {
                       @Override
                       public void handle(ActionEvent t) {
                           stackPane.getChildren().remove(0);        //remove the displayed screen
                           stackPane.getChildren().add(0, view);     //add the screen
                           Timeline fadeIn = new Timeline(
                                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                           fadeIn.play();
                       }
                   }, new KeyValue(opacity, 0.0)));
           fade.play();
       } else {
           stackPane.setOpacity(0.0);
           stackPane.getChildren().add(view);       //no one else been displayed, then just show
           Timeline fadeIn = new Timeline(
                   new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                   new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
           fadeIn.play();
       }
       return true;
   }
}
