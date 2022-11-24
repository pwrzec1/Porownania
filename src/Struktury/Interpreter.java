
package Struktury;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Klasa służąca do interpretowania danych z arkusza kalkulacyjnego (plik CSV)
 * @author Piotr Wrzeciono
 */
public class Interpreter
{
    /**Arkusz podlegający interpretacji*/
    private Arkusz ArkuszAnalizowany;
    /**Tablica z systemami*/
    private final SystemGłos[] Głosy;
    /**Tablica ze słowami kluczowymi "takt".*/
    private final AdresTakt[] Takty;
    /**Lista z błędami w pliku*/
    private final LinkedList<BłędyZWalidacji> ListaBłędów;
    /**Tablica początków głosów w arkuszu*/
    private final int[] Początki;
    /**Tablica końca głsów w arkuszu*/
    private final int[] Końce;
    
    /**
     * Konstruktor klasy
     * @param dane_wejściowe Arkusz z danymi do analizy (plik przepisany z Humdruma)
     */
    public Interpreter(Arkusz dane_wejściowe)
    {
        PrzepiszArkusz(dane_wejściowe);
        

        
        Głosy = ZnajdźSystemy();
        Takty = ZnajdźAdresySłowaTakt();
        
        System.out.println("Wykryta liczba systemów = " + Głosy.length);
        
       
        
        ListaBłędów = new LinkedList<>();
        
        Początki = new int[Głosy.length];
        Końce = new int[Głosy.length];
        
        UzupełnijNumeryTaktów();
        
    }//Koniec interpretacji danych
    
    /**
     * Metoda tworząca kopię arkusza celem interpretacji.
     * @param dane_wejściowe Arkusz wejściowy z pliku CSV.
     */
    private void PrzepiszArkusz(Arkusz dane_wejściowe)
    {
        int liczba_kolumn;
        int liczba_wierszy;
        String zawartość;
        
        int kolumna;
        int wiersz;
        
        liczba_kolumn = dane_wejściowe.getLiczbęKolumn();
        liczba_wierszy = dane_wejściowe.getLiczbęWierszy();
        
        ArkuszAnalizowany = new Arkusz(liczba_wierszy, liczba_kolumn);
        
        for(wiersz = 1; wiersz <= liczba_wierszy; wiersz++)
        {
            for(kolumna = 1; kolumna <= liczba_kolumn; kolumna++)
            {
                zawartość = dane_wejściowe.getZawartość(wiersz, kolumna).toLowerCase(); //Zamiana znaków wszystkich na małe! - pozbycie się rozróżnienia wielkości znaków
                
                    ArkuszAnalizowany.setKomórkę(wiersz, kolumna, zawartość.replace(',', '.'));
            }//next kolumna
            
        }//next wiersz
        
    }//Koniec przepisywania
    
    /**
     * Metoda znajdująca systemy w arkuszu
     * @return Tablica systemów
     */
    private SystemGłos[] ZnajdźSystemy()
    {
        SystemGłos[] wynik;
        LinkedList<SystemGłos> lista;
        int i;
        boolean czy_jest;
        String pomoc;
        SystemGłos Obiekt;
        
        int wiersz;
        int kolumna;
        
        wynik = null;
        
        lista = new LinkedList<>();
        
        for(wiersz = 1; wiersz <= ArkuszAnalizowany.getLiczbęWierszy(); wiersz++)
        {
            for(kolumna = 1; kolumna <= ArkuszAnalizowany.getLiczbęKolumn(); kolumna++)
            {
                pomoc = ArkuszAnalizowany.getZawartość(wiersz, kolumna);
                czy_jest = SystemGłos.CzySystem(pomoc);
                
                if(czy_jest == true)
                {
                    Obiekt = new SystemGłos(pomoc, wiersz, kolumna, pomoc);
                    
                    lista.add(Obiekt);
                }//end if
            }//next kolumna
        }//next wiersz
        
        if(lista.size() > 0)
        {
            
            wynik = new SystemGłos[lista.size()];
            
            for(i = 0; i < wynik.length; i++)
            {
                wynik[i] = lista.get(i);
            }//next i
            
        }//end if
        
        return wynik;
        
    }//Koniec metody znajdującej tablicę systemów w arkuszu
    
