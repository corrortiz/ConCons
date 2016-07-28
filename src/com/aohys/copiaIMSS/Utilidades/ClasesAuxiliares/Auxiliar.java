/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares;


import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * @author Alejandro Ortiz Corro
 */

public class Auxiliar {
    /**
     * Genera un id a lo pendejo
     * @return 
     */
    public String generaID(){
        String uuid = UUID.randomUUID().toString();
        String ID= uuid.substring(0, 5).toUpperCase();
        return ID;
    }
    
    /**
     * Calcula la edad del datePicker
     * @param honomastico
     * @return 
     */
    public String edad(LocalDate honomastico){
        LocalDate start = honomastico;
        String años;
        LocalDate end = LocalDate.now();
        long years = ChronoUnit.YEARS.between(start, end);
        long dias = ChronoUnit.DAYS.between(start, end);
        long meses =  ChronoUnit.MONTHS.between(start, end);
        if (years < 0 || meses < 0 || dias < 0) {
            años = "Fecha Invalida";
        }else if (years == 0) {
            if (meses == 0) {
                if (dias > 31) {
                    años = "1 Mes";
                }else
                    años = Long.toString(dias)+" Dias";
            }else if (meses == 1) {
                años = Long.toString(meses)+" Mes";
            }else
                años = Long.toString(meses)+" Meses";
        }else
            if (years == 1) {
                años = "1 Año";
            }else
                años = Long.toString(years)+ " Años";
        return años;
    }
    
    /**
     * Calcula la edad de la persona
     * @param honomastico
     * @return 
     */
    public int edadNumerico(LocalDate honomastico){
        LocalDate start = honomastico;
        LocalDate end = LocalDate.now();
        long years = ChronoUnit.YEARS.between(start, end);
        return (int)years;
    }

    /**
     * Calcula el IMC
     * @param masa
     * @param altura
     * @return 
     */
    public String imc(float masa, float altura){
        float resulato = (masa / ((altura/100)*(altura/100)));
        String imc=null;
        return imc.format("IMC:  %.2f",resulato);
    }
    
    /**
     * resultado ima
     * @param masa
     * @param altura
     * @return 
     */
    public String resultadoImc(float masa, float altura){
        float imc = (masa / ((altura/100)*(altura/100)));
        String resultado;
        if (imc < 16 ) {
            resultado = "Delgadez Severa";
        }else if (imc >= 16 && imc <= 16.999) {
            resultado = "Delgadez moderada";
        }else if (imc >= 17 && imc <= 18.499) {
            resultado = "Delgadez aceptable";
        }else if (imc >= 18.5 && imc <= 24.999) {
            resultado = "Normal";
        }else if (imc >= 25 && imc <= 29.999) {
            resultado = "Sobrepeso";
        }else if (imc >= 30 && imc <= 34.999) {
            resultado = "Obesidad 1";
        }else if (imc >= 35 && imc <= 40) {
            resultado = "Obesidad 2";
        }else{
            resultado = "Obesidad 3";
        }
        return resultado;
    }
    
    /**
     * Calcula el Indice de Lindegaar
     * @param vacm
     * @param vaci
     * @return 
     */
    public String il(float vacm, float vaci){
        float resulato = ( vacm / vaci);
        String imc=null;
        return imc.format("%.1f",resulato);
    }
    
    
    /**
     * Cargado de peso Ideal
     * @param sexo
     * @param altura
     * @return 
     */
    public String pesoIdeal(String sexo, float altura){
        String pi;
        float ext; 
        if (sexo.equals("Masculino")) {
            ext = (float) (((0.91)*(altura-152.4))+(50));
            pi = Float.toString(ext);
        }else{
            ext = (float) (((0.67)*(altura-152.4))+(45.5) );
            pi = Float.toString(ext);
        }
        return pi.format("%.2f",ext); 
    }
    
    /**
     * Cambia tamaño nodos
     * @param node 
     */
    public void scalado(Node node){
        node.setOnMouseEntered((evento)->{
            node.toFront();
            int lol = 1;
            for (int i = 1; i < 1000; i++) {
                node.setScaleX(lol+.09);
                node.setScaleY(lol+.09);
            }
            
        });
        node.setOnMouseExited((evento)->{
            node.setScaleX(1);
            node.setScaleY(1);
        });
    }
    
