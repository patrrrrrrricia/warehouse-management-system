package model;

/**
 * PRODUCT - entitate care reprezinta un rand din tabelul product
 *         - stocheaza informatiile despre produse, incluzand
 * identificatorul, numele, cantitatea disponibila in stoc si pretul unitar
 */
public class Product
{
    private int id;
    private String name;
    private double stock;
    private double price;

    /**
     * constructor implicit necesar pentru maparea din baza de date
     */
    public Product() {}

    //SETTER SI GETTER
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public double getStock()
    {
        return stock;
    }
    public void setStock(double stock)
    {
        this.stock = stock;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
}