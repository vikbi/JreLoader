
import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.lang.ClassLoader;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.lang.reflect.InvocationTargetException;
public class JarClassLoader extends ClassLoader {
    private String jarFile = "Test1.jar"; //Path to the jar file
    private Hashtable classes = new Hashtable(); //used to cache already defined classes

     public static void main(String[] args) throws InstantiationException,MalformedURLException, IllegalAccessException,
                 NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {

                System.out.println("running");

              JarClassLoader loader = new JarClassLoader();
              Class<?> c = loader.findClass("Test2");
              Object ob = c.newInstance();
              Method md = c.getMethod("show");
              md.invoke(ob);
}

    public JarClassLoader() {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class findClass(String className) {
        byte classByte[];
        Class result = null;

        result = (Class) classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (Exception e) {
        }

        try {
            JarFile jar = new JarFile(jarFile);
            JarEntry entry = jar.getJarEntry(className + ".class");
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}