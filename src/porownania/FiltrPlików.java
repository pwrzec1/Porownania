
package porownania;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Klasa służąca do filtrowania plików  w oknie wyboru plików.
 * @author Piotr Wrzeciono
 */
public class FiltrPlików extends FileFilter
{
    /**Rozszeżenie nazwy pliku*/
    private final String Rozszerzenie;
    /**Opis pliku*/
    private final String OpisPliku;
    
    /**
     * Konstruktor klasy filtru plików o dowolnym rozszerzeniu
     * @param rozszerzenie Rozszerzenie pliku. <b>UWAGA</b> Nazwę rozszerzenia podajemy bez kropki!!!!
     * @param opis Opis pliku
     */
    public FiltrPlików(String rozszerzenie, String opis)
    {
        Rozszerzenie = "." + rozszerzenie.toLowerCase();
        OpisPliku = opis;
    }//Koniec konstruktora
    
    /**
     * Metoda akceptująca plik o zadanym rozszerzeniu.
     * @param arg0 Plik
     * @return <b>true</b>, gdy plik jest zaakceptowany do wyświetlenia, <b>false</b> gdy plik nie jest zaakceptowamu do wyświetlenia.
     */
    @Override
    public boolean accept(File arg0)
    {
        boolean wynik;
        
        wynik = arg0.getName().endsWith(Rozszerzenie) || arg0.isDirectory();
        
        return wynik;
        
    }//Koniec metody akceptującej dany plik

    /**
     * Metoda zwracająca opis pliku
     * @return opis pliku
     */
    @Override
    public String getDescription()
    {
        return this.OpisPliku;
    }//Koniec metody zwracającej opis pliku
    
}//Koniec klasy realizującej filtrowanie plików csv
