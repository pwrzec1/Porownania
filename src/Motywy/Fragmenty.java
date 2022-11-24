
package Motywy;

import Struktury.ArkuszZdarzenie;
import Struktury.Zdarzenie;
import java.util.Arrays;
import java.util.ArrayList;



/**
 * Klasa generująca fragmenty systemu.
 * 
 * Każdy fragment systemu (głosu) składa się z tej samej liczby zdarzeń systemu Humdrum.<br>
 * Fragmenty są tworzone ze skokiem co 1.
 * @author Piotr Wrzeciono
 */
public class Fragmenty
{
    /**Tablica przechowująca fragmenty*/
    private volatile Motyw[] FragmentyGłosu;
    /**Liczba zdarzeń we fragmencie*/
    private final int LiczbaZdarzeń;
    /**Tablica porównań motywów parami*/
    private volatile ArrayList<ParaMotywów> ListaParMotywów;
    
    /**Liczba wszystkich iteracji przy wyszukiwaniu podobnych motywów*/
    private volatile int LiczbaWszystkichIteracji;
    /**Licznik iteracji podczas porównywania*/
    private volatile int LicznikIteracji;
    /**Czy drugi motyw ma być krabem?*/
    private final boolean CzyKrab;
    
    /**
     * Konstruktor klasy
     * @param całość Wszystkie zdarzenia z jednego systemu
     * @param ile Liczba zdarzeń we fragmencie
     * @param krab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public Fragmenty(ArkuszZdarzenie[] całość, int ile, boolean krab)
    {
        int i;
        int max;
        
        max = 1 + całość.length - ile; //Liczba fragmentów o tej samej długości;
        
        FragmentyGłosu = new Motyw[max];
        
        LiczbaZdarzeń = ile;
        CzyKrab = krab;
        
        for(i = 0; i < max; i++)
        {
            FragmentyGłosu[i] = new Motyw(całość, i, ile, krab);
            
        }//next i
        
        ListaParMotywów = new ArrayList<>();
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca liczbę zdarzeń w danym fragmentach.
     * @return Liczba zdarzeń humdrum
     */
    public int getLiczbęZdarzeń()
    {
        return this.LiczbaZdarzeń;
    }//Koniec metody
    
    /**
     * Metoda tworząca listę par motywów.
     * @param maksimum Maksymalna różnica do zapamiętania.
     */
    public void UtwórzListę(double maksimum)
    {
        int i;
        int k;
        ParaMotywów nowa;
        
        LiczbaWszystkichIteracji = FragmentyGłosu.length * (FragmentyGłosu.length - 1)/2;
        LicznikIteracji = 0;
        
        for(i = 0; i < FragmentyGłosu.length - 1; i++)
        {
            for(k = i + 1; k < FragmentyGłosu.length; k++)
            {
                LicznikIteracji++;
                if(i != k)
                {

                    nowa = new ParaMotywów(FragmentyGłosu[i], FragmentyGłosu[k], CzyKrab);
                    
                    if(nowa.getRóżnicę() <= maksimum)
                    {
                        ListaParMotywów.add(nowa);
                    }
                    
                }//end if
                
            }//next k
        }//next i
        
        //Collections.sort(ListaParMotywów);
        
    }//Koniec tworzenia listy
    
