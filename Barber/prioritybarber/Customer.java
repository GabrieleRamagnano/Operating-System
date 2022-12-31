/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prioritybarber;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author studente
 */
// un thread cliente dotato di un 
// meccanismo interno per la sospensione
// e il risveglio selettivo

public class Customer extends Thread{
    // attributi funzionali 
    private BarberShop myShop;
    private Random rnd;
    // attributo interno che definisce la priorità
    private int myPriority;
    
    // attributi di sincronizzazione
    private Semaphore mySem;
    
    // costruttore della classe
    public Customer(BarberShop b, String name){
        // setto il nome nel costruttore del padre
        super(name);
        this.myShop     = b;
        this.rnd        = new Random();
        this.myPriority = this.rnd.nextInt(100); // [0,99]
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
    
    // metodo per leggere la priorità del cliente
    public int getMyPriority(){
        return this.myPriority;
    }
    
    // comportamento del thread
    @Override
    public void run(){
        // vado a farmi tagliare i capelli 10 volte
        for(int i = 0; i < 10; i++){
            try{
                this.myShop.enter(this);
                // mi sospendo per simulare la ricrescita
                Thread.sleep(this.rnd.nextInt(21)+10); // [10,30]
            }catch(InterruptedException e){
                System.out.println(e);
            }
        }// end for
        System.out.println(super.getName()+" termina!!");
    }// end run()
}//end classe
