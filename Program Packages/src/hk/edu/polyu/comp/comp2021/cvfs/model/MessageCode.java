package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * Message to tell the user what condition is.
 */
public enum MessageCode {
    //Warning message
    /** Directory change error*/
    W1("DirChange Error! The working Directory is already the root Directory."),
    /** size input is illegal*/
    W2(" cannot be recognized as a legal size of a Disk."),
    /** Size error*/
    W3("SizeError/OverflowRisk! Disk cannot hold this file because its too big or disk is almost full."),
    /** disk error*/
    W4("DiskError! There is no disk working! Enter \"newDisk n\" (n is size of Disk) to create a new Disk."),
    /** file name error*/
    W5("FileNameError! Null name, too long names(length > 10) and other type chars names(can only use digits and letters) are not allowed."),
    /** file name error*/
    W6("FileNameError! This name is already in the working directory： "),
    /** criterion name error*/
    W7("CriNameError! Null name, too long or short names(length must be 2) and other type chars names(can only use letters) are not allowed."),
    /** criterion name error*/
    W16("CriNameError! This criName has already stored in this system: "),
    /** attribute name error*/
    W8("AtrrNameError! No such this name: "),
    /** File type name error*/
    W9("FileTypeError! No such Document type: "),
    /**Match Error*/
    W17("MatchError!"),
    /** attribute name and val match error */
    W10(" cannot along with "),
    /** operation error*/
    W11("OpError, No such this Op: "),
    /** logic operation error*/
    W12("LogicOPError! LogicOP can only be \"&&\" or \"||\" ! "),
    /** Exist error*/
    W13("ExistError! Cannot find such a file in working Directory: "),
    /** Exist error*/
    W14("ExistError! Cannot find such Directory in current Working Directory: "),
    /** Exist error*/
    W15("ExistError! Cannot find such a Criterion named: "),
    /*** valerror*/
    W18("val with \"contains\" or \"type\" must with double quote."),
    W19("val with no content."),

    // friendly reminding
    /** keywordError*/
    R0("KeywordError! "),
    /** keywordError*/
    R1(" cannot be recognized."),
    /** MissContentError*/
    R2("MissContentError! The command of "),
    /** MissContentError*/
    R3("create new Disk should be: NewDisk size"),
    /** MissContentError*/
    R4("create new Directory should be: NewDir name"),
    /** MissContentError*/
    R5("create new Document should be: NewDoc name DocType content"),
    /** MissContentError*/
    R6("delete Filename should be: delete filename"),
    /** MissContentError*/
    R7("change Directory should be: changeDir dirName"),
    /** MissContentError*/
    R8("create new Simple Criterion should be: newSimpleCri criName attrName op val"),
    /** MissContentError*/
    R9("create new Negation of Criterion should be: newNegation criName1 criName2"),
    /** MissContentError*/
    R10("create new Binary Criterion should be: newBinaryCri criName1 criName3 logicOp criName4"),
    /** MissContentError*/
    R11("search files in current working directory by specified Criterion should be: search CriName"),
    /** MissContentError*/
    R12("search all files in current working directory by specified Criterion should be: rSearch CriName"),
    /** MissContentError*/
    R13("rename a file should be: rename oldFilename newFilename"),

    // successful report
    /** new disk created*/
    M1("New Disk created successfully."),
    /** new dir created*/
    M2("New Directory created successfully."),
    /** new doc created*/
    M3("New Document created successfully."),
    /** file deleted*/
    M4(" deleted successfully."),
    /** rename successfully*/
    M5(" has been changed to file "),
    /** new cri created*/
    M6("New Criterion has been created successfully."),


    /**
     * Welcome
     */
    Welcome("Welcome to the CVFS (COMP Virtual File System) developed by Group 36!");

    private final String message;

    MessageCode(String message){
        this.message = message;
    }

    /**
     * @return messages set
     */
    public String getMessage(){return message;}
}
