package com.apress.gerber.reminders;

/**
 * Created by Administrator on 2018-07-31.
 */


public class Reminder {
    private int mid;
    private  String mContent;
    private  int mImportant;
    public  Reminder(int id,String content,int important)
    {
        mid=id;
        mImportant=important;
        mContent=content;
    }
    public int getId()
    {return mid;}
    public  void setid(int id)
    {
        mid=id;
    }
    public int getImportant()
    {
        return mImportant;
    }
    public  void setImportant(int important)
    {
        mImportant=important;
    }
    public String getContent(){return mContent;}
    public void setContent(String content)
    {
        mContent=content;
    }
}

