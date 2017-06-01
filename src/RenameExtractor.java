import java.io.*;
import java.util.zip.*;
/**
 * Created by CSE_BUET on 4/19/2017.
 */
public class RenameExtractor {
    static File FOLDER=new File("E:\\Teaching\\2017 jan\\310\\Assignment 4");
    static File TargetFolder=new File(FOLDER.getAbsolutePath()+"\\Extracted");
    private static final int BUFFER_SIZE = 4096;
    public static void main(String[] args) {
        RenameExtractor re=new RenameExtractor();
        File listOfFiles[]=FOLDER.listFiles();
        if(!TargetFolder.exists()){
            TargetFolder.mkdir();
        }
        for(int i=0;i<listOfFiles.length;i++){
            if(listOfFiles[i].isFile()){
                String name=listOfFiles[i].getName();
                    int lastDot= name.lastIndexOf(".");
                    if(lastDot>0) {
                        String extension = name.substring(lastDot + 1);
                    int lastUnderScore=name.lastIndexOf("_");
                    String id= name.substring(lastUnderScore + 1, lastDot);
                    if(!id.startsWith("0") && !id.startsWith("1")){
                        System.out.println("trouble maker "+ name);
                    }else {
                        System.out.println( extension + " id: "+id);

                        if(extension.equals("zip")){
                            try {
                                re.unzip(listOfFiles[i].getAbsolutePath(),TargetFolder.getAbsolutePath()+"\\"+id);
                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        else if(extension.equals("cpp")){
                            File idFolder=new File(TargetFolder.getAbsolutePath()+"\\"+id);
                            idFolder.mkdir();
                            File newFile=new File(idFolder.getAbsolutePath()+"\\"+id+".cpp");
                            try {
                                newFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            InputStream is = null;
                            OutputStream os = null;
                            try {
                                is = new FileInputStream(listOfFiles[i]);
                                os = new FileOutputStream(newFile);
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = is.read(buffer)) > 0) {
                                    os.write(buffer, 0, length);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }



    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        System.out.println(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            System.out.println(entry.getName());
            String n=entry.getName();
            if(entry.getName().contains("/")){
                n=entry.getName().substring(entry.getName().indexOf("/")+1);
//                String newDir=entry.getName().substring(0,entry.getName().indexOf("/"));
//                File newDirFile=new File(newDir);
//                if(!newDirFile.exists()){
//                    newDirFile.mkdir();
//                }
            }
            String filePath = destDirectory + File.separator + n;
            System.out.println(filePath);
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();

            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File f=new File(filePath);
        f.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
