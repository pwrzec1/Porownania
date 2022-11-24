
package Motywy;

/**
 * Klasa służąca do przechowywania różnicy pomiędzy motywami
 * @author Pior Wrzeciono
 */
public class ParaMotywów implements Comparable<ParaMotywów>
{
    /**Referencja do pierwszego motywu*/
    private final Motyw Motyw1;
    /**Rererencja do drugiego motywu*/
    private final Motyw Motyw2;
    /**Miara różnicy*/
    private double Różnica;
    /**Rodzaj porównywań dla Comparable*/
    private int RodzajPorównywania;
    
    /**Porównywanie według różnic (wag)*/
    public static final int COMPARABLE_WAGA = 0;
    /**Porównywanie według literowych nazw kolumn*/
    public static final int COMPARABLE_COLUMN = 1;
    
    /**
     * Konstruktor pary motywów
     * @param m1 Pierwszy motyw
     * @param m2 Drugi motyw
     * @param krab <b>false</b>, gdy ma być wpisany motyw normalnie, <b>true</b>, gdy ma być odwrócony.
     */
    public ParaMotywów(Motyw m1, Motyw m2, boolean krab)
    {
        Motyw1 = m1;
        Motyw2 = m2;
        
        if(krab == true) //Pierwszy motyw musi iść normalnie!
        {
            m1.Odkrabowanie();
        }//end if
        
        if(m1 == null) System.out.println("m1 jest null!");
        if(m2 == null) System.out.println("m2 jest null!");
        
        Różnica  = Motyw1.OceńPodobieństwo(m2);
        
        RodzajPorównywania = ParaMotywów.COMPARABLE_WAGA;
        
        
    }//Koniec konstruktora
    
    /**
     * Metoda ustawiająca rodzaj porównywania dla metody compareTo.
     * @param rodzaj Rodzaj porównywania: <b>COMPARABLE_WAGA</b> - porównywanie według różnic pomiędzy motywami, <b>COMPARABLE_COLUMN</b> - porównywanie według nazw kolumn w pliku CSV
     */
    public void UstawRodzajPorównywania(int rodzaj)
    {
        this.RodzajPorównywania = ParaMotywów.COMPARABLE_WAGA;
        
        if(rodzaj >= ParaMotywów.COMPARABLE_WAGA && rodzaj <= ParaMotywów.COMPARABLE_COLUMN)
        {
            this.RodzajPorównywania = rodzaj;
        }//end if
        
    }//end of
    
    /**
     * Metoda zwracająca referencję do motywu pierwszego
     * @return Referencja do motywu pierwszego
     */
    public Motyw getMotyw1()
    {
        return Motyw1;
    }
    
    /**
     * Metoda zwracająca referencję do motywu drugiego
     * @return Referencja do motywu drugiego
     */
    public Motyw getMotyw2()
    {
        return Motyw2;
    }
    
    /**
     * Metoda zwracająca miarę podobieństwa pomiędzy motywami (różnicę)
     * @return Różnica pomiędzy motywami. Im większa wartość, tym większa odległość.
     */
    public double getRóżnicę()
    {
        boolean czy_można1;
        boolean czy_można2;
        
        czy_można1 = Motyw1.CzyMożnaPorównywać();
        czy_można2 = Motyw2.CzyMożnaPorównywać();
        
        if(czy_można1 == false || czy_można2 == false) Różnica = 9000000; //Różnica jest bardzo wielka!!!
        
        return Różnica;
    }

    /**
     * Metoda zwracająca reuzltat porównania dwóch par motywów
     * @param o Inna para motywów
     * @return <b>1</b>, gdy this jest większe od innego, <b>0</b>, gdy jest równe, <b>-1</b>, gdy this jest mniejsze od innego.
     */
    @Override
    public int compareTo(ParaMotywów o) {
        
        double różnica;
        int wynik;
        
        wynik = 0;
        
        if(RodzajPorównywania == ParaMotywów.COMPARABLE_WAGA)
        {
            różnica = Różnica - o.getRóżnicę();
        
            wynik = (int)Math.signum(różnica);
        }//end if
        
        if(RodzajPorównywania == ParaMotywów.COMPARABLE_COLUMN) //W tym przypadku porównujemy tylko nazwę kolumny motywu pierwszego!
        {
            wynik = this.Motyw1.getReferencjęDoMotywu()[0].getNazwęKolumny().compareTo(o.getMotyw1().getReferencjęDoMotywu()[0].getNazwęKolumny());
        }//end if
        
        return wynik;
    }//Koniec metodu compareTo
    
    /**
     * Metoda zwracająca rodzaj porównywania.
     * @return Rodzaj porównywania ze sobą par motywów muzycznych
     */
    public int getRodzajPorównywania()
    {
        return this.RodzajPorównywania;
    }//Koniec metody zwracającej rodzaj porównywania
    
    /**
     * Metoda zwracająca opis pary motywów.
     * @return Opis
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "Motyw 1: " + Motyw1.getLokalizacjęPoczątku() + "; Motyw 2: " + Motyw2.getLokalizacjęPoczątku() + "; różnica: " + Różnica;
        
        return opis;
    }//Koniec metody zwracającej opis par motywów
    
    
}//Koniec klasy
