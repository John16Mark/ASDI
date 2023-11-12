import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ASDI implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    private final Stack<Object> pila = new Stack<>();

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

    private final ArrayList<ArrayList<Object>> tabla = new ArrayList<>();
    ArrayList<NoTerminal> filas = new ArrayList<>(Arrays.asList(NoTerminal.Q,NoTerminal.D,NoTerminal.P,NoTerminal.A,NoTerminal.A1,NoTerminal.A2,NoTerminal.A3,NoTerminal.T,NoTerminal.T1,NoTerminal.T2,NoTerminal.T3));
    ArrayList<TipoToken> columnas = new ArrayList<>(Arrays.asList(TipoToken.SELECT,TipoToken.FROM,TipoToken.DISTINCT,TipoToken.ASTERISCO,TipoToken.PUNTO,TipoToken.COMA,TipoToken.IDENTIFICADOR, TipoToken.EOF));

    public ASDI(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);

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
        /*
        for(int k=0; k<11; k++){
            for(int j=0; j<8; j++){
                if(tabla.get(k).get(j) !=null) {
                    System.out.print("\033[95m");
                }
                System.out.print(tabla.get(k).get(j));
                System.out.print("\033[0m");
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        */
    }

    @Override
    public boolean parse() {
        pila.push(TipoToken.EOF);
        pila.push(NoTerminal.Q);

        while(i<tokens.size()){
            //System.out.println("Comparando: "+pila.peek()+" - "+tokens.get(i).tipo);
            if(pila.peek() instanceof TipoToken && pila.peek() == tokens.get(i).tipo){
                //System.out.println("Iguales: "+pila.peek());
                if(pila.peek() == TipoToken.EOF && tokens.get(i).tipo == TipoToken.EOF){
                    System.out.println("\033[94mAnálisis Sintáctico correcto\033[0m");
                    return true;
                }
                i++;
                pila.pop();
            }
            else if(pila.peek() instanceof NoTerminal){
                int fila = filas.indexOf(pila.peek());
                int columna = columnas.indexOf(tokens.get(i).tipo);

                if(tabla.get(fila).get(columna) == null){
                    System.out.println("\033[91mSe encontraron errores\033[0m");
                    return false;
                } else {
                    pila.pop();
                    ArrayList<Object> celda = (ArrayList<Object>) tabla.get(fila).get(columna);
                    for(int j = celda.size()-1; j>=0 ; j--){
                        //System.out.print(celda.get(j));
                        pila.push(celda.get(j));
                    }
                    //System.out.print("\n");
                }
            } else {
                System.out.println("\033[91mSe encontraron errores\033[0m");
                return false;
            }
        }

        return true;
    }

}
