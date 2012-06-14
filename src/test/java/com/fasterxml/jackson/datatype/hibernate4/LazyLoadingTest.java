package com.fasterxml.jackson.datatype.hibernate4;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.data.Customer;
import com.fasterxml.jackson.datatype.hibernate4.data.Payment;

import org.hibernate.Hibernate;
import org.junit.Test;

public class LazyLoadingTest extends BaseTest
{
    @Test
    public void testGetCustomerJson() throws Exception
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");

        try {
            EntityManager em = emf.createEntityManager();
            
            // false -> no forcing of lazy loading
            ObjectMapper mapper = mapperWithModule(false);
            
            Customer customer = em.find(Customer.class, 103);
            assertFalse(Hibernate.isInitialized(customer.getPayments()));
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customer);
            // should not force loading...
            Set<Payment> payments = customer.getPayments();
            
            System.out.println("--- JSON ---");
            System.out.println(json);
            System.out.println("--- /JSON ---");
            
            assertFalse(Hibernate.isInitialized(payments));
            
            // TODO: verify
            assertNotNull(json);
            /*
            System.out.println("--- JSON ---");
            System.out.println(json);
            System.out.println("--- /JSON ---");
            */
        } finally {
            emf.close();
        }
    }
}
