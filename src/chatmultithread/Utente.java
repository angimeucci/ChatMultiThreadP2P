/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmultithread;

/**
 * Classe astratta dell'utente che ne gestisce le informazioni come in un 
 * database (QUESTA CLASSE VIENE ADOPERATA PER UNO SCOPO MAGGIORE DI QUELLO 
 * RICHIESTO DALL'ESERCIZIO E PER TAL RAGIONE RISULTA APPARENTEMENTE INUTILE)
 * @author dany
 */
public abstract class Utente {
    private String nome = null;    //nome UNIVOCO dell'utente
    private String info = null;    //infomrazioni personali
    private String ip = null;      //indirizzo ip
    /**
     * Sono presenti piu "costruttori" poiché la classe viene adoperata da piu attori 
     */
    /**
     * Costruttore da client
     * @param n nome
     * @param i info
     */
    public void DeterminaUtente(String n,String i){
        nome=n;
        info=i;
    }
    /**
     * Costruttore da Server
     * @param n nome
     * @param i informazioni
     * @param ip indirizzo ip
     */
    public void DeterminaUtente(String n,String i,String ip){nome = n;info = i;this.ip = ip;}

    
    public String getNome() {return nome;}
    public String getInfo() {return info;}    
    public String getIp() {return ip;}
    /**
     * Facilita il getting delle informazioni totali
     * @return Stringa con tutti i valori
     */
    public String getAll(){return (nome+","+info+","+ip);}
}
