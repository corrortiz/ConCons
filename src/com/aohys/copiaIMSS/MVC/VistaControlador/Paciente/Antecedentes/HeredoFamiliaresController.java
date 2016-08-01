/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.ant_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.masAnt_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class HeredoFamiliaresController implements Initializable {

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
        // formato combobox
        formatoRadio();
        try(Connection conex = dbConn.conectarBD()) {
            ante = masAnteHere.cargaSoloUno(paci.getId_paciente(), conex);
            formatoTabla(conex);
            cargaMasAnte(conex);
            limpiaDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    ant_Heredo_Familiar antHere = new ant_Heredo_Familiar();
    masAnt_Heredo_Familiar masAnteHere = new masAnt_Heredo_Familiar();
    masAnt_Heredo_Familiar ante;
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //Heditario y familiares
    @FXML private ComboBox<String> cbbFamInfor;
    @FXML private ComboBox<String> cbbFamResponsable;
    @FXML private ComboBox<String> cbbPadecimeinto;
    @FXML private ComboBox<String> cbbFamPadeciente;
    @FXML private RadioButton rbPadecimiento;
    @FXML private RadioButton rbOtro;
    @FXML private CheckBox chbFinado;
    @FXML private Button bttAgregarPadecimiento;
    //Tabla
    @FXML private TableView<ant_Heredo_Familiar> tvAnteHeredo;
    @FXML private TableColumn<ant_Heredo_Familiar, String> colPadecimiento;
    @FXML private TableColumn<ant_Heredo_Familiar, String> colFamiliar;
    @FXML private TableColumn<ant_Heredo_Familiar, String> colFinado;
    //Parte inferior
    @FXML private RadioButton rbDisfuncionSi;
    @FXML private RadioButton rbDisfuncionNo;
    @FXML private Button bttAceptarInferior;
    
    //Lista para combobox
    ObservableList<String> listaFamiliares = FXCollections.observableArrayList();
    ObservableList<String> listaPadecimientos = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean finitoSiNo = false;
    private boolean DisfuncionSiNo = false;
    private boolean actualizaSiNoMasAnte = false;
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
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
    }    
    
    /**
     * crea las listas de los combo box
     */
    public void creaListas(){
        listaFamiliares.addAll("Abuela materna", "Abuela paterna","Abuelo materno","Abuelo paterno","Cónyuge",
                                "El paciente","Hermana","Hermano","Hija","Hijo","Madre","Ninguno","Otro",
                                "Padre","Tía materna","Tía paterna","Tío materno","Tío paterno");

        listaPadecimientos.addAll(  "Diabetes mellitus","Hipertensión Arterial","Tuberculosis Pulmonar",
                                    "Obesidad","Neoplasias","Cardiopatías","Alergias","Tabaquismo",
                                    "Dependencia a drogas o medicamentos","Malformaciones congénitas",
                                    "Sin antecedentes");
    }
    
    /**
     * carga los datos del combobox
     */
    public void formatoComboBox(){
        cbbFamInfor.setItems(listaFamiliares);
        cbbFamResponsable.setItems(listaFamiliares);
        cbbPadecimeinto.setItems(listaPadecimientos);
        cbbFamPadeciente.setItems(listaFamiliares);
    }
    
    /**
     * formato de radio buttons
     */
    private void formatoRadio(){
        ToggleGroup primeraVez = new ToggleGroup();
        primeraVez.getToggles().addAll(rbPadecimiento,rbOtro);
        rbPadecimiento.setUserData(true);
        rbOtro.setUserData(false);
        primeraVez.selectedToggleProperty().addListener((valor,v,n)->{
            if (n!=null) {
                if (!Boolean.valueOf(n.getUserData().toString())) {
                    cbbPadecimeinto.setEditable(true);
                    cbbPadecimeinto.setValue(null);
                }else{
                    cbbPadecimeinto.setEditable(false);
                    cbbPadecimeinto.setValue(null);
                }
            }
        });
        rbPadecimiento.setSelected(true);
        
        
        ToggleGroup disfami = new ToggleGroup();
        disfami.getToggles().addAll(rbDisfuncionSi, rbDisfuncionNo);
        rbDisfuncionSi.setUserData(true);
        rbDisfuncionNo.setUserData(false);
        rbDisfuncionNo.setSelected(true);
        disfami.selectedToggleProperty().addListener((valor,v,n)->{
            if (Boolean.valueOf(n.getUserData().toString())) {
                DisfuncionSiNo = true;
            }else{
                DisfuncionSiNo = false;
            }
                
        });
        
    }
    
    /**
     * formato del checbox
     */
    private void formatoChecBox(){
        chbFinado.selectedProperty().addListener((observable, viejo, nuevo)->{
            if (chbFinado.isSelected()) {
                finitoSiNo = true;
            }else
                finitoSiNo = false;
        });
    }
    
    /**
     * guarda los datos del padecimiento del paciente
     * @param conex 
     */
    private void guardarPadecimiento(Connection conex){
        String id_antHeredo = aux.generaID();
        String padecimiento_antHeredo = cbbPadecimeinto.getValue();
        String familiares_antHeredo = cbbFamPadeciente.getValue();
        String id_paciente = paci.getId_paciente();
        antHere.agregarPadecimiento(id_antHeredo, 
                padecimiento_antHeredo, familiares_antHeredo, 
                finitoSiNo, id_paciente, conex);
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAgregarPadecimiento.setOnAction(evento->{
            if (continuaSINOagregar()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardarPadecimiento(conex);
                    formatoTabla(conex);
                    limpiaDatos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        bttAceptarInferior.setOnAction(envento->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (actualizaSiNoMasAnte) {
                        actualizaMasAntecedente(conex);
                        aux.informacionUs("Antecedentes heredo-familiares modificados", "Antecedentes heredo-familiares modificados"
                                , "Los antecedentes heredo-familiares han sido modificados con éxito en la base de datos");
                        cordi.lanzaHistoriaMedica(paci);
                    }else{
                        guardaMasAnteHere(conex);
                        aux.informacionUs("Antecedentes heredo-familiares guardados", "Antecedentes heredo-familiares guardados"
                                , "Los antecedentes heredo-familiares han sido guardados con éxito en la base de datos");
                        cordi.lanzaHistoriaMedica(paci);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        bttAceptarInferior.setGraphic(new ImageView(aceptar));
        bttAgregarPadecimiento.setGraphic(new ImageView(guardar));
    }
    
    /**
     * crea y actualiza la tabla segun sea necesario
     * @param conex 
     */
    private void formatoTabla(Connection conex){
        colPadecimiento.setCellValueFactory(new PropertyValueFactory<>  ("padecimiento_antHeredo"));
        colFamiliar.setCellValueFactory(new PropertyValueFactory<>  ("familiares_antHeredo"));
        colFinado.setCellValueFactory(cellData -> {
            ant_Heredo_Familiar ant = cellData.getValue();
            String regresaColumna;
            if (ant.getFinado_antHeredo()) {
                regresaColumna = "SI";
            }else
                regresaColumna = "NO";
            return new ReadOnlyStringWrapper(regresaColumna);
        });
        tvAnteHeredo.setItems(antHere.listaPadecimientosAnte(conex, paci.getId_paciente()));
    }
    
    /**
     * borra el procedimiento seleccionado
     */
    @FXML
    private void borrar(){
         ant_Heredo_Familiar ant = tvAnteHeredo.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.BorrarPadecimiento(ant.getId_antHeredo(), conex);
                 formatoTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona un procedimiento", "Selecciona un procedimiento", 
                     "Es necesario seleccionar un procedimiento para ser borrado;");
    }
    
    /**
     * limpia datos de los combos despues de guardar
     */
    private void limpiaDatos(){
        cbbFamPadeciente.setValue(null);
        cbbPadecimeinto.setValue(null);
        chbFinado.setSelected(false);
    }
    
    /**
     * guarda mas antecedentes herederitarios
     * @param conex 
     */
    private void guardaMasAnteHere(Connection conex){
        String id_masHeredo = aux.generaID();
        String familiarResp_masHeredo = cbbFamResponsable.getValue();
        String id_paciente = paci.getId_paciente();
        String familiarInfor_masHeredo = cbbFamInfor.getValue();
        masAnteHere.agregarMasHeredoFami(id_masHeredo, familiarResp_masHeredo, 
                DisfuncionSiNo, id_paciente, conex, familiarInfor_masHeredo);
    }
    
    /**
     * carga los antecedentes del paciente seleccionado
     * @param conex 
     */
    private void cargaMasAnte(Connection conex){
        if (ante != null) {
            cbbFamInfor.setValue(ante.getFamiliarInfor_masHeredo());
            cbbFamResponsable.setValue(ante.getFamiliarResp_masHeredo());
            if (ante.getDisfuncion_masHeredo()) {
                rbDisfuncionSi.fire();
            }else
                rbDisfuncionNo.fire();
            actualizaSiNoMasAnte = true;
        }
    }
    
    /**
     * actualiza mas antecedentes
     * @param conex 
     */
    private void actualizaMasAntecedente(Connection conex){
        String id_masHeredo = ante.getId_masHeredo();
        String familiarResp_masHeredo = cbbFamResponsable.getValue();
        String id_paciente = paci.getId_paciente();
        String familiarInfor_masHeredo = cbbFamInfor.getValue();
        masAnteHere.actualizaMasAnteHeredo(id_masHeredo, familiarResp_masHeredo, 
                DisfuncionSiNo, id_paciente, conex, familiarInfor_masHeredo);
    }
    
    /**
     * verifica los requisitos 
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbFamInfor, "Familiar informante");
        errorMessage += aux.verificaValufield(cbbFamResponsable, "Familiar responsable del paciente");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * verifica requsitos de lo tros
     * @return 
     */
    private boolean continuaSINOagregar(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbPadecimeinto, "Padecimiento");
        errorMessage += aux.verificaValufield(cbbFamPadeciente, "Familiar");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formatolistas
        creaListas();
        //formato combobox
        formatoComboBox();
        //formato checbox
        formatoChecBox();
        //formato botton guardar
        formatoBotones();
    }    
    
   
}
