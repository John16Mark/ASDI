import java.util.List;

public class Produccion {
    
    final NoTerminal ladoIzq;
    final List<Object> ladoDer;
    
    public Produccion(NoTerminal li, List<Object> ld){
        this.ladoIzq = li;
        this.ladoDer = ld;
    }
}
