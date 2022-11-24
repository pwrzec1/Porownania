
package Struktury;

import java.io.*;
import java.util.Scanner;

/**
 * Klasa służąca do dekodowania zawartości pliku CSV
 * @author Piotr Wrzeciono
 */
public class PlikCSV
{
    /**Nazwa pliku*/
    private final String NazwaPliku;
    /**Plik z danymi CSV*/
    private File Plik;
    
    /**Stan pliku - jaki jest*/
    private int StanPliku;
    
    /**Plik jeszcze nie został sprawdzony - stan początkowy obiektu*/
    public static final int PRZED_WCZYTANIEM  = 0;
    /**Plik istnieje - warto sprawdzać dalej.*/
    public static final int PLIK_ISTNIEJE = 1;
    /**Niestety pliku nie ma na dysku.*/
    public static final int PLIK_NIE_ISTNIEJE = 2;
    /**Plik jest, ale okazał się być katalogiem.*/
    public static final int PLIK_JEST_KATALOGIEM = 3;
    /**Plik został otwarty*/
    public static final int PLIK_OTWARTY = 4;
    /**Niestety pliku nie udało się otworzyć.*/
    public static final int PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ = 5;
    /**Plik udało się wczytać!*/
    public static final int PLIK_WCZYTANO = 6;
    /**Niestety pliku nie udało się wczytać.*/
    public static final int PLIKU_NIE_UDAŁO_SIĘ_WCZYTAĆ = 7;
    /**Nie udało się zamknąć pliku - być może dane są błędne*/
    public static final int PLIKU_NIE_UDAŁO_SIĘ_ZAMKNĄĆ = 8;
    
    /**Stała informująca o kodowaniu pliku CSV. Plik CSV jest kodowany za pomocą <b>UTF-8</b>*/
    public static final int UTF8 = 0;
    /**Stała informująca o kodowaniu pliku CSV. Plik CSV jest kodowany za pomocą Windowska = <b>cp-1250</b>*/
    public static final int CP1250 = 1;
    
    /**Zmienna służąca do przechowywania kodowanie znaków w pliku CSV*/
    private final int KodowanieZnaków;
    
    /**
     * Konstruktor klasy PlikCSV
     * @param nazwa Nazwa pliku.
     * @param kod Kodowanie znaków według stałych z klasy PlikCSV.
     */
    public PlikCSV(String nazwa, int kod)
    {
        StanPliku = PlikCSV.PRZED_WCZYTANIEM;
        this.NazwaPliku = nazwa;
        
        this.KodowanieZnaków = WalidacjaKodowaniaZnaków(kod);
    }//Koniec konstruktora
    
    
    /**
     * Metoda zwracająca informację o kodowaniu znaków po walidacji
     * @param kod Kod znaków przed walidacją
     * @return Kod znaków po walidacji
     */
    private int WalidacjaKodowaniaZnaków(int kod)
    {
        int wynik;
        
        wynik = PlikCSV.UTF8;
        
        if(kod == PlikCSV.CP1250) wynik = kod;
        
        return wynik;
    }//Koniec metody walidacyjnej
    
    /**
     * Metoda zwracająca stan pliku.
     * @return Stan pliku z wartości możliwych ze zdefiniowanych w stałych statycznych klasy PlikCSV.
     */
    public int getStanPliku()
    {
        return StanPliku;
    }//Koniec metody zwracającej stan pliku
    
    /**
     * Metoda zwracająca opis tekstowy stanu pliku CSV.
     * @return Opis
     */
    public String getOpisStanuPliku()
    {
        String opis;
        
        opis = "Pliku jeszcze nie próbowano wczytać";
        
        if(StanPliku == PlikCSV.PLIK_NIE_ISTNIEJE) opis = "Plik " + NazwaPliku + " nie istnieje";
        if(StanPliku == PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ) opis = "Pliku " + NazwaPliku + " nie udało się otworzyć";
        if(StanPliku == PlikCSV.PLIK_OTWARTY) opis = "Plik " + NazwaPliku + " jest otwarty!";
        if(StanPliku == PlikCSV.PLIK_ISTNIEJE) opis = "Plik " + NazwaPliku + " istnieje!";
        if(StanPliku == PlikCSV.PLIK_JEST_KATALOGIEM) opis = "Plik " + NazwaPliku + " jest katalogiem!";
        if(StanPliku == PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_WCZYTAĆ) opis = "Pliku " + NazwaPliku + " pliku nie udało się wczytać!";
        if(StanPliku == PlikCSV.PLIK_WCZYTANO) opis = "Plik " + NazwaPliku + " wczytano!";
        if(StanPliku == PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_ZAMKNĄĆ) opis = "Pliku " + NazwaPliku + " nie udało się zamknąć. Dane mogą być wadliwe.";
        
        
        return opis;
        
    }//Koniec metody zwracającej stan pliku
    
