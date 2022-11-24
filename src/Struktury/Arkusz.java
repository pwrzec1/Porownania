
package Struktury;

/**
 * Klasa reprezentująca arkusz kalkulacyjny
 * @author Piotr Wrzeciono
 */
public class Arkusz
{
    /**Komórki arkusza kalkulacyjnego*/
    private final KomórkaArkusza[][] Komórki;
    /**Liczba wierszy w arkuszu*/
    private final int LiczbaWierszy;
    /**Liczba kolumn w arkuszu*/
    private final int LiczbaKolumn;
    
    /**
     * Konstruktor klasy Arkusz kalkujacyjny
     * @param max_y Liczba wierszy
     * @param max_x Liczba kolumn
     */
    public Arkusz(int max_y, int max_x)
    {
        LiczbaWierszy = max_y;
        LiczbaKolumn = max_x;
        
        Komórki = new KomórkaArkusza[max_y][max_x];
        
        InicjalizacjaKomórek();
    }//Koniec konstruktora
    
    /**
     * Metoda służąca do inicjalizacji komórek arkusza kalkulacyjnego.
     */
    private void InicjalizacjaKomórek()
    {
        int i;
        int j;
        
        for(i = 0; i < LiczbaWierszy; i++)
        {
            for(j = 0; j < LiczbaKolumn; j++)
            {
                Komórki[i][j] = new KomórkaArkusza(i+1, j+1, "");
            }
        }//next i
    }//Koniec inicjalizacji
    
    
    /**
     * Metoda wpisująca nową wartość do komórki arkusza kalkulacyjnego.
     * @param y Numer wiersza (liczony od 1)
     * @param x Numer kolumny (liczony od 1)
     * @param wartość Zawartość komórki do wpisania;
     */
    public void setKomórkę(int y, int x, String wartość)
    {
        int poz_y;
        int poz_x;
        
        poz_y = y - 1;
        poz_x = x - 1;
        
        Komórki[poz_y][poz_x].setWartość(wartość);
        
    }//Koniec metody wpisującej nową wartość do komórki
    
    
    /**
     * Metoda zwracająca zawartość tekstową komórki w arkuszu.
     * @param y Numer wiersza (liczony od 1)
     * @param x Numer kolumny (liczony od 1)
     * @return Zawartość tekstowa komórki
     */
    public String getZawartość(int y, int x)
    {
        int poz_y;
        int poz_x;
        
        poz_y = y - 1;
        poz_x = x - 1;
        
        return Komórki[poz_y][poz_x].getKomórkę();
        
    }//Koniec metody zwracającej zawartość tekstową komórki
    
    /**
     * Metoda zwracająca wartość liczbową komórki (o ile takową posiada).
     * @param y Numer wiersza (liczony od 1)
     * @param x Numer kolumny (liczony od 1)
     * @return Wartość liczbowa (gdy jest) lub <b>Double.NaN</b>, gdy w komórce nie ma wartości liczbowej.
     */
    public double getWartość(int y, int x)
    {
        int poz_y;
        int poz_x;
        
        poz_y = y - 1;
        poz_x = x - 1;
        
        return Komórki[poz_y][poz_x].getWartość();
                
    }//Koniec metody zwracającej zawartość
    
    /**
     * Metoda kodująca numer kolumny jako litery (jak w arkuszu kalkulacyjnym).
     * @param numer Numer kolumny
     * @return Kod literowy kolumny
     */
    public String getNazwęKolumny(int numer)
    {
        int numeryczny;
        String litera;
        int numer2;
        
        numeryczny = (numer - 1) % 26;
        litera = "" + ((char)(65 + (char)numeryczny));
        numer2 = (int)((numer - 1) / 26);
        
        if (numer2 > 0)
        {
            return getNazwęKolumny(numer2) + litera;
        }else 
        {
            return litera;
        }
    }//Koniec metody zwracającej oznaczenie literowe kolumny
    
    /**
     * Metoda zwracająca liczbę wierszy w arkuszu.
     * @return Liczba wierszy
     */
    public int getLiczbęWierszy()
    {
        return Komórki.length;
    }//Koniec metody zwracającej liczbę wierszy
    
    
    /**
     * Metoda zwracająca liczbę kolumn w arkuszu.
     * @return Liczba kolumn
     */
    public int getLiczbęKolumn()
    {
        return Komórki[0].length;
    }//Koniec metody zwracającej liczbę kolumn
    
    
    /**
     * Metoda służąca do pokazania zawartości arkusza w konsoli.
     */
    public void PokażArkusz()
    {
        int i;
        int j;
        
        int kolumna;
        int wiersz;
        
        System.out.print("\t");
        
        for(i = 0; i < LiczbaKolumn; i ++)
        {
            kolumna = i + 1;
            
            System.out.print(getNazwęKolumny(kolumna));
            
            if(i < LiczbaKolumn - 1)
            {
                System.out.print("\t");
            }else
            {
                System.out.println();
            }
            
        }//next i
        
        
        for(i = 0; i < LiczbaWierszy; i++)
        {
            wiersz = i + 1;
            
            System.out.print(wiersz + "\t");
            
            for(j = 0; j < LiczbaKolumn; j++)
            {
                System.out.print(Komórki[i][j].getKomórkę());
                
                if( j < LiczbaKolumn - 1)
                {
                    System.out.print("\t");
                }else
                {
                    System.out.println();
                }//end if
                
            }//next j
            
        }//next i
        
    }//Koniec metody pokazującej arkusz w konsoli
    
}//Koniec klasy
