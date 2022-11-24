
package porownania;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa odpowiadająca za tworzenie listy plików CSV w katalogu.
 * @author Piotr Wrzeciono
 */
public class ListaPlików
{
    /**Nazwa katalogu z plikami CSV*/
    private final String NazwaKatalogu;
    /**Zmienna plikowa dla katalogu*/
    private File Katalog;
    /**Tablica z nazwami plików w katalogu*/
    private String[] TablicaPlików;
    /**Stan obiektu*/
    private int StanListy;
    
    /**Jeszcze niczego nie zaczęto szukać*/
    public static final int PRZED_SPARWDZENIEM = 0;
    /**Katalog istnieje!*/
    public static final int KATALOG_ISTNIEJE = 1;
    /**Podany kataog nie istnieje - błąd!*/
    public static final int PODANY_KATALOG_NIE_ISTNIEJE = 2;
    /**Podany katalog w rzeczywistości jest plikiem - błąd!*/
    public static final int PODANY_KATALOG_JEST_PLIKIEM = 3;
    /**W podanym katalogu nie ma plików CSV*/
    public static final int NIE_ZNALEZIONO_PLIKOW_CSV = 4;
    /**Wczytano listę plików CSV*/
    public static final int ZNALEIZONO_PLIKI_CSV = 5;
    
    /**
     * Konstruktor klasy listy plików
     * @param katalog Katalog, w którym trzeba przeszukać pliki
     */
    public ListaPlików(String katalog)
    {
        NazwaKatalogu = katalog;
        TablicaPlików = null;
        Katalog = null;
        
        StanListy = ListaPlików.PRZED_SPARWDZENIEM;
        
        
        SprawdzanieKatalogu();
        
        TablicaPlików = this.PobierzTablicęPlikówCSV();
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca stan listy plikóœ
     * @return Stan listy plików według stałych zdefiniowanych w klasie.
     */
    public int getStanListy()
    {
        return StanListy;
    }//Koniec zwracania stanu listy
    
    /**
     * Metoda zwracająca opis stanu obiektu
     * @return Opis stanu obiektu, generowany dla stanu listy.
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "";
        
        if(StanListy == ListaPlików.PRZED_SPARWDZENIEM) opis = "Jeszcze nie zaczęto sprawdzać listy plików";
        if(StanListy == ListaPlików.KATALOG_ISTNIEJE) opis = "Katalog '" + NazwaKatalogu + "' istnieje.";
        if(StanListy == ListaPlików.PODANY_KATALOG_NIE_ISTNIEJE) opis = "Katalog '" + NazwaKatalogu + "' nie istnieje!";
        if(StanListy == ListaPlików.PODANY_KATALOG_JEST_PLIKIEM) opis = "Katalog '" + NazwaKatalogu + "' jest w rzeczywistości plikiem!";
        if(StanListy == ListaPlików.NIE_ZNALEZIONO_PLIKOW_CSV) opis = "W katalogu '" + NazwaKatalogu +"' nie znaleziono plików CSV!";
        if(StanListy == ListaPlików.ZNALEIZONO_PLIKI_CSV) opis = "W katalogu '" + NazwaKatalogu + "' znaleziono pliki CSV";
        
        return opis;
    }//Koniec metody toString
    
    /**
     * Metoda służąca do sprawdzenia, czy katalog istnieje i czy przypadkiem nie jest plikiem.
     */
    private void SprawdzanieKatalogu()
    {
        boolean rezultat;
        
        Katalog = new File(NazwaKatalogu); //Tworzenie odpowiedniego obiektu
        
        rezultat = Katalog.exists();
        
        if(rezultat == true)
        {
            StanListy = ListaPlików.KATALOG_ISTNIEJE;
        }else
        {
            StanListy = ListaPlików.PODANY_KATALOG_NIE_ISTNIEJE;
        }//end if
        
        
        if(StanListy == ListaPlików.KATALOG_ISTNIEJE)
        {
            rezultat = Katalog.isDirectory();
            
            if(rezultat == false) StanListy = ListaPlików.PODANY_KATALOG_JEST_PLIKIEM;
        }//end if
    }//Koniec metody sprawdzającej katalog
    
    
    /**
     * Metoda zwracająca tablicę plików CSV z katalogu
     * @return Tablica nazw plików
     */
    private String[] PobierzTablicęPlikówCSV()
    {
        String[] tablica;
        int i;
        List<String> lista_pośrednia;
        File[] tablica_pośrednia;
        String pomoc;
        
        tablica = null;
        
        if(StanListy == ListaPlików.KATALOG_ISTNIEJE)
        {
            lista_pośrednia = new ArrayList<>();
            
            tablica_pośrednia = Katalog.listFiles();
            
            if(tablica_pośrednia == null)
            {
                StanListy = ListaPlików.NIE_ZNALEZIONO_PLIKOW_CSV;
            }else
            {
                for(i = 0; i < tablica_pośrednia.length; i++)
                {                                                            
                    if(tablica_pośrednia[i].isDirectory() == false)
                    {
                        pomoc = tablica_pośrednia[i].getName();
                        
                        if(pomoc.length() > 4) //Krótsze nazwy nie wchodzą w grę. Najkrótsza jest typu: x.csv
                        {
                            pomoc = pomoc.substring(pomoc.length()-4).toLowerCase();
                            
                            if(pomoc.compareTo(".csv") == 0)
                            {
                                lista_pośrednia.add(tablica_pośrednia[i].getName());
                            }//end if
                            
                        }//end if
                    }//end if
                    
                }//next i
                
            }//end if}
            
            if(lista_pośrednia.isEmpty())
            {
                StanListy = ListaPlików.NIE_ZNALEZIONO_PLIKOW_CSV;
            }else
            {
                tablica = new String[lista_pośrednia.size()];
                
                for(i = 0; i < tablica.length; i++)
                {
                    tablica[i] = lista_pośrednia.get(i);
                    
                }//next i
                
                Arrays.sort(tablica);
                
                StanListy = ListaPlików.ZNALEIZONO_PLIKI_CSV;
                
            }//end if
            
        }//end if
        
        return tablica;
        
    }//Koniec metody zwracającej tablicę plików CSV z katalogu
    
    /**
     * Metoda zwracająca tablicę nazw plików CSV w zadanym katalogu
     * @return Tablica nazw plików w katalogu
     */
    public String[] PodajTablicęNazw()
    {
        String[] tab;
        String rozdzielacz;
        String pomoc;
        int i;
        
        tab = TablicaPlików;
        
        rozdzielacz = File.separator;
        
        if(StanListy == ListaPlików.ZNALEIZONO_PLIKI_CSV)
        {
            pomoc = NazwaKatalogu.substring(NazwaKatalogu.length()-1);
            
            if(pomoc.compareTo(rozdzielacz) == 0)
            {
                pomoc = NazwaKatalogu.substring(0, NazwaKatalogu.length()-1);
            }else
            {
                pomoc  = NazwaKatalogu;
            }//end of
            
            for(i = 0; i < tab.length; i++)
            {
                tab[i] = pomoc + rozdzielacz + tab[i];
            }//next i
            
        }//end if
        
        
        return tab;
        
    }//Koniec metody zwracającej tablicę nazw
    
}//Koniec klasy
