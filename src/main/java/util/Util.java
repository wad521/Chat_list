package util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    public static String readBody(HttpServletRequest req){
        // req.getContentLength()获取到body的长度，单位是字节
        int contentLength = req.getContentLength();
        // 使用buffer来保存读到的body内容
        byte[] buffer = new byte[contentLength];
        //这样把req.getInputStream()用try括起来就不用显示的写close，可以自动调用close
        // 此处的inputStream指的是字节流，读写数据单位都是一个一个字节
        // 注意体会字符流字节流的差异
        try(InputStream inputStream = req.getInputStream()){
            inputStream.read(buffer,0,contentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buffer);
    }
}
