
package Struktury;

/**
 * Klasa przechowująca zdarzenie z arkusza z dodatkowymi informacjami
 * @author Piotr Wrzeciono
 */
public class ArkuszZdarzenie
{
    /**Informacja o stanie zdarzenia w arkuszu - walidacja*/
    private int StanZdarzenia;
    /**Wiersz zdarzenia w arkuszu*/
    private final int Wiersz;
    /**Kolumna zdarzenia w arkuszu*/
    private final int Kolumna;
    /**Oznaczenie literowe kolumny*/
    private final String Kolumna_S;
    /**System dla którego zdarzenia ma miejsce*/
    private final int NumerGłosu;
    /**Obiekt reprezentujący zdarzenie*/
    private Zdarzenie ZdarzenieHumdrum;
    /**Referencja do całego arkusza*/
    private Arkusz ReferencjaDoArkusza;
    /**WagaMetryczna*/
    private double WagaMetryczna;
    /**WagaInterwałowa*/
    private double WagaInterwałowa;
    
    /**Stała informująca, że zapis w arkuszu jest bez błędu*/
    public static final int ZAPIS_BEZ_BŁĘDU = 0;
    /**Stała informująca, że zapis w arkuszu ma błąd - numer taktu*/
    public static final int ZAPIS_Z_BŁĘDEM_TAKT = 1;
    /**Stała informująca, że zapis w arkuszu ma błąd - pitch*/
    public static final int ZAPIS_Z_BŁĘDEM_PITCH = 2;
    /**Stała informująca, że zapis w arkuszu ma błąd - interval*/
    public static final int ZAPIS_Z_BŁĘDEM_INTERVAL = 3;
    /**Stała informująca, że zapis w arkuszu ma błąd - onset*/
    public static final int ZAPIS_Z_BŁĘDEM_ONSET = 4;
    /**Stała informująca, że zapis w arkuszu ma błąd - duration*/
    public static final int ZAPIS_Z_BŁĘDEM_DURATION = 5;
    
