package org.drools.compiler.integrationtests;

import org.drools.compiler.CommonTestMethodBase;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;

/**
 * Testing use of default Package.
 */
public class KieDefaultPackageTest extends CommonTestMethodBase {

    @Test
    public void testAllInDefaultPackage() throws Exception {
        //This Model will be in the "default package"
        String model_drl = ""
                + "declare Smurf\n"
                + "Field1 : String\n"
                + "end\n";

        //This DRL is in the "default package"
        String drl = ""
                + "rule \"test\"\n"
                + "when\n"
                + "Smurf()\n"
                + "then\n"
                + "end";

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write( "src/main/resources/model.drl", model_drl );
        kfs.write( "src/main/resources/drl.drl", drl );
        KieBuilder builder = ks.newKieBuilder( kfs ).buildAll();

        assertEquals( 0,
                      builder.getResults().getMessages().size() );
    }

    @Test
    @Ignore("How do you access Type 'Smurf'? Test 1 - No import prefix")
    public void testModelInDefaultPackage1() throws Exception {
        //This Model will be in the "default package"
        String model_drl = ""
                + "declare Smurf\n"
                + "Field1 : String\n"
                + "end\n";

        //This DRL is in a named package, but imports the model
        String drl = ""
                + "package org.smurf\n"
                + "import Smurf\n"
                + "rule \"test\"\n"
                + "when\n"
                + "Smurf()\n"
                + "then\n"
                + "end";

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write( "src/main/resources/model.drl", model_drl );
        kfs.write( "src/main/resources/drl.drl", drl );
        KieBuilder builder = ks.newKieBuilder( kfs ).buildAll();

        assertEquals( 0,
                      builder.getResults().getMessages().size() );
    }

    @Test
    @Ignore("How do you access Type 'Smurf'? Test 2 - Attempting to use defaultPkg prefix")
    public void testModelInDefaultPackage2() throws Exception {
        //This Model will be in the "default package"
        String model_drl = ""
                + "declare Smurf\n"
                + "Field1 : String\n"
                + "end\n";

        //This DRL is in a named package, but imports the model (trying with defaultPkg prefix)
        String drl = ""
                + "package org.smurf\n"
                + "import defaultPkg.Smurf\n"
                + "rule \"test\"\n"
                + "when\n"
                + "Smurf()\n"
                + "then\n"
                + "end";

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write( "src/main/resources/model.drl", model_drl );
        kfs.write( "src/main/resources/drl.drl", drl );
        KieBuilder builder = ks.newKieBuilder( kfs ).buildAll();

        assertEquals( 0,
                      builder.getResults().getMessages().size() );
    }

    @Test
    public void testAllInExplicitPackage() throws Exception {
        //This Model will be in package "org.smurf"
        String model_drl = ""
                + "package org.smurf\n"
                + "declare Smurf\n"
                + "Field1 : String\n"
                + "end\n";

        //This DRL is in package "org.smurf" too
        String drl = ""
                + "package org.smurf\n"
                + "rule \"test\"\n"
                + "when\n"
                + "Smurf()\n"
                + "then\n"
                + "end";

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write( "src/main/resources/model.drl", model_drl );
        kfs.write( "src/main/resources/drl.drl", drl );
        KieBuilder builder = ks.newKieBuilder( kfs ).buildAll();

        assertEquals( 0,
                      builder.getResults().getMessages().size() );
    }

    @Test
    public void testAllInDifferentExplicitPackages() throws Exception {
        //This Model will be in package "org.smurf"
        String model_drl = ""
                + "package org.smurf\n"
                + "declare Smurf\n"
                + "Field1 : String\n"
                + "end\n";

        //This DRL is in package "org.smurf.subpackage"
        String drl = ""
                + "package org.smurf.subpackage\n"
                + "import org.smurf.Smurf\n"
                + "rule \"test\"\n"
                + "when\n"
                + "Smurf()\n"
                + "then\n"
                + "end";

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write( "src/main/resources/model.drl", model_drl );
        kfs.write( "src/main/resources/drl.drl", drl );
        KieBuilder builder = ks.newKieBuilder( kfs ).buildAll();

        assertEquals( 0,
                      builder.getResults().getMessages().size() );
    }

}
