/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Consulta;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Somametropia;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class NotaMedicosController implements Initializable {

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
        ejecutorDeServicio();
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

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    Somametropia somametropia = new Somametropia();
    Somametropia soma;
    //Variables de guardado
    String id_soma;
    int peso_soma = 0;
    int talla_soma = 0;
    int glucosa_soma;
    int sistolica_soma;
    int diastolica_soma;
    int frecCardia_soma;
    int frecRespiratoria_soma;
    int temperatura_soma;
    String id_paciente;
    
    //Conexion
    Hikari dbConn = new Hikari();
    @FXML private AnchorPane anchorPane;
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbEdad;
    @FXML private Label lbCURP;
    @FXML private Label lbSexo;
    @FXML private Label lbIdMedico;
    //FXML texfield
    @FXML private TextField txtPeso;
    @FXML private TextField txtTalla;
    @FXML private TextField txtGlucosa;
    @FXML private TextField txtSistolica;
    @FXML private TextField txtDistolica;
    @FXML private TextField txtCardiaca;
    @FXML private TextField txtRespiratoria;
    @FXML private TextField txtTemperatura;
    @FXML private Button    bttAceptar;
    @FXML private TextArea txaMotivo;
    @FXML private TextArea txaExplora;
    //Labels
    @FXML private Label lbPeso;
    @FXML private Label lbTalla;
    @FXML private Label lbGlucosa;
    @FXML private Label lbSistolica;
    @FXML private Label lbDistolica;
    @FXML private Label lbCardiaca;
    @FXML private Label lbRespiratorioa;
    @FXML private Label lbTemperatura;
    
    //Actualizar si o no
    private boolean actulizar = false;
    private String motivo;
    private String exploracion;
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
     * metodo para el botton on action
     */
    private void acetarContinuaORgresa(){
        Task<Void> task = new Task<Void>() {
           @Override
           protected Void call() throws Exception {
                return null;
           }
       };
        
        task.setOnSucceeded((evento)->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (actulizar) {
                        actualizaSoma(conex);
                        cordi.lanzaNotaMedicaSegundaParte(paci, txaMotivo.getText(), txaExplora.getText());
                    }else{
                        guardaSoma(conex);
                        cordi.lanzaNotaMedicaSegundaParte(paci, txaMotivo.getText(), txaExplora.getText());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        //Maouse en modo esperar
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        
        dbExeccutor.submit(task);
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAceptar.setOnAction(eventon->{
            acetarContinuaORgresa();
        });
        bttAceptar.setGraphic(new ImageView(aceptar));
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        txtPeso.setTextFormatter(new TextFormatter(aux.formato(3, 3)));
        txtTalla.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtGlucosa.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtSistolica.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtDistolica.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtCardiaca.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtRespiratoria.setTextFormatter(new TextFormatter (aux.formato(3, 3)));
        txtTemperatura.setTextFormatter(new TextFormatter (aux.formato(2, 3)));
        txtTemperatura.setTextFormatter(new TextFormatter (aux.formato(2, 3)));
        txaExplora.setTextFormatter(new TextFormatter (aux.formato(400, 4)));
        txaMotivo.setTextFormatter(new TextFormatter (aux.formato(1500, 4)));
        
        
        txtPeso.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                peso_soma = Integer.valueOf(nuevo);
                if (peso_soma != 0 && talla_soma != 0) {
                    lbPeso.setText(aux.resultadoImc(peso_soma, talla_soma));
                    lbTalla.setText(aux.imc(peso_soma, talla_soma));
                }else{
                    lbPeso.setText("");
                    lbTalla.setText("");
                }
                    
            }
        });
        
        txtTalla.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                talla_soma = Integer.valueOf(nuevo);
                if (peso_soma != 0 && talla_soma != 0) {
                    lbPeso.setText(aux.resultadoImc(peso_soma, talla_soma));
                    lbTalla.setText(aux.imc(peso_soma, talla_soma));
                }else{
                    lbPeso.setText("");
                    lbTalla.setText("");
                }
            }
        });
        
        txtGlucosa.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                glucosa_soma = Integer.valueOf(nuevo);
                formatoLabels(lbGlucosa, 110, glucosa_soma, "Hiperglucemia");
            }
        });
        
        txtGlucosa.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                glucosa_soma = Integer.valueOf(nuevo);
                formatoLabels(lbGlucosa, 110, glucosa_soma, "Hiperglucemia");
            }
        });
        
        txtSistolica.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                sistolica_soma = Integer.valueOf(nuevo);
                if (sistolica_soma > 250) {
                    aux.alertaError("Valor incorrecto", "Valor incorrecto", 
                            "La tensión sistólica no debe ser mayor a 250mmHg");
                    txtSistolica.setText("250");
                }
                formatoLabels(lbSistolica, 129, sistolica_soma, "Prob. Hipertensión");
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
                formatoLabels(lbDistolica, 84, diastolica_soma, "Prob. Hipertensión");
            }
        });
        
        txtCardiaca.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                frecCardia_soma = Integer.valueOf(nuevo);
                formatoLabels(lbCardiaca, 100, frecCardia_soma, "Prob. taquicardia");
            }
        });
        
        txtRespiratoria.textProperty().addListener((observable, viejo, nuevo)->{
            if (!nuevo.equals("")) {
                frecRespiratoria_soma = Integer.valueOf(nuevo);
                formatoLabels(lbRespiratorioa, 20, frecRespiratoria_soma, "Prob. taquipnea");
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
                formatoLabels(lbTemperatura, 37, temperatura_soma, "Hipertermia");
            }
        });
        
        txaMotivo.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.TAB)) {
                    Node node = (Node) event.getSource();
                    if (node instanceof TextField) {
                        TextFieldSkin skin = (TextFieldSkin) ((TextField)node).getSkin();
                        if (event.isShiftDown()) {
                            skin.getBehavior().traversePrevious();
                        }
                        else {
                            skin.getBehavior().traverseNext();
                        }               
                    }
                    else if (node instanceof TextArea) {
                        TextAreaSkin skin = (TextAreaSkin) ((TextArea)node).getSkin();
                        if (event.isShiftDown()) {
                            skin.getBehavior().traversePrevious();
                        }
                        else {
                            skin.getBehavior().traverseNext();
                        }
                    }
                    event.consume();
                }
            }
        });
        
        txaExplora.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.TAB)) {
                    Node node = (Node) event.getSource();
                    if (node instanceof TextField) {
                        TextFieldSkin skin = (TextFieldSkin) ((TextField)node).getSkin();
                        if (event.isControlDown()) {
                             skin.getBehavior().callAction("InsertTab");
                         }else{
                            if (event.isShiftDown()) {
                            skin.getBehavior().traversePrevious();
                            }
                            else {
                                skin.getBehavior().traverseNext();
                            }           
                        }
                            
                    }
                    else if (node instanceof TextArea) {
                        TextAreaSkin skin = (TextAreaSkin) ((TextArea)node).getSkin();
                         if (event.isControlDown()) {
                             skin.getBehavior().callAction("InsertTab");
                         }else{
                             if (event.isShiftDown()) {
                                skin.getBehavior().traversePrevious();
                            }
                            else {
                                skin.getBehavior().traverseNext();
                            }
                         }
                    }
                    event.consume();
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
        errorMessage += aux.verificaTexField(txtGlucosa, "Glucosa");
        errorMessage += aux.verificaTexField(txtSistolica, "TA sistólica");
        errorMessage += aux.verificaTexField(txtDistolica, "TA diastólica");
        errorMessage += aux.verificaTexField(txtCardiaca, "Frecuencia cardíaca");
        errorMessage += aux.verificaTexField(txtRespiratoria, "Frecuencia respiratoria");
        errorMessage += aux.verificaTexField(txtTemperatura, "Temperatura");
        errorMessage += aux.verificaTexField(txaExplora, "Exploración física");
        errorMessage += aux.verificaTexField(txaMotivo, "Padecimiento actual");
       
        
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
        glucosa_soma = Integer.valueOf(txtGlucosa.getText());;
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
            txtGlucosa.setText(String.valueOf(somi.getGlucosa_soma()));
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
        glucosa_soma = Integer.valueOf(txtGlucosa.getText());
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
     * limpia los labels
     */
    private void limpiaLabels(){
        lbPeso.setText("");
        lbTalla.setText("");
        lbGlucosa.setText("");
        lbSistolica.setText("");
        lbDistolica.setText("");
        lbCardiaca.setText("");
        lbRespiratorioa.setText("");
        lbTemperatura.setText("");
    }
    
    /**
     * metodo para mostrar los labels adecuados
     * @param label
     * @param limite
     * @param valorActual
     * @param texto 
     */
    private void formatoLabels(Label label, int limite, int valorActual, String texto){
        if (valorActual > limite) {
            label.setText(texto);
        }else
            label.setText("");
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formato botton guardar
        formatoBotones();
        //formato de los texbox
        formatoDeText();
        //Limpia labels
        limpiaLabels();
    } 
}
