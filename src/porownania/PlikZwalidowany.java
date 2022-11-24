
package porownania;

/**
 * Klasa służąca do przechowywania wyniku walidacji plików
 * @author Piotr Wrzeciono
 */
public class PlikZwalidowany
{
    /**Pełna nazwa pliku*/
    private final String PełnaNazwaPliku;
    /**Stan walidacji: <b>true</b>, gdy plik jest poprawny, w innym przypadku <b>false</b>.*/
    private final boolean CzyJestPoprawny;
    
    public PlikZwalidowany(String nazwa, boolean stan)
    {
        PełnaNazwaPliku = nazwa;
        CzyJestPoprawny = stan;
        
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca pełną nazwę pliku
     * @return Nazwa pliku
     */
    public String getNazwęPliku()
    {
        return PełnaNazwaPliku;
    }
    
    /**
     * Metoda zwracająca stan pliku.
     * @return <b>true</b>, gdy plik jest poprawny, <b>false</b>, gdy plik zawiera błędy.
     */
    public boolean CzyPoprawny()
    {
        return this.CzyJestPoprawny;
    }
    
    /**
     * Metoda zwracająca opis pliku zwalidowanego
     * @return Opis pliku
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = PełnaNazwaPliku + "\t";
        
        if(CzyJestPoprawny)
        {
            opis += "poprawny";
        }else
        {
            opis += "błędny";
        }//end if
        
        
        return opis;
    }
    
}//Koniec klasy
