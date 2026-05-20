package model;

/**
 * ORDERS - entitate care reprezinta un rand din tabelul orders
 *        - clasa stocheaza informatiile despre comenzile efectuate,
 * facand legatura intre id-ul comenzii, id-ul clientului, id-ul produsului
 * si cantitatea comandata
 */
public class Orders
{
    private int id;
    private int clientId;
    private int productId;
    private int quantity;

    /**
     * constructor implicit necesar pentru maparea din baza de date.
     */
    public Orders() {}

    //SETTER SI GETTER
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getClientId()
    {
        return clientId;
    }
    public void setClientId(int clientId)
    {
        this.clientId = clientId;
    }
    public int getProductId()
    {
        return productId;
    }
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}