/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleepingbarber;

/**
 *
 * @author studente
 */
public class Customer extends Thread{
    // attributi funzionali
    private BarberShop myShop;
    
    // costruttore
    public Customer(BarberShop b, String name){
        super(name);
        this.myShop = b;
        
    }
    // comportamento del thread
    public void run(){
        for(int i = 0; i < 10; i++){
            // provo a tagliare i capelli
            if(this.myShop.lookAndWait(this)){
               // mi sono stati tagliati i capelli
               System.out.println(super.getName()+" ho tagliato i capelli");
               // simulo la ricrescita
               try{
                   Thread.sleep(100);
               }catch(InterruptedException e){
                   System.out.println(e);
               
               }
            }else{
                // attendo un po' prima di tornare a vedere se c'è posto
                System.out.println(super.getName()+" non ha tagliato i capelli");
                try{
                   Thread.sleep(100);
                }catch(InterruptedException e){
                   System.out.println(e); 
                }
         
            }
            }
        }
    
    }
    

