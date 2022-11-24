
package porownania;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Klasa służąca do filtrowania katalogów
 * @author Piotr Wrzeciono
 */
public class FiltrKatalogów extends FileFilter
{

    /**
     * Metoda zwracająca wartość true, gdy argument jest katalogiem
     * @param arg0 Plik do sprawdzenia
     * @return <b>true</b>, gdy arg0 jest katalogiem, <b>false</b> w innym przypadku.
     */
    @Override
    public boolean accept(File arg0)
    {
        return arg0.isDirectory();
    }//Koniec metody

    /**
     * Metoda zwracająca opis filtru katalogowego
     * @return Opis filtru
     */
    @Override
    public String getDescription()
    {
        String opis;
        
        opis = "Folder";
        
        return opis;
    }//Koniec metody
    
}//Koniec klasy