    /**
     * Metoda wielowątkowego tworzenia listy.
     * @param maksimum Maksymalna różnica do zapamiętania.
     */
    public void UtwórzListęWątkowo(double maksimum)
    {
        int i;
        int początek;
        int koniec;
        int rdzeń;
        Thread[] wątki;
        LiczeniePrzedziałówWątek liczenie;
        TworzenieParWątkowe[] tworzenie;
        int licznik;
        
        int[] tab_licznikowa;
        
        boolean czy_koniec;
        
        LiczbaWszystkichIteracji = FragmentyGłosu.length * (FragmentyGłosu.length - 1)/2;
        LicznikIteracji = 0;
        
        liczenie = new LiczeniePrzedziałówWątek(); //Pobieramy liczbę rdzeni
        
        tab_licznikowa = new int[liczenie.LiczbaRdzeni()];
        
        
        for(i = 0; i < FragmentyGłosu.length - 1; i++) //To zostawiamy bez zmian
        {
            tworzenie = new TworzenieParWątkowe[liczenie.LiczbaRdzeni()];
            wątki = new Thread[tworzenie.length];
            
            for(rdzeń = 0; rdzeń < liczenie.LiczbaRdzeni(); rdzeń++) //Dzielimy tablicę na tyle kawałków, ile mamy rdzeni
            {
                początek = liczenie.getIndeksPoczątkowy(i, rdzeń, FragmentyGłosu.length);
                koniec = liczenie.getIndeksKońcowy(i, rdzeń, FragmentyGłosu.length);
                
                tworzenie[rdzeń] = new Motywy.TworzenieParWątkowe(FragmentyGłosu, i, początek, koniec, maksimum, CzyKrab); //Każdy rdzeń tworzy osobną listę motywów
                wątki[rdzeń] = new Thread(tworzenie[rdzeń]); 
                
                wątki[rdzeń].start(); //Uruchamiamy, nie czekając
                
                tab_licznikowa[rdzeń] = (koniec - początek + 1);
            }//next rdzeń
            
            czy_koniec = false;
            
            do{
                
                licznik = 0;
                
                for(rdzeń = 0; rdzeń < liczenie.LiczbaRdzeni(); rdzeń++)
                {
                    if(tworzenie[rdzeń].getStanWątku() == TworzenieParWątkowe.PO)
                    {
                        licznik = licznik + 1;
                    }
                    
                }//next rdzeń
                
                if(licznik == liczenie.LiczbaRdzeni()) czy_koniec = true;
                
            }while(czy_koniec == false);
            
            for(rdzeń = 0; rdzeń < liczenie.LiczbaRdzeni(); rdzeń++)
            {
                ListaParMotywów.addAll(tworzenie[rdzeń].getListę());
                
                this.LicznikIteracji += tab_licznikowa[rdzeń];
            }//next rdzeń
            
        }//next i
        
        
        
    }//Koniec wątkowego tworzenia listy
    
    
    /**
     * Metoda zwracająca tablicę par motywów.
     * @return Tablica par motywów
     */
    public String[] getMiaryMotywów()
    {
        String[] wynik;
        int i;
        ParaMotywów pomoc;
        int takt1;
        int takt2;
        
        wynik = new String[ListaParMotywów.size()];
        
        for(i = 0; i < wynik.length; i++)
        {
            pomoc = ListaParMotywów.get(i);
            
            takt1 = pomoc.getMotyw1().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
            takt2 = pomoc.getMotyw2().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
            
            
            wynik[i] = pomoc.getMotyw1().getLokalizacjęPoczątku() + "(" + takt1 + ")" + "\t" + pomoc.getMotyw2().getLokalizacjęPoczątku() + "(" + takt2 + ")" + "\t" + pomoc.getRóżnicę();
        }//next i
        
        return wynik;
    }//Koniec metody
    
    
    /**
     * Metoda zwracająca wszystkie pary motywów, których różnica jest mniejsza od zadanej wartości.
     * @param maksimum Maksymalna wartość różnicy pomiędzy motywami. Gdy maksimum jest mniejsze lub równe -1, zwracane są wszystkie pary motywów.
     * @return Tablica par motywów.
     */
    public ParaMotywów[] getParyMotywów(double maksimum)
    {
        ArrayList<ParaMotywów> pomocnicza_lista;
        ParaMotywów[] wynik;
        int i;
        
        wynik = null;
        
        if(maksimum <= -1)
        {
            wynik = new ParaMotywów[ListaParMotywów.size()];
            
            for(i = 0; i < wynik.length; i++) wynik[i] = ListaParMotywów.get(i);
            
        }else
        {
            pomocnicza_lista = new ArrayList<>();
            
            for(i = 0; i < ListaParMotywów.size(); i++)
            {
                if(ListaParMotywów.get(i).getRóżnicę() <= maksimum)
                {
                    pomocnicza_lista.add(ListaParMotywów.get(i));
                }//end if
                
            }//next i
            
            if(pomocnicza_lista.size() > 0)
            {
                wynik = new ParaMotywów[pomocnicza_lista.size()];
                
                for(i = 0; i < wynik.length; i++)
                {
                    wynik[i] = pomocnicza_lista.get(i);
                }//next i
            }//end if
            
            //if(wynik != null) Arrays.sort(wynik);
            
            
        }//end if
        
        return wynik;
        
    }//Koniec metody zwracającej pary motywów
    
    /**
     * Metoda zwracająca procent wszystkich iteracji (dla innego wątku!)
     * @return Procent iteracji
     */
    public int getProcentIteracji()
    {
        int wynik;
        
        wynik = (int)(100 * ((double)this.LicznikIteracji/(double)this.LiczbaWszystkichIteracji));
        
        return wynik;
    }
    
