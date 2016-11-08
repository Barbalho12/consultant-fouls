package br.ufrn.imd.consultantfouls;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultActivity extends AppCompatActivity {

    private static final String ACESS_VINCULO_USER = "http://apitestes.info.ufrn.br/ensino-services/services/consulta/listavinculos/usuario";

    private List<DisciplinaFaltas> listDisciplinaFaltas = new ArrayList<>();
    private int idDiscente = 0;
    private int qtd_turmas = 0;
    private RequestQueue queue = null;

    ProgressBar progressBar;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        listView = (ListView) findViewById (R.id.disciplinaListView);
        progressBar.setIndeterminate(false);
        progressBar.setMax(6);

        searchDisciplinasFaltas();
    }

    private void searchFoults(int idDiscente, int idTTurma, String nome){
        String endereco = "https://apitestes.info.ufrn.br/ensino-services/services/consulta/frequenciadiscente/"+idDiscente+"/"+idTTurma;
        ListenerAdapter responsListTurmas = new ListenerAdapter(idTTurma, nome) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int qntdAM = jsonObject.getInt("qntdAulasMinistradas");
                    int qntdAA = jsonObject.getInt("qntdAulasAssistidas");
                    int cgHor = jsonObject.getInt("cargaHoraria");
                    int cgHorM = jsonObject.getInt("cargaHorariaMinistrada");
                    String dtUltAt = jsonObject.getString("dataUltimaAtualizacao");
                    listDisciplinaFaltas.add(new DisciplinaFaltas(getNome(), qntdAM, qntdAA, cgHor, cgHorM, dtUltAt));
                    VolleyLog.d("Quantidade - "+listDisciplinaFaltas.size()+" - "+qtd_turmas, qtd_turmas);
                    progressBar.setProgress(listDisciplinaFaltas.size());
                    if(listDisciplinaFaltas.size()==qtd_turmas){
                        Intent intent  = new Intent(ResultActivity.this, ListActivity.class);
                        intent.putExtra("listafaltas", (Serializable) listDisciplinaFaltas);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        queue.add(createSringRquest(endereco, responsListTurmas));
    }

    private void searchDisciplinasFaltas(){
        queue = Volley.newRequestQueue(this);
        queue.add(createSringRquest(ACESS_VINCULO_USER, responseInfoVinculoUser));
    }

    private Request createSringRquest(String urlJsonObj, Response.Listener<String> responseInfoUser) {
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlJsonObj, responseInfoUser, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer "+ OAuthTokenRequest.getInstance().getCredential().getAccessToken();
                headers.put("Authorization", auth);
                VolleyLog.d("Authorization"+auth, auth);
                return headers;
            }
        };
        return stringRequest1;
    }

    private Response.Listener<String> responseListTurmas = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            try {
                JSONArray turmas = new JSONArray(response);
                qtd_turmas = turmas.length();
                VolleyLog.d("Quantidade - "+qtd_turmas, qtd_turmas);
                for (int i = 0; i < turmas.length(); i++) {
                    JSONObject jsonObject = turmas.getJSONObject(i);
                    String nomeComponente = jsonObject.getString("nomeComponente");
                    int idTurma = jsonObject.getInt("id");
                    searchFoults(idDiscente, idTurma, nomeComponente);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.Listener<String> responseInfoVinculoUser = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray discentes = jsonObject.getJSONArray("discentes");
                JSONObject discente =  (JSONObject) discentes.get(0);
                idDiscente = discente.getInt("id");
                String consultTumasURL = "http://apitestes.info.ufrn.br/ensino-services/services/consulta/turmas/usuario/discente/"+idDiscente;
                queue.add(createSringRquest(consultTumasURL, responseListTurmas));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



}

