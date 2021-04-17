package view;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.net.URL;

public class FxmlLoader {
    private Pane view;


    public Pane getPage(String fileName){
        try{
           URL fileUrl= getClass().getResource("/view/" + fileName);
            if(fileUrl==null){
                throw new java.io.FileNotFoundException("FXML file can't found");
            }
            view=new FXMLLoader().load(fileUrl);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("No page "+ fileName + " please check FxmlLoader.");
        }
        return view;
    }
}
