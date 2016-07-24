/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author Alejandro Ortiz Corro
 */

public class CodigoJuego {
    
    public void rotarALGO(Node node){
        RotateTransition rotation = new RotateTransition(Duration.seconds(0.5), node);
        rotation.setCycleCount(1);
        rotation.setByAngle(360);
        node.setOnMouseEntered(e -> rotation.play());
        node.setOnMouseExited(e -> rotation.pause());
    }
    

}
