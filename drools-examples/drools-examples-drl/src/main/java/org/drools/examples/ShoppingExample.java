package org.drools.examples;

import java.io.InputStreamReader;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.KnowledgeType;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;


public class ShoppingExample {

    public static final void main(String[] args) throws Exception {
        final KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        builder.addResource( new InputStreamReader( ShoppingExample.class.getResourceAsStream( "Shopping.drl" ) ) ,KnowledgeType.DRL);

        final KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages( builder.getKnowledgePackages() );

        final StatefulKnowledgeSession session = knowledgeBase.newStatefulKnowledgeSession();

        Customer mark = new Customer( "mark",
                                      0 );
        session.insert( mark );

        Product shoes = new Product( "shoes",
                                     60 );
        session.insert( shoes );

        Product hat = new Product( "hat",
                                   60 );
        session.insert( hat );

        session.insert( new Purchase( mark,
                                      shoes ) );
        
        
        
        FactHandle hatPurchaseHandle = session.insert( new Purchase( mark,
                                                                     hat ) );

        session.fireAllRules();

        session.retract( hatPurchaseHandle );
        System.out.println( "Customer mark has returned the hat" );
        session.fireAllRules();
    }
    
    public static class Customer {
        private String name;
        
        private int discount;
            
        public Customer(String name,
                        int discount) {
            this.name = name;
            this.discount = discount;
        }

        public String getName() {
            return name;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }
                       
    }
    
    public static class Discount {
        private Customer customer;
        private int amount;

        public Discount(Customer customer,
                        int amount) {
            this.customer = customer;
            this.amount = amount;
        }    
        
        public Customer getCustomer() {
            return customer;
        }

        public int getAmount() {
            return amount;
        }
        
    }

    public static class Product {
        private String name;
        private float price;
        
        public Product(String name,
                       float price) {
            this.name = name;
            this.price = price;
        }
        
        public String getName() {
            return name;
        }
        
        public float getPrice() {
            return price;
        }
        
        
    }
    
    public static class Purchase {
        private Customer customer;
        private Product product;
        
        public Purchase(Customer customer,
                        Product product) {
            this.customer = customer;
            this.product = product;
        }
        
        public Customer getCustomer() {
            return customer;
        }
        
        public Product getProduct() {
            return product;
        }            
    }    
}
