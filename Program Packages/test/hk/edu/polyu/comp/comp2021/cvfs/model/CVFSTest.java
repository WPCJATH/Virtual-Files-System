package hk.edu.polyu.comp.comp2021.cvfs.model;

import org.junit.Before;
import org.junit.Test;

import static hk.edu.polyu.comp.comp2021.cvfs.model.CVFS.Operation.*;
import static hk.edu.polyu.comp.comp2021.cvfs.model.CVFS.checker.*;
import static hk.edu.polyu.comp.comp2021.cvfs.model.CVFS.dataPool.*;
import static org.junit.Assert.*;

/**
 *
 */
public class CVFSTest {

    /**
     *
     */
    @Test
    public void checkerTest() {
        assertEquals(numChecker("0", '1'), -1);
        assertEquals(numChecker("-1", '1'), -1);
        assertEquals(numChecker("1280", '1'), 1280);

        newDisk("2048");
        assertTrue(memoryChecker());
        assertFalse(memoryChecker(2100));

        assertFalse(nameChecker("aaaaaaaaaaa", '1'));
        assertFalse(nameChecker("abc-", '1'));
        assertTrue(nameChecker("ab12", '1'));

        assertTrue(nameChecker("ab", '2'));
        assertFalse(nameChecker("11", '2'));
        assertFalse(nameChecker("abc", '2'));

        assertNull(typeChecker("dir"));
        assertEquals(typeChecker("txt"), fileType.TXT);
        assertEquals(typeChecker("css"), fileType.CSS);
        assertEquals(typeChecker("java"), fileType.JAVA);
        assertEquals(typeChecker("html"), fileType.HTML);

        assertNull(nameChecker("txt"));
        assertEquals(nameChecker("name"), AttrType.NAME);
        assertEquals(nameChecker("type"), AttrType.TYPE);
        assertEquals(nameChecker("size"), AttrType.SIZE);

        assertTrue(opChecker("contains", AttrType.NAME, "\"abc\""));
        assertTrue(opChecker("equals", AttrType.TYPE, "\"txt\""));
        assertTrue(opChecker(">=", AttrType.SIZE, "108"));
        assertFalse(opChecker("equals", AttrType.NAME, "\"abc\""));
        assertFalse(opChecker("contains", AttrType.TYPE, "\"txt\""));
        assertFalse(opChecker("===", AttrType.SIZE, "108"));
        assertFalse(opChecker("contains", AttrType.NAME, "abc"));
        assertFalse(opChecker("equals", AttrType.TYPE, "txt"));
        assertFalse(opChecker(">=", AttrType.SIZE, "aaa"));

        assertTrue(opChecker("&&"));
        assertFalse(opChecker("|&"));
    }

    /**
     *
     */
    @Test
    public void testCVFSConstructor() {
        Disk disk = new Disk(1280);
        assertEquals(disk.getCAP(), 1280);
        setCurDisk(disk);

        File dir = new Directory("AMA1130", fileType.DIR, 40, null);
        File doc = new Document("classnote", fileType.HTML, "L'Hospital Rule", 72, dir);
        dir.addFile(doc);

        assertEquals(dir.getSize(), 112);
        assertEquals(dir.getContents().indexOf(doc), 0);
        assertEquals(doc.getParent(), dir);

        dir.deleteFile(doc);
        assertEquals(dir.getSize(), 40);

        Criterion cri1 = new Criterion("aa", AttrType.NAME, "contains", "\"i\"", false);
        Criterion cri2 = new Criterion("bb", AttrType.SIZE, ">=", "128", false);
        Criterion cr3 = new Criterion("cc", cri1, cri2, "&&", false);
        assert true;
    }


    /**
     * prepare the data
     */
    @Before
    public void prepare() {
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
        assert true;
    }


    /**
     *
     */
    @Test
    public void dataPoolTest() {

        assertNotNull(getCurDisk());

        changeDir("COMP");
        assertEquals("COMP", getCurDir().getName());

        Criterion cri = new Criterion("ll", AttrType.NAME, "contains", "\"c\"", false);
        addCri(cri);
        assertEquals(hasCri(cri.getCriName()),cri);
        assertNull(hasCri("zz"));

        changeDir("..");
        assertNotNull(hasFile("PolyUoffer"));
        assertNull(hasFile("123"));
    }

    /**
     *
     */
    @Test
    public void OperationTest(){
        list();
        changeDir("COMP");
        changeDir("COMP2021");
        list();
        assert true;

        changeDir("..");
        changeDir("..");

        rlist();
        printAllCriteria();
        assert true;


        rename("PolyUoffer","polyuoffer");
        assertNull(hasFile("PolyUoffer"));
        assertNotNull(hasFile("polyuoffer"));

        search("aa");
        search("cc");
        assert true;

        search("ee");
        search("gg");
        assert true;

        rSearch("ee");
        rSearch("gg");
        assert true;

        search("kk");
        rSearch("kk");
        assert true;

        Isdocument();
        assert true;

        delete("ideaWeb");
        assertNull(hasFile("ideaWeb"));

        changeDir("COMP");
        delete("WIE");
        assertNull(hasFile("WIE"));
        changeDir("..");

        assertEquals(getPos(hasFile("polyuoffer").getParent()),"Disk\\");
        changeDir("COMP");
        assertEquals(getPos(hasFile("ARForm").getParent()),"Disk\\COMP\\");
        changeDir("COMP2021");
        assertEquals(getPos(hasFile("midterm").getParent()),"Disk\\COMP\\COMP2021\\");
        changeDir("project");
        assertEquals(getPos(hasFile("rubrics").getParent()),"Disk\\COMP\\COMP2021\\project\\");

        assertEquals(CVFS.getCurPos(),"Disk\\COMP\\COMP2021\\project\\");
    }
}