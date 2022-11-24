
package BST;

import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 * Klasa implementująca drzewo BST.
 * 
 * Klasa obudowuje klasę WierzchołekBST.
 * @see WierzchołekBST
 * 
 * @author Piotr Wrzeciono
 */
public class DrzewoBST<T extends Comparable<T>>
{
    /**Referencja (wskaźnik) do korzenia drzewa BST*/
    private WierzchołekBST<T> Korzeń;
    
    /**
     * Konstruktor klasy.
     */
    public DrzewoBST()
    {
        Korzeń = null;
    }//Koniec konstruktora
    
    public void Dopisz(T ref)
    {
        if(Korzeń == null)
        {
            Korzeń = new WierzchołekBST<>(ref);
        }else
        {
            Korzeń.Dopisz(ref);
        }//end if
        
    }//Koniec dopisywania
    
    @SuppressWarnings("unchecked")
    public T[] getPosortowaneElementy()
    {
        T[] wynik;
        LinkedList<T> lista_pomocnicza;
        Class pom;
        int i;
        
        wynik = null;
        
        
        if(Korzeń != null)
        {
            lista_pomocnicza = new LinkedList<>();
            
            Korzeń.in_order(lista_pomocnicza);
            
            pom = lista_pomocnicza.get(0).getClass(); //Mechanizm tzw. refleksji
            
            wynik = (T[])Array.newInstance(pom, lista_pomocnicza.size());
            
            for(i = 0; i < wynik.length; i++)
            {
                wynik[i] = lista_pomocnicza.get(i);
            }//next i
            
        }//end if
        
        
        return wynik;
    }//Koniec metody zwracającej posortowane elementy
    
}//Koniec klasy
