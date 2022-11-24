/**
 * Pakiet Motywy przeznaczony jest do porównywania ze sobą różnych motywów muzycznych.
 */
package Motywy;

import Struktury.Arkusz;
import Struktury.ArkuszZdarzenie;

/**
 * Klasa przeznaczona do zapamiętywania motywu muzycznego z tablicy Zdarzeń.
 * @author Piotr Wrzeciono
 */
public class Motyw
{
    /**Liczba parametrów zdarzenia Humdrum*/
    public static final int LICZBA_PARAMETROW_ZDARZENIA = 5;
    
    /**Maksymalna wartość różnicy, używana jako natychmiastowe odrzucenie porównania*/
    public static final double MAKSYMALNA_WARTOSC_ROZNICY = 10000000;
    
    
    /**Tablica przechowująca motyw muzyczny w postaci zdarzeń Humdrum*/
    private final ArkuszZdarzenie[] CiągMuzyczny;
    /**Długość ciągu*/
    private final int LiczbaElementów;
    /**Indeks początkowy w tablicy zawierającej system*/
    private final int IndeksPoczątkowy;
    
    /**Arkusz utworzony z motywu przekazanego tekstem*/
    private Arkusz ArkuszZMotywu;
    
    /**
     * Konstruktor klasy tworzącej motyw muzyczny
     * @param całość Cały system (wszystkie zdarzenia)
     * @param początek Indeks początkowy zdarzenia w systemie
     * @param ile Liczba zdarzeń
     * @param CzyKrab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public Motyw(ArkuszZdarzenie[] całość, int początek, int ile, boolean CzyKrab)
    {
        int i;
        
        int wysokość_1; //Wysokość (MIDI) dźwięku pierwszego
        int wysokość_2; //Wysokość (MIDI) dźwięku drugiego
        int nowy_interwał; //Interwał pomiędzy dźwiękiem drugim a pierwszym
        
        ArkuszZMotywu = null;
        
        if(początek + ile > całość.length)
        {
            początek = całość.length - ile - 1; //Korekcja motywu
            System.out.println("***Zbyt długi motyw***");
        }//end if
        
        IndeksPoczątkowy = początek;
        LiczbaElementów = ile;
        
        CiągMuzyczny = new ArkuszZdarzenie[ile];
        
        for(i = początek; i < początek + ile; i++)
        {
            CiągMuzyczny[i-początek] = całość[i].Kopiuj();
        }//next i
        
        //Teraz korekcja interwałów (na początku motywu!):
        
        wysokość_1 = CiągMuzyczny[0].getZdarzenie().getPitch();
        wysokość_2 = CiągMuzyczny[1].getZdarzenie().getPitch();
        nowy_interwał = KorektaInterwałuHumdrum(wysokość_2 - wysokość_1, wysokość_2);
        
                
        if(CiągMuzyczny[0].getZdarzenie().getInterval() == 0 && CiągMuzyczny[1].getZdarzenie().getInterval() != 0)
        {
            
            CiągMuzyczny[0].getZdarzenie().OznaczPoczątek();
            
            //System.out.println("Oznaczenie początku w:" + CiągMuzyczny[0].getNazwęKolumny() + CiągMuzyczny[0].getWiersz());
        }
        
        CiągMuzyczny[0].getZdarzenie().setInterval(nowy_interwał);
        
        if(CzyKrab)
        {
            WykonajKraba();
            WyliczNaNowoInterwały();
        }//end if
                
        
    }//Koniec konstruktora
    
    
    /**
     * Drugi konstruktor klasy Motyw, służący do wygenerowania motywu z wiersza zapisanego w jednej linii.Ten konstruktor wykorzystywany jest przede wszystkim wtedy, gdy chcemy znaleźć zadany motyw w pliku z arkuszem.<br>
 Motyw może być tylko jednogłosowy.Składnia jest następująca:<br>
 Poszczególne zdarzenia Hummdrum rozdzielone są literą H.
     * <span style="color:red;font-weight: bold;">UWAGA! w każdym zdarzeniu Hummdrum trzeba podać numer taktu!!!</span><br>
     * Podajemy w kolejności następujące wartości: takt, pitch, interval, onset, duration. Poszczególne wartości oddzielamy przecinkami.<br>
     * Przykład:<br>
     * <span style="font-weight: bold;">1,58,0,0,16 P 1,60,2,16,16</span><br>
     * W zapisie można używać spacji, gdyż podczas interpretacji zapisu ten symbol będzie ignorowany. Wartości ułamkowe podajemy z wykorzystaniem kropki!
     * @param WierszZMotywem Wiersz z motywem.
     * @param WagaTon Waga dla interwału tonalnego.
     * @param WagaCzas Waga dla interwału czasowego.
     */
    public Motyw(String WierszZMotywem, double WagaTon, double WagaCzas) throws WyjątekDlaWierszaZMotywem
    {
        int liczba_taktów;
        String[] tablica_taktów;
        String[][] tablica_zdarzeń; //tablica potrzebna do utworzenia arkusza!
        Arkusz arkusz_motywu;
        String[] parametry_zdarzenia;
        
        int wysokość_1; //Wysokość (MIDI) dźwięku pierwszego
        int wysokość_2; //Wysokość (MIDI) dźwięku drugiego
        int nowy_interwał; //Interwał pomiędzy dźwiękiem drugim a pierwszym
        
        int i;
        int j;
        
        tablica_taktów = ZwróćPodzielonyWierszMotywu(WierszZMotywem);
        
        liczba_taktów = 0;
        
        if(tablica_taktów == null) throw new WyjątekDlaWierszaZMotywem(WyjątekDlaWierszaZMotywem.PUSTA_LINIA);
        if(tablica_taktów.length == 0) throw new WyjątekDlaWierszaZMotywem(WyjątekDlaWierszaZMotywem.PUSTA_LINIA);
        if(tablica_taktów.length == 1) throw new WyjątekDlaWierszaZMotywem(WyjątekDlaWierszaZMotywem.MOTYW_ZA_KROTKI);
        
        if(tablica_taktów.length > 1) liczba_taktów = tablica_taktów.length;
        
        for(i = 0; i < liczba_taktów; i++)
        {
            if(CzyTaktJestPoprawny(tablica_taktów[i]) == false) throw new WyjątekDlaWierszaZMotywem(WyjątekDlaWierszaZMotywem.BLAD_W_TAKCIE);
        }//next i
        
        tablica_zdarzeń = new String[tablica_taktów.length][Motyw.LICZBA_PARAMETROW_ZDARZENIA];
        
        for(i = 0; i < tablica_taktów.length; i++)
        {
            
            parametry_zdarzenia = tablica_taktów[i].split(",");
            
            for(j = 0; j < Motyw.LICZBA_PARAMETROW_ZDARZENIA; j++)
            {
                tablica_zdarzeń[i][j] = parametry_zdarzenia[j];
            }//next j
            
        }//next i
        
        arkusz_motywu = this.UtwórzArkuszDoMotywu(tablica_zdarzeń);
        
        ArkuszZMotywu = arkusz_motywu;
        
        CiągMuzyczny = new ArkuszZdarzenie[tablica_taktów.length];
        
        for(i = 0; i < CiągMuzyczny.length; i++)
        {
            CiągMuzyczny[i] = new ArkuszZdarzenie(arkusz_motywu, 1, 3, 3 + i, WagaTon, WagaCzas);
        }//next i
        
        //Teraz korekcja interwałów (na początku motywu!):
        
        wysokość_1 = CiągMuzyczny[0].getZdarzenie().getPitch();
        wysokość_2 = CiągMuzyczny[1].getZdarzenie().getPitch();
        nowy_interwał = KorektaInterwałuHumdrum(wysokość_2 - wysokość_1, wysokość_2);
        
                
        if(CiągMuzyczny[0].getZdarzenie().getInterval() == 0 && CiągMuzyczny[1].getZdarzenie().getInterval() != 0)
        {
            
            CiągMuzyczny[0].getZdarzenie().OznaczPoczątek();
            
            //System.out.println("Oznaczenie początku w:" + CiągMuzyczny[0].getNazwęKolumny() + CiągMuzyczny[0].getWiersz());
        }
        
        CiągMuzyczny[0].getZdarzenie().setInterval(nowy_interwał);
        
        LiczbaElementów = CiągMuzyczny.length;
        IndeksPoczątkowy = 0;
        
        
    }//Koniec drugiego konstruktora
    
