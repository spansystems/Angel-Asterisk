/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.span.temp;

/**
 *
 * @author prashanth_p
 */
public class Cat extends Animal {
    @Override
    public void testClassMethod() {
        System.out.println("The class method in Cat.");
    }
    @Override
    public void testInstanceMethod() {
        System.out.println("The instance method in Cat.");
    }
    public static void main(String[] args) {
        Cat myCat = new Cat();
        Animal myAnimal = myCat;
        ((Animal)myCat).testClassMethod();
        myAnimal.testClassMethod();
        myAnimal.testInstanceMethod();
        
        Animal animal = new Animal();
        ((Cat)animal).testClassMethod();
    }
}
