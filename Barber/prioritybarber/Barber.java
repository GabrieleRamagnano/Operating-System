/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prioritybarber;

/**
 *
 * @author studente
 */
public class Barber extends Thread{
    private BarberShop myShop;
    
    public Barber(BarberShop b){
        super("Barber");
        this.myShop = b;
    }
    // comportamento del thread
    // deve essere in terminazione deferita
    @Override
    public void run(){
        boolean isAlive = true;
        while(isAlive){
            try{
                this.myShop.cut();
            }catch(InterruptedException e){
                System.out.println(e);
                isAlive = false;
                           
            }
        }// end while
        System.out.println(super.getName() + " termina!!!");
    }// end etodo run()
}// end classe
