package hk.edu.polyu.comp.comp2021.cvfs.model;

import hk.edu.polyu.comp.comp2021.cvfs.Application;

import java.util.*;

/**
 * class Disk extending Directory class(below), {contents,size of class Diectory is needed.}
 * The virtual disk of the System will instantiated by class Disk
 */
class Disk extends Directory{

    /**
     * The Capacity of a virtual disk
     */
    private final long CAP;

    /**
     * @param CAP initialize the disk by setting the Capcacity
     */
    Disk(long CAP){
        // call constructor in Directory
        super("DISK",fileType.DIR,0,null);
        this.CAP = CAP;
    }

    /**
     * @return the Capacity of the current Virtual Disk
     */
    public long getCAP() { return CAP; }

    /**
     * @return print the virtual disk in this way
     */
    @Override
    public String toString() {
        return "{Disk}" + " size=" + CAP;
    }

}

/**
 * Considering the common aspects of Document and Directory,
 * (name, size, type, etc.)
 * and in convience thier storage condition
 * in the same ArrayList, abstract class File is created.
 * class Document and class Directory will extend it.
 */
abstract class File {
    private String name;
    private long size;
    private final fileType type;
    private final String content;

    /**
     * @param name name of the file
     * @param type dir txt html java css as specified in enum of fileType.java
     * @param size the size of the file
     * @param content the content of the file(only for Documents)
     */
    File(String name, fileType type, long size,String content) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.content = content;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return type
     */
    public String getType() { return type.getType(); }

    /**
     * @param changeSize this is only set for Directory as well as Disk
     *                   if a file is deleted in a Directory, the size of
     *                   the Directory and curent disk should be change.
     *                   (changeSize can be a negative number when deleting)
     */
    public void setSize(long changeSize) {
        size += changeSize;
    }

    /**
     * @return size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param newName update the name of this file
     */
    public void updataName(String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return "File{}";
    }

    /**
     * @return only for Document to return their content
     */
    public String getContent() {return content;}

    /**
     * @return only for Directory as well as Disk to return the set of files thier contain
     * will be Override in class Directory
     */
    public ArrayList<File> getContents() {return null;}

    /**
     * @param newfile only for Directory as well as Disk to add a new file to their contents
     *                 will be Override in class Directory
     */
    public void addFile(File newfile) { }

    /**
     * @param file only for Directory as well as Disk to add a new file to their contents
     *             will be Override in class Directory
     */
    public void deleteFile(File file){}

    /**
     * @return for both Directory and Document to access its parent Directory
     * will be Override in class Directory and File
     */
    public File getParent(){return null;}
}

/**
 *Used for instantiate directorys stored in virtual disk
 */
class Directory extends File{
    /**
     * contents is ArrayList to store the files in this
     */
    private ArrayList<File> contents;
    private final File parent;

    /**
     * @param name directory name
     * @param type specified as 'dir' but not set by user
     * @param size the initial size of dir (40)
     * @param parent the parent is the current working directory
     */
    Directory(String name,fileType type,long size,File parent){
        super(name, type,size,null);
        this.parent = parent;
        contents = new ArrayList<>();
    }


    /**
     * @param newfile add the new file in the contents, then, update the size
     *                of this and this's parent etc. until to the root dir
     */
    @Override
    public void addFile(File newfile) {
        contents.add(newfile);
        contents.sort(Comparator.comparing(File::getName));
        setSize(newfile.getSize());
        if (parent != null) parent.setSize(newfile.getSize());
    }



    /**
     * @param file delete the file if it in the contents, then, update the size
     *             of this and this's parent etc. until to the root dir
     */
    @Override
    public void deleteFile(File file){
        contents.remove(file);
        setSize(-1*file.getSize());
        if (parent!=null) {
            parent.setSize(-1 * file.getSize());
            return;
        }
        CVFS.dataPool.getCurDisk().setSize(-1 * file.getSize());
    }

    /**
     * @return return all files contained in this as a ArrayList
     */
    @Override
    public ArrayList<File> getContents() { return contents; }

    /**
     * @return the info. of this
     */
    @Override
    public String toString() {
        return "{Dir} " + getName() +" size="+ getSize();
    }

