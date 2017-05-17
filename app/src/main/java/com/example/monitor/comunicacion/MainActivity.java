package com.example.monitor.comunicacion;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvPagina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPagina=(TextView)this.findViewById(R.id.tvPagina);
    }
    public void cargar(View v){
        ComunicacionTask com=new ComunicacionTask();
        com.execute("http://www.aemet.es/xml/municipios/localidad_13001.xml");
    }
    private class ComunicacionTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String cadena="";
            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();
                //String s;
                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);
                NodeList listanombre = doc.getElementsByTagName("nombre");
                cadena += "Localidad: " + listanombre.item(0).getTextContent() + "\n";
                NodeList listaPrecip = doc.getElementsByTagName("prob_precipitacion");
                NodeList listaTemp = doc.getElementsByTagName("estado_cielo");
                double media = 0;
                double media2 = 0;
                for (int i = 0; i < listaPrecip.getLength(); i++) {
                    String valor = listaPrecip.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media += Double.parseDouble(valor);
                    media = media / listaPrecip.getLength();
                    cadena += " Media prob. precipitacion:" + media;
                }
                for (int i = 0; i < listaTemp.getLength(); i++) {
                    String valor = listaTemp.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media2 += Double.parseDouble(valor);
                    media2 = media2 / listaTemp.getLength();
                    cadena += " Temperatura" + media2;
                }
            }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                return cadena;
            }

               /* BufferedReader bf=new BufferedReader(
                        new InputStreamReader(is));
                while((s=bf.readLine())!= null){
                    pagina+=s;
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            return pagina;
        }*/
        @Override
        protected void onPostExecute(String result){

            tvPagina.setText(result);
        }

    }
}
