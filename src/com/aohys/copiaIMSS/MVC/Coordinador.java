/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC;


import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Alejandro Ortiz Corro
 */

public class Coordinador extends Application {

    //Variables
        private Stage primaryStage;
        private BorderPane rootLayout;
        private AnchorPane anchor;
 
        PrincipalController controller = new PrincipalController();
    
    //Conexion
        Vitro dbConn = new Vitro();
        
    /**
     * Lanza la aplicacion
     * @param primaryStage
     *  
     */
    @Override
        public void start(Stage primaryStage) throws Exception{
            // Inicializa la escena
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Login");
            this.primaryStage.getIcons().add(
                    new Image("file:src/com/aohys/copiaIMSS/Utilidades/Imagenes/Minerva.jpg"));
            this.primaryStage.centerOnScreen();
            //Lanza login
            entrarUsuario();
           
            creaDirectorios();
        }

    /**
     * Lanza Todo
     * @param args the command line arguments
     */
        public static void main(String[] args) {
            launch(args);
        }
        
    private void creaDirectorios(){
        new File(System.getenv("AppData")+"/AO Hys/Estudios").mkdirs();
        new File(System.getenv("AppData")+"/AO Hys/Historiales").mkdirs();
        new File(System.getenv("AppData")+"/AO Hys/Laboratoriales").mkdirs();
        new File(System.getenv("AppData")+"/AO Hys/NotasMedicas").mkdirs();
        new File(System.getenv("AppData")+"/AO Hys/Recetas").mkdirs();
    }
        
    /**
    *Lanza escena login
    */
    public void iniLogin(){
        try {
            // Carga el loader.
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Principal/Principal.fxml"));
            stage.setTitle("Sistema De Control Y Organización De Consulta Médica");
            stage.getIcons().add(
                    new Image("file:src/com/aohys/copiaIMSS/Utilidades/Imagenes/Minerva.jpg"));
            rootLayout = (BorderPane) loader.load();
            // Muestra la escena.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            // Da acceso al programa principal.
            PrincipalController controller = loader.getController();
            controller.pasoPrincipal(this, stage);
            stage.centerOnScreen();
            stage.setMaximized(false);
            // Muesta la escena,
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
         
    }
    
    /**
     * Carga el entrar de usuario
     */
    public void entrarUsuario(){
        try {
            // Carga el loader.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Coordinador.class.getResource(
                    "VistaControlador/Principal/Ingreso.fxml"));
            anchor = (AnchorPane) loader.load();
            // Muestra la escena.
            
            Scene scene = new Scene(anchor);
            primaryStage.setScene(scene);
            // Da acceso al programa principal.
            IngresoController controller = loader.getController();
            controller.pasoPrincipal(this, primaryStage);
            primaryStage.centerOnScreen();
            primaryStage.setMaximized(false);
            // Muesta la escena,
            primaryStage.show();
            try(Connection conex = dbConn.conectarBD()) {
                Eliminar("rhuts", conex);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
       
    }
    
    /**
     * Crea la conexion Pool al principio
     * @param ID
     * @param conex 
     */    
    public void Eliminar(String ID, Connection conex){
        String sqlst =  "SELECT\n" +
                        "a.id_bh  \n" +
                        "FROM\n" +
                        "biometriahematica a \n" +
                        "WHERE\n" +
                        "a.id_bh = ''";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst);
                ResultSet res = sttm.executeQuery()) {
            while (res.next()) {
                System.out.println(res.getString("id_log"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            AlertaError("Error de conexión.  ", 
                        "Error de conexión. ", 
                        "No se puso establecer conexión con la base de datos");
        }
    }

    /**
     * Mensaje de herror
     * @param cabeza
     * @param Cabezota
     * @param mensaje 
    */
    public void AlertaError(String cabeza, String Cabezota, String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src//HE//Utilidades//Iconos//Logo.png"));
        alert.setTitle(cabeza);
        alert.setHeaderText(Cabezota);
        alert.setContentText(mensaje);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        }
    }
    
}