    /**
     * Metoda zwracająca postać tekstową opisu.
     * @param tab Tablica z parami motywów
     * @return Opis w postaci tablicy
     */
    public static String[] GenerujTablicęOpisu(ParaMotywów[] tab)
    {
        String[] wynik;
        int i;
        int nr_taktu_m1; //Numer taktu z motywem 1
        int nr_taktu_m2; //Numer taktu z motywem 2
        String kom1; //Oznaczenie komórki z początkiem motywu 1
        String kom2; //Oznaczenie komórki z początkiem motywu 2
        
        String kom1_koniec; //Oznaczenie komórki z końcem motywu pierwszego
        int LiczbaNut; //Liczba nut w motywie
        int kom1_nr_taktu_koniec;
        
        wynik = null;
        
        if(tab == null)
        {
            wynik = new String[1];
            
            wynik[0] = "Tablica par motywów jest pusta";
        }else
        {
            wynik = new String[tab.length];
            
            for(i = 0; i < wynik.length; i++)
            {
                kom1 = tab[i].getMotyw1().getLokalizacjęPoczątku();
                nr_taktu_m1 = tab[i].getMotyw1().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
                
                kom1_koniec = tab[i].getMotyw1().getLokalizacjęKońca();
                LiczbaNut = tab[i].getMotyw1().getDługośćMotywu();
                kom1_nr_taktu_koniec = tab[i].getMotyw1().getReferencjęDoMotywu()[LiczbaNut-1].getZdarzenie().getTakt();
                
                kom2 = tab[i].getMotyw2().getLokalizacjęPoczątku();
                nr_taktu_m2 = tab[i].getMotyw2().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
                
                wynik[i]  = kom1 + "(" + nr_taktu_m1 + ")\t" + kom2 + "(" + nr_taktu_m2 + ")\t" + tab[i].getRóżnicę() + "\t";
                wynik[i] += kom1_koniec + "(" + kom1_nr_taktu_koniec + ")\t" + LiczbaNut;
                
            }//next i
            
        }//end if
        
        
        return wynik;
    }//Koniec metody zwracającej tablicę opisu
    
    //---------------------------------Generacja tablicy przyległości!------------------------------------
    
    /**
     * Metoda tworząca opis tablicowy podobieństw motywów.
     * @param tablica
     * @return Opis w postaci tablicy CSV.
     */
    public static String UtwórzMacierzMotywów(ParaMotywów[] tablica)
    {
        //String opis_tablicowy;
        StringBuilder opis_tablicowy_sb;
        int i;
        int j;
        ArrayList<String> tablica_konwersji;
        ParaSkrócona[][] tablica_wynikowa;
        int n;
        Motyw motyw;
        
        opis_tablicowy_sb = new StringBuilder("\n\n\t");
        
        
        tablica_konwersji = TwórzIndeksyLiczbowe(tablica);
        n = tablica_konwersji.size();
        
        tablica_wynikowa = InicjalizujTablicęSkróconą(n);
        
        WypełnijTablicęSkróconą(tablica_konwersji, tablica_wynikowa, tablica);
                        
        //Teraz tworzymy górny opis tabeli kwadratowej przyległości:
        
        for(i = 0; i < tablica_konwersji.size(); i++)
        {
            opis_tablicowy_sb.append(tablica_konwersji.get(i));
            
            if(i < tablica_konwersji.size() - 1)
            {
                opis_tablicowy_sb.append("\t");
            }
            
        }//next i
        
        opis_tablicowy_sb.append("\t\tInterwały motywu:");
        
        //Teraz dodajemy puste kolumny (tyle samo ile interwałów + 2)
        
        for(i = 0; i < tablica[0].getMotyw1().getLiczbęElementów(); i++)
        {
            opis_tablicowy_sb.append("\t");
        }
        
        opis_tablicowy_sb.append("Motyw zapis skrócony\t");
        
        opis_tablicowy_sb.append("\tonset:");
        
        for(i = 0; i < tablica[0].getMotyw1().getLiczbęElementów() + 1; i++)
        {
            opis_tablicowy_sb.append("\t");
        }
        
        opis_tablicowy_sb.append("duration:\t");
        
        for(i = 0; i < tablica[0].getMotyw1().getLiczbęElementów()-1; i++) //Trzeba trafić we właściwą kolumnę arkusza!!!
        {
            opis_tablicowy_sb.append("\t");
        }
        
        opis_tablicowy_sb.append("Motyw rytmiczny zapis skrócony:\n");
        //
        
        //Teraz generujemy właściwą tablicę
        
        for(i = 0; i < tablica_konwersji.size(); i++)
        {
            opis_tablicowy_sb.append(tablica_konwersji.get(i));
            opis_tablicowy_sb.append("\t");
            motyw = null;
            
            for(j = 0; j < n; j++)
            {
                if(tablica_wynikowa[i][j] != null)
                {
                    opis_tablicowy_sb.append(tablica_wynikowa[i][j].Waga);
                    
                    if(motyw == null) motyw = tablica_wynikowa[i][j].motyw;
                    
                }else
                {
                    if(i != j)
                    {
                        opis_tablicowy_sb.append("x");
                    }else
                    {
                        opis_tablicowy_sb.append("To samo.");
                    }
                }//end if
                
                if(j < n - 1)
                {
                    opis_tablicowy_sb.append("\t");
                }//end if
                
            }//next j
            
            if(motyw != null) opis_tablicowy_sb.append(MotywInterwały(motyw));
            
            opis_tablicowy_sb.append("\n");
            
        }//next i
        
        opis_tablicowy_sb.append("\n\n");
        
        return opis_tablicowy_sb.toString();
    }//Koniec tworzenia tablicy przyległości dla pary motywów
    