    /**
     * Metoda pokazująca znalezione systemu w pliku CSV.
     */
    public void PokażSystemy()
    {
        int i;
        
        if(this.Głosy == null)
        {
            System.out.println("W arkuszu kalkulacyjnym nie znaleziono żadnego głosu!");
        }else
        {
            for(i = 0; i < Głosy.length; i++)
            {
                System.out.println("Głosy[" + i + "] = ");
                System.out.println(Głosy[i]);
            }//next i
            
        }//end if
        
    }//Koniec metody pokazującej systemy
    
    
    /**
     * Metoda zwracająca adresy słowa "takt" w Arkuszu.
     * @return Adresy słowa "takt" w arkuszu - tablica
     */
    private AdresTakt[] ZnajdźAdresySłowaTakt()
    {
        AdresTakt[] tablica;
        int i;
        String pomoc;
        
        int wiersz;
        int kolumna;
        
        int min_wiersz;
        int max_wiersz;
        int max_kolumna;
        
        boolean czy_przestać;
        
        tablica = null;
        czy_przestać = false;
        max_kolumna = ArkuszAnalizowany.getLiczbęKolumn();
        
        if(this.Głosy != null)
        {
            tablica = new AdresTakt[Głosy.length];
            
            for(i = 0; i < tablica.length; i++)
            {
                min_wiersz = Głosy[i].Wiersz + 1; //Przynajmniej jeden wiersz dalej
                
                if(i < tablica.length - 1)
                {
                    max_wiersz = Głosy[i+1].Wiersz - 1; //Co najwyżej jeden wiersz wcześiej
                }else
                {
                    max_wiersz = ArkuszAnalizowany.getLiczbęWierszy();
                }//end if
                
                
                kolumna = 1;
                wiersz = min_wiersz;
                czy_przestać = false;
                
                do{
                    pomoc = ArkuszAnalizowany.getZawartość(wiersz, kolumna).trim();
                    
                    if(pomoc.compareTo("takt") == 0)
                    {
                        czy_przestać = true;
                    }else
                    {
                        kolumna++;
                        
                        if(kolumna > max_kolumna)
                        {
                            kolumna = 1;
                            wiersz++;
                        }//end if
                        
                        if(wiersz > max_wiersz) czy_przestać = true;
                        
                    }//end if
                    
                }while(czy_przestać == false);
                
                
                if(pomoc.compareTo("takt") == 0)//Znaleziono takt!
                {
                    tablica[i] = new AdresTakt(wiersz, kolumna, i);
                }else
                {
                    tablica[i] = null; //Nie znaleziono słowa "takt" !
                }//end if
                
                
            }//next i
            
        }//end if
        
        
        return tablica;
    }//Koniec wyszukwania słów "takt"
    
    /**
     * Metoda pokazująca tablicę słów "takt".
     */
    public void PokażTablicęSłowaTakt()
    {
        boolean czy_wszystko;
        int i;
        
        czy_wszystko = true;
        
        if(Takty != null)
        {
            for(i = 0; i < Takty.length; i++)
            {
                if(Takty[i] == null) czy_wszystko = false;
            }//next i
            
            if(czy_wszystko == false) System.out.println("Tablica taktów nie jest kompletna!");
            
            for(i = 0; i < Takty.length; i++)
            {
                if(Takty[i] == null)
                {
                    System.out.println("Takty[" + i + "] są puste!!!");
                }else
                {
                    System.out.println("Takty[" + i + "]\n" + Takty[i]);
                }//end if
            }//next i
            
        }else
        {
            System.out.println("Tablica ze słowami kluczowymi \"takt\" jest pusta!");
        }//end if
        
    }//Koniec metody pokazującej tablicę słowa takt
    
