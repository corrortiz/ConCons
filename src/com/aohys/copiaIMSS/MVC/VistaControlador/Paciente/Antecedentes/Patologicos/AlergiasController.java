/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.Patologicos;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAlergias;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.AntecedentesMédicosController;
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
public class AlergiasController implements Initializable {

    //Variables de escena
    private AntecedentesMédicosController cordi;
    Paciente paci;
    
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(AntecedentesMédicosController cordi) {
        this.cordi = cordi;
        this.paci = PrincipalController.pacienteAUsar;
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
    patoAlergias patoAlergias = new patoAlergias();
    
    //Conexion
    Hikari dbConn = new Hikari();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML padecimientos previos
    @FXML private RadioButton rbMedicamentos;
    @FXML private RadioButton rbOtros;
    @FXML private ComboBox<String> cbbDescripcion;
    @FXML private Button bttAgregar;
    //FXMl de tabla
    @FXML private TableView<patoAlergias> tvAlergias;
    @FXML private TableColumn<patoAlergias, String> colMedicaOtros;
    @FXML private TableColumn<patoAlergias, String> colDescripcion;
    
    //Lista para combobox
    ObservableList<String> listaMedicamentos = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean medicamentosOtros = true;
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
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
     * verifica los requisitos 
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbDescripcion, "Descripción");
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * formato de veces a la edad
     */
    private void formatoCombobox(){
        listaMedicamentos.addAll("PENICILINA","METAMIZOL","LIDOCAINA","SULFAS", "HIERRO","ÁCIDO FÓLICO","AMINOGLUCÓSIDOS",
                                "TETRACICLINA","CLORAMFENICOL","ANFOTERICINAS","IPATROPIO","ONDANSETRON","CIPROFLOXACINA",
                                "CLINDAMICINA","CEFALOSPORINA","PRIMAQUINA","CLORAQUINA","NISTATINA","CEFALOSPORINAS",
                                "BORTEZOMIB","ABACAVIR LAMIVU","ETRAVIRINA","DIDANOSINA","GALSULFASA","DARUNAVIR",
                                "AMOXICILINA","NEOMICINA","ETARNECEPT","HIDROXICOBALAMI");
        cbbDescripcion.setItems(listaMedicamentos);
    }
    
    /**
     * radios de buttons formatos
     */
    private void formatoRadioButtons(){
        ToggleGroup seleccion = new ToggleGroup();
        seleccion.getToggles().addAll(rbMedicamentos,rbOtros);
        rbMedicamentos.fire();
        seleccion.selectedToggleProperty().addListener((onservable,viejo,nuevo)->{
            if (rbOtros.isSelected()) {
                cbbDescripcion.setEditable(true);
                medicamentosOtros = false;
            }else{
                cbbDescripcion.setEditable(false);
                medicamentosOtros = true;
            }
        });
    }
    
    /**
     * le da formato a los bottones
     */
    private void formatoButtons(){
        bttAgregar.setOnAction((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardaAnteAlerfia(conex);
                    actualizaTabla(conex);
                    limpiaCuadros();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        bttAgregar.setGraphic(new ImageView(guardar));
    }
    
    /**
     * agrega los antencedentes medicos
     * @param conex 
     */
    private void guardaAnteAlerfia(Connection conex){
        String id_pAler = aux.generaID();
        boolean medOtros_pAler = medicamentosOtros;
        String descripcion_pAler = cbbDescripcion.getValue();
        String id_paciente = paci.getId_paciente();
        patoAlergias.agregaPatolergia(id_pAler, medOtros_pAler, descripcion_pAler, id_paciente, conex);
    }
    
    /**
     * actualiza la tabla 
     * @param conex 
     */
    private void actualizaTabla(Connection conex){
        colDescripcion.setCellValueFactory(new PropertyValueFactory<> ("descripcion_pAler"));
        colMedicaOtros.setCellValueFactory(informacionDeCelda->{
            patoAlergias p = informacionDeCelda.getValue();
            String regresaFormato;
            if (p.getMedOtros_pAler()) {
                regresaFormato = "Medicamento";
            }else
                regresaFormato = "Otros";
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        tvAlergias.setItems(patoAlergias.listaAntePaAlergias(conex, paci.getId_paciente()));
        
        colDescripcion.prefWidthProperty().bind(
            tvAlergias.widthProperty()
            .subtract(colMedicaOtros.widthProperty())
            .subtract(3)
            );
    }
    
    /**
     * limpia cuadros 
     */
    private void limpiaCuadros(){
        cbbDescripcion.setValue(null);
    }
    
    /**
     * borra el padecimiento selecionado
     */
    @FXML
    private void borrar(){
         patoAlergias ant = tvAlergias.getSelectionModel().getSelectedItem();
         if (ant != null) {
             try(Connection conex = dbConn.conectarBD()) {
                 ant.borrarAntePatoAlergias(ant.getId_pAler(), conex);
                 actualizaTabla(conex);
             } catch (SQLException e) {
                 e.printStackTrace();
             }
        }else
             aux.alertaError("Selecciona una alergia", "Selecciona una alergia", 
                     "Es necesario seleccionar una alergia para ser borrada");
    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //formato del combobox
      formatoCombobox();
      //formato de los radio buttons
      formatoRadioButtons();
      //formato de botones
      formatoButtons();
    } 
      
    
}
