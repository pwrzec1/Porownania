
package Struktury;

/**
 * Klasa pomocnicza służąca do przechowywania systemu.
 * @author Piotr Wrzeciono
 */
public class SystemGłos
{
    /**Tekst z arkusza kalkulacyjnego*/
    public String Tekst;
    /**Numer systemu (głosu) - liczymy od najnijższego*/
    public int NumerSystemu;
    /**Wiersz w arkuszu kalkulacyjnym*/
    public int Wiersz;
    /**Kolumna w arkuszu kalkulacyjnym*/
    public int Kolumna;
    /**Nazwa tekstowa systemu*/
    public String NazwaTekstowaSystemu;
    
    /**
     * Konstruktor klasy pomocniczej SystemGłos
     * @param tekst Tekst z arkusza
     * @param wiersz Wiersz w Arkuszu
     * @param kolumna Kolumna w Arkuszu
     * @param nazwa_tekstowa Nazwa tesktowa systemu, np. S1
     */
    public SystemGłos(String tekst, int wiersz, int kolumna, String nazwa_tekstowa)
    {
        Tekst = tekst;
        
        Wiersz = wiersz;
        Kolumna = kolumna;
        
        NumerSystemu = WyliczNumer(tekst);
        NazwaTekstowaSystemu = nazwa_tekstowa;
        
    }//Koniec konstruktora
    
    /**
     * Metoda wyznczająca numer systemu.
     * @param tekst Tekst z kodem systemu
     * @return Numer systemu.
     */
    private int WyliczNumer(String tekst)
    {
        String pomoc;
        int wynik;
        
        wynik = 0;
        
        if(SystemGłos.CzySystem(tekst)) //Sprawdzamy, czy jest systemem jeszcze raz, na wszelki wypadek
        {
            pomoc = tekst.trim().substring(1);
            
            wynik = Integer.parseInt(pomoc);
        }//end if
        
        return wynik;
    }//Koniec wyliczania numeru systemu
    
    /**
     * Metoda sprawdzająca, czy tekst jest systemem.
     * @param tekst do sprawdzenia
     * @return <b>true</b>, gdy tekst oznacza system, <b>false</b>, gdy teskt nie jest systemem.
     */
    public static boolean CzySystem(String tekst)
    {
        boolean wynik;
        String pom;
        String pom1;
        String pom2;
        int liczba;
        
        wynik = false;
        pom = tekst.trim();
        
        if(pom.compareTo("") != 0)
        {
            pom1 = pom.substring(0, 1);
            
            if(pom1.compareTo("") != 0) pom1 = pom1.toLowerCase();
            
            pom2 = pom.substring(1).trim();
            
            try
            {
                liczba = Integer.parseInt(pom2);
            }catch(Exception ex)
            {
                liczba = 0;
            }//end try-catch
            
            if(pom1.compareTo("s") == 0 && liczba > 0)
            {
                wynik = true;
                
            }//end if
            
        }//end if
        
        return wynik;
    }//Koniec metody statycznej sprawdzającej, czy tekst zawiera oznaczenie systemu
    
    /**
     * Metoda zwracająca opis systemu - głosu.
     * @return Opis głosu.
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis  = "Położenie (wiersz:kolumna) ......... " + Wiersz + ":" + Kolumna + "\n";
        opis += "Numer systemu ...................... " + NumerSystemu + "\n";
        
        return opis;
    }//Koniec metody toString
    
}//Koniec klasy pomocniczej