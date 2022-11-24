
package Struktury;

import java.nio.charset.StandardCharsets;

/**
 * Klasa służąca do rozpoznawania i usuwania BOM (UTF-8) z tekstu.
 * @author Piotr Wrzeciono
 */
public class UsuwanieBOM
{
    private byte[][] BOMy;
    
    /**
     * Konstruktor klasy UsuwanieBOM
     */
    public UsuwanieBOM()
    {
        this.UtwórzBOMy();
    }//Koniec konstruktora
    
    /**
     * Metoda inicjalizująca tablicę BOMów.
     * 
     * Lista BMów ze strony: https://en.wikipedia.org/wiki/Byte_order_mark
     */
    private void UtwórzBOMy()
    {
        BOMy = new byte[11][];
        
        BOMy[0] = new byte[]{(byte)0xef, (byte)0xbb, (byte)0xbf};
        BOMy[1] = new byte[]{(byte)0xfe, (byte)0xff};
        BOMy[2] = new byte[]{(byte)0xff, (byte)0xfe};
        BOMy[3] = new byte[]{(byte)0x00, (byte)0x00, (byte)0xFE, (byte)0xFF};
        BOMy[4] = new byte[]{(byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00};
        BOMy[5] = new byte[]{(byte)0x2B, (byte)0x2F, (byte)0x76};
        BOMy[6] = new byte[]{(byte)0xF7, (byte)0x64, (byte)0x4C};
        BOMy[7] = new byte[]{(byte)0xDD, (byte)0x73, (byte)0x66, (byte)0x73};
        BOMy[8] = new byte[]{(byte)0x0E, (byte)0xFE, (byte)0xFF};
        BOMy[9] = new byte[]{(byte)0xFB, (byte)0xEE, (byte)0x28};
        BOMy[10] = new byte[]{(byte)0x84, (byte)0x31, (byte)0x95, (byte)0x33};
        
    }//Koniec inicjalizacji BOMów
    
    /**
     * Metoda sprawdzająca, czy w łańcuchu bajtowym jest BOM
     * @param bom Tablica z BOMem
     * @param łańcuch_bajtowy Łańcucuch bajtowy do sprawdzenia.
     * @return <b>true</b>, gdy BOM jest na początku łańcucha, <b>false</b> w innym przypadku.
     */
    private boolean CzyJestBOM(byte[] bom, byte[] łańcuch_bajtowy)
    {
        boolean wynik;
        int i;
        int licznik;
        
        wynik = false;
        
        if(bom.length <= łańcuch_bajtowy.length)
        {
            licznik = 0;
            
            for(i = 0; i < bom.length; i++)
            {
                if(bom[i] == łańcuch_bajtowy[i]) licznik++;
            }//next i
            
            if(licznik == bom.length) wynik = true;
            
        }//end if
        
        return wynik;
    }//Koniec sprawdzania, czy jest BOM
    
    /**
     * Metoda służąca do sprawdzenia, czy jest jakikolwiek BOM
     * @param łańcuch_bajtowy łańcuch bajtowy do sprawdzenia.
     * @return <b>indeks w tablicy BOMy</b>, gdy BOM jest na początku łańcucha, <b>-1</b> w innym przypadku.
     */
    private int CzyJestJakikolwiekBOM(byte[] łańcuch_bajtowy)
    {
        int wynik;
        int i;
        boolean pośredni;
        
        wynik = -1;
        
        for(i = 0; i < BOMy.length; i++)
        {
            pośredni = CzyJestBOM(BOMy[i], łańcuch_bajtowy);
            
            if(pośredni == true) wynik = i;
        }//next i
        
        
        return wynik;
    }//Koniec sprawdzania
    
    /**
     * Metoda usuwająca BOM z łańcucha bajtowego
     * @param łańcuch_bajtowy łańcuch bajtowy z potencjalnym BOMem.
     * @return tablica bajtowa (nowa) lub null(gdy tablica bajtowa zawiera wyłącznie BOM).
     */
    private byte[] UsuńBOM(byte[] łańcuch_bajtowy)
    {
        byte[] nowy;
        int i;
        int który_bom;
        int początek;
        
        nowy = null;
        
        który_bom = this.CzyJestJakikolwiekBOM(łańcuch_bajtowy);
        
        if(który_bom < 0)
        {
            nowy = new byte[łańcuch_bajtowy.length];
            
            for(i = 0; i < nowy.length; i++) nowy[i] = łańcuch_bajtowy[i];
        }else
        {
            
            if(łańcuch_bajtowy.length > BOMy[który_bom].length)
            {
                nowy = new byte[łańcuch_bajtowy.length - BOMy[który_bom].length];
                
                początek = BOMy[który_bom].length;
                
                for(i = początek ; i < łańcuch_bajtowy.length; i++)
                {
                    nowy[i - początek] = łańcuch_bajtowy[i];
                }
                
            }//end if
            
        }//end if
        
        return nowy;
    }//Koniec zwracania poprawionego łańcucha bajtowego
    
    /**
     * Metoda usuwająca BOM ze stringu.
     * @param tekst Tekst z potencjalnym BOMem.
     * @return Tekst bez BOMu.
     */
    public String KorektaBOM(String tekst)
    {
        String nowy;
        byte[] łańcuch_bajtowy;
        byte[] nowy_łańcuch_bajtowy;
        
        nowy = "";
        
        if(tekst != null && tekst.length() > 0)
        {
            łańcuch_bajtowy = tekst.getBytes(StandardCharsets.UTF_16);
            
            nowy_łańcuch_bajtowy = this.UsuńBOM(łańcuch_bajtowy);
            
            if(nowy_łańcuch_bajtowy != null)
            {
                nowy = new String(nowy_łańcuch_bajtowy, StandardCharsets.UTF_16);
            }//end if
            
        }//end if
        
        return nowy.replaceAll("\r", "");
    }
    
}//Koniec klasy