    /**
     * Metoda pomocnicza dla drugiego konstruktora, wydzielająca poszczególne zdarzenia Hummdrum.
     * @param WierszZMotywem Wiersz z motywem.
     * @return Tablica podzielonego wierszu z motywem. W wierszu z motywem usunięto wszystkie spcje!
     */
    private String[] ZwróćPodzielonyWierszMotywu(String WierszZMotywem)
    {
        String[] wynik;
        String WierszBezSpacji;
        
        wynik = null;
        
        if(WierszZMotywem != null) //Działamy tylko na niepustym tekście!!!
        {
            if(WierszZMotywem.length() > 0)
            {
                WierszBezSpacji = WierszZMotywem.replace(" ", ""); //Usuwanie spacji.
                
                WierszBezSpacji = WierszBezSpacji.toUpperCase(); //Prewencyjna zamiana "p" na "P".
                
                wynik = WierszBezSpacji.split("P");
                
            }//end if
        }//end if
        
        return wynik;
    }//Koniec metody rozdzielającej wiersz z motywem.
    
    /**
     * Metoda pomocznicza dla konstruktora drugiego, sprawdzająca, czy zapis taktu w linii jest prawidłowy.
     * @param ZapisTaktu Zapis pojedynczego taktu wydobyty metodą <b>ZwróćPodzielonyWierszMotywu</b>.
     * @return <b>true</b>, gdy zapis taktu ma poprawną składnię, w innym przypadku metoda zwraca wartość <b>false</b>.
     */
    private boolean CzyTaktJestPoprawny(String ZapisTaktu)
    {
        boolean wynik;
        String[] składniki;
        int i;
        
        
        wynik = true;
        
        if(ZapisTaktu == null)
        {
            wynik = false;
        }else if(ZapisTaktu.length() == 0)
        {
            wynik = false;
        }else
        {
            składniki = ZapisTaktu.split(",");
            
            if(składniki == null)
            {
                wynik = false;
            }else if(składniki.length < 5)
            {
                wynik = false;
            }else
            {
                i = 0;
                
                do{
                    if(i < 3)
                    {
                        if(wynik) wynik = CzyPoprawnyInt(składniki[i]);
                    }//end if
                    
                    if(i > 2 && wynik == true)
                    {
                        if(wynik) wynik = CzyPoprawnaDouble(składniki[i]);
                    }//end if
                    
                    i++;
                    
                }while(i < 5 && wynik == true);
                
            }//end if
            
            
        }//end if
        
        
        return wynik;
        
    }//Koniec metody sprawdzającej poprawność zapisu taktu.
    