    /**
     * @return parent
     */
    @Override
    public File getParent() { return parent; }
}

/**
 *Used for instantiate documents stored
 */
class Document extends File{
    private final File parent;

    /**
     * @param name file name
     * @param type specified type: txt css html java
     * @param content content of this as String format
     * @param size the size of this, 40 + content.length()*2
     * @param parent the current working dir
     */
    Document(String name,fileType type,String content,long size,File parent){
        super(name, type,size,content);
        this.parent = parent;
    }


    @Override
    public String toString() {
        return "{Document} "+ getName() +"."+getType()+" size="+ getSize() + " Content: " +getContent();
    }

    /**
     * @return parent
     */
    @Override
    public File getParent() { return parent; }
}

/**
 * class for simple criterion, negation of simple criterion and binary criterion
 * first construtor for simple criterion
 * second construtor for binary ones
 * boolean fieid IsNegation to ensure whether Negation ones
 *
 * when search or rSearch runs, if the logicOp is null, then the cri is a simple one, else binary one
 */
class Criterion {
    private final String criName;
    private AttrType attrName;
    private String op;
    private String val;
    private String logicOp;
    private Criterion CriA ;
    private Criterion CriB ;
    private final boolean IsNegation;

    /**
     * @param criName criterion name
     * @param atrrName attrbute name specified in AttrType.java enum AttrType
     * @param op operation
     * @param val value
     * @param IsNegation Negation: true not Negation: false
     */
    Criterion(String criName, AttrType atrrName, String op, String val, boolean IsNegation){
        this.criName = criName;
        this.attrName = atrrName;
        this.op = op;
        this.val = val;
        this.IsNegation = IsNegation;
    }
    /**
    * @param criName criterion name
     *  @param CriA the subCriterion1
     * @param CriB the subCriterion2
     * @param logicOp && or ||
     * @param IsNegation Negation: true not Negation: false
     */
    Criterion(String criName,Criterion CriA,Criterion CriB,String logicOp,boolean IsNegation){
        this.criName = criName;
        this.IsNegation = IsNegation;
        this.CriA = CriA;
        this.CriB = CriB;
        this.logicOp = logicOp;
    }

    /**@return cri name*/
    public String getCriName() { return criName; }

    /** @return attr name*/
    public AttrType getAttrName() { return attrName; }

    /**@return operation*/
    public String getOp() { return op; }

    /**@return value*/
    public String getVal() { return val; }

    /**@return for Negation ones return true, else return false */
    public boolean isNegation() {
        return IsNegation;
    }

    /**@return logic operation*/
    public String getLogicOp() {
        return logicOp;
    }

    /**@return subCri1*/
    public Criterion getCriA() {
        return CriA;
    }

    /**@return subCri2*/
    public Criterion getCriB() {
        return CriB;
    }

    /**@return info about this*/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (logicOp==null){
            if (!IsNegation) sb.append("{Simple Criterion} ");
            else sb.append("{Negation of Simple Criterion} ");

            sb.append(" Name: ").append(criName);
            sb.append(" Content: ").append(getAttrName());
            sb.append(" ").append(op).append(" ");
            sb.append(val);

            return sb.toString();
        }
        if (!IsNegation) sb.append("{Binary Criterion} ");
        else sb.append("{Negation of Binary Criterion} ");

        sb.append(" Name: ").append(criName);
        sb.append(" SubCri1: ").append(CriA.getCriName());
        sb.append(" ").append(logicOp).append(" ");
        sb.append(" SubCri2: ").append(CriB.getCriName());

        return sb.toString();
    }
}


/**
 *The caculating core and current state core of CVFS
 */
public class CVFS {

    /**
     * @return return the detailed path of current dir
     */
    public static String getCurPos(){
        return Operation.getPos(dataPool.getCurDir());
    }

    /**
     * @param ms tranfer the ms to View so that shown on the CLI
     */
    static void tranMess(String ms){
        Application.showMessage(ms);
    }

