
package porownania;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Struktury.*;
import Motywy.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Klasa służąca do analizy porównawczej motywów muzycznych z plików CSV uprzednio zwalidowanych.
 * @author Piotr Wrzeciono
 */
public class AnalizaPorównawcza implements Runnable
{
    /**Pasek postępu walidacji*/
    private final JProgressBar PasekPostępuWalidacji;
    /**Pasek postępu informujący o procencie przetworzonych plików*/
    private final JProgressBar PasekPostępuAnalizy;
    /**Pole tekstowe służące do wyprowadzenia nazwy pliku aktualnie przetwarzanego*/
    private final JTextField PoleTekstoweAnalizy;
    /**Pole tekstowe służące do wyświetlania statusu przetwarzania*/
    private final JTextArea PoleTekstoweDlaNazwyPliku;
    /**Analiza wyłącznie pojedynczych głosów - na szybkość*/
    private final JCheckBox TylkoPojedyncze;
    /**Progress bar do iteracji*/
    private final JProgressBar PasekIteracji;
    /**Label do podpisu pod paskiem postępu*/
    private final JLabel EtykietaPodPaskiemPostępu;
    /**Tablica plików poprawnie zwalidowanych*/
    private PlikZwalidowany[] TablicaPlików;
    /**Nazwa pliku wyjściowego (z wynikami)*/
    private final String PlikWynikowy;
    /**Tablica z nazwami plików CSV*/
    private final String[] TablicaNazwPlików;
    /**Kodowanie znaków w pliku CSV*/
    private final int KodowanieZnaków;
    /**Minimalna liczba zdarzeń Humdrum*/
    private final int MinimalnaDługość;
    /**Wartość wagi interwałowej*/
    private final double WagaInterwałowa;
    /**Wartość wagi metrycznej*/
    private final double WagaMetryczna;
    /**Maksymalna akceptowanla różnica pomiędzy motywami*/
    private final double MaksymalnaRóżnica;
    /**Stan wątku obliczeniowego*/
    private volatile int StanWątku;
    /**Tekst pierwotny w podpisie (label) pod procentem obliczeń*/
    private final String TekstPierwotny;
    /**Checkbox do kraba*/
    private final JCheckBox KrabBox;
    /**Checkbox do pojedynczej długości*/
    private final JCheckBox JednaDługośćBox;
    /**Przycisk Startu obliczeń*/
    private final JButton StartButton;
    
    /**Pole do zapisu motywu*/
    JTextField MotyDoWyszukaniaTextField;
    /**Checkbox do uruchomienia wyszukiwania motywu.*/
    JCheckBox CzySzukaćMotywuBox;
    
    /**Tekst do zapisu do pliku wynikowego*/
    private String WynikiDoZapisu;
    /**Plik do zapisu wyników*/
    private ZapisywanieTekstuDoPliku ZapisywaczDoPliku;
    
    /**Wyszukiwanie motywu o pojedynczej długości: <b>true</b>, gdy szukamy tylko motywów o zadanej długości. <b>false</b>, gdy szukamy motywów o dowolnej długości.*/
    private final boolean CzyTylkoJednąDługość;
    
    /**Obliczenia jeszcze nie zostały rozpoczęte*/
    private static final int PRZED_OBLICZENIAMI = 0;
    /**W trakcie obliczeń*/
    private static final int W_TRAKCIE_OBLICZEŃ = 1;
    /**Obliczenia zostały zakończone*/
    private static final int PO_OBLICZENIACH = 2;