    /**
     * Metoda pomocnicza sprawdzająca, czy zapis tekstowy liczby da się zamienić na wartość.
     * @param liczba Tekst do sprawdzenia
     * @return <b>true</b>, gdy da się zamienić, <b>false</b>, gdy nie da się zamienić.
     */
    private boolean CzyPoprawnyInt(String liczba)
    {
        boolean wynik;
        int zamiana;
        
        wynik = true;
        
        try{
            
            zamiana = Integer.parseInt(liczba);
            
        }catch(NumberFormatException ex)
        {
            wynik = false;
        }
        
        
        return wynik;
    }//koniec metody sprawdzającej
    
    
    /**
     * Metoda pomocnicza sprawdzająca, czy zapis tekstowy liczby da się zamienić na wartość.
     * @param liczba Tekst do sprawdzenia
     * @return <b>true</b>, gdy da się zamienić, <b>false</b>, gdy nie da się zamienić.
     */
    private boolean CzyPoprawnaDouble(String liczba)
    {
        boolean wynik;
        double zamiana;
        
        wynik = true;
        
        try{
            
            zamiana = Double.parseDouble(liczba);
            
        }catch(NumberFormatException ex)
        {
            wynik = false;
        }
        
        return wynik;
    }//Koniec metody sprawdzającej
    
