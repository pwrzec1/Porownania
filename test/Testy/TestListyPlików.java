
package Testy;

import porownania.ListaPlików;



/**
 * Test listy plików CSV
 * @author Piotr Wrzeciono
 */
public class TestListyPlików
{
    public static void main(String[] arg)
    {
        ListaPlików lista;
        String[] wynik;
        String katalog;
        int i;
        String pom;
        
        katalog = "/home/suigan/Dokumenty/SGGW/semestr zimowy 2019／20/PJTAK/Humdrum/WTC/I/CSV1";
        
        lista = new ListaPlików(katalog);
        
        System.out.println("Stan listy: " + lista);
        
        if(lista.getStanListy() == ListaPlików.ZNALEIZONO_PLIKI_CSV)
        {
            wynik = lista.PodajTablicęNazw();
            
            for(i = 0; i < wynik.length; i++)
            {
                System.out.println("wynik[" + i + "] = " + wynik[i]);
            }//next i
            
        }//end if
        
    }//Koniec metody main
    
}//Koniec klasy
