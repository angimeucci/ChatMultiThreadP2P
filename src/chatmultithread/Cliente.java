/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmultithread;

/**
 * Classe che estende la classe Utente per formare una classe tipo "Client" 
 * dalla parte server
 * @author dany
 */
import java.io.*;
import java.net.*;
public class Cliente extends Utente implements Runnable{
    Server server_appartenenza;         //Determianare il server di base per prelevarne i metodi
    Socket client;                      //Racchiude il socket del cliente singolo
    BufferedReader dati_dal_client;     //dati che vengono ricevuti dal client
    DataOutputStream dati_al_client;    //dati che vengono inviati al client
    
    
    /**
     * inserisce i dati nella classe padre utente 
     * @param n nome del cliente UNIVOCO DENTRO LA CHAT
     * @param i informazioni del cliente NON ATTUALMENTE UTILE NEL PROGRAMMA
     * @param ip indirizzo dell'utente NON ATTUALMENTE UTILE NEL PROGRAMMA
     */
    public void superCostruttor(String n,String i,String ip){   
        super.DeterminaUtente(n,i,ip);
    }
    /**
     * Crea il socket per il singolo client
     * @param client cliente appena costruito
     * @param server server di appartenenza del cliente instanziato per 
     * prelevarne i metodi
     */
    public void setClient(Socket client, Server server) {
        this.client = client;
        server_appartenenza = server;
    }
    public Socket getClient() {return client;}
    /**
     * Salva le classi dalle quali dovra inserire e prendere informazioni
     * dal socket incluso in questa clas
     * @param dati_dal_client socket in entrata
     * @param dati_al_client socket in uscita
     */
    public void setDati_client(BufferedReader dati_dal_client, DataOutputStream dati_al_client) {
        this.dati_dal_client = dati_dal_client;
        this.dati_al_client = dati_al_client;
    }
    /**
     * I setter dei dati dal/al client non vengono usati poiché sono 
     * inizializzati in un singolo metodo
    */
    /**
     * 
     * @return dati in arrivo dal client
     */
    public BufferedReader getDati_dal_client() {return dati_dal_client;}
    /**
     * 
     * @return dati da inviare al client
     */
    public DataOutputStream getDati_al_client() {return dati_al_client;}
    /**
     * Ascolta la risposta del client e invia il messaggio al server che 
     * successivamente si occuperà di tradurlo
     */
    public void run(){  
        String risposta;
        boolean attivo = true;
        while(attivo){
            try{
                risposta = dati_dal_client.readLine();
                server_appartenenza.Traduci(risposta, this.getNome());
            }catch(Exception e){
                System.out.println();
                System.out.println("@ " + this.getNome() + " ha avuto qualche problemino");
                System.out.println("@ cancellazione del client ");
                System.out.println("Server in attesa del client.");
                server_appartenenza.Scollega(this.getNome());
                attivo = false;
            }
        }
    }
    
}
