/* Solution.java
 * Cameron Rose
 * 220263159
 * ADP262S
 * Assignment 3
 */
package za.ac.cput.assignment3project;

import java.io.EOFException;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Solution {
    
    
    private final String stakeholderOut = "stakeholder.ser";
    
    
    FileWriter fileWriter;
    PrintWriter printWriter;
    
   
    FileInputStream fis;
    ObjectInputStream ois;
    
    public void openFile(String filename)
    {
        try
        {
            fileWriter = new FileWriter(new File(filename));
            printWriter = new PrintWriter(fileWriter);
            System.out.println(filename + " created");
            
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(1);
        }
    }
    
    
    private ArrayList<Customer> customersList()
    {
        ArrayList<Customer> customers = new ArrayList<>();
        
        try
        {
            fis = new FileInputStream(new File(stakeholderOut));
            ois = new ObjectInputStream(fis);
            
            // throws an EOFException 
            while (true)
            {
                Object obj = ois.readObject();
                
                if (obj instanceof Customer)
                {
                    customers.add((Customer) obj);
                }
            }
            
        } catch (EOFException eofe)
        {
            
        } catch (IOException | ClassNotFoundException e)
        {
           e.printStackTrace();
           System.exit(1);
            
        } finally
        {
            try
            {
                fis.close();
                ois.close();
                
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        
        if (!customers.isEmpty())
        {
            
            Collections.sort(customers,
                    (Customer c1, Customer c2) -> 
                            c1.getStHolderId().compareTo(c2.getStHolderId())
            );
        }
        
        return customers;
    }
    
    private void writeCustomerOutFile()
    {
        String header = "======================= CUSTOMERS =========================\n";
        String placeholder = "%s\t%-10s\t%-10s\t%-10s\t%-10s\n";
        String separator = "===========================================================\n";
        
        try
        {   
            printWriter.print(header);
            printWriter.printf(placeholder, "ID", "Name", "Surname", 
                    "Date Of Birth", "Age");
            printWriter.print(separator);
            
            for (int i = 0; i < customersList().size(); i++)
            {   
                printWriter.printf(
                        placeholder,
                        customersList().get(i).getStHolderId(),
                        customersList().get(i).getFirstName(),
                        customersList().get(i).getSurName(),
                        formatDate(customersList().get(i).getDateOfBirth()),
                        calculateAge(customersList().get(i).getDateOfBirth())
                );
            }
            
            printWriter.printf(
                    "\nNumber of customers who can rent: %d", 
                    canRent());
            
            printWriter.printf(
                    "\nNumber of customers who cannot rent: %d", 
                    canNotRent());
            
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    
    private String formatDate(String dob)
    {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy", 
                Locale.ENGLISH);

        LocalDate parseDob = LocalDate.parse(dob); 

        
        return parseDob.format(formatter);
    }
    
    private int calculateAge(String dob)
    {
        LocalDate parseDob = LocalDate.parse(dob); 
        int dobYear  = parseDob.getYear();
        
        ZonedDateTime todayDate = ZonedDateTime.now();
        int currentYear = todayDate.getYear();
        
        
        return currentYear - dobYear;
    }
    
    private int canRent()
    {
        int canRent = 0;
        
        for (int i = 0; i < customersList().size(); i++)
        {
            
            if (customersList().get(i).getCanRent())
            {
                canRent += 1;
            }
        }
        
        return canRent;
    }
    
    private int canNotRent()
    {
        int canNotRent = 0;
        
        for (int i = 0; i < customersList().size(); i++)
        {
            
            if (!customersList().get(i).getCanRent())
            {
                canNotRent += 1;
            }
        }
        return canNotRent;
    }
    
    
    private ArrayList<Supplier> suppliersList()
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        
        try
        {
            fis = new FileInputStream(new File(stakeholderOut));
            ois = new ObjectInputStream(fis);
            
            
            while (true)
            {
                Object obj = ois.readObject();
                
                if (obj instanceof Supplier)
                {
                    suppliers.add((Supplier) obj);
                }
            }
            
        } catch (EOFException eofe)
        {
            
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                fis.close();
                ois.close();
                
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
      
        if (!suppliers.isEmpty())
        {
            
            Collections.sort(
                    suppliers, 
                    (Supplier s1, Supplier s2) -> 
                            s1.getName().compareTo(s2.getName())
            );
        }
        
        return suppliers;
    }
    
    private void writeSupplierOutFile()
    {
        String header = "======================= SUPPLIERS =========================\n";
        String placeholder = "%s\t%-20s\t%-10s\t%-10s\n";
        String separator = "===========================================================\n";
        
        try
        {
            printWriter.print(header);
            printWriter.printf(placeholder, "ID", "Name", "Prod Type",
                "Description");
            printWriter.print(separator);
            for (int i = 0; i < suppliersList().size(); i++)
            {
                printWriter.printf(
                        placeholder,
                        suppliersList().get(i).getStHolderId(),
                        suppliersList().get(i).getName(),
                        suppliersList().get(i).getProductType(),
                        suppliersList().get(i).getProductDescription()
                );
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void closeFile(String filename)
    {
        try
        {
            fileWriter.close();
            printWriter.close();
            System.out.println(filename + " has been closed");

        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    public static void main(String[] args)
    {
        Solution s = new Solution();
        
        s.openFile("customerOutFile.txt");
        s.writeCustomerOutFile();
        s.closeFile("customerOutFile.txt");
        
        System.out.println(""); // spacer
        
        s.openFile("supplierOutFile.txt");
        s.writeSupplierOutFile();
        s.closeFile("supplierOutFile.txt");
    }
}


