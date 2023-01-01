/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priorityOrtofrutta;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author studente
 */
// un thread cliente dotato di un 
// meccanismo interno per la sospensione
// e il risveglio selettivo

public class Cliente extends Thread{
    // attributi funzionali 
    private OrtoShop myShop;
    private Random rnd;
    // attributo che definisce la tipologia di richiesta
    private String ortofrutta;
    // attributo interno che definisce la priorità
    private int myPriority;
    
    // attributi di sincronizzazione
    private Semaphore mySem;
    
    // costruttore della classe
    public Cliente(OrtoShop o, 
    		       String name,
    		       String ortofrutta){
        // setto il nome nel costruttore del padre
        super(name);
        this.myShop     = o;
        this.rnd        = new Random();
        this.ortofrutta = ortofrutta;
        this.myPriority = this.rnd.nextInt(11) + 10; // [10,20]
        // ora inizializzo il semaforo interno
        this.mySem      = new Semaphore(0);
    }
    // dichiaro i metodi pubblici per sospendere e risvegliare
    // il thread
    public void block() throws InterruptedException{
        this.mySem.acquire();
    }
    
    public void wakeUp(){
        this.mySem.release();
    }
    
    // metodo per leggere il tipo di prodotto
    public String getOrtofrutta() {
    	return this.ortofrutta;
    }
    
    // metodo per leggere la priorità del cliente
    public int getMyPriority(){
        return this.myPriority;
    }
    
    // comportamento del thread
    @Override
    public void run(){
       /* Il cliente effettua una richiesta di acquisto per il 
        * proprio prodotto */
       try{
    	   this.myShop.sottomettiRichiesta(this);
           /* il cliente si sospende per simulare la formulazione 
            * della richiesta */
           Thread.sleep(this.rnd.nextInt(401) + 10); // [10,500]          
       }catch(InterruptedException e) {        
    	   System.out.println(e);
       }
        System.out.println(super.getName() + " termina!!");
    }// end run()
    
}//end classe
