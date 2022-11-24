
package Motywy;

/**
 * Klasa służąca do obsługi wyjątków związanej z zamiany zapisu motywu w jednej linii na reprezentację obiektową.
 * @author Piotr Wrzeciono
 * @since 2022.06.01
 */
public class WyjątekDlaWierszaZMotywem extends Exception
{
    /**Brak błędu w zapisie motywu.*/
    public static final int BRAK_BLEDU = 0;
    /**Nie wpisano motywu.*/
    public static final int PUSTA_LINIA = 1;
    /**Błąd składni w zapisie taktu*/
    public static final int BLAD_W_TAKCIE = 2;
    /**Motyw zawiera tylko jedno zdarzenie Hummdrum*/
    public static final int MOTYW_ZA_KROTKI = 3;
    /**Jakiś nieznany błąd*/
    public static final int JAKIS_BLAD = 100;
    
    /**Przechowywany kod błędu*/
    private int KodBłędu;
    
    public WyjątekDlaWierszaZMotywem(int kod)
    {
        super();
        KodBłędu = kod;
        
        if(kod < WyjątekDlaWierszaZMotywem.BRAK_BLEDU || kod > WyjątekDlaWierszaZMotywem.MOTYW_ZA_KROTKI)
        {
            KodBłędu = WyjątekDlaWierszaZMotywem.JAKIS_BLAD;
        }
    }//Koniec konstruktora
    
    /**
     * Metoda zwracająca kod błędu.
     * @return Kod błedu.
     */
    public int getKodBłędu()
    {
        return KodBłędu;
    }
    
    /**
     * Metoda zwracająca opis błędu w zależności od jego rodzaju.
     * @return Opis błędu.
     */
    @Override
    public String toString()
    {
        String opis;
        
        opis = "Brak błędu";
        
        if(KodBłędu == WyjątekDlaWierszaZMotywem.PUSTA_LINIA) opis = "Wprowadzono pustą linię nie zawierającą motywu!";
        if(KodBłędu == WyjątekDlaWierszaZMotywem.BLAD_W_TAKCIE) opis = "W jakimś takcie jest błąd składniowy!";
        if(KodBłędu == WyjątekDlaWierszaZMotywem.MOTYW_ZA_KROTKI) opis = "Motyw składa się tylko z jednego taktu!";
        if(KodBłędu == WyjątekDlaWierszaZMotywem.JAKIS_BLAD) opis = "Jakiś nieznany błąd!";
        
        return opis;
    }
    
}//Koniec klasy
