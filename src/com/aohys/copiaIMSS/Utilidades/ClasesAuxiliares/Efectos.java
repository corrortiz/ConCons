/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.rehabSys.Utilidades.ClasesAuxiliares;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * @author Alejandro Ortiz Corro
 */

public class Efectos implements Initializable{

    //FXML de efecto
    @FXML private HBox hBox;
    @FXML private HBox hBoxOcualta;
    @FXML private BorderPane borderPane;
    
    //Altrua del efecto 
    private Double HboxAltura = 0.0d;
    
    @FXML
    public void animacionMuestra(MouseEvent eve){
        hBox.setPrefHeight(0.0d);
        borderPane.setTop(hBox);
        
        Timeline timeline = new Timeline();
        
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(hBox.prefHeightProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300.0d), 
                    new KeyValue(hBox.prefHeightProperty(), HboxAltura))
        );
        timeline.play();
    }
    
    
    @FXML
    public void animacionOculta(MouseEvent eve){
        Timeline timeline = new Timeline();
        
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, 
                    new KeyValue(hBox.prefHeightProperty(), HboxAltura)
                ),
                new KeyFrame(Duration.millis(300.0d), 
                    new KeyValue(hBox.prefHeightProperty(), 0)
                )
        );
        timeline.play();
        
        timeline.setOnFinished((evento)->{
            borderPane.setTop(hBoxOcualta);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
                //Inicia el efecto
        HboxAltura = hBox.getPrefHeight();
        borderPane.setTop(null);
        borderPane.setTop(hBoxOcualta);
    }
    
}
