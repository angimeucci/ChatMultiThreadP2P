/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmultithread;

/**
 * Classe vera e propria del server, gestisce le richieste, le porte e i clienti
 * connessi attraverso i thread
 * @author dany
 * Il protocollo prevede: 
 * @101 : verifica di connessione dal server
 * @202 : aggiunta utente da mettere nella form
 * @303 : rimozione utente
 * @404 : messaggio nomale
 */
import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    ServerSocket socket_server = null;
    HashMap<String, Cliente> sockets_client = null;
    HashMap<String, Thread> listener = null;
    Cliente cliente_in_entrata = null;
    
    String messaggio_client = null;
    String risposta_server = null;
    /**
     * metodo di attesa della connessione del server
     */
    public void connetti(){
        try {
            /**
             * startaggio e monitoraggio delle risorse 
             */
            System.out.println("Server in esecuzione.");
            socket_server = new ServerSocket(3030);
            sockets_client = new HashMap();
            listener = new HashMap();
        }catch (Exception e) {
            System.out.println("Errore nella connessione al server.");
        }
        while(true){
            try{
                /**
                 * operazione di attesa di richiesta del client
                 */
                System.out.println("Server in attesa del client.");
                cliente_in_entrata = new Cliente();
                //socket_client.add(new Cliente());
                cliente_in_entrata.setClient(socket_server.accept(), this);
                /**
                 * Costruzione dei parametri di comunicazione col client
                 */
                System.out.println("Cliente in costruzione");
                cliente_in_entrata.setDati_client(
                    new BufferedReader(new InputStreamReader(cliente_in_entrata.getClient().getInputStream())), 
                    new DataOutputStream(cliente_in_entrata.getClient().getOutputStream()));
                /**
                 * Controllo dell'univocità
                 */
                System.out.println("Ultimi passaggi di costruzione");
                messaggio_client = cliente_in_entrata.dati_dal_client.readLine();
                String[] s = messaggio_client.split(",");
                cliente_in_entrata.superCostruttor(s[0], s[1], cliente_in_entrata.getClient().getInetAddress().toString());
                if(sockets_client.containsKey(cliente_in_entrata.getNome())){
                    System.out.println("Nome gia adoperato");
                    cliente_in_entrata.dati_al_client.writeBytes("@Server : il tuo nome e' gia stato usato" + '\n');
                    throw new Exception();
                }else{
                    sockets_client.put(cliente_in_entrata.getNome(), cliente_in_entrata);
                    System.out.println("Client accettato.");
                    cliente_in_entrata.dati_al_client.writeBytes("@Server : Sei stato accettato" + '\n');
                    /**
                     * aggiunta del client all'hashmap dei client
                     */
                    System.out.println("Aggiunta del nuovo cliente nella lista");
                    cliente_in_entrata.dati_al_client.writeBytes("@Server : Ti stiamo aggiungendo alla lista..." + '\n');
                    attivaUtente(cliente_in_entrata);
                    System.out.println("Client connesso");
                    cliente_in_entrata.dati_al_client.writeBytes("@Server : Benvenuto nel server" + '\n');
                    if(sockets_client.size() == 1) 
                        cliente_in_entrata.dati_al_client.writeBytes("@Server : In attesa di un nuovo utente" + '\n');
                    else{
                        cliente_in_entrata.dati_al_client.writeBytes("@Server : Attualmente ti trovi in gruppo." + '\n');
                        Scrivi(" E' appena entrato nella chat", cliente_in_entrata.getNome());
                    }
                    System.out.println(); 
                }
            }catch(Exception e){
                System.out.println("@Errore nella connessione del nuovo client");
                System.out.println();     
            }
        }
    }
    /**
     * aggiungi un nuovo thread alla lista dei thread degli utenti
     * @param nomedelcliente 
     */
    private void attivaUtente(Cliente nomedelcliente){
        listener.put(nomedelcliente.getNome(), new Thread(nomedelcliente));
        listener.get(nomedelcliente.getNome()).start();
    }
    /**
     * Scrive il messaggio mex ad ogni elemento dell'hashmap escludendo l'autore
     * del messaggio stesso 
     * @param mex messaggio da mandare
     * @param nome autore del messaggio e elemento da escludere dall'hashmap
     */
    public void Scrivi(String mex, String nome){
        try{
            for(String s : sockets_client.keySet()) 
                if(sockets_client.get(s).getNome().equals(nome)){}
                else sockets_client.get(s).getDati_al_client().writeBytes(nome + ": " + mex + '\n'); 
        }catch(Exception e){
            System.out.println("@ERRORE: Non è stato possibile inviare il messaggio " + mex + " a tutti gli altri client");
        }
    }
    /**
     * Effettua dei controlli sul messaggio per verificare che non sia un 
     * comando, esso infatti si differenzia dal messaggio normale attraverso 
     * la @ iniziale
     * @param mex messaggio 
     * @param nome autore
     */
    public void Traduci(String mex, String nome){
        if(mex.charAt(0) == '@'){
            String command = "";
            String message = "";
            for(int i = 1; i < mex.length(); i++){
                if(mex.charAt(i) != ' '){
                    command += mex.charAt(i);
                }else{
                    for(int j = i + 1; j < mex.length(); j++)
                        message += mex.charAt(j);
                    i = mex.length();
                }
            }
            try{
            switch(command){
                case "ext":
                    Scollega(nome);
                    break;
                case "ls":
                    String partecipanti = "Partecipanti: ";
                    for(String s : sockets_client.keySet())
                        partecipanti += s + " "; 
                    sockets_client.get(nome).getDati_al_client().writeBytes(partecipanti + '\n');
                    break;
                default:
                    if(sockets_client.containsKey(command)){
                        sockets_client.get(command).getDati_al_client().writeBytes(nome + " a mandato solo a te: " + message + '\n');
                    }else{
                        sockets_client.get(nome).getDati_al_client().writeBytes("@Server: Errore. comando errato" + '\n');
                    }
                    break;
            }}catch(Exception e){System.out.println("@Errore: qualcosa è andato storto");}
        }else{Scrivi(mex, nome);}
    }
    /**
     * elimina gli utenti dalla lista delle persone attive
     * @param nome nome dell'utente da scollegare
     */
    public void Scollega(String nome){
        try{    listener.get(nome).wait();   }catch(Exception e){}
        listener.remove(nome);
        sockets_client.remove(nome);
        Scrivi("E' appena uscito dalla chat", nome);
    }
}