    /**
     * Metoda generująca obiekt klasy Arkusz dla pojedynczego motywu.
     * 
     * Metoda jest niezbędna, gdyż podczas tworzenia obiektu ArkuszZdarzenie trzeba podać referencję do obiektu klasy Arkusz.
     * @param tablica_zdarzeń Poprawna tablica zdarzeń Hummdrum (utworzona wcześniej w konstruktorze).
     * @return Obiekt klasy Arkusz
     */
    private Arkusz UtwórzArkuszDoMotywu(String[][] tablica_zdarzeń)
    {
        int liczba_kolumn;
        int liczba_wierszy;
        Arkusz arkusz_motywu;
        
        int i;
        
        liczba_wierszy = 9; //Patrz plik Uporządkowanie danych w tworzonym arkuszu dla pojedynczego motywu.ods
        liczba_kolumn = tablica_zdarzeń.length + 2; 
        
        arkusz_motywu = new Arkusz(liczba_wierszy, liczba_kolumn);
        
        arkusz_motywu.setKomórkę(1, 1, "S1"); //System pierwszy, nie ma innego, bo motyw jest jednogłosowy
        arkusz_motywu.setKomórkę(3, 2, "takt");
        arkusz_motywu.setKomórkę(4, 2, "pitch");
        arkusz_motywu.setKomórkę(5, 2, "interval");
        arkusz_motywu.setKomórkę(6, 2, "onset");
        arkusz_motywu.setKomórkę(7, 2, "duration");
        
        for(i = 0; i < tablica_zdarzeń.length; i++)
        {
            
            arkusz_motywu.setKomórkę(3, i+3, tablica_zdarzeń[i][0]);
            arkusz_motywu.setKomórkę(4, i+3, tablica_zdarzeń[i][1]);
            arkusz_motywu.setKomórkę(5, i+3, tablica_zdarzeń[i][2]);
            arkusz_motywu.setKomórkę(6, i+3, tablica_zdarzeń[i][3]);
            arkusz_motywu.setKomórkę(7, i+3, tablica_zdarzeń[i][4]);
            
        }//next i
        
        return arkusz_motywu;
    }//Koniec tworzenia arkusza dla pojedynczego motywu
    
    /**
     * Metoda cofająca tworzenie kraba.
     */
    public void Odkrabowanie()
    {
        WykonajKraba();
        WyliczNaNowoInterwały();
    }//Koniec metody
    
