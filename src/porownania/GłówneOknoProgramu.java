
package porownania;

import Struktury.PlikCSV;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Klasa głównego okna programu.
 * @author Piotr Wrzeciono
 */
public class GłówneOknoProgramu extends javax.swing.JFrame
{
    
    /**Tablica plików SCV*/
    private String[] TablicaPlikówCSV;
    /**Nazwa pliku wynikowego dla walidacji*/
    private String NazwaPlikuWynikowegoDlaWalidacji;
    /**Nazwa bieżącego katalogu CSV*/
    private String NazwaBieżącegoKataloguCSV;
    /**Nazwa bieżącego katalogu do zapisu walidacji*/
    private String NazwaBieżącegoKataloguWalidacji;
    /**Nazwa pliku wynikowego z porównań*/
    private String NazwaPlikuWynikowego;
    /**Katalog z plikiem wynikowym*/
    private String NazwaBieżącegoKataloguWynikowego;
    
    /**Okno wyboru katalogu z plikami CSV*/
    private JFileChooser WybórKatalogówZPlikamiCSV;
    /**Okno wyboru pojedynczego pliku CSV*/
    private JFileChooser WybórPlikuCSV;
    /**Okno wyboru pliku do zapisu walidacji*/
    private JFileChooser WybórPlikuWynikowegoWalidacja;
    /**Okno wyboru pliku do zapisu wyników analizy*/
    private JFileChooser WybórPlikuWynikowegoAnalizy;
    
    /**Filtr katalogów CSV*/
    private FiltrKatalogów FiltrK;
    /**Filtr plików CSV*/
    private FiltrPlików FiltrCSV;
    /**Filtr plików txt - wynikowych*/
    private FiltrPlików FiltrTXT;
    /**Filtr plików wynikowych CSV*/
    private FiltrPlików FiltrWynik;

    /**
     * KOnstruktor głównego okna programu.
     */
    public GłówneOknoProgramu()
    {
        initComponents();
        
        InicjalizacjaOkienPlików();
        
        InicjalizacjaWstępnaMenu();
        
        this.WybórKodowaniaList.setSelectedIndex(0);
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca kodowanie znaków dla plików CSV
     * @return Stałe według klasy PlikCSV.
     * @see PlikCSV
     */
    private int getKodowanieZnakówZMenu()
    {
        int wynik;
        int indeks;
        
        wynik = 0;
        
        indeks = WybórKodowaniaList.getSelectedIndex();
        
        if(indeks == 0) wynik = PlikCSV.UTF8;
        if(indeks == 1) wynik = PlikCSV.CP1250;
        
        return wynik;
        
    }//Koniec metody zwracającej kodowanie znaków plików CSV
    
    
    /**
     * Metoda dokonująca inicjalizacji wstępnej menu okna.
     */
    private void InicjalizacjaWstępnaMenu()
    {
        this.WalidacjaMenu.setEnabled(false);
        
        SterowanieWalidacją();
        
    }//Koniec inicjalizacji wstępnej menu
    
    /**
     * Inicjalizacja okien do wybory plików.
     */
    private void InicjalizacjaOkienPlików()
    {
        this.NazwaBieżącegoKataloguCSV = ".";
        this.NazwaBieżącegoKataloguWalidacji = ".";
        this.NazwaPlikuWynikowegoDlaWalidacji = "";
        this.NazwaBieżącegoKataloguWynikowego = ".";
        this.NazwaPlikuWynikowego = "";
        
        
        
        FiltrK = new FiltrKatalogów();
        FiltrCSV = new FiltrPlików("csv", "Pliki csv z zapisem zdarzeniowym (*.csv)");
        FiltrTXT = new FiltrPlików("txt", "Plik tesktowy do zapisu wyników walidacji (*.txt)");
        FiltrWynik = new FiltrPlików("csv","Plik csv z zapisem wyników porównań (*.csv)");
        
        this.WybórKatalogówZPlikamiCSV = new JFileChooser();
        this.WybórKatalogówZPlikamiCSV.setFileFilter(FiltrK);
        this.WybórKatalogówZPlikamiCSV.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.WybórKatalogówZPlikamiCSV.setDialogTitle("Wybór katalogu z plikami CSV");
        this.WybórKatalogówZPlikamiCSV.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguCSV));
        
