
package porownania;

import Struktury.Arkusz;
import Struktury.BłędyZWalidacji;
import Struktury.Interpreter;
import Struktury.PlikCSV;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import javax.swing.JProgressBar;

/**
 * Klasa służąca do przeprowadzania walidacji plików CSV. Klasa działa w swoim watku!
 * @author Piotr Wrzeciono
 */
public class WalidacjaPlikówCSV implements Runnable
{
    /**Tablica z nazwami plików*/
    private final String[] TablicaNazwPlików;
    /**Nazwa pliku wyjściowego - z informacją o walidacji*/
    private final String NazwaPlikuZWynikami;
    /**Tablica z informacją o wynikacj walidacji*/
    private final String[] WynikiWalidacji;
    /**Statusy plików CSV*/
    private final String[] Statusy;
    /**Kod znaków w plikach CSV*/
    private final int Kod;
    /**Pasek postępu*/
    private final JProgressBar PasekPostępu;
    /**Status zapisu do pliku wyjściowego*/
    private volatile int StatusZapisu;
    /**Status przeprowadzania walidacji - dla wątku*/
    private volatile int StatusWątku;
    /**Tablica z nazwami zwalidowanych plików*/
    private PlikZwalidowany[] TablicaWynikówWalidacji;
    /**Informacja, czy zapisywać walidację do pliku*/
    private final boolean CzyZapisywaćWalidację;
    
    /**Znak końca linii w pliku tekstowyn*/
    String kn;
    
    /**Plik z wynikami walidacji - przed zapisem*/
    public static final int PRZED_ZAPISEM = 0;
    /**Plik z wynikami walidacji udało się otworzyć*/
    public static final int PLIK_OTWARTO = 1;
    /**Pliku z walidacją nie udało się otworzyć*/
    public static final int PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ = 2;
    /**Zapis wyników walidacji się powiódł*/
    public static final int ZAPIS_UDANY = 3;
    /**Błąd zapisu podczas walidacji*/
    public static final int BŁĄD_ZAPISU = 4;
    /**Nie udało się zamknąć pliku wynikowego walidacji CSV*/
    public static final int NIE_UDAŁO_SIĘ_ZAMKNĄĆ_PLIKU = 5;
    /**Tablica z nazwami plików jest pusta!*/
    public static final int PUSTA_TABLICA = 6;
    
    /**Stała informująca o zapisywaniu walidacji do pliku*/
    public static final boolean ZAPISYWANIE = true;
    /**Stała informująca o tym, że rezultat walidacji nie ma być zapisywany do pliku*/
    public static final boolean BRAK_ZAPISYWANIA = false;
    
    
    /**Przed rozpoczęciem walidacji*/
    public static int PRZED = 0;
    /**W trakcie walidackji*/
    public static int W_TRAKCIE = 1;
    /**Walidacja zakończona*/
    public static int PO = 2;
    /**Po przejściu głównej walidacji*/
    public static int PO_MAIN = 3;
    
