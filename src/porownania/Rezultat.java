
package porownania;

import Motywy.Fragmenty;
import Motywy.ParaMotywów;
import java.util.ArrayList;

/**
 * Klasa służąca do przechowywania wyniku poszukiwań motywów.
 * @author Piotr Wrzeciono
 */
public class Rezultat
{
    /**Liczba zdarzeń wykorzystanych w poszukiwaniach*/
    private final int LiczbaZdarzeń;
    /**Posortowana tablica par motywów znalezionych*/
    private final ParaMotywów[] TablicaWynikowa;
    /**Numer systemu. Wartość 0 oznacza wszystkie głosy razem. Wartości od 1 w górę oznaczają poszczególne głosy, liczone od basu.*/
    private final int NumerSystemu;
    /**Lista par motywów o różnicy 0*/
    private final ArrayList<ParaMotywów> ListaZerowychRóżnic;
    /**Oznaczenie literowe systemu*/
    String OznaczenieSystemu;
    
    /**
     * Konstruktor klasy
     * @param liczba Liczba zdarzeń wykorzystanych do wyszukiwania motywu
     * @param wynik Posortowana tablica z wynikami
     * @param system System (numer głosu). Liczba 0 oznacza wszystkie głosy razem, liczba 1 oraz większa od 1 oznacza poszczególny głos. Bas ma numer 1.
     * @param nazwa_systemu Tekstowa nazwa systemu, np. S1
     */
    public Rezultat(int liczba, ParaMotywów[] wynik, int system, String nazwa_systemu)
    {
        LiczbaZdarzeń = liczba;
        TablicaWynikowa = wynik;
        NumerSystemu = system;
        OznaczenieSystemu = nazwa_systemu;
        
        ListaZerowychRóżnic = new ArrayList<>();
        
        UtwórzListęZerowychRóżnic();
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca literowe oznaczenie systemu
     * @return Literowe oznaczenie systemu, np. S1.
     */
    public String getNazwęTekstowąSystemu()
    {
        return OznaczenieSystemu;
    }
    
    /**
     * Metoda służąca do wyprowadzenia par motywów identycznych.
     */
    private void UtwórzListęZerowychRóżnic()
    {
        int i;
        
        for(i = 0; i < TablicaWynikowa.length; i++)
        {
            if(Math.abs(TablicaWynikowa[i].getRóżnicę()) < 0.0001)
            {
                ListaZerowychRóżnic.add(TablicaWynikowa[i]);
            }//end if
            
        }//next i
        
    }//Koniec metody tworzenia listy par zerowych różnic.
    
    /**
     * Metoda zwracająca liczbę zdarzeń Humdrum wykorzystaną do poszukiwania motywu.
     * @return Liczba zdarzeń
     */
    public int getLiczbęZdarzeń()
    {
        return LiczbaZdarzeń;
    }
    
    /**
     * Metoda zwracająca tablicę wynikową z parami motywów.
     * @return Tablica wynikowa
     */
    public ParaMotywów[] getTablicęWyników()
    {
        return this.TablicaWynikowa;
    }
    
    /**
     * Metoda zwracająca numer systemu (głosu), w którym odbywało się wyszukiwanie.
     * @return Numer systemu. Wartość 0 oznacza wszystkie głosy razem, wartość 1 i większa oznacza kolejne głosy. Bas ma numer 1.
     */
    public int getNumerSystemu()
    {
        return NumerSystemu;
    }
    
    /**
     * Metoda zwracająca sformatowany odpowiednio tekst do zapisu do pliku CSV
     * @return Sformatowany tekst.
     */
    @Override
    public String toString()
    {
        String opis;
        int i;
        int ind_m;
        int i_zerowe;
        ParaMotywów para_m;
        
        
        if(NumerSystemu != 0)
        {
            opis = "Numer systemu:\t" + NumerSystemu + "\t\n";
            opis += "Nazwa systemu:\t" + OznaczenieSystemu + "\t\n";
        }else
        {
            opis = "Wszystkie systemy razem połączone\n";
        }//end if
        
        opis += "Liczba zdarzeń:\t" + LiczbaZdarzeń + "\t\n";
        opis += "Liczba par motywów:\t" + TablicaWynikowa.length + "\t\n";
        opis += "Liczba par motywów o różnicy zerowej:\t" + ListaZerowychRóżnic.size() + "\t\n";
        opis += "W nawiasach podano numer taktu\n";
        
        /* To na razie jest usunięte.....
        if(ListaZerowychRóżnic.size() > 0)
        {
            opis += "\n";
            opis += "Lista identycznych motywów:\n";
            opis += "Motyw 1\t Motyw 2\t Różnica\t Koniec motywu 1\n";
            
            for(i_zerowe = 0; i_zerowe < ListaZerowychRóżnic.size(); i_zerowe++)
            {
                
                para_m = ListaZerowychRóżnic.get(i_zerowe);
                
                opis += para_m.getMotyw1().getLokalizacjęPoczątku() + "(" + para_m.getMotyw1().getReferencjęDoMotywu()[0].getZdarzenie().getTakt() + ")" + "\t";
                opis += para_m.getMotyw2().getLokalizacjęPoczątku() + "(" + para_m.getMotyw2().getReferencjęDoMotywu()[0].getZdarzenie().getTakt() + ")" + "\t";
                opis += para_m.getRóżnicę() + "\t";
            
                opis += para_m.getMotyw1().getLokalizacjęKońca();
                ind_m = para_m.getMotyw1().getReferencjęDoMotywu().length - 1;
            
                opis += "(" + para_m.getMotyw1().getReferencjęDoMotywu()[ind_m].getZdarzenie().getTakt() + ")\n";
                
            }//next i_zerowe
            
            opis += "\n";
            
        }//end if
        
        opis += "\nLista wszystkich podobnych motywów\n";
        opis += "Motyw 1\t Motyw 2\t Różnica\t Koniec motywu 1\n";
        
        
        for(i = 0; i < TablicaWynikowa.length; i++)
        {
            
            opis += TablicaWynikowa[i].getMotyw1().getLokalizacjęPoczątku() + "(" + TablicaWynikowa[i].getMotyw1().getReferencjęDoMotywu()[0].getZdarzenie().getTakt() + ")" + "\t";
            opis += TablicaWynikowa[i].getMotyw2().getLokalizacjęPoczątku() + "(" + TablicaWynikowa[i].getMotyw2().getReferencjęDoMotywu()[0].getZdarzenie().getTakt() + ")" + "\t";
            opis += TablicaWynikowa[i].getRóżnicę() + "\t";
            
            opis += TablicaWynikowa[i].getMotyw1().getLokalizacjęKońca();
            ind_m = TablicaWynikowa[i].getMotyw1().getReferencjęDoMotywu().length - 1;
            
            opis += "(" + TablicaWynikowa[i].getMotyw1().getReferencjęDoMotywu()[ind_m].getZdarzenie().getTakt() + ")\n";
            
        }//next i
        */
        
        //Na razie wyprowadzamy tylko macierz jako najlepszy sposób prezentacji!!! (2020-12-13)
        
        opis += Fragmenty.UtwórzMacierzMotywów(TablicaWynikowa);
        
        return opis;
    }//Koniec metody toString
    
}//Koniec klasy