    /**
     * Metoda generujaca w stylu CSV motyw (interwały)
     * @param motyw Motyw muzyczny
     * @return String z motywu.
     */
    private static String MotywInterwały(Motyw motyw)
    {
        String wynik;
        int i;
        StringBuilder wszystko_razem_sb;
        int interval_pomocniczy;
        
        wynik = "\t\t";
        
        wszystko_razem_sb = new StringBuilder("");
        
        for(i = 0; i < motyw.getDługośćMotywu(); i++)
        {
            
            interval_pomocniczy = motyw.getReferencjęDoMotywu()[i].getZdarzenie().getInterval();
            if(motyw.getReferencjęDoMotywu()[i].getZdarzenie().getCzyInterwałJestPoczątkowy() == Zdarzenie.INTERWAL_ZEROWY)
            {
                interval_pomocniczy = motyw.getReferencjęDoMotywu()[i].getZdarzenie().getStaryInterwał();
            }
            
            wynik += interval_pomocniczy;
            
            wszystko_razem_sb.append(interval_pomocniczy); //Utworzenie danych do jednej komórki celem lepszego filtrowania
            wszystko_razem_sb.append(" ");
            
            if(i < motyw.getDługośćMotywu() - 1)
            {
                wynik += "\t";
            }
            
        }//next i
        
        wynik += "\t" + wszystko_razem_sb.toString();
        
        wynik += "\t\t";
        
        for(i = 0; i < motyw.getDługośćMotywu(); i++)
        {
            wynik += motyw.getReferencjęDoMotywu()[i].getZdarzenie().getOnset();
            
            if(i < motyw.getDługośćMotywu() - 1)
            {
                wynik += "\t";
            }
            
        }//next i
        
        wynik += "\t\t";
        
        wszystko_razem_sb = new StringBuilder("");
        
        for(i = 0; i < motyw.getDługośćMotywu(); i++)
        {
            wynik += motyw.getReferencjęDoMotywu()[i].getZdarzenie().getDuration();
            
            wszystko_razem_sb.append(motyw.getReferencjęDoMotywu()[i].getZdarzenie().getDuration()); //Utworzenie dantch do jednej komórki celem lepszego filtrowania
            wszystko_razem_sb.append(" ");
            
            if(i < motyw.getDługośćMotywu() - 1)
            {
                wynik += "\t";
            }
            
        }//next i
        
        wynik += "\t" + wszystko_razem_sb.toString();
        
        return wynik;
    }//Koniec metody tworzącej string z motywu
    
    /**
     * Metoda inicjalizująca tablicę skróconą (miara przyległości)
     * @param n Liczba elementów (wierszy lub kolumn, nie ma znaczenia, bo tablica jest prostokątna)
     * @return Tablica zainicjalizowana.
     */
    private static ParaSkrócona[][] InicjalizujTablicęSkróconą(int n)
    {
        int i;
        int j;
        ParaSkrócona[][] tab;
        
        tab = new ParaSkrócona[n][n];
        
        for(i = 0; i < n; i++)
        {
            for(j = 0; j < n; j++)
            {
                tab[i][j] = null;
            }//next j
        }//next i
        
        return tab;
    }//Koniec inicjalizacji
    
