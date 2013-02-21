package com.github.vmorev.amazon;

import com.amazonaws.services.s3.model.Bucket;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * User: Valentin_Morev
 * Date: 19.02.13
 */
public class S3BucketTest {
    protected static Random random;
    private S3Bucket testBucket;

    @BeforeClass
    public static void setUpClass() {
        random = new Random(System.currentTimeMillis());
    }

    @Before
    public void setUp() {
        String modifier = "-" + random.nextLong();
        testBucket = new S3Bucket("test" + modifier);
        testBucket.createBucket();
    }

    @After
    public void cleanUp() {
        testBucket.deleteBucket();
    }

    @Test
    public void testListBuckets() {
        List<Bucket> buckets = new S3Bucket().listBuckets();
        assertThat(buckets.size(), greaterThan(0));

        boolean match = false;
        for (Bucket bucket : buckets)
            if (bucket.getName().equals(testBucket.getName()))
                match = true;
        assertTrue(match);
    }

    @Test
    public void testListObjects() throws Exception {
        SampleBean bean = SampleBean.getSampleBean();
        testBucket.saveObject(bean.getName(), bean);

        final List<SampleBean> objects = new ArrayList<>();
        testBucket.listObjects(SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                objects.add(obj);
            }
        });

        assertEquals(1, objects.size());
        assertEquals(bean, objects.get(0));
    }

    @Test
    public void testSaveAndGetObject() throws IOException {
        SampleBean bean = SampleBean.getSampleBean();
        testBucket.saveObject(bean.getName(), bean);
        SampleBean resBean = testBucket.getObject(bean.getName(), bean.getClass());
        assertNotNull(resBean);
        assertEquals(bean, resBean);
    }

}
