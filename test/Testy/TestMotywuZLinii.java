
package Testy;

import Motywy.WyjątekDlaWierszaZMotywem;

/**
 * Test generowania motywu (obiektu klasy Motywy.Motyw) z linii tekstu.
 * Testowanie drugiego konstruktora klasy Motyw.
 * @author Piotr Wrzeciono
 */
public class TestMotywuZLinii
{
    public static void main(String[] arghhh)
    {
        String tekst;
        Motywy.Motyw motyw_testowany;
        int ile;
        int i;
        Struktury.ArkuszZdarzenie[] zdarzonka;
        double waga_interwałowa = 0.6;
        double waga_czasowa = 0.8;
        
        zdarzonka = null;
        motyw_testowany = null;
        
        tekst = "1,58,0,0,16 P 1,60,2,16,16";
        
        try
        {
            motyw_testowany = new Motywy.Motyw(tekst, waga_interwałowa, waga_czasowa);
        }catch(WyjątekDlaWierszaZMotywem ex)
        {
            System.out.println(ex);
        }
        
        zdarzonka = motyw_testowany.getReferencjęDoMotywu();
        ile = zdarzonka.length;
        
        System.out.println("Tekst do interpretacji ..... " + tekst);
        System.out.println("waga interwałowa ........... " + waga_interwałowa);
        System.out.println("waga czasowa ............... " + waga_czasowa);
        
        for(i = 0; i < ile; i++)
        {
            System.out.println(zdarzonka[i].getZdarzenie());
        }//next i
        
    }//Koniec main
    
}//Koniec klasy
