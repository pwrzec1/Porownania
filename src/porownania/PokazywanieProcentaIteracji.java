
package porownania;

import Motywy.Fragmenty;
import javax.swing.JProgressBar;

/**
 * Klasa pomocnicza, służąca do pokazywania procenta iteracji;
 * @author suigan
 */
public class PokazywanieProcentaIteracji implements Runnable
{
    /**Referencja do paska progresu iteracji*/
    private final JProgressBar Pasek;
    /**Referencja do obiektu tworzącego porównania */
    private final Fragmenty Referencja;
    /**Stan wątku - sterowanie*/
    private volatile boolean czy_działać;
    
    /**
     * Konstruktor klasy
     * @param pasek Pasek do pokazywania procentu liczby iteracji
     * @param ref Referncja do obiektu porównującego
     */
    public PokazywanieProcentaIteracji(JProgressBar pasek, Fragmenty ref)
    {
        Pasek = pasek;
        Referencja = ref;
        
        Pasek.setMinimum(0);
        Pasek.setMaximum(100);
        Pasek.setStringPainted(true);
        
        czy_działać = true;
        
    }//Koniec konstruktora

    /**
     * Metoda główna sterująca wypisywaniem.
     */
    @Override
    public void run()
    {
        do{
            try{
                Thread.sleep(10); //Spanie na 0.1 sekundy
            }catch(Exception ex)
            {
                System.out.println("Błąd: " + ex);
            }
        
            Pasek.setValue(Referencja.getProcentIteracji());
            
        }while(czy_działać);
        
    }//Koniec metody run
    
    /**
     * Metoda zatrzymująca wyświetlanie progresu.
     */
    public void Stop()
    {
        czy_działać = false;
    }//Koniec metody zatrzymującej wyświetlanie progresu.
    
}//Koniec klasy