    /**
     * @param files transfer the file in Set to View so that shown on the CLI one by one along with thier path
     * @param code '1' for search '2' for rSearch
     */
    static void tranMess(Set<File> files,char code){
        if (files==null) return;
        int size = 0;
        int counter = 0;
        ArrayList<File> sfiles = new ArrayList<>(files);
        sfiles.sort(Comparator.comparing(File::getName));
        for (File file:sfiles){
            tranMess(Operation.getPos(file.getParent())+"   "+file.toString());
            counter++;
            if (file.getType().equals("dir") && code == '2') size += dataPool.BASIC_SIZE;
            else size += file.getSize();
        }
        tranMess("Total: "+counter+", Size: "+size);
    }

    /**
     * @param files transfer the Criterion in ArrayList to View so that shown on the CLI one by one
     */
    static void tranMess(ArrayList<Criterion> files){
        for (Criterion file:files){
            tranMess("   "+file.toString());
        }
    }

    /**
     * store real time data needed
     */
    public static class dataPool{
        /**
         * basic size of docs and dirs
         */
        public static final int BASIC_SIZE = 40;

        /**
         * set null when init means no current disk
         *
         * cannot set curDir as curDisk beacause all dir is File type while disk is Dir type
         * Disk is actually the root directory
         */
        private static Disk curDisk = null;

        /**
         * @return the curDisk, if null, there is no disk
         */
        public static Disk getCurDisk() {
            return curDisk;
        }

        /**
         * @param curDisk set the current disk
         */
        public static void setCurDisk(Disk curDisk) { dataPool.curDisk = curDisk; }

        /**
         * init current directory, null means no disk or the position is in disk right now
         */
        private static File curDir = null;

        /**
         * @return current dir
         */
        public static File getCurDir() {
            return curDir;
        }

        /**
         * @param Dir set current file
         */
        public static void setCurDir(File Dir) {
            curDir = Dir;
        }

        /**
         * store all criterion created
         */
        private static ArrayList<Criterion> AllCri = new ArrayList<>();

        /**
         * @return all criterion has been created
         */
        static ArrayList<Criterion> getAllCri() {
            return AllCri;
        }

        /**
         * @param newCri sort based on CriName after add
         */
        static void addCri(Criterion newCri) {
            AllCri.add(newCri);
            AllCri.sort(Comparator.comparing(Criterion::getCriName));
        }


        /**
         * @param name file name//Cri name ensure whether the current dir has the file with the name
         * @return if found, return the file itself, else null
         */
        static File hasFile(String name) {
            if (getCurDir()==null){
                for (File file:getCurDisk().getContents()){ if (file.getName().equals(name)) return file; }
                return null;
            }
            for (File file:getCurDir().getContents()){if (file.getName().equals(name)) return file;}
            return null;
        }

        /**
         * @param name Cri name ensure whether the cri has been stored
         * @return if found, return the name itself, else null
         */
        static Criterion hasCri(String name){
            for (Criterion cri:getAllCri()){if (cri.getCriName().equals(name)) return cri;}
            return null;
        }
    }


    /**
     * contains checker for
     * the legality for
     * disk size, names contains dir, doc, cri, attr name, op, logicOp, val and whether storage almostly full
     * if got a wrong input, the firendly reminder will transfer to Controller and finally shown on CLI
     */
    static class checker{

        /**
         * @param size checker whether the size user input legal?
         * @param code '1' check for disk, '2' check for value of Criterion
         * @return return the long type size only if the size are digits and no '-'
         *         else return -1
         */
        public static long numChecker(String size,char code){
            for (int i=0;i<size.length();i++) {
                if (!Character.isDigit(size.charAt(i))) {
                    if (code=='1')tranMess(size+MessageCode.W2.getMessage());
                    return -1;
                }
            }
            long s = Long.parseLong(size);
            if (s<=0) return -1;
            return s;
        }

        /**
         * @param size  the new file size
         * @return if the new file added result in storage overflow, return false
         *         else return true
         */
        public static boolean memoryChecker(long size){
            if (dataPool.getCurDisk().getCAP() >= dataPool.getCurDisk().getSize() + size) return true;
            tranMess(MessageCode.W3.getMessage());
            tranMess("Disk Capacity: "+dataPool.getCurDisk().getCAP());
            tranMess("Occupied storage: "+dataPool.getCurDisk().getSize());
            tranMess("Currently available storage: "+(dataPool.getCurDisk().getCAP() - dataPool.getCurDisk().getSize()));
            return false;
        }

