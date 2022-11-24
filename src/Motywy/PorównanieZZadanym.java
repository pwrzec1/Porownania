
package Motywy;

import Struktury.ArkuszZdarzenie;
import java.util.ArrayList;

/**
 * Klasa służąca do realizacji wyszukiwania z zadanym motywem.
 * @author Piotr Wrzeciono
 * @since 2022.06.09
 */
public class PorównanieZZadanym
{
    
    /**Tablica przechowująca cały utwór, w którym połączono wszystkie głosy*/
    private final ArkuszZdarzenie[] CałyUtwór;
    /**Motyw do wyszukania*/
    private final Motyw MotywDoWyszukania;
    /**Tekst reprezentujący motyw do porównań*/
    private final  String TekstMotywu;
    /**Waga związana z interwałem pomiędzy nutami*/
    private final double WagaInterwałowa;
    /**Waga związana z interwałem metrycznym*/
    private final double WagaCzasowa;
    /**Maksymalna wartość różnicy*/
    private final double MaksymalnaWartośćRóżnicy;
    /**Lista z wynikamu wyszukiwania.*/
    private ArrayList<WynikiUproszczone> ListaWyników;

    
    /**
     * Konstruktor klasy
     * @param całość Tutaj należy przekazać całość utworu, w którym połączono wszystkie głosy.
     * @param szukany Tutaj podajemy motyw szukany.
     * @param tekst_motywu Tekst reprezentujący motyw do porównań.
     * @param waga_interwałowa Waga związana z interwałem pomiędzy dźwiękami.
     * @param waga_czasowa Waga związana z wartościami rytmicznymi.
     * @param max_różnica Maksymalna wartość różnicy
     */
    public PorównanieZZadanym(ArkuszZdarzenie[] całość, Motyw szukany, String tekst_motywu ,double waga_interwałowa, double waga_czasowa, double max_różnica)
    {
        
        CałyUtwór = całość;
        MotywDoWyszukania = szukany;
        TekstMotywu = tekst_motywu;
        WagaInterwałowa = waga_interwałowa;
        WagaCzasowa = waga_czasowa;
        MaksymalnaWartośćRóżnicy = max_różnica;
        
        ListaWyników = new ArrayList<>();
        
    }//Koniec konstruktora
    
    public void ZrealizujPorównanie()
    {
        int i;
        int długość_motywu;
        NadajWagi();      
        
        String lokalizacja;
        double różnica;
        
        długość_motywu = MotywDoWyszukania.getDługośćMotywu();
        
        for(i = 0; i < CałyUtwór.length - długość_motywu; i++)
        {
            różnica = ObliczRóżnicę(i);
            
            if(różnica <= MaksymalnaWartośćRóżnicy)
            {
                lokalizacja = CałyUtwór[i].getNazwęKolumny() + CałyUtwór[i].getWiersz();
                
                ListaWyników.add(new WynikiUproszczone(lokalizacja, różnica));
            }//end if
            
        }//next i
        
    }//Koniec realizacji porównywania
    
    
    private double ObliczRóżnicę(int początek)
    {
        int ile;
        Motyw fragment_całości;
        ile = MotywDoWyszukania.getDługośćMotywu();
        double wynik;
        
        fragment_całości = new Motyw(CałyUtwór, początek, ile, false);
        
        wynik = MotywDoWyszukania.OceńPodobieństwo(fragment_całości);
        
        return wynik;
        
    } //Koniec obliczania różnicy
    
    /**Metoda dopisująca wagi do zdarzeń*/
    private void NadajWagi()
    {
        int i;
        
        for(i = 0; i < CałyUtwór.length; i++)
        {
            CałyUtwór[i].getZdarzenie().UstawWagi(WagaInterwałowa, WagaCzasowa);
        }//next i
        
        for(i = 0; i < MotywDoWyszukania.getReferencjęDoMotywu().length; i++)
        {
            MotywDoWyszukania.getReferencjęDoMotywu()[i].getZdarzenie().UstawWagi(WagaInterwałowa, WagaCzasowa);
        }//next i
        
    }//Koniec nadawania wag
    
    /**Metoda generująca raport z wyszukiwań*/
    @Override
    public String toString()
    {
        String raport;
        StringBuilder budowniczy;
        int i;
        
        budowniczy = new StringBuilder();
        
        budowniczy.append("\n");
        
        budowniczy.append("Szukany motyw:\t" + this.TekstMotywu + "\n");
        budowniczy.append("Długość szukanego motywu:\t" + this.MotywDoWyszukania.getDługośćMotywu() + "\n");
        budowniczy.append("Waga interwałowa:\t" + this.WagaInterwałowa + "\n");
        budowniczy.append("Waga metryczna:\t" + this.WagaCzasowa + "\n");
        budowniczy.append("Maksymalna różnica:\t" + this.MaksymalnaWartośćRóżnicy + "\n");
        
        if(ListaWyników.isEmpty())
        {
            budowniczy.append("Nie znaleziono szukanego motywu.");
        }else
        {
            budowniczy.append("Liczba znalezionych motywów\t");
            budowniczy.append(ListaWyników.size() + "\n");
            budowniczy.append("Arkusz\tWaga\n");
            
            for(i = 0; i < ListaWyników.size(); i++)
            {
                budowniczy.append(ListaWyników.get(i));
                budowniczy.append("\n");
            }
        }//end if
        
        raport = budowniczy.toString();
        
        return raport;
    }//Koniec generowania raportów
    
}//Koniec klasy

