package org.brainfarm.java.util;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * JarResources: JarResources maps all resources included in a
 * Zip or Jar file. Additionaly, it provides a method to extract one
 * as a blob.
 */
public final class JarUtils {

   // external debug flag
   public boolean debugOn=false;

   // jar resource mapping tables
   private Hashtable<String, Integer> htSizes=new Hashtable<String, Integer>();  
   private Hashtable<String, byte[]> htJarContents=new Hashtable<String, byte[]>();

   // a jar file
   private String jarFileName;

   /**
    * creates a JarResources. It extracts all resources from a Jar
    * into an internal hashtable, keyed by resource names.
    * @param jarFileName a jar or zip file
    */
   public JarUtils(String jarFileName) {
      this.jarFileName=jarFileName;
      init();
   }

   /**
    * Extracts a jar resource as a blob.
    * @param name a resource name.
    */
   public byte[] getResource(String name) {
      return (byte[])htJarContents.get(name);
   }

   /**
    * initializes internal hash tables with Jar file resources.
    */
   private void init() {
      try {
          // extracts just sizes only. 
          ZipFile zf=new ZipFile(jarFileName);
          Enumeration<? extends ZipEntry> e= zf.entries();
          while (e.hasMoreElements()) {
              ZipEntry ze=(ZipEntry)e.nextElement();
              if (debugOn) {
                 System.out.println(dumpZipEntry(ze));
              }
              htSizes.put(ze.getName(),new Integer((int)ze.getSize()));
          }
          zf.close();

          // extract resources and put them into the hashtable.
          FileInputStream fis=new FileInputStream(jarFileName);
          BufferedInputStream bis=new BufferedInputStream(fis);
          ZipInputStream zis=new ZipInputStream(bis);
          ZipEntry ze=null;
          while ((ze=zis.getNextEntry())!=null) {
             if (ze.isDirectory()) {
                continue;
             }
             if (debugOn) {
                System.out.println(
                   "ze.getName()="+ze.getName()+","+"getSize()="+ze.getSize()
                   );
             }
             int size=(int)ze.getSize();
             // -1 means unknown size. 
             if (size==-1) {
                size=((Integer)htSizes.get(ze.getName())).intValue();
             }
             byte[] b=new byte[(int)size];
             int rb=0;
             int chunk=0;
             while (((int)size - rb) > 0) {
                 chunk=zis.read(b,rb,(int)size - rb);
                 if (chunk==-1) {
                    break;
                 }
                 rb+=chunk;
             }
             // add to internal resource hashtable
             htJarContents.put(ze.getName(),b);
             if (debugOn) {
                System.out.println(
                   ze.getName()+"  rb="+rb+
                   ",size="+size+
                   ",csize="+ze.getCompressedSize()
                   );
             }
          }
       } catch (NullPointerException e) {
          System.out.println("done.");
       } catch (FileNotFoundException e) {
          e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       }
   }

   /**
    * Dumps a zip entry into a string.
    * @param ze a ZipEntry
    */
   private String dumpZipEntry(ZipEntry ze) {
       StringBuffer sb=new StringBuffer();
       if (ze.isDirectory()) {
          sb.append("d "); 
       } else {
          sb.append("f "); 
       }
       if (ze.getMethod()==ZipEntry.STORED) {
          sb.append("stored   "); 
       } else {
          sb.append("defalted ");
       }
       sb.append(ze.getName());
       sb.append("\t");
       sb.append(""+ze.getSize());
       if (ze.getMethod()==ZipEntry.DEFLATED) {
          sb.append("/"+ze.getCompressedSize());
       }
       return (sb.toString());
   }
}
