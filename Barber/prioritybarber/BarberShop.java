/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prioritybarber;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author studente
 */
// l'oggetto condiviso fra i thread cliente ed il thread barbiere

public class BarberShop {
    // attributi funzionali
    // coda esplicita per la gestione del
    // risveglio arbitrario dei thread
    private LinkedList<Customer> myQueue;
    
    // attributi di sincronizzazione
    // a guardia di myQueue
    private ReentrantLock mutex;
    // semaforo contatore per sospendere il barbiere
    private Semaphore newCustomer;
    
    // costruttore 
    public BarberShop(){
        // inizializzo gli attributi funzionali
        this.myQueue     = new LinkedList();
        // attributi di sincronizzazione
        this.mutex       = new ReentrantLock();
        this.newCustomer = new Semaphore(0);
    }// end costruttore
    
    
    // metodi pubblici thread-safe
    // metodo per entrare in attesa del taglio
    // deve essere in mutua esclusione con il metodo
    // che taglia i capelli e deve sospendere il thread 
    // invocante in attesa del taglio
    public void enter(Customer c){
        // devo inserire il riferimento nella coda
        // INIZIO SEZIONE CRITICA
        this.mutex.lock();
        try{
            System.out.println(c.getName()+" si mette in attesa con "+
                    " priorità "+c.getMyPriority());
            this.myQueue.add(c);
            // sveglio il barbiere se stava dormendo
            this.newCustomer.release();
            
        }finally{
            this.mutex.unlock();
            //FINE SEZIONE CRITICA
        }
        // ora posso sospendermi in attesa del servizio
        try{
            c.block();
        }catch(InterruptedException e){
            System.out.println(e);
        }
        // se sono qua significa che il barbiere mi ha tagliato i capelli
        // e sono stato risvegliato 
        /* la traccia deve essere verbosa: priorità a stampa! */
        System.out.println(c.getName()+" priorità: "+c.getMyPriority()+
                " ha tagliato i capelli ");
    }// end metodo enter()
    
    // metodo invocato dal barbiere per tagliare i capelli
    // in mutua esclusione con il metodo enter
    // sospende il barbiere se non ci sono richieste
    // deve inoltrare l'eccezione di InterruptedException per
    // attuare la terminazione deferita del thread barbiere
    public void cut() throws InterruptedException {
        // controllo se ci sono clienti
        this.newCustomer.acquire();
        // se sono qua significa che almeno un cliente si trova
        // in coda
        // devo trovare il cliente migliore (con massima priorità)
        // e tagliare i capelli al lui
        Customer best = null;
        // INIZIO SEZIONE CRITICA
        this.mutex.lock();
        try{
            best = getAndRemoveBestCustomer();
            // abbiamo nella variabile best il miglior cliente
            /* stringa verbosa: priorità a stampa */
            System.out.println("Il barbiere taglia i capelli a: "+
                    best.getName()+" con priorità: "+
                    best.getMyPriority());
        }finally{
            this.mutex.unlock();
            // FINE SEZIONE CRITICA
        }
        // simulo il tempo necessario a tagliare i capelli
        Thread.sleep(20);
        // ora posso liberare il cliente
        best.wakeUp();
    }

    private Customer getAndRemoveBestCustomer() {
        // cerchiamo il miglior cliente
        Customer theBest = null;
        Customer current = null;
        int maxPriority  = -1;
        /**ATTENZIONE!!!**/
        /* a parità di priorità metto >=, che è LIFO,
         > è FIFO */
        for(int i = 0; i < this.myQueue.size(); i++){
            current = this.myQueue.get(i);
            if(current.getMyPriority() > maxPriority){
                maxPriority = current.getMyPriority();
                theBest = current;
            }
       
        }
        // rimuovo dalla coda il cliente migliore
        this.myQueue.remove(theBest);
        return theBest;
        
    }
        
    
    
}// end class
