package framework.steps;

import framework.managers.DriverManager;
import framework.managers.InitManager;
import framework.managers.TestPropManager;
import framework.utils.PropConst;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void before(){
        InitManager.initFramework();
        DriverManager.getINSTANCE().getDriver().get(TestPropManager.getINSTANCE().getProperty(PropConst.BASE_URL));
    }

    @After
    public void after(){
//        InitManager.quitFramework();
    }
}
