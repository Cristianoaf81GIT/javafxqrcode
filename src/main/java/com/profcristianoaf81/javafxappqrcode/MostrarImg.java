/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.profcristianoaf81.javafxappqrcode;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author cristianoaf81
 */
public class MostrarImg extends Application{
    private AnchorPane painelImagem;
    private Label LB_img;
    private Button Imprimir;
    private Scene cena;
    private Stage palco;
    private File abrirCOD;
    private int TIPO_COD_BARRAS;
    private String valorCOD;
    
   
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        inicializarComponentes();
        primaryStage = this.palco;
        primaryStage.setTitle("Código QR/Barras");
        primaryStage.setResizable(true);
        primaryStage.show();
        LB_img.setLayoutX(
          (painelImagem.getWidth()-LB_img.getWidth())/2
        );
        LB_img.setLayoutY(05);
        Imprimir.setLayoutX(
         (painelImagem.getWidth()-Imprimir.getWidth())/2
        );
        Imprimir.setLayoutY(350);
        LB_img.setLayoutX(
          (painelImagem.getWidth()-LB_img.getWidth())/2
        );
        definirEventos();
        
    }
    
    public void ExibirIMG(Image img, int tipoCodigo, String valor){
     if(img!=null){
         LB_img.setGraphic(new ImageView(img));
         this.TIPO_COD_BARRAS = tipoCodigo;
         this.valorCOD = valor;      
     }
    
    }
    
    public void inicializarComponentes(){
     painelImagem = new AnchorPane();
     painelImagem.setPrefSize(350, 400);
     LB_img = new Label();
     LB_img.setPrefSize(300, 300);
     Imprimir = new Button("Imprimir");
     painelImagem.getChildren().addAll(LB_img,Imprimir);
     cena = new Scene(painelImagem);
     this.palco = new Stage();
     this.palco.setScene(cena);
    }
    
    public void definirEventos(){
     Imprimir.setOnAction((ActionEvent e) -> {
         PrinterJob TrabalhoDeImpressao = PrinterJob.getPrinterJob();
         TrabalhoDeImpressao.setPrintable(new Printable() {
             @Override
             public int print(Graphics grphcs
                     , PageFormat pf, int PageIndex) throws PrinterException
             {
                 try {
                     if(PageIndex != 0){
                         return NO_SUCH_PAGE;
                     }
                                        
                     
                    switch(TIPO_COD_BARRAS){
                        case 1:
                        abrirCOD= new File(
                             System.getProperty("user.home")+"/Desktop/"
                             +"QrCode.png"
                        );
                        grphcs.drawImage(ImageIO.read(abrirCOD)
                             , 100
                             , 100
                             , null);
                         
                         break;
                        case 2:
                        PageIndex = 0;
                        abrirCOD= new File(
                            System.getProperty("user.home")+"/Desktop/"
                            +"COD128.png"
                        );
                        grphcs.drawImage(ImageIO.read(abrirCOD)
                             , 100
                             , 100
                             , null);
                        break;
                        default:
                            
                        break;        
                    }
                                 
                    return PAGE_EXISTS;     
                 } catch (IOException ex) {
                     Logger.getLogger(MostrarImg.class.getName())
                             .log(Level.SEVERE
                                     , null
                                     , ex);
                 }
                
                return PAGE_EXISTS; 
             }
         });
         
            
             try {
                 
                 if(TrabalhoDeImpressao.printDialog()){
                    TrabalhoDeImpressao.print();  
                 }   
                
             } catch (PrinterException ex) {
                 System.out.println("erro: "+ex.getMessage());
             }
             
     });
    }
    
    //é possível implementar os metodos da interface printable
    //em um objeto
    class ObjetoDeImpressao implements Printable{

        @Override
        public int print(Graphics grphcs, PageFormat pf, int pageIndex) 
                throws PrinterException {
           Graphics2D g2 = (Graphics2D) grphcs;
           
           if(pageIndex == 0){
               Paper papel = new Paper();
               papel.setSize(5.48, 8.6);
               pf.setPaper(papel);
               pf.setOrientation(PageFormat.PORTRAIT);
               try {
                   abrirCOD = new File(
                   System.getProperty("user.home")+"/Desktop/"
                           +"QrCode.png"
                   );
                   
                   g2.drawImage(ImageIO.read(abrirCOD), null
                           , pageIndex
                           , pageIndex);
                   
               } catch (IOException e) {
                   System.out.println("erro: "+e.getMessage());
               }
               return PAGE_EXISTS;
           }else{
           
               return NO_SUCH_PAGE;
           }
           
           
        }
    
    
    }
    
}
