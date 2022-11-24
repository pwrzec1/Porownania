/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Struktury;

/**
 * Klasa służąca do przechowywania danych o tzw zdarzeniu (według definicji systemu Humdrum).
 * @author Piotr Wrzeciono
 * @since 25.02.2020
 */
public class Zdarzenie 
{
    /**Numer taku związanego ze zdarzeniem*/
    private final int takt;
    /**Wysokość w liczbach półtonów w MIDI*/
    private final int pitch;
    /**Interwał muzyczny*/
    private int interval;
    /**Przesunięcie w czasie*/
    private final double onset;
    /**Czas trwania wyrażony w szesnastkach*/
    private final double duration;
    /**Czy interwał jest zerowy (początek motywu)*/
    private int zerowy_interwał;
    /**Stary interwał przed zmianą*/
    private int stary_interwał;
    
    /**Dokładność porównywania wartości rytmicznych*/
    public static final double DOKLADNOSC_CZASOWA = 0.0001;
    /**Ułamek do diminucji albo augmentacji*/
    public static final double ULAMEK_ROZPOZNAWANY = 0.5;
    /**Wielka domyślna wartość różnicy, na przykład przy pauzach*/
    public static final double WIELKA_LICZBA = 1000000;
    /**Zdarzenie jest na początku motywu i posiada interwał 0*/
    public static final int INTERWAL_ZEROWY = 100;
    /**Zdarzenie jest w środku motywu i posiada interwał niezerowy*/
    public static final int INTERWAL_NIEZEROWY = 101;
    
    /**Waga dla ważności interwału tonalnego*/
    private double WagaInterwałuTonalnego;
    /**Waga dla ważności interwału czasowego*/
    private double WagaInterwałuCzasowego;
 
    
    
    /**
     * Konstruktor klasy Zdarzenie (patrz Humdrum)
     * @param takt Numer taktu związanego ze zdarzeniem.
     * @param pitch Wysokość dźwięku w liczbach półtonów MIDI.
     * @param interval Interwał muzyczny.
     * @param onset  Przesunięcie w czasie (ile szesnastek od początku utworu).
     * @param duration Czas trwania wyrażony w szesnastkach danego zdarzenia.
     * @param WagaTon Waga dla interwału tonalnego
     * @param WagaCzas Waga dla interwału czasowego
     */
    public Zdarzenie(int takt, int pitch, int interval, double onset, double duration, double WagaTon, double WagaCzas)
    {
        this.takt = takt;
        this.pitch = pitch;
        this.interval = interval;
        this.onset = onset;
        this.duration = duration;
        this.WagaInterwałuCzasowego = WagaCzas;
        this.WagaInterwałuTonalnego = WagaTon;
        this.zerowy_interwał = Zdarzenie.INTERWAL_NIEZEROWY;
        this.stary_interwał = 0;
    }//Koniec konstruktora
    
    
    /**
     * Metoda służąca do ustawienia na nowo wag używanych w liczeniu różnicy.
     * @param WagaTon Waga dla interwału tonalnego
     * @param WagaCzas Waga dla interwału czasowego
     */
    public void UstawWagi(double WagaTon, double WagaCzas)
    {
        this.WagaInterwałuTonalnego = WagaTon;
        this.WagaInterwałuTonalnego = WagaCzas;
    }
    
    /**
     * Metoda zwracająca numer taktu
     * @return Numer taktu (0 - przedtakt, wartości większe od 0 - normalne takty)
     */
    public int getTakt()
    {
        return takt;
    
    }
    
    /**
     * Metoda zwracająca wysokość dźwięku
     * @return Wysokość dźwięku wyrażona w liczbie półtonów MIDI.
     */
    public int getPitch()
    {
        return pitch;
    }
    
    /**
     * Metoda zwracająca interwał muzyczny pomiędzy zdarzeniami w czasie.
     * @return Liczba szesnastek.
     */
    public int getInterval()
    {
        return interval;
    }
    
    /**
     * Metoda ustawiająca nową wartość interwału.
     * Metoda ta jest wywoływana podczas tworzenia nowego motywu do porównania.<br>
     * W tym przypadku trzeba utwoirzyć nowy interwał, jako miarę odległości
     * pomiędzy drugim dźwiękiem motywu a pierwszym (czyli wstecz).<br>
     * <span style="color:red;font-weight:bold;">Liczenie interwału od dźwięku przed motywem jest
     * pozbawione sensu muzycznego!</span>
     * @param nowy Nowy interwał muzyczny
     */
    public void setInterval(int nowy)
    {
        this.stary_interwał = interval;
        
        interval = nowy;
    }
    
    /**
     * Metoda służąca do oznaczenia interwału początkowego.
     */
    public void OznaczPoczątek()
    {
        this.zerowy_interwał = Zdarzenie.INTERWAL_ZEROWY;
    }
    
