import java.util.ArrayList;

public class Produccion {
    
    final NoTerminal ladoIzq;
    final ArrayList<Object> ladoDer;
    
    public Produccion(NoTerminal li, ArrayList<Object> ld){
        this.ladoIzq = li;
        this.ladoDer = ld;
    }
}
