
package Motywy;

import java.util.ArrayList;

/**
 * Klasa służąca do wielowątkowego tworzenia par motywów.
 * @author Piotr Wrzeciono
 */
public class TworzenieParWątkowe implements Runnable
{
    /**Lista wynikowa z parami motywów */
    private volatile ArrayList<ParaMotywów> ListaParMotywów;
    /**Tablica motywów*/
    private final Motyw[] FragmentyGłosu;
    /**Indeks główny - stały*/
    private final int IndeksGłówny;
    /**Indeks początkowy - ustawiany dla wątku*/
    private final int IndeksPoczątkowy;
    /**Indeks końcowy - ustawiany dla wątku*/
    private final int IndeksKońcowy;
    /**Status wątku*/
    private volatile int StanWątku;
    /**Maksymalna wartość dopuszczalnej różnicy*/
    private final double MaksymalnaRóżnica;
    /**Czy ma szukać motywów typu krab?*/
    private final boolean CzyKrab;
    
    /**Przed rozpoczęciem wątka*/
    public static final int PRZED = 0;
    /**W trakcie działania wątka*/
    public static final int W_TRAKCIE = 1;
    /**Po zakończeniu działania wątka*/
    public static final int PO = 2;
    
    /**
     * Konstruktor klasy
     * @param motywy Tablica motywów
     * @param indeks Indeks elementu, z którym należy porównywać pozostałe
     * @param początek Pierwszy indeks elementu do porównywania
     * @param koniec Ostatni indeks elementu do porównywania
     * @param maksimum Maksymalna dopuszczalna wartość różnicy do zapamiętania
     * @param krab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public TworzenieParWątkowe(Motyw[] motywy,int indeks, int początek, int koniec, double maksimum, boolean krab)
    {
        ListaParMotywów = new ArrayList<>();
        FragmentyGłosu = motywy;
        IndeksGłówny = indeks;
        IndeksPoczątkowy = początek;
        IndeksKońcowy = koniec;
        CzyKrab = krab;
        
        StanWątku = TworzenieParWątkowe.PRZED;
        
        MaksymalnaRóżnica = maksimum;
        
    }//Koniec konstruktora

    /**
     * Metoda wątkowa.
     */
    @Override
    public void run()
    {
        int i;
        ParaMotywów nowa;
         
        StanWątku = TworzenieParWątkowe.W_TRAKCIE;
        
        for(i = IndeksPoczątkowy; i <= IndeksKońcowy; i++ )
        {
            nowa = new ParaMotywów(FragmentyGłosu[IndeksGłówny], FragmentyGłosu[i], CzyKrab);
            
            if(nowa.getRóżnicę() <= this.MaksymalnaRóżnica)
            {
                ListaParMotywów.add(nowa);
            }//end if
            
        }//next i
        
        StanWątku = TworzenieParWątkowe.PO;
        
        
    }//Koniec metody wątkowej
    
    
    /**
     * Metoda zwracająca stan wątku.
     * @return Stan wątku według stałych statycznych zdefiniowanych w klasie TworzenieParWątkowe
     */
    public int getStanWątku()
    {
        return this.StanWątku;
    }
    
    /**
     * Metoda zwracająca listę par motywów.
     * @return Lista.
     */
    public ArrayList<ParaMotywów> getListę()
    {
        return this.ListaParMotywów;
    }
    
}//Koniec klasy