    /**
     * Metoda informująca o tym, czy zdarzenie jest interwałem początkowym.
     * @return <b>INTERWAL_ZEROWY</b>, gdy zdarzenie jest początkiem, <b>INTERWAL_NIEZEROWY</b>, gdy zdarzenie jest w środku motywu.
     */
    public int getCzyInterwałJestPoczątkowy()
    {
        return this.zerowy_interwał;
    }
    
    /**
     * Metoda zwracająca starą wartość interwału (przed zmianą).
     * @return Stara wartość interwału.
     */
    public int getStaryInterwał()
    {
        return this.stary_interwał;
    }
    
    /**
     * Przesunięcie w czasie od początku utworu, wyrażona w szesnastkach.
     * @return Liczba szesnastek od początku utworu.
     */
    public double getOnset()
    {
        return onset;
    }
    
    /**
     * Czas trwania zdarzenia wyrażony w szesnastkach
     * @return Liczba szesnastek.
     */
    public double getDuration()
    {
        return duration;
    }
    
    /**
     * Metoda służąca do sprawdzenia, czy bieżące zdarzenie można porównywać z innym
     * @param Inne Inne zdarzenie
     * @return <b>true</b>, gdy zdarzenia można porównywać, <b>false</b>, gdy nie można porównywać
     */
    public boolean CzyPorównywać(Zdarzenie Inne)
    {
        boolean rezultat;
        double ułamek;
        
        rezultat = false;
        
        if(duration == Inne.getDuration()) rezultat = true; //Efekt dyskusji 25.02.202 - porównujemy te same wartości rytmiczne!
        
        ułamek = (duration/Inne.getDuration());
        
        if(ułamek < 1) //Te wartości porównujemy ze sobą
        {
            if(Math.abs(ułamek - Zdarzenie.ULAMEK_ROZPOZNAWANY) <= Zdarzenie.DOKLADNOSC_CZASOWA)
            {
                rezultat = true;
            }
        }//end if
        
        if(ułamek > 1)
        {
            if(Math.abs((1/ułamek) - Zdarzenie.ULAMEK_ROZPOZNAWANY) <= Zdarzenie.DOKLADNOSC_CZASOWA)
            {
                rezultat = true;
            }
        }//end if
        
        return rezultat;
    }//Koniec metody porównującej
    
    /**
     * Metoda obliczająca różnicę interwałową pomiędzy dwoma zdarzeniami.
     * @param Inne Inne zdarzenie.
     * @return Liczba półtonów liczona względem MIDI.
     */
    public int RóżnicaPółtonowa(Zdarzenie Inne)
    {
        int różnica;
        
        różnica = this.pitch - Inne.getPitch();
        
        return różnica;
    }//Koniec różnicy półtonowej
    
    /**
     * Metoda obliczająca różnicę czasową pomiędzy dwoma zdarzaniami.
     * @param Inne Inne zdarzenie
     * @return Moduł różnicy w czasie trwania pomiędzy zdarzeniami.
     */
    public double RóżnicaMetryczna(Zdarzenie Inne)
    {
        double wynik;
        
        wynik = Math.abs(this.getDuration() - Inne.getDuration());
        
        
        //Modyfkacja po dyskusji:
        
        if(this.getDuration() == 0 || Inne.getDuration() == 0) wynik = WIELKA_LICZBA;
        
        return wynik;
    }
    
