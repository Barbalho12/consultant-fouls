package br.ufrn.imd.consultantfouls;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultActivity extends AppCompatActivity {

    private String jsonResponse;
    private ProgressDialog pDialog;
    //private TextView texto;

    private static final String ACESS_VINCULO_USER = "http://apitestes.info.ufrn.br/ensino-services/services/consulta/listavinculos/usuario";
    private String urlJsonObjC = null;
    private int idDiscente = 0;
    private List<Integer> idsTurmas = new ArrayList<>();
    private List<String> nomesTurmas = new ArrayList<>();
    private List<String> viewInfor = new ArrayList<>();
    private List<String> infoUserPerTurma = new ArrayList<>();
    private List<String> enderecos = new ArrayList<>();
    private int qtdEnd = 0;
    private RequestQueue queue = null;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
//        pDialog = new ProgressDialog(this);
//        pDialog.setCancelable(false);

        listView = (ListView) findViewById (R.id.disciplinaListView);

        reqJson();
    }
    void test(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, viewInfor);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue = ((String) listView.getItemAtPosition(position)) ;
                Toast.makeText(getApplicationContext(), infoUserPerTurma.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    Response.Listener<String> responseListTurmas = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            try {
                JSONArray turmas = new JSONArray(response);
                for (int i = 0; i < turmas.length(); i++) {
                    JSONObject jsonObject = turmas.getJSONObject(i);
                    String nomeComponente = jsonObject.getString("nomeComponente");
                    int idTurma = jsonObject.getInt("id");
                    idsTurmas.add(idTurma);
                    nomesTurmas.add(nomeComponente);
                    enderecos.add("https://apitestes.info.ufrn.br/ensino-services/services/consulta/frequenciadiscente/"+idDiscente+"/"+idTurma);
                }
                queue.add(createSringRquest(enderecos.get(qtdEnd), responseFrequenciaAluno));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.Listener<String> responseFrequenciaAluno = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                VolleyLog.d("SAIDA", jsonObject.length());
                int qntdAM = jsonObject.getInt("qntdAulasMinistradas");
                int qntdAA = jsonObject.getInt("qntdAulasAssistidas");
                int cgHor = jsonObject.getInt("cargaHoraria");
                int cgHorM = jsonObject.getInt("cargaHorariaMinistrada");
                String dtUltAt = jsonObject.getString("dataUltimaAtualizacao");
                int faltas = (qntdAM - qntdAA);
                int maxfaltas = ((cgHor*60)/45)/4 -1;
                infoUserPerTurma.add(faltas+ "/" + maxfaltas + " falt/max");
                viewInfor.add(nomesTurmas.get(qtdEnd) + "\n"+ faltas+ "/" + maxfaltas + " falt/max");
                if(qtdEnd<enderecos.size()-1)
                    queue.add(createSringRquest(enderecos.get(qtdEnd++), responseFrequenciaAluno));
                else{
                    test();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void reqJson(){

        queue = Volley.newRequestQueue(this);

        Response.Listener<String> responseInfoVinculoUser = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray discentes = jsonObject.getJSONArray("discentes");
                    JSONObject discente =  (JSONObject) discentes.get(0);

                    int idUsuario = discente.getInt("idUsuario");
                    idDiscente = discente.getInt("id");
                    int matricula = discente.getInt("matricula");

                    urlJsonObjC = "http://apitestes.info.ufrn.br/ensino-services/services/consulta/turmas/usuario/discente/"+idDiscente;
                    queue.add(createSringRquest(urlJsonObjC, responseListTurmas));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

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

}

