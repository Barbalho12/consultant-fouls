package br.ufrn.imd.consultantfouls;

/**
 * Created by Barreto on 07/11/2016.
 */

public class DisciplinaFaltas {
    private int qntdAulasMinistradas;
    private int qntdAulasAssistidas;
    private int cargaHoraria;
    private int cargaHorariaMinistrada;
    private String dataUltimaAtualizacao;

    public DisciplinaFaltas() {
    }

    public DisciplinaFaltas(int qntdAulasMinistradas, int qntdAulasAssistidas, int cargaHoraria, int cargaHorariaMinistrada, String dataUltimaAtualizacao) {
        this.qntdAulasMinistradas = qntdAulasMinistradas;
        this.qntdAulasAssistidas = qntdAulasAssistidas;
        this.cargaHoraria = cargaHoraria;
        this.cargaHorariaMinistrada = cargaHorariaMinistrada;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public int getQntdAulasMinistradas() {
        return qntdAulasMinistradas;
    }

    public void setQntdAulasMinistradas(int qntdAulasMinistradas) {
        this.qntdAulasMinistradas = qntdAulasMinistradas;
    }

    public int getQntdAulasAssistidas() {
        return qntdAulasAssistidas;
    }

    public void setQntdAulasAssistidas(int qntdAulasAssistidas) {
        this.qntdAulasAssistidas = qntdAulasAssistidas;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public int getCargaHorariaMinistrada() {
        return cargaHorariaMinistrada;
    }

    public void setCargaHorariaMinistrada(int cargaHorariaMinistrada) {
        this.cargaHorariaMinistrada = cargaHorariaMinistrada;
    }

    public String getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(String dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
}
