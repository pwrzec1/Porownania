
package Testy;

import Struktury.*;
import Motywy.*;
import java.util.Arrays;

/**
 * Klasa służąca do testowania miary motywów;
 * @author Piotr Wrzeciono
 */
public class TestMiaryMotywów
{
    public static void main(String[] arghh)
    {
        PlikCSV plik;
        String nazwa_pliku;
        Arkusz arkusz_z_pliku;
        Interpreter wyciągacz;
        int ile_głosów;
        ArkuszZdarzenie[] całość_głosu;
        Fragmenty fragmenciki;
        String[] opisy;
        ZnajdowanieMotywów Szukacz;
        
        double maks_różnica;
        double waga_interwałowa;
        double waga_metryczna;
        int ile_zdarzeń;
        int nr_systemu;
        
        ParaMotywów[] tab_z_motywami;
        
        int i;
        
        nazwa_pliku = "WTC/I/CSV1/WTC1f01p2.csv";
        
        maks_różnica = 3;
        waga_interwałowa = 0.5;
        waga_metryczna = 0.1;
        ile_zdarzeń = 14;
        nr_systemu = 2;
        
        plik = new PlikCSV(nazwa_pliku, PlikCSV.CP1250);
        arkusz_z_pliku = plik.WczytajDane();
        
        if(plik.getStanPliku() != PlikCSV.PLIK_WCZYTANO)
        {
            System.out.println(plik);
        }else
        {
            wyciągacz = new Interpreter(arkusz_z_pliku);
            ile_głosów = wyciągacz.getLiczbęGłosów();
            
            System.out.println("Liczba głosów ........ " + ile_głosów);
            
            całość_głosu = wyciągacz.getGłos(nr_systemu, waga_interwałowa, waga_metryczna); 
            
            System.out.println("Liczba zdarzeń ...... " + całość_głosu.length);
            
            fragmenciki = new Fragmenty(całość_głosu, ile_zdarzeń, false);
            
            fragmenciki.UtwórzListę(maks_różnica);
            
            tab_z_motywami = fragmenciki.getParyMotywów(maks_różnica);
            
            opisy = Fragmenty.GenerujTablicęOpisu(tab_z_motywami);
            
            System.out.println("Motyw 1\tMotyw 2\tRóżnica\tKoniec motywu\tDługość motywu");
            
            for(i = 0; i < opisy.length; i++)
            {
                System.out.println(opisy[i]);
            }
            
            //---------------------------------------------------------------------------------
            
            System.out.println("-----------------------------------");
            
            Szukacz = new Motywy.ZnajdowanieMotywów(całość_głosu, maks_różnica);
            
            Szukacz.Szukaj(maks_różnica, false);
            
            tab_z_motywami = Szukacz.getZnalezioneParyMotywów();
            
            System.out.println("Maksymalna długość motywu ........ " + Szukacz.getDługośćMotywu());
            
            opisy = Fragmenty.GenerujTablicęOpisu(tab_z_motywami);
            
            System.out.println("Motyw 1\tMotyw 2\tRóżnica\tKoniec motywu\tDługość motywu");
            
            for(i = 0; i < opisy.length; i++)
            {
                System.out.println(opisy[i]);
                
                tab_z_motywami[i].UstawRodzajPorównywania(ParaMotywów.COMPARABLE_COLUMN);
            }
            
            
            Arrays.sort(tab_z_motywami);
            
            System.out.println("\n\n*****************************************\n");
            
            opisy = Fragmenty.GenerujTablicęOpisu(tab_z_motywami);
            
            System.out.println("Motyw 1\tMotyw 2\t Różnica");
            
            for(i = 0; i < opisy.length; i++)
            {
                System.out.println(opisy[i]);
            }
            
        }//end if
        
    }//Koniec metody
    
}//Koniec klasy
