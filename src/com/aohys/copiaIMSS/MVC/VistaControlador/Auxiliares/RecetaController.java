/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares;

import com.aohys.copiaIMSS.BaseDatos.ListaMedicamentos;
import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Receta;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.Reportes.MedicamentosPDF;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class RecetaController implements Initializable {
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
            actualizaTablaPrimeraTabla(conex);
            actualizaTabla(conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    AutoCompletionBinding<String> autoComplete;
    ListaMedicamentos listaMedicamentos = new ListaMedicamentos();
    Receta receta = new Receta();
    
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    //Primera table
    @FXML private TableView<Receta> tvMedicPrevio;
    @FXML private TableColumn<Receta, String> colFecha;
    @FXML private TableColumn<Receta, String> colMedicaPre;
    @FXML private TableColumn<Receta, String> colMedicaViaAdm;
    @FXML private TableColumn<Receta, String> colMedicaIndica;
    @FXML private TableColumn<Receta, String> colMedicaInter;
    @FXML private TableColumn<Receta, String> colMedicaDuracion;
    @FXML private TableColumn<Receta, String> colAgregar;
    //Segundo grid
    @FXML private ComboBox<String> cbbMedicamento;
    @FXML private ComboBox<String> cbbViaAdministracion;
    @FXML private TextField txtIdicaciones;
    @FXML private ComboBox<String> cbbViaTipo;
    @FXML private TextField txtIntervalo;
    @FXML private ComboBox<String> cbbInterTipo;
    @FXML private TextField txtDura;
    @FXML private ComboBox<String> cbbDuraTipo;
    @FXML private TextField txtAdicionales;
    @FXML private Button bttAceptar;
    //Segunda tabla
    @FXML private TableView<Receta> tvMedelDia;
    @FXML private TableColumn<Receta, String> colMedicaDia;
    @FXML private TableColumn<Receta, String> colMedicaViaAdmDia;
    @FXML private TableColumn<Receta, String> colMedicaIndicaDia;
    @FXML private TableColumn<Receta, String> colMedicaInterDia;
    @FXML private TableColumn<Receta, String> colMedicaDuracionDia;
    //Buttons
    @FXML private Button bttImprimir;
    @FXML private Button bttAceptarBajo;
    
    
    //Lista de combobox
    ObservableList<String> listaViaAdministracion = FXCollections.observableArrayList();
    ObservableList<String> listaTipIndicacion = FXCollections.observableArrayList();
    ObservableList<String> listaIntervalos = FXCollections.observableArrayList();
    ObservableList<String> listaIndicaciones = FXCollections.observableArrayList();
    //Lista para imprimir
    ObservableList<Receta> listAnte = FXCollections.observableArrayList();
    ObservableList<Receta> listOriginal = FXCollections.observableArrayList();
    ObservableList<Receta> listDira = FXCollections.observableArrayList();
    
    
    //Imagenes Botones
    Image imprimir = new Image("com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    Image agregar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/add-user.png");
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    Image aceptar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
    }    
   
    /**
     * formato de imagen
     */
    private void formatoImagen(){
        bttAceptar.setGraphic(new ImageView(guardar));
        bttAceptarBajo.setGraphic(new ImageView(aceptar));
        bttImprimir.setGraphic(new ImageView(imprimir));
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAceptar.setOnAction(evento->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    guardarMedica(conex);
                    actualizaTabla(conex);
                    limpiaCuadros();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        bttAceptarBajo.setOnAction(evento->{
            cordi.lanzaHistoriaMedica(paci);
        });
        
        bttImprimir.setOnAction(evento->{
            MedicamentosPDF im = new MedicamentosPDF();
            im.pasoPrincipal(paci, listDira);
        });
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        txtAdicionales.setTextFormatter(new TextFormatter(aux.formato(500, 4)));
        txtIdicaciones.setTextFormatter(new TextFormatter(aux.formato(3, 3)));
        txtIntervalo.setTextFormatter(new TextFormatter(aux.formato(3, 3)));
        txtDura.setTextFormatter(new TextFormatter(aux.formato(3, 3)));
        
        
        aux.toolTipSuperior(txtAdicionales, "campo opcional");
        aux.toolTipSuperior(txtIdicaciones, "campo numérico");
        aux.toolTipSuperior(txtIntervalo, "campo numérico");
        aux.toolTipSuperior(txtDura, "campo numérico");
    }
    
    /**
     * carga formato de los comboboxes
     */
    private void formatoComboboxes(){
        listaIntervalos.addAll("Hora(s)", "Día(s)", "Semana(s)", "Mes(es)", "Año(s)");
        cbbDuraTipo.setItems(listaIntervalos);
        cbbInterTipo.setItems(listaIntervalos);
        
        listaTipIndicacion.addAll("INAHALACIÓN","INTRAMUSCULAR","INTRAVENOSO","NASAL","OFTÁLMICO","ORAL",
                "OTICO","PIEL Y ANEXOS","SUBCUTÁNEO","SUBDÉRMICO","SUBLINGUAL","TÓPICO","VÍA RECTAL","VÍA VAGINAL");
        cbbViaAdministracion.setItems(listaTipIndicacion);
       
        cbbViaAdministracion.valueProperty().addListener((observable, viejo, nuevo)->{
            if (nuevo!=null) {
                formatoIndicaciones(nuevo);
            }
        });
        
        cbbViaTipo.setItems(listaViaAdministracion);
        
        formatoComboMedicamento();
    }
    
    /**
     * formato de los comboboxes
     */
    private void formatoComboMedicamento(){
        cbbMedicamento.getItems().clear();
        cbbMedicamento.setItems(listaMedicamentos.cargaListaMedNom());
        cbbMedicamento.setEditable(true);
        autoComplete = TextFields.bindAutoCompletion(cbbMedicamento.getEditor(), cbbMedicamento.getItems());
        autoComplete.setPrefWidth(1200);
    }

    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbMedicamento, "Medicamento");
        errorMessage += aux.verificaValufield(cbbViaAdministracion, "Vía de administración");
        errorMessage += aux.verificaTexField(txtIdicaciones, "Indicaciones");
        errorMessage += aux.verificaValufield(cbbViaTipo, "Indicaciones");
        errorMessage += aux.verificaTexField(txtIntervalo, "Intervalo");
        errorMessage += aux.verificaValufield(cbbInterTipo, "Intervalo");
        errorMessage += aux.verificaTexField(txtDura, "Duración");
        errorMessage += aux.verificaValufield(cbbDuraTipo, "Duración");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    private void actualizaTabla(Connection conex){
        colMedicaDia.setCellValueFactory(new PropertyValueFactory<> ("nombreMed_rec"));
        colMedicaViaAdmDia.setCellValueFactory(new PropertyValueFactory<> ("viaAdmiMed_rec"));
        colMedicaIndicaDia.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getIndicaMed_rec(), p.getCompleIndicaMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colMedicaInterDia.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getIntevaloMed_rec(), p.getTipoIntervaloMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        colMedicaDuracionDia.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getDuacionMed_rec(), p.getTipoDucacionMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        
       
        List<String> listaID = new LinkedList<>();
        for (Receta rec : listDira) {
            listaID.add(rec.getId_rec());
        }
        
        for (Receta rec : receta.listaRecetas(conex, paci.getId_paciente())) {
            if (!listaID.contains(rec.getId_rec())) {
                listDira.add(rec);
            }
        }
        
       
        tvMedelDia.setItems(listDira);
        
    }
    
    /**
     * guarda receta
     * @param conex 
     */
    public void guardarMedica(Connection conex){
        String id_rec = aux.generaID();
        String nombreMed_rec = cbbMedicamento.getValue();
        Date fecha_rec = Date.valueOf(LocalDate.now());
        String viaAdmiMed_rec = cbbViaAdministracion.getValue();
        int indicaMed_rec = Integer.valueOf(txtIdicaciones.getText());
        String compleIndicaMed_rec = cbbViaTipo.getValue();
        int intevaloMed_rec = Integer.valueOf(txtIntervalo.getText());
        String tipoIntervaloMed_rec = cbbInterTipo.getValue();
        int duacionMed_rec = Integer.valueOf(txtDura.getText());
        String tipoDucacionMed_rec = cbbDuraTipo.getValue();
        String indiAdicionalesMed_rec = txtAdicionales.getText();
        String id_paciente = paci.getId_paciente();
        String id_medico = IngresoController.usua.getId_medico();
        
        receta.agregaMedicamento(id_rec, nombreMed_rec, fecha_rec, viaAdmiMed_rec, indicaMed_rec, 
                compleIndicaMed_rec, intevaloMed_rec, tipoIntervaloMed_rec, duacionMed_rec, tipoDucacionMed_rec, 
                indiAdicionalesMed_rec, id_paciente, id_medico, conex);
    }
    
    /**
     * limpia los cuadros despues de guardar
     */
    private void limpiaCuadros(){
        cbbMedicamento.setValue(null);
        cbbViaAdministracion.setValue(null);
        txtIdicaciones.setText("");
        cbbViaTipo.setValue(null);
        txtIntervalo.setText("");
        cbbInterTipo.setValue(null);
        txtDura.setText("");
        cbbDuraTipo.setValue(null);
        txtAdicionales.setText("");
    }
    
    /**
     * actualiza la primera tabla
     * @param conex 
     */
    private void actualizaTablaPrimeraTabla(Connection conex){
        colFecha.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            LocalDate lol = p.getFecha_rec().toLocalDate();
            DateTimeFormatter kkk = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy");
            String regresaFormato = lol.format(kkk);
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        
        colMedicaPre.setCellValueFactory(new PropertyValueFactory<> ("nombreMed_rec"));
        colMedicaViaAdm.setCellValueFactory(new PropertyValueFactory<> ("viaAdmiMed_rec"));
        //Dummy data para el boton
        colAgregar.setCellValueFactory(new PropertyValueFactory<> ("viaAdmiMed_rec"));
        
        colMedicaIndica.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getIndicaMed_rec(), p.getCompleIndicaMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        
        colMedicaInter.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getIntevaloMed_rec(), p.getTipoIntervaloMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        
        colMedicaDuracion.setCellValueFactory(informacionDeCelda->{
            Receta p = informacionDeCelda.getValue();
            String regresaFormato = String.format("%d %s", 
                p.getDuacionMed_rec(), p.getTipoDucacionMed_rec());
            return new ReadOnlyStringWrapper(regresaFormato);
        });
        
        colAgregar.setCellFactory (col -> {
            TableCell<Receta, String> cell = new TableCell<Receta, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        this.setGraphic(null);
                        this.setText(null);
                    }else{
                        Button ima = new Button("Agrega a receta");
                        ima.setOnAction(evento->{
                            Receta rec = getTableView().getItems().get(getIndex());
                            agregaMedaLista(rec);
                        });
                        ima.setGraphic(new ImageView(agregar));
                        this.setText(null);
                        this.setGraphic(ima);  
                    }
                    
                }
            };
            return cell;
        });
        listAnte.addAll(receta.listaRecetasAnteriores(conex, paci.getId_paciente()));
        listOriginal.addAll(listAnte);
        tvMedicPrevio.setItems(listAnte);
        
        colAgregar.prefWidthProperty().bind(
            tvMedelDia.widthProperty()
            .subtract(colAgregar.widthProperty())
        );
        
    }
    
    
   
    
    /**
     * borra en el fxml el medicamento seleccionado
     */
    @FXML
    private void borrar(){
         Receta ant = tvMedelDia.getSelectionModel().getSelectedItem();
         boolean borra = false;
         if (ant != null) {
            //Crea una lista de id de los objetos antiguos
            List<String> listaID = new LinkedList<>();
            for (Receta rec : listOriginal) {
                listaID.add(rec.getId_rec());
            }
            //Verifica que el id sea o no sea de los objetos antiguos
            //Tiene que ser en string por que no reconoce objetos como tal ya que cada que se crean los objetos son
            //diferentes
            if (listaID.contains(ant.getId_rec())) {
                borra = true;
            }
            //verifica si es de lo nuevo o de lo antigo 
            if (!borra) {
                //si es parte de lo nuevo entonces lo borra de la base de datos
                try(Connection conex = dbConn.conectarBD()) {
                   ant.borrarMedicamento(ant.getId_rec(), conex);
                   listDira.remove(ant);
               } catch (SQLException e) {
                   e.printStackTrace();
               }
             }else{
                //si es parte de lo antiguo lo manda a su tabla y lo quita de la tabla vieja 
                 listDira.remove(ant);
                 listAnte.add(ant);
             }
                 
        }else
             aux.alertaError("Selecciona un medicamento", "Selecciona un medicamento", 
                     "Es necesario seleccionar un medicamento para ser borrado;");
    }
    
    /**
     * agrega un medicamento antiguo a la receta actual
     * @param rec 
     */
    private void agregaMedaLista(Receta rec){
        listDira.add(rec);
        listAnte.remove(rec);
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
        //formato de los comboboxesz
        formatoComboboxes();
    } 
    
    
    
    /**
     * le da formato al segundo combobox
     * @param string 
     */
    private void formatoIndicaciones(String string){
        cbbViaTipo.getItems().clear();
        listaViaAdministracion.clear();
        switch (string) {
            case "INAHALACIÓN":
                listaViaAdministracion.addAll("INAHALACIÓN");
                break;
            case "INTRAMUSCULAR":
                listaViaAdministracion.addAll("AMPOLLETA(S)","MILILITRO(S)","UI");
                break;
            case "INTRAVENOSO":
                listaViaAdministracion.addAll("AMPOLLETA(S)","MILILITRO(S)","UI");
                break;
            case "NASAL":
                listaViaAdministracion.addAll("GOTA(S)","APLICACIÓN(ES) TÓPICA(S)","MILILITRO(S)");
                break;
            case "OFTÁLMICO":
                listaViaAdministracion.addAll("GOTA(S)","APLICACIÓN(ES) TÓPICA(S)","MILILITRO(S)");
                break;
            case "ORAL":
                listaViaAdministracion.addAll("TABLETA(S)","CÁPSULA(S)","COMPRIMIDO(S)","CUCHARADA(S)","GOTA(S)",
                        "MILILITRO(S)","ONZA(S)");
                break;
            case "OTICO":
                listaViaAdministracion.addAll("GOTA(S)","APLICACIÓN(ES) TÓPICA(S)","MILILITRO(S)");
                break;
            case "PIEL Y ANEXOS":
                listaViaAdministracion.addAll("APLICACIÓN(ES) TÓPICA(S)");
                break;
            case "SUBCUTÁNEO":
                listaViaAdministracion.addAll("AMPOLLETA(S)","MILILITRO(S)","UI");
                break;
            case "SUBLINGUAL":
                listaViaAdministracion.addAll("TABLETA(S)","CÁPSULA(S)","COMPRIMIDO(S)","GOTA(S)",
                        "MILILITRO(S)");
                break;
            case "TÓPICO":
                listaViaAdministracion.addAll("APLICACIÓN(ES) TÓPICA(S)");
                break;
            case "VÍA RECTAL":
                listaViaAdministracion.addAll("APLICACIÓN(ES) TÓPICA(S)","SUPOSITORIO(S)","MILILITRO(S)");
                break;
            case "VÍA VAGINAL":
                listaViaAdministracion.addAll("APLICACIÓN(ES) TÓPICA(S)","ÓVULO(S) VAGINAL(ES)","TABLETA(S) VAGINAL(ES)","MILILITRO");
                break;
            default:
                break;
        }
        
    }
}