    /**
     * Metoda służąca do uzupełnienia brakujących numerów taktów.
     */
    private void UzupełnijNumeryTaktów()
    {
        String bieżący_numer;
        int i;
        int wiersz;
        int kolumna;
        String pomoc;
        String pomoc1;
        String pomoc2;
        String pomoc3;
        String zawartość;
        boolean czy_puste;
        
        
        if(Takty != null)
        {
            for(i = 0; i < Takty.length; i++)
            {
                wiersz = Takty[i].Wiersz;
                kolumna = Takty[i].Kolumna + 1;
                
                Początki[i] = kolumna;
                
                bieżący_numer = ArkuszAnalizowany.getZawartość(wiersz, kolumna).trim();
                
                do{
                    
                    pomoc = ArkuszAnalizowany.getZawartość(wiersz + 1, kolumna);
                    pomoc1 = ArkuszAnalizowany.getZawartość(wiersz + 2, kolumna);
                    pomoc2 = ArkuszAnalizowany.getZawartość(wiersz + 3, kolumna);
                    pomoc3 = ArkuszAnalizowany.getZawartość(wiersz + 4, kolumna);
                    
                    zawartość = ArkuszAnalizowany.getZawartość(wiersz, kolumna).trim();
                    
                    czy_puste = this.CzyWszystkiePuste(pomoc1, pomoc2, pomoc3, pomoc);
                    
                    if(zawartość.compareTo(bieżący_numer) != 0 && czy_puste == false)
                    {
                        if(zawartość.compareTo("") == 0)
                        {
                            ArkuszAnalizowany.setKomórkę(wiersz, kolumna, bieżący_numer);
                        }else
                        {
                            bieżący_numer = ArkuszAnalizowany.getZawartość(wiersz, kolumna).trim();
                        }//end if
                        
                        WalidacjaLiczb(wiersz, kolumna, true);
                        WalidacjaLiczb(wiersz + 1, kolumna, true);
                        WalidacjaLiczb(wiersz + 2, kolumna, false);
                        WalidacjaLiczb(wiersz + 3, kolumna, true);
                        WalidacjaLiczb(wiersz + 4, kolumna, true);
                        
                        
                    }//end if
                    
                    
                    if(czy_puste == false) kolumna++; //Bez tego if liczba kolumn jest o 1 za duża!
                    
                    if(kolumna > ArkuszAnalizowany.getLiczbęKolumn()) czy_puste = true;
                    
                    if(czy_puste == true) this.Końce[i] = kolumna - 1;
                    
                }while(czy_puste == false);
            }//next i
            
        }//end if
        
        
    }//Koniec uzupełniania numerów taktów
    
    /**
     * Metoda sprawdzająca, czy wszystkie teksty są puste.
     * @param t1 Tekst pierwszy
     * @param t2 Tekst drugi
     * @param t3 Tekst trzeci
     * @param t4 Tekst czwarty
     * @return <b>true</b>, gdy wszystkie teksty są puste, <b>false</b> w innym przypadku
     */
    private boolean CzyWszystkiePuste(String t1, String t2, String t3, String t4)
    {
        String tab[];
        boolean wynik;
        int i;
        
        wynik = true;
        tab = new String[4];
        
        tab[0] = t1.trim();
        tab[1] = t2.trim();
        tab[2] = t3.trim();
        tab[3] = t4.trim();
        
        for(i = 0; i < tab.length; i++)
        {
            if(tab[i].compareTo("") != 0) wynik = false;
        }//next i
        
        
        return wynik;
        
    }//Koniec metody sprawdzającej, czy wszystkie teksty są puste
    