        /**
         * rewrite but only check is there a disk exist
         * @return no disk return false, else return true
         */
        public static boolean memoryChecker(){
            if (dataPool.getCurDisk()!=null)return true;
            tranMess(MessageCode.W4.getMessage());
            return false;
        }

        /**
         * @param name name code == '1' check docName and dirName code=='2' check criName
         * @param checkCode 1 or 2
         * @return only if the name legal return true
         */
        public static boolean nameChecker(String name, char checkCode){
            int length = name.length();

            if (checkCode=='1'){
                // length legality
                if (length<=10 && length>0) {
                    for (int i = 0; i < length; i++) {
                        // char legality
                        if (!Character.isDigit(name.charAt(i)) && !Character.isLetter(name.charAt(i))) {
                            tranMess(MessageCode.W5.getMessage());
                            return false;
                        }
                    }
                    // check whether aleady exist in working directory
                    if (dataPool.hasFile(name) == null) return true;
                    tranMess(MessageCode.W6.getMessage() + name);
                    return false;
                }
                tranMess(MessageCode.W5.getMessage());
            }

            if(checkCode=='2'){
                // length legality
                if (length==2) {
                    // chars legality
                    if (!Character.isLetter(name.charAt(0)) && !Character.isLetter(name.charAt(1))) {
                        tranMess(MessageCode.W7.getMessage());
                        return false;
                    }
                    // check whether already stored
                    if (dataPool.hasCri(name) == null) return true;
                    tranMess(MessageCode.W16.getMessage() + name);
                    return false;
                }
                tranMess(MessageCode.W7.getMessage());
            }
            return false;
        }

        /**
         * @param name String format of AttrType
         * @return if legal, return the AttrTyoe format of the attrName, else null
         */
        public static AttrType nameChecker(String name){
            switch (name){
                case "name": return AttrType.NAME;
                case "type": return AttrType.TYPE;
                case "size": return AttrType.SIZE;
                default: tranMess(MessageCode.W8.getMessage()+name);
                    return null;
            }
        }


        /**
         * @param type check the file type in String format
         * @return if legal, return the fileTyoe format, else null
         */
        public static fileType typeChecker(String type){
            switch (type){
                case "txt":
                case ".txt":
                    return fileType.TXT;
                case "java":
                case ".java":
                    return fileType.JAVA;
                case "html":
                case ".html":
                    return fileType.HTML;
                case "css":
                case ".css":
                    return fileType.CSS;
                default: tranMess(MessageCode.W9.getMessage()+type);return null;
            }
        }

        /**
         * @param op operation only can be contains, equals and > < <= >= == !=
         * @param attrName already checked and ensured
         * @param val String in "" or an int in String format
         * @return true only if op and val are legal and match
         */
        public static boolean opChecker(String op, AttrType attrName, String val){
            char code;
            switch (attrName){
                case NAME:
                    if (!op.equals("contains")) {
                        tranMess(MessageCode.W17.getMessage()+attrName.getType()+MessageCode.W10.getMessage()+op);
                        return false;
                    }
                    code = '1';
                    break;
                case TYPE:
                    if (!op.equals("equals")) {
                        tranMess(MessageCode.W17.getMessage()+attrName.getType()+MessageCode.W10.getMessage()+op);
                        return false;
                    }
                    code = '1';
                    break;
                case SIZE:
                    if (!((op.equals(">") || op.equals("<") || op.equals(">=") || op.equals("<=") || op.equals("==") || op.equals("!=")))) {
                        tranMess(MessageCode.W17.getMessage()+attrName.getType() + MessageCode.W10.getMessage() + op);
                        return false;
                    }
                    code = '2';
                    break;
                default: tranMess(MessageCode.W11.getMessage() + op);return false;
            }
            if (!valChecker(val,code)) {
                tranMess(MessageCode.W17.getMessage()+attrName.getType() + MessageCode.W10.getMessage() + val);
                return false;
            }
            return true;
        }

