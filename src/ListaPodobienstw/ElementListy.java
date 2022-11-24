
package ListaPodobienstw;

/**
 * Element listy do przechowywania takich samych obiektów (referencja)
 * @author Piotr Wrzeciono
 * @param <T> Klasa implementująca interfejs Identyfikacja
 * @see ListaPodobienstw.Identyfikacja
 */
public class ElementListy<T extends Identyfikacja>
{
    private final T Referencja;
    private ElementListy<T> Następny;
    private final Class<T> KlasaObiektu;
    
    /**
     * Konstruktor elementu listy.
     * @param obiekt Referencja do obiektu
     */
    @SuppressWarnings("unchecked")
    public ElementListy(T obiekt)
    {
        Referencja = obiekt;
        Następny = null;
        KlasaObiektu = (Class<T>) obiekt.getClass();
        
    }//Koniec konstruktora
    
    /**
     * Metoda sprawdzająca, czy dany obiekt jest już w liście
     * @param obiekt Obiekt do sprawdzenia
     * @return <b>true</b>, gdy dany obiekt już jest, <b>false</b>, gdy nie ma.
     */
    public boolean CzyJest(T obiekt)
    {
        boolean wynik;
        
        wynik = false;
        
        if(obiekt.getIdentyfikatorTożsamości().compareTo(Referencja.getIdentyfikatorTożsamości()) == 0)
        {
            wynik = true;
        }else
        {
            if(Następny != null) wynik = Następny.CzyJest(obiekt);
        }//end if
        
        return wynik;
    }//Koniec  metody wyszukującej
    
    /**
     * Metoda dopisująca obiekt do listy, pod warunkiem, że już jest identyczny.
     * @param obiekt Obiekt do zapamiętania
     * @return <b>true</b>, gdy dopisanie się powiodło, <b>false</b> w innym przypadku.
     */
    public boolean Dopisz(T obiekt)
    {
        boolean wynik;
        
        wynik = CzyJest(obiekt);
        
        if(wynik)
        {
            DopiszDoKońcaListy(obiekt);
        }//end if
        
        return wynik;
    }//Koniec dopisywania
    
    /**
     * Metoda realizująca dopisywanie rekurencyjne do końca listy. 
     * Metoda ta musi być wywoływana z poziomu metody Dopisz.
     * @see ElementListy.Dopisz
     * @param obiekt Obiekt do zapisu
     */
    private void DopiszDoKońcaListy(T obiekt)
    {
        if(Następny == null)
        {
            Następny = new ElementListy<>(obiekt);
        }else
        {
            Następny.DopiszDoKońcaListy(obiekt);
        }//end if
    }//Koniec metody dopisującej.
    
    /**
     * Metoda służąca do usuwania listy.
     */
    public void UsuńListę()
    {
        if(Następny != null)
        {
            Następny.UsuńListę();
        }//end if
        
        Następny = null;
    }//Koniec metody usuwającej listę
    
    /**
     * Metoda generująca listę przygotowaną dla arkusza kalkulacyjnego.
     * @return Tekst listy.
     */
    public String GenerujOpis()
    {
        String opis;
        
        opis = Referencja.getOpisObiektu();
        
        if(Następny == null)
        {
            opis += "\n";
        }else
        {
            opis += "\t" + Następny.GenerujOpis();
        }//end if
        
        return opis;
    }//Koniec metody generującej opis tekstowy listy
    
    /**
     * Metoda służąca do liczenia elementów listy.
     * @param licznik Obiekt służący do przechowywania wartości typu <b>int</b>.
     */
    private void PoliczElementy(WartośćLicznika licznik)
    {
        licznik.Wartość++;
        
        if(Następny != null) Następny.PoliczElementy(licznik);
        
    }//Koniec metody liczącej elementy tablicy
    
    /**
     * Metoda zwracająca liczbę elementów w liście.
     * @return Liczba elementów w liście.
     */
    public int IleElementów()
    {
        WartośćLicznika licznik;
        
        licznik = new WartośćLicznika();
        
        PoliczElementy(licznik);
        
        return licznik.Wartość;
    }//Koniec metody zwracającej liczbę elementów
    
    /**
     * Metoda przepisująca obiekty z listy do tablicy.
     * @param Tablica z elementami listy (od najstarszego do najmłodszego).
     * @param licznik Licznik do liczenia indeksów tablicy.
     */
    private void PrzepiszObiektyDoTablicy(T[] Tablica, WartośćLicznika licznik)
    {
        Tablica[licznik.Wartość] = this.Referencja;
        
        if(Następny != null)
        {
            licznik.Wartość++;
            PrzepiszObiektyDoTablicy(Tablica, licznik);
        }//end if
        
    }//Koniec przepisywania
    
    /**
     * Metoda zwracająca tablicę z elementami listy.
     * @return Tablica z elementami listy od najstarszego do najmłodszego.
     */
    @SuppressWarnings("unchecked")
    public T[] getTablicę()
    {
        int liczba_elementów;
        T[] tablica;
        WartośćLicznika licznik;
        
        licznik = new WartośćLicznika();
        
        this.PoliczElementy(licznik);
        liczba_elementów = licznik.Wartość;
        
        tablica = (T[])java.lang.reflect.Array.newInstance(KlasaObiektu, liczba_elementów);
        
        licznik.Wartość = 0; //Zaczynamy od indeksu zerowego
        
        this.PrzepiszObiektyDoTablicy(tablica, licznik);
        
        return tablica;
        
    }//Koniec tworzenia tablicy
    
}//Koniec klasy

/**
 * Klasa pomocnicza służąca do przechowywania i zmiany licznika elementów w tablicy.
 * @author Piotr Wrzeciono
 */
class WartośćLicznika
{
    /**Przechowywana wartość*/
    public int Wartość;
    
    /**Konstruktor klasy.*/
    public WartośćLicznika()
    {
        Wartość = 0;
    }
    
}//Koniec klasy