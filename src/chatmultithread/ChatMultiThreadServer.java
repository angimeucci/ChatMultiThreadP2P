/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatmultithread;

/**
 * Launcher del server
 * @author dany
 */
import java.io.*;
import java.net.*;
import java.util.*;
public class ChatMultiThreadServer {
    /**
     * Launcher di server
     * @param args 
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.connetti();
    }
}
