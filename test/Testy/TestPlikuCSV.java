
package Testy;

import Struktury.Arkusz;
import Struktury.Interpreter;
import Struktury.PlikCSV;

/**
 * Testowanie pliku CSV - wczytania danych
 * @author Piotr Wrzeciono
 */
public class TestPlikuCSV
{
    public static void main(String[] arg)
    {
        String nazwa;
        Arkusz zawartość_pliku;
        PlikCSV plik;
        int kodowanie;
        Interpreter Odczytywacz;
        
        nazwa = "WTC1f01.csv"; //Plik do testów
        kodowanie = PlikCSV.CP1250;
        
        plik = new PlikCSV(nazwa, kodowanie);
        
        zawartość_pliku = plik.WczytajDane();
        
        System.out.println("Status pliku: " + plik.getOpisStanuPliku());
        
        
        if(plik.getStanPliku() == PlikCSV.PLIK_WCZYTANO)
        {
            zawartość_pliku.PokażArkusz();
            
            Odczytywacz = new Interpreter(zawartość_pliku);
            
            Odczytywacz.PokażSystemy();
            
            Odczytywacz.PokażTablicęSłowaTakt();
            
            System.out.println("\n\n--------Arkusz analizowany:-------------\n");
            
            
            Odczytywacz.PokażListęBłędów();
        }//end if
        
    }//Koniec metody main
    
}//Koniec klasy
