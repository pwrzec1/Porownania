
package ListaPodobienstw;

/**
 * Klasa służąca do obsługi listy podobieństw.
 * @author Piotr Wrzeciono
 * @param <T> Klasa implementująca interfejs Identyfikacja
 * @see ListaPodobienstw.Identyfikacja
 */
public class ListaP<T extends Identyfikacja>
{
    /**Lista właściwa*/
    private ElementListy<T> ListaWłaściwa;
    
    /**
     * Konstruktor listy.
     */
    public ListaP()
    {
        ListaWłaściwa = null;
    }//Koniec konstruktora
    
    /**
     * Metoda dopisująca nowy elemenyt do listy (jeżeli jest nowy).
     * @param obiekt Nowy obiekt do zapamiętania
     * @return <b>true</b>, gdy zapis się powiódł, <b>false</b> w innym przypadku.
     */
    public boolean Dopisz(T obiekt)
    {
        boolean rezultat;
        
        rezultat = true;
        
        if(ListaWłaściwa == null)
        {
            ListaWłaściwa = new ElementListy<>(obiekt);
        }else
        {
            rezultat = ListaWłaściwa.Dopisz(obiekt);
        }//end if
        
        return rezultat;
    }//Koniec dopisywania
    
    /**
     * Metoda sprawdzająca, czy dany element jest w liście.
     * @param obiekt Obiekt do sprawdzenia
     * @return <b>true</b>, gdy obiekt jest, <b>false</b> w innym przypadku (nawet, jak lista jest pusta).
     */
    public boolean CzyJest(T obiekt)
    {
        boolean wynik;
        
        wynik = false;
        
        if(ListaWłaściwa != null)
        {
            wynik = ListaWłaściwa.CzyJest(obiekt);
        }//end if
        
        return wynik;
    }//Koniec wyszukiwania
    
    /**
     * Metoda zwracająca informację o tym, czy lista jest pusta.
     * @return <b>true</b>, gdy lista jest pusta. W innym przypadku zwraca wartość <b>false</b>.
     */
    public boolean CzyPusta()
    {
        return ListaWłaściwa == null;
    }//Koniec metody
    
    
    /**
     * Metoda generująca opis listy w stylu arkusza kalkulacyjnego.
     * @return Opis listy.
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "";
        
        if(ListaWłaściwa != null)
        {
            opis = ListaWłaściwa.GenerujOpis();
        }
        
        return opis;
    }//Koniec metody toString
    
    /**
     * Metoda służąca do usuwania listy z pamięci RAM (zabezpieczenie przed wyciekiem pamięci).
     */
    public void UsuńListę()
    {
        if(ListaWłaściwa != null)
        {
            ListaWłaściwa.UsuńListę();
            
            ListaWłaściwa = null; //To jest potrzebne na koniec!
            
        }//end if
        
    }//Koniec usuwania listy z pamięci
    
    /**
     * Metoda zwracająca listę przekształconą w tablicę.
     * @return Tablica uporządkowana od najmłodszego do nastarszego elementu
     */
    public T[] getTablicę()
    {
        T[] tablica;
        
        tablica = null;
        
        if(ListaWłaściwa != null)
        {
            tablica = ListaWłaściwa.getTablicę();
        }
        
        return tablica;
    }//Koniec metody zwracającej tablicę
    
    /**
     * Metoda zwracająca liczbę elementów w liście.
     * @return Liczba elementów. Gdy wartość zwracana wynosi 0, to lista jest pusta.
     */
    public int getLiczbęElementów()
    {
        int ile;
        
        ile = 0;
        
        if(ListaWłaściwa != null)
        {
            ile = ListaWłaściwa.IleElementów();
        }//end if
        
        return ile;
    }//Koniec metody
    
}//Koniec klasy
