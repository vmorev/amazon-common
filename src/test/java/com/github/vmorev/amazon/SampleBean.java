package com.github.vmorev.amazon;

import java.util.*;

/**
 * User: Valentin_Morev
 * Date: 20.02.13
 */
public class SampleBean {
    protected static Random random = new Random(System.currentTimeMillis());
    private String name;
    private String description;
    private List<String> tags = new ArrayList<>();
    private long timestamp;
    private Map<String, String> props = new HashMap<>();

    public static SampleBean getSampleBean() {
        SampleBean bean = new SampleBean();
        bean.setTimestamp(System.currentTimeMillis());
        String modifier = "-" + random.nextLong();
        bean.setName("Test name" + modifier);
        bean.setDescription("Test description" + modifier);
        bean.getTags().add("Tag1" + modifier);
        bean.getTags().add("Tag2" + modifier);
        bean.getProps().put("Prop1" + modifier, "Value1");
        bean.getProps().put("Prop2" + modifier, "Value2");
        return bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleBean that = (SampleBean) o;

        if (timestamp != that.timestamp) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!name.equals(that.name)) return false;
        if (props != null ? !props.equals(that.props) : that.props != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;

        return true;
    }

    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (props != null ? props.hashCode() : 0);
        return result;
    }
}