    /**
     * Metoda służąca do zamiany oznaczenia liczbowego kodowania na właściwy tekst.
     * @return Tekst opisujący kodowanie znaków.
     */
    private String getKodTekstowyKodowaniaZnaków()
    {
        String tekst;
        
        tekst = "utf-8";
        
        if(this.KodowanieZnaków == PlikCSV.CP1250) tekst = "cp1250";
        
        return tekst;
    }
    
    
    /**
     * Metoda wczytująca wszystkie linie z pliku CSV.
     * @param kodowanie - kodowanie znaków w pliku CSV (według stałych PLikCSV.CP1250 lub PlikCSV.UTF8.
     * @return Linie z pliku CSV - każdy element tablicy jest oddzielną linią.
     */
    private String[] WczytajCałyPLik(int kodowanie)
    {
        File plik;
        BufferedReader skaner_czytający;
        String[] tablica;
        String cały_tekst;
        String linia;
        UsuwanieBOM usuwanieBOMu;
        
        plik = new File(NazwaPliku);
        tablica = null;
        cały_tekst = "";
        skaner_czytający = null;
        
        usuwanieBOMu = new UsuwanieBOM();
        
        //1. Sprawdzamy czy plik istnieje.
        if(plik.exists() == true)
        {
            StanPliku = PlikCSV.PLIK_ISTNIEJE;
        }else
        {
            StanPliku = PlikCSV.PLIK_NIE_ISTNIEJE;
        }//end if
        
        //2. Jeżeli plik istnieje, sprawdzamy na wszelki wypadek, czy plik nie jest katalogiem.
        if(StanPliku == PlikCSV.PLIK_ISTNIEJE)
        {
            if(plik.isDirectory() == true) StanPliku = PlikCSV.PLIK_JEST_KATALOGIEM;
        }//end if
        
        //3. Jeżeli plik istnieje i nie jest katalogiem, otwieramy go.
        if(StanPliku == PlikCSV.PLIK_ISTNIEJE)
        {
            try {
                skaner_czytający = new BufferedReader(new InputStreamReader(new FileInputStream(plik), this.getKodTekstowyKodowaniaZnaków()));
                StanPliku = PlikCSV.PLIK_OTWARTY;
            } catch (Exception ex) {
                StanPliku = PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ;
                
                skaner_czytający = null;
            }//end try-catch
        }//end if
        
        //4. Jeżeli plik udało się otworzyć, to wczytujemy zawartość (jako plik tekstowy)
        if(StanPliku == PlikCSV.PLIK_OTWARTY)
        {
            try {
                                
                    do
                    {
                        linia = skaner_czytający.readLine();
                        if(linia != null) cały_tekst += usuwanieBOMu.KorektaBOM(linia) + "\n";
                    }while(linia != null);
                
                StanPliku = PlikCSV.PLIK_WCZYTANO;
                
            }catch(Exception ex)
            {
                System.out.println(ex);
                StanPliku = PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_WCZYTAĆ;
            }//end try-catch
        }//end if
        
        //5. Jeżeli plik udało się wczytać, to próbujemy go zamknąć.
        if(skaner_czytający != null)
        {
            try{
                skaner_czytający.close();
            }catch(Exception ex)
            {
                StanPliku = PlikCSV.PLIKU_NIE_UDAŁO_SIĘ_ZAMKNĄĆ;
            }//end try-catch
            
        }//end if
        
        //6. Jeżeli plik udało się zamknąć i został uprzednio wczytany, odtwarzamy poszczególne linie.
        if(StanPliku == PlikCSV.PLIK_WCZYTANO)
        {
            tablica = cały_tekst.split("\n");
        }//end if
        
        return tablica;
        
    }//Koniec metody wczytujacej cały plik CSV
    
    
    /**
     * Metoda zwracająca poszczególne komórki z wiersza;
     * @param linia_z_pliku
     * @return 
     */
    private String[] PodziałLinii(String linia_z_pliku)
    {
        String[] wynik;
        int i;
        
        wynik = null;
        
        //Przetwarzamy tylko linie z danymi!
        if(linia_z_pliku.compareTo("") != 0)
        {
            if(linia_z_pliku.contains(";") == true)
            {
                wynik = linia_z_pliku.split(";"); //Znakiem separacji jest średnik.
            }else
            {
                wynik = linia_z_pliku.split("\t"); //Znakiem seperacji jest tabulacja
            }//end of
            
        }//end if
        
        return wynik;
        
    }//Koniec metody zwracającej poszczególne komórki tekstu
    
    /**
     * Metoda wczytująca zawartość pliku CSV do arkusza.
     * @return Zwraca arkusz kalkulacyjny
     */
    public Arkusz WczytajDane()
    {
        int liczba_wierszy;
        int liczba_kolumn;
        int wiersz;
        int kolumna;
        int y;
        int x;
        int i;
        String wartość;
        Arkusz wynik;
        
        String[] linie_w_pliku;
        String[] komórki_w_wierszu;
        
        wynik = null;
        linie_w_pliku = null;
        komórki_w_wierszu = null;
        
        
        linie_w_pliku = this.WczytajCałyPLik(KodowanieZnaków);
        
        if(StanPliku == PlikCSV.PLIK_WCZYTANO)
        {
            liczba_wierszy = linie_w_pliku.length;
            liczba_kolumn = PodziałLinii(linie_w_pliku[0]).length;
            
            //Szukanie najdłuższego wiersza w pliku CSV
            for(i = 0; i < liczba_wierszy; i++)
            {
                if(liczba_kolumn < PodziałLinii(linie_w_pliku[i]).length)
                {
                    liczba_kolumn = PodziałLinii(linie_w_pliku[i]).length;
                }//end if
                
            }//next i
            
            
            wynik = new Arkusz(liczba_wierszy, liczba_kolumn);
            
            
            for(y = 0; y < liczba_wierszy; y++)
            {
                wiersz = y + 1;
                komórki_w_wierszu = PodziałLinii(linie_w_pliku[y]);
                
                for(x = 0; x < liczba_kolumn; x++)
                {
                    kolumna = x + 1;
                    
                    wartość = "";
                    
                    if(x < komórki_w_wierszu.length)
                    {
                        wartość = komórki_w_wierszu[x];
                    }//end if
                    
                    wynik.setKomórkę(wiersz, kolumna, wartość);
                    
                }//next x
                
            }//next y
            
        }//end if
        
        return wynik;
        
    }//Koniec metrody zwracającej Wczytane dane
    
    
}//Koniec klasy
