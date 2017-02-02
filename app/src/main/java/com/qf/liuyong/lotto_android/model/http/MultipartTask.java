package com.qf.liuyong.lotto_android.model.http;

import android.os.Handler;
import android.os.Message;

import com.qf.liuyong.lotto_android.model.http.exception.RequestError;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class MultipartTask implements Runnable{
    private RequestInfo requestInfo = null;
    private RequestConfig config = null;
    private ResponseInfo responseInfo = null;

    private String requestUrl = null;
    private Map<String, String> requestParams = null;
    private HttpURLConnection mConnection;
    private Handler mHandler = new Handler();

    private long total;
    private long current;
    private int progress;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            long current = (Long)msg.obj;
            int progress = msg.arg1;
            if (responseInfo.getUpdateListener() != null) {
                responseInfo.getUpdateListener().onUpdateProgress(current, total, progress);
            }
        }
    };

    public MultipartTask(RequestInfo requestInfo, ResponseInfo responseInfo, RequestConfig config) {
        this.requestInfo = requestInfo;
        if (requestInfo.files == null)
            throw new RuntimeException();
        this.responseInfo = responseInfo;
        this.config = config;
        requestUrl = requestInfo.url;
        requestParams = new HashMap<String, String>();
        //先放默认参数
        if (config.defaultParams != null && requestInfo.withDefaultParams) {
            requestParams.putAll(config.defaultParams);
        }
        //后放请求参数，保证请求参数优先
        if (requestInfo.params != null) {
            requestParams.putAll(requestInfo.params);
        }
        total = 0;
        current = 0;
        progress = 0;
        Map<String, File> fileMap = requestInfo.files;
        for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
            total += fileEntry.getValue().length();
        }
    }


    private void writeFile(String name, File file, DataOutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:image/png\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        out.write(head);
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
            current += bytes;
            progress = (int) (current * 100 / total);
//            System.out.println(current + " " + total + " " + progress);

            Message message = handler.obtainMessage();
            message.obj = current;
            message.arg1 = progress;

            handler.sendMessage(message);
        }
        in.close();
    }

    @Override
    public void run() {
        String BOUNDARY = "----------" + System.currentTimeMillis();
        try {
            URL url = new URL(requestUrl);
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setRequestMethod("POST");
            mConnection.setDoInput(true);
            mConnection.setDoOutput(true);
            mConnection.setUseCaches(false);
            mConnection.setRequestProperty("Connection", "Keep-Alive");
            mConnection.setRequestProperty("Charset", "UTF-8");
            mConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            StringBuilder sb = new StringBuilder();
            DataOutputStream out = new DataOutputStream(mConnection.getOutputStream());
            if (requestParams != null) {
                int i = 0;
                sb.delete(0, sb.length());
                sb.append("\r\n");
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    sb.append("--" + BOUNDARY + "\r\n");
                    sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
                    if (i == requestParams.size() - 1) {
                        sb.append(value + "");
                    } else {
                        sb.append(value + "\r\n");
                    }
                    i++;
                }
                out.write((sb.toString()).getBytes());
            }

            sb.delete(0, sb.length());

            Set<Map.Entry<String, File>> set = requestInfo.files.entrySet();
            int i = 0;
            for (Map.Entry<String, File> e : set) {


                byte[] foot = null;
                foot = ("\r\n--" + BOUNDARY + "\r\n").getBytes("utf-8");
                out.write(foot);
                File file = e.getValue();
                writeFile(e.getKey(), file, out);

                if (i == requestInfo.files.size() - 1) {
                    foot = ("\r\n--" + BOUNDARY + "--").getBytes("utf-8");
                    out.write(foot);
                } else {
                }
                i++;
            }


            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    mConnection.getInputStream()));
            sb.delete(0, sb.length());
            String line = null;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                sb.append(line);
            }

            int code = mConnection.getResponseCode();
            if (code != 200) {
                throw new Exception(line);
            }
            final String response = sb.toString();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (responseInfo.getListener() != null) {
                        responseInfo.getListener().onResponse(response);
                    }
                    responseInfo.setNetworkResponse(response);

                    requestInfo.requestClient.execute(new DataHandlerTask(responseInfo, false));
                }
            });
            return;
        } catch (final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (responseInfo.getListener() != null) {
                        responseInfo.getListener().onError(new RequestError(e));
                    }
                }
            });
        }
    }
}
