package moduloIDF;

/**
 *
 * @author Nicoyarce
 */
public class ValorEnergetico {

    private String titulo;
    private double valor;
    private boolean seleccionado;

    public ValorEnergetico(String titulo, String valor, boolean eleccion) {
        this.titulo = titulo;
        this.valor = Double.parseDouble(valor);
        this.seleccionado = eleccion;
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

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccion(boolean eleccion) {
        this.seleccionado = eleccion;
    }

    public void vaciar() {
        this.titulo = "";
        this.valor = 0.0;
    }

    @Override
    public String toString() {
        return titulo + ": " + valor;
    }

}