    /**
     * Metoda pokazująca arkusz analizowany w konsoli.
     */
    public void PokażArkusz()
    {
        ArkuszAnalizowany.PokażArkusz();
    }//Koniec metody pokazujacej arkusz w konsoli
    
    
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
            test = Double.parseDouble(tekst.trim());
                
        }catch(Exception ex)
        {
            wynik = false;
        }//end tr-catch
        
        
        
        
        if(tekst.trim().compareTo("") == 0) wynik = false;
        
        return wynik;
    }//Koniec sprawdzania, czy liczba
    
    
    /**
     * Metoda walidująca arkusz analizowany pod względem obecności liczb
     * @param wiersz Wiersz w arkuszu
     * @param kolumna  Komórka w arkuszu.
     * @param czy_ma_być_dodatnia Wartość <b>true</b> służy do sprawdzenia dodatkowo wartości liczbowej (znak). Wartość <b>false</b> wyłącza sporawdzanie znaku.
     */
    private void WalidacjaLiczb(int wiersz, int kolumna, boolean czy_ma_być_dodatnia)
    {
        String wartość;
        boolean czy_liczba;
        BłędyZWalidacji błąd;
        
        wartość = ArkuszAnalizowany.getZawartość(wiersz, kolumna);
        
        czy_liczba = CzyLiczba(wartość);
        
        if(czy_liczba == false)
        {
            
            błąd = new BłędyZWalidacji(wartość, ArkuszAnalizowany.getNazwęKolumny(kolumna), wiersz, kolumna, BłędyZWalidacji.WARTOSC_TEKSTOWA);
            
            wartość = "BŁĄD!";
            
            ListaBłędów.add(błąd);
            
            ArkuszAnalizowany.setKomórkę(wiersz, kolumna, wartość);
        }else
        {
            if((Double.parseDouble(wartość) < 0) && (czy_ma_być_dodatnia == true))
            {
                błąd = new BłędyZWalidacji(wartość, ArkuszAnalizowany.getNazwęKolumny(kolumna), wiersz, kolumna, BłędyZWalidacji.WARTOSC_UJEMNA);
            
                wartość = "BŁĄD ZNAKU!";
            
                ListaBłędów.add(błąd);
            
                ArkuszAnalizowany.setKomórkę(wiersz, kolumna, wartość);
                
            }//end if
        }//end if
        
        
        
    }//Koniec metody walidującej dane
    
    /**
     * Metoda zwracająca listę błędów w pliku CSV.
     * @return Referencja do listy błędów.
     */
    public LinkedList<BłędyZWalidacji> getListęBłędów()
    {
        return this.ListaBłędów;
    }//Koniec metody zwracającej referecnję do listy błędów
    
    /**
     * Metoda pokazująca w konsoli listę błędów z pliku CSV.
     */
    public void PokażListęBłędów()
    {
        int i;
        
        for(i = 0; i < ListaBłędów.size(); i++)
        {
            System.out.println(ListaBłędów.get(i));
        }//next i
        
    }//Koniec pokazywania listy błędów
    
    /**
     * Metoda generująca tablicę ze zdarzeniami
     * @param i Numer głosu (liczymy od basu, od wartości 1)
     * @param WagaTon Waga interwałowa
     * @param WagaCzas Waga metryczna
     * @return Tablica z głosem w zdarzeniach Humdrum'a
     */
    public ArkuszZdarzenie[] getGłos(int i, double WagaTon, double WagaCzas)
    {
        ArkuszZdarzenie[] wynik;
        int ile;
        int wiersz;
        int kolumna;
        int numer;
        int j;
        
        i--; //Korekcja
        
        ile = this.Końce[i] - Początki[i] + 1;
        wynik = new ArkuszZdarzenie[ile];
        
        numer = Takty[i].NumerSystemu;
        wiersz = Takty[i].Wiersz;
        
        for(j = Początki[i]; j <= Końce[i]; j++)
        {
            kolumna = j;
                                  
            wynik[j - Początki[i]] = new Struktury.ArkuszZdarzenie(ArkuszAnalizowany, numer, wiersz, kolumna, WagaTon, WagaCzas);
            
            if(wynik[j - Początki[i]].getStanZdarzenia() != ArkuszZdarzenie.ZAPIS_BEZ_BŁĘDU)
            {
                System.out.println("Error!" + wynik[j - Początki[i]].getOpisWalidacji());
            }
            
        }//next i
        
        
        return wynik;
    }//Koniec metody generującej tablicę zawierającą zdarzenia
    
    /**
     * Metoda zwracająca liczbę głosów w utworze.
     * @return Liczba głosów.
     */
    public int getLiczbęGłosów()
    {
        return Głosy.length;
    }//Koniec metody zwracającej liczbę głosów
    
    
    /**
     * Metoda generująca scalony głos - wszystkie głosy razem.
     * @param WagaTon Waga interwałowa
     * @param WagaCzas Waga metryczna
     * @return Pełne połączenie.
     */
    public ArkuszZdarzenie[] getCałośćPołączoną(double WagaTon, double WagaCzas)
    {
        ArrayList<ArkuszZdarzenie> lista_zdarzeń;
        ArkuszZdarzenie[] wynik;
        ArkuszZdarzenie[] pojedynczy;
        
        int i;
        int j;
        
        wynik = null;
        lista_zdarzeń = new ArrayList<>();
        
        if(Głosy.length > 0) //W innym przypadku nie ma sensu
        {
            for(i = 0; i < Głosy.length; i++)
            {
                pojedynczy = getGłos(i + 1, WagaTon, WagaCzas);
                
                for(j = 0; j < pojedynczy.length; j++)
                {
                    lista_zdarzeń.add(pojedynczy[j]);
                }//next j
                
            }//next i
            
            wynik = new ArkuszZdarzenie[lista_zdarzeń.size()];
            
            for(i = 0; i < lista_zdarzeń.size(); i++)
            {
                wynik[i] = lista_zdarzeń.get(i);
            }//next i
            
        }//end if
        
        return wynik;
        
    }//Koniec metody tworzącej jedną całość połączoną
    
    
    /**
     * Metoda zwracająca oznaczenia tekstowe systemów (głosów)
     * @return Oznaczenie literowe systemów.
     */
    public String[] getOznaczeniaGłosów()
    {
        String[] wynik;
        int i;
        
        wynik = new String[Głosy.length];
        
        for(i = 0; i < wynik.length; i++)
        {
            wynik[i] = Głosy[i].NazwaTekstowaSystemu;
        }
        
        return wynik;
    }//Koniec metody zwracającej literowe oznaczenie systemu.
    
}//Koniec klasy