    private static void WypełnijTablicęSkróconą(ArrayList<String> tablica_konwersji, ParaSkrócona[][] tab_skrócona, ParaMotywów[] motywy)
    {
        int wiersz;
        int kolumna;
        int i;
        String adres1;
        String adres2;
        
        for(i = 0; i < motywy.length; i++)
        {
            adres1 = getOpisKomórkiArkusza(motywy[i], 1);
            adres2 = getOpisKomórkiArkusza(motywy[i], 2);
            
            wiersz = tablica_konwersji.indexOf(adres1);
            kolumna = tablica_konwersji.indexOf(adres2);
            
            //Tablica jest kwadratowa!
            
            tab_skrócona[wiersz][kolumna] = new ParaSkrócona();
            
            tab_skrócona[wiersz][kolumna].IndeksTekstowyY = adres1;
            tab_skrócona[wiersz][kolumna].IndeksX = wiersz;
            tab_skrócona[wiersz][kolumna].IndeksTekstowyY = adres2;
            tab_skrócona[wiersz][kolumna].IndeksY = kolumna;
            tab_skrócona[wiersz][kolumna].Waga = motywy[i].getRóżnicę();
            tab_skrócona[wiersz][kolumna].motyw = motywy[i].getMotyw1();
            
            
            tab_skrócona[kolumna][wiersz] = new ParaSkrócona();
            
            tab_skrócona[kolumna][wiersz].IndeksTekstowyY = adres2;
            tab_skrócona[kolumna][wiersz].IndeksX = kolumna;
            tab_skrócona[kolumna][wiersz].IndeksTekstowyY = adres1;
            tab_skrócona[kolumna][wiersz].IndeksY = wiersz;
            tab_skrócona[kolumna][wiersz].Waga = motywy[i].getRóżnicę();
            tab_skrócona[kolumna][wiersz].motyw = motywy[i].getMotyw2();
            
        }//next i
        
    }//Koniec wypełniania
    
    
    /**
     * Metoda zwracająca opis tekstowy adresu komórki z arkusza pliku z Humdruma
     * @param para Para motywów
     * @param który Opis którego motywu zwrócić (1 lub 2)
     * @return Opis motywu
     */
    private static String getOpisKomórkiArkusza(ParaMotywów para, int który)
    {
        String opis;
        
        if(który == 1)
        {
            opis  = para.getMotyw1().getReferencjęDoMotywu()[0].getNazwęKolumny();
            opis += para.getMotyw1().getReferencjęDoMotywu()[0].getWiersz();
            opis += "(";
            opis += para.getMotyw1().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
            opis += ")";
        }else
        {
            opis  = para.getMotyw2().getReferencjęDoMotywu()[0].getNazwęKolumny();
            opis += para.getMotyw2().getReferencjęDoMotywu()[0].getWiersz();
            opis += "(";
            opis += para.getMotyw2().getReferencjęDoMotywu()[0].getZdarzenie().getTakt();
            opis += ")";
        }
        
        return opis;
    }//Koniec metody zwracającej opis
    
    /**
     * Metoda tworząca indeksy liczbowe dla tablicy podobieństw motywów.
     * @param tablica Tablica z parami motywów
     * @return Lista tablicowa z przyporządkowanymi wartościami.
     */
    private static ArrayList<String> TwórzIndeksyLiczbowe(ParaMotywów[] tablica)
    {
        ArrayList<String> wynik;
        int i;
        
        String Tekst1;
        String Tekst2;
        
        wynik = new ArrayList<>();
        
        for(i = 0; i < tablica.length; i++)
        {
            
            Tekst1 = getOpisKomórkiArkusza(tablica[i], 1);            
            Tekst2 = getOpisKomórkiArkusza(tablica[i], 2);
            
            if(i == 0)
            {
                wynik.add(Tekst1);
                wynik.add(Tekst2);
            }else
            {
                if(wynik.indexOf(Tekst1) < 0) wynik.add(Tekst1);
                if(wynik.indexOf(Tekst2) < 0) wynik.add(Tekst2);
            }//end if
        }//next i
        
        return wynik;
    }//Koniec metody tworzącej indeksy liczbowe
    
    
}//Koniec klasy fragmenty

/**
 * Klasa pomocnicza służąca do przechowywania danych potrzebnych w tablicy przeległości.
 * @author Piotr Wrzeciono
 */
class ParaSkrócona
{
    /**Nazwa komórki według arkusza kalkulacyjnego - wiersz*/
    public String IndeksTekstowyY;
    /**Nazwa komórki według arkusza kalkulacyjnego - kolumna*/
    public String IndeksTekstowyX;
    /**Numer kolumny*/
    public int IndeksY;
    /**Numer wiersza*/
    public int IndeksX;
    /**Waga liczbowa*/
    public double Waga;
    /**Motyw muzyczny związany z wierszem*/
    public Motyw motyw;
    
    /**
     * Konstruktor klasy pomocniczej
     */
    public ParaSkrócona()
    {
        IndeksTekstowyY = "";
        IndeksTekstowyX = "";
        IndeksY = 0;
        IndeksX = 0;
        Waga = -1; //Brak
        motyw = null;
    }//Koniec konstruktora
    
}//Koniec klasy