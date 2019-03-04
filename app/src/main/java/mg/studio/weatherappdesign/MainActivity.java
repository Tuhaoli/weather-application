package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        //to do when the button is clicked
        new DownloadUpdate().execute();
        //((TextView)findViewById(R.id.temperature_of_the_day)).setText("27");
        Toast.makeText(MainActivity.this, "Update Complete!", Toast.LENGTH_SHORT).show();//安卓提示信息
        /*
        try {
            JSONObject root = new JSONObject();//实例一个JSONObject对象
            root.put("cat","it");//对其添加一个数据

            JSONArray languages = new JSONArray();//实例一个JSON数组
            JSONObject lan1 = new JSONObject();//实例一个lan1的JSON对象
            lan1.put("id",1);//对lan1对象添加数据
            lan1.put("ide","Eclipse");//对lan1对象添加数据
            lan1.put("name","Java");//对lan1对象添加数据
            JSONObject lan2 = new JSONObject();//实例一个lan2的JSON对象
            lan2.put("id",2);//对lan2对象添加数据
            lan2.put("ide","XCode");//对lan2对象添加数据
            lan2.put("name","Swift");//对lan2对象添加数据
            JSONObject lan3 = new JSONObject();//实例一个lan3的JSON对象
            lan3.put("id",3);//对lan3对象添加数据
            lan3.put("ide","Visual Studio");//对lan3对象添加数据
            lan3.put("name","C#");//对lan3对象添加数据
            languages.put(0,lan1);//将lan1对象添加到JSON数组中去，角标为0
            languages.put(1,lan2);//将lan2对象添加到JSON数组中去，角标为1
            languages.put(2,lan3);//将lan3对象添加到JSON数组中去，角标为2

            root.put("languages",languages);//然后将JSON数组添加到名为root的JSON对象中去

            FileOutputStream fos = new FileOutputStream(file);//创建一个文件输出流
            fos.write(root.toString().getBytes());//将生成的JSON数据写出
            fos.close();//关闭输出流
            Toast.makeText(getApplicationContext(),"创建成功！",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});*/
    }

    //发送请求，获取网络数据
    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://39.107.228.154:4000/Courses/forecast.json";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();//输出结果
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                //将json文本里面的内容全部转换储存到了line字符串中，接下来只需要对line进行get等一系列处理
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    String[] temp = new String[5];
                    String[] weat = new String[5];
                    String[] date = new String[5];
                    String loca = "Unknown Location";
                    String line1 = line;
                    for(int i=0; i<5; i++){
                        line1 = line1.substring(line1.indexOf("temp") + 6);//substring
                        temp[i] = String.valueOf((int)(Double.valueOf(line1.substring(0, line1.indexOf(","))) + 0.5));
                        line1 = line1.substring(line1.indexOf("main") + 7);
                        weat[i] = line1.substring(0, line1.indexOf('"'));
                        line1 = line1.substring(line1.indexOf("dt_txt") + 9);
                        date[i] = line1.substring(0, 10);

                        for(int j=0; j<7; j++)
                            line1 = line1.substring(line1.indexOf("dt_txt") + 9);
                    }

                line1 = line1.substring(line1.indexOf("name") + 7);
                loca = line1.substring(0, line1.indexOf('"'));
                line1 = line1.substring(line1.indexOf("country") + 10);
                loca += "," + line1.substring(0, line1.indexOf('"'));
                String result = "";
                   for(int i=0;i<5;i++)
                    result += date[i] + "," + weat[i] + "," + temp[i] + ",";

                    buffer.append(result + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();//将temp输出的结构体，成若干个字符串

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override


/*
    private void parseJSONWithJSONObject(String JsonData) {
             try
             {
             JSONArray jsonArray = new JSONArray(jsonData);
             for (int i=0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String temp = jsonObject.getString("temperature");
                    String main = jsonObject.getString("weather description");
                    //String version = jsonObect.getString("version");
                    System.out.println("temperature" + temp + ";weather description" + main);
                  }
                }
             catch (Exception e)
            {
                    e.printStackTrace();
                 }
        }*/
        protected void onPostExecute(String msg) {
            //Update the temperature displayed
            String[] items = msg.split(",");
            String[] weeklong = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
            String[] weekshort = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
            ((TextView) findViewById(R.id.today)).setText(weeklong[getDayofWeek(items[0])]);
            checkWeatherBitmap(items[1], R.id.img_weather_condition);
            ((TextView) findViewById(R.id.tv_location)).setText(items[15]+", "+items[16]);
            ((TextView) findViewById(R.id.tv_date)).setText(items[0]);
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(items[2]);
            checkWeatherBitmap(items[4], R.id.img_weather_condition2);
            ((TextView) findViewById(R.id.day2)).setText(weekshort[getDayofWeek(items[3])]);
            checkWeatherBitmap(items[7], R.id.img_weather_condition3);
            ((TextView) findViewById(R.id.day3)).setText(weekshort[getDayofWeek(items[6])]);
            checkWeatherBitmap(items[10], R.id.img_weather_condition4);
            ((TextView) findViewById(R.id.day4)).setText(weekshort[getDayofWeek(items[9])]);
            checkWeatherBitmap(items[13], R.id.img_weather_condition5);
            ((TextView) findViewById(R.id.day5)).setText(weekshort[getDayofWeek(items[12])]);
        }

    }
    protected void checkWeatherBitmap(String msg, int targetid){
        if (msg.equals("Clear"))
            ((ImageView)findViewById(targetid)).setImageResource(R.drawable.sunny_small);
        else if (msg.equals("Clouds"))
            ((ImageView)findViewById(targetid)).setImageResource(R.drawable.partly_sunny_small);
        else if (msg.equals("Rain"))
            ((ImageView)findViewById(targetid)).setImageResource(R.drawable.rainy_small);
        else
            ((ImageView)findViewById(targetid)).setImageResource(R.drawable.notification);
    }

    private int getDayofWeek(String dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK)-1;
    }
}

