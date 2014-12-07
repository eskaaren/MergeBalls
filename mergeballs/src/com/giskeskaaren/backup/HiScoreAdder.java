package com.giskeskaaren.backup;

import android.content.Context;

/**
 * Creator: Eivin Giske Skaaren
 * Email: eskaaren@yahoo.no
 * Date: 5/16/12
 * Time: 12:09 AM
 */
public class HiScoreAdder {
    private SQLiteAdapter mySQLiteAdapter;
    private String[] hiscores = new String[] {};
    private Context context;
    private double lastscore;
    private String name;

    public HiScoreAdder(String name, Context c) {
        this.context = c;
        this.name = name;
    }

    public void addScore(double score, String dbname) {
        this.lastscore = score;

        //Create the DB if needed
        mySQLiteAdapter = new SQLiteAdapter(context, dbname);
        mySQLiteAdapter.openToWrite();
        //mySQLiteAdapter.deleteAll();  //Uncomment to clear DB
        if (mySQLiteAdapter.queueAll().equals("")) {
            mySQLiteAdapter.insert("1.\t\t\t\t----------\t\t\t\t10000");
            mySQLiteAdapter.insert("2.\t\t\t\t----------\t\t\t\t10000");
            mySQLiteAdapter.insert("3.\t\t\t\t----------\t\t\t\t10000");
            mySQLiteAdapter.insert("4.\t\t\t\t----------\t\t\t\t10000");
            mySQLiteAdapter.insert("5.\t\t\t\t----------\t\t\t\t10000");
        }
        mySQLiteAdapter.close();

        //Read from DB
        mySQLiteAdapter = new SQLiteAdapter(context, dbname);
        mySQLiteAdapter.openToRead();
        String contentRead = mySQLiteAdapter.queueAll();
        hiscores = contentRead.split("\n");
        mySQLiteAdapter.close();

        if (lastscore > 0) {
            //Check against last score and replace if it should in on the list.
            String [] tmp, tmp2;

            for (int i = 0; i < hiscores.length; ++i) {
                tmp = hiscores[i].split("\t\t\t\t");


                if (Double.parseDouble(tmp[2]) > lastscore) {

                    for (int j = hiscores.length-1; j > i; --j) {
                        tmp2 = hiscores[j-1].split("\t\t\t\t");
                        hiscores[j] = j+1+".\t\t\t\t"+tmp2[1]+"\t\t\t\t"+tmp2[2];
                    }
                    for (int nl = name.length(); nl < 10; ++nl) {
                        name += " ";
                    }
                    tmp[1] = name;
                    tmp[2] = Double.toString(lastscore);
                    hiscores[i] = i+1+".\t\t\t\t"+tmp[1]+"\t\t\t\t"+tmp[2];
                    i = hiscores.length;
                }
            }

            mySQLiteAdapter.openToWrite();
            mySQLiteAdapter.deleteAll();
            for (int i = 0; i < hiscores.length; ++i) {
                mySQLiteAdapter.insert(hiscores[i]);
            }
        }
        mySQLiteAdapter.close();

    }


    public String[] getHiscores(String dbname) {
        addScore(0, dbname); //read in correct db
        return hiscores;
    }

}
