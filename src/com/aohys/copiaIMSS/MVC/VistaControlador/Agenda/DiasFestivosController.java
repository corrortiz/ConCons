/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Agenda;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloCita.DiasFestivos.listaDiasFestivosTask;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class DiasFestivosController implements Initializable {
//Variables de escena
    private PrincipalController cordi;
    DiasFestivos diasFes = new DiasFestivos();
    Auxiliar aux = new Auxiliar();
    
    //Base de datos
    Hikari dbConn = new Hikari();
    
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void pasoPrincipal(PrincipalController cordi) {
        this.cordi = cordi;
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
    
    //FXML 
    @FXML private DatePicker dpFecha;
    
    //Tabla
    @FXML private TableView<DiasFestivos> tvFechas;
    @FXML private TableColumn<DiasFestivos, Date> clmFeche;
    ObservableList<DiasFestivos> listDiasFest = FXCollections.observableArrayList();
    //Guarda
    @FXML private Button bttGuardar;
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    
    @FXML
    public void borrar(){
        DiasFestivos modulito = tvFechas.getSelectionModel().getSelectedItem();
        if (modulito != null) {
            try(Connection conex = dbConn.conectarBD()) {
                modulito.eliminar(modulito.getId_DiasFes(), conex);
                aux.informacionUs("Festivo borrado", "Festivo borrado", 
                        "El Festivo ha sido borrado de la base de datos exitosamente");
                listDiasFest.remove(modulito);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            aux.alertaError("Selecciona un festivo", "Selecciona un festivo", 
                    "Se debe seleccionar un festivo para ser borrado");
        }
    }
    
    /**
     * Guardar el festivo
     * @param conex 
     */
    public void guardar(Connection conex){
        Date festivo = Date.valueOf(dpFecha.getValue());
        diasFes.agregaDiaFes(aux.generaID(), festivo, conex);
        actualizaListadiasFestivos();
    }

    /**
     * actualiza la lista de los dias festivos 
     */
    private void actualizaListadiasFestivos(){
        listaDiasFestivosTask task = diasFes.new listaDiasFestivosTask();
        task.setOnSucceeded(evento->{
            listDiasFest.clear();
            listDiasFest.addAll(task.getValue());
        });
        dbExeccutor.submit(task);
    }
    
    /**
     * Cargar tabla
     * @param conex 
     */
    public void cargaTabla(){
        clmFeche.setCellValueFactory  (new PropertyValueFactory<>("fecha_DiasFes"));
        tvFechas.setItems(listDiasFest);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dpFecha.setValue(LocalDate.now());
        //carga el ejecutor de servicio 
        ejecutorDeServicio();
        //formato tabla
        cargaTabla();
        //actualiza la informacion inicial
        actualizaListadiasFestivos();
        
        bttGuardar.setOnAction((evento)->{
            try(Connection conex = dbConn.conectarBD()) {
                guardar(conex);
                aux.informacionUs("Festivo guardado", 
                                  "Festivo guardado", 
                                  "El día festivo ha sido agregado correctamente a la base de datos");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        bttGuardar.setGraphic(new ImageView(guardar));
    }  
}