    /**
     * Crea un toltip al lado del nodo y dice lo que quieras
     * @param node
     * @param contenido 
     */
    public void toolTip(Node node, String contenido){
        Tooltip tp = new Tooltip(contenido);
        tp.setWrapText(true);
        
        node.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (newValue) {
                Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
                tp.show(  node,
                            boundsInScreen.getMinX()+boundsInScreen.getWidth()+10, //
                            boundsInScreen.getMinY());
            }else
                tp.hide();
        }); 
    }
    
    /**
     * crea un toltip del nodo en el lado superior 
     * @param node
     * @param contenido 
     */
    public void toolTipSuperior(Node node, String contenido){
        Tooltip tp = new Tooltip(contenido);
        tp.setWrapText(true);
        
        node.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (newValue) {
                Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
                tp.show(  node,
                            boundsInScreen.getMinX(), //
                            boundsInScreen.getMinY()-65f);
                
            }else
                tp.hide();
        }); 
    }
    
    
    
    
    
    
    /**
     * Alerta Error
     * @param cabeza
     * @param Cabezota
     * @param mensaje 
     */
    public void alertaError(String cabeza, String Cabezota, String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/com/aohys/copiaIMSS/Utilidades/Imagenes/Minerva.jpg"));
        alert.setTitle(cabeza);
        alert.setHeaderText(Cabezota);
        alert.setContentText(mensaje);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("file:src/com/aohys/copiaIMSS/Utilidades/CSSstyle/MantoEstelar.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        }
    }
    
    /**
     * Alerta usuario 
     * @param cabeza
     * @param Cabezota
     * @param mensaje 
     */
    public void informacionUs(String cabeza, String Cabezota, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/com/aohys/copiaIMSS/Utilidades/Imagenes/Minerva.jpg"));
        alert.setTitle(cabeza);
        alert.setHeaderText(Cabezota);
        alert.setContentText(mensaje);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("file:src/com/aohys/copiaIMSS/Utilidades/CSSstyle/MantoEstelar.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        }
        
    }

    /**
     * Carga edad con mes
     * @param edad
     * @return 
     */
    public String edadConMes(Date edad) {
        LocalDate start = edad.toLocalDate();
        String años;
        LocalDate end = LocalDate.now();
        int entre = end.getYear() - start.getYear();
        LocalDate endr;
        if (entre == 1) {
            endr = edad.toLocalDate().plusYears(1);
        }else
            endr = start.plusYears(entre-1);
        long years = ChronoUnit.YEARS.between(start, end);
        long dias = ChronoUnit.DAYS.between(start, end);
        long meses =  ChronoUnit.MONTHS.between(start, end);
        long entreCumple =  ChronoUnit.MONTHS.between(endr, end);
        if (years < 0 || meses < 0 || dias < 0) {
            años = "Fecha Invalida";
        }else if (years == 0) {
            if (meses == 0) {
                if (dias > 31) {
                    años = "1 Mes";
                }else
                    años = Long.toString(dias)+" Dias";
            }else if (meses == 1) {
                años = Long.toString(meses)+" Mes";
            }else
                años = Long.toString(meses)+" Meses";
        }else
            if (years == 1) {
                if(entreCumple == 0){
                    años = "1 año";
                }else
                    if (entreCumple == 1) {
                        años = "1 año "+Long.toString(entreCumple)+" mes";
                    }else
                        años = "1 año "+Long.toString(entreCumple)+" meses";
            }else{
                if (entreCumple == 12) {
                    años = Long.toString(years)+ " años";
                }else
                    if (entreCumple == 1) {
                        años = Long.toString(years)+ " años " +Long.toString(entreCumple)+" mes";
                    }else
                        años = Long.toString(years)+ " años " +Long.toString(entreCumple)+" meses";
            }
                
        return años;
    }
    
   
    /**
     * Efecto de transicion 
     * @param view
     * @param stackPane
     * @return 
     */
    public boolean setScreen(Parent view, StackPane stackPane) {       
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
    
    
    /**
     * Cargar formatos
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
                        cambio.setText(cambio.getText().replaceAll("[^a-zA-Z0-9ñÑ\\-\\s\\\\\\\\!\\\"#$%&()*+,./:;<=>?@\\\\[\\\\]^_{|}~]+", ""));
                        break;
                }
                if (newLength > nivel) {
                    cambio.setText("");
                    alertaError("Solo pude tener "+nivel+" caracteres", 
                                "Solo pude tener "+nivel+" caracteres", 
                                "Este campo solo pude tener "+nivel+" caracteres");
                }
        }
            return cambio;
        };
        
        return modifyChange;
    }
    
    /**
     * regresa string texfield
     * @param txtNode
     * @param nombreCampo
     * @return 
     */
    public String verificaTexField(TextInputControl txtNode, String nombreCampo){
        String errorMessage = "";
        if (txtNode.getText() == null || txtNode.getText().length() == 0) {
            txtNode.requestFocus();
            errorMessage += ""+nombreCampo+" !\n"; 
        }
        return errorMessage;
    }
    
    /**
     * verifica las otras clases 
     * @param valueNode
     * @param nombreCampo
     * @return 
     */
    public String verificaValufield(ComboBoxBase valueNode, String nombreCampo){
        String errorMessage = "";
        if (valueNode.getValue() == null || valueNode.getValue().toString().length() == 0) {
            valueNode.requestFocus();
            errorMessage += nombreCampo+" !\n"; 
        }
        return errorMessage;
    }
    
    
    /**
     * Crea un combobox con filtro para buscar contenidos dentro de su lista
     * @param valueNode
     * @param lista 
     */
    public void comboboxFiltro(ComboBox valueNode, ObservableList lista){
        AutoCompletionBinding<String> autoComplete;
        valueNode.getItems().clear();
        valueNode.setItems(lista);
        valueNode.setEditable(true);
        autoComplete = TextFields.bindAutoCompletion(valueNode.getEditor(), valueNode.getItems());
        autoComplete.setPrefWidth(1200);
    }
    
}
