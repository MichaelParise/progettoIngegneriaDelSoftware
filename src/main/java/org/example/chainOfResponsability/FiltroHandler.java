package org.example.chainOfResponsability;

import org.example.builderPattern.Libro;

import java.util.List;

public abstract class FiltroHandler implements Handler{
    private Handler next;
    private String parametro;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    @Override
    public List<Libro> filtra(List<Libro> listaLibri){
        List<Libro> filtrata = applicaFiltro(listaLibri, parametro);
        if(next != null)
            return next.filtra(filtrata);
        return filtrata;
    }

    protected abstract List<Libro> applicaFiltro(List<Libro> listaLibri,String parametro);

}
