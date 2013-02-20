package com.github.vmorev.amazon;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.collection.IsIn.isIn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * User: Valentin_Morev
 * Date: 19.02.13
 */
public class SDBDomainTest {
    protected static Random random;
    private SDBDomain testDomain;

    @BeforeClass
    public static void setUpClass() {
        random = new Random(System.currentTimeMillis());
    }

    @Before
    public void setUp() throws Exception {
        String modifier = "-" + random.nextLong();
        testDomain = new SDBDomain("test" + modifier);
        testDomain.createDomain();
    }

    @After
    public void cleanUp() {
        testDomain.deleteDomain();
    }

    @Test
    public void testListDomains() throws Exception {
        final List<String> domains = new ArrayList<>();
        SDBDomain.listDomains(new AmazonService.ListFunc<String>() {
            public void process(String obj) {
                domains.add(obj);
            }
        });
        assertThat(testDomain.getName(), isIn(domains));
    }

    @Test
    public void testListObjects() throws Exception {
        SampleBean bean = SampleBean.getSampleBean();
        testDomain.saveObject(bean.getName(), bean);

        final List<SampleBean> beans = new ArrayList<>();
        testDomain.listObjects("select * from `" + testDomain.getName() + "`", SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                beans.add(obj);
            }
        });
        assertEquals(1, beans.size());
        assertEquals(bean, beans.get(0));
    }

    @Test
    public void testSendGet() throws Exception {
        SampleBean bean = SampleBean.getSampleBean();
        testDomain.saveObject(bean.getName(), bean);
        SampleBean resBean = testDomain.getObject(bean.getName(), true, bean.getClass());
        assertEquals(bean, resBean);
    }

}
