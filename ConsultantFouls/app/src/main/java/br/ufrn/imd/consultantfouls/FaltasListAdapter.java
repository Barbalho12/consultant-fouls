package br.ufrn.imd.consultantfouls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class FaltasListAdapter extends ArrayAdapter<DisciplinaFaltas> {
    public FaltasListAdapter(Context context, int textViewResourceId, List<DisciplinaFaltas> objects) {
        super(context, textViewResourceId, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_faltas, null);
        }

        DisciplinaFaltas item = getItem(position);

        if (item!= null) {

            TextView nome_disciplina = (TextView) view.findViewById(R.id.nome_disciplina);
            String nome_disciplinaS[] = item.getNomeDisciplina().trim().split(" ");
            String result = "";

            for (int i = 0; i < nome_disciplinaS.length; i++) {
                if (nome_disciplinaS[i].length() == 1){
                    result += nome_disciplinaS[i].toLowerCase()+" ";
                    continue;
                }
                result += nome_disciplinaS[i].charAt(0)+nome_disciplinaS[i].substring(1).toLowerCase() + " ";
            }

            nome_disciplina.setText( result );

            int faltas = ( item.getQntdAulasMinistradas() -  item.getQntdAulasAssistidas());
            int maxfaltas = (( item.getCargaHoraria()*60)/45)/4 -1;

            TextView info_faltas = (TextView) view.findViewById(R.id.info_faltas);
            info_faltas.setText(faltas+"/"+maxfaltas + "  faltas");
            ProgressBar progressBar = progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setIndeterminate(false);

            if(faltas/maxfaltas < 0.5 && faltas/maxfaltas > 0.0){
                progressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                progressBar.setMax(maxfaltas);
                progressBar.setProgress(faltas);
            }else if(faltas/maxfaltas < 1.0){
                progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                progressBar.setMax(maxfaltas);
                progressBar.setProgress(faltas);
            }else{
                progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                progressBar.setMax(faltas);
                progressBar.setProgress(faltas);
            }

            TextView last_update = (TextView) view.findViewById(R.id.last_update);
            String last_updateS = item.getDataUltimaAtualizacao();
            if(!last_updateS.trim().equals("null")){
                last_update.setText("Atualizado em "+last_updateS);
            }else{
                last_update.setText("ainda não foi atualizado");
            }
        }
        return view;
    }

}