    /**
     * Metoda służąca do korekcji wielkości interwału według metody Humdrum
     * @param interwał Interwał (różnica dwóch dźwięków MIDI)
     * @param jeden_ton Wysokość w zdarzeniu następnym, potrzebna do wykrycia pauzy.
     * @param poprzedni_ton Wysokość w zdarzeniu bieżącym (poprzedzającym jeden_ton).
     * @return Skorygowany interwał
     */
    private int KorektaInterwałuHumdrum(int interwał, int jeden_ton)
    {
        int znak;
        int wynik;
        
        wynik = 0;
        
                 
            znak = 1;
            if(interwał < 0) znak = -1;
        
            interwał = Math.abs(interwał);
        
        
            if(interwał == 0 && jeden_ton == 0) //Pauza
            {
                wynik = 0;
            }
            if(interwał == 0 && jeden_ton != 0) //Pryma
            {
                wynik = 1;
            }else if(interwał > 0 && interwał <= 2)//sekunda
            {
                wynik = 2;
            }else if(interwał > 2 && interwał <= 4) //tercja
            {
                wynik = 3;
            }else if(interwał > 4 && interwał <=6) //kwarta
            {
                wynik = 4;
            }else if(interwał == 7) //kwinta
            {
                wynik = 5;
            }else if(interwał > 7 && interwał <= 9) //seksta
            {
                wynik = 6;
            }else if(interwał > 9 && interwał <12) //septyma
            {
                wynik = 7;
            }else if(interwał == 12) //oktawa
            {
                wynik = 8;
            }else if(interwał > 12 && interwał <= 14) //nona
            {
                wynik = 9;
            }else if(interwał > 14 && interwał <= 16) //decyma
            {
                wynik = 10;
            }else if(interwał > 16 && interwał <= 18) //undecyma
            {
                wynik = 11;
            }//end if
            
            wynik = wynik * znak;
            
                
        return wynik;
    }//Koniec korekty
    
    /**
     * Metoda służąca do utworzenia kraba muzycznego (motyw od tyłu).
     */
    private void WykonajKraba()
    {
        ArkuszZdarzenie Pomoc;
        int i;
        int j;
        
        //Najpierw zamieniamy
        
        j = CiągMuzyczny.length - 1;
        
        for(i = 0; i < j; i++)
        {
            Pomoc = CiągMuzyczny[i];
            CiągMuzyczny[i] = CiągMuzyczny[j];
            CiągMuzyczny[j] = Pomoc;
            
            j--;
            
        }//Koniec zamiany
        
    }//Koniec metody wykonującej kraba
    
    /**
     * Metoda potrzebna do wywołania po wykonaniu kraba.
     */
    private void WyliczNaNowoInterwały()
    {
        int i;
        int różnica;
        int interwał;
        int jeden_ton;
        
        jeden_ton = 0;
        
        for(i = CiągMuzyczny.length - 1; i > 0; i--)
        {
            różnica = CiągMuzyczny[i].getZdarzenie().getPitch() - CiągMuzyczny[i-1].getZdarzenie().getPitch();
            
            jeden_ton = CiągMuzyczny[i].getZdarzenie().getPitch();
            
            interwał = this.KorektaInterwałuHumdrum(różnica, jeden_ton);
            
            CiągMuzyczny[i].getZdarzenie().setInterval(interwał);
        }//next i
        
        różnica = CiągMuzyczny[1].getZdarzenie().getPitch() - CiągMuzyczny[0].getZdarzenie().getPitch();
        
        interwał = this.KorektaInterwałuHumdrum(różnica, jeden_ton);
        
        CiągMuzyczny[0].getZdarzenie().setInterval(interwał);
        
    }//Koniec liczenia na nowo interwałów
    
    /**
     * Metoda służąca do sprawdzenia, czy motyw nadaje się do porównywania.
     * 
     * Metoda ta sprawdza, czy zdarzenia w motywie należą do tego samego systemu.
     * Sprawdzenie to jest konieczne w przypadku przeszukiwania całości, czyli wszystkich głosów jednocześnie.
     * @return <b>true</b>, gdy można porównywać (wszystkie zdarzenia są z tego samego systemu). W innym przypadku <b>false</b>.
     */
    public boolean CzyMożnaPorównywać()
    {
        boolean wynik;
        int i;
        int system_początkowy;
        
        wynik = true;
        
        system_początkowy = CiągMuzyczny[0].getNumerGłosu();
        
        i = 0;
        
        do{
            
            if(CiągMuzyczny[i].getNumerGłosu() != system_początkowy) wynik = false;
            
            i++;
            
        }while(wynik == true && i < CiągMuzyczny.length);
        
        
        return wynik;
    }//Koniec metody zwracającej informację, czy można porównywać
    