        this.WybórPlikuCSV = new JFileChooser();
        this.WybórPlikuCSV.setFileFilter(FiltrCSV);
        this.WybórPlikuCSV.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.WybórKatalogówZPlikamiCSV.setDialogTitle("Wybór pojedynczego pliku CSV");
        this.WybórKatalogówZPlikamiCSV.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguCSV));
        
        this.WybórPlikuWynikowegoWalidacja = new JFileChooser();
        this.WybórPlikuWynikowegoWalidacja.setFileFilter(FiltrTXT);
        this.WybórPlikuWynikowegoWalidacja.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.WybórPlikuWynikowegoWalidacja.setDialogTitle("Wybór pliku do zapisu wyników walidacji");
        this.WybórPlikuWynikowegoWalidacja.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguWalidacji));
        
        
        this.WybórPlikuWynikowegoAnalizy = new JFileChooser();
        this.WybórPlikuWynikowegoAnalizy.setFileFilter(FiltrWynik);
        this.WybórPlikuWynikowegoAnalizy.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.WybórPlikuWynikowegoAnalizy.setDialogTitle("Wybór pliku do zapisu wyników analizy");
        this.WybórPlikuWynikowegoAnalizy.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguWynikowego));
        
        this.KrabCheckBox.setVisible(false);
        this.TylkoJednąDługośćCheckBox.setVisible(true);
        
    }//Koniec inicjalizacji
    
    /**
     * Metoda wczytujaca listę plików z katalogu CSV.
     */
    private void WczytajKatalogCSV()
    {
        int rezultat;
        ListaPlików pomocnicza;
        
        WybórKatalogówZPlikamiCSV.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguCSV));
        
        rezultat = WybórKatalogówZPlikamiCSV.showOpenDialog(this);
        
        if(rezultat == JFileChooser.APPROVE_OPTION)
        {
            TablicaPlikówCSV = null;
            NazwaBieżącegoKataloguCSV = WybórKatalogówZPlikamiCSV.getSelectedFile().getAbsolutePath();
            
            pomocnicza = new ListaPlików(NazwaBieżącegoKataloguCSV);
            
            TablicaPlikówCSV = pomocnicza.PodajTablicęNazw();
            
            //PokażTablicęPlików();
            
        }//end if
        
        SterowanieWalidacją();
        
    }//Koniec wczytywania tablicy plików CSV
    
    /**
     * Metoda wczytująca nazwę pojedynczego pliku CSV do programu.
     */
    private void WczytajPojedynczyPlikCSV()
    {
        int rezultat;
        
        this.WybórPlikuCSV.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguCSV));
        
        rezultat = WybórPlikuCSV.showOpenDialog(this);
        
        if(rezultat == JFileChooser.APPROVE_OPTION)
        {
            this.TablicaPlikówCSV = null;
            
            TablicaPlikówCSV = new String[1];
            
            TablicaPlikówCSV[0] = WybórPlikuCSV.getSelectedFile().getAbsolutePath();
            
            NazwaBieżącegoKataloguCSV = WybórPlikuCSV.getCurrentDirectory().getAbsolutePath();
            
            //this.PokażTablicęPlików();
            
        }//end if
        
        SterowanieWalidacją();
        
    }//Koniec wczytywania pojedynczego pliku CSV
    
    /**
     * Metoda wczytująca nazwę pliku do zapisu walidacji.
     */
    private void WczytajPlikDoZapisuWalidacji()
    {
        int rezultat;
        
        this.WybórPlikuWynikowegoWalidacja.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguWalidacji));
        
        rezultat = WybórPlikuWynikowegoWalidacja.showSaveDialog(this);
        
        if(rezultat == JFileChooser.APPROVE_OPTION)
        {
            this.NazwaPlikuWynikowegoDlaWalidacji = WybórPlikuWynikowegoWalidacja.getSelectedFile().getAbsolutePath();
            
            this.NazwaBieżącegoKataloguWalidacji = WybórPlikuWynikowegoWalidacja.getCurrentDirectory().getAbsolutePath();
            
            //this.PokażTablicęPlików();
        }//end if
        
        SterowanieWalidacją();
        
    }//Koniec wczytywania nazwy pliku do zapisu walidacji
    
    
    /**
     * Metoda wczytująca nazwę pliku wynikowego do analizy.
     */
    private void PodajPlikWynikowy()
    {
        int rezultat;
        
        this.WybórPlikuWynikowegoAnalizy.setCurrentDirectory(new File(this.NazwaBieżącegoKataloguWynikowego));
        
        rezultat = WybórPlikuWynikowegoAnalizy.showSaveDialog(this);
        
        if(rezultat == JFileChooser.APPROVE_OPTION)
        {
            this.NazwaPlikuWynikowego = WybórPlikuWynikowegoAnalizy.getSelectedFile().getAbsolutePath();
            
            this.NazwaBieżącegoKataloguWynikowego = WybórPlikuWynikowegoAnalizy.getCurrentDirectory().getAbsolutePath();
            
        }//end if
        
        SterowanieWalidacją();
        
    }//Koniec wczytywania pojedynczego pliku CSV
    
    /**
     * Metoda służąca do sterowania menu z walidacji.
     */
    private void SterowanieWalidacją()
    {
        this.WalidacjaMenu.setEnabled(false);
        
        if(this.TablicaPlikówCSV != null && this.NazwaPlikuWynikowegoDlaWalidacji.length() > 0)
        {
            WalidacjaMenu.setEnabled(true);
        }//end if
        
        
        if(this.NazwaPlikuWynikowego.compareTo("") == 0 || this.TablicaPlikówCSV == null)
        {
            this.MinimalnaLiczbaTextField.setEnabled(false);
            this.MiaraInterwałowaWagaTextField.setEnabled(false);
            this.MiaraMetrycznaWagaTextField.setEnabled(false);
            this.MaksymalnaRóżnicaTextField.setEnabled(false);
            this.StartObliczeńButton.setEnabled(false);
            
        }else
        {
            
            this.MinimalnaLiczbaTextField.setEnabled(true);
            this.MiaraInterwałowaWagaTextField.setEnabled(true);
            this.MiaraMetrycznaWagaTextField.setEnabled(true);
            this.MaksymalnaRóżnicaTextField.setEnabled(true);
            this.StartObliczeńButton.setEnabled(true);
            
        }//end if
        
    }//Koniec metody sterowania walidacją
    
    /**
     * Metoda uruchamiająca proces walidacji plików.
     */
    private void WalidacjaPlików()
    {
        WalidacjaPlikówCSV walidator;
        Thread wątek_sterujący;
        int kod_znaków;
        
        kod_znaków = getKodowanieZnakówZMenu();
        walidator = new WalidacjaPlikówCSV(TablicaPlikówCSV, this.ProgresOgólnyProgressBar, this.NazwaPlikuWynikowegoDlaWalidacji, kod_znaków, WalidacjaPlikówCSV.ZAPISYWANIE);
        
        if(walidator.getStatusZapisu() == WalidacjaPlikówCSV.PUSTA_TABLICA)
        {
            JOptionPane.showMessageDialog(this, walidator, "Błąd!", JOptionPane.ERROR_MESSAGE);
        }else
        {
            wątek_sterujący = new Thread(walidator);
            
            wątek_sterujący.start();
            
        }//end if
    }//Koniec metody walidującej pliki CSV
    
    
    /**
     * Metoda pomocnicza - testowa, informująca o danych dotyczących plików.
     */
    private void PokażTablicęPlików()
    {
        int i;
        
        if(TablicaPlikówCSV == null)
        {
            System.out.println("Nie wczytano żadnej listy plików!");
        }else
        {
            for(i = 0; i < TablicaPlikówCSV.length; i++)
            {
                System.out.println("Tablica[" + i + "] = " + TablicaPlikówCSV[i]);
            }//next i
            
        }//end if
        
        if(this.NazwaPlikuWynikowegoDlaWalidacji.compareTo("") == 0)
        {
            System.out.println("Nie podano pliku wyjściowego walidacji!");
        }else
        {
            System.out.println("Plik wyjściowy dla walidacji = " + NazwaPlikuWynikowegoDlaWalidacji);
        }//end if
        
        System.out.println("Katalog CSV ........... " + this.NazwaBieżącegoKataloguCSV);
        System.out.println("Katalog walidacji ..... " + this.NazwaBieżącegoKataloguWalidacji);
        
    }//Koniec pokazywania tablicy plików
    
    /**
     * Metoda wywołująca analizę plików.
     */
    private void AnalizaPlików()
    {
        int minimalny_motyw;
        double waga_interwałowa;
        double waga_metryczna;
        double max_różnica;
        int kod_znaków;
        Thread wątek;
        
        int odpowiedź;
        boolean czy_liczyć;
        
        AnalizaPorównawcza analizator;
        
        kod_znaków = getKodowanieZnakówZMenu();
        
        minimalny_motyw = (int)getLiczbę(this.MinimalnaLiczbaTextField);
        waga_interwałowa = getLiczbę(this.MiaraInterwałowaWagaTextField);
        waga_metryczna = getLiczbę(this.MiaraMetrycznaWagaTextField);
        max_różnica = getLiczbę(this.MaksymalnaRóżnicaTextField);
        
        czy_liczyć = true;
        
        if(minimalny_motyw > 0 && waga_interwałowa > 0 && waga_metryczna > 0 && max_różnica > 0)
        {
            analizator = new porownania.AnalizaPorównawcza(TablicaPlikówCSV, NazwaPlikuWynikowego, kod_znaków, minimalny_motyw, waga_interwałowa, waga_metryczna, max_różnica, this.ProgresOgólnyProgressBar, PostępObliczeńProgressBar, NazwaPlikuTextArea, KomunikatyTextField, this.PojedynczeCheckBox, this.IteracjeProgressBar, this.ProcentIteracjaLabel, this.KrabCheckBox, this.StartObliczeńButton, this.TylkoJednąDługośćCheckBox, this.MotywDoSzukaniaTextField, this.CzySzukaćMotywuCheckBox);
            
            if(analizator.getStatusZapisywaczaDoPliku() == ZapisywanieTekstuDoPliku.PLIK_JUŻ_ISTNIEJE)
            {
                odpowiedź = JOptionPane.showConfirmDialog(this, "Plik wynikowy już istnieje. Czy chcesz go nadpisać?", "Uwaga - pytanie!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if(odpowiedź == JOptionPane.NO_OPTION) czy_liczyć = false;
            }//end if
            
            if(czy_liczyć)
            {
                wątek = new Thread(analizator);
            
                wątek.start();
                
            }//end if
            
        }//end if warunku na uruchomienie analizy
        
    }//Koniec analizy plików
    
    /**
     * Metoda zwracająca wartość liczbową z pola tekstowego
     * @param pole Referencja do pola tekstowego
     * @return Zwracana wartość
     */
    private double getLiczbę(JTextField pole)
    {
        double wynik;
        
        wynik = 0;
        
        try{
            
            wynik = Double.parseDouble(pole.getText().trim());
            
        }catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Podana wartość tekstowa nie jest liczbą!\nSprawdź, co zostało wpisane.", "Błąd konwersji", JOptionPane.ERROR_MESSAGE);
            
            wynik = -1;
        }//end try-catch
        
        
        return wynik;
    }//Koniec metody zwracającej wartość double
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GłównyPanelOkna = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        WybórKodowaniaList = new javax.swing.JList<>();
        KodowanieZnkakówLabel = new javax.swing.JLabel();
        ProgresOgólnyProgressBar = new javax.swing.JProgressBar();
        ProcentWaliacjiLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        MinimalnaLiczbaLabel = new javax.swing.JLabel();
        WagaInterwałowaLabel = new javax.swing.JLabel();
        WagaMetrycznaLabel = new javax.swing.JLabel();
        MaksymalnaRóżnicaLabel = new javax.swing.JLabel();
        MinimalnaLiczbaTextField = new javax.swing.JTextField();
        MiaraInterwałowaWagaTextField = new javax.swing.JTextField();
        MiaraMetrycznaWagaTextField = new javax.swing.JTextField();
        MaksymalnaRóżnicaTextField = new javax.swing.JTextField();
        StartObliczeńButton = new javax.swing.JButton();
        PostępObliczeńProgressBar = new javax.swing.JProgressBar();
        ProcentPrzetworzonychPlikówLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        NazwaPlikuTextArea = new javax.swing.JTextArea();
        KomunikatyTextField = new javax.swing.JTextField();
        PojedynczeCheckBox = new javax.swing.JCheckBox();
        IteracjeProgressBar = new javax.swing.JProgressBar();
        ProcentIteracjaLabel = new javax.swing.JLabel();
        KrabCheckBox = new javax.swing.JCheckBox();
        TylkoJednąDługośćCheckBox = new javax.swing.JCheckBox();
        MotywDoWyszukaniaJLabel = new javax.swing.JLabel();
        MotywDoSzukaniaTextField = new javax.swing.JTextField();
        CzySzukaćMotywuCheckBox = new javax.swing.JCheckBox();
        MenuProgramu = new javax.swing.JMenuBar();
        NazwaWynikiAnalizyPlikMenu = new javax.swing.JMenu();
        PojedynczyPllkCSVMenu = new javax.swing.JMenuItem();
        KatalogCSVMenu = new javax.swing.JMenuItem();
        PlikWalidacjaMenu = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        KoniecMenu = new javax.swing.JMenuItem();
        WalidacjaMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Porównywanie motywów muzycznych");

        WybórKodowaniaList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Kodowanie UTF-8", "Kodowanie Windows" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(WybórKodowaniaList);

        KodowanieZnkakówLabel.setText("Wybór kodowania znaków w plikach CSV:");

        ProcentWaliacjiLabel.setText("Procent zwalidowanych plików");

        MinimalnaLiczbaLabel.setText("Minimalna liczba zdarzeń Humdrum w motywie:");

        WagaInterwałowaLabel.setText("Waga dla miary interwałowej:");

        WagaMetrycznaLabel.setText("Waga dla miary metrycznej:");

        MaksymalnaRóżnicaLabel.setText("Maksymalna akceptowalna wartość różnicy:");

        MinimalnaLiczbaTextField.setText("7");
        MinimalnaLiczbaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinimalnaLiczbaTextFieldActionPerformed(evt);
            }
        });

        MiaraInterwałowaWagaTextField.setText("0.2");

        MiaraMetrycznaWagaTextField.setText("0.1");
        MiaraMetrycznaWagaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MiaraMetrycznaWagaTextFieldActionPerformed(evt);
            }
        });

        MaksymalnaRóżnicaTextField.setText("0.6");

        StartObliczeńButton.setText("Start");
        StartObliczeńButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartObliczeńButtonActionPerformed(evt);
            }
        });

        ProcentPrzetworzonychPlikówLabel.setText("Procent przetworzonych plików");

        NazwaPlikuTextArea.setEditable(false);
        NazwaPlikuTextArea.setColumns(20);
        NazwaPlikuTextArea.setLineWrap(true);
        NazwaPlikuTextArea.setRows(5);
        jScrollPane2.setViewportView(NazwaPlikuTextArea);

        KomunikatyTextField.setEditable(false);

        PojedynczeCheckBox.setSelected(true);
        PojedynczeCheckBox.setText("Tylko pojedyncze głosy");

        ProcentIteracjaLabel.setText("Procent zrealizowanych iteracji dla danej liczby zdarzeń Humdrum.");

        KrabCheckBox.setText("Czy szukać kraba?");

        TylkoJednąDługośćCheckBox.setText("Tylko jedną długość");
        TylkoJednąDługośćCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TylkoJednąDługośćCheckBoxActionPerformed(evt);
            }
        });

        MotywDoWyszukaniaJLabel.setText("Motyw do wyszukania:");

        MotywDoSzukaniaTextField.setText("5,48,0,74,2 P 5,50,2,76,2 P 5,52,2,78,2 P 5,53,2,80,3 P 6,55,2,83,0.5 P 6,53,-2,83,0.5 P 6,52,-2,5,2 P 6,57,4,84,2 P 6,50,-5,86,2 P 6,55,4,88,3 P 6,57,2,90,1 P 6,55,-2,93,1 P 6,53,-2,94,1 P 6,52,-2,95,1");
        MotywDoSzukaniaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MotywDoSzukaniaTextFieldActionPerformed(evt);
            }
        });

        CzySzukaćMotywuCheckBox.setText("Szukanie motywu w plikach");

        javax.swing.GroupLayout GłównyPanelOknaLayout = new javax.swing.GroupLayout(GłównyPanelOkna);
        GłównyPanelOkna.setLayout(GłównyPanelOknaLayout);
        GłównyPanelOknaLayout.setHorizontalGroup(
            GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PostępObliczeńProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GłównyPanelOknaLayout.createSequentialGroup()
                        .addGap(0, 315, Short.MAX_VALUE)
                        .addComponent(KodowanieZnkakówLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ProgresOgólnyProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                        .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MinimalnaLiczbaLabel)
                            .addComponent(WagaInterwałowaLabel)
                            .addComponent(WagaMetrycznaLabel)
                            .addComponent(MaksymalnaRóżnicaLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(MinimalnaLiczbaTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(MiaraInterwałowaWagaTextField)
                            .addComponent(MiaraMetrycznaWagaTextField)
                            .addComponent(MaksymalnaRóżnicaTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(StartObliczeńButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PojedynczeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(KrabCheckBox)
                                    .addComponent(TylkoJednąDługośćCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane2)
                    .addComponent(KomunikatyTextField)
                    .addComponent(IteracjeProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                        .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProcentWaliacjiLabel)
                            .addComponent(ProcentPrzetworzonychPlikówLabel)
                            .addComponent(ProcentIteracjaLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                        .addComponent(MotywDoWyszukaniaJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                                .addComponent(CzySzukaćMotywuCheckBox)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(MotywDoSzukaniaTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE))))
                .addContainerGap())
        );
        GłównyPanelOknaLayout.setVerticalGroup(
            GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GłównyPanelOknaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(KodowanieZnkakówLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(ProgresOgólnyProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProcentWaliacjiLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MinimalnaLiczbaLabel)
                    .addComponent(MinimalnaLiczbaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TylkoJednąDługośćCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(WagaInterwałowaLabel)
                    .addComponent(MiaraInterwałowaWagaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PojedynczeCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(WagaMetrycznaLabel)
                    .addComponent(MiaraMetrycznaWagaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(KrabCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MaksymalnaRóżnicaLabel)
                    .addComponent(MaksymalnaRóżnicaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StartObliczeńButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PostępObliczeńProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProcentPrzetworzonychPlikówLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(KomunikatyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IteracjeProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ProcentIteracjaLabel)
                .addGap(18, 18, 18)
                .addGroup(GłównyPanelOknaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MotywDoWyszukaniaJLabel)
                    .addComponent(MotywDoSzukaniaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(CzySzukaćMotywuCheckBox)
                .addContainerGap())
        );

        NazwaWynikiAnalizyPlikMenu.setText("Pliki");

        PojedynczyPllkCSVMenu.setText("Pojedynczy plik CSV");
        PojedynczyPllkCSVMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PojedynczyPllkCSVMenuActionPerformed(evt);
            }
        });
        NazwaWynikiAnalizyPlikMenu.add(PojedynczyPllkCSVMenu);

        KatalogCSVMenu.setText("Cały katalog z plikami CSV");
        KatalogCSVMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KatalogCSVMenuActionPerformed(evt);
            }
        });
        NazwaWynikiAnalizyPlikMenu.add(KatalogCSVMenu);

        PlikWalidacjaMenu.setText("Plik z wynikami walidacji");
        PlikWalidacjaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlikWalidacjaMenuActionPerformed(evt);
            }
        });
        NazwaWynikiAnalizyPlikMenu.add(PlikWalidacjaMenu);

        jMenuItem1.setText("Plik z wynikami analizy");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        NazwaWynikiAnalizyPlikMenu.add(jMenuItem1);

        KoniecMenu.setText("Koniec");
        KoniecMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KoniecMenuActionPerformed(evt);
            }
        });
        NazwaWynikiAnalizyPlikMenu.add(KoniecMenu);

        MenuProgramu.add(NazwaWynikiAnalizyPlikMenu);

        WalidacjaMenu.setText("Walidacja");
        WalidacjaMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                WalidacjaMenuMenuSelected(evt);
            }
        });
        WalidacjaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WalidacjaMenuActionPerformed(evt);
            }
        });
        MenuProgramu.add(WalidacjaMenu);

        setJMenuBar(MenuProgramu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(GłównyPanelOkna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(GłównyPanelOkna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PojedynczyPllkCSVMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PojedynczyPllkCSVMenuActionPerformed
        
        this.WczytajPojedynczyPlikCSV();
        
    }//GEN-LAST:event_PojedynczyPllkCSVMenuActionPerformed

    private void KatalogCSVMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KatalogCSVMenuActionPerformed
        
        
        this.WczytajKatalogCSV();
        
    }//GEN-LAST:event_KatalogCSVMenuActionPerformed

    private void PlikWalidacjaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlikWalidacjaMenuActionPerformed
        
        this.WczytajPlikDoZapisuWalidacji();
        
    }//GEN-LAST:event_PlikWalidacjaMenuActionPerformed

    private void KoniecMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KoniecMenuActionPerformed
        
        this.dispose();
        
    }//GEN-LAST:event_KoniecMenuActionPerformed

    private void WalidacjaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WalidacjaMenuActionPerformed
        
        
    }//GEN-LAST:event_WalidacjaMenuActionPerformed

    private void WalidacjaMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_WalidacjaMenuMenuSelected
        
        
        this.WalidacjaPlików();
        
    }//GEN-LAST:event_WalidacjaMenuMenuSelected

    private void MinimalnaLiczbaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinimalnaLiczbaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MinimalnaLiczbaTextFieldActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        
        this.PodajPlikWynikowy();
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void StartObliczeńButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartObliczeńButtonActionPerformed
        
        this.AnalizaPlików();
        
    }//GEN-LAST:event_StartObliczeńButtonActionPerformed

    private void MiaraMetrycznaWagaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MiaraMetrycznaWagaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MiaraMetrycznaWagaTextFieldActionPerformed

    private void TylkoJednąDługośćCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TylkoJednąDługośćCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TylkoJednąDługośćCheckBoxActionPerformed

    private void MotywDoSzukaniaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MotywDoSzukaniaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MotywDoSzukaniaTextFieldActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CzySzukaćMotywuCheckBox;
    private javax.swing.JPanel GłównyPanelOkna;
    private javax.swing.JProgressBar IteracjeProgressBar;
    private javax.swing.JMenuItem KatalogCSVMenu;
    private javax.swing.JLabel KodowanieZnkakówLabel;
    private javax.swing.JTextField KomunikatyTextField;
    private javax.swing.JMenuItem KoniecMenu;
    private javax.swing.JCheckBox KrabCheckBox;
    private javax.swing.JLabel MaksymalnaRóżnicaLabel;
    private javax.swing.JTextField MaksymalnaRóżnicaTextField;
    private javax.swing.JMenuBar MenuProgramu;
    private javax.swing.JTextField MiaraInterwałowaWagaTextField;
    private javax.swing.JTextField MiaraMetrycznaWagaTextField;
    private javax.swing.JLabel MinimalnaLiczbaLabel;
    private javax.swing.JTextField MinimalnaLiczbaTextField;
    private javax.swing.JTextField MotywDoSzukaniaTextField;
    private javax.swing.JLabel MotywDoWyszukaniaJLabel;
    private javax.swing.JTextArea NazwaPlikuTextArea;
    private javax.swing.JMenu NazwaWynikiAnalizyPlikMenu;
    private javax.swing.JMenuItem PlikWalidacjaMenu;
    private javax.swing.JCheckBox PojedynczeCheckBox;
    private javax.swing.JMenuItem PojedynczyPllkCSVMenu;
    private javax.swing.JProgressBar PostępObliczeńProgressBar;
    private javax.swing.JLabel ProcentIteracjaLabel;
    private javax.swing.JLabel ProcentPrzetworzonychPlikówLabel;
    private javax.swing.JLabel ProcentWaliacjiLabel;
    private javax.swing.JProgressBar ProgresOgólnyProgressBar;
    private javax.swing.JButton StartObliczeńButton;
    private javax.swing.JCheckBox TylkoJednąDługośćCheckBox;
    private javax.swing.JLabel WagaInterwałowaLabel;
    private javax.swing.JLabel WagaMetrycznaLabel;
    private javax.swing.JMenu WalidacjaMenu;
    private javax.swing.JList<String> WybórKodowaniaList;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
