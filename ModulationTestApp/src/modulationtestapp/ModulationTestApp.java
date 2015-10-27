/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulationtestapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.omg.CORBA.CharSeqHelper;

/**
 *
 * @author Piotrek
 */
public class ModulationTestApp  {

    /**
     * @param name
     * @param <error>
     * @param args the command line arguments
     */
    public static void saveInFile(String name, double[]tab)
    {
        try {
            FileWriter fw = new FileWriter(name);
            
            for (int i=0;i<tab.length;++i)
            {
                fw.write(Double.toString(tab[i]) + "\n");
            }
            fw.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            File file = new File (WaveRecord.getInstance().fileNameIn);
            System.out.println(file.getAbsolutePath());
            
            BufferedReader input = new BufferedReader(new FileReader(WaveRecord.getInstance().fileNameIn));
            String buf;
            short[] tab= new short[1];
            while ((buf = input.readLine()) != null)
            {
                //System.out.println("!" + buf + "!");
                
                tab[0] = (short) (0x7fff * Float.parseFloat(buf));
//                System.out.println(tab[0]);
                WaveRecord.getInstance().appendData(tab);
                
            }
            System.out.println("q" + buf + "q");
            input.close();
            System.out.println("Input finished" + Integer.toString(WaveRecord.getInstance().getData().length));
            
            Modulator mod = new Modulator();
            System.out.println("Start modulation");
            mod.doIt();
            System.out.println("End modulation");
            WaveRecord.getInstance().saveInFile();
            System.out.println("Output saved");
            
            
            
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModulationTestApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            
        }
        
        
    }
    
}
