/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmultithread;

import java.util.*;

/**
 * Lancia di base il display del cliente che a sua volta inizializzera tutto il 
 * resto delle classi (client, lstenerclient, utente)
 * @author dany
 */
public class ChatMultiThread {

    /**
     * Avvia la schermata utente che gestirà il resto delle classi
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SchermataClient display = new SchermataClient();
        display.setVisible(true);
        display.setResizable(false);
    }
    
}
