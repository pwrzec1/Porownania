/**
 * Pakiet służący do implementacji binarnego drzewa poszukiwań BST.
 */
package BST;

import java.util.LinkedList;

/**
 * Implementacja wierzchołka drzewa BST
 * @author Piotr Wrzeciono
 * @param <T> Typ dowolny, ale implementujący interfejs Comparable
 */
public class WierzchołekBST<T extends Comparable<T>>
{
    /**Referencja do lewego poddrzewa*/
    private WierzchołekBST<T> Lewy;
    /**Referencja do prawego poddrzewa*/
    private WierzchołekBST<T> Prawy;
    /**Referencja do wartości*/
    private T Wartość;
    
    /**
     * Konstruktor klasy Wierzchołek BST
     * @param ref Referencja do obiektu
     */
    public WierzchołekBST(T ref)
    {
        Lewy = null;
        Prawy = null;
        Wartość = ref;
    }//Koniec konstruktora
    
    /**
     * Metoda dopisująca do drzewa BST
     * @param ref Referencja do zapisu
     */
    public void Dopisz(T ref)
    {
        int porównanie;
        
        porównanie = this.Wartość.compareTo(ref);
        
        if(porównanie > 0) //Idziemy na prawo
        {
            if(Prawy != null)
            {
                Prawy = new WierzchołekBST<>(ref);
            }else
            {
                Prawy.Dopisz(ref);
            }//end if
            
        }//end if
        
        if(porównanie < 0) //Dopisujemy do lewego
        {
            if(Lewy == null)
            {
                Lewy = new WierzchołekBST<>(ref);
            }else
            {
                Lewy.Dopisz(ref);
            }//end if
            
        }//end if
        
    }//Koniec metody dopisującej
    
    /**
     * Metoda zwracająca uporządkowane obiekty według poruszania się po drzewie in order.
     * @param Lista Lista do przechowywania wyników chodzenia po drzewie
     */
    public void in_order(LinkedList<T> Lista)
    {
        if(Lewy != null)
        {
            Lewy.in_order(Lista);
        }
        
        Lista.add(Wartość);
        
        if(Prawy != null)
        {
            Prawy.in_order(Lista);
        }
    }//Koniec metody poruszającej się po drzewie jak in order
    
    /**
     * Metoda usuwająca drzewo BST z pamięci RAM, wykorzystując chodzenie po drzewie post oreder.
     */
    public void UsuńDrzewo()
    {
        if(Lewy != null) Lewy.UsuńDrzewo();
        if(Prawy != null) Prawy.UsuńDrzewo();
        
        Lewy = null;
        Prawy = null;
    }//Koniec metody usuwającej drzewo z pamięci RAM
    
    
}//Koniec klasy wierzchołka BST
