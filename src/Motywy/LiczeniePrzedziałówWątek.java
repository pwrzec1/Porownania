
package Motywy;

/**
 * Klasa służąca do liczenia przedziałów do tworzenia wątków
 * @author Piotr Wrzeciono
 */
public class LiczeniePrzedziałówWątek
{
    /**Liczba wątków procesora*/
    private final int LiczbaWątkówProcesora;
    
    /**
     * Konstruktor klasy.
     */
    public LiczeniePrzedziałówWątek()
    {
        LiczbaWątkówProcesora = Runtime.getRuntime().availableProcessors();
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca liczbę rdzeni procesora.
     * @return Liczba rdzeni (wątków) procesora.
     */
    public int LiczbaRdzeni()
    {
        return this.LiczbaWątkówProcesora;
    }
    
    /**
     * Metoda zwracająca indeks początkowy do podziału tablicy
     * @param indeks_główny indeks główny (dzielimy wszystko, co jest powyżej)
     * @param numer_rdzenia Numer rdzenia procesora (od 0 do N-1)
     * @param ile_elementów Liczba wszystkich elementów w tablicy
     * @return indeks początkowy dla przeglądania reszty tablicy.
     */
    public int getIndeksPoczątkowy(int indeks_główny, int numer_rdzenia, int ile_elementów)
    {
        int liczba_elementów;
        int kwant;
        int wynik;
        int przesunięcie;
        int iloczyn;
        
        wynik = indeks_główny + 1;
        
        if(LiczbaWątkówProcesora > 1)
        {
            liczba_elementów = ile_elementów - indeks_główny - 1;
            kwant = (int)liczba_elementów/LiczbaWątkówProcesora;
            
            przesunięcie = indeks_główny + 1;
            
            iloczyn = numer_rdzenia * kwant;
            
            wynik = przesunięcie + iloczyn;
        }//end if
        
        return wynik;
    }//Koniec metody zwracającej indeks początkowy
    
    /**
     * Metoda wyliczająca indeks końcowy.
     * @param indeks_główny Indeks główny (dzielimy wszystko, co jest powyżej).
     * @param numer_rdzenia Numer rdzenia procesora (od 0 do N-1)
     * @param ile_elementów Liczba wszystkich elementów w tablicy
     * @return Indeks końcowy do przeglądania reszty tablicy
     */
    public int getIndeksKońcowy(int indeks_główny, int numer_rdzenia, int ile_elementów)
    {
        int indeks_początkowy;
        int kwant;
        int wynik;
        int liczba_elementów;
        
        wynik = ile_elementów - 1;
        
        if(LiczbaWątkówProcesora > 1)
        {
            if(numer_rdzenia == LiczbaWątkówProcesora - 1)
            {
                wynik = ile_elementów - 1;
            }//end if
            
            if(numer_rdzenia < LiczbaWątkówProcesora - 1)
            {
                wynik = getIndeksPoczątkowy(indeks_główny, numer_rdzenia + 1, ile_elementów) - 1;
            }
            
        }//end if
        
        
        return wynik;
    }//Koniec liczenia indeksu końcowego
    
}//Koniec klasy