    /**
     * Konstruktor klasy ArkuszZdarzenie
     * @param arkusz Arkusz z pliku CSV
     * @param numer_systemu Numer systemu (numer głosu). Im mniejszy, tym niższy.
     * @param wiersz Położenie wiersza dla komórki oznaczającą takt
     * @param kolumna Położenie kolumny dla komórki oznaczającej takt
     * @param waga_ton Waga dla obliczania różnicy interwałowej pomiędzy zdarzeniami
     * @param waga_czas Waga dla oblcizania różnicy czasowej pomiędzy zdarzeniami
     */
    public ArkuszZdarzenie(Arkusz arkusz, int numer_systemu, int wiersz, int kolumna, double waga_ton, double waga_czas)
    {
        StanZdarzenia = ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU;
        NumerGłosu = numer_systemu;
        Wiersz = wiersz;
        Kolumna = kolumna;
        Kolumna_S = arkusz.getNazwęKolumny(kolumna);
        ZdarzenieHumdrum = null; //Na sam początek
        
        ReferencjaDoArkusza = arkusz;
        WagaInterwałowa = waga_ton;
        WagaMetryczna = waga_czas;
        
        UtwórzZdarzenie(arkusz, waga_ton, waga_czas);
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca kopię zdarzenia Humdrum
     * @return Kopia zdarzenia Humdrum
     */
    public ArkuszZdarzenie Kopiuj()
    {
        ArkuszZdarzenie kopia;
        
        kopia = new ArkuszZdarzenie(ReferencjaDoArkusza, NumerGłosu, Wiersz, Kolumna, WagaInterwałowa, WagaMetryczna);
        
        return kopia;
        
    }//Koniec metody zwracającej kopię zdarzenia
    
    /**
     * Metoda zwracająca opis walidacji - potrzebny do wyprowadzenia informacji o błędzie.
     * @return Opis błędu w komórce arkusza.
     */
    public String getOpisWalidacji()
    {
        String opis;
        
        opis = ""; //Gdy nie ma błędu - nic nie piszemy, no po co.
        
        if(StanZdarzenia > ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            opis = "W komórce arkusza o adresie " + Kolumna_S + ":";
            
            if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_TAKT) opis += Wiersz + "błąd w numerze taktu.";
            if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_PITCH) opis += (Wiersz + 1) + "błąd w zapisie pitch.";
            if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_INTERVAL) opis += (Wiersz + 2) + "błąd w zapisie interval.";
            if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_ONSET) opis += (Wiersz + 3) + "błąd w zapisie onset.";
            if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_DURATION) opis += (Wiersz + 4) + "błąd w zapisie duration.";
            
        }//end if
        
        
        return opis;
        
    }//Koniec metody zwracającej opis walidacji
    
    /**
     * Metoda sprawdzająca, czy tekst jest liczbą
     * @param tekst Tekst do sprawdzenia
     * @return <b>true</b>, gdy tekst jest liczbą, <b>false</b>, gdy tekst nie jest liczbą.
     */
    private boolean CzyLiczba(String tekst)
    {
        boolean wynik;
        double test;
        
        wynik = true;
        
        try
        {
            test = Double.parseDouble(tekst);
        }catch(Exception ex)
        {
            wynik = false;
        }//end tr-catch
        
        return wynik;
    }//Koniec sprawdzania, czy liczba
    
    /**
     * Metoda zwracająca numer głosu związanego ze zdarzeniem.
     * @return Numer głosu (systemu).
     */
    public int getNumerGłosu()
    {
        return this.NumerGłosu;
    }//Koniec metody zwracającej numer głosu
    
    /**
     * Metoda zwracająca numer wiersza związanego ze zdarzeniem.
     * @return Numer wiersza (liczy się od 1)
     */
    public int getWiersz()
    {
        return Wiersz;
    }
    
    /**
     * Metoda zwracająca numer kolumny związanego ze zdarzeniem.
     * @return Numer kolumny (liczy się od 1)
     */
    public int getKolumnę()
    {
        return Kolumna;
    }
    
    /**
     * Metoda zwracająca literowe oznaczenie kolumny w arkuszu.
     * @return Literowa nazwa kolumny.
     */
    public String getNazwęKolumny()
    {
        return this.Kolumna_S;
    }
    
    /**
     * Metoda zwracająca wynik walidacji danych
     * @return Wynik walidacji danych z arkusza
     */
    public int getStanZdarzenia()
    {
        return this.StanZdarzenia;
    }//Koniec metody biorącej stan ze zdarzenia
    
    /**
     * Metoda tworząca Zdarzenie do analizy motywów.
     * @param arkusz Arkusz z danymi
     * @param waga_ton Waga dla obliczania różnicy interwałowej pomiędzy zdarzeniami
     * @param waga_czas Waga dla oblcizania różnicy czasowej pomiędzy zdarzeniami
     */
    private void UtwórzZdarzenie(Arkusz arkusz, double waga_ton, double waga_czas)
    {
        int NumerTaktu;
        int pitch;
        int interval;
        double onset;
        double duration;
        
        String takt_s;
        String pitch_s;
        String interval_s;
        String onset_s;
        String duration_s;
        
        takt_s = arkusz.getZawartość(Wiersz, Kolumna).trim();
        pitch_s = arkusz.getZawartość(Wiersz + 1, Kolumna).trim();
        interval_s = arkusz.getZawartość(Wiersz + 2, Kolumna).trim();
        onset_s = arkusz.getZawartość(Wiersz + 3, Kolumna).trim();
        duration_s = arkusz.getZawartość(Wiersz + 4, Kolumna).trim();
        
        NumerTaktu = 0;
        pitch = 0;
        interval = 0;
        onset = 0;
        duration = 0;
        
        if(CzyLiczba(takt_s) == true)
        {
            NumerTaktu = Integer.parseInt(takt_s);
        }else
        {
            StanZdarzenia = ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_TAKT;
        }//end if
        
        if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            if(CzyLiczba(pitch_s) == true)
            {
                pitch = (int)Double.parseDouble(pitch_s);
            }else
            {
                StanZdarzenia = ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_PITCH;
            }//end if
        }//end if
        
        if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            if(CzyLiczba(interval_s) == true)
            {
                interval = (int)Double.parseDouble(interval_s);
            }else
            {
                StanZdarzenia = ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_INTERVAL;
            }//end if
        }//end if
        
        
        if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            if(CzyLiczba(onset_s) == true)
            {
                onset = Double.parseDouble(onset_s);
            }else
            {
                StanZdarzenia = ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_ONSET;
            }//end if
            
        }//end if
        
        
        if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            if(CzyLiczba(duration_s) == true)
            {
                duration = Double.parseDouble(duration_s);
            }else
            {
                StanZdarzenia = ArkuszZdarzenie.ZAPIS_Z_BŁĘDEM_DURATION;
            }
        }//end if
        
        
        if(StanZdarzenia == ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
        {
            this.ZdarzenieHumdrum = new Struktury.Zdarzenie(NumerTaktu, pitch, interval, onset, duration, waga_ton, waga_czas);
        }//end if
        
    }//Koniec metody tworzącej zdarzenie
       
    /**
     * Metoda zwracająca zdarzenie Humdrum
     * @return Referencja do zdarzenia.
     */
    public Zdarzenie getZdarzenie()
    {
        return this.ZdarzenieHumdrum;
    }//Koniec metody zwracającej Zdarzenie Humdrum

    
}//Koniec klasy
