package assets;

import java.util.ArrayList;

public class MyList<E> extends ArrayList<E> {
    private final int maxSize; 
    public MyList(int maximumSize){
        super(); 
        this.maxSize = maximumSize;
    }

    @Override
    public boolean add(E element){
        if(size() < maxSize){
            return this.add(element);
        }else{
            throw new IllegalStateException("List is already full");
        }
    }
}
