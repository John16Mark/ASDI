import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ASDI implements Parser{

    private int i = 0;
    private final List<Token> tokens;
    private final Stack<Object> pila = new Stack<>();

    // Producciones
    private ArrayList<Object> Q = new ArrayList<>(Arrays.asList(TipoToken.SELECT, NoTerminal.D, TipoToken.FROM, NoTerminal.T));
    private ArrayList<Object> D_1 = new ArrayList<>(Arrays.asList(TipoToken.DISTINCT, NoTerminal.P));
    private ArrayList<Object> D_2 = new ArrayList<>(Arrays.asList(NoTerminal.P));
    private ArrayList<Object> P_1 = new ArrayList<>(Arrays.asList(TipoToken.ASTERISCO));
    private ArrayList<Object> P_2 = new ArrayList<>(Arrays.asList(NoTerminal.A));
    
    private ArrayList<Object> A = new ArrayList<>(Arrays.asList(NoTerminal.A2, NoTerminal.A1));
    private ArrayList<Object> A1_1 = new ArrayList<>(Arrays.asList(TipoToken.COMA, NoTerminal.A));
    private ArrayList<Object> A1_2 = new ArrayList<>();
    private ArrayList<Object> A2 = new ArrayList<>(Arrays.asList(TipoToken.IDENTIFICADOR, NoTerminal.A3));
    private ArrayList<Object> A3_1 = new ArrayList<>(Arrays.asList(TipoToken.PUNTO, TipoToken.IDENTIFICADOR));
    private ArrayList<Object> A3_2 = new ArrayList<>();
    
    private ArrayList<Object> T = new ArrayList<>(Arrays.asList(NoTerminal.T2, NoTerminal.T1));
    private ArrayList<Object> T1_1 = new ArrayList<>(Arrays.asList(TipoToken.COMA, NoTerminal.T));
    private ArrayList<Object> T1_2 = new ArrayList<>();
    private ArrayList<Object> T2 = new ArrayList<>(Arrays.asList(TipoToken.IDENTIFICADOR, NoTerminal.T3));
    private ArrayList<Object> T3_1 = new ArrayList<>(Arrays.asList(TipoToken.IDENTIFICADOR));
    private ArrayList<Object> T3_2 = new ArrayList<>();

    // Tabla, filas son no terminales, columnas son tokens
    private final ArrayList<ArrayList<Object>> tabla = new ArrayList<>();
    ArrayList<NoTerminal> filas = new ArrayList<>(Arrays.asList(NoTerminal.Q,NoTerminal.D,NoTerminal.P,NoTerminal.A,NoTerminal.A1,NoTerminal.A2,NoTerminal.A3,NoTerminal.T,NoTerminal.T1,NoTerminal.T2,NoTerminal.T3));
    ArrayList<TipoToken> columnas = new ArrayList<>(Arrays.asList(TipoToken.SELECT,TipoToken.FROM,TipoToken.DISTINCT,TipoToken.ASTERISCO,TipoToken.PUNTO,TipoToken.COMA,TipoToken.IDENTIFICADOR, TipoToken.EOF));

    public ASDI(List<Token> tokens){
        this.tokens = tokens;

        // Creación de la tabla
        for(int k=1; k<=11; k++){
            tabla.add(new ArrayList<Object>(Arrays.asList(null,null,null,null,null,null,null,null)));
        }
        tabla.get(0).set(0, Q);

        tabla.get(1).set(2, D_1);
        tabla.get(1).set(3, D_2);
        tabla.get(1).set(6, D_2);

        tabla.get(2).set(3, P_1);
        tabla.get(2).set(6, P_2);

        tabla.get(3).set(6, A);

        tabla.get(4).set(1, A1_2);
        tabla.get(4).set(5, A1_1);

        tabla.get(5).set(6, A2);

        tabla.get(6).set(1, A3_2);
        tabla.get(6).set(4, A3_1);
        tabla.get(6).set(5, A3_2);

        tabla.get(7).set(6, T);

        tabla.get(8).set(5, T1_1);
        tabla.get(8).set(7, T1_2);

        tabla.get(9).set(6, T2);

        tabla.get(10).set(5, T3_2);
        tabla.get(10).set(6, T3_1);
        tabla.get(10).set(7, T3_2);

    }

    @Override
    public boolean parse() {
        pila.push(TipoToken.EOF);
        pila.push(NoTerminal.Q);

        // Analizando todos los tokens
        while(i<tokens.size()){
            // Si el tope de la pila y el token apuntado son iguales
            if(pila.peek() instanceof TipoToken && pila.peek() == tokens.get(i).tipo){
                // Si el tope de la pila y el token apuntado son fin de cadena -> Terminar el análisis
                if(pila.peek() == TipoToken.EOF && tokens.get(i).tipo == TipoToken.EOF){
                    System.out.println("\033[94mAnálisis Sintáctico correcto\033[0m");
                    return true;
                }
                i++;
                pila.pop();
            }
            // Si el tope de la pila es un NoTerminal
            else if(pila.peek() instanceof NoTerminal){
                int fila = filas.indexOf(pila.peek());
                int columna = columnas.indexOf(tokens.get(i).tipo);

                // Si no hay nada en la celda -> error
                if(tabla.get(fila).get(columna) == null){
                    System.out.println("\033[91mSe encontraron errores\033[0m");
                    return false;
                } else {
                    // Sacar un elemento de la pila
                    pila.pop();
                    ArrayList<Object> celda = (ArrayList<Object>) tabla.get(fila).get(columna);
                    
                    // Añadir todos los elementos de la producción en orden inverso
                    for(int j = celda.size()-1; j>=0 ; j--){
                        pila.push(celda.get(j));
                    }
                }
            } else {
                System.out.println("\033[91mSe encontraron errores\033[0m");
                return false;
            }
        }

        return true;
    }

}
