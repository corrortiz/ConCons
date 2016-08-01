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
import com.aohys.copiaIMSS.MVC.Modelo.Somametropia;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class SomatometríaController implements Initializable {

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
        try(Connection conex = dbConn.conectarBD()) {
            soma = somametropia.cargaSoloUno(paci.getId_paciente(), conex);
            cargaComponentes(soma);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    Somametropia somametropia = new Somametropia();
    Somametropia soma;
    //Variables de guardado
    String id_soma;
    int peso_soma;
    int talla_soma;
    int glucosa_soma;
    int sistolica_soma;
    int diastolica_soma;
    int frecCardia_soma;
    int frecRespiratoria_soma;
    int temperatura_soma;
    String id_paciente;
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML texfield
    @FXML private ImageView imageView;
    @FXML private TextField txtPeso;
    @FXML private TextField txtTalla;
    @FXML private TextField txtSistolica;
    @FXML private TextField txtDistolica;
    @FXML private TextField txtCardiaca;
    @FXML private TextField txtRespiratoria;
    @FXML private TextField txtTemperatura;
    @FXML private Button    bttAceptar;
    //Actualizar si o no
    private boolean actulizar = false;
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
     * formato de imagen
     */
    private void formatoImagen(){
        Image image = new Image
            ("com/aohys/copiaIMSS/Utilidades/Imagenes/davinci.gif");
        imageView.setImage(image);
        bttAceptar.setGraphic(new ImageView(guardar));
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAceptar.setOnAction(eventon->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (actulizar) {
                        actualizaSoma(conex);
                        aux.informacionUs("Somatometría actualizada", "Somatometría actualizada", 
                                "Somatometría actualizada exitosamente en la base de datos");
                        cordi.lanzaHistoriaMedica(paci);
                    }else{
                        guardaSoma(conex);
                        aux.informacionUs("Somatometría guardada", "Somatometría guardada", 
                                "Somatometría guardada exitosamente en la base de datos");
                        cordi.lanzaHistoriaMedica(paci);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        txtPeso.setTextFormatter(new TextFormatter(aux.formato(3, 3)));
        txtTalla.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtSistolica.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtDistolica.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtCardiaca.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtRespiratoria.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtTemperatura.setTextFormatter(new TextFormatter (aux.formato(2, 3)));
        
        
        txtSistolica.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                sistolica_soma = Integer.valueOf(nuevo);
                if (sistolica_soma>250) {
                    aux.alertaError("Valor incorrecto", "Valor incorrecto", 
                            "La tensión sistólica no debe ser mayor a 250mmHg");
                    txtSistolica.setText("250");
                }
            }
        });
        
        txtDistolica.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                diastolica_soma = Integer.valueOf(nuevo);
                if (diastolica_soma > 200) {
                    aux.alertaError("Valor incorrecto", "Valor incorrecto", 
                            "La tensión diastólica no debe ser mayor a 200mmHg");
                    txtDistolica.setText("200");
                }
            }
        });
        
        txtTemperatura.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                temperatura_soma = Integer.valueOf(nuevo);
                if (temperatura_soma > 43) {
                    aux.alertaError("Valor incorrecto", "Valor incorrecto", 
                            "La temperatura no debe ser mayor a 43°C");
                    txtTemperatura.setText("43");
                }
            }
        });
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaTexField(txtPeso, "Peso");
        errorMessage += aux.verificaTexField(txtTalla, "Talla");
        errorMessage += aux.verificaTexField(txtSistolica, "TA sistólica");
        errorMessage += aux.verificaTexField(txtDistolica, "TA diastólica");
        errorMessage += aux.verificaTexField(txtCardiaca, "Frecuencia cardíaca");
        errorMessage += aux.verificaTexField(txtRespiratoria, "Frecuencia respiratoria");
        errorMessage += aux.verificaTexField(txtTemperatura, "Temperatura");
       
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * guarda somarhut
     * @param conex 
     */
    private void guardaSoma(Connection conex){
        id_soma = aux.generaID();
        peso_soma = Integer.valueOf(txtPeso.getText());
        talla_soma = Integer.valueOf(txtTalla.getText());
        glucosa_soma = 0;
        sistolica_soma = Integer.valueOf(txtSistolica.getText());
        diastolica_soma = Integer.valueOf(txtDistolica.getText());
        frecCardia_soma = Integer.valueOf(txtCardiaca.getText());
        frecRespiratoria_soma = Integer.valueOf(txtRespiratoria.getText());;
        temperatura_soma = Integer.valueOf(txtTemperatura.getText());
        String id_paciente = paci.getId_paciente();
        
        somametropia.agregaSomametro(id_soma, peso_soma, talla_soma, glucosa_soma, sistolica_soma, 
                diastolica_soma, frecCardia_soma, frecRespiratoria_soma, temperatura_soma, id_paciente, conex);
    }
   
    /**
     * carga componentes de los textfields si existe datos del paciente
     * @param somi 
     */
    private void cargaComponentes(Somametropia somi){
        if (somi!=null) {
            txtPeso.setText(String.valueOf(somi.getPeso_soma()));
            txtTalla.setText(String.valueOf(somi.getTalla_soma()));
            txtSistolica.setText(String.valueOf(somi.getSistolica_soma()));
            txtDistolica.setText(String.valueOf(somi.getDiastolica_soma()));
            txtCardiaca.setText(String.valueOf(somi.getFrecCardia_soma()));
            txtRespiratoria.setText(String.valueOf(somi.getFrecRespiratoria_soma()));
            txtTemperatura.setText(String.valueOf(somi.getTemperatura_soma()));
            actulizar = true;
        }
    }
    
    /**
     * actualiza los datos de somatometria  
     * @param conex 
     */
    private void actualizaSoma(Connection conex){
        id_soma = soma.getId_soma();
        peso_soma = Integer.valueOf(txtPeso.getText());
        talla_soma = Integer.valueOf(txtTalla.getText());
        glucosa_soma = soma.getGlucosa_soma();
        sistolica_soma = Integer.valueOf(txtSistolica.getText());
        diastolica_soma = Integer.valueOf(txtDistolica.getText());
        frecCardia_soma = Integer.valueOf(txtCardiaca.getText());
        frecRespiratoria_soma = Integer.valueOf(txtRespiratoria.getText());;
        temperatura_soma = Integer.valueOf(txtTemperatura.getText());
        String id_paciente = paci.getId_paciente();
        
        somametropia.actualizaSomatometria(id_soma, peso_soma, talla_soma, glucosa_soma, sistolica_soma, 
                diastolica_soma, frecCardia_soma, frecRespiratoria_soma, temperatura_soma, id_paciente, conex);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formato botton guardar
        formatoBotones();
        //Le da formato a los imagenes
        formatoImagen();
        //formato de los texbox
        formatoDeText();
    } 
    
}
