package mpqa4lg.opin.io;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * [4Android]
 */
public class ReaderUtils4Android
{
    public static AssetManager assetManager;

    private static final int BUFFERSIZE = 16384;
    
    public static ArrayList<String> readFileToLines(String f) {
        return readFileToLines(f, Charset.forName("UTF-8"));
    }
    
    public static ArrayList<String> readFileToLinesIgnoreEmpty(String f) {
        return readFileToLinesIgnoreEmpty(f, Charset.forName("UTF-8"));
    }
    
    public static ArrayList<String> readFileToLinesIgnoreEmptyAndComments(String f) {
        return readFileToLinesIgnoreEmptyAndComments(f, Charset.forName("UTF-8"));
    }
    
    public static String readFileToString(String f) {
        return readFileToString(f, Charset.forName("UTF-8"));
    }
    
    public static String readFileToString(String f, final Charset cs) {
        final StringBuilder sb = new StringBuilder();
        InputStreamReader sReader = null;
        BufferedReader bReader = null;
        try {
            final char[] buffer = new char[4096];
            sReader = new InputStreamReader(ReaderUtils4Android.assetManager.open(f), cs);
            bReader = new BufferedReader(sReader, 16384);
            int read = 0;
            while ((read = bReader.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, read);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            try {
                if (sReader != null) {
                    sReader.close();
                }
                if (bReader != null) {
                    bReader.close();
                }
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        try {
            if (sReader != null) {
                sReader.close();
            }
            if (bReader != null) {
                bReader.close();
            }
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        return sb.toString();
    }
    
    public static ArrayList<String> readFileToLines(String f, final Charset cs) {
        InputStreamReader sReader = null;
        BufferedReader bReader = null;
        final ArrayList<String> output = new ArrayList<String>();
        try {
            sReader = new InputStreamReader(ReaderUtils4Android.assetManager.open(f), cs);
            bReader = new BufferedReader(sReader, 16384);
            String line;
            while ((line = bReader.readLine()) != null) {
                output.add(line);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            try {
                if (sReader != null) {
                    sReader.close();
                }
                if (bReader != null) {
                    bReader.close();
                }
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        try {
            if (sReader != null) {
                sReader.close();
            }
            if (bReader != null) {
                bReader.close();
            }
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        return output;
    }
    
    public static ArrayList<String> readFileToLinesIgnoreEmpty(String f, final Charset cs) {
        InputStreamReader sReader = null;
        BufferedReader bReader = null;
        final ArrayList<String> output = new ArrayList<String>();
        try {
            sReader = new InputStreamReader(ReaderUtils4Android.assetManager.open(f), cs);
            bReader = new BufferedReader(sReader, 16384);
            String line;
            while ((line = bReader.readLine()) != null) {
                line = line.trim();
                if (!line.equals("")) {
                    output.add(line);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            try {
                if (sReader != null) {
                    sReader.close();
                }
                if (bReader != null) {
                    bReader.close();
                }
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        try {
            if (sReader != null) {
                sReader.close();
            }
            if (bReader != null) {
                bReader.close();
            }
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        return output;
    }
    
    public static ArrayList<String> readFileToLinesIgnoreEmptyAndComments(String f, final Charset cs) {
        InputStreamReader sReader = null;
        BufferedReader bReader = null;
        final ArrayList<String> output = new ArrayList<String>();
        try {
            sReader = new InputStreamReader(ReaderUtils4Android.assetManager.open(f), cs);
            bReader = new BufferedReader(sReader, 16384);
            String line;
            while ((line = bReader.readLine()) != null) {
                line = line.trim();
                if (!line.equals("") && !line.startsWith("#")) {
                    output.add(line);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            try {
                if (sReader != null) {
                    sReader.close();
                }
                if (bReader != null) {
                    bReader.close();
                }
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        try {
            if (sReader != null) {
                sReader.close();
            }
            if (bReader != null) {
                bReader.close();
            }
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        return output;
    }
    
    public static void emptyBuffer(final Process p, final boolean show) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            if (show) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
            else {
                String line;
                while ((line = br.readLine()) != null) {}
            }
            br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            if (show) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
            else {
                String line;
                while ((line = br.readLine()) != null) {}
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