    /**
     * Metoda zwracająca numer systemu
     * @return Numer systemu
     */
    public int getSystem()
    {
        return CiągMuzyczny[0].getNumerGłosu();
    }//Koniec metody zwracającej system
    
    /**
     * Metoda zwracająca lokalizację początku motywu w arkuszu.
     * @return Lokalizacja w stylu arkusza kalkulacyjnego (plik CSV)
     */
    public String getLokalizacjęPoczątku()
    {
        return CiągMuzyczny[0].getNazwęKolumny() + CiągMuzyczny[0].getWiersz();
    }//Koniec metody zwracającej lokalizację pocżatku
    
    /**
     * Metoda zwracająca lokalizację końca motywu w arkuszu.
     * @return Lokalizacja końca motywu w stylu arkusza kalkulacyjnego (plik CSV).
     */
    public String getLokalizacjęKońca()
    {
        return CiągMuzyczny[CiągMuzyczny.length-1].getNazwęKolumny() + CiągMuzyczny[CiągMuzyczny.length-1].getWiersz();
    }//Koniec metody zwracającej lokalizację końca
    
    /**
     * Metoda zwracająca długość motywu (liczba zdarzeń Humdrum)
     * @return Długość motywu
     */
    public int getDługośćMotywu()
    {
        return CiągMuzyczny.length;
    }//Koniec metody zwracającej długość motywu
    
    /**
     * Metoda zwracająca indeks początkowy motywu
     * @return Indeks początkowy motywu
     */
    public int getIndeksPoczątkowy()
    {
        return this.IndeksPoczątkowy;
    }
    
    
    /**
     * Metoda zwracająca liczbę elementów motywi
     * @return Liczba zdarzeń w motywie
     */
    public int getLiczbęElementów()
    {
        return this.LiczbaElementów;
    }//Koniec metody zwracjącej liczbę zdarzeń
    
    /**
     * Metoda zwracająca referencję do motywu
     * @return Referencja do tablicy motywu
     */
    public ArkuszZdarzenie[] getReferencjęDoMotywu()
    {
        return CiągMuzyczny;
    }//Koniec metody zwracającej referencję do motywu.
    
    /**
     * Metoda obliczająca ocenę podobieństwa jednego motywu do drugiego.
     * @param Inny Inny motyw muzyczny (o tej samej długości!)
     * @return Waga podobieństwa. Wartość 0 oznacza, że inny motyw jest identyczny. Im większa wartość, tym większa różnica.
     */
    public double OceńPodobieństwo(Motyw Inny)
    {
        double suma;
        int i;
        boolean początek;
        int ile; //Ile elementów do porównywania - wybieramy krótszy z motywów, gdy są różnej długości.
        
        suma = 0;
        początek = true;
        ile = LiczbaElementów;
        
        if(Inny.getReferencjęDoMotywu().length < ile) ile = Inny.getReferencjęDoMotywu().length;
        
        for(i = 0; i < ile; i++)
        {
            suma += Math.abs(CiągMuzyczny[i].getZdarzenie().getMiaręRóżnicy(Inny.getReferencjęDoMotywu()[i].getZdarzenie(), początek));
            początek = false;
            
        }//next i
        
        return suma;
    }//Koniec zwracania podobieństwa
    
    /**
     * Metoda zwracająca opis motywu
     * @return Opis motywu
     */
    @Override
    public String toString()
    {
        return "liczba elementów ....... " + LiczbaElementów;
    }//Koniec metody toString
   
    
    /**
     * Metoda zwracająca referencję do arkusza utworzonego z zapisu w jednej linii tekstu.
     * @return Referencja.
     */
    public Arkusz getArkuszZMotywu()
    {
        return ArkuszZMotywu;
    }
    
}//Koniec klasy
