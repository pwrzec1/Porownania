
package Struktury;

/**
 * Klasa reprezentująca komórkę arkusza kalkulacyjnego
 * @author Piotr Wrzeciono
 */
public class KomórkaArkusza
{
    /**Numer wiersza w arkuszu - liczymy od 1*/
    private final int Wiersz;
    /**Numer kolumny w arkuszu - liczmy od 1*/
    private final int Kolumna;
    /**String Wartość w komórce*/
    private String Wartość;
    
    /**
     * Konstruktor klasy KomórkaArkusza
     * @param wiersz Kolumna w arkuszu (numerujemy od 1)
     * @param kolumna Kolumna w arkuszu (numerujemy od 1)
     * @param wartość Wartość w komórce
     */
    public KomórkaArkusza(int wiersz, int kolumna, String wartość)
    {
        Wiersz= wiersz;
        Kolumna = kolumna;
        Wartość = wartość;
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca numer wiersza
     * @return Numer wiersza (od 1)
     */
    public int getWiersz()
    {
        return Wiersz;
    }
    
    /**
     * Metoda zwracająca numer kolumny
     * @return Numer kolumny (od 1)
     */
    public int getKolumnę()
    {
        return Kolumna;
    }
    
    /**
     * Metoda zwracająca wartość komórki w postaci tekstowej
     * @return Zawartość komórki
     */
    public String getKomórkę()
    {
        return Wartość;
    }
    
    /**
     * Metoda zwracająca wartość w komórce
     * @return Wartość double w komórce
     */
    public double getWartość()
    {
        double wynik;
        String do_konwersji;
        
        do_konwersji = Wartość.trim();
        
        do_konwersji = do_konwersji.replace(',', '.'); //Zamiana przecinków na kropki
        
        try{
            wynik = Double.parseDouble(do_konwersji);
        }catch(Exception ex)
        {
            wynik = Double.NaN;
        }//end try-catch
        
        return wynik;
    } //Koniec metody zwracającej wartość;
    
    
    /**
     * Metoda wpisująca nową wartość do komórki
     * @param wartość Nowa wartość <b>String</b>
     */
    public void setWartość(String wartość)
    {
        Wartość = wartość;
    }//Koniec metody ustawiającej wartość komórki
    
    
    /**
     * Metoda zwracająca zawartość komórki w arkuszu
     * @return Zawartość w komórce - String
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = Wartość.trim();
        
        return opis;
    }//Koniec metody toString
    
}//Koniec klasy
