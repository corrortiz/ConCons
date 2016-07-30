/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Resultados;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Laboratorial;
import com.aohys.copiaIMSS.MVC.Modelo.ListaLabora;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados.PDFLab;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados.PDFLab.agregarPDFTask;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados.PDFLab.cargaSolaUnResultadoTask;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.databaseThreadFactory;
import com.aohys.rehabSys.Utilidades.ClasesAuxiliares.Efectos;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class ResLabController implements Initializable {
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
            actualizaTabla(conex);
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
    Laboratorial laboratorial = new Laboratorial();
    ListaLabora listaLabora = new ListaLabora();
    PDFLab pDFLab = new PDFLab();
    PDFLab compruevaPDFLab = null;
    ListaLabora compruevaListaLabora = null;
    BooleanProperty mostarHyperLink = new SimpleBooleanProperty(true);
    BooleanProperty mostarBottonAgregar = new SimpleBooleanProperty(true);
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    //Variables
    @FXML private TextField otroExamen;
    @FXML private TextField otroCultivo;
    //Tabla
    @FXML private TableView<ListaLabora> tvFechaLabo;
    @FXML private TableColumn<ListaLabora, String> colFecha;
    //Agregar resultados
    @FXML private Button bttAgregarResultados;
    @FXML private Hyperlink hlResultado;
    @FXML private ProgressIndicator pgiSubidaPDF;
    @FXML private AnchorPane anchorPane;
    
    private File pdf = null;
    //Imagenes Botones
    Image impresora = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    Image guardarA = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image salir = 
        new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
    }    
   
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAgregarResultados.setOnAction(evento->{
            if (compruevaListaLabora != null) {
                    fileshowcer();
            }else
                aux.alertaError("Selecciona un requerimiento de estudios", "Selecciona un requerimiento de estudios",
                        "Es necesario seleccionar un requerimiento de estudios para poder agregar los resultados");
            
        });
        
        hlResultado.setOnAction(evento->{
            habrePDF();
        });
        
        pgiSubidaPDF.setVisible(false);
        
        hlResultado.setGraphic(new ImageView(impresora));
        bttAgregarResultados.setGraphic(new ImageView(guardarA));
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(TextField textField, String string){
        if (!string.equals("")) {
            textField.setDisable(true);
            textField.setText(string);
            textField.setStyle("-fx-opacity: 1;");
        }else{
            textField.setDisable(true);
            textField.setText("");
            textField.setStyle("-fx-opacity: .3;");
        }
            
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        //errorMessage += aux.verificaTexField(txtPeso, "Peso");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * formato de checbox
     * @param checkBox
     * @param bool 
     */
    private void seleccionaYSesactiva(CheckBox checkBox, boolean bool){
        if (bool) {
            checkBox.setSelected(true);
            checkBox.setDisable(true);
            checkBox.setStyle("-fx-opacity: 1;");
        }else{
            checkBox.setSelected(false);
            checkBox.setDisable(true);
            checkBox.setStyle("-fx-opacity: .1;");
        }
    }
    
  
    
    private void actualizaTabla(Connection conex){
        colFecha.setCellValueFactory(cellData -> {
            ListaLabora p = cellData.getValue();
            LocalDate lol = p.getFecha_lab().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String ageInYear = lol.format(kkk);
            return new ReadOnlyStringWrapper(ageInYear);
        });
        tvFechaLabo.setItems(listaLabora.listaLab(conex, paci.getId_paciente()));
        
        tvFechaLabo.getSelectionModel().selectedItemProperty().addListener((observable,viejo,nuevo)->{
            
                cargarPDF(pgiSubidaPDF, nuevo.getId_lab(), nuevo);
            
        });
    }
    /**
     * clase que carga un pdf 
     * @param dbActividad
     * @param dato
     * @param nuevo 
     */
    public void cargarPDF(final ProgressIndicator dbActividad, String dato, ListaLabora nuevo) {
        cargaSolaUnResultadoTask task = pDFLab.new cargaSolaUnResultadoTask(dato);
        //Bindigs del indicador de progreso
        dbActividad.visibleProperty().bind(
                task.runningProperty()
        );
        dbActividad.progressProperty().bind(
                task.progressProperty()
        );
        //Cuando termina
        task.setOnSucceeded((WorkerStateEvent t) -> {
            try(Connection conexionInterna = dbConn.conectarBD()) {
                Laboratorial lab = laboratorial.cargaSoloUno(nuevo.getId_lab(), conexionInterna);
                compruevaListaLabora = nuevo;
                compruevaPDFLab = task.getValue();
                pdf = null;
                if (compruevaPDFLab!= null) {
                    pdf = compruevaPDFLab.getPdf_pdfLab();
                }
                cargarComponentes(lab);
                formatoHiper();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //Maouse en modo esperar
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        //lanza el iris
        dbExeccutor.submit(task);
      }
    
    
    
    
    /**
     * file showcer
     */
    public void fileshowcer(){
         FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("Archivos PDF", "*.PDF");
        fileChooser.getExtensionFilters().addAll(extFilterAll);
        fileChooser.setTitle("Carga Resultados");
        //Show open file dialog
        pdf = fileChooser.showOpenDialog(null);

        if (pdf!=null) {
            guardaPDF(pgiSubidaPDF);
        }
        
    }
    
    /**
     * aparece y desapace botones
     */
    private void formatoHiper(){
        creaBindindsParaMostrarOno(hlResultado, mostarHyperLink);
        creaBindindsParaMostrarOno(bttAgregarResultados, mostarBottonAgregar);
        if (pdf!=null) {
            mostarHyperLink.setValue(false);
            mostarBottonAgregar.setValue(true);
        }else{
            mostarHyperLink.setValue(true);
            mostarBottonAgregar.setValue(false);
        }
    }
    
    /**
     * metodo para crear bindis de mostrar o no mostrar el nodo
     * @param node
     * @param booleanProperty 
     */
    private void creaBindindsParaMostrarOno(Node node, BooleanProperty booleanProperty){
       node.disableProperty().bind(booleanProperty);
       node.opacityProperty().bind(new Efectos().bindgAModo(node));
    }

    
    /**
     * agrega el pdf a la base de datos 
     * @param dbActividad
     * @param dato 
     */
    public void guardaPDF(final ProgressIndicator dbActividad) {
        String id_pdfLab = aux.generaID();
        File pdf_pdfLab = pdf;
        String id_lab = compruevaListaLabora.getId_lab();
        agregarPDFTask task = pDFLab.new agregarPDFTask(id_pdfLab, pdf_pdfLab, id_lab);
        //Bindigs del indicador de progreso
        dbActividad.visibleProperty().bind(
                task.runningProperty()
        );
        dbActividad.progressProperty().bind(
                task.progressProperty()
        );
        //Cuando termina
        task.setOnSucceeded((WorkerStateEvent t) -> {
            formatoHiper();
        });
        //Maouse en modo esperar
        anchorPane.getScene().getRoot().cursorProperty()
                .bind(Bindings.when(task.runningProperty())
                        .then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
        //lanza el iris
        dbExeccutor.submit(task);
      }
    
    /**
     * abre el pdf seleccionado 
     */
    private void habrePDF(){
        Desktop dt = Desktop.getDesktop();
        try {
            dt.open(pdf);
        } catch (IOException ex) {
            Logger.getLogger(ResLabController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        //formato hiper
        formatoHiper();
    } 
    
    private void cargarComponentes(Laboratorial lab){
        seleccionaYSesactiva(chbBIOMETRÍAHEMÁTICACOMPLETA,lab.getBIOMETRÍAHEMÁTICACOMPLETA());
        seleccionaYSesactiva(chbFORMULAROJA,lab.getFORMULAROJA());
        seleccionaYSesactiva(chbCOOMBS,lab.getCOOMBS());
        seleccionaYSesactiva(chbTIEMPODECOAGULACIONTPINRTPTTT,lab.getTIEMPODECOAGULACIONTPINRTPTTT());
        seleccionaYSesactiva(chbGRUPOSANGUÍNEOYRH,lab.getGRUPOSANGUÍNEOYRH());
        seleccionaYSesactiva(chbQUÍMICASANGUINEACOMPLETA,lab.getQUÍMICASANGUINEACOMPLETA());
        seleccionaYSesactiva(chbQUÍMICASANGUINEA,lab.getQUÍMICASANGUINEA());
        seleccionaYSesactiva(chbGLUCOSASERICA,lab.getGLUCOSASERICA());
        seleccionaYSesactiva(chbACURICO,lab.getACURICO());
        seleccionaYSesactiva(chbCOLESTEROL,lab.getCOLESTEROL());
        seleccionaYSesactiva(chbTRIGLICERIDOS,lab.getTRIGLICERIDOS());
        seleccionaYSesactiva(chbPERFILDELÍPIDOS,lab.getPERFILDELÍPIDOS());
        seleccionaYSesactiva(chbPRUEBASDEFUNCIONHEPÁTICA,lab.getPRUEBASDEFUNCIONHEPÁTICA());
        seleccionaYSesactiva(chbELECTROLITOSSERICOS,lab.getELECTROLITOSSERICOS());
        seleccionaYSesactiva(chbECARDIACAS,lab.getECARDIACAS());
        seleccionaYSesactiva(chbOTRASENZIMASTROPONINAMIOGLOBINA,lab.getOTRASENZIMASTROPONINAMIOGLOBINA());
        seleccionaYSesactiva(chbGENERALDEORINA,lab.getGENERALDEORINA());
        seleccionaYSesactiva(chbDEPURACIÓNDECREATININAORINA24HRS,lab.getDEPURACIÓNDECREATININAORINA24HRS());
        seleccionaYSesactiva(chbPERFILTIROIDE,lab.getPERFILTIROIDE());
        seleccionaYSesactiva(chbPAPANICOLAOU,lab.getPAPANICOLAOU());
        seleccionaYSesactiva(chbGONADROTOFINACORIONICAHUM,lab.getGONADROTOFINACORIONICAHUM());
        seleccionaYSesactiva(chbPERFILHORMONAL,lab.getPERFILHORMONAL());
        seleccionaYSesactiva(chbANTIGENOESPECIFICODEPRÓSTATA,lab.getANTIGENOESPECIFICODEPRÓSTATA());
        seleccionaYSesactiva(chbPROTCREACTIVA,lab.getPROTCREACTIVA());
        seleccionaYSesactiva(chbFACTORREUMATOIDE,lab.getFACTORREUMATOIDE());
        seleccionaYSesactiva(chbANTIESTRESTOLISINAS,lab.getANTIESTRESTOLISINAS());
        seleccionaYSesactiva(chbREACIONESFEBRILES,lab.getREACIONESFEBRILES());
        seleccionaYSesactiva(chbVIHELISAWesternBlot,lab.getVIHELISAWesternBlot());
        seleccionaYSesactiva(chbUROCULTIVO,lab.getUROCULTIVO());
        seleccionaYSesactiva(chbCOPROCULTIVO,lab.getCOPROCULTIVO());
        seleccionaYSesactiva(chbCOPRLÓGICO,lab.getCOPRLÓGICO());
        seleccionaYSesactiva(chbCOPROPARASITOSCÓPICO,lab.getCOPROPARASITOSCÓPICO());
        seleccionaYSesactiva(chbCULTIVOFARINGEO,lab.getCULTIVOFARINGEO());
        formatoDeText(otroExamen, lab.getOTROEXAMEN());
        formatoDeText(otroCultivo, lab.getOTROCULTIVO());
    }
            
    
    //FXMl de chechbox
    @FXML private CheckBox chbBIOMETRÍAHEMÁTICACOMPLETA;
    @FXML private CheckBox chbFORMULAROJA;
    @FXML private CheckBox chbCOOMBS;
    @FXML private CheckBox chbTIEMPODECOAGULACIONTPINRTPTTT;
    @FXML private CheckBox chbGRUPOSANGUÍNEOYRH;
    @FXML private CheckBox chbQUÍMICASANGUINEACOMPLETA;
    @FXML private CheckBox chbQUÍMICASANGUINEA;
    @FXML private CheckBox chbGLUCOSASERICA;
    @FXML private CheckBox chbACURICO;
    @FXML private CheckBox chbCOLESTEROL;
    @FXML private CheckBox chbTRIGLICERIDOS;
    @FXML private CheckBox chbPERFILDELÍPIDOS;
    @FXML private CheckBox chbPRUEBASDEFUNCIONHEPÁTICA;
    @FXML private CheckBox chbELECTROLITOSSERICOS;
    @FXML private CheckBox chbECARDIACAS;
    @FXML private CheckBox chbOTRASENZIMASTROPONINAMIOGLOBINA;
    @FXML private CheckBox chbGENERALDEORINA;
    @FXML private CheckBox chbDEPURACIÓNDECREATININAORINA24HRS;
    @FXML private CheckBox chbPERFILTIROIDE;
    @FXML private CheckBox chbPAPANICOLAOU;
    @FXML private CheckBox chbGONADROTOFINACORIONICAHUM;
    @FXML private CheckBox chbPERFILHORMONAL;
    @FXML private CheckBox chbANTIGENOESPECIFICODEPRÓSTATA;
    @FXML private CheckBox chbPROTCREACTIVA;
    @FXML private CheckBox chbFACTORREUMATOIDE;
    @FXML private CheckBox chbANTIESTRESTOLISINAS;
    @FXML private CheckBox chbREACIONESFEBRILES;
    @FXML private CheckBox chbVIHELISAWesternBlot;
    @FXML private CheckBox chbUROCULTIVO;
    @FXML private CheckBox chbCOPROCULTIVO;
    @FXML private CheckBox chbCOPRLÓGICO;
    @FXML private CheckBox chbCOPROPARASITOSCÓPICO;
    @FXML private CheckBox chbCULTIVOFARINGEO;

    /**
     * formato de imagen
     */
    private void formatoImagen(){
        chbBIOMETRÍAHEMÁTICACOMPLETA.setDisable(true);
        chbFORMULAROJA.setDisable(true);
        chbCOOMBS.setDisable(true);
        chbTIEMPODECOAGULACIONTPINRTPTTT.setDisable(true);
        chbGRUPOSANGUÍNEOYRH.setDisable(true);
        chbQUÍMICASANGUINEACOMPLETA.setDisable(true);
        chbQUÍMICASANGUINEA.setDisable(true);
        chbGLUCOSASERICA.setDisable(true);
        chbACURICO.setDisable(true);
        chbCOLESTEROL.setDisable(true);
        chbTRIGLICERIDOS.setDisable(true);
        chbPERFILDELÍPIDOS.setDisable(true);
        chbPRUEBASDEFUNCIONHEPÁTICA.setDisable(true);
        chbELECTROLITOSSERICOS.setDisable(true);
        chbECARDIACAS.setDisable(true);
        chbOTRASENZIMASTROPONINAMIOGLOBINA.setDisable(true);
        chbGENERALDEORINA.setDisable(true);
        chbDEPURACIÓNDECREATININAORINA24HRS.setDisable(true);
        chbPERFILTIROIDE.setDisable(true);
        chbPAPANICOLAOU.setDisable(true);
        chbGONADROTOFINACORIONICAHUM.setDisable(true);
        chbPERFILHORMONAL.setDisable(true);
        chbANTIGENOESPECIFICODEPRÓSTATA.setDisable(true);
        chbPROTCREACTIVA.setDisable(true);
        chbFACTORREUMATOIDE.setDisable(true);
        chbANTIESTRESTOLISINAS.setDisable(true);
        chbREACIONESFEBRILES.setDisable(true);
        chbVIHELISAWesternBlot.setDisable(true);
        chbUROCULTIVO.setDisable(true);
        chbCOPROCULTIVO.setDisable(true);
        chbCOPRLÓGICO.setDisable(true);
        chbCOPROPARASITOSCÓPICO.setDisable(true);
        chbCULTIVOFARINGEO.setDisable(true);
        otroExamen.setDisable(true);
        otroCultivo.setDisable(true);
        chbBIOMETRÍAHEMÁTICACOMPLETA.setStyle("-fx-opacity: .1;");
        chbFORMULAROJA.setStyle("-fx-opacity: .1;");
        chbCOOMBS.setStyle("-fx-opacity: .1;");
        chbTIEMPODECOAGULACIONTPINRTPTTT.setStyle("-fx-opacity: .1;");
        chbGRUPOSANGUÍNEOYRH.setStyle("-fx-opacity: .1;");
        chbQUÍMICASANGUINEACOMPLETA.setStyle("-fx-opacity: .1;");
        chbQUÍMICASANGUINEA.setStyle("-fx-opacity: .1;");
        chbGLUCOSASERICA.setStyle("-fx-opacity: .1;");
        chbACURICO.setStyle("-fx-opacity: .1;");
        chbCOLESTEROL.setStyle("-fx-opacity: .1;");
        chbTRIGLICERIDOS.setStyle("-fx-opacity: .1;");
        chbPERFILDELÍPIDOS.setStyle("-fx-opacity: .1;");
        chbPRUEBASDEFUNCIONHEPÁTICA.setStyle("-fx-opacity: .1;");
        chbELECTROLITOSSERICOS.setStyle("-fx-opacity: .1;");
        chbECARDIACAS.setStyle("-fx-opacity: .1;");
        chbOTRASENZIMASTROPONINAMIOGLOBINA.setStyle("-fx-opacity: .1;");
        chbGENERALDEORINA.setStyle("-fx-opacity: .1;");
        chbDEPURACIÓNDECREATININAORINA24HRS.setStyle("-fx-opacity: .1;");
        chbPERFILTIROIDE.setStyle("-fx-opacity: .1;");
        chbPAPANICOLAOU.setStyle("-fx-opacity: .1;");
        chbGONADROTOFINACORIONICAHUM.setStyle("-fx-opacity: .1;");
        chbPERFILHORMONAL.setStyle("-fx-opacity: .1;");
        chbANTIGENOESPECIFICODEPRÓSTATA.setStyle("-fx-opacity: .1;");
        chbPROTCREACTIVA.setStyle("-fx-opacity: .1;");
        chbFACTORREUMATOIDE.setStyle("-fx-opacity: .1;");
        chbANTIESTRESTOLISINAS.setStyle("-fx-opacity: .1;");
        chbREACIONESFEBRILES.setStyle("-fx-opacity: .1;");
        chbVIHELISAWesternBlot.setStyle("-fx-opacity: .1;");
        chbUROCULTIVO.setStyle("-fx-opacity: .1;");
        chbCOPROCULTIVO.setStyle("-fx-opacity: .1;");
        chbCOPRLÓGICO.setStyle("-fx-opacity: .1;");
        chbCOPROPARASITOSCÓPICO.setStyle("-fx-opacity: .1;");
        chbCULTIVOFARINGEO.setStyle("-fx-opacity: .1;");
    }

}


