
package porownania;

import java.util.ArrayList;

/**
 * Klasa przechowująca wyniki wyszukiwania motywów
 * @author Piotr Wrzeciono
 */
public class WynikWyszukiwania
{
    /**Nazwa pliku analizowanego*/
    private final String NazwaPliku;
    /**Lista wyników*/
    private final ArrayList<Rezultat> ListaWyników;
    /**Waga interwałowa*/
    private final double WagaInterwałowa;
    /**Waga metryczna*/
    private final double WagaMetryczna;
    /**Maksymalna uwzględniana różnica*/
    private final double MaksymalnaRóżnica;
    /**Minimalna długość motywu*/
    private final int MinimalnaDługośćMotywu;
    
    /**
     * Konstruktor klasy
     * @param nazwa_pliku_wejściowego Nazwa pliku wejściowego (analizowanego).
     * @param waga_int Waga interwałowa.
     * @param waga_m Waga metryczna.
     * @param max_r Maksymalna wartość różnicy.
     * @param min_d Minimalna liczba zdarzeń Humdrum w motywie.
     */
    public WynikWyszukiwania(String nazwa_pliku_wejściowego, double waga_int, double waga_m, double max_r, int min_d)
    {
        NazwaPliku = nazwa_pliku_wejściowego;
        ListaWyników = new ArrayList<>();
        
        WagaInterwałowa = waga_int;
        WagaMetryczna = waga_m;
        MaksymalnaRóżnica = max_r;
        MinimalnaDługośćMotywu = min_d;
        
    }//Koniec konstruktora
    
    /**
     * Metoda dopisująca wynik wyszukowania do listy
     * @param wynik Uzyskany wynik wyszukiwania
     */
    public void DopiszWyniki(Rezultat wynik)
    {
        ListaWyników.add(wynik);
    }//Koniec dopisywania do listy
    
    /**
     * Metoda generująca nagłówek do pliku z wynikami
     * @return Nagłówek
     */
    private String GenerujNagłówek()
    {
        String tekst;
        
        tekst  = "Nazwa pliku:\t" + NazwaPliku +"\t\n";
        
        tekst += "Waga interwałowa:\t" + WagaInterwałowa +"\t\n";
        tekst += "Waga metryczna:\t" + WagaMetryczna + "\t\n";
        tekst += "Maksymalna różnica:\t" + MaksymalnaRóżnica + "\t\n";
        tekst += "Minimalna długość motywu:\t" + MinimalnaDługośćMotywu + "\t\n\n\n";
        
        return tekst;
    }//Koniec metody generującej nagłówek do pliku z wynikami
    
    /**
     * Metoda generująca tabelę z wynikami.
     * @return Tekst tabeli CSV do pliku.
     */
    private String GenerujTabelkę()
    {
        String tabelka;
        int i;
        
        tabelka = "";
        
        //System.out.println("Lista wyników: size: " + ListaWyników.size());
        
        for(i = ListaWyników.size() - 1; i >= 0; i--)
        {
            tabelka += ListaWyników.get(i) + "\n\n";
        }//next i
        
        
        return tabelka;
        
    }//Koniec metody generującej tabelkę
    
    /**
     * Metoda służąca do generacji treści do pliku do zapisu.
     * @return Treść pliku z wynikami.
     */
    @Override
    public String toString()
    {
        String tekst;
        
        tekst = this.GenerujNagłówek();
        
        if(ListaWyników.isEmpty())
        {
            tekst += "Nie uzyskano żadnych wyników\n\n";
        }else
        {   
            tekst += this.GenerujTabelkę();
        }//end if
        
        return tekst;
    }//Koniec metody toString
    
}//Koniec klasy
