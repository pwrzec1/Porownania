package Motywy;

/**
 * Struktura danych służąca do przechowywania wyników porównywania.
 * @author Piotr Wrzeciono
 * @since 2022.06.09
 */
class WynikiUproszczone
{
    /**Lokalizacja wyniku w arkuszu kalkulacyjnym - plik z utworem*/
    public String LokalizacjaWArkuszu;
    /**Wyliczona wartość różnicy*/
    public double WartośćRóżnicy;
    
    /**
     * Konstruktor klasy.
     * @param lokalizacja Lokalizacja początku motywu w arkuszu kalkulacyjnym
     * @param różnica Wyliczona różnica pomiędzy motywami.
     */
    public WynikiUproszczone(String lokalizacja, double różnica)
    {
        LokalizacjaWArkuszu = lokalizacja;
        WartośćRóżnicy = różnica;
    }
    
    /**
     * Metoda zwracająca informację o wyniku
     * @return 
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = LokalizacjaWArkuszu + "\t" + WartośćRóżnicy;
        
        return opis;
    }
    
}//Koniec klasy