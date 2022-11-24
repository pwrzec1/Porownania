
package Testy;

import Motywy.LiczeniePrzedziałówWątek;

/**
 * Test liczenia przedziałów do wielowątkowości
 * @author Piotr Wrzeciono
 */
public class TestLiczeniaPrzedziałów
{
    public static void main(String[] arghh)
    {
        int liczba_elementów;
        int i;
        
        int początek;
        int koniec;
        int główny;
        
        int kwant;
        
        LiczeniePrzedziałówWątek liczenie;
        
        liczba_elementów = 31;
        główny = 0;
        
        liczenie = new LiczeniePrzedziałówWątek();
        
        for(i = 0; i < liczenie.LiczbaRdzeni(); i++)
        {
            początek = liczenie.getIndeksPoczątkowy(główny, i, liczba_elementów);
            koniec = liczenie.getIndeksKońcowy(główny, i, liczba_elementów);
            
            System.out.println("i  = " + i + ", początek = " + początek + ", koniec = " + koniec);
        }//next i
        
        
    }//Koniec main
    
}//Koniec klasy
