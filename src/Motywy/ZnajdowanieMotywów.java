
package Motywy;

import Struktury.ArkuszZdarzenie;

/**
 * Klasa służąca do znajdowania najdłuższych motywów w jednym głosie.
 * @author Piotr Wrzeciono
 */
public class ZnajdowanieMotywów
{
    /**Arkusz ze zdarzeniami muzycznymi*/
    private final ArkuszZdarzenie[] CałyGłos;
    /**Maksymalna wartość różnicy. Powyżej tej wartości motywy będą uznawane za niepodobne*/
    private final double MaksymalnaRóżnica;
    /**Lista znalezionych par motywów*/
    private ParaMotywów[] TablicaZnalezionych;
    /**Długość znalezionego motywu*/
    private int NajwiększaDługość;
    
    /**
     * Kontsruktor klasy szukającej motywów
     * @param głos Pojedynczy głos (system)
     * @param maks Maksymalna wartość różnicy uwzględnianej w wyszukiwaniach
     */
    public ZnajdowanieMotywów(ArkuszZdarzenie[] głos, double maks)
    {
        CałyGłos = głos;
        MaksymalnaRóżnica = maks;
        TablicaZnalezionych = null;
        
        NajwiększaDługość = -1; //Niczego jeszcze nie szukano
    }//Koniec konstruktora
    
    /**
     * Metoda wyszukująca najdłuższy motyw.
     * @param maksimum Maksymalna dopuszczalna różnica pomiędzy motywami
     * @param krab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public void Szukaj(double maksimum, boolean krab)
    {
        int liczba_zdarzeń;
        Fragmenty wyszukiwanie_fragmentów;
        Fragmenty wyszukiwanie_poprzednie;
        Fragmenty wyszukiwanie_pop;
        boolean czy_jest_zero; //Zmienna logiczna informująca o tym, czy trafiono na sytuację, w której wśród wyliczonych różnic jest zero
        ParaMotywów[] tablica_par;
        
        if(CałyGłos != null) //W tym przypadku nie warto liczyć!
        {
            liczba_zdarzeń = 2; //Minimalna liczba zdarzeń tworzących jakikolwiek motyw muzyczny;
            
            wyszukiwanie_poprzednie = null;
            wyszukiwanie_pop = null;
            
            do{
                wyszukiwanie_fragmentów = new Fragmenty(CałyGłos, liczba_zdarzeń, krab);
                
                wyszukiwanie_fragmentów.UtwórzListęWątkowo(maksimum);
                //wyszukiwanie_fragmentów.UtwórzListę();
                
                tablica_par = wyszukiwanie_fragmentów.getParyMotywów(MaksymalnaRóżnica);
                
                if(tablica_par != null) //Jeżeli coś znaleziono
                {
                    
                    czy_jest_zero = this.CzyJestZero(tablica_par);
                    
                    if(czy_jest_zero)
                    {
                        liczba_zdarzeń++;
                        
                        wyszukiwanie_pop = wyszukiwanie_poprzednie;
                        wyszukiwanie_poprzednie = wyszukiwanie_fragmentów; //Na wszelki przypadek, aby nie szukać wyników
                        
                    }else
                    {
                        liczba_zdarzeń -= 2;
                        
                        TablicaZnalezionych = wyszukiwanie_pop.getParyMotywów(MaksymalnaRóżnica);
                        
                        this.NajwiększaDługość = liczba_zdarzeń;
                    }//end if
                    
                }else
                {
                    czy_jest_zero = false;
                }//end if
                
            }while(czy_jest_zero == true && liczba_zdarzeń < CałyGłos.length); //Nie należy wyskakiwać poza maksymalny indeks tablicy
            
            if(czy_jest_zero == true || liczba_zdarzeń == CałyGłos.length) //Przeszukaliśmy całość, a nie znaleziono nieczego podobnego 
            {
                if(czy_jest_zero != false) //Tak na wszelki przypadek, gdy najdłuższy motyw równy jest całemu głosowi!
                {
                    TablicaZnalezionych = wyszukiwanie_fragmentów.getParyMotywów(MaksymalnaRóżnica);
                }//end if
                
            }//end if
            
        }//end if
        
    }//Koniec metody szukającej
    
    /**
     * Metoda sprawdzająca, czy w wynikacj jest różnica o wartości 0.
     * @param tab Tablica z parami motywów
     * @return <b>true</b>, gdy trafi się na wartość 0, <b>false</b>, gdy nie trafi się na wartość zero.
     */
    private boolean CzyJestZero(ParaMotywów[] tab)
    {
        boolean wynik;
        int i;
        
        wynik = false;
        i = 0;
        
        do{
            wynik = tab[i].getRóżnicę() == 0;
            i++;
        }while(wynik == false && i < tab.length);
        
        return wynik;
        
    }//Koniec metody sprawdzającej, czy jest w wynikach 0
    
    /**
     * Metoda zwracająca największą znalezioną długość motywu.
     * @return Liczba zdarzeń składająca się na najdłuższy motyw.
     */
    public int getDługośćMotywu()
    {
        return this.NajwiększaDługość;
    }//Koniec metody zwracającej długość motywu.
    
    /**
     * Metoda zwracająca pary znalezionych motywów
     * @return Tablica znalezionych najdłuższych motywów.
     */
    public ParaMotywów[] getZnalezioneParyMotywów()
    {
        return this.TablicaZnalezionych;
    }//Koniec metody zwracającej tablicę znalezionych par motywów
    
    
    
    
}//Koniec klasy
