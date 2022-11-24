/**
 * Pakiet będący implementacją listy służącej do przechowywania referencji do obiektów identycznych.
 * @author Piotr Wrzeciono
 */
package ListaPodobienstw;

/**
 * Interfejs zawierający metodę zwracającej tekstowy identyfikator podobieństwa.
 * @author Piotr Wrzeciono
 */
public interface Identyfikacja<T>
{
    /**
     * Metoda zwracająca tekstowy identyfikator tożsamości obiektu.
     * @return Identyfikator
     */
    public String getIdentyfikatorTożsamości();
    
    /**
     * Metoda tworząca tekstowy identyfikator tożsamości obiektu.
     * @param obiekt Obiekt dla którego ma być tworzony Identyfikator.
     */
    public void TwórzIdentyfikatorTożsamości(T obiekt);
    
    /**
     * Metoda zwracająca opis obiektu potrzebny do generacji opisu.
     * @return Opis obiektu.
     */
    public String getOpisObiektu();
    
}//Koniec interfejsu
