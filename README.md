Amazon common library
============

This library can be used to work with Amazon S3, SQS and SimpleDB services. 
Application built using [aws-java-sdk](http://aws.amazon.com/sdkforjava/).

###Sample of S3 usage:
```java
    S3Bucket bucket = new S3Bucket("bucketName");
    bucket.createBucket();

    SampleBean bean = SampleBean.getSampleBean();
    
    bucket.saveObject(bean.getName(), bean);

    bucket.listObjects(SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                //Do something with SampleBean object here
            }
        });

    SampleBean newBean = bucket.getObject(bean.getName(), bean.getClass());

    bucket.deleteBucket();
```

###Sample of SQS usage:
```java
    SQSQueue queue = new SQSQueue("bucketName");
    queue.createQueue();

    SampleBean bean = SampleBean.getSampleBean();
    
    queue.sendMessage(bean);

    queue.receiveMessages(1, 1, SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                //Do something with SampleBean object here
            }
        });

    queue.deleteQueue();
```

###Sample of SimpleDB usage:
```java
    SDBDomain domain = new SDBDomain("bucketName");
    domain.createDomain();

    SampleBean bean = SampleBean.getSampleBean();
    
    domain.saveObject(bean.getName(), bean);

    domain.listObjects("select * from `" + testDomain.getName() + "`", SampleBean.class, new AmazonService.ListFunc<SampleBean>() {
            public void process(SampleBean obj) throws Exception {
                //Do something with SampleBean object here
            }
        });

    SampleBean newBean = domain.getObject(bean.getName(), true, bean.getClass()); //boolean true for consistent read if necessary

    domain.deleteDomain();
```

Code is available under Apache 2.0 license.

Pre-requirements are to have:
- [Amazon AWS](http://aws.amazon.com) account

Compilation can be done with maven by calling:
```bash
maven clean install
```

You will need to do some configuration for tests to pass otherwise you could just turn off tests:
```bash
mvn clean install -Dmaven.test.skip=true
```

The configuration required is about to put your credentials into config files:

###src/main/resources/aws.json
```
{
    "accessKey" : "YOUR_KEY",
    "secretKey" : "YOUR_SECRET_KEY"
}
```
