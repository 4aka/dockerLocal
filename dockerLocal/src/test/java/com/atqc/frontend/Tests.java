package com.atqc.frontend;

import com.atqc.pages.Page;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Tests extends BaseTest{

    @Test
    public void test1() {
        String data = "dbewdyguydqwgdquywdgqwuyd";

        Page p = new Page();
        p.search(data);

        Assert.assertEquals(data, p.newSearchField.getAttribute("value"));
    }

    @Test
    public void test2() {
        String data = "dbewdyguydqwgdquywdgqwuyd";

        Page p = new Page();
        p.search(data);

        Assert.assertEquals(data, p.newSearchField.getAttribute("value"));
    }

}