    /**
     * Konstruktor klasy służącej do walidacji plików CSV z notacją zdarzeniową.
     * @param tab_nazw Tablica nazw plików
     * @param pasek Pasek postępu
     * @param nazwa_out Nazwa pliku wyjściowego (tekstowego)
     * @param kod_znaków_csv Kod znaków CSV według klasy PlikCSV.
     * @param zapisywać Informacja o tym, czy dane mają być zapisywane <b>ZAPISYWANIE</b>, czy też nie <b>BRAK_ZAPISYWANIA</b>.
     */
    public WalidacjaPlikówCSV(String[] tab_nazw, JProgressBar pasek, String nazwa_out, int kod_znaków_csv, boolean zapisywać)
    {
        TablicaNazwPlików = tab_nazw;
        PasekPostępu = pasek;
        NazwaPlikuZWynikami = nazwa_out;
        Kod = kod_znaków_csv;
        
        WynikiWalidacji = new String[tab_nazw.length];
        Statusy = new String[tab_nazw.length];
        StatusZapisu = WalidacjaPlikówCSV.PRZED_ZAPISEM;
        
        TablicaWynikówWalidacji = new PlikZwalidowany[tab_nazw.length];
        
        if(this.CzyTablicaPlikówJestPusta()) StatusZapisu = WalidacjaPlikówCSV.PUSTA_TABLICA;
        
        PasekPostępu.setMinimum(0); //Przygotowanie paska postępu
        PasekPostępu.setMaximum(100);
        PasekPostępu.setStringPainted(true);
        
        CzyZapisywaćWalidację = zapisywać;
        
        StatusWątku = WalidacjaPlikówCSV.PRZED;
        
        
        kn = "\r\n";
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca stan zapisu plików.
     * @return Stan zapisu według stałych zdefiniowanych w klasie WalidacjaPlikówCSV.
     */
    public int getStatusZapisu()
    {
        return StatusZapisu;
    }//Koniec zwracania stanu zapisu
    
    /**
     * Metoda zwracająca status wątku analitycznego
     * @return Status wątku.
     */
    public int getStatusWątku()
    {
        return this.StatusWątku;
    }//Koniec metody zwracającej status wątku
    
    /**
     * Metoda zwracająca opis status walidacji.
     * @return Opis rezultatu walidacji
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "Nie rozpoczęto jeszcze walidacji.";
        
        if(StatusZapisu == WalidacjaPlikówCSV.PLIK_OTWARTO) opis = "Plik wynikowy udało się otworzyć!";
        if(StatusZapisu == WalidacjaPlikówCSV.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ) opis = "Pliku wynikowego nie udało się otworzyć!";
        if(StatusZapisu == WalidacjaPlikówCSV.ZAPIS_UDANY) opis = "Zapis zakończony powodzeniem!";
        if(StatusZapisu == WalidacjaPlikówCSV.BŁĄD_ZAPISU) opis = "Błąd zapisu w pliku wynikowym!";
        if(StatusZapisu == WalidacjaPlikówCSV.NIE_UDAŁO_SIĘ_ZAMKNĄĆ_PLIKU) opis = "Nie udało się zamknąć pliku wynikowego!";
        if(StatusZapisu == WalidacjaPlikówCSV.PUSTA_TABLICA) opis = "Tablica z nazwami plików jest pusta!";
        
        return opis;
    }//Koniec metody zwracającej opis statusu.
    
    /**
     * Metoda sprawdzająca, czy tablica plików jest pusta.
     * @return <b>true</b>, gdy tablica plików CSV nie zawiera nic, <b>false</b> w przeciwnym przypadku.
     */
    private boolean CzyTablicaPlikówJestPusta()
    {
        boolean wynik;
        int i;
        
        wynik = false;
        
        if(TablicaNazwPlików == null)
        {
            wynik = true;
        }else
        {
            if(TablicaNazwPlików.length == 0)
            {
                wynik = true;
            }else
            {
                for(i = 0; i < TablicaNazwPlików.length; i++)
                {
                    if(TablicaNazwPlików[i].length() == 0) wynik = true; //Wystarczy jedna pusta nazwa!
                }//next i
                
            }//end if
            
        }//end if TablicaPlików == null
        
        return wynik;
    }//Koniec metody sprawdzającej, czy tablica plików jest pusta
    
    /**
     * Metoda licząca procent z całości.
     * @param x Liczba - mniejsza lub równa n
     * @param n Maksymalna liczba
     * @return  Procent
     */
    private int getProcent(int x, int n)
    {
        int wynik;
        double pośredni;
        
        pośredni = 100 * ((double)x/(double)n);
        
        wynik = (int)pośredni;
        
        return wynik;
    }//Koniec metody liczącej procent (do paska postępu
    
    /**
     * Metoda realizująca walidację jako osobny wątek.
     */
    @Override
    public void run()
    {
        int i;
        PlikCSV plik_walidowany;
        Arkusz arkusz_walidowany;
        Interpreter walidator;
        LinkedList<BłędyZWalidacji> lista_błędów;
        boolean czy_poprawny;
        int procent;
        
        
        if(this.StatusZapisu != WalidacjaPlikówCSV.PUSTA_TABLICA) //W innym przypadku nie warto cokolwiek robić!
        {
            
            this.StatusWątku = WalidacjaPlikówCSV.W_TRAKCIE;
            
            for(i = 0; i < TablicaNazwPlików.length; i++)
            {
                plik_walidowany = new PlikCSV(TablicaNazwPlików[i], this.Kod);
                
                arkusz_walidowany = plik_walidowany.WczytajDane();
                
                if(arkusz_walidowany == null)
                {
                    System.out.println("Nie udało się utworzyć arkusza:" + TablicaNazwPlików[i]);
                }
                
                
                if(plik_walidowany.getStanPliku() != PlikCSV.PLIK_WCZYTANO) //Pliku nie udało się poprawnie wczytać;
                {
                    Statusy[i] = plik_walidowany.getOpisStanuPliku();
                    
                    czy_poprawny = false;
                }else
                {
                    Statusy[i] = "";
                    walidator = new Interpreter(arkusz_walidowany);
                    
                    lista_błędów = walidator.getListęBłędów();
                    
                    WynikiWalidacji[i] = GenerujOpisWalidacji(lista_błędów);
                    
                    czy_poprawny = lista_błędów.isEmpty();
                    
                }//end if
                
                procent = this.getProcent(i, TablicaNazwPlików.length);
                
                    
                PasekPostępu.setValue(procent);
                
                this.TablicaWynikówWalidacji[i] = new PlikZwalidowany(TablicaNazwPlików[i], czy_poprawny);
                
            }//next i
            
            this.StatusWątku = WalidacjaPlikówCSV.PO_MAIN;
            
            if(this.CzyZapisywaćWalidację)
            {
                this.ZapiszWyniki();
            }else
            {
                StatusWątku = WalidacjaPlikówCSV.PO;
            }//end if
            
            PasekPostępu.setValue(100);
            
        }//end if
        
    }//Koniec metody run
    
    /**
     * Metoda służąca do generacji opisu wyniku walidacji
     * @return Opis tekstowy walidacji
     */
    private synchronized String GenerujOpisWalidacji(LinkedList<BłędyZWalidacji> błędy)
    {
        String opis;
        int i;
  
        int N;
        
        N = błędy.size();
        
        opis = "BRAK UWAG" + kn;
        
        if(N > 0)
        {
            opis = "ZNALEZIONE BŁĘDY:" + kn;
            
            for(i = 0; i < N; i++)
            {
                opis += błędy.get(i);
                
                if(i < N - 1) opis += kn;
            }//next i
            
        }//end if
        
        return opis;
    }//Koniec generowania opisu walidacji
    
    
    /**
     * Metoda generująca komunikat o rezultacie walidacji do pliku wynikowego
     * @param i Indeks pliku
     * @return Opis rezultatu walidacji
     */
    private synchronized String UtwórzKomunikat(int i)
    {
        String wynik;
        
        wynik = "Plik: " + TablicaNazwPlików[i] + kn;
        
        wynik += this.Statusy[i] + kn;
        
        wynik += this.WynikiWalidacji[i] + kn;
        
        wynik += "-------------------------------" + kn + kn;
        
        return wynik;
    }//Koniec metody tworzącej komunikat o walidacji dla pojedynczego pliku
    
    /**
     * Metoda zapisująca wyniki walidacji plików.
     */
    private synchronized void ZapiszWyniki()
    {
        File plik;
        FileOutputStream strumień_out;
        OutputStreamWriter zapisywacz;
        String pomoc;
        int i;
        
        plik = new File(this.NazwaPlikuZWynikami);
        strumień_out = null;
        
        try {
            strumień_out = new FileOutputStream(plik);
            
            this.StatusZapisu = WalidacjaPlikówCSV.PLIK_OTWARTO;
            
        } catch (Exception ex) {
            
            this.StatusZapisu = WalidacjaPlikówCSV.PLIKU_NIE_UDAŁO_SIĘ_OTWORZYĆ;
            
        }//end of try-catch
        
        
        if(StatusZapisu == WalidacjaPlikówCSV.PLIK_OTWARTO)
        {
            zapisywacz = new OutputStreamWriter(strumień_out);
            
            try{
                
                for(i = 0; i < TablicaNazwPlików.length; i++)
                {
                    pomoc = UtwórzKomunikat(i);
                    
                    zapisywacz.write(pomoc);
                }//next i
                
                StatusZapisu = WalidacjaPlikówCSV.ZAPIS_UDANY;
            }catch(Exception ex)
            {
                StatusZapisu = WalidacjaPlikówCSV.BŁĄD_ZAPISU;
            }//end of try-catch
            
            if(StatusZapisu == WalidacjaPlikówCSV.ZAPIS_UDANY)
            {
                try{
                    zapisywacz.close();
                }catch(Exception ex)
                {
                    StatusZapisu = WalidacjaPlikówCSV.NIE_UDAŁO_SIĘ_ZAMKNĄĆ_PLIKU;
                }//end try-catch
            }//end if
            
        }//end if
        
    }//Koniec metody zapisującej wyniki
    
    /**
     * Metoda zwracająca tablicę plików CSV z poprawną walidacją.
     * @return Tablica plików poprawnych (bez błędów)
     */
    public PlikZwalidowany[] getTablicęPlikówPoprawnych()
    {
        PlikZwalidowany[] tab;
        LinkedList<PlikZwalidowany> lista;
        int i;
        
        lista = new LinkedList<>();
        tab = null;
        
        if(StatusWątku == WalidacjaPlikówCSV.PO_MAIN || StatusWątku == WalidacjaPlikówCSV.PO)
        {   
            for(i = 0; i < this.TablicaWynikówWalidacji.length; i++)
            {
                if(TablicaWynikówWalidacji[i].CzyPoprawny())
                {
                    lista.add(TablicaWynikówWalidacji[i]);
                }//end if
                
            }//next i
            
            if(lista.size() > 0)
            {
                tab = new PlikZwalidowany[lista.size()];
                
                for(i = 0; i < tab.length; i++)
                {
                    tab[i] = lista.get(i);
                }//next i
                
            }//end if
            
        }//end if
        
        
        return tab;
        
    }//Koniec metody zwracającej tablicę plików poprawnych
    
}//Koniec walidacji