    /**
     * Metoda zwracająca miarę różnicy pomiędzy zdarzeniami.
     * W mierze bierze udział wektor składający się z dwóch zmiennych: różnica półtonowa i różnica metryczna.<br>
     * Dla każdej ze wspomnianych zmiennych przyporządkowano wagę (liczba dodatnia i większa od 0).<br>
     * Wynik zwracany jest kombinacją liniową wspomnianych różnic z uwzględnieniem odpowiadącym im wag.<br>
     * Gdy zdarzenia są identyczne, miara różnicy wynosi 0. Im większa różnica pomiędzy zdarzeniami, tym większa wartość zwracana.
     * @param Inne Inne zdarzenie.
     * @param CzyPoczątek Informacja o początku motywu. Gdy porównujemy początek, przypisujemy wartość <b>true</b>. W innym przypadku wartość <b>false</b>.
     * @return Różnica pomiędzy zdarzeniami.
     */
    public double getMiaręRóżnicy(Zdarzenie Inne, boolean CzyPoczątek)
    {
        double wynik;
        
        wynik = (this.WagaInterwałuTonalnego * RóżnicaInterwał(Inne, CzyPoczątek)) + (this.WagaInterwałuCzasowego * RóżnicaMetryczna(Inne));
        
        return wynik;
    }//Koniec getMiaręRóżnicy
    
    
    /**
     * Metoda obliczająca różnicę w interwałach
     * @param Inne Inne zdarzenie
     * @param CzyPoczątek Informacja o początku motywu. Gdy porównujemy początek, przypisujemy wartość <b>true</b>. W innym przypadku wartość <b>false</b>.
     * @return Różnica
     */
    public double RóżnicaInterwał(Zdarzenie Inne, boolean CzyPoczątek)
    {
        double wynik;
        
        wynik = Math.abs(this.interval - Inne.getInterval());
        
        if(CzyPoczątek)
        {
         if(this.pitch == 0 || Inne.getPitch() == 0 || this.interval == 0 || Inne.getInterval() == 0) wynik = WIELKA_LICZBA; //Tutaj jest pauza!
        }
        
        //Jak jesteśmy na początku i trafimy na interval == 1, trzeba na nowo wyliczyć interwał z pitch.
        
        //if(this.pitch == 0 && Inne.getPitch() == 0) wynik = 0; //W tym przypadku trafiamy na pauzy w tych samych miejscach!!!!!!
        
        return wynik;
    }//
    
    
    /**
     * Metoda zwracająca różnicę interwałową pomiędzy dwoma zdarzeniami.
     * 
     * 
     * <table><tr><td>Wartość (moduł)</td><td>Interpretacja</td></tr><tr><td> 1 </td><td>Unison</td></tr><tr><td> 2 </td><td>Sekunda</td></tr><tr><td> 3 </td><td>Tercja</td></tr><tr><td> 4 </td><td>Kwarta</td></tr><tr><td> 0.5 </td><td>Tryton</td></tr><tr><td></td><td>Kwinta</td></tr><tr><td> 6 </td><td>Seksta</td></tr><tr><td> 7 </td><td>Septyma</td></tr><tr><td> 8 </td><td>Oktawa</td></tr><tr><td> 9 </td><td>Nona</td></tr><tr><td> 10 </td><td>Decyma</td></tr><tr><td> 11 </td><td>Undecyma</td></tr><tr><td> 0.75 </td><td>Oktawa + tryton</td></tr><tr><td> 12 </td><td>Duodecyma</td></tr><tr><td> 13 </td><td>Tercdecyma</td></tr><tr><td> 14 </td><td>Kwartdecyma</td></tr><tr><td> 15 </td><td>Kwintdecyma = dwie oktawy</td></tr></table>
     * 
     * @param Inne Inne zdarzenie
     * @return Interwał.
     */
    public double RóżnicaInterwałowa(Zdarzenie Inne)
    {
        double różnica;
        int liczba_półtonów;
        double signum;
        
        różnica = 0;
        liczba_półtonów = Math.abs(this.RóżnicaPółtonowa(Inne));
        signum = Math.signum(this.RóżnicaPółtonowa(Inne));
        
        if(liczba_półtonów == 0) różnica = 1;
        if(liczba_półtonów == 1 || liczba_półtonów == 2) różnica = 2;
        if(liczba_półtonów == 3 || liczba_półtonów == 4) różnica = 3;
        if(liczba_półtonów == 5) różnica = 4;
        if(liczba_półtonów == 6) różnica = 0.5; //Uwaga tryton!
        if(liczba_półtonów == 7) różnica = 5;
        if(liczba_półtonów == 8 || liczba_półtonów == 9) różnica = 6;
        if(liczba_półtonów == 10 || liczba_półtonów == 11) różnica = 7;
        if(liczba_półtonów == 12) różnica = 8;
        if(liczba_półtonów == 13 || liczba_półtonów == 14) różnica = 9;
        if(liczba_półtonów == 15 || liczba_półtonów == 16) różnica = 10;
        if(liczba_półtonów == 17) różnica = 11;
        if(liczba_półtonów == 18) różnica = 0.75;
        if(liczba_półtonów == 19) różnica = 12;
        if(liczba_półtonów == 20 || liczba_półtonów == 21) różnica = 13;
        if(liczba_półtonów == 22 || liczba_półtonów == 23) różnica = 14;
        if(liczba_półtonów == 24) różnica = 15;
        
        różnica = różnica * signum;
        
        return różnica;
    }//Koniec różnicy interwałowej
    
    /**
     * Wyprowadzenie informacji na temat zdarzenia Humdrum.
     * @return Co w zdarzeniu piszczy ;-)
     */
    public String toString()
    {
        String opis;
        
        opis  = "takt ..... " + this.takt + ", pitch ..... " + this.pitch + ", interval ..... " + this.interval;
        opis += ", onset ..... " + this.onset + ", duration ..... " + this.duration;
        opis += ", waga ton ..... " + this.WagaInterwałuTonalnego + ", waga czas ..... " + this.WagaInterwałuCzasowego;
        
        return opis;
    }//Koniec metody toString
    
}//Koniec klasy
