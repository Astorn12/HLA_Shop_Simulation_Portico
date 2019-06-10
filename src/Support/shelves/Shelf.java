package Support.shelves;

import Interractions.ShelfRegistration;

/**
 * Created by osiza on 03.04.2019.
 */
public class Shelf {
    int id;

    int size;
    int fill;

    String product;

    public Shelf(int id,int size, String product) {

        this.id=id;
        this.size = size;
        this.fill = size;
        this.product=product;
    }



    public Shelf(ShelfRegistration sr)
    {
        this.size=sr.getSize();
        this.fill=sr.getSize();
        this.id=sr.getShelfId();
        this.product=sr.getProduct();
    }



    public void update(int fill)
    {
        this.fill=fill;
    }



    public void setSize(int size) {
        this.size = size;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }



    public int getSize() {
        return size;
    }

    public int getFill() {
        return fill;
    }

    public int takeProduct(int n)
    {
        int notEnought=0;

        if(fill-n>=0)
        {
            fill-=n;
        }else{
            notEnought=n-fill;
            fill=0;
        }
        return notEnought;
    }

    public int putProduct(int n)
    {
        int rest=0;
        if(n+fill<=size)
        {
            this.fill+=n;

        }
        else{
            rest=n+fill-size;
            fill=size;
        }

        return rest;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString()
    {
        return "Półka:"+this.getId()+" z "+ this.getFill()+"/"+this.getSize()+" "+ this.getProduct()+ " | \n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void decreaseStock(int n )
    {
        if(n<=this.fill)
     this.fill-=n;
    }
    public void increaseStock(int n)
    {
     this.fill+=n;
     if(fill>this.size)
     {
         this.fill=size;
     }
    }
}