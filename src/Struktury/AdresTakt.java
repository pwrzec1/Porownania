package Struktury;

/**
 * Klasa pomocnicza służąca do przechowywania namiarów na słowo "takt"
 * @author Piotr Wrzeciono
 */
public class AdresTakt
{
    /**Numer wiersza, gdzie słowo "takt" się znajduje*/
    public int Wiersz;
    /**Numer kolumny, gdzie słowo "takt" się znajduje*/
    public int Kolumna;
    /**Numer systemu przynależnego do słowa "takt"*/
    public int NumerSystemu;
    
    /**
     * Konstruktor klasy.
     * @param wiersz Wiersz komórki ze słowem "takt".
     * @param kolumna Kolumna komórki ze słowem "takt".
     * @param numer Numer systemu przynelżnego do słowa takt.
     */
    public AdresTakt(int wiersz, int kolumna, int numer)
    {
        Wiersz = wiersz;
        Kolumna = kolumna;
        NumerSystemu = numer;
    }//Koniec konstruktora
    
    /**
     * Metoda generująca opis położenia słowa kluczowego "takt".
     * @return Opis położenia "takt".
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis  = "Położenie słowa \"takt\" (wiersz:kolumna) ......... " + Wiersz + ":" + Kolumna + "\n";
        opis += "Numer systemu ..................................... " + NumerSystemu + "\n";
        
        return opis;
    }//Koniec metody pokazującej pozycję słowa "takt"
    
}//Koniec klasy