    /**
     * Konstruktor Analizy porównawczej dla motywów muzycznych.
     * @param tab_plików Tablica z nazwami plików CSV
     * @param nazwa_pliku_wynikowego Nazwa pliku wynikowego
     * @param kod_znaków Kodowanie znaków według klasy PlikCSV.
     * @param min_długość Minimalna liczba zdarzeń Humdrum w motywie
     * @param waga_int Wartość wagi interwałowej
     * @param waga_m Wartość wagi metrycznej
     * @param max_r Maksymalna dopuszczalna różnica pomiędzy motywami
     * @param walidacja_bar Pasek posępu dla walidacji
     * @param analiza_bar Pasek postępu dla analizy
     * @param pliki_pole Pole tekstowe do wyświetlania nazwy pliku
     * @param analiza_pole POle tekstowe do informowania o stanie przetwarzania pliku.
     * @param tylko_pojedyncze Referencja do obiektu, który informuje program, że ma analizować wyłącznie pojedyncze głosy
     * @param iteracje Referencja do paska postępu dla iteracji
     * @param etykieta Label pod paskiem postępu.
     * @param krab_box CheckBox służący do wyszukiwania formy krab (rak).
     * @param start_button Przycisk startu.
     * @param tylko_jedną_długość Wyszukujemy tylko motywy o zadanej długości.
     * @param motyw_pole Pole do wyszukiwania zadanego motywu
     * @param czy_szukać_motywu Checkbox czy szukać motywu.
     */
    public AnalizaPorównawcza(String[] tab_plików, String nazwa_pliku_wynikowego, int kod_znaków, int min_długość, double waga_int, double waga_m, double max_r, JProgressBar walidacja_bar, JProgressBar analiza_bar, JTextArea pliki_pole, JTextField analiza_pole, JCheckBox tylko_pojedyncze, JProgressBar iteracje, JLabel etykieta, JCheckBox krab_box, JButton start_button, JCheckBox tylko_jedną_długość, JTextField motyw_pole, JCheckBox czy_szukać_motywu)
    {
        TablicaNazwPlików = tab_plików;
        PlikWynikowy = nazwa_pliku_wynikowego;
        
        PasekPostępuWalidacji = walidacja_bar;
        PasekPostępuAnalizy = analiza_bar;
        PoleTekstoweAnalizy = analiza_pole;
        PoleTekstoweDlaNazwyPliku = pliki_pole;
        EtykietaPodPaskiemPostępu = etykieta;
        JednaDługośćBox = tylko_jedną_długość;
        MotyDoWyszukaniaTextField = motyw_pole;
        CzySzukaćMotywuBox = czy_szukać_motywu;
        
        PasekIteracji = iteracje;
        
        KodowanieZnaków = kod_znaków;
        
        TablicaPlików = null;
        KrabBox = krab_box;
        StartButton = start_button;
        
        MinimalnaDługość = min_długość;
        WagaInterwałowa = waga_int;
        WagaMetryczna = waga_m;
        MaksymalnaRóżnica = max_r;
        
        TylkoPojedyncze = tylko_pojedyncze;
        
        PasekPostępuAnalizy.setMaximum(100);
        PasekPostępuAnalizy.setMinimum(0);
        PasekPostępuAnalizy.setStringPainted(true);
        
        TekstPierwotny = this.EtykietaPodPaskiemPostępu.getText();
        
        CzyTylkoJednąDługość = tylko_jedną_długość.isSelected();
        
        Walidacja();
        
        WynikiDoZapisu  = "Wyniki analizy wyszukiwania motywów muzycznych.\n";
        WynikiDoZapisu += "Rodzaj wyszukiwania:\t";
        
        if(TylkoPojedyncze.isSelected())
        {
            WynikiDoZapisu += "każdy głos oddzielnie\n";
        }else
        {
            WynikiDoZapisu += "każdy głos oddzielnie oraz wszystkie połączone w jeden system\n";
        }//end if
        
        if(JednaDługośćBox.isSelected())
        {
            WynikiDoZapisu += "Wyszukiwanie dla jednej zadanej długości motywu\n";
        }
        
        StanWątku = AnalizaPorównawcza.PRZED_OBLICZENIAMI;
        
        ZapisywaczDoPliku = new ZapisywanieTekstuDoPliku(PlikWynikowy);
        
        
        //PokażTablicęPlików();
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca status zapisywacza do pliku
     * @return Status zapisywacza.
     */
    public int getStatusZapisywaczaDoPliku()
    {
        return ZapisywaczDoPliku.getStatusPliku();
    }
    
    /**
     * Metoda służąca do waliacji plików i generacji tablicy z prawidłowymi plikami CSV.
     * Metoda testowa.
     */
    private void Walidacja()
    {
        WalidacjaPlikówCSV walidator;
        Thread wątek;
        
        walidator = new porownania.WalidacjaPlikówCSV(TablicaNazwPlików, PasekPostępuWalidacji, PlikWynikowy, KodowanieZnaków, WalidacjaPlikówCSV.BRAK_ZAPISYWANIA);
        
        wątek = new Thread(walidator);
        
        wątek.start();
        
        do{ //Czekanie na zakończenie walidacji
            
        }while(walidator.getStatusWątku() != WalidacjaPlikówCSV.PO);
        
        
        TablicaPlików = walidator.getTablicęPlikówPoprawnych();
        
    }//Koniec metody realizującej walidację plików
    
    /**
     * Metoda służąca do pokazywania tablicy plików zwalidowanych.
     */
    private void PokażTablicęPlików()
    {
        int i;
        
        if(this.TablicaPlików == null)
        {
            System.out.print("Tablica plików nie istnieje!");
        }else
        {
            if(TablicaPlików.length == 0)
            {
                System.out.println("Tablica plików jest pusta!");
            }else
            {
                for(i = 0; i < TablicaPlików.length; i++)
                {
                    System.out.println(TablicaPlików[i]);
                }
            }//end if
        }//end if
        
    }//Koniec metody pokazującej tablicę plików zwalidowanych
    
    /**
     * Metoda zwracająca stan wątka obliczeniowego
     * @return Bierzący stan
     */
    public int getStanWątkaObliczeniowego()
    {
        return StanWątku;
    }//Koniec metody zwracającej stan
    
    /**
     * Główna metoda wątku obliczeniowego
     */
    @Override
    public void run()
    {
        PlikCSV plik_bieżący;
        Arkusz dane_z_pliku;
        ArkuszZdarzenie[][] głosy;
        String nazwa_pliku;
        Interpreter czytadło;
        ZnajdowanieMotywówCałość motywy_całość;
        Fragmenty motyw_pojedynczy;
        ParaMotywów[] pomocnicza_tab;
        
        
        Motyw motyw_do_porównywania;
        boolean czy_motyw_jest_prawidłowy;
        Interpreter interpreter_motywu;
        String tekst_z_wynikami;
        String tekst_z_zakodowanym_motywem;
        PorównanieZZadanym porównywarka_motywów;
        ArkuszZdarzenie[] całość_dla_motywu;
        
        
        WynikWyszukiwania[] tablica_wyników;
        Rezultat nowy;
        
        Thread wątek_iteracyjny_sterowanie;
        PokazywanieProcentaIteracji pokazywanie_iteracji;
        
        int liczba_głosów;
        int liczba_zdarzeń_humdrum;
        int i; //indeks
        int j;
        int k;
        boolean czy_upakowano;
        String opis;
        
        this.EtykietaPodPaskiemPostępu.setText(this.TekstPierwotny);
        
        motyw_do_porównywania = null;
        czy_motyw_jest_prawidłowy = false;
        tekst_z_wynikami = "";
        całość_dla_motywu = null;
        tekst_z_zakodowanym_motywem = "";
        
        //Pierwszy etap przygotowania do szukania motywu
        if(CzySzukaćMotywuBox.isSelected() == true)
        {
            tekst_z_zakodowanym_motywem = MotyDoWyszukaniaTextField.getText();
            
            try {
                motyw_do_porównywania = new Motyw(tekst_z_zakodowanym_motywem, WagaInterwałowa, WagaMetryczna);
                
                czy_motyw_jest_prawidłowy = true;
                
                tekst_z_wynikami = "\n******************************";
                
            } catch (WyjątekDlaWierszaZMotywem ex)
            {
                JOptionPane.showMessageDialog(EtykietaPodPaskiemPostępu.getParent(), ex, "Błąd", JOptionPane.ERROR_MESSAGE);
                
                CzySzukaćMotywuBox.setSelected(false); //Profilatyczne wyłączenie
            }
        }//end if
        
                
        if(this.TablicaPlików != null) //Tylko w tym przypadku warto cokolwiek robić!
        {
            StartButton.setEnabled(false);
            
            StanWątku = AnalizaPorównawcza.W_TRAKCIE_OBLICZEŃ;
            
            this.WyprowadźProcentPrzetworzonychPlików(0, TablicaPlików.length);
            
            tablica_wyników = new WynikWyszukiwania[TablicaPlików.length];
            
            for(i = 0; i < TablicaPlików.length; i++) //i = indeks plików
            {
                tablica_wyników[i] = new porownania.WynikWyszukiwania(TablicaPlików[i].getNazwęPliku(), WagaInterwałowa, WagaMetryczna, MaksymalnaRóżnica, MinimalnaDługość);
                
                     
                //W pierwszym kroku wczytujemy plik z tablicy
                nazwa_pliku = TablicaPlików[i].getNazwęPliku();
                
                plik_bieżący = new Struktury.PlikCSV(nazwa_pliku, this.KodowanieZnaków);
                
                PoleTekstoweDlaNazwyPliku.setText(nazwa_pliku);
                
                dane_z_pliku = plik_bieżący.WczytajDane();
                
                if(plik_bieżący.getStanPliku() == PlikCSV.PLIK_WCZYTANO)
                {
                    //Teraz musimy pobrać głosy
                    
                    PoleTekstoweAnalizy.setText("Wczytywanie pliku....");
                    
                    czytadło = new Interpreter(dane_z_pliku);
                    
                    liczba_głosów = czytadło.getLiczbęGłosów();
                    
                    głosy = new ArkuszZdarzenie[liczba_głosów][];
                    
                    for(j = 1; j <= liczba_głosów; j++ ) //Wczytanie każdego z głosów oddzielnie
                    {
                        głosy[j-1] = czytadło.getGłos(j, WagaInterwałowa, WagaMetryczna);
                    }//next j
                    
                    //Teraz przygotowanie do analizy wszystkich głosów razem
                    PoleTekstoweAnalizy.setText("Tworzenie reprezentacji wszystkich głosów w jednej strukturze....");
                    
                    motywy_całość = new Motywy.ZnajdowanieMotywówCałość(liczba_głosów, MaksymalnaRóżnica);
                    
                    for(j = 0; j < liczba_głosów; j++)
                    {
                        motywy_całość.DopiszGłos(j+1, głosy[j]);
                    }//next j
                    
                    czy_upakowano = motywy_całość.Upakuj(); //Pakowanie wszystkich głosów do reprezentacji jednego
                    
                    
                    //Teraz dla każdego głosu sprawdzamy
                    
                    for(j = 1; j <= liczba_głosów; j++)
                    {
                        k = this.MinimalnaDługość;
                        do{
                            
                            opis  = "Liczba systemów: " + liczba_głosów  + ", Szukanie motywów dla systemu nr: " + j + ". ";
                            opis += " Liczba zdarzeń w motywie: " + k;
                            
                            
                            PoleTekstoweAnalizy.setText(opis);
                                    
                            motyw_pojedynczy = new Motywy.Fragmenty(głosy[j-1], k, this.KrabBox.isSelected());
                            
                            pokazywanie_iteracji = new PokazywanieProcentaIteracji(this.PasekIteracji, motyw_pojedynczy);
                            wątek_iteracyjny_sterowanie = new Thread(pokazywanie_iteracji);
                            wątek_iteracyjny_sterowanie.start();
                            
                            motyw_pojedynczy.UtwórzListę(this.MaksymalnaRóżnica);
                            
                            pomocnicza_tab = motyw_pojedynczy.getParyMotywów(this.MaksymalnaRóżnica);
                            
                            if(pomocnicza_tab != null)
                            {
                                
                                nowy = new porownania.Rezultat(k, pomocnicza_tab, j, czytadło.getOznaczeniaGłosów()[j-1]);
                                
                                tablica_wyników[i].DopiszWyniki(nowy);
                                
                                pokazywanie_iteracji.Stop();
                                wątek_iteracyjny_sterowanie = null;
                                pokazywanie_iteracji = null;
                                
                                if(CzyTylkoJednąDługość == false) k++;
                                
                            }//end if
                            
                        }while(pomocnicza_tab != null && CzyTylkoJednąDługość == false);
                        
                    }//next j
                                        
                    if(TylkoPojedyncze.isSelected() == false)
                    {
                        k = this.MinimalnaDługość;
                    
                        do{
                        
                            opis = "Przeszukiwanie całości. Liczba zdarzeń Humdrum: " + k;
                        
                            this.PoleTekstoweAnalizy.setText(opis);
                        
                            motywy_całość.UtwórzPorównanie(k, this.KrabBox.isSelected());
                            
                            pokazywanie_iteracji = new PokazywanieProcentaIteracji(this.PasekIteracji, motywy_całość.getReferencjęDoSzukania());
                            wątek_iteracyjny_sterowanie = new Thread(pokazywanie_iteracji);
                            wątek_iteracyjny_sterowanie.start();
                            
                            motywy_całość.UruchomPorównanie(this.MaksymalnaRóżnica);
                        
                            pomocnicza_tab = motywy_całość.getTablicęMotywów();
                            
                        
                            if(pomocnicza_tab != null)
                            {
                                
                                nowy = new porownania.Rezultat(k, pomocnicza_tab, 0, "Wszystkie systemy razem");
                                
                                tablica_wyników[i].DopiszWyniki(nowy);
                                
                                if(CzyTylkoJednąDługość == false) k++;
                                
                            }//end if
                            
                            pokazywanie_iteracji.Stop();
                            wątek_iteracyjny_sterowanie = null;
                            pokazywanie_iteracji = null;
                        
                        }while(pomocnicza_tab != null && CzyTylkoJednąDługość == false);
                    
                    }//end if Czy tylko pojedyncze głosy == false
                    
                    this.WyprowadźProcentPrzetworzonychPlików(i+1, TablicaPlików.length); //Liczba plików jest większa od 0.
                    
                    
                    if(czy_motyw_jest_prawidłowy) //Szukanie motywu jako ekstra
                    {
                        całość_dla_motywu = czytadło.getCałośćPołączoną(WagaInterwałowa, WagaMetryczna);
                        porównywarka_motywów = new Motywy.PorównanieZZadanym(całość_dla_motywu, motyw_do_porównywania, tekst_z_zakodowanym_motywem, WagaInterwałowa, WagaInterwałowa, MaksymalnaRóżnica);
                        porównywarka_motywów.ZrealizujPorównanie();
                        
                        tekst_z_wynikami += "\nNazwa pliku: " + nazwa_pliku + "\n";
                        tekst_z_wynikami += porównywarka_motywów + "\n";
                                                
                    }//end if czy_motyw_jest_prawidłowy
                    
                }//end if PlikCSV.PLIK_WCZYTANO
                
                
                
            }//next i (Następny plik)
            
            EtykietaPodPaskiemPostępu.setText("Przygotowanie pliku wynikowego....");
            
            this.PasekIteracji.setValue(0);
            
            //Poczekajka
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(AnalizaPorównawcza.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Koniec poczekajki
            
            ZapisywaczDoPliku.OtwórzPlik();
            
            System.out.println("Zaczęto proces zapisywania wyników.....");
            
            if(ZapisywaczDoPliku.getStatusPliku() == ZapisywanieTekstuDoPliku.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ)
            {
                System.out.println("Pliku nie udało się otworzyć!");
                JOptionPane.showMessageDialog(EtykietaPodPaskiemPostępu.getParent(), "Pliku wynikowego nie udało się otworzyć!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }else
            {
                
                System.out.println("Plik udało się otworzyć!");
                System.out.println("tablica_wyników.length = " + tablica_wyników.length);
                
                EtykietaPodPaskiemPostępu.setText("Zapisywanie danych....");
                
                ZapisywaczDoPliku.ZapiszTekst(WynikiDoZapisu);
                
            
                for(i = 0; i < tablica_wyników.length && ZapisywaczDoPliku.getStatusPliku() == ZapisywanieTekstuDoPliku.ZAPIS_UDANY; i++)
                {
                    ZapisywaczDoPliku.ZapiszTekst(tablica_wyników[i].toString());
                    
                    
                    System.out.println("Zapisano wyniki o indeksie: " + i);
                                
                    this.PasekIteracji.setValue(this.ObliczProcent(i+1, tablica_wyników.length)); //Liczba plików jest większa od 0.
                }//next i
                
                if(czy_motyw_jest_prawidłowy)
                    {
                        ZapisywaczDoPliku.ZapiszTekst(tekst_z_wynikami + "\n\n");
                    }//end if
                            
            }//end if
            
            if(ZapisywaczDoPliku.getStatusPliku() == ZapisywanieTekstuDoPliku.ZAPIS_NIEUDANY)
            {
                System.out.println("Zapis nieudany!!!!! BŁĄD!");
                JOptionPane.showMessageDialog(EtykietaPodPaskiemPostępu.getParent(), "Błąd zapisu danych do pliku!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            
            ZapisywaczDoPliku.ZamknijPlik();
            
            this.EtykietaPodPaskiemPostępu.setText("Zakończono proces zapisu");
            
            JOptionPane.showMessageDialog(PasekIteracji.getParent(), "Zakończono zapis do pliku.", "Komunikat", JOptionPane.INFORMATION_MESSAGE);
            
            this.EtykietaPodPaskiemPostępu.setText(this.TekstPierwotny);
        
            StartButton.setEnabled(true);
            
        }//end if TablicaPlików != null
        
        
        StanWątku = AnalizaPorównawcza.PO_OBLICZENIACH;
        
    }//Koniec metody run
    
    /**
     * Metoda obliczająca procent całości.
     * @param i Liczba czegoś
     * @param ile 100 procent wszystkiego
     * @return Procent całkowity.
     */
    private int ObliczProcent(int i, int ile)
    {
        int wynik;
        
        wynik = (int)(100 * ((double)i/(double)ile));
        
        return wynik;
    }//Koniec obliczania procentów
    
    
    private void WyprowadźProcentPrzetworzonychPlików(int i, int ile)
    {
        int procent;
        
        procent = ObliczProcent(i, ile);
        
        PasekPostępuAnalizy.setValue(procent);
        
    }//Koniec metody wyprowadzającej procenty
    
        
    
}//Koniec klasy
