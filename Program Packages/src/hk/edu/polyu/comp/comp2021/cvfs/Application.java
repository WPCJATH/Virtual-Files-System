package hk.edu.polyu.comp.comp2021.cvfs;

import hk.edu.polyu.comp.comp2021.cvfs.model.CVFS;
import hk.edu.polyu.comp.comp2021.cvfs.model.MessageCode;

import java.util.Scanner;

import static hk.edu.polyu.comp.comp2021.cvfs.model.CVFS.Operation.*;
import static hk.edu.polyu.comp.comp2021.cvfs.model.CVFS.Operation.newBinaryCri;


/**
 * The componet of MVC pattern: get input from user, show issues to user
 */
class View{
    /**
     * @param curPos Get the current position from Application.main
     *               by call method in CVFS class in specified String format
     *               then show the current position to the CLI
     * @return return false only if the user enter the keyword "quit" or "exit" (Ignore letter case)
     */
    public static Boolean getInput(String curPos) {
        System.out.print("\nCVFS "+curPos+" >> ");
        Scanner scan = new Scanner(System.in);

        String[] s = scan.nextLine().split(" ");

        if (s[0].toLowerCase().equals("quit")||s[0].toLowerCase().equals("exit")) return false;
        Application.Reader(s);
        return true;
    }

    /**
     * @param ms Show the messages to CLI if any
     */
    public static void printMessage(String ms){System.out.println(ms);}
}


/**
 * Controller: bridge bewteen View and Model
 */
public  class Application {

    /**
     * @param b recognize the infomation get from the user from getInput of class View
     *          if the command is correct, it will be successfully recognized and delivered to CVFS.java
     *          to do further conducting.
     *
     *          if the command is wrong or missing some component, it will let View to tell the user
     *          what's wrong with his or her command.
     */
    public static void Reader(String[] b) {
        switch (b[0].toLowerCase()) {
            case "newdisk":
            case "ndisk":
                if (b.length>=2) CVFS.Operation.newDisk(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R3.getMessage());}
                break;
            case "newdir":
            case "ndir":
                if (b.length>=2) CVFS.Operation.newDir(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R4.getMessage());}
                break;
            case "newdoc":
            case "ndoc":
                if (b.length>=4) {
                    StringBuilder sb = new StringBuilder(b[3]);
                    for (int i=4;i<b.length;i++) sb.append(' ').append(b[i]);
                    CVFS.Operation.newDoc(b[1], b[2], sb.toString());
                }
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R5.getMessage());}
                break;
            case "rename":
            case "rn":
                if (b.length>=3) CVFS.Operation.rename(b[1],b[2]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R13.getMessage());}
                break;
            case "delete":
            case "dl":
                if (b.length>=2) CVFS.Operation.delete(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R6.getMessage());}
                break;
            case "changedir":
            case "cd":
                if (b.length>=2) CVFS.Operation.changeDir(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R7.getMessage());}
                break;
            case "list":
            case "ls":
                CVFS.Operation.list();
                break;
            case "rlist":
            case "rls":
                CVFS.Operation.rlist();
                break;
            case "newsimplecri":
            case "nsc":
                if (b.length>=5) CVFS.Operation.newSimpleCri(b[1],b[2],b[3],b[4]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R8.getMessage());}
                break;
            case "newnegation":
            case "nn":
                if (b.length>=3) CVFS.Operation.newNegation(b[1],b[2]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R9.getMessage());}
                break;
            case "newbinarycri":
            case "nbc":
                if (b.length>=5) CVFS.Operation.newBinaryCri(b[1],b[2],b[3],b[4]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R10.getMessage());}
                break;
            case "printallcriteria":
            case "pac":
                CVFS.Operation.printAllCriteria();
                break;
            case "search":
            case "sr":
                if (b.length>=2) CVFS.Operation.search(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R11.getMessage());}
                break;
            case "rsearch":
            case "rsr":
                if (b.length>=2) CVFS.Operation.rSearch(b[1]);
                else{showMessage(MessageCode.R2.getMessage() + MessageCode.R12.getMessage());}
                break;
            default:
            showMessage(MessageCode.R0.getMessage() + b[0] + MessageCode.R1.getMessage());
        }
    }

    /**
     * @param message some message what to tell the user by calling View and then shown on CLI
     */
    public static void showMessage(String message){
        View.printMessage(message);
    }

    /**
     * @param args
     * initialize the tolerant Simple Criterion, keep get input from CLI and based on CLI to make
     * reactions, unless the user enter 'exit' or 'quit' to shut down the system
     */
    public static void main(String[] args){
        CVFS.Operation.Isdocument();
        fortest();
        showMessage(MessageCode.Welcome.getMessage());

        boolean IsContinue = true;

        while (IsContinue)
            IsContinue = View.getInput(CVFS.getCurPos());

        showMessage("Bye!");
    }

    /**
     * for test, avoid repeatly enter datas
     */
    static void fortest(){
        newDisk("2048");

        newDir("COMP");
        changeDir("COMP");
        newDoc("ARForm", "html", "This is AR Form");
        newDoc("WIE", "css", "Before graduate, WIE is needed.");

        newDir("COMP2021");
        changeDir("COMP2021");
        newDoc("assign1", "java", "assignment 1.");
        newDoc("midterm", "txt", "COMP2021 does not have a midterm!");

        newDir("project");
        changeDir("project");
        newDoc("descrip", "html", "I do not like group project, I prefer individual ones.");
        newDoc("sourcecode", "java", "Source code is not here.");
        newDoc("tutrial", "css", "tutrial of OOP");
        newDoc("rubrics", "txt", "Once upload, A+ gained.");
        changeDir("..");
        changeDir("..");

        newDir("DataStr");
        changeDir("DataStr");
        newDoc("quiz1", "html", "What a hard quiz!");
        newDoc("leccode", "java", "So many");

        changeDir("..");
        changeDir("..");
        newDoc("PolyUoffer", "html", "Congratulations on being admitted to PolyU!");
        newDoc("ideaWeb", "css", "I hope idea has a web version.");

        newSimpleCri("aa","name","contains","\"i\"");
        newSimpleCri("bb","name","contains","\"a\"");
        newSimpleCri("cc","type","equals","\"html\"");
        newSimpleCri("dd","type","equals","\"txt\"");
        newSimpleCri("ee","size",">=","102");
        newSimpleCri("ff","size","==","88");

        newNegation("gg","ee");
        newNegation("hh","cc");

        newBinaryCri("ii","aa","&&","ff");
        newBinaryCri("jj","cc","||","ee");
        newBinaryCri("kk","ii","&&","jj");
    }
}
