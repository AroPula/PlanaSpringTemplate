package com.richuff.entity;

public class MyObject {
    private String name;
    private Integer age;
    private String phone;
    private String email;

    public MyObject() {
    }

    private MyObject(MyObjectBuilder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.phone = builder.phone;
        this.email = builder.email;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public static MyObject.MyObjectBuilder Builder() {
        return new MyObjectBuilder();
    }

    public static class MyObjectBuilder{
        private String name;
        private Integer age;
        private String phone;
        private String email;

        public MyObjectBuilder setName(String name){
            this.name = name;
            return this;
        }
        public MyObjectBuilder setAge(Integer age){
            this.age = age;
            return this;
        }
        public MyObjectBuilder setPhone(String phone){
            this.phone = phone;
            return this;
        }
        public MyObjectBuilder setEmail(String email){
            this.email = email;
            return this;
        }

        public MyObject build(){
            return new MyObject(this);
        }
    }
}
