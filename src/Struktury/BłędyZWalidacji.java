
package Struktury;

/**
 * Kklasa służąca do przechowywania błędów znalezionych w procesie walidacji
 * @author Piotr Wrzeciono
 */
public class BłędyZWalidacji
{
    /**Zamiast wartości liczbowej podano wartość tekstową.*/
    public static final int WARTOSC_TEKSTOWA = 1;
    /**Podana wartość liczbowa powinna być dodatnia, a jest ujemna.*/
    public static final int WARTOSC_UJEMNA = 2;
    
    private int KodBłędu;
    
    
    /**Nieprawidłowy tekst w komórce*/
    private String TekstWKomórce;
    /**Kod literowy kolumny*/
    private String KodLiterowyKolumny;
    /**Numer wiersza*/
    private int Wiersz;
    /**Numer kolumny*/
    private int Kolumna;

    /**
     * Konstruktor klasy
     * @param tekst Błędny tekst w komórce
     * @param oznaczenie_kolumny Literowe oznaczenie kolumny
     * @param wiersz Numer wiersza w arkuszu
     * @param kolumna Numer kolumny w arkuszu
     * @param kod_błędu Kod błędu (WARTOSC_TEKSTOWA lub WARTOSC_UJEMNA)
     */
    public BłędyZWalidacji(String tekst,String oznaczenie_kolumny, int wiersz, int kolumna, int kod_błędu)
    {
        TekstWKomórce = tekst;
        KodLiterowyKolumny = oznaczenie_kolumny;
        Wiersz = wiersz;
        Kolumna = kolumna;
        
        KodBłędu = kod_błędu;
        
    }//Koniec konstruktora
    
    /**
     * Metoda generująca wyniki walidacji liczbowej arkusza.
     * @return Opis błędu
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "Błąd w komórce (kolumna:wiersz) ..... (" + Kolumna + ":" + Wiersz + ")\t(" + KodLiterowyKolumny + "" + Wiersz + ")\t";
        
        if(KodBłędu == BłędyZWalidacji.WARTOSC_TEKSTOWA)
        {
            opis += "Zamiast wartości liczbowej jest: '" + TekstWKomórce + "'";
        }
        
        if(KodBłędu == BłędyZWalidacji.WARTOSC_UJEMNA)
        {
            opis += "Zamiast wartości dodatniej, jest: '" + TekstWKomórce +"'";
        }
        
        return opis;
    }//Koniec metody toString
    
}//Koniec klasy