        /**
         * call by opChecker()
         * @param val check the val
         * @param checkerCode '1' check whether val is a String with "", '2' check whether an int in String format
         * @return return true only if legal
         */
        private static boolean valChecker(String val,char checkerCode){
            if (checkerCode=='1'){
                if (!(val.startsWith("\"") && val.endsWith("\""))){ tranMess(MessageCode.W18.getMessage());return false; }
                if (val.length()<=2){tranMess(MessageCode.W19.getMessage());return false;}
                return true;
            }
            else if(checkerCode=='2'){
                if (numChecker(val,'2')!=-1)
                    return Long.parseLong(val)>0;
            }
            return false;
        }

        /**
         * @param op check for logicOp only can be && or ||
         * @return only if op be && or | return true, else false
         */
        public static boolean opChecker(String op){
            if( op.equals("&&") || op.equals("||")) return true;
            tranMess(MessageCode.W12.getMessage());
            return false;
        }

    }

    /**
     *All neccessary core Operation already here
     * if operation operates successfully, the issue will be reported by transferring message to Application
     */
    public static class Operation {
        /**
         * @param cap create a new disk when the cap is after checking and legal, otherwise, terminating
         *            after creation, set the current disk be this new one
         */
        public static void newDisk(String cap) {
            long Cap = checker.numChecker(cap,'1');

            if (Cap!=-1) {
                Disk newDisk = new Disk(Cap);
                dataPool.setCurDisk(newDisk);
                dataPool.setCurDir(null);

                tranMess(MessageCode.M1.getMessage());
                tranMess(newDisk.toString());
            }
        }

        /**
         * @param name     newDoc name
         * @param type     file type in String formay
         * @param content  content of the doc
         *                  if all param checked and legal,
         *                 disk is exist and won't be overflow
         *                 newDoc will be created and reported
         */
        public static void newDoc(String name, String type, String content) {
            fileType type_ = checker.typeChecker(type);

            if (checker.memoryChecker() && checker.nameChecker(name, '1') && type_ != null) {
                long size = dataPool.BASIC_SIZE + 2 * content.length();

                if (checker.memoryChecker(size)) {
                    File newDoc;
                    if (dataPool.getCurDir() !=null) {
                        newDoc = new Document(name, type_, content, size, dataPool.getCurDir());
                        dataPool.getCurDir().addFile(newDoc);
                        dataPool.getCurDisk().setSize(size);
                    }
                    else {
                        newDoc = new Document(name, type_, content, size, null);
                        dataPool.getCurDisk().addFile(newDoc);
                    }
                    tranMess(MessageCode.M3.getMessage());
                    tranMess(newDoc.toString());
                }
            }
        }


        /**
         * @param name name of dir
         *              if name checked and legal,
         *                  disk is exist and won't be overflow
         *                  newDir will be created and reported
         */
        public static void newDir(String name) {
            if(checker.memoryChecker() && checker.nameChecker(name, '1') && checker.memoryChecker(dataPool.BASIC_SIZE)) {
                File newDir;
                if (dataPool.getCurDir() != null) {
                    newDir = new Directory(name, fileType.DIR, dataPool.BASIC_SIZE, dataPool.getCurDir());
                    dataPool.getCurDir().addFile(newDir);
                    dataPool.getCurDisk().setSize(dataPool.BASIC_SIZE);
                }
                else {
                    newDir = new Directory(name, fileType.DIR, dataPool.BASIC_SIZE, null);
                    dataPool.getCurDisk().addFile(newDir);
                }
                tranMess(MessageCode.M2.getMessage());
                tranMess(newDir.toString());
            }
        }

        /**
         * @param name name of file to be deleted
         *             if disk exists, file of such name found
         *             the file will be removed and reported
         */
        public static void delete(String name) {
            if (!checker.memoryChecker()) return;

            File file = dataPool.hasFile(name);
            if (file==null) { tranMess(MessageCode.W13.getMessage()+name);return; }
            if (dataPool.getCurDir()==null){
                dataPool.getCurDisk().deleteFile(dataPool.hasFile(name));
            }
            else {
                dataPool.getCurDisk().setSize(-1 * file.getSize());
                dataPool.getCurDir().deleteFile(dataPool.hasFile(name));
            }
            tranMess("File "+ name + MessageCode.M4.getMessage());
        }

