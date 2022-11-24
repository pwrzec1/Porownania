
package Testy;

import Motywy.Motyw;
import Motywy.PorównanieZZadanym;
import Motywy.WyjątekDlaWierszaZMotywem;
import Struktury.Arkusz;
import Struktury.ArkuszZdarzenie;
import Struktury.Interpreter;
import Struktury.PlikCSV;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Testowanie wyszukiwania zadanego motywu
 * @author Piotr Wrzeciono
 */
public class TestWyszukiwaniaMotywu
{
    public static void main(String[] arghh)
    {
        String nazwa;
        Arkusz zawartość_pliku;
        PlikCSV plik;
        int kodowanie;
        Interpreter interpret;
        ArkuszZdarzenie[] całość;
        double waga_tonalna;
        double waga_rytmiczna;
        double max_różnica;
        Motyw motyw_do_szukania;
        String motyw_do_szukania_s;
        PorównanieZZadanym porównywarka;
        
        waga_tonalna = 0.2;
        waga_rytmiczna = 0.1;
        max_różnica = 0.8;
        
        
        nazwa = "WTC/WTC Test/WTC1f01p2.csv";
        kodowanie = PlikCSV.UTF8;
        
        plik = new PlikCSV(nazwa, kodowanie);
        
        zawartość_pliku = plik.WczytajDane();
        
        System.out.println(plik.getOpisStanuPliku());
        
        interpret = new Interpreter(zawartość_pliku);
        całość = interpret.getCałośćPołączoną(waga_tonalna, waga_rytmiczna);
        
        System.out.println("Liczba elementów w całości: " + całość.length);
        
        motyw_do_szukania = null;
/*        
5       5 	5	5	6	6	6	6	6	6	6	6	6	6
48	50	52	53	55	53	52	57	50	55	57	55	53	52
0	2	2	2	2	-2	-2	4	-5	4	2	-2	-2	-2
74	76	78	80	83	83	5	84	86	88	90	93	94	95
2	2	2	3	0.5	0.5	2	2	2	3	1	1	1	1
P       P       P       P       P       P       P       P       P       P       P       P       P       
*/

        motyw_do_szukania_s = "5,48,0,74,2 P 5,50,2,76,2 P 5,52,2,78,2 P 5,53,2,80,3 P 6,55,2,83,0.5 P 6,53,-2,83,0.5 P 6,52,-2,5,2 P 6,57,4,84,2 P 6,50,-5,86,2 P 6,55,4,88,3 P 6,57,2,90,1 P 6,55,-2,93,1 P 6,53,-2,94,1 P 6,52,-2,95,1";
        
        try {
            motyw_do_szukania = new Motyw(motyw_do_szukania_s, waga_tonalna, waga_rytmiczna);
            
            motyw_do_szukania.getArkuszZMotywu().PokażArkusz();
            
            
        } catch (WyjątekDlaWierszaZMotywem ex) {
            
            System.out.println(ex);
        }
        
        porównywarka = new Motywy.PorównanieZZadanym(całość, motyw_do_szukania, motyw_do_szukania_s, waga_tonalna, waga_rytmiczna, max_różnica);
            
        porównywarka.ZrealizujPorównanie();
        
        
        System.out.println(porównywarka);
        
    }//Koniec main
    
}//Koniec klasy
