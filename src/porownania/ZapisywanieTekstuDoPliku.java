
package porownania;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa służąca do zapisywania tekstu do pliku.
 * @author Piotr Wrzeciono
 */
public class ZapisywanieTekstuDoPliku
{
    /**Plik jeszcze nie otwarty.*/
    public static final int PRZED_OTWARCIEM = 0;
    /**Plik udało się otworzyć.*/
    public static final int PLIK_OTWARTY = 1;
    /**Pliku nie udało się otworzyć.*/
    public static final int PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ = 2;
    /**Zapis się powiódł.*/
    public static final int ZAPIS_UDANY = 3;
    /**Zapis się nie udał.*/
    public static final int ZAPIS_NIEUDANY = 4;
    /**Plik już istnieje.*/
    public static final int PLIK_JUŻ_ISTNIEJE = 5;
    /**Plik już zamknięty.*/
    public static final int PLIK_ZAMKNIĘTY = 6;
    /**Pliku nie udało się zamknąć.*/
    public static final int BŁĄD_ZAMKNIĘCIA = 7;
    
    /**Własność przechowująca status pliku.*/
    private int StatusPliku;
    /**Nazwa pliku tekstowego*/
    private final String NazwaPliku;
    /**Zmienna plikowa*/
    private final File Plik;
    /**Strumień do zapisu*/
    FileOutputStream StrumieńPlikowy;
    /**Zapisywacz do pliku*/
    OutputStreamWriter Zapisywacz;
    
    /**
     * Konstruktor klasy służącej do zapisywania tekstu do pliku.
     * @param nazwa_pliku Nazwa pliku tesktowego do zapisu.
     */
    public ZapisywanieTekstuDoPliku(String nazwa_pliku)
    {
        NazwaPliku = nazwa_pliku;
        StatusPliku = ZapisywanieTekstuDoPliku.PRZED_OTWARCIEM;
        
        Plik = new File(NazwaPliku);
        
        if(Plik.exists()) StatusPliku = ZapisywanieTekstuDoPliku.PLIK_JUŻ_ISTNIEJE;
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca status pliku (zgodnie ze stałymi klasy ZapisywanieTekstuDoPliku).
     * @return Status pliku.
     */
    public int getStatusPliku()
    {
        return StatusPliku;
    }
    
    
    /**
     * Metoda służąca do otwarcia pliku do zapisu.
     */
    private void UtwórzStrumieńDoZapisu()
    {
        try {
            StrumieńPlikowy = new FileOutputStream(Plik, false);
            StatusPliku = ZapisywanieTekstuDoPliku.PLIK_OTWARTY;
        } catch (FileNotFoundException ex) {
            StatusPliku = ZapisywanieTekstuDoPliku.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ;
        }
    }//Koniec tworzenia strumienia do zapisu
    
    /**
     * Metoda służąca do otwarcia pliku do zapisu.
     */
    public void OtwórzPlik()
    {
        UtwórzStrumieńDoZapisu();
        
        if(StatusPliku == ZapisywanieTekstuDoPliku.PLIK_OTWARTY)
        {
            Zapisywacz = new OutputStreamWriter(StrumieńPlikowy,StandardCharsets.UTF_8);
        }//end if
        
    }//Koniec otwarcia pliku
    
    /**
     * Metoda służąca do zapisu tekstu do pliku (pod warunkiem, że jest to możliwe).
     * @param tekst Tekst do zapisu
     */
    public void ZapiszTekst(String tekst)
    {
        if(StatusPliku == PLIK_OTWARTY || StatusPliku == ZAPIS_UDANY)
        {
            try {
                Zapisywacz.write(tekst);
                Zapisywacz.flush(); //Brak tego powoduje gubienie danych podczas zapisu!!!!!
                StatusPliku = ZAPIS_UDANY;
            } catch (IOException ex) {
                StatusPliku = ZAPIS_NIEUDANY;
            }
        }//end if
        
    }//Koniec metody zapisującej teskt do pliku
    
    /**
     * Metoda służąca do zamknięcia pliku.
     */
    public void ZamknijPlik()
    {
        if(StatusPliku != PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ && StatusPliku != PRZED_OTWARCIEM && StatusPliku != PLIK_JUŻ_ISTNIEJE && StatusPliku != PLIK_ZAMKNIĘTY)
        {
            try {
                StrumieńPlikowy.close();
                StatusPliku = PLIK_ZAMKNIĘTY;
            } catch (IOException ex) {
                StatusPliku = BŁĄD_ZAMKNIĘCIA;
            }
        }//end if
        
    }//Koniec zamykania pliku.
    
}//Koniec klasy