        /**
         * @param oldName old file name
         * @param newName new file name
         *                if disk exists, file of such name found,
         *                new name is legal, name change will be done and reported
         */
        public static void rename(String oldName, String newName) {
            if (!checker.memoryChecker()) return;
            File file = dataPool.hasFile(oldName);
            if (file==null) { tranMess(MessageCode.W13.getMessage()+oldName); return; }
            if (checker.nameChecker(newName, '1')){
                file.updataName(newName);
                tranMess("File "+ oldName + MessageCode.M5.getMessage() + newName);
            }
        }

        /**
         * @param name name of Dir to change or '..' which means back up
         *             only dir name found or the current dir is not root dir
         *             the operation will be deducted
         *             if current dir is the root dir and '..' is input, error issue will reported
         */
        public static void changeDir(String name){
            if (!checker.memoryChecker()) return;
            if (name.equals("..")){
                if (dataPool.getCurDir()==null) tranMess(MessageCode.W1.getMessage());
                else dataPool.setCurDir(dataPool.getCurDir().getParent());
            }
            else {
                File file = dataPool.hasFile(name);
                if (file == null || !Objects.equals(file.getType(), "dir")) { tranMess(MessageCode.W14.getMessage() + name);return; }
                dataPool.setCurDir(file);
            }
        }

        private static int Counter = 0;
        private static long Size = 0;
        /**
         * trasfer the string format of file to controller in such current directory 1 by 1
         */
        public static void list(){
            if (!checker.memoryChecker()) return;
            if (dataPool.getCurDir()==null) {
                for(File file :dataPool.getCurDisk().getContents()){ if (file.getType().equals("dir")) {tranMess(file.toString());Counter++; Size+=file.getSize();}}
                for(File file :dataPool.getCurDisk().getContents()){ if (!file.getType().equals("dir")) {tranMess(file.toString());Counter++; Size+=file.getSize();}}
            }
            else {
                for(File file :dataPool.getCurDir().getContents()){ if (file.getType().equals("dir")) {tranMess(file.toString());Counter++; Size+=file.getSize();}}
                for(File file :dataPool.getCurDir().getContents()){ if (!file.getType().equals("dir")) {tranMess(file.toString());Counter++; Size+=file.getSize();}}
            }
            tranMess("Total: "+Counter+", Size: "+Size);
            Counter = 0;
            Size = 0;
        }

        /**
         * return files contains in the current dir in an ArrayList (sorted, dir first)
         */
        private static ArrayList<File> list_(File f){
            ArrayList<File> files = new ArrayList<>();
            if (f==null) {
                for(File file :dataPool.getCurDisk().getContents()){ if (!file.getType().equals("dir")) files.add(file); }
                for(File file :dataPool.getCurDisk().getContents()){ if (file.getType().equals("dir")) files.add(file); }
            }
            else {
                for(File file :f.getContents()){ if (!file.getType().equals("dir")) files.add(file); }
                for(File file :f.getContents()){ if (file.getType().equals("dir")) files.add(file); }
            }
            return files;
        }

        /**
         *trasfer the string format of all files(files in child dir included)
         * to controller in such current directory 1 by 1
         */
        public static void rlist(){
            if (!checker.memoryChecker()) return;
            rlist(list_(dataPool.getCurDir())," ");
            tranMess("Total: "+Counter+", Size: "+Size);
            Counter = 0;
            Size = 0;
        }

        /**
         * make sure nicely print all the files and thier paths to the CLI
         */
        private static void rlist(ArrayList<File> files,String retract){
            for (File file:files){
                tranMess(retract +getPos(file.getParent()) + " " + file.toString());
                Counter++;
                if (file.getType().equals("dir")) {
                    rlist(list_(file), "   " + retract);
                    Size+=dataPool.BASIC_SIZE;
                }
                else Size+=file.getSize();
            }
        }

        /**
         * return alll files(files in child dir included) contains in the current dir in an ArrayList
         */
        private static ArrayList<File> rlist_(){
            ArrayList<File> allfiles = new ArrayList<>();
            return rlist_(allfiles,list_(dataPool.getCurDir()));
        }

        /**
         * @param allfiles put all files in this arraylist
         * @param nextfiles files to add as well as do list_()
         * @return alll files(files in child dir included) contains in the current dir in an ArrayList
         */
        private static ArrayList<File> rlist_(ArrayList<File> allfiles, ArrayList<File> nextfiles){
            for (File file:nextfiles){
                if (file.getType().equals("dir")) rlist_(allfiles,list_(file));
                allfiles.add(file);
            }
            return allfiles;
        }


