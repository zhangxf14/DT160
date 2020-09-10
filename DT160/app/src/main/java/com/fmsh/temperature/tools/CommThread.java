package com.fmsh.temperature.tools;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.fmsh.temperature.util.LogUtil;
import com.fmsh.temperature.util.MyConstant;
import com.fmsh.temperature.util.NFCUtils;
import com.fmsh.temperature.util.UIUtils;

import java.io.IOException;

/**
 * Created by wyj on 2018/7/4.
 */
public class CommThread extends Thread {
    private Tag mTag;
    private Handler mHandler;
    private int type;
    private WorkerThreadHandler mWorkerThreadHan;
    public static Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public CommThread() {


    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        mWorkerThreadHan = new WorkerThreadHandler();
        Looper.loop();

    }

    public Handler getWorkerThreadHan() {
        return mWorkerThreadHan;
    }

    private static class WorkerThreadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int type = msg.what;
            int arg1 = msg.arg1;
            LogUtil.d(arg1 + "");
            if (type != -1) {
                if (msg.obj instanceof Tag) {

                    Tag tag = (Tag) msg.obj;
                    if (MyConstant.MEASURETYPE == 0) {
                        String[] techList = tag.getTechList();
                        for (String tech : techList) {
                            LogUtil.d(tech);
                            if (tech.contains("NfcV")) {
                                if (arg1 == 0) {

                                    NFCUtils.readNfcVData(tag, type);
                                } else {
                                    if(type == 11){

                                        NFCUtils.startV(tag, 11);
                                        NFCUtils.startV(tag, 28);
                                        NFCUtils.startV(tag, 34);
                                    }else {
                                        NFCUtils.startV(tag,type);
                                    }
                                }
                            }
                            if (tech.contains("NfcA")) {
                                if (arg1 == 0) {
                                    NFCUtils.readNfcAData(tag, type);
                                } else {
                                    if(type == 11){
                                        NFCUtils.startA(tag, 11);
                                        NFCUtils.startA(tag, 28);
                                        NFCUtils.startA(tag, 34);
                                    }else {
                                        NFCUtils.startA(tag,type);
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
    }


}
