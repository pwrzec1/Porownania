
package Motywy;

import Struktury.ArkuszZdarzenie;

/**
 * Klasa służąca do znajdowania motywów w całości
 * @author Piotr Wrzeciono
 */
public class ZnajdowanieMotywówCałość
{
    /**Wyszukiwanie pojedynczego motywu*/
    private Fragmenty Szukanie;
    /**Poszczególne głosy utworu*/
    private final ArkuszZdarzenie[][] GłosyPoszczególne;
    /**Liczba głosów w utworze*/
    private final int LiczbaGłosów;
    /**Połączone wszystkie głosy  - do analizy tematycznej*/
    private ArkuszZdarzenie[] GłosyPołączone;
    /**Tablica motywów wynikowych*/
    private ParaMotywów[] TablicaWynikowa;
    /**Maksymalna akceptowalna różnica*/
    private final double MaksymalnaRóżnica;
    
    /**
     * Konstruktor klasy
     * @param liczba_głosów Liczba głosów w utworze 
     * @param max_różnica Maksymalna dopuszczalna różnica pomiędzy motywami
     */
    public ZnajdowanieMotywówCałość(int liczba_głosów, double max_różnica)
    {
        int i;
        
        GłosyPoszczególne = new ArkuszZdarzenie[liczba_głosów][];
        LiczbaGłosów = liczba_głosów;
        GłosyPołączone = null;
        Szukanie = null;
        TablicaWynikowa = null;
        
        MaksymalnaRóżnica = max_różnica;
        
        for(i = 0; i < LiczbaGłosów; i++)
        {
            GłosyPoszczególne[i] = null;
        }//next i
        
    }//Koniec konstruktora
    
    
    /**
     * Metoda dopisująca głos do porównywania całości. 
     * <p style="color:red;"><b>UWAGA!</b> metody przepisują wyłącznie referencje!</p>
     * @param numer_systemu numer systemu Humdrum (liczymy od 1)
     * @param głos Tablica ze zdarzeniami humdrum reprezentującymi głos.
     */
    public void DopiszGłos(int numer_systemu, ArkuszZdarzenie[] głos)
    {
        
        int indeks;
                
        indeks = numer_systemu - 1;
        
        GłosyPoszczególne[indeks] = głos;
    }//Koniec dopisywania głosu
    
    
    /**
     * Metoda pakująca wszystkie głosy do jednego sztucznego.
     * @return <b>true</b>, gdy operacja się powiodła, <b>false</b>, gdy się nie udała.
     */
    public boolean Upakuj()
    {
        boolean wynik;
        int i;
        int j;
        int k;
        int ile_sum;
        
        
        wynik = true;
        
        for(i = 0; i < LiczbaGłosów; i++) //Sprawdzamy, czy wszystkie głosy zostały zapisane w obiekcie
        {
            if(GłosyPoszczególne[i] == null) wynik = false;
        }//next i
        
        
        if(wynik == true)
        {
            k = 0;
            
            ile_sum = 0;
            
            for(i = 0; i < LiczbaGłosów; i++) ile_sum += GłosyPoszczególne[i].length; //Zliczamy sumacyjną liczbę zdarzeń
            
            GłosyPołączone = new ArkuszZdarzenie[ile_sum]; //Powołujemy instancję tablicy z głosami połączonymi
            
            for(i = 0; i < LiczbaGłosów; i++) //Tworzymy głos połączony, od systemu najniższego.
            {
                for(j = 0; j < GłosyPoszczególne[i].length; j++)
                {
                  GłosyPołączone[k]  = GłosyPoszczególne[i][j];
                  k++;
                }//next i
            }//next i
            
        }//end if
        
        return wynik;
    }//Koniec metody pakującej wszystkie głosy do jednego
    
    /**
     * Metoda wywołująca wyszukiwanie motywów w całości
     * @param ile_zdarzeń Liczba zdarzeń do wyszukania
     * @param krab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public void UtwórzPorównanie(int ile_zdarzeń, boolean krab)
    {
        if(GłosyPołączone != null)
        {
            Szukanie = new Fragmenty(GłosyPołączone, ile_zdarzeń, krab);
            
        }//end if
    }//Koniec metody tworzącej 
    
    
    /**
     * Metoda uruchamiająca właściwe porównywanie.
     * @param maksimum Maksymalna dopuszczalna różnica do zapamiętania
     */
    public void UruchomPorównanie(double maksimum)
    {
        if(Szukanie != null)
        {
            Szukanie.UtwórzListę(maksimum);
            this.TablicaWynikowa = Szukanie.getParyMotywów(MaksymalnaRóżnica);
        }
    }
    
    /**
     * Metoda zwracająca tablicę motywów (wyniki porównywania).
     * @return Tablica par motywów posortowana rosnąco
     */
    public ParaMotywów[] getTablicęMotywów()
    {
        return this.TablicaWynikowa;
    }//Koniec metody zwracającej tablicę motywów
    
    /**
     * Metoda zwracająca referencję do referencji obiektu szukającego.
     * @return Referencja;
     */
    public Fragmenty getReferencjęDoSzukania()
    {
        return Szukanie;
    }
    
    
}//Koniec klasy
