package moduloIDF;

/**
 *
 * @author Nicoyarce
 */
public class ValoresEnergeticos {

    private String titulo;
    private double valor;

    public ValoresEnergeticos(String titulo, double valor) {
        this.titulo = titulo;
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = Double.parseDouble(valor);
    }

    public void vaciar() {
        this.titulo = "";
        this.valor = 0.0;
    }
}