        /**
         * create new Simple Cri
         * @param criName name of cri
         * @param attrName attribute name
         * @param op operation
         * @param val value
         *            if disk exist, all param checked and legal, criName doesn't exist
         *            new simple cri will be create
         *            and reported
         */
        public static void newSimpleCri(String criName,String attrName,String op,String val){
            if (checker.nameChecker(criName,'2')){
                AttrType type = checker.nameChecker(attrName);
                if (type!=null && checker.opChecker(op,type,val)){
                    Criterion newCri = new Criterion(criName,type,op,val,false);
                    dataPool.addCri(newCri);
                    tranMess(MessageCode.M6.getMessage());
                    tranMess(newCri.toString());
                }
            }
        }

        /**
         * @param criName1 new cri name
         * @param criName2 a cri alreadt stored
         *                 only if the new name checked and lagal
         *                 the cri named cirName2 found
         *                 new negation will created and reported.
         *
         *                 however, if cri1 itself is a negation of a cri, doesn't exist
         *                 the new cri will be not a negation
         */
        public static void newNegation(String criName1,String criName2){
            Criterion cri2 = dataPool.hasCri(criName2);
            if (cri2==null) { tranMess(MessageCode.W15.getMessage() + criName2); return;}
            if (checker.nameChecker(criName1,'2')){
                Criterion newCri;
                if (cri2.getLogicOp()==null) newCri = (new Criterion(criName1,cri2.getAttrName(),cri2.getOp(),cri2.getVal(),!cri2.isNegation()));
                else newCri =(new Criterion(criName1,cri2.getCriA(),cri2.getCriB(),cri2.getLogicOp(),!cri2.isNegation()));
                dataPool.addCri(newCri);
                tranMess(MessageCode.M6.getMessage());
                tranMess(newCri.toString());
            }

        }

        /**
         * @param criName1 name of new cri
         * @param criName3 name of a exist cri
         * @param logicOp && or ||
         * @param criName4 cri name of a exist cri
         *                 only if disk exists
         *                 name of new cri checked and legal, doesn't exist
         *                 logicOp checked and legal
         *                 new binary cri will be created and reported
         */
        public static void newBinaryCri(String criName1,String criName3,String logicOp,String criName4){
            if (!checker.nameChecker(criName1,'2') || !checker.opChecker(logicOp)) return;

            Criterion cri3 = dataPool.hasCri(criName3);
            Criterion cri4 = dataPool.hasCri(criName4);

            if (cri3==null) { tranMess(MessageCode.W15.getMessage() + criName3); return;}
            if (cri4==null) { tranMess(MessageCode.W15.getMessage() + criName4); return;}

            Criterion newCri = new Criterion(criName1,cri3,cri4,logicOp,false);
            dataPool.addCri(newCri);
            tranMess(MessageCode.M6.getMessage());
            tranMess(newCri.toString());
        }

        /**
         * to init Isdocument when system just started
         */
        public static void Isdocument(){
            dataPool.addCri(new Criterion("IsDocument", AttrType.TYPE,"equals","\"dir\"",true));
        }

        /**
         * transfer all exist cri to View to show in CLI 1 by 1
         */
        public static void printAllCriteria(){
            tranMess(dataPool.getAllCri());
        }

        /**
         * @param criName cri name
         *                only if disk exist
         *                cri exist
         *                files in current dir and match the criterion will transferred
         */
        public static void search(String criName) {
            if (!checker.memoryChecker()) return;
            if (dataPool.hasCri(criName)==null) { tranMess(MessageCode.W15.getMessage()+criName);return; }
            tranMess(appliCri(Objects.requireNonNull(dataPool.hasCri(criName)),'1'),'1');
        }

        /**
         * @param criName cri name
         *              only if disk exist
         *              cri exist
         *              all files(files in child dir included) in current dir and match the criterion will transferred
         */
        public static void rSearch(String criName) {
            if (!checker.memoryChecker()) return;
            if (dataPool.hasCri(criName)==null) { tranMess(MessageCode.W15.getMessage()+criName);return; }
            tranMess(appliCri(Objects.requireNonNull(dataPool.hasCri(criName)),'2'),'2');
        }

