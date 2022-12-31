/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priorityOrtofrutta;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import prioritybarber.Customer;

/**
 *
 * @author studente
 */
// l'oggetto condiviso fra i thread cliente ed il thread barbiere

public class OrtoShop {
    // attributi funzionali
    // coda esplicita per la gestione del
    // risveglio arbitrario dei thread
    private LinkedList<Cliente> myInsalata;
    private LinkedList<Cliente> myPomodoro;
    private LinkedList<Cliente> myOrderInsalata;
    private LinkedList<Cliente> myOrderPomodoro;

    // attributi di sincronizzazione
    // a guardia di myQueue
    private ReentrantLock mutex;
    /* semaforo contatore per notificare gli ordini e...
     * ...sospendere il gestore */
    private Semaphore newOrder;
    // semaforo contatore per sospendere il gestore
    private Semaphore newCustomer;
    
    // costruttore 
    public OrtoShop(){
        // inizializzo gli attributi funzionali
        this.myInsalata      = new LinkedList<>();
        this.myPomodoro      = new LinkedList<>();
        this.myOrderInsalata = new LinkedList<>();
        this.myOrderPomodoro = new LinkedList<>();
        // attributi di sincronizzazione
        this.mutex       	 = new ReentrantLock();
        this.newOrder    	 = new Semaphore(0);
        this.newCustomer     = new Semaphore(0);
    }// end costruttore
    
    
    // metodi pubblici thread-safe
    /* metodo per entrare in attesa dell'accettazione
    // deve essere in mutua esclusione con il metodo
    // che formula l'ordine e deve sospendere il thread 
    // invocante in attesa dell'accettazione */
    public void sottomettiRichiesta(Cliente c){
        // devo inserire il riferimento nella coda
        // INIZIO SEZIONE CRITICA
        this.mutex.lock();
        try{
            System.out.println(c.getName() + " " +
            		           c.getOrtofrutta() +
            		           " si mette in attesa con priorità " +
                               c.getMyPriority());
            
            // inserisco le richieste nelle apposite code
            if (c.getOrtofrutta() == "INSALATA")
               this.myInsalata.add(c);
            else
               this.myPomodoro.add(c);
            	
            // sveglio il gestore se stava dormendo
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
        // se sono qua significa che il gestore ha accettato la richiesta
        // e sono stato risvegliato 
        System.out.println(c.getName()+" priorità: " + c.getMyPriority()+
                " ha tagliato i capelli ");
    }// end metodo sottomettiRichiesta()
    
    /* metodo invocato dal gestore per formulare gli ordini
     * in mutua esclusione con il metodo sottomettiRichiesta
     * sospende il gestore se non ci sono richieste
     * deve inoltrare l'eccezione di InterruptedException per
     * attuare la terminazione deferita del thread gestore */
    public void formulaOrdini() throws InterruptedException {
        // controllo se ci sono clienti
        this.newCustomer.acquire();
        /* se sono qua significa che almeno un cliente si trova
         * in coda: ora controllo se ho richieste sufficienti per
         * formulare l'ordine... */  
        
        
        // INIZIO SEZIONE CRITICA
        this.mutex.lock();
        try{
        	verificaOrdine();     	
        	
        }finally{
            this.mutex.unlock();
            // FINE SEZIONE CRITICA
        }
        
        // mi blocco in attesa dell'ordine
        this.newOrder.acquire();
        // simulo il tempo necessario a inviare l'ordine
        Thread.sleep(100);
        // ora posso liberare i clienti
        for (Cliente c : this.myOrderInsalata)
            c.wakeUp();
        for (Cliente c : this.myOrderPomodoro)
        	c.wakeUp();
    }

    
    private void verificaOrdine() {
    	
       
    	Cliente best = null;
    	Cliente best1 = null;
    	
    	System.out.println("siamo arrivati ?");
    	
        	if (!this.myInsalata.isEmpty() && this.myInsalata.size() >= 3) {
        		
        		for (Cliente c : this.myInsalata) {
        			
                    best = getAndRemoveBestInsalata();
                    // abbiamo nella variabile best il miglior cliente
                    System.out.println("Il gestore seleziona l'insalata: "+
                                        best.getName() + " con priorità: "+
                                        best.getMyPriority());
        			this.myOrderInsalata.add(best);
        		}
        	}
        		
            System.out.println("siamo arrivati ?");
            
            if (!this.myPomodoro.isEmpty() && this.myPomodoro.size() >= 2) {
            	
            	for (Cliente c : this.myPomodoro) {
        			
                    best1 = getAndRemoveBestPomodoro();
                    // abbiamo nella variabile best il miglior cliente
                    System.out.println("Il gestore seleziona il pomodoro: "+
                                        best1.getName() + " con priorità: "+
                                        best1.getMyPriority());
           			this.myOrderPomodoro.add(best1);
           		}
             }
            
            System.out.println("siamo arrivati ?");  
            
        	if (this.myOrderInsalata.size() >= 3 && this.myOrderPomodoro.size() >= 2)
        		this.newOrder.release();
        	else {
        		
        		for (Cliente c: this.myOrderInsalata)
        			this.myInsalata.add(c);
        	
        		for (Cliente c: this.myOrderPomodoro)
        			this.myPomodoro.add(c);
        	}
        	  
    }
     
    
    
    private Cliente getAndRemoveBestInsalata() {
        // cerchiamo la miglior insalata
        Cliente theBest = null;
        Cliente current = null;
        int maxPriority  = -1;
        
        for(int i = 0; i < this.myInsalata.size(); i++){
            current = this.myInsalata.get(i);
            if(current.getMyPriority() > maxPriority){
                maxPriority = current.getMyPriority();
                theBest = current;
            }
       
        }
        // rimuovo dalla coda l'insalata più pesante
        this.myInsalata.remove(theBest);
        return theBest;
        
    }
    
    private Cliente getAndRemoveBestPomodoro() {
        // cerchiamo la miglior insalata
        Cliente theBest = null;
        Cliente current = null;
        int maxPriority  = -1;
        
        for(int i = 0; i < this.myPomodoro.size(); i++){
            current = this.myPomodoro.get(i);
            if(current.getMyPriority() > maxPriority){
                maxPriority = current.getMyPriority();
                theBest = current;
            }
       
        }
        // rimuovo dalla coda l'insalata più pesante
        this.myPomodoro.remove(theBest);
        return theBest;
        
    }
        
        
    
    
}// end class
