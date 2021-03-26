package FileReader.ReadPropertyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadingPropertyFile {
    Properties prop=new Properties();
    FileInputStream ip;



    public ReadingPropertyFile(){
        {
            try {
                ip = new FileInputStream("src/main/java/DB/db.properties");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            this.prop.load(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ReadingPropertyFile(File file){
        try {
            ip = new FileInputStream(file.getAbsolutePath());
            this.prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProp(String propName){
        return prop.getProperty(propName);
    }
}
