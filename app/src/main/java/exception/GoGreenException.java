package exception;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tejal Shah.
 */
public class GoGreenException extends Exception {

    public enum GoGreenError {
        InvalidErrorCode(0, "Invalid Error Code:"),
        EventReadingException(1, "Green Event Exception");

        private int errNum;
        private String errMsg;

        private GoGreenError(final int errNm, String errMg){
            errNum = errNm;
            errMsg = errMg;
        }

        public int getErrNum() {
            return errNum;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public static GoGreenError findErrorByValue(int errNm){
            for (GoGreenError g: GoGreenError.values()){
                if(g.getErrNum()==errNm){
                    return g;
                }
            }
            return null;
        }
    }

    public GoGreenError error;
    public int errorNumber;
    public String errorContext = "";

    public GoGreenException(){
        super();
        error = GoGreenError.findErrorByValue(0);
        errorNumber = 0;
    }

    public GoGreenException(int errno, Context context){
        super();
        if (errno<0 || errno>6) errno = 0;
        error = GoGreenError.findErrorByValue(errno);
        errorNumber = errno;
        logGreenError(context, "Err Type");
    }

    public GoGreenException(int errno, String ctxt, Context context){
        super();
        if (errno<0 || errno>6) errno = 0;
        error = GoGreenError.findErrorByValue(errno);
        errorNumber = errno;
        this.errorContext = (ctxt!=null?ctxt:"");
        logGreenError(context, "Err Type");
    }

    public String fix(int errno) {

        if (0<=errno && errno<=5){
            GoGreenExceptionFixer fixer = new GoGreenExceptionFixer();

            switch(errno){
                case 1: return fixer.fix(1);
                case 2: return fixer.fix(2);
                case 3: return fixer.fix(3);
                case 4: return fixer.fix(4);
                case 5: return fixer.fix(5);
                default: return "Incorrect Errorcode--cannot resolve error";
            }
        }

        return "Incorrect Errorcode--cannot resolve error";
    }

    private void logGreenError(Context c, String type) {
        File folder = c.getCacheDir();
        File f = new File(folder, "ErrorLog.txt");
        try {
            if(!f.exists() || f.isDirectory()){
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
            BufferedWriter buff = new BufferedWriter(fw);
            buff.write(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
            buff.newLine();
            buff.write("Error in: " + type);
            buff.newLine();
            buff.close();

            //Check ErrorLog Content
            File fol = c.getCacheDir();
            File fi = new File(fol, "ErrorLog.txt");
            FileInputStream fis = new FileInputStream(fi);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("#PrintErrorLog", line);
            }
            buff.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


