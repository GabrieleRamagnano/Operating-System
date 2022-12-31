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
public class Barber extends Thread{
    private BarberShop shop;
    
    // costruttore
    public Barber(BarberShop s){
        super("Barber");
        this.shop = s;
    }
    
    // comportamento del barbiere 
    // che si trova in terminazione DEFERITA
    
    @Override
      public void run(){
        // TERMINAZIONE DEFERITA
        // deve essere in un ciclo infinito
        // finchè non arriva un interrupt
        boolean isAlive = true;
        while(isAlive){
            try{
                this.shop.cut();
            }catch(InterruptedException e){
                System.out.println("Il barbiere riceve l'interrupt");
                System.out.println(e);
                isAlive = false;
            }            
        
        }
        System.out.println(super.getName()+" termina!!!");
        
    }// end run()
}