        /**
         * @param Cri Criterion
         * @param code code '1' search    '2' rsearch
         * @return set of files match such cri
         *
         * considering that binaryCri may combined with binaryCri(s), so recursion is needed
         * and the set operation will be used to filter criterion files
         * Details are in the report (requirments.search)
         */
        private static Set<File> appliCri(Criterion Cri,char code){
            if (Cri.getLogicOp()!=null){
                Set<File> A = appliCri(Cri.getCriA(),code);
                Set<File> B = appliCri(Cri.getCriB(),code);
                if (Cri.getLogicOp().equals("&&"))
                    return (Cri.isNegation())? CalcuSet(new HashSet<>((code=='1')?list_(dataPool.getCurDir()): rlist_()),CalcuSet(A,B,'2'),'3'):CalcuSet(A,B,'2');
                return (Cri.isNegation())? CalcuSet(new HashSet<>((code=='1')?list_(dataPool.getCurDir()): rlist_()),CalcuSet(A,B,'1'),'3'):CalcuSet(A,B,'1');
            }
            else return appliSimpleCri(Cri,code);
        }

        /**
         * @param Cri simple cri name
         * @param code '1' do search '2' do rsearch
         * @return set of files match such simple
         */
        private static Set<File> appliSimpleCri(Criterion Cri,char code){
            Set<File> filter = new HashSet<>();

            ArrayList<File> allFile;
            String s = Cri.getVal();

            if (code == '1') allFile = list_(dataPool.getCurDir());
            else allFile = rlist_();


            switch (Cri.getAttrName()){
                case NAME:
                    for (File file:allFile){
                        if (file.getName().contains((s).substring(1, s.length()-1))) filter.add(file);
                    }
                    break;

                case TYPE:
                    for (File file:allFile){
                        if (file.getType().equals((s).substring(1, s.length()-1))) filter.add(file);
                    }
                    break;

                case SIZE:
                    switch (Cri.getOp()){
                        case ">":
                            for (File file:allFile){ if (file.getSize() > Long.parseLong(s)) filter.add(file); }
                            break;
                        case "<":
                            for (File file:allFile){ if (file.getSize() < Long.parseLong(s)) filter.add(file); }
                            break;
                        case ">=":
                            for (File file:allFile){ if (file.getSize() >= Long.parseLong(s)) filter.add(file); }
                            break;
                        case "<=":
                            for (File file:allFile){ if (file.getSize() <= Long.parseLong(s)) filter.add(file); }
                            break;
                        case "==":
                            for (File file:allFile){ if (file.getSize() == Long.parseLong(s)) filter.add(file); }
                            break;
                        case "!=":
                            for (File file:allFile){ if (file.getSize() != Long.parseLong(s)) filter.add(file); }
                            break;
                        default: return null;
                    }
                    break;
                default:
                    return null;
            }
            if (Cri.isNegation()){ return CalcuSet(new HashSet<>(allFile),filter,'3'); }
            return filter;
        }

        /**
         * support set calculation for sets of files
         * @param a set of files
         * @param b set of files
         * @param code ‘1’ a Union b '2' a Intersection b '3' a - b
         * @return the result after set calculation
         */
        private static Set<File> CalcuSet(Set<File> a,Set<File> b,char code){
            Set<File> forRe = new HashSet<>(a);
            switch (code){
                case '1' :
                    forRe.addAll(b);break;
                case '2':
                    forRe.retainAll(b);break;
                case '3':
                    forRe.removeAll(b);break;
            }

            return forRe;
        }

        /**
         * @param file contais in the disk
         * @return the path of such file in String format
         */
        static String getPos(File file){
            if (dataPool.getCurDisk()==null){ return "no Disk"; }
            if (file==null){ return "Disk\\";}

            StringBuilder SBForRe = new StringBuilder();//"CVFS Disk"

            while (file!=null){
                SBForRe.insert(0,file.getName());
                SBForRe.insert(0,"\\");
                file = file.getParent();
            }

            return SBForRe.insert(0,"Disk").append("\\").toString();
        }

    }

}
