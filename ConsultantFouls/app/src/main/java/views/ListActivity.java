package views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import adapters.FaltasListAdapter;
import model.DisciplinaFaltas;
import br.ufrn.imd.consultantfouls.R;

public class ListActivity extends AppCompatActivity {

    private ListView faltasListView;
    private FaltasListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List<DisciplinaFaltas> fornecedores = (List<DisciplinaFaltas>)
                getIntent().getSerializableExtra("listafaltas");

        faltasListView = (ListView) findViewById(R.id.disciplinaListView);
        adapter = new FaltasListAdapter(this,
                R.layout.list_faltas, fornecedores);

        faltasListView.setAdapter(adapter);
    }
}
