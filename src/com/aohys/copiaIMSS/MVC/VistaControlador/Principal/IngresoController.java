/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Principal;


import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Coordinador;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class IngresoController implements Initializable {
    //Variables de escena
    private Coordinador cordi;
    private Usuario usuario = new Usuario();
    static public Usuario usua;
    private Stage stage;

    //Base de datos
    Vitro dbConn = new Vitro();
    
    /**
     * Inicia la esecena 
     * @param cordi
     * @param stage 
     */
    public void pasoPrincipal(Coordinador cordi, Stage stage) {
        this.cordi = cordi;
        this.stage = stage;
        jugantoTrans();
    }
    
    private void jugantoTrans(){
        Text msg = new Text("JavaFX animation is cool!");
        msg.setTextOrigin(VPos.TOP);
        msg.setFont(Font.font(24));
        vBox.getChildren().add(msg);
        double sceneWidth = stage.getScene().getWidth();
        double msgWidth = msg.getLayoutBounds().getWidth();
        KeyValue initKeyValue =
                new KeyValue(msg.translateXProperty(), sceneWidth);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        KeyValue endKeyValue =
                new KeyValue(msg.translateXProperty(), -1.0 * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(3), endKeyValue);
        Timeline timeline = new Timeline(endFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.setRate(3);
        timeline.play();
    }
    
    
    //Variables a utilizar
    Usuario med = new Usuario();
    Auxiliar aux = new Auxiliar();
    @FXML private VBox vBox;
    /**
     * Regresa un medico
     * @return 
     */
    public Usuario regresaMedico(){
        Usuario medicoR = med.CargaSoloUno(txtUSuario.getText());
        return medicoR;
    }
    
    //Botones FXML
    @FXML private Button bttAceptar;
    @FXML private TextField txtUSuario;
    @FXML private PasswordField pswContraseña;
    @FXML private ImageView imageView;
    
    Image img = new Image("com/aohys/copiaIMSS/Utilidades/Imagenes/ingreso.png");
    
        
    /**
     * Agrega las instrucciones
     */
    public void toltiP(){
        aux.toolTip(txtUSuario,     "Ingresa el Usuario");
        aux.toolTip(pswContraseña,  "Ingresa la Contraseña");
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicia los campos
        txtUSuario.setTextFormatter(new TextFormatter(formato       (10, 4)));
        pswContraseña.setTextFormatter(new TextFormatter(formato    (10, 4)));
        
        imageView.setImage(img);
        
        // On action de los botones
        bttAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                if (esValido()) {
                    try(Connection conex = dbConn.conectarBD()) {
                        if (med.verificarContraseña(txtUSuario.getText(), pswContraseña.getText(),conex)) {
                            usua = regresaMedico();
                            cordi.iniLogin();
                            stage.hide();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        //Carga las instrucciones
        toltiP();
    }    

    /**
     * Da formato a los campos
     * @param nivel
     * @param marcador
     * @return 
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
                        cambio.setText(cambio.getText().replaceAll("[^a-zA-Z0-9\\s\\\\\\\\!\\\"#$%&()*+,./:;<=>?@\\\\[\\\\]^_{|}~]+", ""));
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
     * Comprueba los campos
     * @return si estan los campos o no
     */
    public boolean esValido(){
        String errorMessage = "";

        if (txtUSuario.getText() == null || txtUSuario.getText().length() == 0) {
            txtUSuario.requestFocus();
            errorMessage += "Usuario!\n"; 
        }
        if (pswContraseña.getText() == null || pswContraseña.getText().length() == 0) {
            pswContraseña.requestFocus();
            errorMessage += "Contraseña!\n"; 
        }
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            alert.setTitle("Campos  Vacíos");
            alert.setHeaderText("Agregue los siguientes campos:");
            alert.setContentText(errorMessage);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            }
            return false;
        }
    }
    
}
