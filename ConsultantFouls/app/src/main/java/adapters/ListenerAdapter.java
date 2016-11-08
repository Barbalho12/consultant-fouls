package adapters;

import com.android.volley.Response;

public abstract class ListenerAdapter implements Response.Listener<String> {
    private int id;
    private String nome;

    public ListenerAdapter(int id, String nome){
        this.id = id;
        this.nome = nome;
    }

    @Override
    public void onResponse(String response) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
